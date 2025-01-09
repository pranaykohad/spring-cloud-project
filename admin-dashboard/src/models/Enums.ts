export enum ToastType {
  SUCCESS = 'success',
  ERROR = 'error',
  WARNING = 'warning',
  INFO = 'info',
}

export enum LocalStorageKeys {
  LOGGED_IN_USER_DETAILS = 'LOGGED_IN_USER_DETAILS',
  INMEMORY_USERNAME = 'INMEMORY_USERNAME',
}

export enum Msg {
  INIT_MSG,
  ENABLE_LOADER,
  DISABLE_LOADER,
  UPDATE_LOGGEDIN_USER_DETAILS,
}

export enum SearchOperator {
  EQUALS,
  LIKE,
  GT,
  GTE,
  LT,
  LTE,
  RANGE,
}

export enum SortOrder {
  ASC,
  DESC,
}

export const DisplayPages = [1, 2, 3, 4, 5];

export enum APP_ROUTES {
  BASE = '',
  LOGIN = 'login',
  LOGOUT = 'logout',
  SIGNIN = 'signin',
  SETTING = 'setting',
  PROFILE = 'profile',
  USER_LIST = 'user-list',
  EVENT_LIST = 'event-list',
  EVENT_DETAILS = 'event-details',
  ADD_SECURITY = 'add-security',

}
