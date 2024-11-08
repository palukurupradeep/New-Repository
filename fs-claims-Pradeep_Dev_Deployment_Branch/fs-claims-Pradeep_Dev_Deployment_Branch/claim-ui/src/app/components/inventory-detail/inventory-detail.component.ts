import { Component } from '@angular/core';
import { ProductCatalogService } from 'src/app/ngrx/store/services/product-catalog.service';

@Component({
  selector: 'app-inventory-detail',
  templateUrl: './inventory-detail.component.html',
  styleUrls: ['./inventory-detail.component.scss']
})
export class InventoryDetailComponent {

  inventoryData: any[] = []; // This will hold the inventory data to display in the table

  constructor(private ProductCatalogService: ProductCatalogService) {}

  ngOnInit(): void {
    // Subscribe to the inventory data updates
    this.ProductCatalogService.inventoryDetail$.subscribe((data) => {
      this.inventoryData = data;
    });
  }

}
