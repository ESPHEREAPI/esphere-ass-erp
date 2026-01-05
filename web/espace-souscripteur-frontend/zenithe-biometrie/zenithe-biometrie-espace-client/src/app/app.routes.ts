import { Routes } from '@angular/router';
import { AdminLayoutComponentComponent } from './admin-layout.component/admin-layout.component.component';
import { DashboardComponent } from './dashboard/dashboard.component';
import { LoginComponent } from './module-users/login/login.component';
import { UsersComponent } from './module-users/users/users.component';
import { AuthGuard } from './auth/auth.guard';
import { RoleGuard } from './auth/role-guard';
import { UserListComponent } from './user-manager/user-list/user-list.component';
import { UserFormComponent } from './user-manager/user-form/user-form.component';
import { UserDetailComponent } from './user-manager/user-detail/user-detail.component';
import { GeneralSettingsComponent } from './module-users/general-settings/general-settings.component';
import { SecuritySettingsComponent } from './module-users/security-settings/security-settings.component';
import { UserRoleComponent } from './user-manager/user-role/user-role.component';
import { HomeComponent } from './pages/home/home.component';
import { AdherentListComponent } from './pages/adherent-list/adherent-list.component';
import { AdherentFormComponent } from './pages/adherent-form/adherent-form.component';
import { AdherentProfileComponent } from './pages/adherent-profile/adherent-profile.component';
import { DetailConsomationAdherentComponent } from './pages/detail-consomation-adherent/detail-consomation-adherent.component';

export const routes: Routes = [
  // ✅ Route de login SANS protection (accessible à tous)
  {
    path: 'login',
    component: LoginComponent
  },
  
  // ✅ Route home publique
  {
    path: 'home',
    component: HomeComponent
  },
  
  // ✅ Routes protégées sous AdminLayout
  {
    path: '',
    canActivate: [AuthGuard], // Seulement AuthGuard ici
    component: AdminLayoutComponentComponent,
    children: [
      // Redirection par défaut vers dashboard
      { 
        path: '', 
        redirectTo: 'dashboard', 
        pathMatch: 'full' 
      },
      { 
        path: 'dashboard', 
        component: DashboardComponent 
      },
      {
        path: 'adherents',
        children: [
          {
            path: '',
            redirectTo: 'adherents-list',
            pathMatch: 'full'
          },
          {
            path: 'adherents-list',
            component: AdherentListComponent,
            data: { title: 'Liste des Adhérents' }
          },
          {
            path: 'create',
            component: AdherentFormComponent,
            data: { title: 'Créer un Adhérent' }
          },
          {
            path: ':id',
            component: AdherentProfileComponent,
            data: { title: 'Profil Adhérent' }
          },
          {
            path: ':id/edit',
            component: AdherentFormComponent,
            data: { title: 'Modifier un Adhérent' }
          }
        ]
      },
      {
        path: 'historique',
        component: DetailConsomationAdherentComponent,
        data: { title: 'historique un Adhérent' }
      },
      {
        path: 'role',
        component: UserRoleComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'create',
        component: UserFormComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['WRITE'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'edit/:id',
        component: UserFormComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['UPDATE'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'detail/:id',
        component: UserDetailComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'general',
        component: GeneralSettingsComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'security',
        component: SecuritySettingsComponent,
        canActivate: [RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      }
    ]
  },
  
  // ✅ Route wildcard pour capturer les URLs invalides
  {
    path: '**',
    redirectTo: 'login'
  }
];