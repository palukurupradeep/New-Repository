import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimDetailInvoiceComponent } from './claim-detail-invoice.component';

describe('ClaimDetailInvoiceComponent', () => {
  let component: ClaimDetailInvoiceComponent;
  let fixture: ComponentFixture<ClaimDetailInvoiceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimDetailInvoiceComponent]
    });
    fixture = TestBed.createComponent(ClaimDetailInvoiceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
