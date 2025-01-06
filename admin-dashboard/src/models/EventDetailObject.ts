export interface EventMedia {
  id: number;
  mediaUrl: string;
  mediaType: string;
  isForCover: boolean;
}

export interface EventOverview {
  id: number;
  eventTitle: string;
  eventDescription: string;
  organizer: string;
  userMinAge: number;
  createdOn: string;
  eventPhotos: EventMedia[];
}


export interface EventOverviewError {
  eventTitle: string;
  eventDescription: string;
  userMinAge: string;
}
