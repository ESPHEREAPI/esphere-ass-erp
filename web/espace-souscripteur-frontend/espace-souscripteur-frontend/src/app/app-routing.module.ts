import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// Guards
import { AuthGuard } from '@core/guards/auth.guard';

// Layouts
import { MainLayoutComponent } from '@shared/components/layout/main-layout/main-layout.component';

/**
 * Routes de l'application
 */
const routes: Routes = [
  // Route de connexion (sans layout)
  {
    path: 'login',
    loadChildren: () => import('./modules/auth/auth.module').then(m => m.AuthModule)
  },

  // Routes avec le layout principal (protégées par AuthGuard)
  {
    path: '',
    component: MainLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      // Redirection par défaut vers le dashboard
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },

      // Module Dashboard
      {
        path: 'dashboard',
        loadChildren: () => import('./modules/dashboard/dashboard.module').then(m => m.DashboardModule),
        data: { 
          roles: ['SOUSCRIPTEUR', 'ADMIN'],
          permissions: ['dashboard.view']
        }
      },

      // Module Adhérents
      {
        path: 'adherents',
        loadChildren: () => import('./modules/adherents/adherents.module').then(m => m.AdherentsModule),
        data: { 
          roles: ['SOUSCRIPTEUR', 'ADMIN'],
          permissions: ['adherents.view']
        }
      },

      // Module Ayants Droit
      {
        path: 'ayants-droit',
        loadChildren: () => import('./modules/ayants-droit/ayants-droit.module').then(m => m.AyantsDroitModule),
        data: { 
          roles: ['SOUSCRIPTEUR', 'ADMIN'],
          permissions: ['ayants-droit.view']
        }
      },

      // Module Reporting
      {
        path: 'reporting',
        loadChildren: () => import('./modules/reporting/reporting.module').then(m => m.ReportingModule),
        data: { 
          roles: ['SOUSCRIPTEUR', 'ADMIN'],
          permissions: ['reporting.view']
        }
      },

      // Module Notifications
      {
        path: 'notifications',
        loadChildren: () => import('./modules/notifications/notifications.module').then(m => m.NotificationsModule),
        data: { 
          roles: ['SOUSCRIPTEUR', 'ADMIN'],
          permissions: ['notifications.view']
        }
      },

      // Module Profil utilisateur
      {
        path: 'profile',
        loadChildren: () => import('./modules/profile/profile.module').then(m => m.ProfileModule),
        data: { 
          roles: ['SOUSCRIPTEUR', 'ADMIN']
        }
      }
    ]
  },

  // Page d'accès refusé
  {
    path: 'access-denied',
    loadChildren: () => import('./modules/error/error.module').then(m => m.ErrorModule)
  },

  // Page 404 - Not Found
  {
    path: '404',
    loadChildren: () => import('./modules/error/error.module').then(m => m.ErrorModule)
  },

  // Wildcard route - redirection vers 404
  {
    path: '**',
    redirectTo: '404'
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      useHash: false,
      enableTracing: false, // Mettre à true pour debug
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled'
    })
  ],
  exports: [RouterModule]
})
export class AppRoutingModule { }
