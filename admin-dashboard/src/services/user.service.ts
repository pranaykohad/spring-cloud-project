import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  UserBasicDetails,
  UserSecuredDetails,
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

  getUserSecuredDetails(): Observable<UserSecuredDetails> {
    return this.http.get<UserSecuredDetails>(
      `${this.baseUrl}api/user/system/secured-details`
    );
  }

  generateOtp(): Observable<void> {
    return this.http.get<void>(`${this.baseUrl}api/user/system/generate-otp`);
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
}
