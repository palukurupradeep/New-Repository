import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagerGrpComponent } from './user-manager-grp.component';

describe('UserManagerGrpComponent', () => {
  let component: UserManagerGrpComponent;
  let fixture: ComponentFixture<UserManagerGrpComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UserManagerGrpComponent]
    });
    fixture = TestBed.createComponent(UserManagerGrpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
