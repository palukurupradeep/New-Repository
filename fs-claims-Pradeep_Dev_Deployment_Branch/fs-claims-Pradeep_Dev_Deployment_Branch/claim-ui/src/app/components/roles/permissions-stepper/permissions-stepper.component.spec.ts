import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PermissionsStepperComponent } from './permissions-stepper.component';

describe('PermissionsStepperComponent', () => {
  let component: PermissionsStepperComponent;
  let fixture: ComponentFixture<PermissionsStepperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PermissionsStepperComponent]
    });
    fixture = TestBed.createComponent(PermissionsStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
