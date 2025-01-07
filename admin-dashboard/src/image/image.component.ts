import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  Output,
  ViewChild,
} from '@angular/core';
import { EventMedia } from '../models/EventDetailObject';
import { ToastService } from '../services/toast.service';
import { SharedModule } from '../shared/shared.module';

@Component({
  selector: 'app-image',
  standalone: true,
  imports: [SharedModule],
  templateUrl: './image.component.html',
  styleUrl: './image.component.scss',
})
export class ImageComponent {
  @ViewChild('eventMediaTemplate') myInputRef!: ElementRef;

  constructor(private toastService: ToastService) {}

  private _eventMedia!: EventMedia;

  editableEventMedia!: EventMedia;

  @Input()
  set eventMedia(value: EventMedia) {
    if (value) {
      this._eventMedia = value;
      this.editableEventMedia = JSON.parse(JSON.stringify(value));
    }
  }

  get eventMedia(): EventMedia {
    return this._eventMedia;
  }

  @Input()
  eventTitle!: string;

  @Output()
  editableEventMediaEmitter = new EventEmitter<EventMedia>();

  onUpload(event: Event) {
    if (!this.eventTitle) {
      this.toastService.showErrorToast(
        'please add event title before add photos'
      );
    }
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length == 1) {
      const fileList: FileList = input.files;
      const file = fileList[0];
      this.handleFile(file);
    }
  }

  private handleFile(file: File) {
    if (!file.type.startsWith('image/')) {
      this.toastService.showErrorToast('Only image files are allowed');
      return;
    }

    const maxSize = 5 * 1024 * 1024; // 5 MB
    if (file.size > maxSize) {
      this.toastService.showErrorToast('File size exceeds 5 MB');
      return;
    }

    this.editableEventMedia.mediaFile = new File(
      [file],
      `${this.eventTitle}-${this.editableEventMedia.index}.${file.name
        .split('.')
        .pop()}`,
      {
        type: file.type,
      }
    );
    this.editableEventMedia.mediaType = file.type;

    const reader = new FileReader();
    reader.onload = () => {
      this.editableEventMedia.mediaUrl = reader.result as string;
    };
    reader.readAsDataURL(file);
    this.editableEventMediaEmitter.emit(this.editableEventMedia);
  }

  cancel() {
    this.editableEventMedia = JSON.parse(JSON.stringify(this.eventMedia));
  }
}
