import { Component, OnInit } from '@angular/core';
import { MessageService } from '../behaviorSubject/message.service';
import { ToastType } from '../models/Enums';
import {
  UserBasicDetails,
  UserSecuredDetails,
} from '../models/UserUpdateRequest';
import { SystemUserAuthService } from '../services/system-user-auth.service';
import { ToastService } from '../services/toast.service';
import { SharedModule } from '../shared/shared.module';
import { LocalstorageService } from './../services/localstorage.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss',
})
export class ProfileComponent implements OnInit {
  userBasicDetails!: UserBasicDetails;
  newUserBasicDetails: UserBasicDetails = {
    userName: '',
    profilePicUrl: '',
    displayName: '',
    profilePicFile: new File([], ''),
  };
  userSecuredDetails!: UserSecuredDetails;
  newUserSecuredDetails: UserSecuredDetails = {
    userName: '',
    otp: '',
    password: '',
    phone: '',
    email: '',
  };
  enableBasicUpdateBtn: boolean = false;
  enableSecuredUpdateBtn: boolean = false;

  constructor(
    private toastService: ToastService,
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService,
    private messageService: MessageService
  ) {}

  ngOnInit(): void {
    this.getUserBasicDetails();
    this.getUserSecuredDetails();
  }

  updateBasicDetails() {
    this.systemUserAuthService
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
        `${this.userBasicDetails.displayName}-profile-pic.${file.name
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

  validateBasicDetails(value: string, type: string) {
    switch (type) {
      case 'DISPLAYNAME':
        this.newUserBasicDetails.displayName = value.trim();
        this.enableBasicUpdateBtn = true;
        break;
    }
  }

  updateSecuredDetails() {}

  cancelBasicDetailsUpdate(value: string) {
    this.getUserBasicDetails();
    this.newUserBasicDetails.profilePicUrl = '';
    this.newUserBasicDetails.profilePicFile = new File([], '');
    value = '';
    this.enableBasicUpdateBtn = false;
  }

  cancelSecuredDetailsUpdate() {
    this.getUserSecuredDetails();
    this.enableSecuredUpdateBtn = false;
  }

  private getUserBasicDetails() {
    this.systemUserAuthService
      .getUserBasicDetails()
      .subscribe((res: UserBasicDetails) => {
        this.userBasicDetails = res;
        this.newUserBasicDetails.userName = this.userBasicDetails.userName;
      });
  }

  private getUserSecuredDetails() {
    this.systemUserAuthService
      .getUserBasicDetails()
      .subscribe((res: UserBasicDetails) => {
        this.userBasicDetails = res;
        this.newUserBasicDetails.userName = this.userBasicDetails.userName;
      });
  }
}
