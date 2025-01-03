import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { EventListDto } from '../../models/EventListDto';
import { SharedModule } from '../../shared/shared.module';
import { TableRowSelectEvent } from 'primeng/table';
import { PaginatorComponent } from '../../paginator/paginator.component';

@Component({
  selector: 'app-event-datatable',
  standalone: true,
  imports: [SharedModule, PaginatorComponent],
  templateUrl: './event-datatable.component.html',
  styleUrl: './event-datatable.component.scss',
})
export class EventDatatableComponent {
  private DATE_COLUMN = new Set([
    'createdOn',
    'bookingOpenAt',
    'bookingCloseAt',
  ]);

  private _eventListDto!: EventListDto;
  displayPages: number[] = [1, 2, 3, 4, 5];
  @ViewChild('myButton') buttonRef!: ElementRef<HTMLButtonElement>;

  @Input()
  set eventListDto(eventListDto: EventListDto) {
    if (eventListDto) {
      this._eventListDto = eventListDto;
    }
  }

  get eventListDto(): EventListDto {
    return this._eventListDto;
  }

  @Output()
  eventNameEmitter = new EventEmitter<string>();

  @Output()
  pageEmitter = new EventEmitter<number>();

  constructor(private router: Router) {}

  onRowSelect(event: TableRowSelectEvent) {
    if (event.data) {
      this.router.navigate(['event-details', event.data.id]);
    }
  }

  customSort(event: any) {}

  isDateColumn(column: string): boolean {
    return this.DATE_COLUMN.has(column);
  }
}
