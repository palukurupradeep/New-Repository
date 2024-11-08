import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-maintain-disposition',
  templateUrl: './maintain-disposition.component.html',
  styleUrls: ['./maintain-disposition.component.scss']
})
export class MaintainDispositionComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public claimNumber: string = '';
  public selectedLine: any = [];
  public claimAddresses: any[] = [];
  public claimId: number = 0;
  public claimDocuments: any[] = [];
  public documentNumber:string = '';
  public claimAddressesData: any[] = [];
  public sellingCompanyData:any = [];
  public noteText: any = [];
  public dropDwnDispositionvalues: any = [];
  public userId!: number;
  public dispositionForm!: FormGroup;
  public specialInstComments:string = '';
  public dispoTypedata:string = '';
  public selectedNote:any;
  public stadardMsg: string = '';
  public endUserInfo: any ;
  public fetchLinedata: any;
  public dispotypeData:string = '';
  public  totalAmountUsd: number = 0;


  constructor(
    private bodyClassService: BodyClassService,
    private route: ActivatedRoute,
    private claimdetailservice:ClaimDetailService,
    private notesTasksService:NotesTasksService,
    private form: FormBuilder,
    private router: Router,
    private toasterService: ToasterService) { }

    ngOnInit(){
      this.userId =Number(localStorage.getItem("userId"));
      this.dispositionForm = this.form.group({
        attn: [''],
        phone: [''],
        fax: [''],
        email: [''],
        notetemplate:[''],
        specialinstructions:['']
      })
      this.route.paramMap.subscribe(params => {
        // this.selectedLine = window.history.state.selectedLine;
        // console.log(window.history.state.selectedLine, '..........123')
        // this.claimAddresses = window.history.state.claimAddresses;
        // console.log(window.history.state.claimAddresses, '..........123')
        this.claimId = window.history.state.claimId;
        // console.log(window.history.state.claimId, '..........claimId')
        // this.claimDocuments = window.history.state.claimDocuments;
        //  console.log(window.history.state.claimDocuments, '..........123')
        // this.claimNumber =window.history.state.claimNumber;
        this.claimNumber = window.history.state.claimNumber;
        this.documentNumber= window.history.state.documentNumber;
        // this.endUserInfo = window.history.state.endUserInfo
        //   console.log(window.history.state.endUserInfo, '..........endUserInfo')
          // this.sellingCompanyData = window.history.state.sellingCompanyDetails;
        this.claimAddresses.forEach(address => {
          // need to check which id to be passed
          if (address.addressTypeId === 6) {
            console.log(address, 'address')
            const { fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode, emailAddress, faxNumber, phoneNumber } = address;
            this.claimAddressesData.push({ fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode, emailAddress, faxNumber, phoneNumber });
          }
  
        });
      });
      this.fetchLineDetails();
      this.fetchNoteTemplateBynoteTypeCode();
      this.dispositionForm.disable();
    }

    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }
  
    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

   
    public fetchNoteTemplateBynoteTypeCode() {
      this.notesTasksService.fetchNoteTemplateBynoteTypeCode('DISPOSITIO').subscribe((data: any) => {
        this.noteText = data.body
  
      })
  
    }
    
    public openVoidModal(){
      const modelDiv = document.getElementById('voidModalDispo');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
    }

    public closeModal(){
      const modelDiv = document.getElementById('voidModalDispo');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
      
    }
    public confirmToClaimdetail(){
      this.claimdetailservice.dispositonVoid(this.fetchLinedata.claimDispositionHeaderId).subscribe(
        data=>{
        this.closeModal();
        this.toasterService.showToast('success', 'Disposition Voided Successfully');
        this.router.navigate(['/claim-detail', this.claimNumber]);
        
      },
    error=>{
      this.closeModal();
      this.toasterService.showToast('error', 'Disposition Voided Failed');
    })
     }

    public navToClaimdetail(){
      this.router.navigate(['/claim-detail', this.claimNumber]);
    }

  public fetchLineDetails(){
    this.claimdetailservice.fetchDispositonDetailsByClaimIdAndDispositonNumber(this.claimId,this.documentNumber).subscribe(data=>{
      this.fetchLinedata = data.body
      this.selectedNote = this.fetchLinedata.noteTemplateId
      this.dispositionForm.patchValue({
        attn:this.fetchLinedata.attentionName ,
        phone: this.fetchLinedata.phoneNumber,
        fax: this.fetchLinedata.faxNumber,
        email: this.fetchLinedata.emailAddress,
        specialinstructions:this.fetchLinedata.specialInstructions
      })
      this.dispotypeData = this.fetchLinedata.dispositionType
      this.calculateTotalAmountUsd();
    })

  }
 

  public getdispositioType(){
    return this.fetchLinedata.dispositionType
  }

  public  calculateTotalAmountUsd(): void {
    if (this.fetchLinedata && this.fetchLinedata.claimDispositionLineDTO) {
      this.totalAmountUsd = this.fetchLinedata.claimDispositionLineDTO.reduce((total:any, line:any) => total + line.amountUsd, 0);
      // console.log('Total Amount USD:', this.totalAmountUsd);
    }
  }
}
