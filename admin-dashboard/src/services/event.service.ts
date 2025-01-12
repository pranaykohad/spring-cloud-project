import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { environment } from '../../environment';
import {
  EventMedia,
  EventMediaRequest,
  EventOverview,
} from '../models/EventDetailObject';
import { EventListDto } from '../models/EventListDto';
import { SearchRequest } from '../models/SearchRequest';

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

  saveEventPhotos(
    eventPhotos: File[],
    eventMediaRequest: EventMediaRequest
  ): Observable<EventMedia[]> {
    const formData = new FormData();
    eventPhotos.forEach((i) => {
      formData.append('eventPhotos', i);
    });
    formData.append('eventMediaReqObject', JSON.stringify(eventMediaRequest));
    return this.http.patch<EventMedia[]>(
      `${this.baseUrl}api/event/event-photos`,
      formData
    );
  }
}
