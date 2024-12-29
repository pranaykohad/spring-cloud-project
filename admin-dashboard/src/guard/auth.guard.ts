import { LocalstorageService } from './../services/localstorage.service';
import { Injectable } from '@angular/core';
import {
  CanActivate,
  Router,
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
} from '@angular/router';
import { LocalStorageKeys } from '../models/Enums';
import { LoggedinUserDetails } from '../models/LoggedinUserDetails';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router, private localstorageService: LocalstorageService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): boolean {
    if (this.isAuthenticated()) {
      return true;
    } else {
      this.router.navigate(['login']);
      return false;
    }
  }

  isAuthenticated(): boolean {
    const loggedInUserDetails: LoggedinUserDetails = this.localstorageService.getItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS);
    return loggedInUserDetails ? this.isTokenValid(loggedInUserDetails.jwt) : false;
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
