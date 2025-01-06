import { Page } from './Page';
import { ColumnConfig } from './UserInfoListObject';

export interface EventType {
  eventTypeName: string;
}

export interface Event {
  id: string;
  eventTitle: string;
  eventDescription: string;
  createdOn: string;
  bookingOpenFrom: string;
  bookingOpenAt: string;
  bookingOpen: string;
  userMinAge: number;
  organizer: string;
  eventType: EventType;
}

export interface EventListDto {
  columnConfig: ColumnConfig;
  eventList: Event[];
  eventPage: Page;
}
