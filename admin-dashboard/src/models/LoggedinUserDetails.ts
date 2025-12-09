export interface LoggedInUserDetails {
  jwt: string;
  displayName: string;
  profilePicUrl: string;
  profilePic: any;
  roles: string[];
  userName: string;
  phone: string;
}
