import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductCatalogService } from 'src/app/ngrx/store/services/product-catalog.service';

@Component({
  selector: 'app-product-catalog-specifications',
  templateUrl: './product-catalog-specifications.component.html',
  styleUrls: ['./product-catalog-specifications.component.scss']
})
export class ProductCatalogSpecificationsComponent {

  productSpecForm!: FormGroup;
  productOptions: any[] = [];
  productCatalog: any[] = [];

  constructor(private ProductCatalogService: ProductCatalogService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.productSpecificationForm();
    this.loadProductOptions();

    // Subscribe to product selection changes
    this.productSpecForm.get('product')?.valueChanges.subscribe(() => {
      this.resetProductCatalog();
    });
  }


  productSpecificationForm(): void {
    this.productSpecForm = this.fb.group({
      product: ['',Validators.required],
      styleNumber: [''],
      styleName: [''],
      custRefNumber: [''],
      stdWidth: [{ value: '144', disabled: true }]
    });
  }

   // Reset the product catalog when the product selection changes
   resetProductCatalog(): void {
    this.ProductCatalogService.resetProductCatalog();  // Reset in the service
  }

  // Load product options from the service
  loadProductOptions(): void {
    this.ProductCatalogService.getProductOptions().subscribe(data => {
      this.productOptions = data;
    });
  }

  onFind(): void {
    const productPayload = this.productSpecForm.value;  // Get the form values as payload
    console.log('Payload:', productPayload);
    // Call the service method with the payload
    this.ProductCatalogService.searchProductSpecifications(productPayload).subscribe(data => {
      this.productCatalog = data;  // Store the response in productCatalog array
      console.log('Product Catalog:', this.productCatalog);
    });
  }
}
