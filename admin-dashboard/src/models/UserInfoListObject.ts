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
  totalRows: number;
  rowsPerPage: number;
}

export interface UserInfoListObject {
  columnConfig: ColumnConfig;
  userInfoList: UserInfo[];
}
