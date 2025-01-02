import { Component, OnInit } from '@angular/core';
import { UserInfoListObject } from '../models/UserInfoListObject';
import { UserService } from '../services/user.service';
import { SharedModule } from '../shared/shared.module';
import { UserDatatableComponent } from './user-datatable/user-datatable.component';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [SharedModule, UserDatatableComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
})
export class UserComponent implements OnInit {
  userInfoListObject!: UserInfoListObject;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.getUserInfoList();
  }

  private getUserInfoList() {
    this.userService.getUserInfoList().subscribe((res) => {
      this.userInfoListObject = res;
    });
  }
}
