import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TotalItemizedCostComponent } from './total-itemized-cost.component';

describe('TotalItemizedCostComponent', () => {
  let component: TotalItemizedCostComponent;
  let fixture: ComponentFixture<TotalItemizedCostComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TotalItemizedCostComponent]
    });
    fixture = TestBed.createComponent(TotalItemizedCostComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
