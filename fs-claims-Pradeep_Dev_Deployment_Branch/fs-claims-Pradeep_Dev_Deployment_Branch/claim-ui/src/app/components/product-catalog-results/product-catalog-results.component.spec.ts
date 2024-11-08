import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogResultsComponent } from './product-catalog-results.component';

describe('ProductCatalogResultsComponent', () => {
  let component: ProductCatalogResultsComponent;
  let fixture: ComponentFixture<ProductCatalogResultsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCatalogResultsComponent]
    });
    fixture = TestBed.createComponent(ProductCatalogResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
