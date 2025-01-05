import { Component, EventEmitter, Input, Output } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { EventListDto } from '../models/EventListDto';
import { Page } from '../models/Page';

@Component({
  selector: 'app-paginator',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss',
})
export class PaginatorComponent {
  @Input()
  page!: Page;

  @Output()
  pageEmitter = new EventEmitter<number>();

}
