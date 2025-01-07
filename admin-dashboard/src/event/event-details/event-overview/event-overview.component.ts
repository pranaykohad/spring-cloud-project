import { Component, Input } from '@angular/core';
import {
  EventMedia,
  EventOverview,
  EventOverviewError,
} from '../../../models/EventDetailObject';
import { EventService } from '../../../services/event.service';
import { ToastService } from '../../../services/toast.service';
import { SharedModule } from '../../../shared/shared.module';
import { ImageComponent } from '../../../image/image.component';

@Component({
  selector: 'app-event-overview',
  standalone: true,
  imports: [SharedModule, ImageComponent],
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
    eventTitle: 'pranay',
    eventDescription: '',
    organizer: '',
    userMinAge: 0,
    createdOn: '',
    eventPhotos: [
      {
        id: 0,
        mediaUrl: 'assets/img/event image 1.png',
        mediaType: '',
        isForCover: true,
        mediaFile: new File([], ''),
        index: 0,
      },
      {
        id: 0,
        mediaUrl: 'assets/img/event image 2.png',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 1,
      },
      {
        id: 0,
        mediaUrl: 'assets/img/event image 3.png',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 2,
      },
      {
        id: 0,
        mediaUrl: 'assets/img/event image 4.png',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 3,
      },
      {
        id: 0,
        mediaUrl: '',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 4,
      },
      {
        id: 0,
        mediaUrl: '',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 5,
      },
      {
        id: 0,
        mediaUrl: '',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 6,
      },
      {
        id: 0,
        mediaUrl: '',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 7,
      },
      {
        id: 0,
        mediaUrl: '',
        mediaType: '',
        isForCover: false,
        mediaFile: new File([], ''),
        index: 8,
      },
    ],
  };

  eventOverviewError: EventOverviewError = {
    eventTitle: '',
    eventDescription: '',
    userMinAge: '',
  };

  eventOverview!: EventOverview;
  enableEventSaveBtn: boolean = false;

  constructor(
    private eventService: EventService,
    private toastService: ToastService
  ) {}

  editableEventMediaHandler(event: EventMedia) {
    this.eventOverviewEdit.eventPhotos[event.index] = event;
    this.enableEventSaveBtn = true;
  }

  onUpload(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files) {
      const fileList: FileList = input.files;
      for (let i = 0; i < fileList.length; i++) {
        const file = fileList[i];
        if (!file.type.startsWith('image/')) {
          this.toastService.showErrorToast('Only image files are allowed');
          return;
        }

        const maxSize = 10 * 1024 * 1024; // 5 MB
        if (file.size > maxSize) {
          this.toastService.showErrorToast('File size exceeds 5 MB');
          return;
        }

        const eventMedia: EventMedia = {
          id: 0,
          mediaFile: new File(
            [file],
            `${this.eventOverviewEdit.eventTitle}-${i}.${file.name
              .split('.')
              .pop()}`,
            {
              type: file.type,
            }
          ),
          isForCover: false,
          mediaUrl: '',
          mediaType: file.type,
          index: 0,
        };

        const reader = new FileReader();
        reader.onload = () => {
          eventMedia.mediaUrl = reader.result as string;
        };
        reader.readAsDataURL(file);
        this.eventOverviewEdit.eventPhotos.push(eventMedia);
      }
      console.log(this.eventOverviewEdit);
    }
  }

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

  onIsCoverMediaChange(value: boolean) {}

  getEventOverview() {
    this.eventService
      .getEventOverview(this.eventId, this.organizer)
      .subscribe((res: EventOverview) => {
        this.eventOverview = res;
        this.eventOverviewEdit = JSON.parse(JSON.stringify(this.eventOverview));
      });
  }
}
