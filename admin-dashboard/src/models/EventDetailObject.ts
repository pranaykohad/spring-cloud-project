export interface EventMedia {
  id?: number;
  mediaUrl: string;
  coverMedia: boolean;
  mediaFile: File;
  mediaIndex: number;
}

export interface EventOverview {
  id?: number;
  userName: string;
  otp: string;
  eventTitle: string;
  eventDescription: string;
  organizer: string;
  userMinAge: number;
  createdOn: string;
}

export interface EventOverviewError {
  eventTitle: string;
  eventDescription: string;
  userMinAge: string;
}

export interface EventIdentifier {
  id: number;
  organizer: string;
}

export interface EventMediaReqObject {
  id?: number;
  coverMedia: boolean;
  mediaIndex: number;
  fileName: string;
}

export interface EventMediaRequest {
  eventId: number;
  otp: string;
  eventMediaReqObject: EventMediaReqObject[];
}
