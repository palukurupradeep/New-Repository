import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductCatalogClaimInfoComponent } from './product-catalog-claim-info.component';

describe('ProductCatalogClaimInfoComponent', () => {
  let component: ProductCatalogClaimInfoComponent;
  let fixture: ComponentFixture<ProductCatalogClaimInfoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductCatalogClaimInfoComponent]
    });
    fixture = TestBed.createComponent(ProductCatalogClaimInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
