import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AttachmentDto } from '../models/AttachmentDto';

@Injectable({
  providedIn: 'root',
})
export class AzureService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  downloadBlob(): Observable<AttachmentDto> {
    return this.http.get<AttachmentDto>(`${this.baseUrl}api/user/profile/download`);
  }
}
