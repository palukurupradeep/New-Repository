import { TestBed } from '@angular/core/testing';

import { BatchjobService } from './batchjob.service';

describe('BatchjobService', () => {
  let service: BatchjobService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BatchjobService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
