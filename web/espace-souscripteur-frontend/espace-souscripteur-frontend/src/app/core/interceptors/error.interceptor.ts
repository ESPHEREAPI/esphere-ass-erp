import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { TranslateService } from '@ngx-translate/core';

/**
 * Intercepteur HTTP pour gérer les erreurs
 */
@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(private translate: TranslateService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {
        let errorMessage = '';

        if (error.error instanceof ErrorEvent) {
          // Erreur côté client
          errorMessage = `Error: ${error.error.message}`;
        } else {
          // Erreur côté serveur
          errorMessage = this.getServerErrorMessage(error);
        }

        // Afficher l'erreur sauf pour certains codes
        if (![401, 403].includes(error.status)) {
          this.showError(errorMessage, error.status);
        }

        return throwError(() => error);
      })
    );
  }

  /**
   * Récupère le message d'erreur du serveur
   */
  private getServerErrorMessage(error: HttpErrorResponse): string {
    switch (error.status) {
      case 400:
        return error.error?.message || this.translate.instant('errors.bad_request');
      case 401:
        return this.translate.instant('errors.unauthorized');
      case 403:
        return this.translate.instant('errors.forbidden');
      case 404:
        return error.error?.message || this.translate.instant('errors.not_found');
      case 409:
        return error.error?.message || this.translate.instant('errors.conflict');
      case 422:
        return this.getValidationErrors(error);
      case 500:
        return this.translate.instant('errors.server_error');
      case 503:
        return this.translate.instant('errors.service_unavailable');
      default:
        return error.error?.message || this.translate.instant('errors.unknown');
    }
  }

  /**
   * Récupère les erreurs de validation
   */
  private getValidationErrors(error: HttpErrorResponse): string {
    if (error.error?.errors && Array.isArray(error.error.errors)) {
      return error.error.errors
        .map((err: any) => `${err.field}: ${err.message}`)
        .join('\n');
    }
    return error.error?.message || this.translate.instant('errors.validation');
  }

  /**
   * Affiche une alerte d'erreur
   */
  private showError(message: string, status: number): void {
    const title = this.translate.instant('errors.error_title');
    
    Swal.fire({
      title: title,
      text: message,
      icon: 'error',
      confirmButtonText: this.translate.instant('common.ok'),
      confirmButtonColor: '#dc3545'
    });

    // Log pour le développement
    if (!environment.production) {
      console.error(`[HTTP Error ${status}]:`, message);
    }
  }
}

// Import de l'environnement pour le logging
import { environment } from '@environments/environment';
