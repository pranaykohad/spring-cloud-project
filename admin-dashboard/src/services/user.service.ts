import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { UserInfoRespone } from '../models/UserInfoListObject';
import {
  UserBasicDetails,
  UserSecuredDetailsReq,
  UserSecuredDetailsRes,
} from '../models/UserUpdateRequest';
import { SearchRequest } from '../models/SearchRequest';

@Injectable({
  providedIn: 'root',
})
export class UserService {

  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getOrganizerList(): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.baseUrl}api/user/system/organiser-list`
    );
  }

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

  activateUser(userName: string, otp: string): Observable<void> {
    return this.http.get<void>(
      `${this.baseUrl}api/user/system/activate-user?userName=${encodeURIComponent(userName)}&otp=${otp}`
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

  getUserInfoList(searchRequest: SearchRequest): Observable<UserInfoRespone> {
    return this.http.post<UserInfoRespone>(
      `${this.baseUrl}api/user/system/user-list`, searchRequest
    );
  }
}
