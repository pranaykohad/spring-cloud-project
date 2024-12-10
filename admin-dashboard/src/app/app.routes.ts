import { Routes } from '@angular/router';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { LoginComponent } from '../home/login/login.component';
import { SigninComponent } from '../home/signin/signin.component';
import { AppComponent } from './app.component';

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
