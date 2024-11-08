import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MfgProductionRoutingComponent } from './mfg-production-routing.component';

describe('MfgProductionRoutingComponent', () => {
  let component: MfgProductionRoutingComponent;
  let fixture: ComponentFixture<MfgProductionRoutingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MfgProductionRoutingComponent]
    });
    fixture = TestBed.createComponent(MfgProductionRoutingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
