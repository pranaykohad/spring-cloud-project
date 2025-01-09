import { EventService } from '../../../services/event.service';
import { Component, input, Input, OnInit } from '@angular/core';
import { SharedModule } from '../../../shared/shared.module';
import { ImageComponent } from '../../../image/image.component';
import { EventIdentifier, EventMedia } from '../../../models/EventDetailObject';

@Component({
  selector: 'app-event-photos',
  standalone: true,
  imports: [SharedModule, ImageComponent],
  templateUrl: './event-photos.component.html',
  styleUrl: './event-photos.component.scss',
})
export class EventPhotosComponent implements OnInit {
  private _eventIdentifier!: EventIdentifier;

  @Input()
  set eventIdentifier(value: EventIdentifier) {
    if (value && value.id !== 0) {
      this._eventIdentifier = value;
      this.getEventPhotos();
    }
  }

  get eventIdentifier(): EventIdentifier {
    return this._eventIdentifier;
  }

  constructor(private eventService: EventService) {}

  ngOnInit(): void {
    this.getBlankEventPhotos();
  }

  eventPhotos!: EventMedia[];

  getBlankEventPhotos() {
    this.eventPhotos = [];
    for (let index = 0; index < 9; index++) {
      this.eventPhotos.push({
        id: index,
        eventId: index,
        mediaUrl: '',
        mediaType: '',
        isForCover: true,
        mediaFile: new File([], ''),
        index: index,
      });
    }
  }

  getEventPhotos() {
    this.eventService
      .getEventPhotos(this.eventIdentifier.id, this.eventIdentifier.organizer)
      .subscribe((res: EventMedia[]) => {
        this.eventPhotos = res;
      });
  }

  editableEventMediaHandler(event: EventMedia) {
    this.eventPhotos[event.index] = event;
  }

  saveEventPhotos() {

  }

  cancelEventPhotos(){

  }

}
