import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PhotoMaintenanceComponent } from './photo-maintenance.component';

describe('PhotoMaintenanceComponent', () => {
  let component: PhotoMaintenanceComponent;
  let fixture: ComponentFixture<PhotoMaintenanceComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PhotoMaintenanceComponent]
    });
    fixture = TestBed.createComponent(PhotoMaintenanceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
