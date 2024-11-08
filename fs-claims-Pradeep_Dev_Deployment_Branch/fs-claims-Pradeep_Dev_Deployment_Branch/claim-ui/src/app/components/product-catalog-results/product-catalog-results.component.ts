import { Component } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { ProductCatalogService } from 'src/app/ngrx/store/services/product-catalog.service';


export interface Product {
  styleNumber: string;
  styleName: string;
  product: string;
  sellingCompanyCode: string;
  sellingCompany: string;
  status: string;
}

@Component({
  selector: 'app-product-catalog-results',
  templateUrl: './product-catalog-results.component.html',
  styleUrls: ['./product-catalog-results.component.scss']
})
export class ProductCatalogResultsComponent {


  productCatalog: Product[] = [];
  sortedProducts: Product[] = [];

  constructor( private ProductCatalogService: ProductCatalogService,) { }

  ngOnInit(): void {
    // Subscribe to the product catalog updates from the service
    this.ProductCatalogService.productCatalog$.subscribe(data => {
      this.productCatalog = data;
      this.sortedProducts = data.slice();
      console.log('Product Catalog results:', this.productCatalog);
    });
  }
  sortData(sort: Sort) {
    const data = this.productCatalog.slice(); // Use productCatalog for sorting

    if (!sort.active || sort.direction === '') {
      this.sortedProducts = data;
      return;
    }

    this.sortedProducts = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'styleNumber':
          return compare(a.styleNumber, b.styleNumber, isAsc);
        case 'styleName':
          return compare(a.styleName, b.styleName, isAsc);
        case 'product':
          return compare(a.product, b.product, isAsc);
        case 'sellingCompany':
          return compare(a.sellingCompany, b.sellingCompany, isAsc);
        case 'status':
          return compare(a.status, b.status, isAsc);
        default:
          return 0;
      }
    });
  }
}

// Comparison function for sorting
function compare(a: string | number, b: string | number, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
