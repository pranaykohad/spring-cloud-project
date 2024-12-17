import { LocalstorageService } from './../services/localstorage.service';
import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { UserAppInfoService } from '../services/user-app-info.service';
import { SharedModule } from '../shared/shared.module';
import { UserAuthService } from '../services/user-auth.service';
import { LocalStorageKeys } from '../models/Enums';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {

  constructor(
    private systemUserAuthService: UserAuthService,
    private localstorageService: LocalstorageService
  ) {}

  ngOnInit(): void {
  }

}
