import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MaintainDispositionComponent } from './maintain-disposition.component';

describe('MaintainDispositionComponent', () => {
  let component: MaintainDispositionComponent;
  let fixture: ComponentFixture<MaintainDispositionComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MaintainDispositionComponent]
    });
    fixture = TestBed.createComponent(MaintainDispositionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
