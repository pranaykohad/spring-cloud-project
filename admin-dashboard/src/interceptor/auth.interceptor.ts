import {
  HttpEvent,
  HttpHandler,
  HttpHeaders,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { LocalstorageService } from '../services/localstorage.service';
import { Router } from '@angular/router';
import { LocalStorageKeys } from '../models/Enums';
import { SystemUserResponse } from '../models/SystemUserResponse';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private excludedUrls: string[] = [
    'api/user/system/auth/login',
    'api/user/system/auth/signin',
  ];

  constructor(
    private localstorageService: LocalstorageService,
    private router: Router
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const isExcluded = this.excludedUrls.some((url) => req.url.includes(url));
    const loggedInUserDetails: SystemUserResponse = this.localstorageService.getItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS);

    if (!isExcluded && loggedInUserDetails?.jwt) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${loggedInUserDetails.jwt}`,
      });
      const clonedReq = req.clone({ headers: headers });
      return next.handle(clonedReq).pipe(
        catchError((error) => {
          if (error.status === 401) {
            this.router.navigate(['login']);
          }
          return throwError(() => error);
        })
      );
    }

    return next.handle(req);
  }
}
