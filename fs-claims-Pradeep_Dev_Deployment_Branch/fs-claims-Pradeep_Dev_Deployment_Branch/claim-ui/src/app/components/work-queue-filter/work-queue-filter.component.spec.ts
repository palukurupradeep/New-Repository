import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkQueueFilterComponent } from './work-queue-filter.component';

describe('WorkQueueFilterComponent', () => {
  let component: WorkQueueFilterComponent;
  let fixture: ComponentFixture<WorkQueueFilterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkQueueFilterComponent]
    });
    fixture = TestBed.createComponent(WorkQueueFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
