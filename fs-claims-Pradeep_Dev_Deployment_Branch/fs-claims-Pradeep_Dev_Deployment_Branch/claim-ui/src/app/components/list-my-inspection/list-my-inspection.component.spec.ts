import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListMyInspectionComponent } from './list-my-inspection.component';

describe('ListMyInspectionComponent', () => {
  let component: ListMyInspectionComponent;
  let fixture: ComponentFixture<ListMyInspectionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListMyInspectionComponent]
    });
    fixture = TestBed.createComponent(ListMyInspectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
