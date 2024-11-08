import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminMenuConfigurationComponent } from './admin-menu-configuration.component';

describe('AdminMenuConfigurationComponent', () => {
  let component: AdminMenuConfigurationComponent;
  let fixture: ComponentFixture<AdminMenuConfigurationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminMenuConfigurationComponent]
    });
    fixture = TestBed.createComponent(AdminMenuConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
