import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShopOrderRollsComponent } from './shop-order-rolls.component';

describe('ShopOrderRollsComponent', () => {
  let component: ShopOrderRollsComponent;
  let fixture: ComponentFixture<ShopOrderRollsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShopOrderRollsComponent]
    });
    fixture = TestBed.createComponent(ShopOrderRollsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
