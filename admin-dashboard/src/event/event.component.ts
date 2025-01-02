import { Component, OnInit } from '@angular/core';
import { EventListDto } from '../models/EventListDto';
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

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.getEventList();
  }

  private getEventList() {
    this.eventService.getEventList().subscribe((res: EventListDto) => {
      this.eventListDto = res;
    });
  }
}
