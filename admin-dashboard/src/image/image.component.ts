import { Component, EventEmitter, Input, Output } from '@angular/core';
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
  constructor(private toastService: ToastService) {}

  editableEventMedia!: EventMedia;
  imagePreview!: string;
  tempFile!: File;

  private _eventMedia!: EventMedia;

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
  eventId!: number;

  @Input()
  organizer!: string;

  @Output()
  editableEventMediaEmitter = new EventEmitter<EventMedia>();

  onUpload(event: Event) {
    if (!this.eventId || this.eventId === 0) {
      this.toastService.showErrorToast(
        'please add event title before add photos'
      );
    }

    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length == 1) {
      const file = input.files[0];

      if (!file.type.startsWith('image/')) {
        this.toastService.showErrorToast('Only image files are allowed');
        return;
      }

      const maxSize = 5 * 1024 * 1024; // 5 MB
      if (file.size > maxSize) {
        this.toastService.showErrorToast('File size exceeds 5 MB');
        return;
      }

      const timestamp = new Date()
        .toISOString()
        .replace(/[-:.]/g, '')
        .slice(0, 15);

      this.tempFile = new File(
        [file],
        `${this.organizer}-${this.eventId}-${
          this.editableEventMedia.mediaIndex
        }-${timestamp}.${file.name.split('.').pop()}`,
        {
          type: file.type,
        }
      );

      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result as string;
      };
      reader.readAsDataURL(file);
      const emitFile: EventMedia = {
        mediaUrl: '',
        coverMedia: this.editableEventMedia.mediaIndex === 0,
        mediaFile: this.tempFile,
        mediaIndex: this.editableEventMedia.mediaIndex,
      };
      this.editableEventMediaEmitter.emit(emitFile);
    }
  }

  cancel() {
    this.editableEventMedia = JSON.parse(JSON.stringify(this.eventMedia));
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(this.editableEventMedia.mediaFile);
  }
}
