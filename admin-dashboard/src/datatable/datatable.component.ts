import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { SharedModule } from '../shared/shared.module';
import { UserInfo, UserInfoListObject } from '../models/UserInfo';

@Component({
  selector: 'app-datatable',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './datatable.component.html',
  styleUrl: './datatable.component.scss',
})
export class DatatableComponent {
  first: number = 0;
  @Input()
  userInfoListObject!: UserInfoListObject;
}
