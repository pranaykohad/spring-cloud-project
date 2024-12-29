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
          import('../dashboard/main/dashboard.component').then(
            (m) => m.DashboardComponent
          ),
        canActivate: [AuthGuard],
        data: {
          roles: [
            'SYSTEM_USER',
            'ADMIN_USER',
            'SUPPORT_USER',
            'SUPER_ADMIN_USER',
            'ORGANIZER_USER'
          ],
        },
        children: [
          {
            path: '',
            loadComponent: () =>
              import('../dashboard/main/main/main.component').then(
                (m) => m.MainComponent
              ),
            canActivate: [AuthGuard],
            data: {
              roles: [
                'SYSTEM_USER',
                'ADMIN_USER',
                'SUPPORT_USER',
                'SUPER_ADMIN_USER',
                'ORGANIZER_USER'
              ],
            },
          },
          {
            path: 'profile/:userName',
            loadComponent: () =>
              import('../profile/profile.component').then(
                (m) => m.ProfileComponent
              ),
          },
          {
            path: 'setting',
            loadComponent: () =>
              import('../setting/setting.component').then(
                (m) => m.SettingComponent
              ),
          },
          {
            path: 'event',
            loadComponent: () =>
              import('../event/event/event.component').then(
                (m) => m.EventComponent
              ),
          },
          {
            path: 'add-security',
            loadComponent: () =>
              import('../add-security-person/add-security-person.component').then(
                (m) => m.AddSecurityPersonComponent
              ),
          },
        ],
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
