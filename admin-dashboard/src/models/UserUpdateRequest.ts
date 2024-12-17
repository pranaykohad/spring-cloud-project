export interface UserBasicDetails {
  userName: string;
  profilePicUrl: string;
  displayName: string;
  profilePicFile: File;
}

export interface UserBasicDetailsError {
  displayName: string;
}

export interface UserSecuredDetails {
  userName: string;
  password: string;
  confirmPassword: string;
  phone: string;
  email: string;
  otp: string;
}

export interface UserSecuredDetailsError {
  password: string;
  confirmPassword: string;
  phone: string;
  email: string;
}
