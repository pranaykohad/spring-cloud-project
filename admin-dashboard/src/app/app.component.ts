import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { AzureService } from '../services/azure.service';
import { subscribe } from 'diagnostics_channel';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  constructor(private azureService: AzureService) {
    this.azureService.downloadBlob().subscribe((res) => {
      console.log(res);
    });
  }
}
