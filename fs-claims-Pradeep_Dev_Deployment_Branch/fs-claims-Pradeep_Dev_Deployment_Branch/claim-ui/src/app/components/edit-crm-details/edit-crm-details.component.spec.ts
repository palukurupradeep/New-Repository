import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCrmDetailsComponent } from './edit-crm-details.component';

describe('EditCrmDetailsComponent', () => {
  let component: EditCrmDetailsComponent;
  let fixture: ComponentFixture<EditCrmDetailsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditCrmDetailsComponent]
    });
    fixture = TestBed.createComponent(EditCrmDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
