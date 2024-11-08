import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimVendorDetailComponent } from './claim-vendor-detail.component';

describe('ClaimVendorDetailComponent', () => {
  let component: ClaimVendorDetailComponent;
  let fixture: ComponentFixture<ClaimVendorDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimVendorDetailComponent]
    });
    fixture = TestBed.createComponent(ClaimVendorDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
