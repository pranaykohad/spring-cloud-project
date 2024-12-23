import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ToastType } from '../models/Enums';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  toastConfig: {} = {
    closeButton: true,
    easing: 'ease-in',
    progressBar: true,
    progressAnimation: 'increasing',
    timeOut: 3000,
  };

  constructor(private toastr: ToastrService) {}

  showSuccessToast(message: string) {
    this.showToast('', message, ToastType.SUCCESS);
  }

  showErrorToast(message: string) {
    this.showToast('', message, ToastType.ERROR);
  }

  showWarnToast(message: string) {
    this.showToast('', message, ToastType.WARNING);
  }

  showInfoToast(message: string) {
    this.showToast('', message, ToastType.INFO);
  }

  private showToast(title: string, message: string, type: ToastType) {
    switch (type) {
      case ToastType.SUCCESS:
        this.toastr.success(message, title, this.toastConfig);
        break;
      case ToastType.ERROR:
        this.toastr.error(message, title, this.toastConfig);
        break;
      case ToastType.WARNING:
        this.toastr.warning(message, title, this.toastConfig);
        break;
      case ToastType.INFO:
        this.toastr.info(message, title, this.toastConfig);
        break;
      default:
        this.toastr.success(message, title, this.toastConfig);
    }
  }
}
