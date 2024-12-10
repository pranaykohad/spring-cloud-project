import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { SystemUserResponse } from '../models/SystemUserResponse';
import { SystemUserSigninRequest } from '../models/SystemUserSigninRequest';
import { SystemUserLoginRequest } from '../models/SystemUserLoginRequest';
import { LocalstorageService } from './localstorage.service';

@Injectable({
  providedIn: 'root',
})
export class SystemUserAuthService {
  private baseUrl = environment.apiUrl;

  constructor(
    private http: HttpClient,
    private localstorageService: LocalstorageService
  ) {}

  // validateJwtToken(jwtToken: string): Observable<boolean> {
  //   return this.http.get<boolean>(
  //     `${this.baseUrl}api/user/system/auth/validate-jwt-token?jwtToken=${jwtToken}`
  //   );
  // }

  testAPI(userName: string): Observable<SystemUserResponse> {
    let headers = new HttpHeaders({
      Authorization: `Bearer ${this.localstorageService.getItem('jwtToken')}`,
    });

    return this.http.get<SystemUserResponse>(
      `${this.baseUrl}api/user/system/get-by-username?userName=${userName}`);
  }

  userLogin(
    systemUserLoginDto: SystemUserLoginRequest
  ): Observable<SystemUserResponse> {
    return this.http.post<SystemUserResponse>(
      `${this.baseUrl}api/user/system/auth/login`,
      systemUserLoginDto,
    );
  }

  userSignin(systemUserSigninDto: SystemUserSigninRequest) {
    return this.http.post<string>(
      `${this.baseUrl}api/user/system/auth/signin`,
      systemUserSigninDto
    );
  }
}
