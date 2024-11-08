import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeclineClaimComponent } from './decline-claim.component';

describe('DeclineClaimComponent', () => {
  let component: DeclineClaimComponent;
  let fixture: ComponentFixture<DeclineClaimComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DeclineClaimComponent]
    });
    fixture = TestBed.createComponent(DeclineClaimComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
