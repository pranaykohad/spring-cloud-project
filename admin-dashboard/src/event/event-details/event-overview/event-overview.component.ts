import { Component } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';

@Component({
  selector: 'app-event-overview',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './event-overview.component.html',
  styleUrl: './event-overview.component.scss',
})
export class EventOverviewComponent {
  images: string[] = [
    'assets/img/event image 1.png',
    'assets/img/event image 2.png',
    'assets/img/event image 3.png',
    'assets/img/event image 4.png',
  ];
}
