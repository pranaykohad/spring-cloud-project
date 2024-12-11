import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { SystemUserAuthService } from '../../services/system-user-auth.service';
import { SystemUserLoginRequest } from '../../models/SystemUserLoginRequest';
import { LocalstorageService } from '../../services/localstorage.service';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { LocalStorageKeys, ToastType } from '../../models/Enums';
import { SystemUserResponse } from '../../models/SystemUserResponse';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent implements OnInit {
  systemUserLoginRequest: SystemUserLoginRequest = {
    userName: 'pranay',
    password: 'pranay',
  };
  rememberMe: boolean = false;
  systemUserResponse !: SystemUserResponse;

  constructor(
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {}

  login() {
    this.systemUserAuthService
      .userLogin(this.systemUserLoginRequest)
      .subscribe({
        next: (res: SystemUserResponse) => {
          this.systemUserResponse = res;
          this.localstorageService.setItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS, res);
          this.router.navigate(['']);
          this.toastService.showToast(
            '',
            `${this.systemUserResponse.displayName} logged in`,
            ToastType.SUCCESS
          );
        },
        error: (err) => {
          this.toastService.showToast('', err.error, ToastType.ERROR);
        },
      });
  }
}
