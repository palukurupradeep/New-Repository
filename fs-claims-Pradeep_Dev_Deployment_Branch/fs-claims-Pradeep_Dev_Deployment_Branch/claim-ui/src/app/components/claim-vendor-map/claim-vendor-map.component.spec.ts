import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimVendorMapComponent } from './claim-vendor-map.component';

describe('ClaimVendorMapComponent', () => {
  let component: ClaimVendorMapComponent;
  let fixture: ComponentFixture<ClaimVendorMapComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimVendorMapComponent]
    });
    fixture = TestBed.createComponent(ClaimVendorMapComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
