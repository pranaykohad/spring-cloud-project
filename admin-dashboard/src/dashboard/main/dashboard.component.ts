import { MessageService } from './../../behaviorSubject/message.service';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoggedinUserDetails } from '../../models/SystemUserResponse';
import { NavbarComponent } from '../../navbar/navbar.component';
import { LocalstorageService } from '../../services/localstorage.service';
import { UserAuthService } from '../../services/user-auth.service';
import { ToastService } from '../../services/toast.service';
import { SharedModule } from '../../shared/shared.module';
import { Subscription } from 'rxjs/internal/Subscription';

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
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService,
    private messageService: MessageService
  ) {}

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  ngOnInit(): void {
    this.getLoggedinUserDetails();
    this.subscription = this.messageService.currentMessage$.subscribe((msg) => {
      if (msg === 'UPDATE_LOGGEDIN_USER_DETAILS') {
        this.getLoggedinUserDetails();
      }
    });
  }

  private getLoggedinUserDetails() {
    this.systemUserAuthService.getLoggedinUserDetails().subscribe({
      next: (res: LoggedinUserDetails) => {
        this.loggedinUserDetails = res;
      },
    });
  }
}
