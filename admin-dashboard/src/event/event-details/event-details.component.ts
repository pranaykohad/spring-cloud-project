import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { EventIdentifier } from '../../models/EventDetailObject';
import { SharedModule } from '../../shared/shared.module';
import { BookingInfoComponent } from './booking-info/booking-info.component';
import { EventOverviewComponent } from './event-overview/event-overview.component';
import { EventPhotosComponent } from './event-photos/event-photos.component';
import { PerformerInfoComponent } from './performer-info/performer-info.component';

@Component({
  selector: 'app-event-details',
  standalone: true,
  imports: [
    SharedModule,
    EventOverviewComponent,
    BookingInfoComponent,
    PerformerInfoComponent,
    EventPhotosComponent,
  ],
  templateUrl: './event-details.component.html',
  styleUrl: './event-details.component.scss',
})
export class EventDetailsComponent implements OnInit {
  eventIdentifier: EventIdentifier = {
    id: 0,
    organizer: 'new',
  };
  organizerList!: string[];
  activeIndex: number = 0;

  constructor(
    private activatedRoute: ActivatedRoute,
    private cdrf: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.activatedRoute.paramMap.subscribe((params: ParamMap) => {
      const id = Number(params.get('eventId'));
      const org = params.get('organizer');
      if (id !== null && org !== null) {
        this.eventIdentifier = {
          id: 0,
          organizer: 'new',
        };
        this.eventIdentifier.id = id;
        this.eventIdentifier.organizer = org;
      }
    });
  }
}
