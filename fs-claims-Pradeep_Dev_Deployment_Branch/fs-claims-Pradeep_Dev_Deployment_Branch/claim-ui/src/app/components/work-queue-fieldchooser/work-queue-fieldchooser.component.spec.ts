import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkQueueFieldchooserComponent } from './work-queue-fieldchooser.component';

describe('WorkQueueFieldchooserComponent', () => {
  let component: WorkQueueFieldchooserComponent;
  let fixture: ComponentFixture<WorkQueueFieldchooserComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WorkQueueFieldchooserComponent]
    });
    fixture = TestBed.createComponent(WorkQueueFieldchooserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
