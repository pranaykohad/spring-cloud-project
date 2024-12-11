import { Routes } from '@angular/router';
import { AuthGuard } from '../guard/auth.guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./app.component').then((m) => m.AppComponent),
    children: [
      {
        path: '',
        loadComponent: () =>
          import('../dashboard/dashboard.component').then(
            (m) => m.DashboardComponent
          ),
        canActivate: [AuthGuard],
      },
      {
        path: 'login',
        loadComponent: () =>
          import('../home/login/login.component').then((m) => m.LoginComponent),
      },
      {
        path: 'signin',
        loadComponent: () =>
          import('../home/signin/signin.component').then(
            (m) => m.SigninComponent
          ),
      },
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
