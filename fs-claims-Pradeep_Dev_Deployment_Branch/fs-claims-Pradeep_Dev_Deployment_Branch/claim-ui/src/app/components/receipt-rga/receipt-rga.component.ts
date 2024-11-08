import { Component,Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { FormBuilder, FormGroup } from '@angular/forms';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-receipt-rga',
  templateUrl: './receipt-rga.component.html',
  styleUrls: ['./receipt-rga.component.scss']
})
export class ReceiptRgaComponent {
  rgaForm:FormGroup;
  freightForm: FormGroup;
  selectedRga:any =null;
  searchRgaNumber: string = '';
  
  constructor(
    private bodyClassService: BodyClassService,
    private form: FormBuilder
  ){
    this.rgaForm = this.form.group({
      rganumber: ['']
    });
    this.freightForm = this.form.group({
      carrierCode: [''],
      name: [''],
      prepaidCollect: [''],
      billFreightNbr: [''],
      billFreightCharge: [''],
      dateTime: [''],
      status: ['']
    });
  }
 
  freightData=[
    {
      carrierCode: 'CC001',
      name: 'Show /Charlotte /M',
      prepaidCollect: 'Collect',
      billFreightNbr: 'BF123',
      billFreightCharge: '$100',
      dateTime: '03/17/2024 11:54AM',
      status: ''
    },
  {
      carrierCode: 'CC002',
      name: 'Show / New York / M',
      prepaidCollect: 'Prepaid',
      billFreightNbr: 'BF124',
      billFreightCharge: '$150',
      dateTime: '03/18/2024 10:30 AM',
      status: 'Completed',
  },
  ];
  RgaData = [
    {
      rgaNumber: 'RGA123',
      rgaDate: '04/18/2024',
      claimNumber: '648537 - 03/27/2024 (open)',
      customer: '0234533 - All trades contracting Inc, Clinton, NJ 08809',
      specialInstruction: 'Inv#2019054 ORD#506132 PO#Bernard'
    },
    {
      rgaNumber: 'RGA124',
      rgaDate: '05/22/2024',
      claimNumber: '648538 - 04/15/2024 (closed)',
      customer: '0243534 - Some Other Company, New York, NY 10001',
      specialInstruction: 'Inv#2019054 ORD#506132 PO#Bernard'
    },
    // Add more RGA records as needed
  ];

  
    @Input() screenWidth = 0;
    public isSideNavCollapsed: boolean = true;
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }
    ngOnInit(){
      
     
      // if (this.RgaData && this.RgaData.length > 0) {
      //   // Initialize with the first RGA in the list
      //   this.selectedRga = this.RgaData[0];
      // }
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public openModalnew(){
      const modelDiv = document.getElementById('openModalnew');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
    }
    
    public closeModal(id:string){
      const modelDiv = document.getElementById(id);
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }}

      updateFreight(){

      }
      filterRgaData() {
        console.log("Form Value: ", this.rgaForm.value);

  this.searchRgaNumber = this.rgaForm.get('rganumber')?.value;
  console.log("Search RGA Number: --------", this.searchRgaNumber);
        
        // Check if there's a value and trim any leading/trailing spaces
        if (this.searchRgaNumber && this.searchRgaNumber.trim()) {
          this.selectedRga = this.RgaData.find(rga => 
            rga.rgaNumber.trim().toLowerCase() === this.searchRgaNumber.trim().toLowerCase()
          );
    
          if (!this.selectedRga) {
            console.log("RGA not found!");
          } else {
            console.log("Selected RGA: ", this.selectedRga);
          }
      }
    }

      public openFreightModal(){
        const modelDiv = document.getElementById('freightModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
        }
      }
      
      public closeFreightModel(){
        const modelDiv = document.getElementById('freightModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'none';
        }}
      
      
}
