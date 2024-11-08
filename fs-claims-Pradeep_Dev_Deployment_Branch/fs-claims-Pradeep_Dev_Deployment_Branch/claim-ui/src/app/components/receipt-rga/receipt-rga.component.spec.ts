import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceiptRgaComponent } from './receipt-rga.component';

describe('ReceiptRgaComponent', () => {
  let component: ReceiptRgaComponent;
  let fixture: ComponentFixture<ReceiptRgaComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReceiptRgaComponent]
    });
    fixture = TestBed.createComponent(ReceiptRgaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
