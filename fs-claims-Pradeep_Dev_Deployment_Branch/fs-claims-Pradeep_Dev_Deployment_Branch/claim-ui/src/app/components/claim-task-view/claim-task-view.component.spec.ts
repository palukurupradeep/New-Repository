import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimTaskViewComponent } from './claim-task-view.component';

describe('ClaimTaskViewComponent', () => {
  let component: ClaimTaskViewComponent;
  let fixture: ComponentFixture<ClaimTaskViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimTaskViewComponent]
    });
    fixture = TestBed.createComponent(ClaimTaskViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
