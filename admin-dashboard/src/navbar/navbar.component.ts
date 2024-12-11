import { LocalstorageService } from './../services/localstorage.service';
import { Component, ElementRef, Input, ViewChild } from '@angular/core';
import { SystemUserResponse } from '../models/SystemUserResponse';
import { SystemUserAuthService } from '../services/system-user-auth.service';
import { SharedModule } from '../shared/shared.module';
import { Router } from '@angular/router';
import { LocalStorageKeys } from '../models/Enums';
import { MenuItem } from 'primeng/api';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  @Input()
  systemUserResponse!: SystemUserResponse;
  urbanShowsLogo: string = 'assets/img/Urban Shows Logo.png';
  model: MenuItem[] = [
    {
      id: 'profile',
      label: 'Profile',
      icon: 'pi pi-user',
      command: () => {
        this.openProfile();
      },
    },
    {
      id: 'setting',
      label: 'Setting',
      icon: 'pi pi-cog',
      textAlign: 'center',
      command: () => {
        this.openSetting();
      },
    },
    {
      id: 'logout',
      label: 'Logout',
      icon: 'pi pi-sign-out',
      textAlign: 'center',
      command: () => {
        this.logout();
      },
    },
  ];

  constructor(
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router
  ) {}

  openProfile() {}

  openSetting() {}

  logout() {
    const loggedInUserDetails: SystemUserResponse =
      this.localstorageService.getItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS);
    if (loggedInUserDetails !== null) {
      this.systemUserAuthService
        .userLogout(loggedInUserDetails.jwt)
        .subscribe((res: boolean) => {
          if (res) {
            this.localstorageService.clear();
            this.router.navigate(['login']);
          }
        });
    }
  }
}
