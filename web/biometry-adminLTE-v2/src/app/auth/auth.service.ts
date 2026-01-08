import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError, tap, finalize } from 'rxjs/operators';
//import { environment } from '../../environments/environment';
import { UserSession } from '../models/user-session';
import { ApiResponse } from '../models/api-response';
import { LoginRequest } from '../models/login-request';
import { User } from '../models/user';
import { Router } from '@angular/router';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  // Configuration API avec Gateway
  private readonly API_URL = environment.apiUrl; // https://localhost:8080/gateway-proxy/api/service-biometrie
  private readonly LOGIN_ENDPOINT = '/auth/users/login';
  private readonly LOGOUT_ENDPOINT = '/auth/users/logout';
  private readonly REFRESH_TOKEN_ENDPOINT = '/auth/users/refresh';
  private readonly VERIFY_TOKEN_ENDPOINT = '/auth/users/verify';
  
  // Storage keys
  private readonly CURRENT_USER_KEY = 'currentUser';
  private readonly AUTH_TOKEN_KEY = 'auth_token';
  private readonly REFRESH_TOKEN_KEY = 'refresh_token';
  private readonly TOKEN_EXPIRY_KEY = 'token_expiry';
  
  // Subjects pour l'état de l'utilisateur
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser$: Observable<User | null>;
  
  // État de chargement
  private loadingSubject = new BehaviorSubject<boolean>(false);
  public loading$ = this.loadingSubject.asObservable();

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const storedUser = this.getStoredUser();
    this.currentUserSubject = new BehaviorSubject<User | null>(storedUser);
    this.currentUser$ = this.currentUserSubject.asObservable();
    
    if (storedUser) {
      this.verifyTokenValidity();
    }
    
    this.logConfiguration();
  }

  /**
   * 📋 Logger la configuration au démarrage
   */
  private logConfiguration(): void {
    if (environment.enableDebugLogs) {
      console.log('🔧 AuthService Configuration:');
      console.log('  API URL:', this.API_URL);
      console.log('  HTTPS Enabled:', environment.enableHttps);
      console.log('  Strict SSL:', environment.strictSSL);
      console.log('  Timeout:', environment.httpTimeout + 'ms');
      console.log('  Environment:', environment.production ? 'Production' : 'Development');
    }
  }

  get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  isLoggedIn(): boolean {
    const user = this.currentUserValue;
    const token = this.getToken();
    
    if (!user || !token) {
      return false;
    }
    
    return !this.isTokenExpired();
  }

  /**
   * 🔑 Connexion utilisateur (adapté pour Gateway)
   */
  login(credentials: LoginRequest): Observable<ApiResponse<UserSession>> {
    this.loadingSubject.next(true);
    
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'Accept': 'application/json',
      'X-Gateway-Token': environment.token_key // Token Gateway
    });

    // Adapter les credentials au format backend
    const loginData = {
      username: credentials.username,
      password: credentials.password
    };

    const fullUrl = `${this.API_URL}${this.LOGIN_ENDPOINT}`;
    
    if (environment.enableDebugLogs) {
      console.log('🔐 Login attempt to:', fullUrl);
      console.log('📦 Payload:', { username: loginData.username, password: '***' });
    }

    return this.http.post<ApiResponse<UserSession>>(
      fullUrl,
      loginData,
      { headers }
    ).pipe(
      tap(response => {
        if (environment.enableDebugLogs) {
          console.log('✅ Login response received:', { 
            success: response.success, 
            message: response.message 
          });
        }
        
        if (response && response.success && response.data) {
          const userData = response.data;
          
          if (userData.userDTO) {
            this.storeUserData(
              userData.userDTO, 
              userData.token, 
              userData.refreshToken, 
              userData.expiresAt
            );
            this.currentUserSubject.next(userData.userDTO);
            
            if (credentials.rememberMe) {
              this.enableRememberMe();
            }
            
            console.log('✅ Authentication successful for:', userData.userDTO.username);
          }
        } else {
          console.warn('⚠️ Login failed:', response.message);
          throw {
            type: 'ApplicationError',
            message: response.message || 'Échec de la connexion',
            serverMessage: response.message,
            success: false,
            context: 'Login'
          };
        }
      }),
      catchError(error => this.handleError(error, 'Login')),
      finalize(() => this.loadingSubject.next(false))
    );
  }

  logout(): Observable<any> {
    this.loadingSubject.next(true);
    
    const token = this.getToken();
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json',
      'X-Gateway-Token': environment.token_key
    });

    return this.http.post(
      `${this.API_URL}${this.LOGOUT_ENDPOINT}`,
      {},
      { headers }
    ).pipe(
      tap(() => console.log('✅ Logout successful')),
      catchError(error => {
        console.warn('⚠️ Logout API error:', error.message);
        return throwError(() => error);
      }),
      finalize(() => {
        this.clearUserData();
        this.currentUserSubject.next(null);
        this.loadingSubject.next(false);
        this.router.navigate(['/login']);
      })
    );
  }

  refreshToken(): Observable<any> {
    const refreshToken = localStorage.getItem(this.REFRESH_TOKEN_KEY);
    
    if (!refreshToken) {
      return throwError(() => new Error('No refresh token available'));
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Gateway-Token': environment.token_key
    });

    return this.http.post<any>(
      `${this.API_URL}${this.REFRESH_TOKEN_ENDPOINT}`,
      { refreshToken },
      { headers }
    ).pipe(
      tap(response => {
        console.log('✅ Token refreshed');
        
        if (response.token) {
          localStorage.setItem(this.AUTH_TOKEN_KEY, response.token);
          
          if (response.refreshToken) {
            localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
          }
          
          if (response.expiresAt) {
            this.setTokenExpiry(response.expiresAt);
          }
        }
      }),
      catchError(error => {
        console.error('❌ Token refresh failed');
        this.logout();
        return this.handleError(error, 'Token Refresh');
      })
    );
  }

  private verifyTokenValidity(): void {
    const token = this.getToken();
    
    if (!token) {
      this.logout();
      return;
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'X-Gateway-Token': environment.token_key
    });

    this.http.get(`${this.API_URL}${this.VERIFY_TOKEN_ENDPOINT}`, { headers })
      .pipe(
        catchError(error => {
          console.warn('⚠️ Token verification failed');
          this.logout();
          return throwError(() => error);
        })
      )
      .subscribe({
        next: () => console.log('✅ Token is valid'),
        error: () => console.error('❌ Token is invalid')
      });
  }

  private storeUserData(
    user: User, 
    token?: string, 
    refreshToken?: string, 
    expiresAt?: Date
  ): void {
    localStorage.setItem(this.CURRENT_USER_KEY, JSON.stringify(user));
    
    if (token) {
      localStorage.setItem(this.AUTH_TOKEN_KEY, token);
    }
    
    if (refreshToken) {
      localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
    }
    
    if (expiresAt) {
      this.setTokenExpiry(expiresAt);
    }
  }

  private clearUserData(): void {
    localStorage.removeItem(this.CURRENT_USER_KEY);
    localStorage.removeItem(this.AUTH_TOKEN_KEY);
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
    localStorage.removeItem(this.TOKEN_EXPIRY_KEY);
    localStorage.removeItem('remember_me');
  }

  public  getStoredUser(): User | null {
    const userJson = localStorage.getItem(this.CURRENT_USER_KEY);
    
    if (!userJson) {
      return null;
    }
    
    try {
      return JSON.parse(userJson).userDTO;
    } catch (error) {
      console.error('❌ Error parsing stored user');
      this.clearUserData();
      return null;
    }
  }

  getToken(): string | null {
    return localStorage.getItem(this.AUTH_TOKEN_KEY);
  }

  private setTokenExpiry(expiresAt: Date): void {
    if (expiresAt) {
      const expiryTime = new Date(expiresAt).getTime();
      localStorage.setItem(this.TOKEN_EXPIRY_KEY, expiryTime.toString());
    }
  }

  isTokenExpired(): boolean {
    const expiryTime = localStorage.getItem(this.TOKEN_EXPIRY_KEY);
    
    if (!expiryTime) {
      return true;
    }
    
    return Date.now() > parseInt(expiryTime, 10);
  }

  private enableRememberMe(): void {
    localStorage.setItem('remember_me', 'true');
  }

  getUserRole(): string | null {
    const user = this.currentUserValue;
    return user?.role?.name || null;
  }

  hasRole(role: string): boolean {
    const userRole = this.getUserRole();
    return userRole === role;
  }

  hasAnyRole(roles: string[]): boolean {
    const userRole = this.getUserRole();
    return userRole ? roles.includes(userRole) : false;
  }

  /**
   * 🚨 Gestion centralisée des erreurs HTTPS/Gateway
   */
  private handleError(error: any, context: string): Observable<never> {
    if (environment.enableDebugLogs) {
      console.log('🔍 === ERROR ANALYSIS START ===');
      console.log('Raw error:', error);
      console.log('Error type:', typeof error);
      console.log('Is HttpErrorResponse:', error instanceof HttpErrorResponse);
      console.log('Error properties:', Object.keys(error || {}));
      console.log('=== ERROR ANALYSIS END ===');
    }
    
    let errorDetails: any = {
      type: 'UnknownError',
      context,
      timestamp: new Date().toISOString(),
      message: '',
      status: null,
      statusText: '',
      url: ''
    };

    // Cas 1 : HttpErrorResponse standard
    if (error instanceof HttpErrorResponse) {
      errorDetails.type = 'HttpError';
      errorDetails.status = error.status || 0;
      errorDetails.statusText = error.statusText || 'Unknown';
      errorDetails.url = error.url || 'Unknown';
      
      console.log('✅ HttpErrorResponse detected - Status:', errorDetails.status);
      this.handleHttpError(error, errorDetails, context);
    }
    // Cas 2 : Objet avec propriété error (erreur enveloppée)
    else if (error && error.error instanceof HttpErrorResponse) {
      console.log('📦 Wrapped HttpErrorResponse detected');
      return this.handleError(error.error, context);
    }
    // Cas 3 : Erreur réseau (ErrorEvent)
    else if (error && error.error instanceof ErrorEvent) {
      errorDetails.type = 'NetworkError';
      errorDetails.message = error.error.message;
      errorDetails.status = 0;
      
      console.error('❌ Network error:', errorDetails.message);
      
      this.router.navigate(['/network-error'], {
        state: { 
          previousUrl: this.router.url,
          error: errorDetails
        }
      });
    }
    // Cas 4 : Erreur applicative (success: false du backend)
    else if (error && typeof error === 'object' && error.success === false) {
      errorDetails.type = 'ApplicationError';
      errorDetails.message = error.message || error.serverMessage || 'Erreur applicative';
      errorDetails.serverMessage = error.serverMessage;
      
      console.error('❌ Application error:', errorDetails.message);
    }
    // Cas 5 : Error JavaScript standard
    else if (error instanceof Error) {
      errorDetails.type = 'JavaScriptError';
      errorDetails.message = error.message || 'Erreur JavaScript';
      
      console.error('❌ JavaScript error:', error.message);
      console.error('Stack:', error.stack);
    }
    // Cas 6 : Objet d'erreur avec status défini
    else if (error && typeof error === 'object' && error.status) {
      errorDetails.type = 'HttpError';
      errorDetails.status = error.status;
      errorDetails.statusText = error.statusText || '';
      errorDetails.url = error.url || '';
      errorDetails.message = this.extractErrorMessage(error);
      
      console.log('📊 Error object with status:', errorDetails.status);
      this.handleHttpError(error, errorDetails, context);
    }
    // Cas 7 : String (message simple)
    else if (typeof error === 'string') {
      errorDetails.type = 'StringError';
      errorDetails.message = error;
      
      console.error('❌ String error:', error);
    }
    // Cas 8 : Erreur complètement inconnue
    else {
      errorDetails.message = 'Une erreur inconnue est survenue';
      
      console.error('❌ Unknown error type');
      console.error('Error value:', error);
      
      // Tentative d'extraction d'informations
      if (error && typeof error === 'object') {
        try {
          errorDetails.rawError = JSON.stringify(error, null, 2);
        } catch (e) {
          errorDetails.rawError = String(error);
        }
      }
    }

    if (environment.enableErrorLogs) {
      console.error(`❌ ${context} final error:`, errorDetails);
    }

    return throwError(() => ({
      ...errorDetails,
      originalError: error
    }));
  }

  /**
   * 🔴 Gérer les erreurs HTTP spécifiques

  private handleHttpError(error: HttpErrorResponse, errorDetails: any, context: string): void {
    const status = error.status;
    
    switch (status) {
      case 0:
        errorDetails.message = 'Impossible de contacter le serveur. Vérifiez votre connexion ou le certificat SSL.';
        console.error('❌ No response from server (Gateway/SSL issue)');
        this.router.navigate(['/network-error'], {
          state: { previousUrl: this.router.url, error: errorDetails }
        });
        break;

      case 400:
        errorDetails.message = 'Requête invalide';
        errorDetails.serverMessage = this.extractErrorMessage(error);
        console.error('❌ Bad Request:', errorDetails.serverMessage);
        break;

      case 401:
        errorDetails.message = 'Session expirée';
        console.error('❌ Unauthorized - Session expired');
        this.clearUserData();
        this.currentUserSubject.next(null);
        this.router.navigate(['/login'], {
          queryParams: { sessionExpired: 'true' }
        });
        break;

      case 403:
        errorDetails.message = 'Accès refusé';
        console.error('❌ Forbidden');
        this.router.navigate(['/error'], {
          queryParams: { code: 403 },
          state: { errorCode: 403, errorDetails }
        });
        break;

      case 404:
        errorDetails.message = 'Service non trouvé. Vérifiez l\'URL de la Gateway.';
        console.error('❌ Not Found - Check Gateway URL');
        break;

      case 408:
        errorDetails.message = 'Délai d\'attente dépassé';
        console.error('❌ Timeout');
        this.router.navigate(['/network-error'], {
          state: { previousUrl: this.router.url, error: errorDetails }
        });
        break;

      case 502:
        errorDetails.message = 'Gateway indisponible';
        console.error('❌ Bad Gateway');
        this.router.navigate(['/error'], {
          queryParams: { code: 502 },
          state: { errorCode: 502, errorDetails }
        });
        break;

      case 503:
        errorDetails.message = 'Service temporairement indisponible';
        console.error('❌ Service Unavailable');
        this.router.navigate(['/error'], {
          queryParams: { code: 503 },
          state: { errorCode: 503, errorDetails }
        });
        break;

      case 504:
        errorDetails.message = 'Gateway timeout';
        console.error('❌ Gateway Timeout');
        this.router.navigate(['/error'], {
          queryParams: { code: 504 },
          state: { errorCode: 504, errorDetails }
        });
        break;

      default:
        errorDetails.message = `Erreur ${status}`;
        console.error(`❌ HTTP Error ${status}`);
        if (status >= 400) {
          this.router.navigate(['/error'], {
            queryParams: { code: status },
            state: { errorCode: status, errorDetails }
          });
        }
    }
  }
    */

  /**
 * 🔴 Gérer les erreurs HTTP spécifiques
 */
private handleHttpError(error: HttpErrorResponse, errorDetails: any, context: string): void {
  const status = error.status;
  
  // ✅ Ne pas rediriger automatiquement pendant le login
  const isLoginContext = context === 'Login';
  
  switch (status) {
    case 0:
      errorDetails.message = 'Impossible de contacter le serveur. Vérifiez votre connexion ou le certificat SSL.';
      console.error('❌ No response from server (Gateway/SSL issue)');
      
      // ✅ NE PAS rediriger si on est en train de se connecter
      if (!isLoginContext) {
        this.router.navigate(['/network-error'], {
          state: { previousUrl: this.router.url, error: errorDetails }
        });
      }
      break;

    case 400:
      errorDetails.message = 'Requête invalide';
      errorDetails.serverMessage = this.extractErrorMessage(error);
      console.error('❌ Bad Request:', errorDetails.serverMessage);
      break;

    case 401:
      // ✅ Gérer différemment selon le contexte
      if (isLoginContext) {
        errorDetails.message = 'Identifiants incorrects';
        console.error('❌ Login failed - Invalid credentials');
      } else {
        errorDetails.message = 'Session expirée';
        console.error('❌ Unauthorized - Session expired');
        this.clearUserData();
        this.currentUserSubject.next(null);
        this.router.navigate(['/login'], {
          queryParams: { sessionExpired: 'true' }
        });
      }
      break;

    case 403:
      errorDetails.message = 'Accès refusé';
      console.error('❌ Forbidden');
      if (!isLoginContext) {
        this.router.navigate(['/error'], {
          queryParams: { code: 403 },
          state: { errorCode: 403, errorDetails }
        });
      }
      break;

    case 404:
      errorDetails.message = 'Service non trouvé. Vérifiez l\'URL de la Gateway.';
      console.error('❌ Not Found - Check Gateway URL');
      break;

    case 408:
      errorDetails.message = 'Délai d\'attente dépassé';
      console.error('❌ Timeout');
      if (!isLoginContext) {
        this.router.navigate(['/network-error'], {
          state: { previousUrl: this.router.url, error: errorDetails }
        });
      }
      break;

    case 502:
      errorDetails.message = 'Gateway indisponible';
      console.error('❌ Bad Gateway');
      if (!isLoginContext) {
        this.router.navigate(['/error'], {
          queryParams: { code: 502 },
          state: { errorCode: 502, errorDetails }
        });
      }
      break;

    case 503:
      errorDetails.message = 'Service temporairement indisponible';
      console.error('❌ Service Unavailable');
      if (!isLoginContext) {
        this.router.navigate(['/error'], {
          queryParams: { code: 503 },
          state: { errorCode: 503, errorDetails }
        });
      }
      break;

    case 504:
      errorDetails.message = 'Gateway timeout';
      console.error('❌ Gateway Timeout');
      if (!isLoginContext) {
        this.router.navigate(['/error'], {
          queryParams: { code: 504 },
          state: { errorCode: 504, errorDetails }
        });
      }
      break;

    default:
      errorDetails.message = `Erreur ${status}`;
      console.error(`❌ HTTP Error ${status}`);
      if (status >= 400 && !isLoginContext) {
        this.router.navigate(['/error'], {
          queryParams: { code: status },
          state: { errorCode: status, errorDetails }
        });
      }
  }
}

  private extractErrorMessage(error: HttpErrorResponse): string {
    if (error.error?.message) return error.error.message;
    if (typeof error.error === 'string') return error.error;
    if (error.error?.error) return error.error.error;
    if (error.message) return error.message;
    if (error.statusText && error.statusText !== 'Unknown Error') return error.statusText;
    
    return 'Une erreur est survenue';
  }
}