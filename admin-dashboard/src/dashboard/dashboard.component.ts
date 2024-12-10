import { SystemUserAuthService } from '../services/system-user-auth.service';
import { LocalstorageService } from '../services/localstorage.service';
import { ToastService } from '../services/toast.service';
import { SystemUserResponse } from '../models/SystemUserResponse';
import { Router } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { NavbarComponent } from '../navbar/navbar.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [SharedModule, NavbarComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss',
})
export class DashboardComponent implements OnInit {

  systemUserResponse: SystemUserResponse = {
    jwt: '',
    email: '',
    phone: '',
    displayName: '',
  };

  constructor(
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {
    this.systemUserAuthService.testAPI('pranay').subscribe({
      next: (res: SystemUserResponse) => {
        this.systemUserResponse = res;
      },
    });
  }
}
