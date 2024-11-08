import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogSpecViewColorsComponent } from './product-catalog-spec-view-colors.component';

describe('ProductCatalogSpecViewColorsComponent', () => {
  let component: ProductCatalogSpecViewColorsComponent;
  let fixture: ComponentFixture<ProductCatalogSpecViewColorsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCatalogSpecViewColorsComponent]
    });
    fixture = TestBed.createComponent(ProductCatalogSpecViewColorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
