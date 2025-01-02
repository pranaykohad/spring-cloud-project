import { ColumnConfig } from './UserInfoListObject';

export interface EventType {
  eventTypeName: string;
}

export interface Event {
  id: string;
  eventName: string;
  eventDescription: string;
  createdOn: string;
  bookingOpenFrom: string;
  bookingOpenTill: string;
  openBooking: string;
  userMinAge: number;
  organizer: string;
  eventType: EventType;
}

export interface EventListDto {
  columnConfig: ColumnConfig;
  eventList: Event[];
}
