import {
  Component,
  ComponentRef,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { MessageService } from '../behaviorSubject/message.service';
import {
  UserBasicDetails,
  UserBasicDetailsError,
  UserSecuredDetailsError,
  UserSecuredDetailsReq,
  UserSecuredDetailsRes,
} from '../models/UserUpdateRequest';
import { OtpComponent } from '../popups/otp/otp.component';
import { ToastService } from '../services/toast.service';
import { UserAuthService } from '../services/user-auth.service';
import { UserService } from '../services/user.service';
import { SharedModule } from '../shared/shared.module';
import { EditableUserSecuredDetailsRes } from './../models/UserUpdateRequest';
import { LocalstorageService } from './../services/localstorage.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [SharedModule],
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

  userSecuredDetailsRes!: UserSecuredDetailsRes;

  editableUserSecuredDetailsRes!: EditableUserSecuredDetailsRes;

  userSecuredDetailsReq: UserSecuredDetailsReq = {
    userName: '',
    password: '',
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
    this.userSecuredDetailsReq.password = '';
    this.newUserSecuredDetailsError.password = '';
    this.enableSecuredUpdateBtn = true;
  }

  clearConfirmPasswordField() {
    this.editableUserSecuredDetailsRes.confirmPassword = '';
    this.newUserSecuredDetailsError.confirmPassword = '';
    this.enableBasicUpdateBtn = true;
  }

  validateSecuredDetails(): boolean {
    let isSecuredDetailsValid = true;
    if (!this.editableUserSecuredDetailsRes.password.length) {
      this.newUserSecuredDetailsError.password = 'Password cannot be blank!';
      isSecuredDetailsValid = false;
    }
    if (!this.editableUserSecuredDetailsRes.confirmPassword.length) {
      this.newUserSecuredDetailsError.confirmPassword =
        'Confirm password cannot be blank!';
      isSecuredDetailsValid = false;
    }
    if (
      this.editableUserSecuredDetailsRes.password !==
      this.editableUserSecuredDetailsRes.confirmPassword
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
    this.editableUserSecuredDetailsRes.password =
      this.editableUserSecuredDetailsRes.password.trim();
    this.editableUserSecuredDetailsRes.confirmPassword =
      this.editableUserSecuredDetailsRes.confirmPassword.trim();
    this.editableUserSecuredDetailsRes.phone =
      this.editableUserSecuredDetailsRes.phone.trim();
    this.editableUserSecuredDetailsRes.email =
      this.editableUserSecuredDetailsRes.email.trim();
    return isSecuredDetailsValid;
  }

  private loadOtpCompoment() {
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    this.otpComponent.instance.otpEmiter.subscribe((res) => {
      this.editableUserSecuredDetailsRes.otp = res;
      this.messageService.sendMessage('ENABLE_LOADER');
      const finalObject = {
        userName: this.editableUserSecuredDetailsRes.userName,
        otp: this.editableUserSecuredDetailsRes.otp,
        password: this.editableUserSecuredDetailsRes.password,
        phone: this.editableUserSecuredDetailsRes.phone,
        email: this.editableUserSecuredDetailsRes.email,
      };
      this.userService
        .updateUserSecuredDetails(finalObject)
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
            this.messageService.sendMessage('DISABLE_LOADER');
          },
          complete: () => {
            this.messageService.sendMessage('DISABLE_LOADER');
          },
        });
      this.cancelSecuredDetailsUpdate();
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
      .subscribe((res: UserSecuredDetailsRes) => {
        this.userSecuredDetailsRes = res;
        this.editableUserSecuredDetailsRes = {
          userName: res.userName,
          password: res.password,
          confirmPassword: res.password,
          phone: res.phone,
          email: res.email,
          otp: '',
        };
      });
  }

  private isValidPhoneNumber(): boolean {
    return /^[0-9]{10}$/.test(this.editableUserSecuredDetailsRes.phone);
  }

  private isValidEmail(): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(this.editableUserSecuredDetailsRes.email);
  }
}
