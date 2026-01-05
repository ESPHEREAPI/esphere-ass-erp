import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ToastModule } from 'primeng/toast';
import { MessagesModule } from 'primeng/messages';
import { Subject, takeUntil, finalize } from 'rxjs';

import { AuthService } from '../../auth/auth.service';
import { UserSession } from '../../model/user-session';
import { ApiResponse } from '../../model/auth-reponse';


interface LoginCredentials {
  username: string;
  password: string;
  remember: boolean;
}

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ToastModule,
    MessagesModule
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit, OnDestroy {
  
  // État du composant
  credentials: LoginCredentials = {
    username: '',
    password: '',
    remember: false
  };
  
  isLoading = false;
  showPassword = false;
  currentYear = new Date().getFullYear();
  returnUrl = '/dashboard';
  
  // Gestion des erreurs
  private readonly destroy$ = new Subject<void>();
  private maxLoginAttempts = 3;
  private loginAttempts = 0;
  private lockoutTime = 5 * 60 * 1000; // 5 minutes en millisecondes
  
  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private messageService: MessageService
  ) {
    // Rediriger si déjà connecté
    if (this.authService.isLoggedIn()) {
      this.router.navigate([this.returnUrl]);
    }
  }

  ngOnInit(): void {
    this.initializeComponent();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  private initializeComponent(): void {
    // Récupérer l'URL de retour des paramètres de requête
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/dashboard';
    
    // Vérifier les tentatives de connexion précédentes
    this.checkLoginAttempts();
    
    // Nettoyer les messages d'erreur précédents
    this.messageService.clear();
  }

  /**
   * Gestion de la soumission du formulaire de connexion
   */
  onLogin(form: NgForm): void {
    if (form.invalid || this.isLoading) {
      this.markFormGroupTouched(form);
      return;
    }

    if (this.isAccountLocked()) {
      this.showLockoutMessage();
      return;
    }

    this.performLogin();
    // this.router.navigateByUrl('dashboard');
  }

private performLogin(): void {
  this.isLoading = true;
  this.messageService.clear();

  const { username, password } = this.credentials;

  this.authService.login(username.trim(), password)
    .pipe(
      takeUntil(this.destroy$),
      finalize(() => {
        this.isLoading = false;
      })
    )
    .subscribe({
      next: (response: ApiResponse<UserSession>) => {
        if (response.success) {
          if (response.data) {
            this.messageService.add({
              severity: 'success',
              summary: 'Connexion',
              detail: response.message
            });
            this.handleLoginSuccess(response.data);
          } else {
            this.messageService.add({
              severity: 'error',
              summary: 'Erreur',
              detail: 'Aucune donnée de session utilisateur reçue.'
            });
          }
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: response.message
          });
        }
      },
      error: (error) => {
        if (error.status === 401) {
          console.error("Erreur login:", error.error.message); // <-- message du backend
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: error.error.message
          });
          this.handleLoginError(error);
        } else {
          this.messageService.add({
            severity: 'error',
            summary: 'Erreur',
            detail: 'Erreur de connexion au serveur'
          });
          this.handleLoginError(error);
        }
      }
    });
}




  private handleLoginSuccess(userSession: UserSession): void {
    console.log("usersDTO", userSession);
    const user = userSession.userDTO;
    
    if (!user) {
      this.showErrorMessage('Erreur de connexion', 'Données utilisateur non valides');
      return;
    }

    // Vérifier l'état du compte
    if (user.echeck_connection === true) {
      this.showErrorMessage('Compte bloqué', user.messageEcheck || 'Votre compte est temporairement bloqué');
      this.incrementLoginAttempts();
      return;
    }

    // Connexion réussie
    this.resetLoginAttempts();
    this.showSuccessMessage('Connexion réussie', 'Bienvenue dans votre espace !');
    
    // Navigation avec délai pour permettre l'affichage du message
    setTimeout(() => {
      this.router.navigate([this.returnUrl]);
    }, 1000);
  }

  private handleLoginError(error: any): void {
    console.error('Erreur de connexion:', error);
    
    this.incrementLoginAttempts();
    
    let errorMessage = 'Une erreur est survenue lors de la connexion';
    
    // Gestion spécifique des erreurs
    if (error.status === 401) {
      errorMessage = 'Email ou mot de passe incorrect';
    } else if (error.status === 403) {
      errorMessage = 'Accès refusé. Contactez votre administrateur';
    } else if (error.status === 429) {
      errorMessage = 'Trop de tentatives. Veuillez patienter';
    } else if (error.status === 0) {
      errorMessage = 'Impossible de contacter le serveur. Vérifiez votre connexion';
    } else if (error.error?.message) {
      errorMessage = error.error.message;
    }

    this.showErrorMessage('Échec de la connexion', errorMessage);
    
    // Effacer le mot de passe en cas d'erreur
    this.credentials.password = '';
  }

  /**
   * Basculer la visibilité du mot de passe
   */
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  /**
   * Gestion des tentatives de connexion
   */
  private checkLoginAttempts(): void {
    const attempts = localStorage.getItem('loginAttempts');
    const lastAttempt = localStorage.getItem('lastLoginAttempt');
    
    if (attempts && lastAttempt) {
      this.loginAttempts = parseInt(attempts, 10);
      const timeSinceLastAttempt = Date.now() - parseInt(lastAttempt, 10);
      
      if (timeSinceLastAttempt > this.lockoutTime) {
        this.resetLoginAttempts();
      }
    }
  }

  private incrementLoginAttempts(): void {
    this.loginAttempts++;
    localStorage.setItem('loginAttempts', this.loginAttempts.toString());
    localStorage.setItem('lastLoginAttempt', Date.now().toString());
  }

  private resetLoginAttempts(): void {
    this.loginAttempts = 0;
    localStorage.removeItem('loginAttempts');
    localStorage.removeItem('lastLoginAttempt');
  }

  private isAccountLocked(): boolean {
    return this.loginAttempts >= this.maxLoginAttempts;
  }

  private showLockoutMessage(): void {
    const remainingTime = this.lockoutTime - (Date.now() - parseInt(localStorage.getItem('lastLoginAttempt') || '0', 10));
    const minutes = Math.ceil(remainingTime / 60000);
    
    this.showErrorMessage(
      'Compte temporairement verrouillé',
      `Trop de tentatives échouées. Réessayez dans ${minutes} minute(s).`
    );
  }

  /**
   * Marquer tous les champs du formulaire comme touchés pour afficher les erreurs
   */
  private markFormGroupTouched(form: NgForm): void {
    Object.keys(form.controls).forEach(key => {
      const control = form.controls[key];
      control.markAsTouched();
    });
  }

  /**
   * Méthodes utilitaires pour les messages
   */
  private showSuccessMessage(summary: string, detail: string): void {
    this.messageService.add({
      severity: 'success',
      summary,
      detail,
      life: 3000
    });
  }

  private showErrorMessage(summary: string, detail: string): void {
    this.messageService.add({
      severity: 'error',
      summary,
      detail,
      life: 5000
    });
  }

  private showWarningMessage(summary: string, detail: string): void {
    this.messageService.add({
      severity: 'warn',
      summary,
      detail,
      life: 4000
    });
  }
}