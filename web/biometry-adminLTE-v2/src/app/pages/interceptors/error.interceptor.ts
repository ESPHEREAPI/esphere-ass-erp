import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const router = inject(Router);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      let errorCode = error.status;
      let errorDetails = {
        url: req.url,
        timestamp: new Date(),
        message: error.message
      };

      // Gérer les erreurs réseau (pas de connexion)
      if (error.status === 0 || error.error instanceof ProgressEvent) {
        // Erreur de connexion réseau
        const currentUrl = router.url;
        router.navigate(['/network-error'], {
          state: { previousUrl: currentUrl }
        });
        return throwError(() => error);
      }

      // Gérer les erreurs HTTP spécifiques
      switch (errorCode) {
        case 401:
          // Non autorisé - rediriger vers login
          console.error('Unauthorized access - redirecting to login');
          router.navigate(['/login'], {
            queryParams: { returnUrl: router.url }
          });
          break;

        case 403:
          // Accès interdit
          router.navigate(['/error'], {
            queryParams: { code: 403 },
            state: { errorCode: 403, errorDetails }
          });
          break;

        case 404:
          // Ressource non trouvée
          router.navigate(['/error'], {
            queryParams: { code: 404 },
            state: { errorCode: 404, errorDetails }
          });
          break;

        case 408:
          // Timeout
          router.navigate(['/network-error'], {
            state: { previousUrl: router.url }
          });
          break;

        case 429:
          // Too many requests
          router.navigate(['/error'], {
            queryParams: { code: 429 },
            state: { errorCode: 429, errorDetails }
          });
          break;

        case 500:
        case 502:
        case 503:
        case 504:
          // Erreurs serveur
          router.navigate(['/error'], {
            queryParams: { code: errorCode },
            state: { errorCode, errorDetails }
          });
          break;

        default:
          // Autres erreurs 4xx ou 5xx
          if (errorCode >= 400) {
            router.navigate(['/error'], {
              queryParams: { code: errorCode },
              state: { errorCode, errorDetails }
            });
          }
      }

      return throwError(() => error);
    })
  );
};