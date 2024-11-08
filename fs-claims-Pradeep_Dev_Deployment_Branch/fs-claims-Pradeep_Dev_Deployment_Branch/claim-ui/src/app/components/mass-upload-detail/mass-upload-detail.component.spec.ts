import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MassUploadDetailComponent } from './mass-upload-detail.component';

describe('MassUploadDetailComponent', () => {
  let component: MassUploadDetailComponent;
  let fixture: ComponentFixture<MassUploadDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MassUploadDetailComponent]
    });
    fixture = TestBed.createComponent(MassUploadDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
