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

export interface UserInfoRespone {
  columnConfig: ColumnConfig;
  userInfoList: UserInfo[];
  userPage: UserPage;
}

export interface UserPage {
  totalRecords: number;
  filteredRecords: number;
  totalPages: number;
  currentPage: number;
  rowStartIndex: number;
  rowEndIndex: number;
  recordsPerPage: number;
  displayPagesIndex: number[];
}
