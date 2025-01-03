export interface UserInfo {
  userName: string;
  password: string;
  email: string;
  phone: string;
  displayName: string;
  roles: string[];
  profilePicUrl: string;
  status: string;
  createdAt: string;
}

export interface ColumnConfig {
  columns: string[];
}

export interface UserInfoListObject {
  columnConfig: ColumnConfig;
  userInfoList: UserInfo[];
  totalRecords: number;
  totalPages: number;
  recordsPerPage: number;
}
