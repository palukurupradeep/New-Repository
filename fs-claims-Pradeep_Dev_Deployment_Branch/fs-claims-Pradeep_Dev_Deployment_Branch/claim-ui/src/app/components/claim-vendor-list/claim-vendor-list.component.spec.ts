import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimVendorListComponent } from './claim-vendor-list.component';

describe('ClaimVendorListComponent', () => {
  let component: ClaimVendorListComponent;
  let fixture: ComponentFixture<ClaimVendorListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimVendorListComponent]
    });
    fixture = TestBed.createComponent(ClaimVendorListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
