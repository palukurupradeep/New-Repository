import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddTaskListComponent } from './add-task-list.component';

describe('AddTaskListComponent', () => {
  let component: AddTaskListComponent;
  let fixture: ComponentFixture<AddTaskListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AddTaskListComponent]
    });
    fixture = TestBed.createComponent(AddTaskListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
