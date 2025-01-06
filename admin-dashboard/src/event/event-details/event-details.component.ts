import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { EventOverviewComponent } from './event-overview/event-overview.component';
import { BookingInfoComponent } from './booking-info/booking-info.component';
import { PerformerInfoComponent } from '../performer-info/performer-info.component';

@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [SharedModule, EventOverviewComponent, BookingInfoComponent, PerformerInfoComponent],
  templateUrl: './event-details.component.html',
  styleUrl: './event-details.component.scss',
})
export class EventDetailsComponent implements OnInit {
  sample: string[] = ['A', 'B', 'C', 'D'];

  eventId!: string;

  constructor(private activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.eventId = this.activatedRoute.snapshot.params['eventId'];
  }
}
