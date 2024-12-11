import { LocalstorageService } from './../services/localstorage.service';
import { Component, OnInit } from '@angular/core';
import { AzureService } from '../services/azure.service';
import { UserAppInfoService } from '../services/user-app-info.service';
import { SharedModule } from '../shared/shared.module';
import { SystemUserAuthService } from '../services/system-user-auth.service';
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
    private systemUserAuthService: SystemUserAuthService,
    private localstorageService: LocalstorageService
  ) {}

  ngOnInit(): void {
  }

}
