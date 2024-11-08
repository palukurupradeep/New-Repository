import { Component, OnInit } from '@angular/core';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';

@Component({
  selector: 'app-product-catalog-spec-view',
  templateUrl: './product-catalog-spec-view.component.html',
  styleUrls: ['./product-catalog-spec-view.component.scss']
})
export class ProductCatalogSpecViewComponent  implements OnInit{

  productSpecifications: { key: string, value: string }[] = [];

  constructor(private productCatalogSpecService: ProductCatalogSpecService) {}

  ngOnInit(): void {
    this.getProductSpecifications();
    
  }

  getProductSpecifications(): void {
    this.productCatalogSpecService.getProductSpecifications()
      .subscribe((data: { [key: string]: string }) => {
        this.productSpecifications = Object.entries(data).map(([key, value]) => ({ key, value }));
      });
  }

}
