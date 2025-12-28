import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { SystemUserLoginRequest } from '../models/SystemUserLoginRequest';
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';
import { SystemUserSigninRequest } from '../models/SystemUserSigninRequest';

@Injectable({
  providedIn: 'root',
})
export class UserAuthService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  validateJwtToken(jwtToken: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.baseUrl}api/user/system/auth/validate-jwt?token=${jwtToken}`
    );
  }

  getLoggedinUserDetails(): Observable<LoggedInUserDetails> {
    return this.http.get<LoggedInUserDetails>(
      `${this.baseUrl}api/user/system/loggedin-user-details`
    );
  }

  userLogin(
    systemUserLoginDto: SystemUserLoginRequest
  ): Observable<LoggedInUserDetails> {
    return this.http.post<LoggedInUserDetails>(
      `${this.baseUrl}api/user/auth/system/login`,
      systemUserLoginDto
    );
  }

  userRegistration(systemUserSigninDto: SystemUserSigninRequest) {
    return this.http.post<string>(
      `${this.baseUrl}api/user/auth/system/register`,
      systemUserSigninDto
    );
  }

  userLogout(): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.baseUrl}api/user/verification/logout`
    );
  }
}
