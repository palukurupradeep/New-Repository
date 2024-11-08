import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewViewRgaComponent } from './new-view-rga.component';

describe('NewViewRgaComponent', () => {
  let component: NewViewRgaComponent;
  let fixture: ComponentFixture<NewViewRgaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewViewRgaComponent]
    });
    fixture = TestBed.createComponent(NewViewRgaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
