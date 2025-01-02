import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { EventListDto } from '../../models/EventListDto';
import { SharedModule } from '../../shared/shared.module';
import { TableRowSelectEvent } from 'primeng/table';

@Component({
  selector: 'app-event-datatable',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './event-datatable.component.html',
  styleUrl: './event-datatable.component.scss',
})
export class EventDatatableComponent{
  private DATE_COLUMN = new Set([
    'createdOn',
    'bookingOpenAt',
    'bookingCloseAt',
  ]);
  constructor(private router: Router) {}

  first: number = 0;
  @Input()
  eventListDto!: EventListDto;

  @Output()
  eventNameEmitter = new EventEmitter<string>();

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
