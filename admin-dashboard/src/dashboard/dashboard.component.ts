import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SystemUserResponse } from '../models/SystemUserResponse';
import { NavbarComponent } from '../navbar/navbar.component';
import { LocalstorageService } from '../services/localstorage.service';
import { SystemUserAuthService } from '../services/system-user-auth.service';
import { ToastService } from '../services/toast.service';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SharedModule, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {
  systemUserResponse!: SystemUserResponse;

  constructor(
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.systemUserAuthService.getUserDetailsByUsername('pranay').subscribe({
      next: (res: SystemUserResponse) => {
        this.systemUserResponse = res;
      },
    });
  }
}
