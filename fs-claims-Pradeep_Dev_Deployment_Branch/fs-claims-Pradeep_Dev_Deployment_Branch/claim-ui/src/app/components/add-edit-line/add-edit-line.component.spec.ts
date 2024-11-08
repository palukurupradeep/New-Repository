import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddEditLineComponent } from './add-edit-line.component';

describe('AddEditLineComponent', () => {
  let component: AddEditLineComponent;
  let fixture: ComponentFixture<AddEditLineComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddEditLineComponent]
    });
    fixture = TestBed.createComponent(AddEditLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
