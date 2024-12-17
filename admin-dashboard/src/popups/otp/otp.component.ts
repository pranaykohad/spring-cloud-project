import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { UserService } from '../../services/user.service';

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

  otp1!: string;
  otp2!: string;
  otp3!: string;
  otp4!: string;
  otp5!: string;
  otp6!: string;
  otpFinal!: string;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.clearAndSendOtp();
  }

  private clearAndSendOtp() {
    this.userService.generateOtp().subscribe();
    this.otp1 = '';
    this.otp2 = '';
    this.otp3 = '';
    this.otp4 = '';
    this.otp5 = '';
    this.otp6 = '';
    this.otpFinal = '';
  }

  next(nextInput: HTMLInputElement | null) {
    this.otpFinal =
      this.otp1 + this.otp2 + this.otp3 + this.otp4 + this.otp5 + this.otp6;
    nextInput?.focus();
    if (this.otpFinal.length == 6) {
      this.otpEmiter.emit(this.otpFinal);
    }
  }

  resendOtp() {
    this.clearAndSendOtp();
  }
}
