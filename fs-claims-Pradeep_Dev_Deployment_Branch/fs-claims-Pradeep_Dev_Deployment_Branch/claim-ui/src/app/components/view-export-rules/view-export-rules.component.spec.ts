import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewExportRulesComponent } from './view-export-rules.component';

describe('ViewExportRulesComponent', () => {
  let component: ViewExportRulesComponent;
  let fixture: ComponentFixture<ViewExportRulesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewExportRulesComponent]
    });
    fixture = TestBed.createComponent(ViewExportRulesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
