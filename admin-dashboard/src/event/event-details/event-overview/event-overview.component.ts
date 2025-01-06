import { Component, Input } from '@angular/core';
import {
  EventOverview,
  EventOverviewError,
} from '../../../models/EventDetailObject';
import { EventService } from '../../../services/event.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-event-overview',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './event-overview.component.html',
  styleUrl: './event-overview.component.scss',
})
export class EventOverviewComponent {
  @Input()
  eventId!: number;

  @Input()
  organizer!: string;

  private _isNewEvent!: boolean;

  @Input()
  set isNewEvent(value: boolean) {
    if (value === true) {
      this._isNewEvent = true;
    } else {
      this._isNewEvent = false;
      this.getEventOverview();
    }
  }

  get isNewEvent() {
    return this._isNewEvent;
  }

  eventOverviewEdit: EventOverview = {
    id: 0,
    eventTitle: '',
    eventDescription: '',
    organizer: '',
    userMinAge: 0,
    createdOn: '',
    eventPhotos: [],
  };

  eventOverviewError: EventOverviewError = {
    eventTitle: '',
    eventDescription: '',
    userMinAge: '',
  };

  eventOverview!: EventOverview;
  enableEventSaveBtn: boolean = false;

  constructor(private eventService: EventService) {}

  clearEventDetailsValidation() {
    this.eventOverviewError = {
      eventTitle: '',
      eventDescription: '',
      userMinAge: '',
    };
    this.enableEventSaveBtn = true;
  }

  saveEventDetails() {
    if (this.validateEventDetails() && this.enableEventSaveBtn) {
      this.eventService
        .saveEventOverview(this.eventOverviewEdit)
        .subscribe((res: boolean) => {});
    }
  }

  validateEventDetails(): boolean {
    let isEventDetailsValid = true;
    if (this.eventOverviewEdit.eventTitle.length <= 5) {
      this.eventOverviewError.eventTitle = 'Event title is too short';
      isEventDetailsValid = false;
    }
    if (this.eventOverviewEdit.eventDescription.length <= 10) {
      this.eventOverviewError.eventDescription =
        'Event description is too short';
    }
    if (
      this.eventOverviewEdit.userMinAge <= 0 &&
      this.eventOverviewEdit.userMinAge >= 100
    ) {
      this.eventOverviewError.userMinAge = 'Invalid user minium age';
    }
    if (isEventDetailsValid) {
      this.cancelEventDetailsChanges();
    }
    this.eventOverviewEdit.eventTitle =
      this.eventOverviewEdit.eventTitle.trim();
    this.eventOverviewEdit.eventDescription =
      this.eventOverviewEdit.eventDescription.trim();
    this.eventOverviewEdit.userMinAge;
    return false;
  }

  cancelEventDetailsChanges() {
    if (this.isNewEvent) {
      this.eventOverviewEdit = {
        id: 0,
        eventTitle: '',
        eventDescription: '',
        organizer: '',
        userMinAge: 0,
        createdOn: '',
        eventPhotos: [],
      };
    } else {
      this.getEventOverview();
    }
    this.enableEventSaveBtn = false;
    this.eventOverviewError = {
      eventTitle: '',
      eventDescription: '',
      userMinAge: '',
    };
  }

  getEventOverview() {
    this.eventService
      .getEventOverview(this.eventId, this.organizer)
      .subscribe((res: EventOverview) => {
        this.eventOverview = res;
        this.eventOverviewEdit = this.eventOverview;
      });
  }
}
