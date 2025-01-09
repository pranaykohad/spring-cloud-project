import { Injectable } from '@angular/core';
import { environment } from '../../environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';
import { EventListDto } from '../models/EventListDto';
import { SearchRequest } from '../models/SearchRequest';
import { EventMedia, EventOverview } from '../models/EventDetailObject';

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

  getEventOverview(
    eventId: number,
    organizer: string
  ): Observable<EventOverview> {
    return this.http.get<EventOverview>(
      `${this.baseUrl}api/event/event-overview?eventId=${encodeURIComponent(
        eventId
      )}&&organizer=${encodeURIComponent(organizer)}`
    );
  }

  saveEventOverview(eventOverview: EventOverview): Observable<number> {
    return this.http.patch<number>(
      `${this.baseUrl}api/event/event-overview`,
      eventOverview
    );
  }

  getEventPhotos(eventId: number, organizer: string): Observable<EventMedia[]> {
    return this.http.get<EventMedia[]>(
      `${this.baseUrl}api/event/event-photos?eventId=${encodeURIComponent(
        eventId
      )}&&organizer=${encodeURIComponent(organizer)}`
    );
  }

}
