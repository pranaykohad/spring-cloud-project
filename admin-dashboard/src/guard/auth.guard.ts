import { LocalstorageService } from './../services/localstorage.service';
import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';
import { LocalStorageKeys } from '../models/Enums';
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(
    private router: Router,
    private localstorageService: LocalstorageService
  ) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    const loggedInUserDetails: LoggedInUserDetails =
      this.localstorageService.getItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS);
    if (
      this.isAuthenticated(loggedInUserDetails) &&
      this.isAuthorized(
        route.data['roles'],
        loggedInUserDetails.roles,
        state.url
      )
    ) {
      return true;
    } else {
      this.router.navigate(['login']);
      return false;
    }
  }

  private isAuthenticated(loggedInUserDetails: LoggedInUserDetails): boolean {
    return loggedInUserDetails
      ? this.isTokenValid(loggedInUserDetails.jwt)
      : false;
  }

  private isAuthorized(
    allowedRoles: string[],
    userRoles: string[],
    url: string
  ) {
    const userRolesSet = new Set(userRoles);
    if (url === '/' && userRolesSet.has('ORGANIZER_USER')) {
      this.router.navigate(['event-list']);
    }
    return allowedRoles.some((r) => userRolesSet.has(r));
  }

  private isTokenValid(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Math.floor(new Date().getTime() / 1000);
      return payload.exp > currentTime;
    } catch (e) {
      return false;
    }
  }
}
