import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError, timer } from 'rxjs';
import { catchError, map, tap, retry, timeout } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { UserSession } from '../model/user-session';
import { LoginRequest } from '../model/login-request';
import { AuthResponse } from '../model/auth-response';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  
  // Configuration
  private readonly apiUrl = `${environment.apiUrl}/gateway-proxy/api/esphere-ass-microservice-admin/auth`;
  private readonly tokenKey = 'auth_token';
  private readonly userKey = 'current_user';
  private readonly refreshThreshold = 5 * 60 * 1000; // 5 minutes
  private readonly requestTimeout = 30000; // 30 secondes
  
  // État de l'authentification
  private currentUserSubject = new BehaviorSubject<UserSession | null>(this.getUserFromStorage());
  public readonly currentUser$ = this.currentUserSubject.asObservable();
  
  // Gestion du refresh automatique
  private refreshTimer?: any;
  
  constructor(private http: HttpClient) {
    this.initializeRefreshTimer();
  }

  /**
   * Getter pour l'utilisateur actuel
   */
  public get currentUserValue(): UserSession | null {
    return this.currentUserSubject.value;
  }

  /**
   * Connexion utilisateur - VERSION ROBUSTE
   */
  login(username: string, password: string): Observable<AuthResponse> {
    // Validation des entrées
    if (!username?.trim() || !password?.trim()) {
      return throwError(() => ({
        status: 400,
        error: { message: 'Username et mot de passe requis' }
      }));
    }

    const loginRequest: LoginRequest = {
      username: username.trim().toUpperCase(), // Oracle usernames en majuscules
      password: password
    };

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest'
    });

    console.log('🔐 Tentative de connexion:', loginRequest.username);

    return this.http.post<AuthResponse>(
      `${this.apiUrl}/login`, 
      loginRequest, 
      { headers }
    ).pipe(
      timeout(this.requestTimeout),
      retry({
        count: 2,
        delay: (error: HttpErrorResponse) => {
          // Retry uniquement pour erreurs serveur/réseau
          if (error.status >= 500 || error.status === 0) {
            console.warn('⚠️ Retry connexion après erreur:', error.status);
            return timer(1000);
          }
          return throwError(() => error);
        }
      }),
      map((response: AuthResponse) => {
        console.log('📦 Réponse brute du serveur:', response);
        
        // Vérifier la structure de la réponse
        if (!response || typeof response !== 'object') {
          throw {
            status: 500,
            error: { message: 'Réponse serveur invalide' }
          };
        }

        return response;
      }),
      tap((response: AuthResponse) => {
        // Traiter la réponse selon le succès
        if (response.success && response.data) {
          console.log('✅ Authentification réussie');
          
          // Normaliser la date d'expiration
          if (response.data.expiresAt && typeof response.data.expiresAt === 'string') {
            response.data.expiresAt = new Date(response.data.expiresAt);
          }
          
          // Valider la session
          if (!this.isValidUserSession(response.data)) {
            throw {
              status: 500,
              error: { message: 'Session utilisateur invalide' }
            };
          }
          
          // Sauvegarder la session
          this.handleSuccessfulLogin(response.data);
        } else {
          console.warn('❌ Authentification échouée:', response.message);
          
          // Convertir en erreur HTTP pour traitement uniforme
          throw {
            status: 401,
            error: { 
              message: response.message || 'Échec de l\'authentification',
              errorCode: response.errorCode
            }
          };
        }
      }),
      catchError((error: any) => this.handleLoginError(error))
    );
  }

  /**
   * Déconnexion sécurisée
   */
  logout(): Observable<any> {
    const headers = this.getAuthHeaders();
    
    return this.http.post<any>(
      `${this.apiUrl}/logout`, 
      {},
      { headers }
    ).pipe(
      timeout(10000),
      tap(() => {
        console.log('👋 Déconnexion réussie');
        this.clearAuthData();
      }),
      catchError(error => {
        console.warn('⚠️ Erreur déconnexion serveur:', error);
        // Toujours déconnecter localement
        this.clearAuthData();
        return of(null);
      })
    );
  }

  /**
   * Rafraîchissement du token
   */
  refreshToken(): Observable<AuthResponse> {
    const currentUser = this.currentUserValue;
    
    if (!currentUser?.token) {
      return throwError(() => new Error('Aucun token à rafraîchir'));
    }

    const headers = this.getAuthHeaders();
    
    return this.http.post<AuthResponse>(
      `${this.apiUrl}/refresh`, 
      { token: currentUser.token },
      { headers }
    ).pipe(
      timeout(15000),
      tap((response: AuthResponse) => {
        if (response.success && response.data && this.isValidUserSession(response.data)) {
          console.log('🔄 Token rafraîchi');
          this.handleSuccessfulLogin(response.data);
        }
      }),
      catchError(error => {
        console.error('❌ Erreur refresh token:', error);
        this.clearAuthData();
        return throwError(() => error);
      })
    );
  }

  /**
   * Vérification de l'état de connexion
   */
  isLoggedIn(): boolean {
    const currentUser = this.currentUserValue;
    
    if (!currentUser?.token || !currentUser.expiresAt) {
      return false;
    }

    const now = new Date();
    const expirationDate = new Date(currentUser.expiresAt);
    
    // Vérifier expiration
    if (expirationDate <= now) {
      console.log('⏰ Token expiré');
      this.clearAuthData();
      return false;
    }

    // Vérifier structure JWT
    if (!this.isValidJwtStructure(currentUser.token)) {
      console.warn('⚠️ Structure JWT invalide');
      this.clearAuthData();
      return false;
    }

    return true;
  }

  /**
   * Vérification des permissions
   */
  hasPermission(permissionCode: string): boolean {
    if (!permissionCode?.trim()) return false;
    
    const currentUser = this.currentUserValue;
    if (!currentUser?.permissions || !Array.isArray(currentUser.permissions)) {
      return false;
    }
    
    return currentUser.permissions.includes(permissionCode.trim());
  }

  hasAnyPermission(permissionCodes: string[]): boolean {
    if (!Array.isArray(permissionCodes) || permissionCodes.length === 0) {
      return false;
    }
    return permissionCodes.some(code => this.hasPermission(code));
  }

  hasAllPermissions(permissionCodes: string[]): boolean {
    if (!Array.isArray(permissionCodes) || permissionCodes.length === 0) {
      return false;
    }
    return permissionCodes.every(code => this.hasPermission(code));
  }

  /**
   * MÉTHODES PRIVÉES
   */

  private handleSuccessfulLogin(userSession: UserSession): void {
    this.saveUserToStorage(userSession);
    this.currentUserSubject.next(userSession);
    this.initializeRefreshTimer();
  }

  private handleLoginError(error: any): Observable<never> {
    console.error('❌ Erreur login détaillée:', error);
    
    let errorMessage = 'Erreur de connexion inconnue';
    let errorCode: string | undefined;

    if (error.error) {
      errorMessage = error.error.message || errorMessage;
      errorCode = error.error.errorCode;
    } else if (error.message) {
      errorMessage = error.message;
    }

    // Messages spécifiques selon le statut
    switch (error.status) {
      case 0:
        errorMessage = 'Impossible de contacter le serveur. Vérifiez votre connexion.';
        break;
      case 400:
        errorMessage = error.error?.message || 'Données de connexion invalides';
        break;
      case 401:
        errorMessage = error.error?.message || 'Identifiants incorrects';
        break;
      case 403:
        errorMessage = 'Accès refusé. Contactez votre administrateur.';
        break;
      case 404:
        errorMessage = 'Service d\'authentification non disponible';
        break;
      case 429:
        errorMessage = 'Trop de tentatives. Veuillez patienter.';
        break;
      case 500:
      case 502:
      case 503:
        errorMessage = 'Erreur serveur. Veuillez réessayer plus tard.';
        break;
    }

    return throwError(() => ({
      status: error.status || 500,
      error: { 
        message: errorMessage,
        errorCode: errorCode
      }
    }));
  }

  private saveUserToStorage(userSession: UserSession): void {
    try {
      const dataToSave = {
        ...userSession,
        expiresAt: userSession.expiresAt instanceof Date 
          ? userSession.expiresAt.toISOString() 
          : userSession.expiresAt
      };
      
      localStorage.setItem(this.userKey, JSON.stringify(dataToSave));
      
      if (userSession.token) {
        localStorage.setItem(this.tokenKey, userSession.token);
      }
      
      console.log('💾 Session sauvegardée');
    } catch (error) {
      console.error('❌ Erreur sauvegarde localStorage:', error);
    }
  }

  private getUserFromStorage(): UserSession | null {
    try {
      const userData = localStorage.getItem(this.userKey);
      if (!userData) {
        return null;
      }

      const user: UserSession = JSON.parse(userData);
      
      // Reconstituer la date
      if (user.expiresAt) {
        user.expiresAt = new Date(user.expiresAt);
      }

      // Valider
      if (this.isValidUserSession(user)) {
        console.log('📂 Session chargée depuis le stockage');
        return user;
      }

      this.clearAuthData();
      return null;
      
    } catch (error) {
      console.error('❌ Erreur lecture localStorage:', error);
      this.clearAuthData();
      return null;
    }
  }

  public clearAuthData(): void {
    try {
      localStorage.removeItem(this.userKey);
      localStorage.removeItem(this.tokenKey);
      this.currentUserSubject.next(null);
      
      if (this.refreshTimer) {
        clearTimeout(this.refreshTimer);
        this.refreshTimer = undefined;
      }
      
      console.log('🗑️ Données d\'authentification effacées');
    } catch (error) {
      console.error('❌ Erreur nettoyage auth data:', error);
    }
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem(this.tokenKey);
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : '',
      'X-Requested-With': 'XMLHttpRequest'
    });
  }

  private isValidUserSession(userSession: any): boolean {
    const isValid = userSession && 
           typeof userSession === 'object' &&
           userSession.token &&
           typeof userSession.token === 'string' &&
           userSession.userDTO &&
           typeof userSession.userDTO === 'object' &&
           userSession.expiresAt;
    
    if (!isValid) {
      console.warn('⚠️ Session utilisateur invalide:', userSession);
    }
    
    return isValid;
  }

  private isValidJwtStructure(token: string): boolean {
    if (!token) return false;
    const parts = token.split('.');
    return parts.length === 3;
  }

  private initializeRefreshTimer(): void {
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
    }

    const currentUser = this.currentUserValue;
    if (!currentUser?.expiresAt) {
      return;
    }

    const now = new Date();
    const expirationDate = new Date(currentUser.expiresAt);
    const timeUntilRefresh = expirationDate.getTime() - now.getTime() - this.refreshThreshold;

    if (timeUntilRefresh > 0) {
      this.refreshTimer = setTimeout(() => {
        if (this.isLoggedIn()) {
          console.log('🔄 Rafraîchissement automatique du token...');
          this.refreshToken().subscribe({
            next: () => console.log('✅ Token rafraîchi automatiquement'),
            error: (error) => console.warn('⚠️ Échec refresh automatique:', error)
          });
        }
      }, timeUntilRefresh);
    }
  }
}