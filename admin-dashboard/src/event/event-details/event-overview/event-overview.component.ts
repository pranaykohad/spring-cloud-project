import {
  Component,
  Input,
  OnInit,
  ViewChild,
  ViewContainerRef,
  ComponentRef,
} from '@angular/core';
import {
  EventIdentifier,
  EventOverview,
  EventOverviewError,
} from '../../../models/EventDetailObject';
import { EventService } from '../../../services/event.service';
import { ToastService } from '../../../services/toast.service';
import { SharedModule } from '../../../shared/shared.module';
import { OtpComponent } from '../../../popups/otp/otp.component';
import { LoggedInUserDetails } from '../../../models/LoggedinUserDetails';
import { LocalstorageService } from '../../../services/localstorage.service';
import { APP_ROUTES, LocalStorageKeys } from '../../../models/Enums';
import { Router } from '@angular/router';

@Component({
  selector: 'app-event-overview',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './event-overview.component.html',
  styleUrl: './event-overview.component.scss',
})
export class EventOverviewComponent implements OnInit {
  private _eventIdentifier!: EventIdentifier;

  otpComponent!: ComponentRef<OtpComponent>;

  @ViewChild('otpContainer', { read: ViewContainerRef, static: true })
  otpContainer!: ViewContainerRef;

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

  eventOverviewEdit!: EventOverview;
  eventOverviewError!: EventOverviewError;
  eventOverview!: EventOverview;
  enableEventSaveBtn: boolean = false;

  constructor(
    private eventService: EventService,
    private toastService: ToastService,
    private localstorageService: LocalstorageService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.initEventOverview();
  }

  clearEventDetailsValidation() {
    this.eventOverviewError = {
      eventTitle: '',
      eventDescription: '',
      userMinAge: '',
    };
    this.enableEventSaveBtn = true;
  }

  saveEventInfornmation() {
    if (this.validateEventDetails() && this.enableEventSaveBtn) {
      this.loadOtpCompoment();
    }
  }

  cancelEventDetailsChanges() {
    if (this.eventIdentifier.id === 0) {
      this.initEventOverview();
      this.clearError();
    } else {
      this.buildEventOverviewObject();
    }
    this.enableEventSaveBtn = false;
  }

  private validateEventDetails(): boolean {
    let isEventDetailsValid = true;
    if (this.eventOverviewEdit.eventTitle.length <= 5) {
      this.eventOverviewError.eventTitle = 'Event title is too short';
      isEventDetailsValid = false;
    }
    if (this.eventOverviewEdit.eventDescription.length <= 10) {
      this.eventOverviewError.eventDescription =
        'Event description is too short';
      isEventDetailsValid = false;
    }
    if (
      this.eventOverviewEdit.userMinAge <= 0 ||
      this.eventOverviewEdit.userMinAge >= 100
    ) {
      this.eventOverviewError.userMinAge = 'Invalid user minium age';
      isEventDetailsValid = false;
    }
    if (isEventDetailsValid) {
      this.clearError();
    }
    this.eventOverviewEdit.eventTitle =
      this.eventOverviewEdit.eventTitle.trim();
    this.eventOverviewEdit.eventDescription =
      this.eventOverviewEdit.eventDescription.trim();
    this.eventOverviewEdit.userMinAge;
    return isEventDetailsValid;
  }

  private clearError() {
    this.eventOverviewError = {
      eventTitle: '',
      eventDescription: '',
      userMinAge: '',
    };
  }

  private buildEventOverviewObject() {
    if (
      this.eventIdentifier.id !== 0 &&
      this.eventIdentifier.organizer !== 'new'
    ) {
      this.eventService
        .getEventOverview(
          this.eventIdentifier.id,
          this.eventIdentifier.organizer
        )
        .subscribe((res: EventOverview) => {
          this.eventOverview = res;
          this.eventOverviewEdit = JSON.parse(
            JSON.stringify(this.eventOverview)
          );
          const loggedInUser: LoggedInUserDetails =
            this.localstorageService.getItem(
              LocalStorageKeys.LOGGED_IN_USER_DETAILS
            );
          this.eventOverviewEdit.userName = loggedInUser.userName;
        });
    } else {
      this.initEventOverview();
    }
  }

  private initEventOverview() {
    const loggedInUser: LoggedInUserDetails = this.localstorageService.getItem(
      LocalStorageKeys.LOGGED_IN_USER_DETAILS
    );
    this.eventOverviewEdit = {
      userName: loggedInUser.userName,
      otp: '',
      eventTitle: '',
      eventDescription: '',
      organizer: this.eventIdentifier.organizer,
      userMinAge: 0,
      createdOn: '',
    };
    this.eventOverviewError = {
      eventTitle: '',
      eventDescription: '',
      userMinAge: '',
    };
  }

  private loadOtpCompoment() {
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    const loggedInUser: LoggedInUserDetails = this.localstorageService.getItem(
      LocalStorageKeys.LOGGED_IN_USER_DETAILS
    );
    this.otpComponent.instance.userName = loggedInUser.userName;
    this.otpComponent.instance.device = 'PHONE';
    this.otpComponent.instance.otpEmiter.subscribe((res) => {
      this.eventOverviewEdit.otp = res;
      this.eventService.saveEventOverview(this.eventOverviewEdit).subscribe({
        next: (res: number) => {
          this.toastService.showSuccessToast(
            'Event overview is added/updated successfully'
          );
          this.router.navigate([
            `${APP_ROUTES.EVENT_DETAILS}/${res}/${this.eventIdentifier.organizer}`,
          ]);
        },
        error: (err: string) => {
          this.otpComponent.instance.visible = false;
        },
        complete: () => {
          this.otpComponent.instance.visible = false;
          this.enableEventSaveBtn = false;
        },
      });
    });
  }
}
