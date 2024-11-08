import { Component,Input } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Sort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-resolution-maintenance',
  templateUrl: './resolution-maintenance.component.html',
  styleUrls: ['./resolution-maintenance.component.scss']
})
export class ResolutionMaintenanceComponent {

  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true; 
  resolutions: any;
  public resolutionsData:any
  public sortedresolutions :any;
  resolutionForm = this._formBuilder.group({
    // firstCtrl: ['', Validators.required],
    resolutionName: ['', Validators.required],
    description: ['', Validators.required],
    procedureManual: ['']
   
    });
  selectedResolution: any;
  type!: string;
  selectedId!: number;
  public tabSearchText: any = {};
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};

  constructor(private bodyClassService: BodyClassService, 
    private toasterService: ToasterService,
     private smartCodeService: SmartCodeService,
     private _formBuilder: FormBuilder,
     private router:Router) {}

  onToggleSideNav(data: SideNavToggle): void {``
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  ngOnInit() {
    this.getResolutions();
  }

  setFormValues(item: any, type: string) {
    if(type == 'edit') {
    this.resolutionForm.patchValue({
      resolutionName: item.resolutionName,
      description: item.description,
      procedureManual: item.procedureManual
    });
    this.selectedResolution = item;
  }
  else {
    this.resolutionForm.patchValue({
      resolutionName: '',
      description: '',
      procedureManual: ''
    });
    this.selectedResolution ={};
  }
  this.type= type;
  }

  getResolutions(){
    // let userId = Number(localStorage.getItem("userId"));
    this.smartCodeService.getAllClaimResolutions().subscribe(
      (response: any) => {
        this.resolutions = response.body;
        this.resolutionsData = response.body;
        this.sortedresolutions = response.body.slice()
        this.resolutions.map((item:any) => {
          item.procedureManual = 'https://' + item.procedureManual;
        })
       
        // this.resolutions.forEach((element:any)=>{
        //   element['selected'] = false;
        // })
      },
      (error: any) => {
        // console.error('Error fetching claim resolutions:', error);
      }
    );
  }

  closeModal() {
    const modelDiv = document.getElementById('deleteResolutionModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  openDeleteModel(id:number ) {
    const modelDiv = document.getElementById('deleteResolutionModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.selectedId = id;
  }

  deleteResolution() {
    this.smartCodeService.deleteResolutionId( this.selectedId).subscribe(data=>{
      this.toasterService.showToast('success', 'Resolution  deleted sucessfully');
    
      this.getResolutions();
      this.closeModal();
     },
     error=>{
      this.closeModal();
      this.toasterService.showToast('error', error.error.detail);
    
     })
    

  }

  closeResoltionModel(){
    const modelDiv = document.getElementById('addModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  addOrUpdateResolution() {
     // Extract the procedureManual value and handle the case where it might be null or undefined
     const procedureManual = this.resolutionForm.controls.procedureManual.value || '';
     const sanitizedProcedureManual = procedureManual.replace(/^https?:\/\//, ''); // Remove http:// or https://
 
    let payload = {};
    if(this.type == 'edit') {
     payload = {
      "resolutionId": this.selectedResolution.resolutionId,
  "resolutionName": this.resolutionForm.controls.resolutionName.value,
  "description": this.resolutionForm.controls.description.value,
  // "procedureManual": this.resolutionForm.controls.procedureManual.value,
      "procedureManual": sanitizedProcedureManual,
  "createdByUserId": this.selectedResolution.createdByUserId,
  "modifiedByUserId":  Number(localStorage.getItem('userId')),
    }
  }
    else {
      payload = {
    "resolutionName": this.resolutionForm.controls.resolutionName.value,
    "description": this.resolutionForm.controls.description.value,
    // "procedureManual": this.resolutionForm.controls.procedureManual.value,
    "procedureManual": sanitizedProcedureManual,
  "createdByUserId":  Number(localStorage.getItem('userId')),
  "modifiedByUserId":  Number(localStorage.getItem('userId')),
      }
    }
  

    this.smartCodeService.addOrupdateClaimResolutions(payload).subscribe((data:any)=>{
this.closeResoltionModel();
      if(this.type == 'edit') 
      {
      this.toasterService.showToast('success', 'Claim resolution updated successfully.');
     }
      else {
        this.toasterService.showToast('success', 'Claim resolution added successfully.');
      }
     this.getResolutions();
  });

}

navToDashboard(){
  this.router.navigate(['/dashboard'])
}

onTabSearch() {
  if (!this.tabSearchText.resolutionNameText && !this.tabSearchText.resolutionDescription 
    && !this.tabSearchText.proceduremanualUrl
     
) {
      this.sortedresolutions = this.resolutionsData;
  } else {
    if (
      this.tabSearchText.resolutionNameText != undefined &&
      this.tabSearchText.resolutionNameText != ''
    ) {
      this.sortedresolutions = this.resolutionsData.filter((user: any) => {
        if (user.resolutionName != null) {
          return user.resolutionName
            .toLowerCase()
            .includes(this.tabSearchText.resolutionNameText.toLowerCase());
        }
      });
    }
    if (
      this.tabSearchText.resolutionDescription != undefined &&
      this.tabSearchText.resolutionDescription != ''
    ) {
      this.sortedresolutions = this.resolutionsData.filter((user: any) => {
        if (user.description != null) {
          return user.description
            .toLowerCase()
            .includes(this.tabSearchText.resolutionDescription.toLowerCase());
        }
      });
    }
    if (
      this.tabSearchText.proceduremanualUrl != undefined &&
      this.tabSearchText.proceduremanualUrl != ''
    ) {
      this.sortedresolutions = this.resolutionsData.filter((user: any) => {
        if (user.procedureManual != null) {
          return user.procedureManual
            .toLowerCase()
            .includes(this.tabSearchText.proceduremanualUrl.toLowerCase());
        }
      });
    }
    
  }
}

sortData(sort: Sort) {
  const data = this.resolutions.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortedresolutions = data;
    return;
  }

  this.sortedresolutions = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'resolutionName':
        return this.compare(a.resolutionName, b.resolutionName, isAsc);
      case 'description':
        return this.compare(a.description, b.description, isAsc);
      case 'proceduremanual':
        return this.compare(a.procedureManual, b.procedureManual, isAsc);
      default:
        return 0;
    }
  });
}


// Comparison function for sorting
 compare(a: string | number, b: string | number, isAsc: boolean) {
return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

}