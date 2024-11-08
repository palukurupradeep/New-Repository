import { Component } from '@angular/core';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';

@Component({
  selector: 'app-product-catalog-claim-info',
  templateUrl: './product-catalog-claim-info.component.html',
  styleUrls: ['./product-catalog-claim-info.component.scss']
})
export class ProductCatalogClaimInfoComponent {

  claimData: any;
 
  constructor(private ProductCatalogSpecService: ProductCatalogSpecService) { }
 
 
  ngOnInit(): void {
    this.getClaimData();
  }
 
  // Fetch claim data from service
  getClaimData(): void {
    this.ProductCatalogSpecService.getClaimData().subscribe(data => {
      this.claimData = data;
    });
  }

}
