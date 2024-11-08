import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductCatalogService } from 'src/app/ngrx/store/services/product-catalog.service';

@Component({
  selector: 'app-view-inventory',
  templateUrl: './view-inventory.component.html',
  styleUrls: ['./view-inventory.component.scss']
})
export class ViewInventoryComponent {
  inventoryForm!: FormGroup;
  colorOptions: string[] = [];
  locationOptions: string[] = [];
  inventoryDetail: any[] =[];


  constructor(private fb: FormBuilder,
    private ProductCatalogService: ProductCatalogService) {

  }

  ngOnInit(): void {
    this.inventoryForm = this.fb.group({
      orderDate: ['', Validators.required],
      colorNumber: [],
      location: [],
      grade: [],
      refNumber: []
      // Add other controls as necessary
    });
    
    this.inventoryForm.get('product')?.valueChanges.subscribe(() => {
      this.resetInventoryData();
    });

    // Fetch mock data for color and location options
    this.ProductCatalogService.getColorOptions().subscribe(colors => this.colorOptions = colors);
    this.ProductCatalogService.getLocationOptions().subscribe(locations => this.locationOptions = locations);
  }

  resetInventoryData(): void {
    this.ProductCatalogService.resetInventoryDetail();  // Reset in the service
  }

  onSubmit() {
    
    if (this.inventoryForm.valid) {
      console.log('Form submitted:', this.inventoryForm.value);
      const formData = this.inventoryForm.value;
      // Call the service and pass formData (including order date)
      this.ProductCatalogService.getInventoryData(formData).subscribe((data) => {
        this.inventoryDetail = data // Pass filtered data to result table
        console.log('inventoryDetail:', this.inventoryDetail);
      });
    }
  }

  onReset() {
    this.inventoryForm.reset();
  }
}
