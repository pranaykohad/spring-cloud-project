import {
  Component,
  ComponentRef,
  Input,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { Router } from '@angular/router';
import { ImageComponent } from '../../../image/image.component';
import { APP_ROUTES, LocalStorageKeys } from '../../../models/Enums';
import {
  EventIdentifier,
  EventMedia,
  EventMediaReqObject,
  EventMediaRequest,
} from '../../../models/EventDetailObject';
import { LoggedInUserDetails } from '../../../models/LoggedinUserDetails';
import { OtpComponent } from '../../../popups/otp/otp.component';
import { EventService } from '../../../services/event.service';
import { LocalstorageService } from '../../../services/localstorage.service';
import { ToastService } from '../../../services/toast.service';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-event-photos',
  standalone: true,
  imports: [SharedModule, ImageComponent],
  templateUrl: './event-photos.component.html',
  styleUrl: './event-photos.component.scss',
})
export class EventPhotosComponent implements OnInit {
  private _eventIdentifier!: EventIdentifier;

  @Input()
  set eventIdentifier(value: EventIdentifier) {
    if (value) {
      this._eventIdentifier = value;
      this.buildEventOverviewObject();
    }
  }

  get eventIdentifier(): EventIdentifier {
    return this._eventIdentifier;
  }

  eventPhotos!: EventMedia[];
  enableEventMediaSaveBtn: boolean = false;
  isValidMediaUrl: boolean = true;

  otpComponent!: ComponentRef<OtpComponent>;

  @ViewChild('otpContainer', { read: ViewContainerRef, static: true })
  otpContainer!: ViewContainerRef;

  constructor(
    private eventService: EventService,
    private localstorageService: LocalstorageService,
    private toastService: ToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getBlankEventPhotos();
  }

  removeAllEventPhotos() {
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    const loggedInUser: LoggedInUserDetails = this.localstorageService.getItem(
      LocalStorageKeys.LOGGED_IN_USER_DETAILS
    );
    this.otpComponent.instance.userName = loggedInUser.userName;
    this.otpComponent.instance.device = 'PHONE';
    this.otpComponent.instance.otpEmiter.subscribe((res) => {
      this.eventService
        .removeAllEventPhotos(
          this.eventIdentifier.id,
          res,
          this.eventIdentifier.organizer
        )
        .subscribe({
          next: (res: EventMedia[]) => {
            if (res.length) {
              this.eventPhotos = res;
              this.checkIsValidMediaUrl();
              this.toastService.showSuccessToast(
                'All event media are removed successfully'
              );
              this.router.navigate([
                `${APP_ROUTES.EVENT_DETAILS}/${this.eventIdentifier.id}/${this.eventIdentifier.organizer}`,
              ]);
            } else {
              this.toastService.showSuccessToast(
                'All event media are not removed successfully'
              );
            }
          },
          error: (err: string) => {
            this.otpComponent.instance.visible = false;
          },
          complete: () => {
            this.otpComponent.instance.visible = false;
            this.enableEventMediaSaveBtn = false;
          },
        });
    });
  }

  getBlankEventPhotos() {
    this.eventPhotos = [];
    for (let index = 0; index < 9; index++) {
      this.eventPhotos.push({
        mediaUrl: '',
        coverMedia: index === 0 ? true : false,
        mediaFile: new File([], ''),
        mediaIndex: index,
      });
    }
    this.checkIsValidMediaUrl();
  }

  getExistingEventPhotos() {
    this.eventService
      .getEventPhotos(this.eventIdentifier.id, this.eventIdentifier.organizer)
      .subscribe((res: EventMedia[]) => {
        this.eventPhotos = res;
        this.checkIsValidMediaUrl();
      });
  }

  editableEventMediaHandler(event: EventMedia) {
    this.eventPhotos[event.mediaIndex] = event;
    this.enableEventMediaSaveBtn = true;
  }

  saveEventPhotos() {
    if (this.enableEventMediaSaveBtn) {
      const files: File[] = [];
      const eventMediaReqList: EventMediaReqObject[] = [];
      this.eventPhotos.forEach((i) => {
        if (i.mediaFile) {
          files.push(i.mediaFile);
          eventMediaReqList.push({
            coverMedia: i.coverMedia,
            mediaIndex: i.mediaIndex,
            fileName: i.mediaFile.name,
            id: i.id,
          });
        }
      });
      if (eventMediaReqList.length && files.length) {
        this.loadOtpCompoment(files, eventMediaReqList);
      }
    }
  }

  cancelEventPhotos() {
    this.enableEventMediaSaveBtn = false;
  }

  private buildEventOverviewObject() {
    if (
      this.eventIdentifier.id !== 0 &&
      this.eventIdentifier.organizer !== 'new'
    ) {
      this.getExistingEventPhotos();
    } else {
      this.getBlankEventPhotos();
    }
  }

  private loadOtpCompoment(
    files: File[],
    eventMediaReqList: EventMediaReqObject[]
  ) {
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    const loggedInUser: LoggedInUserDetails = this.localstorageService.getItem(
      LocalStorageKeys.LOGGED_IN_USER_DETAILS
    );
    this.otpComponent.instance.userName = loggedInUser.userName;
    this.otpComponent.instance.device = 'PHONE';
    this.otpComponent.instance.otpEmiter.subscribe((res) => {
      const eventMediaRequest: EventMediaRequest = {
        eventId: this.eventIdentifier.id,
        otp: res,
        eventMediaReqObject: eventMediaReqList,
      };
      this.eventService.saveEventPhotos(files, eventMediaRequest).subscribe({
        next: (res: EventMedia[]) => {
          if (res.length) {
            this.eventPhotos = res;
            this.checkIsValidMediaUrl();
            this.toastService.showSuccessToast(
              'Event photos are added/updated successfully'
            );
            this.router.navigate([
              `${APP_ROUTES.EVENT_DETAILS}/${this.eventIdentifier.id}/${this.eventIdentifier.organizer}`,
            ]);
          } else {
            this.toastService.showSuccessToast(
              'Event overview are not added/updated successfully'
            );
          }
        },
        error: (err: string) => {
          this.otpComponent.instance.visible = false;
        },
        complete: () => {
          this.otpComponent.instance.visible = false;
          this.enableEventMediaSaveBtn = false;
        },
      });
    });
  }

  private checkIsValidMediaUrl() {
    this.isValidMediaUrl = this.eventPhotos.some((i) => i.mediaUrl?.length);
  }
}
