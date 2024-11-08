import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AssociateDocsComponent } from './associate-docs.component';

describe('AssociateDocsComponent', () => {
  let component: AssociateDocsComponent;
  let fixture: ComponentFixture<AssociateDocsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AssociateDocsComponent]
    });
    fixture = TestBed.createComponent(AssociateDocsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
