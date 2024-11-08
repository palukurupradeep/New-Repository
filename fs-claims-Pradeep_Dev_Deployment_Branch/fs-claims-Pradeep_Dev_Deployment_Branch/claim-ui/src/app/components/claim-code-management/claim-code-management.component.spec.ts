import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimCodeManagementComponent } from './claim-code-management.component';

describe('ClaimCodeManagementComponent', () => {
  let component: ClaimCodeManagementComponent;
  let fixture: ComponentFixture<ClaimCodeManagementComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimCodeManagementComponent]
    });
    fixture = TestBed.createComponent(ClaimCodeManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
