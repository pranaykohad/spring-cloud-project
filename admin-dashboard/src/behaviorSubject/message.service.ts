import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs/internal/BehaviorSubject';
import { InternalMsg } from '../models/InternalMsg';
import { Msg } from '../models/Enums';

@Injectable({
  providedIn: 'root',
})
export class MessageService {
  private messageSource: BehaviorSubject<InternalMsg> =
    new BehaviorSubject<InternalMsg>({ msg: Msg.INIT_MSG, value: '' });

  public readonly currentMessage$ = this.messageSource.asObservable();

  sendMessage(msg: InternalMsg) {
    this.messageSource.next(msg);
  }

  enableLoader(){
    this.sendMessage({
      msg: Msg.ENABLE_LOADER,
      value: null,
    });
  }

  disableLoader(){
    this.sendMessage({
      msg: Msg.DISABLE_LOADER,
      value: null,
    });
  }

}
