import {
  Component,
  ComponentRef,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { MessageService } from '../behaviorSubject/message.service';
import { ToastType } from '../models/Enums';
import {
  UserBasicDetails,
  UserBasicDetailsError,
  UserSecuredDetails,
  UserSecuredDetailsError,
} from '../models/UserUpdateRequest';
import { UserAuthService } from '../services/user-auth.service';
import { ToastService } from '../services/toast.service';
import { SharedModule } from '../shared/shared.module';
import { LocalstorageService } from './../services/localstorage.service';
import { OtpComponent } from '../popups/otp/otp.component';
import { UserService } from '../services/user.service';
import { error } from 'console';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [SharedModule, OtpComponent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  oldUserBasicDetails!: UserBasicDetails;

  newUserBasicDetails: UserBasicDetails = {
    userName: '',
    profilePicUrl: '',
    displayName: '',
    profilePicFile: new File([], ''),
  };

  oldUserSecuredDetails!: UserSecuredDetails;

  newUserSecuredDetails: UserSecuredDetails = {
    userName: '',
    password: '',
    confirmPassword: '',
    phone: '',
    email: '',
    otp: '',
  };

  userBasicDetailsError: UserBasicDetailsError = {
    displayName: '',
  };

  newUserSecuredDetailsError: UserSecuredDetailsError = {
    password: '',
    confirmPassword: '',
    phone: '',
    email: '',
  };

  enableBasicUpdateBtn: boolean = false;

  enableSecuredUpdateBtn: boolean = false;

  @ViewChild('otpContainer', { read: ViewContainerRef, static: true })
  otpContainer!: ViewContainerRef;
  otpComponent!: ComponentRef<OtpComponent>;

  constructor(
    private toastService: ToastService,
    private systemUserAuthService: UserAuthService,
    private localstorageService: LocalstorageService,
    private messageService: MessageService,
    private userService: UserService
  ) {}

  ngOnInit(): void {
    this.getUserBasicDetails();
    this.getUserSecuredDetails();
  }

  updateBasicDetails() {
    if (this.validateBasicDetails()) {
      this.userService
        .updateUserBasicDetails(this.newUserBasicDetails)
        .subscribe((res: Boolean) => {
          if (res) {
            this.toastService.showSuccessToast(
              'User basic details are updated successfully'
            );
            this.messageService.sendMessage('UPDATE_LOGGEDIN_USER_DETAILS');
            this.enableBasicUpdateBtn = false;
            this.newUserBasicDetails = {
              userName: '',
              profilePicUrl: '',
              displayName: '',
              profilePicFile: new File([], ''),
            };
            this.getUserBasicDetails();
          }
        });
    }
  }

  updateSecuredDetails() {
    if (this.validateSecuredDetails()) {
      this.loadOtpCompoment();
    }
  }

  onUpload(event: Event) {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length == 1) {
      const file = input.files[0];

      if (!file.type.startsWith('image/')) {
        this.toastService.showErrorToast('Only image files are allowed');
        return;
      }

      const maxSize = 5 * 1024 * 1024; // 5 MB
      if (file.size > maxSize) {
        this.toastService.showErrorToast('File size exceeds 5 MB');
        return;
      }

      this.newUserBasicDetails.profilePicFile = new File(
        [file],
        `${this.oldUserBasicDetails.displayName}-profile-pic.${file.name
          .split('.')
          .pop()}`,
        {
          type: file.type,
        }
      );
      this.enableBasicUpdateBtn = true;

      const reader = new FileReader();
      reader.onload = () => {
        this.newUserBasicDetails.profilePicUrl = reader.result as string;
      };
      reader.readAsDataURL(file);
    }
  }

  clearBasicDetailsValidation() {
    this.userBasicDetailsError.displayName = '';
    this.enableBasicUpdateBtn = true;
  }

  clearSecuredDetailsValidation(type?: string) {
    switch (type) {
      case 'PASSWORD':
        this.newUserSecuredDetailsError.password = '';
        break;
      case 'CONFIRMPASSWORD':
        this.newUserSecuredDetailsError.confirmPassword = '';
        break;
      case 'EMAIL':
        this.newUserSecuredDetailsError.email = '';
        break;
      case 'PHONE':
        this.newUserSecuredDetailsError.phone = '';
        break;
      default:
        this.newUserSecuredDetailsError.password = '';
        this.newUserSecuredDetailsError.confirmPassword = '';
        this.newUserSecuredDetailsError.email = '';
        this.newUserSecuredDetailsError.phone = '';
    }
    this.enableSecuredUpdateBtn = true;
  }

  validateBasicDetails(): boolean {
    let isBasicDetailsValid = true;
    if (this.newUserBasicDetails.displayName.length <= 3) {
      this.userBasicDetailsError.displayName = 'Display name is too short!';
      isBasicDetailsValid = false;
    }
    if (isBasicDetailsValid) {
      this.clearBasicDetailsValidation();
    }
    this.newUserBasicDetails.displayName =
      this.newUserBasicDetails.displayName.trim();
    return isBasicDetailsValid;
  }

  clearPasswordField() {
    this.newUserSecuredDetails.password = '';
    this.newUserSecuredDetailsError.password = '';
    this.enableSecuredUpdateBtn = true;
  }

  clearConfirmPasswordField() {
    this.newUserSecuredDetails.confirmPassword = '';
    this.newUserSecuredDetailsError.confirmPassword = '';
    this.enableBasicUpdateBtn = true;
  }

  validateSecuredDetails(): boolean {
    let isSecuredDetailsValid = true;
    if (!this.newUserSecuredDetails.password.length) {
      this.newUserSecuredDetailsError.password = 'Password cannot be blank!';
      isSecuredDetailsValid = false;
    }
    if (!this.newUserSecuredDetails.confirmPassword.length) {
      this.newUserSecuredDetailsError.confirmPassword =
        'Confirm password cannot be blank!';
      isSecuredDetailsValid = false;
    }
    if (
      this.newUserSecuredDetails.password !==
      this.newUserSecuredDetails.confirmPassword
    ) {
      this.newUserSecuredDetailsError.confirmPassword =
        'Password and confirm password must be same';
      isSecuredDetailsValid = false;
    }
    if (!this.isValidPhoneNumber()) {
      this.newUserSecuredDetailsError.phone = 'Invalid phone number!';
      isSecuredDetailsValid = false;
    }
    if (!this.isValidEmail()) {
      this.newUserSecuredDetailsError.email = 'Invalid email!';
      isSecuredDetailsValid = false;
    }
    if (isSecuredDetailsValid) {
      this.clearSecuredDetailsValidation();
    }
    this.newUserSecuredDetails.password =
      this.newUserSecuredDetails.password.trim();
    this.newUserSecuredDetails.confirmPassword =
      this.newUserSecuredDetails.confirmPassword.trim();
    this.newUserSecuredDetails.phone = this.newUserSecuredDetails.phone.trim();
    this.newUserSecuredDetails.email = this.newUserSecuredDetails.email.trim();
    return isSecuredDetailsValid;
  }

  private loadOtpCompoment() {
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    this.otpComponent.instance.otpEmiter.subscribe((res) => {
      this.newUserSecuredDetails.otp = res;
      this.userService
        .updateUserSecuredDetails(this.newUserSecuredDetails)
        .subscribe({
          next: (res: Boolean) => {
            if (res) {
              this.toastService.showSuccessToast(
                'Secured details are updated successfully'
              );
              this.otpComponent.instance.visible = false;
            }
          },
          error: (err: string) => {
            this.toastService.showErrorToast('otp is invalid or expired');
          },
        });
    });
  }

  cancelBasicDetailsUpdate(value: string) {
    this.getUserBasicDetails();
    this.newUserBasicDetails.profilePicUrl = '';
    this.newUserBasicDetails.profilePicFile = new File([], '');
    value = '';
    this.enableBasicUpdateBtn = false;
  }

  cancelSecuredDetailsUpdate() {
    this.getUserSecuredDetails();
    this.clearSecuredDetailsValidation();
    this.enableSecuredUpdateBtn = false;
  }

  private getUserBasicDetails() {
    this.userService
      .getUserBasicDetails()
      .subscribe((res: UserBasicDetails) => {
        this.oldUserBasicDetails = res;
        this.newUserBasicDetails = this.oldUserBasicDetails;
      });
  }

  private getUserSecuredDetails() {
    this.userService
      .getUserSecuredDetails()
      .subscribe((res: UserSecuredDetails) => {
        this.oldUserSecuredDetails = res;
        this.newUserSecuredDetails = this.oldUserSecuredDetails;
        this.newUserSecuredDetails.confirmPassword =
          this.newUserSecuredDetails.password;
      });
  }

  private isValidPhoneNumber(): boolean {
    return /^[0-9]{10}$/.test(this.newUserSecuredDetails.phone);
  }

  private isValidEmail(): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(this.newUserSecuredDetails.email);
  }
}
