import { ActivatedRouteSnapshot, CanActivate, CanActivateFn, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from './auth.service';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class PublicGuard implements CanActivate {

 
  constructor(
    private router: Router,
    private authService: AuthService
  ) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // 🔍 Vérifier si l'utilisateur est déjà connecté
    const currentUser = this.authService.currentUserValue;
    
    if (currentUser) {
      // ❌ L'utilisateur EST connecté
      // Il ne devrait pas accéder aux pages publiques (login, forgot-password)
      console.log('🔐 PublicGuard: Utilisateur déjà connecté, redirection vers dashboard');
      this.router.navigate(['/dashboard']);
      return false; // Bloquer l'accès à la page demandée
    }

    // ✅ L'utilisateur N'EST PAS connecté
    // Il peut accéder aux pages publiques
    console.log('✅ PublicGuard: Utilisateur non connecté, accès autorisé');
    return true; // Autoriser l'accès à la page demandée
  }
}
