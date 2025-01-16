import {
  Component,
  ComponentRef,
  Input,
  ViewChild,
  ViewContainerRef,
} from '@angular/core';
import { Router } from '@angular/router';
import { MegaMenuItem, MenuItem } from 'primeng/api';
import { APP_ROUTES, LocalStorageKeys } from '../models/Enums';
import { LoggedInUserDetails } from '../models/LoggedinUserDetails';
import { UserAuthService } from '../services/user-auth.service';
import { SharedModule } from '../shared/shared.module';
import { LocalstorageService } from './../services/localstorage.service';
import { MegaMenu } from 'primeng/megamenu';
import { Menu } from 'primeng/menu';
import { AddEventComponent } from '../popups/add-event/add-event.component';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss',
})
export class NavbarComponent {
  addEventComponent!: ComponentRef<AddEventComponent>;

  @ViewChild('addEventContainer', { read: ViewContainerRef, static: true })
  addEventContainer!: ViewContainerRef;

  @Input()
  set loggedInUserDetails(value: LoggedInUserDetails) {
    this._loggedInUserDetails = value;
    if (value) {
      this.loggedInUserRoles = new Set(value.roles);
      this.setMegaPanel();
      this.setUserMenu();
    }
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

  userModel!: MegaMenuItem[];

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

  onMouseLeave(menu: MegaMenu | Menu) {
    menu.hide();
  }

  openSection(section: string) {
    switch (section) {
      case APP_ROUTES.PROFILE:
        this.openProfile();
        break;
      case APP_ROUTES.LOGOUT:
        this.logout();
        break;
      case `${APP_ROUTES.EVENT_DETAILS}/0/new`:
        this.handleAddEvent();
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

  private setUserMenu() {
    this.userModel = [
      {
        label: `<div class="d-flex flex-column align-items-start pointer-cursor">
                  <div>${this.loggedInUserDetails.displayName}</div>
                  <div>${this.loggedInUserDetails.roles}</div>
                </div>`,
        items: [
          [
            {
              items: [
                {
                  label: 'Profile',
                  command: () => {
                    this.openSection('profile');
                  },
                },
                {
                  label: 'Setting',
                  command: () => {
                    this.openSection('setting');
                  },
                },
                {
                  label: 'Logout',
                  command: () => {
                    this.logout();
                  },
                },
              ],
            },
          ],
        ],
      },
    ];
  }

  private setMegaPanel() {
    this.navbarModel = [
      {
        label: 'User',
        icon: 'pi pi-users',
        items: [
          [
            {
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

  private handleAddEvent() {
    if (this.isInAllowedRole(['ORGANIZER_USER'])) {
      this.router.navigate([
        `${APP_ROUTES.EVENT_DETAILS}/0/${this.loggedInUserDetails.userName}`,
      ]);
    } else if (
      this.isInAllowedRole([
        'SYSTEM_USER',
        'ADMIN_USER',
        'SUPPORT_USER',
        'SUPER_ADMIN_USER',
      ])
    ) {
      this.addEventContainer.clear();
      this.addEventComponent =
        this.addEventContainer.createComponent(AddEventComponent);
      this.addEventComponent.instance.visible = true;
      this.addEventComponent.instance.organizerNameEmiter.subscribe(
        (res: string) => {
          this.router.navigate([`${APP_ROUTES.EVENT_DETAILS}/0/${res}`]);
          this.addEventComponent.instance.visible = false;
        }
      );
    }
  }

  private isSectionVisible(section: string): boolean {
    let isVisible: boolean = false;
    switch (section) {
      case APP_ROUTES.USER_LIST:
        return this.isInAllowedRole([
          'SYSTEM_USER',
          'ADMIN_USER',
          'SUPPORT_USER',
          'SUPER_ADMIN_USER',
        ]);
      case APP_ROUTES.ADD_SECURITY:
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
