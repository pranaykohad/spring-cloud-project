import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { SystemUserLoginRequest } from '../models/SystemUserLoginRequest';
import { LoggedinUserDetails } from '../models/LoggedinUserDetails';
import { SystemUserSigninRequest } from '../models/SystemUserSigninRequest';
import {
  UserBasicDetails,
  UserSecuredDetailsReq,
} from '../models/UserUpdateRequest';

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

  getLoggedinUserDetails(): Observable<LoggedinUserDetails> {
    return this.http.get<LoggedinUserDetails>(
      `${this.baseUrl}api/user/system/details`
    );
  }

  userLogin(
    systemUserLoginDto: SystemUserLoginRequest
  ): Observable<LoggedinUserDetails> {
    return this.http.post<LoggedinUserDetails>(
      `${this.baseUrl}api/user/system/auth/login`,
      systemUserLoginDto
    );
  }

  userSignin(systemUserSigninDto: SystemUserSigninRequest) {
    return this.http.post<string>(
      `${this.baseUrl}api/user/system/auth/signup`,
      systemUserSigninDto
    );
  }

  userLogout(token: string): Observable<boolean> {
    return this.http.get<boolean>(
      `${this.baseUrl}api/user/system/auth/logout?token=${token}`
    );
  }
}
