import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { UserInfoListObject } from '../models/UserInfoListObject';
import {
  UserBasicDetails,
  UserSecuredDetailsReq,
  UserSecuredDetailsRes,
} from '../models/UserUpdateRequest';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getUserBasicDetails(userName: string): Observable<UserBasicDetails> {
    return this.http.get<UserBasicDetails>(
      `${this.baseUrl}api/user/system/basic-details?userName=${encodeURIComponent(userName)}`
    );
  }

  getUserSecuredDetails(userName: string): Observable<UserSecuredDetailsRes> {
    return this.http.get<UserSecuredDetailsRes>(
      `${this.baseUrl}api/user/system/secured-details?userName=${encodeURIComponent(userName)}`
    );
  }

  generateOtp(userName: string, device: string): Observable<void> {
    return this.http.get<void>(
      `${this.baseUrl}api/user/system/generate-otp?userName=${encodeURIComponent(userName)}&device=${device}`
    );
  }

  userActivation(userName: string, otp: string): Observable<void> {
    return this.http.get<void>(
      `${this.baseUrl}api/user/system/user-activation?userName=${encodeURIComponent(userName)}&otp=${otp}`
    );
  }

  updateBasicDetails(userBasicDetails: UserBasicDetails): Observable<Boolean> {
    const formData = new FormData();
    formData.append('profilePicFile', userBasicDetails.profilePicFile);
    formData.append('displayName', userBasicDetails.displayName);
    formData.append('userName', userBasicDetails.userName);
    return this.http.patch<Boolean>(
      `${this.baseUrl}api/user/system/update-basic-details`,
      formData
    );
  }

  updateSecuredDetails(
    UserBasicDetails: UserSecuredDetailsReq
  ): Observable<Boolean> {
    return this.http.patch<Boolean>(
      `${this.baseUrl}api/user/system/update-secured-details`,
      UserBasicDetails
    );
  }

  getUserInfoList(): Observable<UserInfoListObject> {
    return this.http.get<UserInfoListObject>(
      `${this.baseUrl}api/user/system/user-list`
    );
  }
}
