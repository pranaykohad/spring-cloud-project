import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Router } from '@angular/router';
import { TableRowSelectEvent } from 'primeng/table';
import { UserInfoListObject } from '../../models/UserInfoListObject';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-user-datatable',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './user-datatable.component.html',
  styleUrl: './user-datatable.component.scss',
})
export class UserDatatableComponent {
  constructor(private router: Router) {}

  first: number = 0;
  @Input()
  userInfoListObject!: UserInfoListObject;

  @Output()
  userNameEmitter = new EventEmitter<string>();

  onRowSelect(event: TableRowSelectEvent) {
    if (event.data) {
      this.router.navigate(['profile', event.data.userName]);
    }
  }

  //TODO:
  // custom sorting
  // custom filter
  // custom search
  // global search

  customSort(event: any) {}
}
