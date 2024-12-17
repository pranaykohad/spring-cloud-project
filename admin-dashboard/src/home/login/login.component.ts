import { Component, OnInit } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { UserAuthService } from '../../services/user-auth.service';
import { SystemUserLoginRequest } from '../../models/SystemUserLoginRequest';
import { LocalstorageService } from '../../services/localstorage.service';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { LocalStorageKeys, ToastType } from '../../models/Enums';
import { LoggedinUserDetails } from '../../models/SystemUserResponse';

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
  loggedinUserDetails!: LoggedinUserDetails;
  enableLoginButton: boolean = false;

  constructor(
    private systemUserAuthService: UserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService
  ) {

  }

  ngOnInit(): void {
  }

  validate() {
    this.enableLoginButton =
      this.systemUserLoginRequest.userName.length !== 0 &&
      this.systemUserLoginRequest.password.length !== 0;
  }

  login() {
    this.systemUserLoginRequest.userName =
      this.systemUserLoginRequest.userName.trim();
    this.systemUserLoginRequest.password =
      this.systemUserLoginRequest.password.trim();
    this.systemUserAuthService
      .userLogin(this.systemUserLoginRequest)
      .subscribe({
        next: (res: LoggedinUserDetails) => {
          this.loggedinUserDetails = res;
          this.localstorageService.setItem(
            LocalStorageKeys.LOGGED_IN_USER_DETAILS,
            res
          );
          this.router.navigate(['']);
          this.toastService.showSuccessToast(
            `${this.loggedinUserDetails.displayName} logged in`
          );
        },
        error: (err) => {
          this.toastService.showErrorToast(err.error);
        },
      });
  }

  nagivateToSignin() {
    this.router.navigate(['signin']);
  }
}
