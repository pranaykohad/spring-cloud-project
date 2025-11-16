import { SearchOperator } from './Enums';

export interface SearchFilter {
  key: string;
  value: any;
  valueTo?: any;
  operator: SearchOperator;
}
