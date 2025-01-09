import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-add-event',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './add-event.component.html',
  styleUrl: './add-event.component.scss',
})
export class AddEventComponent implements OnInit {
  constructor(private userService: UserService) {}

  selectedOrganizer!: string;

  organizerList!: string[];

  @Input()
  visible!: boolean;

  @Output()
  organizerNameEmiter: EventEmitter<string> = new EventEmitter<string>();

  ngOnInit(): void {
    this.userService.getOrganizerList().subscribe((res: string[]) => {
      this.organizerList = res;
    });
  }

  selectRole(value: string) {}

  proceed() {
    if (this.selectedOrganizer) {
      this.organizerNameEmiter.emit(this.selectedOrganizer);
    }
  }
}
