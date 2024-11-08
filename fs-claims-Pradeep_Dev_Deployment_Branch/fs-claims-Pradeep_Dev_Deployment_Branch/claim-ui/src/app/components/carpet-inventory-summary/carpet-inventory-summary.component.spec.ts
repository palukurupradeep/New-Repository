import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarpetInventorySummaryComponent } from './carpet-inventory-summary.component';

describe('CarpetInventorySummaryComponent', () => {
  let component: CarpetInventorySummaryComponent;
  let fixture: ComponentFixture<CarpetInventorySummaryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CarpetInventorySummaryComponent]
    });
    fixture = TestBed.createComponent(CarpetInventorySummaryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
