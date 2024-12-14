
export interface SystemUserSigninRequest {
  userName: string;
  password: string;
  confirmPassword: string;
  email: string;
  phone: string;
  displayName: string;
  roles: string[];
}

export interface SystemUserSigninRequestError {
  userName: string;
  password: string;
  confirmPassword: string;
  email: string;
  phone: string;
  displayName: string;
  roles: string;
}
