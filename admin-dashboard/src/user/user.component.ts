import { Component, OnInit } from '@angular/core';
import { UserInfoRespone } from '../models/UserInfoListObject';
import { UserService } from '../services/user.service';
import { SharedModule } from '../shared/shared.module';
import { UserDatatableComponent } from './user-datatable/user-datatable.component';
import { SearchRequest } from '../models/SearchRequest';
import { SearchOperator, SortOrder } from '../models/Enums';

@Component({
  selector: 'app-user',
  standalone: true,
  imports: [SharedModule, UserDatatableComponent],
  templateUrl: './user.component.html',
  styleUrl: './user.component.scss',
})
export class UserComponent implements OnInit {
  userInfoListObject!: UserInfoRespone;
  searchRequest!: SearchRequest;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.searchRequest = {
      currentPage: 0,
      sortColumn: 'userName',
      sortOrder: SortOrder.ASC,
      searchFilters: [
        // {
        //   key: 'userName',
        //   value: 'nilhesh',
        //   operator: SearchOperator.LIKE,
        //   valueTo: null,
        // },
      ],
    };
    this.getUserInfoList();
  }

  pageEmitHandler(pageNumber: number) {
    this.searchRequest.currentPage = pageNumber;
    this.getUserInfoList();
  }

  sortEventHandler(value: { sortColumn: string; sortOrder: number }) {
    const sortColumn: string = value?.sortColumn;
    const sortOrder: SortOrder =
      value.sortOrder === 1 ? SortOrder.ASC : SortOrder.DESC;
    if (
      this.searchRequest.sortColumn !== sortColumn ||
      this.searchRequest.sortOrder !== sortOrder
    ) {
      this.searchRequest.sortColumn = sortColumn;
      this.searchRequest.sortOrder = sortOrder;
      this.searchRequest.currentPage = 0;
      this.getUserInfoList();
    }
  }

  private getUserInfoList() {
    this.userService.getUserInfoList(this.searchRequest).subscribe((res) => {
      this.userInfoListObject = res;
    });
  }
}
