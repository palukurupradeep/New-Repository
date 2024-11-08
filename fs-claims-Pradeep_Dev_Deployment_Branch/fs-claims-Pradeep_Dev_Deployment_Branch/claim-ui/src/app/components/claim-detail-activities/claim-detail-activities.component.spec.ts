import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ClaimDetailActivitiesComponent } from './claim-detail-activities.component';

describe('ClaimDetailActivitiesComponent', () => {
  let component: ClaimDetailActivitiesComponent;
  let fixture: ComponentFixture<ClaimDetailActivitiesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClaimDetailActivitiesComponent]
    });
    fixture = TestBed.createComponent(ClaimDetailActivitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
