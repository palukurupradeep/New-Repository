import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RollHistoryComponent } from './roll-history.component';

describe('RollHistoryComponent', () => {
  let component: RollHistoryComponent;
  let fixture: ComponentFixture<RollHistoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RollHistoryComponent]
    });
    fixture = TestBed.createComponent(RollHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
