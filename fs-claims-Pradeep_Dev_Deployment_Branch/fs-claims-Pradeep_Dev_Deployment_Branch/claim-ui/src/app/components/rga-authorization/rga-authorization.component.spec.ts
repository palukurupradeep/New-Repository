import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RGAAuthorizationComponent } from './rga-authorization.component';

describe('RGAAuthorizationComponent', () => {
  let component: RGAAuthorizationComponent;
  let fixture: ComponentFixture<RGAAuthorizationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RGAAuthorizationComponent]
    });
    fixture = TestBed.createComponent(RGAAuthorizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
