import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InspectionScreenComponent } from './inspection-screen.component';

describe('InspectionScreenComponent', () => {
  let component: InspectionScreenComponent;
  let fixture: ComponentFixture<InspectionScreenComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectionScreenComponent]
    });
    fixture = TestBed.createComponent(InspectionScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
