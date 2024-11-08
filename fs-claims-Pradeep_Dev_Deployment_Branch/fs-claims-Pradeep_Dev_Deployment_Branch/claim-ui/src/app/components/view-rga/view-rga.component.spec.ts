import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewRgaComponent } from './view-rga.component';

describe('ViewRgaComponent', () => {
  let component: ViewRgaComponent;
  let fixture: ComponentFixture<ViewRgaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewRgaComponent]
    });
    fixture = TestBed.createComponent(ViewRgaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
