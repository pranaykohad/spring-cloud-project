import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { EventListDto } from '../models/EventListDto';

@Component({
  selector: 'app-paginator',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss',
})
export class PaginatorComponent {
  @Input()
  eventListDto!: EventListDto;

  @Output()
  pageEmitter = new EventEmitter<number>();

}
