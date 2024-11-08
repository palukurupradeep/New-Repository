import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveRgaComponent } from './remove-rga.component';

describe('RemoveRgaComponent', () => {
  let component: RemoveRgaComponent;
  let fixture: ComponentFixture<RemoveRgaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RemoveRgaComponent]
    });
    fixture = TestBed.createComponent(RemoveRgaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
