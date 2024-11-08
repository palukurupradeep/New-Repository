import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogSpecificationsComponent } from './product-catalog-specifications.component';

describe('ProductCatalogSpecificationsComponent', () => {
  let component: ProductCatalogSpecificationsComponent;
  let fixture: ComponentFixture<ProductCatalogSpecificationsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCatalogSpecificationsComponent]
    });
    fixture = TestBed.createComponent(ProductCatalogSpecificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
