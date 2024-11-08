import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimRateDetailComponent } from './claim-rate-detail.component';

describe('ClaimRateDetailComponent', () => {
  let component: ClaimRateDetailComponent;
  let fixture: ComponentFixture<ClaimRateDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimRateDetailComponent]
    });
    fixture = TestBed.createComponent(ClaimRateDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
