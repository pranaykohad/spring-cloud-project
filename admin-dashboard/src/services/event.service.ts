import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { EventListDto } from '../models/EventListDto';
import { SearchRequest } from '../models/SearchRequest';
import { EventOverview } from '../models/EventDetailObject';

@Injectable({
  providedIn: 'root',
})
export class EventService {
  private baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getEventList(searchRequest: SearchRequest): Observable<EventListDto> {
    return this.http.post<EventListDto>(
      `${this.baseUrl}api/event/list`,
      searchRequest
    );
  }

  getEventOverview(eventId: number, organizer: string) {
    return this.http.get<EventOverview>(
      `${this.baseUrl}api/event/event-overview?eventId=${encodeURIComponent(
        eventId
      )}&&organizer=${encodeURIComponent(organizer)}`
    );
  }

  saveEventOverview(eventOverview: EventOverview) {
    return this.http.post<boolean>(
      `${this.baseUrl}api/event/event-overview`,
      eventOverview
    );
  }
}
