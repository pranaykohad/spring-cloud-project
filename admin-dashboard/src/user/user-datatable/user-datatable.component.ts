import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { TableRowSelectEvent } from 'primeng/table';
import { UserInfoRespone } from '../../models/UserInfoListObject';
import { SharedModule } from '../../shared/shared.module';
import { SortEvent } from 'primeng/api';
import { PaginatorComponent } from '../../paginator/paginator.component';

@Component({
  selector: 'app-user-datatable',
  standalone: true,
  imports: [SharedModule, PaginatorComponent],
  templateUrl: './user-datatable.component.html',
  styleUrl: './user-datatable.component.scss',
})
export class UserDatatableComponent {
  displayPages: number[] = [1, 2, 3, 4, 5];
  constructor(private router: Router) {}

  @Input()
  userInfoListObject!: UserInfoRespone;

  @Output()
  sortEventEmitter = new EventEmitter<{
    sortColumn: string;
    sortOrder: number;
  }>();

  @Output()
  pageEmitter = new EventEmitter<number>();

  onRowSelect(event: TableRowSelectEvent) {
    if (event.data) {
      this.router.navigate(['profile', event.data.userName]);
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
}
