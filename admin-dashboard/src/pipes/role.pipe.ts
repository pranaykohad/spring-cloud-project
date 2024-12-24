import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'role',
  standalone: true,
})
export class RolePipe implements PipeTransform {
  transform(roles: string[]): string[] {
    if (!roles || !Array.isArray(roles)) return roles;
    return roles.map((role) =>
      role
        .toLowerCase()
        .split('_')
        .map((word) => word.charAt(0).toUpperCase() + word.slice(1))
        .join(' ')
    );
  }
}
