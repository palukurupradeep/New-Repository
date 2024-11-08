import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimCustomerDetailsComponent } from './claim-customer-details.component';

describe('ClaimCustomerDetailsComponent', () => {
  let component: ClaimCustomerDetailsComponent;
  let fixture: ComponentFixture<ClaimCustomerDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimCustomerDetailsComponent]
    });
    fixture = TestBed.createComponent(ClaimCustomerDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
