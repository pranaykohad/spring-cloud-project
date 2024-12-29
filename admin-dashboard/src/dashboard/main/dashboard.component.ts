import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs/internal/Subscription';
import { Msg } from '../../models/Enums';
import { InternalMsg } from '../../models/InternalMsg';
import { LoggedinUserDetails } from '../../models/SystemUserResponse';
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
  loggedinUserDetails!: LoggedinUserDetails;
  private subscription: Subscription = new Subscription();

  constructor(
    private systemUserAuthService: UserAuthService,
    private messageService: MessageService
  ) {}

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

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

  private getLoggedinUserDetails() {
    this.systemUserAuthService.getLoggedinUserDetails().subscribe({
      next: (res: LoggedinUserDetails) => {
        this.loggedinUserDetails = res;
      },
    });
  }
}
