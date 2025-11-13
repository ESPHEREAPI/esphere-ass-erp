import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Language } from '../../../models/langue';
import { LangueService } from '../../../services/langue.service';
import { CommonModule } from '@angular/common';
import { TranslateModule } from '@ngx-translate/core';

import { MessageService } from 'primeng/api';
import { finalize, Subject, takeUntil } from 'rxjs';
import { UserSession } from '../../../models/user-session';
import { ApiResponse } from '../../../models/api-response';
import { MessagesModule } from 'primeng/messages';
import { ToastModule } from 'primeng/toast';
import { AuthService } from '../../../auth/auth.service';

interface LoginCredentials {
  username: string;
  password: string;
  remember: boolean;
}
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, TranslateModule, FormsModule, ReactiveFormsModule, RouterLink, ToastModule,
    MessagesModule],

  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit, OnDestroy {
  languages: Language[] = [
    { code: 'en', name: 'English' },
    { code: 'fr', name: 'Français' }
  ];

  currentYear = new Date().getFullYear();
  appVersion = '2.4.18';
  isOpen = false;
  currentLanguage: Language = this.languages[0];
  loginForm!: FormGroup;

  // Error handling
  errorMessage = '';
  showError = false;
  // État du composant
  credentials: LoginCredentials = {
    username: '',
    password: '',
    remember: false
  };

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }


  // Loading states
  isLoading = false;
  loadingProgress = 0;
  returnUrl = 'dashboard';
  // Gestion des erreurs
  // Gestion des erreurs
  private readonly destroy$ = new Subject<void>();
  private maxLoginAttempts = 3;
  private loginAttempts = 0;
  private lockoutTime = 5 * 60 * 1000; // 5 minutes en millisecondes


  constructor(
    private router: Router,
    private fb: FormBuilder,
    private langueService: LangueService,
    private authService: AuthService,

    private route: ActivatedRoute,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      remember: [false]
    });

    const index = this.languages.findIndex(
      l => l.code === this.langueService._userLanguage
    );
    if (index !== -1) {
      this.currentLanguage = this.languages[index];
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.credentials = this.loginForm.value;
    // Réinitialiser les erreurs
    this.errorMessage = '';
    this.showError = false;

    // Démarrer l'animation de chargement
    this.isLoading = true;
    this.animateProgress();

    // Simuler l'authentification (remplacer par votre vrai service)
    this.performLogin();
    setTimeout(() => {
      console.log('Login:', this.credentials.username);
      console.log('Mot de passe:', this.credentials.password);
      console.log('Se souvenir:', this.credentials.remember);

      // 👉 Ici ta logique d'authentification réelle
     // this.router.navigateByUrl('/dashboard');
    }, 2000);
  }


  private performLogin(): void {
    this.isLoading = true;
    this.messageService.clear();

  //  const { username, password } = this.credentials;
  this.credentials.username=this.loginForm.get("username")?.value ?? "";
  this.credentials.password=this.loginForm.get("password")?.value ?? "";
  this.credentials.remember=this.loginForm.get("password")?.value ?? "";

    this.authService.login(this.credentials)
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
    // this.resetLoginAttempts();
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

  // Animation de la barre de progression
  private animateProgress(): void {
    const interval = setInterval(() => {
      if (this.loadingProgress >= 100) {
        clearInterval(interval);
      } else {
        this.loadingProgress += Math.random() * 30;
        if (this.loadingProgress > 100) {
          this.loadingProgress = 100;
        }
      }
    }, 200);
  }

  toggleDropdown(): void {
    this.isOpen = !this.isOpen;
  }

  selectLanguage(lang: Language): void {
    this.currentLanguage = lang;
    this.isOpen = false;
    this.langueService.setLanguage(this.currentLanguage.code);
  }

  get f() {
    return this.loginForm.controls;
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
  private showSuccessMessage(summary: string, detail: string): void {
    this.messageService.add({
      severity: 'success',
      summary,
      detail,
      life: 3000
    });
  }

}

