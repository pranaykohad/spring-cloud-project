import { Component } from '@angular/core';
import { AzureService } from '../services/azure.service';
import { subscribe } from 'diagnostics_channel';
import { UserAppInfoService } from '../services/user-app-info.service';
import { SharedModule } from '../shared/shared.module';
import { HomeComponent } from '../home/home.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [SharedModule, HomeComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  constructor(private azureService: AzureService, private userAppInfoService: UserAppInfoService) {
    // this.azureService.getBlob().subscribe((res) => {
    // });

    // this.userAppInfoService.getCsrfToken().subscribe(res=>{
    //   console.log(res);
    // });
  }
}
