import { Injectable } from "@angular/core";
import {
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from "@angular/common/http";
import { Observable } from "rxjs";

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwcmFuYXkiLCJpYXQiOjE3MzM1MDAyMDEsImV4cCI6MTczMzU4NjYwMX0.UFeMZ0C_UTnDPjTsQKvphpb3bZxRa_-0AK_CvFgSqKA';

    if (token) {
      const cloned = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`,
        },
      });
      return next.handle(cloned);
    }

    return next.handle(req);
  }
}
