import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { EventListDto } from '../models/EventListDto';

@Injectable({
  providedIn: 'root'
})
export class EventService {

   private baseUrl = environment.apiUrl;

    constructor(private http: HttpClient) {}

    getEventList(): Observable<EventListDto> {
        return this.http.get<EventListDto>(
          `${this.baseUrl}api/event/list`
        );
      }
}
