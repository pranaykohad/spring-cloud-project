import { isPlatformBrowser } from '@angular/common';
import { Inject, Injectable, PLATFORM_ID } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class LocalstorageService {
  private isBrowser: boolean;

  constructor(@Inject(PLATFORM_ID) private platformId: object) {
    this.isBrowser = isPlatformBrowser(this.platformId);
  }

  setItem(key: string, value: string): void {
    if (this.isBrowser) {
      // localStorage.setItem(key, JSON.stringify(value));
      localStorage.setItem(key, value);
    }
  }

  getItem(key: string): any | null {
    if (this.isBrowser) {
      const value = localStorage.getItem(key);
      return value;
      // if (value) {
      //   const userObject = JSON.parse(value);
      //   return userObject;
      // }
    }
    return null;
  }

  removeItem(key: string): void {
    if (this.isBrowser) {
      localStorage.removeItem(key);
    }
  }

  clear(): void {
    localStorage.clear();
  }
}
