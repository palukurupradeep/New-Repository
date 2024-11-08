import { TestBed } from '@angular/core/testing';

import { ClaimDetailService } from './claim-detail.service';

describe('ClaimDetailService', () => {
  let service: ClaimDetailService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClaimDetailService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
