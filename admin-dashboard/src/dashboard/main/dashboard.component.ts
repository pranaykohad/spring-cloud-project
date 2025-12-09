import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { Msg } from '../../models/Enums';
import { InternalMsg } from '../../models/InternalMsg';
import { LoggedInUserDetails } from '../../models/LoggedinUserDetails';
import { NavbarComponent } from '../../navbar/navbar.component';
import { UserAuthService } from '../../services/user-auth.service';
import { SharedModule } from '../../shared/shared.module';
import { MessageService } from './../../behaviorSubject/message.service';

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
    private messageService: MessageService
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
        if (this.loggedInUserDetails.profilePic) {
          this.loggedInUserDetails.profilePicUrl = `data:image/jpeg;base64,${this.loggedInUserDetails.profilePic}`;
        }
      },
    });
  }
}
