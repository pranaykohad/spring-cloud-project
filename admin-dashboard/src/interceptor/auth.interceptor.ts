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
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';
import { ToastService } from '../services/toast.service';
import { MessageService } from '../behaviorSubject/message.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private excludedUrls: string[] = [
    'api/user/system/auth/login',
    'api/user/system/auth/signin',
  ];

  constructor(
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService,
    private messageService: MessageService
  ) {}

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const isExcluded = this.excludedUrls.some((url) => req.url.includes(url));
    const loggedInUserDetails: LoggedInUserDetails =
      this.localstorageService.getItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS);

    if (!isExcluded && loggedInUserDetails?.jwt) {
      const headers = new HttpHeaders({
        Authorization: `Bearer ${loggedInUserDetails.jwt}`,
      });
      req = req.clone({ headers: headers });
    }

    return this.Appinterceptor(next, req);
  }

  private Appinterceptor(
    next: HttpHandler,
    clonedReq: HttpRequest<any>
  ): Observable<HttpEvent<any>> {
    return next.handle(clonedReq).pipe(
      catchError((error) => {
        this.messageService.disableLoader();
        if (error.status === 401) {
          this.toastService.showErrorToast(error.error.error);
          // this.router.navigate(['login']);
        } else if (error.status === 503) {
          this.toastService.showErrorToast(error.error.error);
        } else {
          this.toastService.showErrorToast(error.error.error);
        }
        return throwError(() => error);
      })
    );
  }
}
