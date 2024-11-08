import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RollLineageComponent } from './roll-lineage.component';

describe('RollLineageComponent', () => {
  let component: RollLineageComponent;
  let fixture: ComponentFixture<RollLineageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RollLineageComponent]
    });
    fixture = TestBed.createComponent(RollLineageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
