import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminDataScreensComponent } from './admin-data-screens.component';

describe('AdminDataScreensComponent', () => {
  let component: AdminDataScreensComponent;
  let fixture: ComponentFixture<AdminDataScreensComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminDataScreensComponent]
    });
    fixture = TestBed.createComponent(AdminDataScreensComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
