import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ManagerCrmComponent } from './manager-crm.component';

describe('ManagerCrmComponent', () => {
  let component: ManagerCrmComponent;
  let fixture: ComponentFixture<ManagerCrmComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ManagerCrmComponent]
    });
    fixture = TestBed.createComponent(ManagerCrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
