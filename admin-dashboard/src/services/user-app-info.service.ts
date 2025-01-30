import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserAppInfoService {

  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getCsrfToken(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}api/user/app-info/active-profile`);
  }
}
