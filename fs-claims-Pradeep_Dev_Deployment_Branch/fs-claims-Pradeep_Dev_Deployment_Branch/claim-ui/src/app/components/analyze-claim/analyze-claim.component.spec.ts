import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyzeClaimComponent } from './analyze-claim.component';

describe('AnalyzeClaimComponent', () => {
  let component: AnalyzeClaimComponent;
  let fixture: ComponentFixture<AnalyzeClaimComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnalyzeClaimComponent]
    });
    fixture = TestBed.createComponent(AnalyzeClaimComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
