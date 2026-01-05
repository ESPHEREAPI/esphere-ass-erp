import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
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



  {

    path: '',
    canActivate: [AuthGuard, RoleGuard],
    component: AdminLayoutComponentComponent,
    children: [

      { path: 'dashboard', component: DashboardComponent },

      // { path: 'users', component: UsersComponent },
      {
    path: 'adherents',
    children: [
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
      },
      
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
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'create',
        component: UserFormComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['WRITE'],
          permissionCheckMethod: 'any'
        }
      },

      {
        path: 'edit/:id',
        component: UserFormComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['UPDATE'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'detail/:id',
        component: UserDetailComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'general',
        component: GeneralSettingsComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      },
      {
        path: 'security',
        component: SecuritySettingsComponent,
        canActivate: [AuthGuard, RoleGuard],
        data: {
          permissions: ['READ'],
          permissionCheckMethod: 'any'
        }
      }

      //{ path: 'settings/general', component: GeneralSettingsComponent },
      // { path: 'settings/security', component: SecuritySettingsComponent },
    ]
  },
  {
    path: 'login', component: LoginComponent,
    canActivate: [AuthGuard],
  },
  { path: 'home', component: HomeComponent },


];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }