import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListRgaComponent } from './list-rga.component';

describe('ListRgaComponent', () => {
  let component: ListRgaComponent;
  let fixture: ComponentFixture<ListRgaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ListRgaComponent]
    });
    fixture = TestBed.createComponent(ListRgaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
