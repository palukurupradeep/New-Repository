import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InspectionMaintenanceComponent } from './inspection-maintenance.component';

describe('InspectionMaintenanceComponent', () => {
  let component: InspectionMaintenanceComponent;
  let fixture: ComponentFixture<InspectionMaintenanceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectionMaintenanceComponent]
    });
    fixture = TestBed.createComponent(InspectionMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
