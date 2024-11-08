import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InspectionRequestComponent } from './inspection-request.component';

describe('InspectionRequestComponent', () => {
  let component: InspectionRequestComponent;
  let fixture: ComponentFixture<InspectionRequestComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InspectionRequestComponent]
    });
    fixture = TestBed.createComponent(InspectionRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
