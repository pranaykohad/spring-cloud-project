import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { PerformerInfoComponent } from '../performer-info/performer-info.component';
import { BookingInfoComponent } from './booking-info/booking-info.component';
import { EventOverviewComponent } from './event-overview/event-overview.component';

@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [
    SharedModule,
    EventOverviewComponent,
    BookingInfoComponent,
    PerformerInfoComponent,
  ],
  templateUrl: './event-details.component.html',
  styleUrl: './event-details.component.scss',
})
export class EventDetailsComponent implements OnInit {
  eventId!: number;
  organizer!: string;
  isNewEvent!: boolean;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.eventId = this.activatedRoute.snapshot.params['eventId'];
    this.organizer = this.activatedRoute.snapshot.params['organizer'];
    if (Number(this.eventId) === 0 && this.organizer === 'new') {
      this.isNewEvent = true;
    }
  }
}
