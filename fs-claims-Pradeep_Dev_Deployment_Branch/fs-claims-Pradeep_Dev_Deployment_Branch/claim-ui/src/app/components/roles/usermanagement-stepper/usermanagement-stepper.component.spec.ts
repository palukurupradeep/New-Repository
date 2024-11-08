import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UsermanagementStepperComponent } from './usermanagement-stepper.component';

describe('UsermanagementStepperComponent', () => {
  let component: UsermanagementStepperComponent;
  let fixture: ComponentFixture<UsermanagementStepperComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UsermanagementStepperComponent]
    });
    fixture = TestBed.createComponent(UsermanagementStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
