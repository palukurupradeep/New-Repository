import { Component } from '@angular/core';
import { ProductCatalogService } from 'src/app/ngrx/store/services/product-catalog.service';

@Component({
  selector: 'app-plant-information-data',
  templateUrl: './plant-information-data.component.html',
  styleUrls: ['./plant-information-data.component.scss']
})
export class PlantInformationDataComponent {

  plantInformation: { key: string, value: string }[] = [];

  constructor(private productCatalogService: ProductCatalogService) { }

  ngOnInit(): void {
    this.getPlantInformation();
  }

  getPlantInformation(): void {
    this.productCatalogService.getPlantInformation()
      .subscribe(data => {
        this.plantInformation = [
          { key: 'Plant Manager', value: data.plantManager },
          { key: 'Quality Manager', value: data.qualityManager },
          { key: 'Quality Manager Phone Number', value: data.qualityManagerPhoneNumber },
          { key: 'Address 1', value: data.address1 },
          { key: 'Address 2', value: data.address2 },
          { key: 'City, State Zip', value: data.cityStateZip },
          { key: 'County', value: data.county },
          { key: 'Country', value: data.country },
          { key: 'Phone Number', value: data.phoneNumber }
        ];
      });
  }

}
