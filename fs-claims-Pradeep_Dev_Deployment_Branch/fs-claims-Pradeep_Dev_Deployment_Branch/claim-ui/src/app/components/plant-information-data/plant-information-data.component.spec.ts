import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlantInformationDataComponent } from './plant-information-data.component';

describe('PlantInformationDataComponent', () => {
  let component: PlantInformationDataComponent;
  let fixture: ComponentFixture<PlantInformationDataComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlantInformationDataComponent]
    });
    fixture = TestBed.createComponent(PlantInformationDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
