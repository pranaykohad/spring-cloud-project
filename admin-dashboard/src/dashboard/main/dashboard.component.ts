import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { LocalStorageKeys, Msg } from '../../models/Enums';
import { InternalMsg } from '../../models/InternalMsg';
import { LoggedInUserDetails } from '../../models/LoggedinUserDetails';
import { NavbarComponent } from '../../navbar/navbar.component';
import { UserAuthService } from '../../services/user-auth.service';
import { SharedModule } from '../../shared/shared.module';
import { MessageService } from './../../behaviorSubject/message.service';
import { LocalstorageService } from '../../services/localstorage.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SharedModule, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit, OnDestroy {
  loggedInUserDetails!: LoggedInUserDetails;
  private subscription: Subscription = new Subscription();

  constructor(
    private systemUserAuthService: UserAuthService,
    private messageService: MessageService,
    private localstorageService: LocalstorageService
  ) {}

  ngOnInit(): void {
    this.getLoggedinUserDetails();
    this.subscription = this.messageService.currentMessage$.subscribe(
      (msg: InternalMsg) => {
        if (msg.msg === Msg.UPDATE_LOGGEDIN_USER_DETAILS) {
          this.getLoggedinUserDetails();
        }
      }
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  private getLoggedinUserDetails() {
    this.systemUserAuthService.getLoggedinUserDetails().subscribe({
      next: (res: LoggedInUserDetails) => {
        this.loggedInUserDetails = res;
      },
    });
  }
}
