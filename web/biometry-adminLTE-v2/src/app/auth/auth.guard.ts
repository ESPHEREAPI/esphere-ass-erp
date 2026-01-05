import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';


@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const currentUser = this.authService.currentUserValue;
    
    // ✅ DÉCOMMENTER ET CORRIGER
    if (currentUser) {
      // Vérifier le rôle si nécessaire
      if (route.data['roles'] && route.data['roles'].length > 0) {
        const userRole = currentUser.role;
        if (userRole && route.data['roles'].includes(userRole)) {
          return true;
        }
        this.router.navigate(['/access-denied']);
        return false;
      }
      return true; // ← IMPORTANT : Autoriser si connecté
    }

    // Pas connecté, rediriger vers login
    this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }
}