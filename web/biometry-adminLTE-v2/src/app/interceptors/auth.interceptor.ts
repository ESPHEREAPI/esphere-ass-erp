// src/app/interceptors/auth.interceptor.ts

import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';

import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../auth/auth.service';

/**
 * 🔐 Intercepteur d'authentification
 * 
 * Rôles :
 * 1. Ajouter automatiquement le token Bearer à toutes les requêtes
 * 2. Gérer le refresh automatique du token si expiré
 * 3. Exclure certaines routes publiques
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  
  // Routes publiques qui n'ont pas besoin de token
  const publicRoutes = [
    '/auth/login',
    '/auth/register',
    '/auth/forgot-password',
    '/auth/reset-password',
    '/auth/verify-email'
  ];
  
  // Vérifier si la requête est vers une route publique
  const isPublicRoute = publicRoutes.some(route => req.url.includes(route));
  
  // Si route publique, ne pas ajouter de token
  if (isPublicRoute) {
    return next(req);
  }
  
  // Récupérer le token
  const token = authService.getToken();
  
  // Si pas de token, laisser passer (l'API retournera 401)
  if (!token) {
    return next(req);
  }
  
  // Cloner la requête et ajouter le header Authorization
  const clonedRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });
  
  // Envoyer la requête avec le token
  return next(clonedRequest).pipe(
    catchError((error: HttpErrorResponse) => {
      // Si erreur 401 (Unauthorized) et qu'on a un refresh token
      if (error.status === 401 && !req.url.includes('/auth/refresh')) {
        console.log('🔄 Token expired, attempting refresh...');
        
        // Tenter de rafraîchir le token
        return authService.refreshToken().pipe(
          switchMap(() => {
            // Token rafraîchi avec succès, réessayer la requête
            const newToken = authService.getToken();
            const retryRequest = req.clone({
              setHeaders: {
                Authorization: `Bearer ${newToken}`
              }
            });
            
            console.log('✅ Token refreshed, retrying request');
            return next(retryRequest);
          }),
          catchError(refreshError => {
            // Échec du refresh, déconnecter l'utilisateur
            console.error('❌ Token refresh failed, logging out');
            authService.logout().subscribe();
            return throwError(() => refreshError);
          })
        );
      }
      
      // Pour les autres erreurs, les laisser passer
      return throwError(() => error);
    })
  );
};