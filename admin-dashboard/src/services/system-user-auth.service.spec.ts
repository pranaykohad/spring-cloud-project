import { TestBed } from '@angular/core/testing';

import { SystemUserAuthService } from './system-user-auth.service';

describe('SystemUserAuthService', () => {
  let service: SystemUserAuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SystemUserAuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
