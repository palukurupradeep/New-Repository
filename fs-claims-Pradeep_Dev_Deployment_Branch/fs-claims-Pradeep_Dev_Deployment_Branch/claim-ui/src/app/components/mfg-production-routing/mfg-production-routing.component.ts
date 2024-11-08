import { Component, OnInit } from '@angular/core';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';

@Component({
  selector: 'app-mfg-production-routing',
  templateUrl: './mfg-production-routing.component.html',
  styleUrls: ['./mfg-production-routing.component.scss']
})
export class MfgProductionRoutingComponent implements OnInit{

  mfgProductionRoutingData: { process: string, plant: string }[] = [];

  constructor(private productCatalogSpecService: ProductCatalogSpecService) {}

  ngOnInit(): void {
    this.getMfgProductionRouting();
  }

  getMfgProductionRouting(){
    this.productCatalogSpecService.getMfgProductionRoutingData().subscribe((data) => {
      this.mfgProductionRoutingData = data;
    });
  }

}
