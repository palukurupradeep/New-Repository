import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { plantTableData, ProductCatalogSpecService, RaTableData, FormData, RaFormData } from 'src/app/ngrx/store/services/product-catalog-spec.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-process-rga-receipt',
  templateUrl: './process-rga-receipt.component.html',
  styleUrls: ['./process-rga-receipt.component.scss']
})
export class ProcessRgaReceiptComponent {

  plants: string[] = []; // Array to hold plant data
  selectedPlant!: string ; // Variable to hold the selected plant
  plantTableData: plantTableData[] = [];
  raTableData: RaTableData[] = [];
  formData!: FormData;
  form: FormGroup;
  raFormData!: RaFormData;




  constructor(
    private bodyClassService: BodyClassService,
    private fb: FormBuilder,
    private productCatalogSpecService: ProductCatalogSpecService) 
    { 
        this.form = this.fb.group({
          sendToPlant: [''],
          scrapqty: [''],
          trailerNumber: [''],
          date: [''],
          cmnt: [''],
        });
    }
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  ngOnInit(): void {
    this.loadPlants();
    this.loadPlantTableData();
    this.loadRaTableData();
    this.loadFormData();
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public confirmModal() {
    const modelDiv = document.getElementById('confirmModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public closeModal(id: string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  loadPlants(): void {
    this.productCatalogSpecService.getPlantsData().subscribe(
      (data) => {
        this.plants = data; // Assign fetched data to plants array
      },
      (error) => {
        console.error('Error fetching plants:', error);
      }
    );
  }

  loadPlantTableData(): void {
    this.productCatalogSpecService.getPlantTableData().subscribe(
      (data) => {
        this.plantTableData = data; // Assign fetched data to tableData array
      },
      (error) => {
        console.error('Error fetching table data:', error);
      }
    );
  }

  loadRaTableData(): void {
    this.productCatalogSpecService.getRaTableData().subscribe(
      (data) => {
        this.raTableData = data; // Assign fetched data to the tableData array
      },
      (error) => {
        console.error('Error fetching table data:', error);
      }
    );
  }

  loadFormData(): void {
    this.productCatalogSpecService.getFormData().subscribe(
      (data) => {
        this.formData = data; // Assign fetched data to the formData object
      },
      (error) => {
        console.error('Error fetching form data:', error);
      }
    );
  }

  loadRaFormData(): void {
    this.productCatalogSpecService.getRaFormData().subscribe((data) => {
      this.raFormData = data;

      // Patch the form with the received data
      this.form.patchValue({
        sendToPlant: this.raFormData?.sendToPlant[0], // Set the default plant
        scrapqty: this.raFormData?.quantity,
        trailerNumber: this.raFormData?.trailerNumber,
        date: this.raFormData?.expectedReceiveDate,
        cmnt: this.raFormData?.comment,
      });
    });
  }

}
