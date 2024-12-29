export interface LoggedInUserDetails {
  jwt: string;
  displayName: string;
  profilePicUrl: string;
  roles: string[];
  userName: string;
  phone: string;
}
