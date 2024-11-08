import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogSpecComponent } from './product-catalog-spec.component';

describe('ProductCatalogSpecComponent', () => {
  let component: ProductCatalogSpecComponent;
  let fixture: ComponentFixture<ProductCatalogSpecComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCatalogSpecComponent]
    });
    fixture = TestBed.createComponent(ProductCatalogSpecComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
