import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DuplicateComponent } from './duplicate.component';

describe('DuplicateComponent', () => {
  let component: DuplicateComponent;
  let fixture: ComponentFixture<DuplicateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DuplicateComponent]
    });
    fixture = TestBed.createComponent(DuplicateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
