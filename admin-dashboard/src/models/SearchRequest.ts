import { SortOrder } from "./Enums";
import { SearchFilter } from "./SearchFilter";

export interface SearchRequest {
  currentPage: number;
  sortColumn: string;
  sortOrder: SortOrder;
  searchFilters: SearchFilter[];
}
