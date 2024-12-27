import { Router } from '@angular/router';
import {
  Component,
  EventEmitter,
  Input,
  OnChanges,
  Output,
  SimpleChanges,
} from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { UserInfo, UserInfoListObject } from '../models/UserInfo';
import { TableRowSelectEvent } from 'primeng/table';

@Component({
  selector: 'app-datatable',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './datatable.component.html',
  styleUrl: './datatable.component.scss',
})
export class DatatableComponent {
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

  customSort(event: any) {}
}
