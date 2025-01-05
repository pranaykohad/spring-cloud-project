export interface Page {
  totalRecords: number;
  filteredRecords: number;
  totalPages: number;
  currentPage: number;
  rowStartIndex: number;
  rowEndIndex: number;
  recordsPerPage: number;
  displayPagesIndex: number[];
}
