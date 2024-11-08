import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimVendorProfileComponent } from './claim-vendor-profile.component';

describe('ClaimVendorProfileComponent', () => {
  let component: ClaimVendorProfileComponent;
  let fixture: ComponentFixture<ClaimVendorProfileComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimVendorProfileComponent]
    });
    fixture = TestBed.createComponent(ClaimVendorProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
