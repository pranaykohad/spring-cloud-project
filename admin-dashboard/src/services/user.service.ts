import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
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

  getUserBasicDetails(): Observable<UserBasicDetails> {
    return this.http.get<UserBasicDetails>(
      `${this.baseUrl}api/user/system/basic-details`
    );
  }

  generateOtp(): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}api/user/system/generate-otp`);
  }

  updateUserBasicDetails(
    userBasicDetails: UserBasicDetails
  ): Observable<Boolean> {
    const formData = new FormData();
    formData.append('profilePicFile', userBasicDetails.profilePicFile);
    formData.append('displayName', userBasicDetails.displayName);
    return this.http.patch<Boolean>(
      `${this.baseUrl}api/user/system/udpate-basic-details`,
      formData
    );
  }

  getUserSecuredDetails(): Observable<UserSecuredDetailsRes> {
    return this.http.get<UserSecuredDetailsRes>(
      `${this.baseUrl}api/user/system/secured-details`
    );
  }

  updateUserSecuredDetails(
    UserBasicDetails: UserSecuredDetailsReq
  ): Observable<Boolean> {
    return this.http.patch<Boolean>(
      `${this.baseUrl}api/user/system/update-secured-details`,
      UserBasicDetails
    );
  }
}
