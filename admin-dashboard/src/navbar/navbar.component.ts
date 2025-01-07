import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { MegaMenuItem, MenuItem } from 'primeng/api';
import { APP_ROUTES, LocalStorageKeys } from '../models/Enums';
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';
import { UserAuthService } from '../services/user-auth.service';
import { SharedModule } from '../shared/shared.module';
import { LocalstorageService } from './../services/localstorage.service';
import { MegaMenu } from 'primeng/megamenu';
import { Menu } from 'primeng/menu';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  onMouseLeave(menu: MegaMenu | Menu) {
    menu.hide();
  }

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
              // label: 'User Section',
              items: [
                {
                  label: 'User List',
                  visible: this.isSectionVisible('user-list'),
                  command: () => {
                    this.openSection('');
                  },
                },
                {
                  label: 'Add Security Person',
                  visible: this.isSectionVisible('add-security'),
                  command: () => {
                    this.openSection('add-security');
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
              // label: 'Event Section',
              items: [
                {
                  label: 'Event List',
                  command: () => {
                    this.openSection(APP_ROUTES.EVENT_LIST);
                  },
                },
                {
                  label: 'Add Event',
                  command: () => {
                    this.openSection(`${APP_ROUTES.EVENT_DETAILS}/0/new`);
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
        this.openSection('profile');
      },
    },
    {
      id: 'setting',
      label: 'Setting',
      icon: 'pi pi-cog',
      textAlign: 'center',
      command: () => {
        this.openSection('setting');
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

  openSection(section: string) {
    switch (section) {
      case APP_ROUTES.PROFILE:
        this.openProfile();
        break;
      case 'logout':
        this.openSection('logout');
        break;
      default:
        this.router.navigate([section]);
    }
  }

  logout() {
    this.systemUserAuthService.userLogout().subscribe((res: boolean) => {
      if (res) {
        const inMemoryUserName = this.localstorageService.getItem(
          LocalStorageKeys.INMEMORY_USERNAME
        );
        this.localstorageService.clear();
        this.localstorageService.setItem(
          LocalStorageKeys.INMEMORY_USERNAME,
          inMemoryUserName
        );
        this.router.navigate([APP_ROUTES.LOGIN]);
      }
    });
  }

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

  private openProfile() {
    if (this.loggedInUserDetails.userName !== null) {
      this.router.navigate([
        APP_ROUTES.PROFILE,
        this.loggedInUserDetails.userName,
      ]);
    }
  }
}
