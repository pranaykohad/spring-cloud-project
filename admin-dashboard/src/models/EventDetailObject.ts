export interface EventMedia {
  id?: number;
  eventId: number;
  mediaUrl: string;
  mediaType: string;
  isForCover: boolean;
  mediaFile: File;
  index: number;
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
