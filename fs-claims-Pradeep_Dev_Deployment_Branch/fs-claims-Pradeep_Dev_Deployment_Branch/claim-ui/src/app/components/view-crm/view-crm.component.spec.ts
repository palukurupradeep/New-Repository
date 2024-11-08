import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewCrmComponent } from './view-crm.component';

describe('ViewCrmComponent', () => {
  let component: ViewCrmComponent;
  let fixture: ComponentFixture<ViewCrmComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewCrmComponent]
    });
    fixture = TestBed.createComponent(ViewCrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
