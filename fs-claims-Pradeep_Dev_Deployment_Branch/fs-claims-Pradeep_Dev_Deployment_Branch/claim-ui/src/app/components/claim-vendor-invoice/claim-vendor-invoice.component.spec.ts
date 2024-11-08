import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimVendorInvoiceComponent } from './claim-vendor-invoice.component';

describe('ClaimVendorInvoiceComponent', () => {
  let component: ClaimVendorInvoiceComponent;
  let fixture: ComponentFixture<ClaimVendorInvoiceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimVendorInvoiceComponent]
    });
    fixture = TestBed.createComponent(ClaimVendorInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
