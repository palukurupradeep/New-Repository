import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlantInformationComponent } from './plant-information.component';

describe('PlantInformationComponent', () => {
  let component: PlantInformationComponent;
  let fixture: ComponentFixture<PlantInformationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PlantInformationComponent]
    });
    fixture = TestBed.createComponent(PlantInformationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
