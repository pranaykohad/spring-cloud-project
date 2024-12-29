import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { MegaMenuItem, MenuItem } from 'primeng/api';
import { LocalStorageKeys } from '../models/Enums';
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';
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
  set loggedInUserDetails(value: LoggedInUserDetails) {
    this._loggedInUserDetails = value;
    if (value) {
      this.loggedInUserRoles = new Set(value.roles);
      this.setMegaPanel();
    }
  }

  private setMegaPanel() {
    this.navbarModel = [
      {
        label: 'User',
        icon: 'pi pi-users',
        items: [
          [
            {
              label: 'User Section',
              items: [
                {
                  label: 'User List',
                  visible: this.isSectionVisible('user-list'),
                  command: () => {
                    this.openUserSection();
                  },
                },
                {
                  label: 'Add Security Person',
                  visible: this.isSectionVisible('add-security'),
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
    ];
  }

  get loggedInUserDetails(): LoggedInUserDetails {
    if (this._loggedInUserDetails) {
      this.loggedInUserRoles = new Set(this._loggedInUserDetails.roles);
    }
    return this._loggedInUserDetails;
  }

  loggedInUserRoles: Set<String> = new Set<String>();

  urbanShowsLogo!: string;
  navbarModel!: MegaMenuItem[];

  private _loggedInUserDetails!: LoggedInUserDetails;

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
    private systemUserAuthService: UserAuthService,
    private localstorageService: LocalstorageService,
    private router: Router
  ) {}

  private isSectionVisible(section: string): boolean {
    let isVisible: boolean = false;
    switch (section) {
      case 'user-list':
        return this.isInAllowedRole([
          'SYSTEM_USER',
          'ADMIN_USER',
          'SUPPORT_USER',
          'SUPER_ADMIN_USER',
        ]);
      case 'add-security':
        return this.isInAllowedRole([
          'SYSTEM_USER',
          'ADMIN_USER',
          'SUPPORT_USER',
          'SUPER_ADMIN_USER',
          'ORGANIZER_USER',
        ]);
    }
    return isVisible;
  }

  private isInAllowedRole(allowedRoles: string[]): boolean {
    return allowedRoles.some((i) => this.loggedInUserRoles.has(i));
  }

  openProfile() {
    if (this.loggedInUserDetails.userName !== null) {
      this.router.navigate(['profile', this.loggedInUserDetails.userName]);
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
    const loggedinUserDetails: LoggedInUserDetails =
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
