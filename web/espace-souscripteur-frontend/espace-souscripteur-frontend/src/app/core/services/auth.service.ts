import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { map, tap, catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from '@environments/environment';
import {
  User,
  LoginCredentials,
  AuthResponse,
  ChangePassword,
  ResetPassword
} from '@core/models/auth.model';

/**
 * Service de gestion de l'authentification
 */
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private currentUserSubject: BehaviorSubject<User | null>;
  public currentUser: Observable<User | null>;
  private apiUrl = `${environment.apiUrl}/auth`;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {
    const storedUser = localStorage.getItem(environment.userKey);
    this.currentUserSubject = new BehaviorSubject<User | null>(
      storedUser ? JSON.parse(storedUser) : null
    );
    this.currentUser = this.currentUserSubject.asObservable();
  }

  /**
   * Récupère l'utilisateur actuellement connecté
   */
  public get currentUserValue(): User | null {
    return this.currentUserSubject.value;
  }

  /**
   * Authentifie un utilisateur
   */
  login(credentials: LoginCredentials): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials)
      .pipe(
        tap(response => {
          // Stockage du token et des informations utilisateur
          if (response.token && response.user) {
            localStorage.setItem(environment.tokenKey, response.token);
            if (response.refreshToken) {
              localStorage.setItem(environment.refreshTokenKey, response.refreshToken);
            }
            localStorage.setItem(environment.userKey, JSON.stringify(response.user));
            this.currentUserSubject.next(response.user);
          }
        })
      );
  }

  /**
   * Déconnecte l'utilisateur
   */
  logout(): void {
    // Nettoyage du stockage local
    localStorage.removeItem(environment.tokenKey);
    localStorage.removeItem(environment.refreshTokenKey);
    localStorage.removeItem(environment.userKey);
    this.currentUserSubject.next(null);
    this.router.navigate(['/login']);
  }

  /**
   * Rafraîchit le token d'accès
   */
  refreshToken(): Observable<AuthResponse> {
    const refreshToken = localStorage.getItem(environment.refreshTokenKey);
    return this.http.post<AuthResponse>(`${this.apiUrl}/refresh-token`, { refreshToken })
      .pipe(
        tap(response => {
          if (response.token) {
            localStorage.setItem(environment.tokenKey, response.token);
            if (response.refreshToken) {
              localStorage.setItem(environment.refreshTokenKey, response.refreshToken);
            }
          }
        })
      );
  }

  /**
   * Vérifie si l'utilisateur est authentifié
   */
  isAuthenticated(): boolean {
    const token = localStorage.getItem(environment.tokenKey);
    return !!token && !this.isTokenExpired(token);
  }

  /**
   * Vérifie si le token est expiré
   */
  private isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const expirationDate = new Date(payload.exp * 1000);
      return expirationDate < new Date();
    } catch (error) {
      return true;
    }
  }

  /**
   * Récupère le token d'accès
   */
  getToken(): string | null {
    return localStorage.getItem(environment.tokenKey);
  }

  /**
   * Change le mot de passe de l'utilisateur
   */
  changePassword(data: ChangePassword): Observable<any> {
    return this.http.post(`${this.apiUrl}/change-password`, data);
  }

  /**
   * Demande de réinitialisation de mot de passe
   */
  requestPasswordReset(email: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email });
  }

  /**
   * Réinitialise le mot de passe avec le token
   */
  resetPassword(data: ResetPassword): Observable<any> {
    return this.http.post(`${this.apiUrl}/reset-password`, data);
  }

  /**
   * Vérifie si l'utilisateur a une permission spécifique
   */
  hasPermission(permission: string): boolean {
    const user = this.currentUserValue;
    return user ? user.permissions.includes(permission) : false;
  }

  /**
   * Vérifie si l'utilisateur a un rôle spécifique
   */
  hasRole(role: string): boolean {
    const user = this.currentUserValue;
    return user ? user.role === role : false;
  }
}
