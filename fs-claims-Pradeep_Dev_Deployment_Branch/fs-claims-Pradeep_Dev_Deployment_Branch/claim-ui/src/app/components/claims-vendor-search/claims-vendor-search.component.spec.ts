import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimsVendorSearchComponent } from './claims-vendor-search.component';

describe('ClaimsVendorSearchComponent', () => {
  let component: ClaimsVendorSearchComponent;
  let fixture: ComponentFixture<ClaimsVendorSearchComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimsVendorSearchComponent]
    });
    fixture = TestBed.createComponent(ClaimsVendorSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
