import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreatePermissionsetStepperComponent } from './create-permissionset-stepper.component';

describe('CreatePermissionsetStepperComponent', () => {
  let component: CreatePermissionsetStepperComponent;
  let fixture: ComponentFixture<CreatePermissionsetStepperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreatePermissionsetStepperComponent]
    });
    fixture = TestBed.createComponent(CreatePermissionsetStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
