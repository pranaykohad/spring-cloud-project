import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { UserService } from '../../services/user.service';
import { SharedModule } from '../../shared/shared.module';

@Component({
  selector: 'app-otp',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './otp.component.html',
  styleUrl: './otp.component.scss',
})
export class OtpComponent implements OnInit, OnDestroy {
  @Input()
  visible!: boolean;

  @Output()
  otpEmiter: EventEmitter<string> = new EventEmitter<string>();

  currentTimerValue: number = 0;
  otpTimeout: number = 10;
  warningTimeout: number = 8;
  otpArray!: number[];
  enableResendBtn: boolean = false;
  enableProgressBar: boolean = false;

  private timeInterval: any;

  constructor(private userService: UserService) {}

  ngOnDestroy(): void {
    if (this.timeInterval) {
      clearInterval(this.timeInterval);
    }
  }

  ngOnInit(): void {
    this.clearAndSendOtp();
  }

  getCurrentTimerValue() {
    return Math.abs((this.currentTimerValue / this.otpTimeout) * 100);
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
    this.clearOtp();
    this.clearAndSendOtp();
  }

  clearOtp(){
    this.otpArray = [];
  }

  private clearAndSendOtp() {
    this.enableProgressBar = true;
    // this.userService.generateOtp().subscribe(); // do not gnerate new otp while testing
    this.clearOtp();
    this.timeInterval = setInterval(() => {
      console.log('interval');
      if (this.currentTimerValue < this.otpTimeout) {
        this.currentTimerValue = this.currentTimerValue + 1;
      } else {
        clearInterval(this.timeInterval);
        this.enableResendBtn = true;
        this.enableProgressBar = false;
        this.currentTimerValue = 0;
      }
    }, 1000);
  }
}
