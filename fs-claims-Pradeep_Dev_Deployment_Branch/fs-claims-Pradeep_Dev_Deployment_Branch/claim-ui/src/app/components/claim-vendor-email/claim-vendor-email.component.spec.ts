import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimVendorEmailComponent } from './claim-vendor-email.component';

describe('ClaimVendorEmailComponent', () => {
  let component: ClaimVendorEmailComponent;
  let fixture: ComponentFixture<ClaimVendorEmailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimVendorEmailComponent]
    });
    fixture = TestBed.createComponent(ClaimVendorEmailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
