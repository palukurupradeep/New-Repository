import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ViewNoteTemplateComponent } from './view-note-template.component';

describe('ViewNoteTemplateComponent', () => {
  let component: ViewNoteTemplateComponent;
  let fixture: ComponentFixture<ViewNoteTemplateComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ViewNoteTemplateComponent]
    });
    fixture = TestBed.createComponent(ViewNoteTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
