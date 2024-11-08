import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessRgaReceiptComponent } from './process-rga-receipt.component';

describe('ProcessRgaReceiptComponent', () => {
  let component: ProcessRgaReceiptComponent;
  let fixture: ComponentFixture<ProcessRgaReceiptComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProcessRgaReceiptComponent]
    });
    fixture = TestBed.createComponent(ProcessRgaReceiptComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
