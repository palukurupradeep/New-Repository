import { Component } from '@angular/core';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';

@Component({
  selector: 'app-shop-order-rolls',
  templateUrl: './shop-order-rolls.component.html',
  styleUrls: ['./shop-order-rolls.component.scss']
})
export class ShopOrderRollsComponent {

  shopOrderRolls: { [key: string]: string }[] = [];

  constructor(private productCatalogSpecService: ProductCatalogSpecService) {}

  ngOnInit(): void {
    this.getShopOrderRollData();
  }

  getShopOrderRollData(): void {
    this.productCatalogSpecService.getShopOrderRollData()
      .subscribe((data: { [key: string]: string }[]) => {
        this.shopOrderRolls = data;
      });
  }

}
