import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkQueueTableComponent } from './work-queue-table.component';

describe('WorkQueueTableComponent', () => {
  let component: WorkQueueTableComponent;
  let fixture: ComponentFixture<WorkQueueTableComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkQueueTableComponent]
    });
    fixture = TestBed.createComponent(WorkQueueTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
