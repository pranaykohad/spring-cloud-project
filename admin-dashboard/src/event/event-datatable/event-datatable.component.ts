import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { TableRowSelectEvent } from 'primeng/table';
import { EventListDto } from '../../models/EventListDto';
import { PaginatorComponent } from '../../paginator/paginator.component';
import { SharedModule } from '../../shared/shared.module';
import { SortEvent } from 'primeng/api';
import { SortOrder } from '../../models/Enums';

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

  displayPages: number[] = [1, 2, 3, 4, 5];

  @Input()
  eventListDto!: EventListDto;

  @Output()
  sortEventEmitter = new EventEmitter<{
    sortColumn: string;
    sortOrder: number;
  }>();

  @Output()
  pageEmitter = new EventEmitter<number>();

  constructor(private router: Router) {}

  onRowSelect(event: TableRowSelectEvent) {
    if (event.data) {
      this.router.navigate(['event-details', event.data.id]);
    }
  }

  customSort(event: SortEvent) {
    if (event?.field && event?.order) {
      this.sortEventEmitter.emit({
        sortColumn: event.field,
        sortOrder: event.order,
      });
    }
  }

  isDateColumn(column: string): boolean {
    return this.DATE_COLUMN.has(column);
  }
}
