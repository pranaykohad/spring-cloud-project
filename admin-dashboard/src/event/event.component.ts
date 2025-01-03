import { Component, OnInit } from '@angular/core';
import { SearchOperator, SortOrder } from '../models/Enums';
import { EventListDto } from '../models/EventListDto';
import { SearchRequest } from '../models/SearchRequest';
import { EventService } from '../services/event.service';
import { SharedModule } from '../shared/shared.module';
import { EventDatatableComponent } from './event-datatable/event-datatable.component';

@Component({
  selector: 'app-event',
  standalone: true,
  imports: [SharedModule, EventDatatableComponent],
  templateUrl: './event.component.html',
  styleUrl: './event.component.scss',
})
export class EventComponent implements OnInit {
  eventListDto!: EventListDto;
  searchRequest!: SearchRequest;

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.searchRequest = {
      currentPage: 0,
      sortColumn: 'id',
      sortOrder: SortOrder.ASC,
      searchFilters: [
        {
          key: 'organizer',
          value: 'nile',
          operator: SearchOperator.LIKE,
          valueTo: null,
        }
      ],
    };
    this.getEventList();
  }

  pageEmitHandler(pageNumber: number) {
    this.searchRequest.currentPage = pageNumber;
    this.getEventList();
  }

  private getEventList() {
    this.eventService
      .getEventList(this.searchRequest)
      .subscribe((res: EventListDto) => {
        this.eventListDto = res;
      });
  }
}
