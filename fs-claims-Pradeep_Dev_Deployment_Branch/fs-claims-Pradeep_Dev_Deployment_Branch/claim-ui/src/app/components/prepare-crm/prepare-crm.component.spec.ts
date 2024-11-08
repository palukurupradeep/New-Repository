import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareCrmComponent } from './prepare-crm.component';

describe('PrepareCrmComponent', () => {
  let component: PrepareCrmComponent;
  let fixture: ComponentFixture<PrepareCrmComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepareCrmComponent]
    });
    fixture = TestBed.createComponent(PrepareCrmComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
