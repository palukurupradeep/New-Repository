import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DefectPercentComponent } from './defect-percent.component';

describe('DefectPercentComponent', () => {
  let component: DefectPercentComponent;
  let fixture: ComponentFixture<DefectPercentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DefectPercentComponent]
    });
    fixture = TestBed.createComponent(DefectPercentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
