import { TestBed } from '@angular/core/testing';

import { GlobalErrService } from './global-err.service';

describe('GlobalErrService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GlobalErrService = TestBed.get(GlobalErrService);
    expect(service).toBeTruthy();
  });
});
