export interface UserBasicDetails {
  userName: string;
  profilePicUrl: string;
  displayName: string;
  profilePicFile: File;
}

export interface UserSecuredDetails {
  userName: string;
  otp: string;
  password: string;
  phone: string;
  email: string;
}
