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
    const jwtToken = this.localstorageService.getItem('jwtToken');

    if (!isExcluded && jwtToken) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${this.localstorageService.getItem('jwtToken')}`,
      });
      const clonedReq = req.clone({ headers: headers });
      return next.handle(clonedReq).pipe(
        catchError((error) => {
          if (error.status === 401) {
            this.router.navigate([''], {
              queryParams: { returnUrl: req.url },
            });
          }
          return throwError(() => error);
        })
      );
    }

    return next.handle(req);
  }
}
