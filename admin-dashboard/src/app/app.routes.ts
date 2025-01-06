import { Routes } from '@angular/router';
import { AuthGuard } from '../guard/auth.guard';
import { APP_ROUTES } from '../models/Enums';

export const routes: Routes = [
  {
    path: APP_ROUTES.BASE,
    loadComponent: () => import('./app.component').then((m) => m.AppComponent),
    children: [
      {
        path: APP_ROUTES.BASE,
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
            path: APP_ROUTES.BASE,
            loadComponent: () =>
              import('../user/user.component').then(
                (m) => m.UserComponent
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
            path: `${APP_ROUTES.PROFILE}/:userName`,
            loadComponent: () =>
              import('../profile/profile.component').then(
                (m) => m.ProfileComponent
              ),
          },
          {
            path: APP_ROUTES.SETTING,
            loadComponent: () =>
              import('../setting/setting.component').then(
                (m) => m.SettingComponent
              ),
          },
          {
            path: APP_ROUTES.EVENT_LIST,
            loadComponent: () =>
              import('../event/event.component').then(
                (m) => m.EventComponent
              ),
          },
          {
            path: `${APP_ROUTES.EVENT_DETAILS}/:eventId`,
            loadComponent: () =>
              import('../event/event-details/event-details.component').then(
                (m) => m.EventDetailsComponent
              ),
          },
          {
            path: APP_ROUTES.ADD_SECURITY,
            loadComponent: () =>
              import('../add-security-person/add-security-person.component').then(
                (m) => m.AddSecurityPersonComponent
              ),
          },
        ],
      },
      {
        path: APP_ROUTES.LOGIN,
        loadComponent: () =>
          import('../home/login/login.component').then((m) => m.LoginComponent),
      },
      {
        path: APP_ROUTES.SIGNIN,
        loadComponent: () =>
          import('../home/signin/signin.component').then(
            (m) => m.SigninComponent
          ),
      },
    ],
  },
  { path: '**', redirectTo: '', pathMatch: 'full' },
];
