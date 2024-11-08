import { TestBed } from '@angular/core/testing';

import { ProductCatalogSpecService } from './product-catalog-spec.service';

describe('ProductCatalogSpecService', () => {
  let service: ProductCatalogSpecService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductCatalogSpecService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
