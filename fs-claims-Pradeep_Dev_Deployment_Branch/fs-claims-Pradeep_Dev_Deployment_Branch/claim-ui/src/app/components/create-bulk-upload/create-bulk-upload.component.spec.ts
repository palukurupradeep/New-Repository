import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateBulkUploadComponent } from './create-bulk-upload.component';

describe('CreateBulkUploadComponent', () => {
  let component: CreateBulkUploadComponent;
  let fixture: ComponentFixture<CreateBulkUploadComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CreateBulkUploadComponent]
    });
    fixture = TestBed.createComponent(CreateBulkUploadComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
