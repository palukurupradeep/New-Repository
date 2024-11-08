import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminCountConfigComponent } from './admin-count-config.component';

describe('AdminCountConfigComponent', () => {
  let component: AdminCountConfigComponent;
  let fixture: ComponentFixture<AdminCountConfigComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AdminCountConfigComponent]
    });
    fixture = TestBed.createComponent(AdminCountConfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
