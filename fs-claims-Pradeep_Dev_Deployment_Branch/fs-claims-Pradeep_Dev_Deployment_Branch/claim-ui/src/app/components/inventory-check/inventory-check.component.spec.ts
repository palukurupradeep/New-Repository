import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InventoryCheckComponent } from './inventory-check.component';

describe('InventoryCheckComponent', () => {
  let component: InventoryCheckComponent;
  let fixture: ComponentFixture<InventoryCheckComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InventoryCheckComponent]
    });
    fixture = TestBed.createComponent(InventoryCheckComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
