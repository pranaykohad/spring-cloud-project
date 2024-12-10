import { Injectable } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ToastType } from '../models/ToastType';

@Injectable({
  providedIn: 'root',
})
export class ToastService {
  constructor(private toastr: ToastrService) {}

  showToast(title: string, message: string, type: ToastType) {
    switch (type) {
      case ToastType.SUCCESS:
        this.toastr.success(message, title, {
          closeButton: true,
          easing: 'ease-in',
          progressBar: true,
          progressAnimation: 'increasing',
        });
        break;
      case ToastType.ERROR:
        this.toastr.error(message, title, {
          closeButton: true,
          easing: 'ease-in',
          progressBar: true,
          progressAnimation: 'increasing',
        });
        break;
      case ToastType.WARNING:
        this.toastr.warning(message, title, {
          closeButton: true,
          easing: 'ease-in',
          progressBar: true,
          progressAnimation: 'increasing',
        });
        break;
      case ToastType.INFO:
        this.toastr.info(message, title, {
          closeButton: true,
          easing: 'ease-in',
          progressBar: true,
          progressAnimation: 'increasing',
        });
        break;
      default:
        this.toastr.success(message, title, {
          closeButton: true,
          easing: 'ease-in',
          progressBar: true,
          progressAnimation: 'increasing',
        });
    }
  }
}
