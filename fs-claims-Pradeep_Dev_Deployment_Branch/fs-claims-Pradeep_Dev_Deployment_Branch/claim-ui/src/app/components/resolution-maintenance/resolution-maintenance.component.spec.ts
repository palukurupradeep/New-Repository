import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResolutionMaintenanceComponent } from './resolution-maintenance.component';

describe('ResolutionMaintenanceComponent', () => {
  let component: ResolutionMaintenanceComponent;
  let fixture: ComponentFixture<ResolutionMaintenanceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ResolutionMaintenanceComponent]
    });
    fixture = TestBed.createComponent(ResolutionMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
