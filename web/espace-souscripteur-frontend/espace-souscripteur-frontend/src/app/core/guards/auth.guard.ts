import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  Router,
  CanActivate
} from '@angular/router';
import { AuthService } from '@core/services/auth.service';

/**
 * Guard pour protéger les routes qui nécessitent une authentification
 */
@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const currentUser = this.authService.currentUserValue;

    if (currentUser && this.authService.isAuthenticated()) {
      // Vérifier les rôles si spécifiés dans la route
      const requiredRoles = route.data['roles'] as Array<string>;
      
      if (requiredRoles && requiredRoles.length > 0) {
        const hasRequiredRole = requiredRoles.some(role => 
          this.authService.hasRole(role)
        );
        
        if (!hasRequiredRole) {
          this.router.navigate(['/access-denied']);
          return false;
        }
      }

      // Vérifier les permissions si spécifiées dans la route
      const requiredPermissions = route.data['permissions'] as Array<string>;
      
      if (requiredPermissions && requiredPermissions.length > 0) {
        const hasAllPermissions = requiredPermissions.every(permission =>
          this.authService.hasPermission(permission)
        );
        
        if (!hasAllPermissions) {
          this.router.navigate(['/access-denied']);
          return false;
        }
      }

      return true;
    }

    // Rediriger vers la page de connexion avec l'URL de retour
    this.router.navigate(['/login'], {
      queryParams: { returnUrl: state.url }
    });
    return false;
  }
}
