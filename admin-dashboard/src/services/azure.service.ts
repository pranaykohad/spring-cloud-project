import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AzureService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getBlob(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}api/user/profile/download`);
  }

  private downloadBlob(res: any) {
    console.log('res: ', res);
  }
}
