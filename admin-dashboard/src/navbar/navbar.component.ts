import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { MegaMenuItem, MenuItem } from 'primeng/api';
import { LocalStorageKeys } from '../models/Enums';
import { LoggedinUserDetails } from '../models/LoggedinUserDetails';
import { UserAuthService } from '../services/user-auth.service';
import { SharedModule } from '../shared/shared.module';
import { LocalstorageService } from './../services/localstorage.service';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  @Input()
  loggedinUserDetails!: LoggedinUserDetails;
  urbanShowsLogo!: string;
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

  navbarModel: MegaMenuItem[] = [
    {
      label: 'User',
      icon: 'pi pi-users',
      items: [
        [
          {
            label: 'User Section',
            items: [
              {
                label: 'User',
                command: () => {
                  this.openUserSection();
                },
              },
            ],
          },
        ],
      ],
    },
    {
      label: 'Events',
      icon: 'pi pi-calendar-plus',
      items: [
        [
          {
            label: 'Event Section',
            items: [
              {
                label: 'Event',
                command: () => {
                  this.openEventSection();
                },
              },
            ],
          },
        ],
      ],
    },
    {
      label: 'Settings',
      icon: 'pi pi-fw pi-cog',
      items: [
        [
          {
            label: 'Setting 1',
            items: [{ label: 'Setting 1.1' }, { label: 'Setting 1.2' }],
          },
          {
            label: 'Setting 2',
            items: [{ label: 'Setting 2.1' }, { label: 'Setting 2.2' }],
          },
          {
            label: 'Setting 3',
            items: [{ label: 'Setting 3.1' }, { label: 'Setting 3.2' }],
          },
        ],
        [
          {
            label: 'Technology 4',
            items: [{ label: 'Setting 4.1' }, { label: 'Setting 4.2' }],
          },
        ],
      ],
    },
  ];

  constructor(
    private systemUserAuthService: UserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router
  ) {}

  openProfile() {
    if (this.loggedinUserDetails.userName !== null) {
      this.router.navigate(['profile', this.loggedinUserDetails.userName]);
    }
  }

  navigateToHome() {
    this.router.navigate(['']);
  }

  openSetting() {
    this.router.navigate(['setting']);
  }

  openUserSection() {
    this.router.navigate(['']);
  }

  openEventSection() {
    this.router.navigate(['event']);
  }

  logout() {
    const loggedinUserDetails: LoggedinUserDetails =
      this.localstorageService.getItem(LocalStorageKeys.LOGGED_IN_USER_DETAILS);
    if (loggedinUserDetails !== null) {
      this.systemUserAuthService
        .userLogout(loggedinUserDetails.jwt)
        .subscribe((res: boolean) => {
          if (res) {
            const inMemoryUserName = this.localstorageService.getItem(
              LocalStorageKeys.INMEMORY_USERNAME
            );
            this.localstorageService.clear();
            this.localstorageService.setItem(
              LocalStorageKeys.INMEMORY_USERNAME,
              inMemoryUserName
            );
            this.router.navigate(['login']);
          }
        });
    }
  }
}
