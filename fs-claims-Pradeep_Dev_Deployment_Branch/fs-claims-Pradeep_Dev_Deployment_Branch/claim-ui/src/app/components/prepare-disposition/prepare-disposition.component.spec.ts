import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PrepareDispositionComponent } from './prepare-disposition.component';

describe('PrepareDispositionComponent', () => {
  let component: PrepareDispositionComponent;
  let fixture: ComponentFixture<PrepareDispositionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PrepareDispositionComponent]
    });
    fixture = TestBed.createComponent(PrepareDispositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
