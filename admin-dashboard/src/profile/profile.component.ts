import { LocalstorageService } from './../services/localstorage.service';
import {
  Component,
  ComponentRef,
  OnInit,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from '../behaviorSubject/message.service';
import { LocalStorageKeys, Msg } from '../models/Enums';
import { Status } from '../models/Status';
import {
  UserBasicDetails,
  UserBasicDetailsError,
  UserSecuredDetailsError,
  UserSecuredDetailsReq,
  UserSecuredDetailsRes,
} from '../models/UserUpdateRequest';
import { OtpComponent } from '../popups/otp/otp.component';
import { ToastService } from '../services/toast.service';
import { UserService } from '../services/user.service';
import { SharedModule } from '../shared/shared.module';
import { EditableUserSecuredDetailsRes } from './../models/UserUpdateRequest';
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  statusList: Status[] = [
    {
      name: 'ACTIVE',
      value: 'ACTIVE',
    },
    {
      name: 'INACTIVE',
      value: 'INACTIVE',
    },
  ];
  newUserBasicDetails: UserBasicDetails = {
    userName: '',
    profilePicUrl: '',
    displayName: '',
    profilePicFile: new File([], ''),
    createdAt: '',
  };
  userSecuredDetailsReq: UserSecuredDetailsReq = {
    userName: '',
    password: '',
    phone: '',
    email: '',
    otp: '',
    status: '',
    phoneValidated: false,
    emailValidated: false,
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
  status: boolean = false;
  blockSelfChanges: boolean = true;
  userPhoneOtp: string = '';
  userEmailOtp: string = '';

  userName!: string;
  loggedInUser!: LoggedInUserDetails;
  userSecuredDetails!: UserSecuredDetailsRes;
  editSecuredDetails!: EditableUserSecuredDetailsRes;
  oldUserBasicDetails!: UserBasicDetails;
  otpComponent!: ComponentRef<OtpComponent>;

  @ViewChild('otpContainer', { read: ViewContainerRef, static: true })
  otpContainer!: ViewContainerRef;

  constructor(
    private toastService: ToastService,
    private messageService: MessageService,
    private userService: UserService,
    private activatedRoute: ActivatedRoute,
    private localstorageService: LocalstorageService
  ) {}

  ngOnInit(): void {
    this.userName = this.activatedRoute.snapshot.params['userName'];
    this.loggedInUser = this.localstorageService.getItem(
      LocalStorageKeys.LOGGED_IN_USER_DETAILS
    );
    this.blockSelfChanges = this.loggedInUser.userName === this.userName;
    this.getUserBasicDetails();
    this.getUserSecuredDetails();
  }

  onStatusChenge(value: boolean) {
    if (this.blockSelfChanges) {
      return;
    }
    this.status = value;
    this.editSecuredDetails.status = value ? 'ACTIVE' : 'INACTIVE';
    this.enableSecuredUpdateBtn = true;
  }

  updateBasicDetails() {
    if (this.validateBasicDetails() && this.enableBasicUpdateBtn) {
      this.userService
        .updateBasicDetails(this.newUserBasicDetails)
        .subscribe((res: Boolean) => {
          if (res) {
            this.toastService.showSuccessToast(
              'User basic details are updated successfully'
            );
            this.messageService.sendMessage({
              msg: Msg.UPDATE_LOGGEDIN_USER_DETAILS,
              value: null,
            });
            this.enableBasicUpdateBtn = false;
            this.newUserBasicDetails = {
              userName: '',
              profilePicUrl: '',
              displayName: '',
              profilePicFile: new File([], ''),
              createdAt: '',
            };
            this.getUserBasicDetails();
          }
        });
    }
  }

  updateSecuredDetails() {
    if (this.validateSecuredDetails() && this.enableSecuredUpdateBtn) {
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
        `${this.oldUserBasicDetails.userName}-profile-pic.${file.name
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
    this.editSecuredDetails.password = '';
    this.newUserSecuredDetailsError.password = '';
    this.enableSecuredUpdateBtn = true;
  }

  clearConfirmPasswordField() {
    this.editSecuredDetails.confirmPassword = '';
    this.newUserSecuredDetailsError.confirmPassword = '';
    this.enableBasicUpdateBtn = true;
  }

  validateSecuredDetails(): boolean {
    let isSecuredDetailsValid = true;
    if (!this.editSecuredDetails.password.length) {
      this.newUserSecuredDetailsError.password = 'Password cannot be blank!';
      isSecuredDetailsValid = false;
    }
    if (!this.editSecuredDetails.confirmPassword.length) {
      this.newUserSecuredDetailsError.confirmPassword =
        'Confirm password cannot be blank!';
      isSecuredDetailsValid = false;
    }
    if (
      this.editSecuredDetails.password !==
      this.editSecuredDetails.confirmPassword
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
    this.editSecuredDetails.password = this.editSecuredDetails.password.trim();
    this.editSecuredDetails.confirmPassword =
      this.editSecuredDetails.confirmPassword.trim();
    this.editSecuredDetails.phone = this.editSecuredDetails.phone.trim();
    this.editSecuredDetails.email = this.editSecuredDetails.email.trim();
    return isSecuredDetailsValid;
  }

  loadOtpForActivation(device: string) {
    if (
      (device === 'EMAIL' && this.editSecuredDetails.emailValidated) ||
      (device === 'PHONE' && this.editSecuredDetails.phoneValidated)
    ) {
      return;
    }
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    this.otpComponent.instance.userName = this.userName;
    this.otpComponent.instance.device = device;
    this.otpComponent.instance.otpEmiter.subscribe((otp) => {
      this.userService.userActivation(this.userName, otp).subscribe((res) => {
        if (device === 'EMAIL') {
          this.editSecuredDetails.emailValidated = true;
          this.toastService.showSuccessToast("Opt is send on client's email");
        } else {
          this.editSecuredDetails.phoneValidated = true;
          this.toastService.showSuccessToast("Opt is send on client's phone");
        }
        this.enableSecuredUpdateBtn = true;
        this.otpComponent.instance.visible = false;
      });
    });
  }

  private loadOtpCompoment() {
    this.otpContainer.clear();
    this.otpComponent = this.otpContainer.createComponent(OtpComponent);
    this.otpComponent.instance.visible = true;
    this.otpComponent.instance.userName = this.loggedInUser.userName;
    this.otpComponent.instance.device = 'PHONE';
    this.otpComponent.instance.otpEmiter.subscribe((res) => {
      this.editSecuredDetails.otp = res;
      this.messageService.enableLoader();
      let finalObject = {
        userName: this.editSecuredDetails.userName,
        otp: this.editSecuredDetails.otp,
        password: this.editSecuredDetails.password,
        phone: this.editSecuredDetails.phone,
        email: this.editSecuredDetails.email,
        status: this.editSecuredDetails.status,
        phoneValidated: this.editSecuredDetails.phoneValidated,
        emailValidated: this.editSecuredDetails.emailValidated,
      };
      finalObject.password =
        this.userSecuredDetails.password !== this.editSecuredDetails.password
          ? this.editSecuredDetails.password.trim()
          : '';
      this.userService.updateSecuredDetails(finalObject).subscribe({
        next: (res: Boolean) => {
          if (res) {
            this.toastService.showSuccessToast(
              'Secured details are updated successfully'
            );
            this.getUserSecuredDetails();
            this.otpComponent.instance.visible = false;
          }
        },
        error: (err: string) => {
          this.otpComponent.instance.visible = false;
        },
      });
      this.enableSecuredUpdateBtn = false;
    });
  }

  cancelBasicDetailsUpdate() {
    this.getUserBasicDetails();
    this.newUserBasicDetails.profilePicUrl = '';
    this.newUserBasicDetails.profilePicFile = new File([], '');
    this.enableBasicUpdateBtn = false;
  }

  cancelSecuredDetailsUpdate() {
    this.cancelBasicDetailsUpdate();
    this.cancelSecuredDetailsUpdate();
    this.enableSecuredUpdateBtn = false;
  }

  syncDetails() {
    this.getUserBasicDetails();
    this.getUserSecuredDetails();
    this.enableBasicUpdateBtn = false;
    this.enableSecuredUpdateBtn = false;
  }

  private getUserBasicDetails() {
    this.userService
      .getUserBasicDetails(this.userName)
      .subscribe((res: UserBasicDetails) => {
        this.oldUserBasicDetails = res;
        this.newUserBasicDetails = this.oldUserBasicDetails;
      });
  }

  private getUserSecuredDetails() {
    this.userService
      .getUserSecuredDetails(this.userName)
      .subscribe((res: UserSecuredDetailsRes) => {
        this.userSecuredDetails = res;
        this.editSecuredDetails = {
          userName: res.userName,
          password: res.password,
          confirmPassword: res.password,
          phone: res.phone,
          email: res.email,
          status: res.status,
          otp: '',
          phoneValidated: res.phoneValidated,
          emailValidated: res.emailValidated,
        };
        this.status = res.status === 'ACTIVE';
      });
  }

  private isValidPhoneNumber(): boolean {
    return /^[0-9]{10}$/.test(this.editSecuredDetails.phone);
  }

  private isValidEmail(): boolean {
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    return emailRegex.test(this.editSecuredDetails.email);
  }
}
