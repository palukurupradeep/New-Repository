import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditRgaComponent } from './edit-rga.component';

describe('EditRgaComponent', () => {
  let component: EditRgaComponent;
  let fixture: ComponentFixture<EditRgaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EditRgaComponent]
    });
    fixture = TestBed.createComponent(EditRgaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
