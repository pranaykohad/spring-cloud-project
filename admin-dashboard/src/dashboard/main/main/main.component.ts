import { Component, OnInit } from '@angular/core';
import { DatatableComponent } from '../../../datatable/datatable.component';
import { UserInfoListObject } from '../../../models/UserInfo';
import { SharedModule } from '../../../shared/shared.module';
import { UserService } from './../../../services/user.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-main',
  standalone: true,
  imports: [SharedModule, DatatableComponent],
  templateUrl: './main.component.html',
  styleUrl: './main.component.scss',
})
export class MainComponent implements OnInit {
  userInfoListObject!: UserInfoListObject;

  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const userName = this.route.snapshot.queryParams['userName'];
    if (userName) {
      this.router.navigate(['profile', userName]);
      return;
    }
    this.getUserInfoList();
  }

  private getUserInfoList() {
    this.userService.getUserInfoList().subscribe((res) => {
      this.userInfoListObject = res;
    });
  }
}
