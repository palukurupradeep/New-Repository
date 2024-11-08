import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogSpecViewComponent } from './product-catalog-spec-view.component';

describe('ProductCatalogSpecViewComponent', () => {
  let component: ProductCatalogSpecViewComponent;
  let fixture: ComponentFixture<ProductCatalogSpecViewComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCatalogSpecViewComponent]
    });
    fixture = TestBed.createComponent(ProductCatalogSpecViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
