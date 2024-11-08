import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyWorkQueueComponent } from './my-work-queue.component';

describe('MyWorkQueueComponent', () => {
  let component: MyWorkQueueComponent;
  let fixture: ComponentFixture<MyWorkQueueComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyWorkQueueComponent]
    });
    fixture = TestBed.createComponent(MyWorkQueueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
