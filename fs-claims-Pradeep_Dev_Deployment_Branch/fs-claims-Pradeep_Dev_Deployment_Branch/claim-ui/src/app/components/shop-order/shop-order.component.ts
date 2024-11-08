import { Component, Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-shop-order',
  templateUrl: './shop-order.component.html',
  styleUrls: ['./shop-order.component.scss']
})
export class ShopOrderComponent {

  shopOrderData: { [key: string]: string } = {};
  productionRoutingData: { process: string, plant: string }[] = [];

  constructor(
    private bodyClassService: BodyClassService,
    private productCatalogSpecService: ProductCatalogSpecService
  ) { }

  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  ngOnInit(): void {
    this.getShopOrder();
    this.getProductionRouting();
  }

  getShopOrder() {
    this.productCatalogSpecService.getShopOrderData().subscribe(data => {
      this.shopOrderData = data;
    });
  }

  getProductionRouting() {
    this.productCatalogSpecService.getProductionRoutingData().subscribe(data => {
      this.productionRoutingData = data;
    });
  }

}
