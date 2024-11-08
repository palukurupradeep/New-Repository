import { TestBed } from '@angular/core/testing';

import { PickupScheduleService } from './pickup-schedule.service';

describe('PickupScheduleService', () => {
  let service: PickupScheduleService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PickupScheduleService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
