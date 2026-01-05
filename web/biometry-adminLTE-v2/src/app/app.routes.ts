import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login/login/login.component';
import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout/admin-layout.component';

import { DashboardComponent } from './pages/dashboard/dashboard/dashboard.component';
import { ForgotPasswordComponent } from './pages/forgot-password/forgot-password.component';
import { NetworkErrorComponent } from './pages/errors/network-error/network-error.component';
import { HttpErrorComponent } from './pages/errors/http-error/http-error.component';
import { PublicGuard } from './auth/public.guard';
import { AuthGuard } from './auth/auth.guard';

export const routes: Routes = [

    
 {
    path: '',
    redirectTo: '/login',
    pathMatch: 'full'
  },
    {
    path: 'login',
    component: LoginComponent,
        canActivate: [PublicGuard]  // ⭐ NOUVEAU
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent,
        canActivate: [PublicGuard]  // ⭐ NOUVEAU
  },
   {
    path: 'network-error',
    component: NetworkErrorComponent,
    //  canActivate: [PublicGuard]  // ⭐ NOUVEAU
  },
  {
    path: 'error',
    component: HttpErrorComponent
  },
   {
  path: '',
    component: AdminLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      {
        path: 'dashboard',
        data: { breadcrumb: 'Tableau de bord' },
       component: DashboardComponent
      },

    ]
    },
     {
    path: '**',
    component: HttpErrorComponent,
    data: { errorCode: 404 }
  }
];
