import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'column',
  standalone: true,
})
export class ColumnPipe implements PipeTransform {
  transform(value: string): string {
    if (!value) return '';
    return value
      .split(/(?=[A-Z])|_/)
      .map((word) => word.charAt(0).toUpperCase() + word.slice(1).toLowerCase())
      .join(' ');
  }
}
