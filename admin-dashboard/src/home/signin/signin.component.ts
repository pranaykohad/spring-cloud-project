import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import {
  SystemUserSigninRequest,
  SystemUserSigninRequestError,
} from '../../models/SystemUserSigninRequest';
import { LocalstorageService } from '../../services/localstorage.service';
import { SystemUserAuthService } from '../../services/system-user-auth.service';
import { ToastService } from '../../services/toast.service';
import { SharedModule } from '../../shared/shared.module';
import { ToastType } from '../../models/Enums';

@Component({
  selector: 'app-signin',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './signin.component.html',
  styleUrl: './signin.component.scss',
})
export class SigninComponent implements OnInit {
  rolelist: string[] = [
    'SYSTEM_USER',
    'ADMIN_USER',
    'SUPPORT_USER',
    'ORGANIZER_USER',
  ];
  selectedRole!: string;
  termsAndConditionsRead: boolean = false;
  signinReq: SystemUserSigninRequest = {
    userName: '',
    password: '',
    email: '',
    phone: '',
    displayName: '',
    confirmPassword: '',
    roles: [],
  };
  signinRequestError: SystemUserSigninRequestError = {
    userName: '',
    password: '',
    confirmPassword: '',
    email: '',
    phone: '',
    displayName: '',
    roles: '',
  };

  constructor(
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router,
    private toastService: ToastService
  ) {}

  ngOnInit(): void {}

  selectRole(roleValue: string) {
    this.signinReq.roles = [];
    if (roleValue !== null) {
      this.signinReq.roles.push(roleValue);
      this.clearValidation('ROLE');
    }
  }

  signin() {
    if (this.validateUser()) {
      this.systemUserAuthService.userSignin(this.signinReq).subscribe((res) => {
        if (res) {
          this.toastService.showSuccessToast(
            'Sign in request submitted successfully'
          );
          this.nagivateToLogin();
        }
      });
    }
  }

  clearValidation(type?: string) {
    switch (type) {
      case 'USERNAME':
        this.signinRequestError.userName = '';
        break;
      case 'PASSWORD':
        this.signinRequestError.password = '';
        break;
      case 'CONFIRMPASSWORD':
        this.signinRequestError.confirmPassword = '';
        break;
      case 'EMAIL':
        this.signinRequestError.email = '';
        break;
      case 'DISPLAYNAME':
        this.signinRequestError.displayName = '';
        break;
      case 'ROLE':
        this.signinRequestError.roles = '';
        break;
      case 'PHONE':
        this.signinRequestError.phone = '';
        break;
      default:
        this.signinRequestError.userName = '';
        this.signinRequestError.password = '';
        this.signinRequestError.confirmPassword = '';
        this.signinRequestError.email = '';
        this.signinRequestError.displayName = '';
        this.signinRequestError.roles = '';
        this.signinRequestError.phone = '';
    }
  }

  validateUser(): boolean {
    let isUserValid = true;
    if (!this.signinReq.userName.length) {
      this.signinRequestError.userName = 'Username cannot be blank!';
      isUserValid = false;
    }
    if (!this.signinReq.password.length) {
      this.signinRequestError.password = 'Password cannot be blank!';
      isUserValid = false;
    }
    if (!this.signinReq.confirmPassword.length) {
      this.signinRequestError.confirmPassword =
        'Confirm password cannot be blank!';
      isUserValid = false;
    }
    if (!this.signinReq.displayName.length) {
      this.signinRequestError.displayName = 'Display name cannot be blank!';
      isUserValid = false;
    }
    if (!this.signinReq.phone.length) {
      this.signinRequestError.phone = 'Phone cannot be blank!';
      isUserValid = false;
    }
    if (!this.signinReq.email.length) {
      this.signinRequestError.email = 'Email cannot be blank!';
      isUserValid = false;
    }
    if (!this.signinReq.roles.length) {
      this.signinRequestError.roles = 'Role cannot be blank!';
      isUserValid = false;
    }
    if (this.signinReq.password !== this.signinReq.confirmPassword) {
      this.signinRequestError.confirmPassword =
        'Password and confirm password must be same';
      isUserValid = false;
    }
    if (!this.isValidPhoneNumber()) {
      this.signinRequestError.phone = 'Invalid phone number!';
      isUserValid = false;
    }
    if (!this.isValidEmail()) {
      this.signinRequestError.email = 'Invalid email!';
      isUserValid = false;
    }
    if (isUserValid) {
      this.clearValidation();
    }
    this.signinReq.displayName = this.signinReq.displayName.trim();
    this.signinReq.password = this.signinReq.password.trim();
    this.signinReq.confirmPassword = this.signinReq.confirmPassword.trim();
    this.signinReq.email = this.signinReq.email.trim();
    this.signinReq.phone = this.signinReq.phone.trim();
    this.signinReq.userName = this.signinReq.userName.trim();
    return isUserValid;
  }

  nagivateToLogin() {
    this.router.navigate(['login']);
  }

  private isValidPhoneNumber(): boolean {
    return /^[0-9]{10}$/.test(this.signinReq.phone);
  }

  private isValidEmail(): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(this.signinReq.email);
  }
}
