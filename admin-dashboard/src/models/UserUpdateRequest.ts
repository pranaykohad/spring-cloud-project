export interface UserBasicDetails {
  userName: string;
  profilePicUrl: string;
  displayName: string;
  profilePicFile: File;
  createdAt: string;
}

export interface UserBasicDetailsError {
  displayName: string;
}

export interface UserSecuredDetailsReq {
  userName: string;
  password: string;
  phone: string;
  email: string;
  status: string;
  otp: string;
}

export interface UserSecuredDetailsRes {
  userName: string;
  password: string;
  phone: string;
  email: string;
  status: string;
}

export interface EditableUserSecuredDetailsRes {
  userName: string;
  password: string;
  confirmPassword: string;
  phone: string;
  email: string;
  status: string;
  otp: string;
}

export interface UserSecuredDetailsError {
  password: string;
  confirmPassword: string;
  phone: string;
  email: string;
}
