import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of, throwError, timer } from 'rxjs';
import { catchError, map, retry, tap, timeout } from 'rxjs/operators';
import { environment } from '../../environments/environment';
import { UserSession } from '../model/user-session';
import { User } from '../model/user';
import { LoginRequest } from '../model/login-request';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = `${environment.apiUrl}/gateway-proxy/api/service-biometrie-partenaire/users`;

  private  loginInfos: LoginRequest | undefined;
    private readonly refreshThreshold = 5 * 60 * 1000; // 5 minutes avant expiration
  private readonly requestTimeout = 30000; // 30 secondes
  
  private readonly tokenKey = 'auth_token';
  private readonly userKey = 'current_user';

  
  // État de l'authentification
  private currentUserSubject = new BehaviorSubject<UserSession | null>(this.getUserFromStorage());
  public readonly currentUser$ = this.currentUserSubject.asObservable();
  
  // Gestion du refresh automatique
  private refreshTimer?: any;
  
  constructor(private http: HttpClient) {
    this.initializeRefreshTimer();
    // Log pour debug SSL
    console.log('AuthService initialized with API URL:', this.apiUrl);
  }

  /**
   * Getter pour l'utilisateur actuel
   */
  public get currentUserValue(): UserSession | null {
    return this.currentUserSubject.value;
  }

  /**
   * Connexion utilisateur avec gestion d'erreurs avancée et SSL
   */
// Et dans la méthode login, modifiez le tap :

login(username: string, password: string): Observable<UserSession> {
  if (!username?.trim() || !password?.trim()) {
    return throwError(() => new Error('User name et mot de passe requis'));
  }

  const loginRequest: LoginRequest = {
    userName: username,
    passWord: password
  };

  const headers = this.getSecureHeaders();

  return this.http.post<UserSession>(`${this.apiUrl}/login`, loginRequest, { 
    headers
  })
  .pipe(
    timeout(this.requestTimeout),
    retry({
      count: 2,
      delay: (error: HttpErrorResponse) => {
        console.log('Retry attempt for error:', error.status, error.message);
        
        if (error.status >= 500 || error.status === 0 || 
            error.message.includes('ERR_CERT') || 
            error.message.includes('SSL')) {
          return timer(1000);
        }
        return throwError(() => error);
      }
    }),
    catchError((error: HttpErrorResponse) => {
      return this.handleLoginError(error);
    }),
    tap(authResponse => {
      console.log('Réponse auth reçue:', authResponse);

      // Vérification avec usersDTO (avec 's')
      if (!authResponse.usersDTO) {
        console.error('Aucune session utilisateur reçue');
        throw new Error('Aucune session utilisateur reçue');
      }

      if (!this.isValidUserSession(authResponse)) {
        console.error('Session utilisateur invalide');
        throw new Error('Session utilisateur invalide');
      }

      this.handleSuccessfulLogin(authResponse);
    })
  );
}
  

  /**
   * Déconnexion sécurisée
   */
/**
 * Déconnexion complète avec nettoyage de tout le localStorage
 */
/**
 * Déconnexion complète avec gestion robuste
 */
logout(): Observable<any> {
  console.log('🔴 Début de la déconnexion...');
  
  const headers = this.getAuthHeaders();
  
  // ✅ IMPORTANT : Nettoyage local AVANT l'appel serveur
  this.clearAllAuthData();
  console.log('✅ Données locales nettoyées');
  
  // Appel serveur (fire and forget)
  return this.http.post<any>(
    `${this.apiUrl}/logout`, 
    {},
    { headers }
  ).pipe(
    timeout(5000),
    catchError(error => {
      console.warn('⚠️ Erreur serveur lors du logout (ignorée):', error);
      // Retourner un succès même en cas d'erreur
      return of({ success: true, message: 'Déconnexion locale effectuée' });
    }),
    map(response => {
      console.log('✅ Déconnexion complète');
      return { success: true, ...response };
    }),
    // Garantir qu'on ne renvoie jamais d'erreur
    catchError(() => {
      console.log('✅ Déconnexion locale effectuée (fallback)');
      return of({ success: true });
    })
  );
}

/**
 * Nettoyage complet de toutes les données d'authentification
 */
private clearAllAuthData(): void {
  try {
    console.log('🧹 Nettoyage complet du localStorage...');
    
    // 1. Supprimer les clés d'authentification spécifiques
    localStorage.removeItem(this.userKey);
    localStorage.removeItem(this.tokenKey);
    
    // 2. Réinitialiser l'état de l'utilisateur
    this.currentUserSubject.next(null);
    
    // 3. Arrêter le timer de rafraîchissement
    if (this.refreshTimer) {
      clearTimeout(this.refreshTimer);
      this.refreshTimer = undefined;
    }
    
    console.log('✅ Nettoyage complet terminé');
    
  } catch (error) {
    console.error('❌ Erreur lors du nettoyage complet:', error);
    // Forcer le nettoyage en cas d'erreur
    try {
      localStorage.clear();
      this.currentUserSubject.next(null);
    } catch (e) {
      console.error('❌ Impossible de vider le localStorage:', e);
    }
  }
}

/**
 * Nettoyage complet de toutes les données d'authentification
 */


/**
 * Méthode publique pour un nettoyage complet (utilisable depuis les composants)
 */
public forceLogout(): void {
  console.log('Déconnexion forcée...');
  this.clearAllAuthData();
  // Rediriger vers la page de login si nécessaire
  // this.router.navigate(['/login']);
}

  /**
   * Enregistrement d'utilisateur
   */
  register(user: User): Observable<User> {
    if (!this.isValidUserData(user)) {
      return throwError(() => new Error('Données utilisateur invalides'));
    }

    const headers = this.getSecureHeaders();

    return this.http.post<User>(`${this.apiUrl}/auth/register`, user, { 
      headers,
      withCredentials: false 
    }).pipe(
      timeout(this.requestTimeout),
      catchError(this.handleApiError.bind(this))
    );
  }

  /**
   * Rafraîchissement du token
   */
  refreshToken(): Observable<UserSession> {
    const currentUser = this.currentUserValue;
    
    if (!currentUser?.token) {
      return throwError(() => new Error('Aucun token à rafraîchir'));
    }

    const headers = this.getAuthHeaders();
    
    return this.http.post<UserSession>(
      `${this.apiUrl}/auth/refresh-token`, 
      {},
      { headers, withCredentials: false }
    ).pipe(
      timeout(15000),
      tap(userSession => {
        if (this.isValidUserSession(userSession)) {
          this.handleSuccessfulLogin(userSession);
        }
      }),
      catchError(error => {
        console.error('Erreur refresh token:', error);
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
    
    // Vérifier si le token n'est pas expiré
    if (expirationDate <= now) {
      this.clearAuthData();
      return false;
    }

    // Vérifier l'intégrité du token (basique)
    if (!this.isValidJwtStructure(currentUser.token)) {
      this.clearAuthData();
      return false;
    }

    return true;
  }

  /**
   * Vérification des permissions
   */
  hasPermission(permissionCode: string): boolean {
    if (!permissionCode?.trim()) {
      return false;
    }

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
   * Vérification si le token doit être rafraîchi
   */
  shouldRefreshToken(): boolean {
    const currentUser = this.currentUserValue;
    
    if (!currentUser?.expiresAt) {
      return false;
    }

    const now = new Date();
    const expirationDate = new Date(currentUser.expiresAt);
    const timeUntilExpiration = expirationDate.getTime() - now.getTime();

    return timeUntilExpiration <= this.refreshThreshold;
  }

  /**
   * MÉTHODES PRIVÉES
   */

  private handleSuccessfulLogin(userSession: UserSession): void {
    this.saveUserToStorage(userSession);
    this.currentUserSubject.next(userSession);
    this.initializeRefreshTimer();
  }

  private handleLoginError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Erreur de connexion inconnue';

    if (error.error instanceof ErrorEvent) {
      // Erreur côté client
      errorMessage = 'Erreur de connexion réseau';
    } else {
      // Gestion spécifique des erreurs SSL
      if (error.message?.includes('ERR_CERT_COMMON_NAME_INVALID')) {
        errorMessage = 'Erreur de certificat SSL. Vérifiez la configuration du serveur.';
      } else if (error.message?.includes('ERR_CERT') || error.message?.includes('SSL')) {
        errorMessage = 'Erreur de certificat SSL. Contactez votre administrateur.';
      } else {
        // Erreurs serveur standard
        switch (error.status) {
          case 400:
            errorMessage = 'Données de connexion invalides';
            break;
          case 401:
            errorMessage = 'Username ou mot de passe incorrect';
            break;
          case 403:
            errorMessage = 'Accès refusé';
            break;
          case 404:
            errorMessage = 'Service non disponible';
            break;
          case 429:
            errorMessage = 'Trop de tentatives, veuillez patienter';
            break;
          case 500:
            errorMessage = 'Erreur serveur interne';
            break;
          case 0:
            errorMessage = 'Impossible de contacter le serveur (vérifiez votre connexion)';
            break;
          default:
            errorMessage = error.error?.message || 'Erreur de connexion';
        }
      }
    }

    console.error('Erreur login détaillée:', {
      status: error.status,
      message: error.message,
      error: error.error,
      url: error.url
    });
    
    return throwError(() => ({ ...error, message: errorMessage }));
  }

  private handleApiError(error: HttpErrorResponse): Observable<never> {
    console.error('Erreur API:', error);
    
    let errorMessage = 'Erreur lors de la requête';
    
    // Gestion spécifique des erreurs SSL
    if (error.message?.includes('ERR_CERT') || error.message?.includes('SSL')) {
      errorMessage = 'Erreur de certificat SSL';
    } else if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.message) {
      errorMessage = error.message;
    }

    return throwError(() => ({ ...error, message: errorMessage }));
  }

  private saveUserToStorage(userSession: UserSession): void {
    try {
      const dataToSave = {
        ...userSession,
        expiresAt: userSession.expiresAt
      };
      
      localStorage.setItem(this.userKey, JSON.stringify(dataToSave));
      
      if (userSession.token) {
        localStorage.setItem(this.tokenKey, userSession.token);
      }
    } catch (error) {
      console.error('Erreur sauvegarde localStorage:', error);
    }
  }

  private getUserFromStorage(): UserSession | null {
    try {
      const userData = localStorage.getItem(this.userKey);
      if (!userData) {
        return null;
      }

      const user: UserSession = JSON.parse(userData);
      
      if (user.expiresAt) {
        user.expiresAt = new Date(user.expiresAt);
      }

      if (this.isValidUserSession(user)) {
        return user;
      }

      this.clearAuthData();
      return null;
      
    } catch (error) {
      console.error('Erreur lecture localStorage:', error);
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
    } catch (error) {
      console.error('Erreur nettoyage auth data:', error);
    }
  }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem(this.tokenKey);
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': token ? `Bearer ${token}` : '',
      'X-Requested-With': 'XMLHttpRequest',
      'Accept': 'application/json'
    });
  }

  /**
   * Headers sécurisés pour les requêtes (avec gestion SSL)
   */
  private getSecureHeaders(): HttpHeaders {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
      'Accept': 'application/json',
      // Headers pour améliorer la compatibilité SSL
     // 'Cache-Control': 'no-cache',
     // 'Pragma': 'no-cache'
    });
  }

  private isValidUserSession(userSession: any): boolean {
     // Vérification de base
  if (!userSession || typeof userSession !== 'object') {
    console.error('UserSession invalide: objet null ou non-objet');
    return false;
  }

  // Log pour debug
  console.log('Validation UserSession:', {
    hasToken: !!userSession.token,
    hasUsersDTO: !!userSession.usersDTO,
    hasUserDTO: !!userSession.userDTO,
    hasExpiresAt: !!userSession.expiresAt
  });

  // Vérification du token
  if (!userSession.token) {
    console.error('UserSession invalide: token manquant');
    return false;
  }

  // Vérification de usersDTO (avec 's' comme dans votre backend)
  if (!userSession.usersDTO) {
    console.error('UserSession invalide: usersDTO manquant');
    return false;
  }

  // Vérification de expiresAt
  if (!userSession.expiresAt) {
    console.error('UserSession invalide: expiresAt manquant');
    return false;
  }

  return true;
  }

  private isValidUserData(user: any): boolean {
    return user && 
           typeof user === 'object' &&
           user.email &&
           user.password;
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
          this.refreshToken().subscribe({
            next: () => console.log('Token rafraîchi automatiquement'),
            error: (error) => console.warn('Échec refresh automatique:', error)
          });
        }
      }, timeUntilRefresh);
    }
  }
  
}
