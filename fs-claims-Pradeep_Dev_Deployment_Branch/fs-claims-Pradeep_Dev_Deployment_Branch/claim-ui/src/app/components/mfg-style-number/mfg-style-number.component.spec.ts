import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MfgStyleNumberComponent } from './mfg-style-number.component';

describe('MfgStyleNumberComponent', () => {
  let component: MfgStyleNumberComponent;
  let fixture: ComponentFixture<MfgStyleNumberComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MfgStyleNumberComponent]
    });
    fixture = TestBed.createComponent(MfgStyleNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
