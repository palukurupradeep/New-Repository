import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReassignClaimComponent } from './reassign-claim.component';

describe('ReassignClaimComponent', () => {
  let component: ReassignClaimComponent;
  let fixture: ComponentFixture<ReassignClaimComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReassignClaimComponent]
    });
    fixture = TestBed.createComponent(ReassignClaimComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
