import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-otp',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './otp.component.html',
  styleUrl: './otp.component.scss',
})
export class OtpComponent implements OnInit {
  @Input()
  visible!: boolean;

  @Output()
  otpEmiter: EventEmitter<string> = new EventEmitter<string>();

  otpArray!: number[];

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.clearAndSendOtp();
  }

  private clearAndSendOtp() {
    this.userService.generateOtp().subscribe();
    this.otpArray = [];
  }

  onInput(target: any, nextInput: HTMLInputElement | null) {
    if (!/^\d{1}$/.test(target.value)) {
      target.value = '';
    } else {
      nextInput?.focus();
      if (this.otpArray.length == 6) {
        this.otpEmiter.emit(this.otpArray.join(''));
      }
    }
  }

  resendOtp() {
    this.clearAndSendOtp();
  }
}
