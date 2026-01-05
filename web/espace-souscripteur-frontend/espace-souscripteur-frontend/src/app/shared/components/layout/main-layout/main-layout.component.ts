import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { AuthService } from '@core/services/auth.service';
import { NotificationService } from '@core/services/notification.service';
import { User } from '@core/models/auth.model';
import { Observable } from 'rxjs';

/**
 * Composant du layout principal avec AdminLTE
 */
@Component({
  selector: 'app-main-layout',
  templateUrl: './main-layout.component.html',
  styleUrls: ['./main-layout.component.scss']
})
export class MainLayoutComponent implements OnInit {
  currentUser: User | null = null;
  unreadNotificationsCount$: Observable<number>;
  sidebarCollapsed = false;
  currentLang: string = 'fr';

  menuItems = [
    {
      label: 'menu.dashboard',
      icon: 'fas fa-tachometer-alt',
      route: '/dashboard',
      permission: 'dashboard.view'
    },
    {
      label: 'menu.adherents',
      icon: 'fas fa-users',
      permission: 'adherents.view',
      children: [
        {
          label: 'menu.adherents_list',
          route: '/adherents/list',
          permission: 'adherents.list'
        },
        {
          label: 'menu.adherent_profile',
          route: '/adherents/profile',
          permission: 'adherents.profile'
        }
      ]
    },
    {
      label: 'menu.ayants_droit',
      icon: 'fas fa-user-friends',
      route: '/ayants-droit',
      permission: 'ayants-droit.view'
    },
    {
      label: 'menu.reporting',
      icon: 'fas fa-chart-bar',
      route: '/reporting',
      permission: 'reporting.view'
    },
    {
      label: 'menu.notifications',
      icon: 'fas fa-bell',
      route: '/notifications',
      permission: 'notifications.view'
    }
  ];

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService,
    private translate: TranslateService,
    private router: Router
  ) {
    this.unreadNotificationsCount$ = this.notificationService.unreadCount$;
  }

  ngOnInit(): void {
    this.authService.currentUser.subscribe(user => {
      this.currentUser = user;
    });

    // Récupérer la langue depuis localStorage ou utiliser la langue par défaut
    const savedLang = localStorage.getItem('language') || 'fr';
    this.setLanguage(savedLang);

    // Initialiser AdminLTE
    this.initAdminLTE();
  }

  /**
   * Initialise les fonctionnalités AdminLTE
   */
  private initAdminLTE(): void {
    // Initialisation du sidebar collapse
    const body = document.querySelector('body');
    const savedCollapse = localStorage.getItem('sidebar-collapsed');
    
    if (savedCollapse === 'true' && body) {
      body.classList.add('sidebar-collapse');
      this.sidebarCollapsed = true;
    }
  }

  /**
   * Toggle du sidebar
   */
  toggleSidebar(): void {
    const body = document.querySelector('body');
    if (body) {
      body.classList.toggle('sidebar-collapse');
      this.sidebarCollapsed = !this.sidebarCollapsed;
      localStorage.setItem('sidebar-collapsed', this.sidebarCollapsed.toString());
    }
  }

  /**
   * Change la langue de l'application
   */
  setLanguage(lang: string): void {
    this.currentLang = lang;
    this.translate.use(lang);
    localStorage.setItem('language', lang);
  }

  /**
   * Vérifie si l'utilisateur a la permission pour afficher un menu
   */
  hasPermission(permission: string): boolean {
    return this.authService.hasPermission(permission);
  }

  /**
   * Déconnexion
   */
  logout(): void {
    if (confirm(this.translate.instant('messages.confirm.logout'))) {
      this.authService.logout();
    }
  }

  /**
   * Navigue vers les notifications
   */
  goToNotifications(): void {
    this.router.navigate(['/notifications']);
  }

  /**
   * Navigue vers le profil utilisateur
   */
  goToProfile(): void {
    this.router.navigate(['/profile']);
  }
}
