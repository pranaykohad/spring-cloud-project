import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { SystemUserLoginRequest } from '../models/SystemUserLoginRequest';
import { LoggedinUserDetails } from '../models/SystemUserResponse';
import { SystemUserSigninRequest } from '../models/SystemUserSigninRequest';
import {
  UserBasicDetails,
  UserSecuredDetails,
} from '../models/UserUpdateRequest';

@Injectable({
  providedIn: 'root',
})
export class SystemUserAuthService {
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

  getUserBasicDetails(): Observable<UserBasicDetails> {
    return this.http.get<UserBasicDetails>(
      `${this.baseUrl}api/user/system/basic-details`
    );
  }

  getUserSecuredDetails(): Observable<UserSecuredDetails> {
    return this.http.get<UserSecuredDetails>(
      `${this.baseUrl}api/user/system/secured-details`
    );
  }

  updateUserBasicDetails(
    userBasicDetails: UserBasicDetails
  ): Observable<Boolean> {
    console.table(userBasicDetails);
    const formData = new FormData();
    formData.append('profilePicFile', userBasicDetails.profilePicFile);
    formData.append('displayName', userBasicDetails.displayName);
    return this.http.patch<Boolean>(
      `${this.baseUrl}api/user/system/udpate-basic-details`,
      formData
    );
  }

  updateUserSecuredDetails(
    UserBasicDetails: UserSecuredDetails
  ): Observable<Boolean> {
    return this.http.patch<Boolean>(
      `${this.baseUrl}api/user/system/udpate-secured-details`,
      UserBasicDetails
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
