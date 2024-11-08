import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EndUserClaimDetailComponent } from './end-user-claim-detail.component';

describe('EndUserClaimDetailComponent', () => {
  let component: EndUserClaimDetailComponent;
  let fixture: ComponentFixture<EndUserClaimDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EndUserClaimDetailComponent]
    });
    fixture = TestBed.createComponent(EndUserClaimDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
