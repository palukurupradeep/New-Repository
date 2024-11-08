import { TestBed } from '@angular/core/testing';

import { SmartCodeService } from './smart-code.service';

describe('SmartCodeService', () => {
  let service: SmartCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SmartCodeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
