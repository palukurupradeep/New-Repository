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
  selector: 'app-prepare-disposition',
  templateUrl: './prepare-disposition.component.html',
  styleUrls: ['./prepare-disposition.component.scss']
})
export class PrepareDispositionComponent {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public dropDwnDispositionvalues: any = []
  public crmLineDetails: any[] = [];
  public claimAddresses: any[] = [];
  public claimAddressesData: any[] = [];
  public claimDocuments: any[] = [];
  public filteredDocuments: any[] = [];
  public isAnyLineSelected = false;
  public claimNumber: string = '';
  public selectedLine: any = [];
  public dispoLineDetails: any = [];
  public issueModalMsg: string = '';
  public deletelineItems: any;
  public selectedLineItems: any = [];
  public claimId: number[] = [];
  public dispositionForm!: FormGroup
  public noteText: any = [];
  public selectedNote:any;
  public userId!: number;
  public dispoTypedata:string = '';
  public sellingCompanyData:any = [];
  public customerNumber:number = 0;
  public specialInstComments:string = '';
  public stadardMsg: string = '';
  public endUserInfo:any;
  public crmNumber:number = 0;
  public dispositionType:string = '';
  public rgaDetails: any;


  constructor(
    private bodyClassService: BodyClassService,
    private claimdetailservice: ClaimDetailService,
    private route: ActivatedRoute,
    private form: FormBuilder,
    private router: Router,
    private notesTasksService: NotesTasksService,
    private toasterService: ToasterService,
    private claimService:ClaimDetailService) { }

  ngOnInit() {
//  console.log(this.dispositionType,'.......dispositionType')
    this.userId =Number(localStorage.getItem("userId"));
    this.dispositionForm = this.form.group({
      attn: [''],
      phone: [''],
      fax: [''],
      email: ['']
    })
    this.route.paramMap.subscribe(params => {
      this.dispositionType = window.history.state.dispositionType
      if( this.dispositionType == 'CRM'){
        this.selectedLine = window.history.state.selectedLine;
        // console.log(window.history.state.selectedLine, '..........123')
        this.claimAddresses = window.history.state.claimAddresses;
        // console.log(window.history.state.claimAddresses, '..........123')
        this.claimId = window.history.state.claimId;
        this.claimDocuments = window.history.state.claimDocuments;
        //  console.log(window.history.state.claimDocuments, '..........123')
        this.crmNumber =window.history.state.crmNumber;
        this.claimNumber =  window.history.state.claimNumber;
        this.customerNumber= window.history.state.customerNumber;
        this.endUserInfo =window.history.state.endUserInfo
          // console.log(window.history.state.endUserInfo, '..........endUserInfo')
          this.sellingCompanyData = window.history.state.sellingCompanyDetails;
          // console.log(window.history.state.sellingCompanyData, '..........sellingCompanyDetails')
        this.claimAddresses.forEach(address => {
          // need to check which id to be passed
          if (address.addressTypeId === 6) {
            console.log(address, 'address')
            const { fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode, emailAddress, faxNumber, phoneNumber } = address;
            this.claimAddressesData.push({ fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode, emailAddress, faxNumber, phoneNumber });
          }
          this.dispoTypedata = 'CREDIT'
        });
        this.dispositionForm.patchValue({
          attn:this.claimAddressesData[0].fullName,
          phone: this.claimAddressesData[0].phoneNumber,
          fax: this.claimAddressesData[0].faxNumber,
          email: this.claimAddressesData[0].emailAddress
        })
      
      } else if(this.dispositionType == 'DECLINE'||
        this.dispositionType == 'SAMPLE REQUESTED'||
        this.dispositionType == 'REQUEST INFORMATION'||
        this.dispositionType == 'PEND'
      ){
        // this.selectedLineItems = window.history.state.selectedDocument;
      console.log(this.dispositionType,'...dispositionType')
          this.selectedLine = window.history.state.selectedDocument;
          console.log(window.history.state.selectedDocument, '........ this.selectedLine')
          this.claimAddresses = window.history.state.claimAddresses;
          console.log(window.history.state.claimAddresses, '..........claimAddresses')
          this.claimId = window.history.state.claimId;
          this.claimDocuments = window.history.state.claimDocuments;
           console.log(window.history.state.claimDocuments, '..........claimDocuments')
          // this.crmNumber =window.history.state.crmNumber;
          this.claimNumber =  window.history.state.claimNumber;
          this.customerNumber= window.history.state.customerNumber;
          this.endUserInfo =window.history.state.endUserInfo
            // console.log(window.history.state.endUserInfo, '..........endUserInfo')
            this.sellingCompany(window.history.state.sellingCompanyDetails);
            console.log(window.history.state.sellingCompanyData, '..........sellingCompanyDetails')
          this.claimAddresses.forEach(address => {
            // need to check which id to be passed
            if (address.addressTypeId === 6) {
              console.log(address, 'address')
              const { fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode, emailAddress, faxNumber, phoneNumber } = address;
              this.claimAddressesData.push({ fullName, claimAddressId, addressLine1, addressLine2, city, countryId, postalCode, emailAddress, faxNumber, phoneNumber });
            }
          });
          this.dispositionForm.patchValue({
            attn:this.claimAddressesData[0].fullName,
            phone: this.claimAddressesData[0].phoneNumber,
            fax: this.claimAddressesData[0].faxNumber,
            email: this.claimAddressesData[0].emailAddress
          })
        
        

      }
      else{
        this.rgaDetails = window.history.state.rgaDetails
        this.dispositionForm.patchValue({
          attn: this.rgaDetails?.attentionName,
          phone: this.rgaDetails?.phoneNumber,
          fax: this.rgaDetails?.faxNumber,
          email: this.rgaDetails?.email
        })
        this.dispoTypedata = 'RGA'
      }
   
      

    });
  
    // this.selectedLine = window.history.state.selectedLine;
    this.getAllDispositionTypes();
    this.fetchNoteTemplateBynoteTypeCode();
    // this.sellingCompany();
  }

  public sellingCompany(id:any){
    this.claimService.getAllSellingCompanyData().subscribe(
      (data: any) => {
        this.sellingCompanyData = data.filter((ele:any)=>ele.sellingCompanyCode == id)
         
        
      })
  }



  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public getAllDispositionTypes() {
    this.claimdetailservice.getAllDispositionTypes().subscribe(data => {
      // this.dropDwnDispositionvalues = data.body
      if(this.dispositionType == 'CRM'){
        let dropDownValues = data.body.filter((ele:any)=>ele.dispositionTypeDescription == 'FIELD DESTROY' ||
        ele.dispositionTypeDescription == 'LABOR' || ele.dispositionTypeDescription == 'CREDIT'
        )
        this.dropDwnDispositionvalues = dropDownValues
      }else if(this.dispositionType == 'RGA'){
        let dropDownRGAValues = data.body.filter((ele:any)=>ele.dispositionTypeDescription == 'RGA')
        this.dropDwnDispositionvalues = dropDownRGAValues
      } else if(this.dispositionType == 'DECLINE'){
        let values = data.body.filter((ele:any)=>ele.dispositionTypeDescription == 'DECLINE')
        this.dropDwnDispositionvalues = values
        this.dispoTypedata = values[0].dispositionTypeDescription
      } else if(this.dispositionType == 'SAMPLE REQUESTED'){
        let values = data.body.filter((ele:any)=>ele.dispositionTypeDescription == 'SAMPLE REQUESTED')
        this.dropDwnDispositionvalues = values
        this.dispoTypedata = values[0].dispositionTypeDescription
      } else if(this.dispositionType == 'REQUEST INFORMATION'){
        let values = data.body.filter((ele:any)=>ele.dispositionTypeDescription == 'REQUEST MORE INFO')
        this.dropDwnDispositionvalues = values
        this.dispoTypedata = values[0].dispositionTypeDescription
      } else if(this.dispositionType == 'PEND'){
        let values = data.body.filter((ele:any)=>ele.dispositionTypeDescription == 'ALLOWANCE TO KEEP')
        this.dropDwnDispositionvalues = values
        this.dispoTypedata = values[0].dispositionTypeDescription
      }
    })
  }

  public closePrepareDispoScreen() {
    const modelDiv = document.getElementById('prepareDispoModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public closePrepareDispoModal() {
    const modelDiv = document.getElementById('prepareDispoModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.router.navigate(['/claim-detail', this.claimNumber]);
  }

  public closeModal(id:any) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
   
  }
  
  public closeModals(id:string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
   
  }

  public fetchNoteTemplateBynoteTypeCode() {
    this.notesTasksService.fetchNoteTemplateBynoteTypeCode('DISPOSITIO').subscribe((data: any) => {
      this.noteText = data.body

    })

  }

  public setNoteStandardMsg(){
    let msgArr = this.noteText.filter((ele:any)=>ele.noteTemplateId == this.selectedNote)
    this.stadardMsg = msgArr[0]?.noteTemplateText
  }

  public saveAsDraftdispo(type:string){
    const dispositionpayload = 
    {
      "createdByUserId": this.userId,
      "modifiedByUserId": this.userId,
      "claimDispositionHeaderId": 0,
      "dispositionStatus": "P",
      "dispositionType": this.dispoTypedata,
      "documentNumber": this.crmNumber,  
      "claimId":  this.claimId,
      "companyName":  this.sellingCompanyData[0].sellingCompanyName,
      "attentionName":this.dispositionForm.value.attn,
      "phoneNumber": this.dispositionForm.value.phone,
      "faxNumber": this.dispositionForm.value.fax,
      "endUserName": "john",
      "customerNumber":this.customerNumber,
      "claimReasonCode": this.selectedLine[0].selectedLine.claimReasonDefinition.claimReasonCode,
      "amountUsd":this.selectedLine[0].selectedLine.lineAmountUsd,
      "amountForeign": this.selectedLine[0].selectedLine.lineAmountForeign,
      "issuedByUserId": this.userId,
      "emailAddress":this.dispositionForm.value.email,
      "specialInstructions": this.specialInstComments,
      "noteTemplateId":this.selectedNote,
      "claimDispositionLineDTO": this.selectedLine.map((line:any) => ({
              "claimDispositionLineId": 0,
              "dispositionLineNumber": this.selectedLine.length,
              "claimLineId": line.claimLineId,
              "rollNumber": line.selectedLine.rollNumber,
              "styleNumber": line.selectedLine.styleNumber,
              "colorNumber": line.selectedLine.colorNumber,
              "quantity": line.selectedLine.quantity,
              "widthInFeet": line.selectedLine.widthInFeet || 0,
              "lengthInFeet": line.selectedLine.lengthInFeet || 0,
               "amountUsd": line.selectedLine.lineAmountUsd,
              "amountForeign": line.selectedLine.lineAmountForeign
      })
    )
  }
  if(type == 'issue'){
    dispositionpayload.dispositionStatus = 'I'
  }
  this.claimdetailservice.dispositionHoldOrIssue(dispositionpayload).subscribe(data=>{
   
     if(type=='issue'){
      this.toasterService.showToast('success', 'Disposition Issued Successfully');
      this.router.navigate(['/claim-detail', this.claimNumber]);
     }else{
      this.toasterService.showToast('success', 'Disposition saved As Draft Successfully');
     }
  },
  (error:any) => {
    this.toasterService.showToast('error', 'Failed To Save As Draft');
     
  })
  }

  public saveAsDraftRGA(type:string){
    const dispositionpayload = 
    {
      "createdByUserId": this.userId,
      "modifiedByUserId": this.userId,
      "claimDispositionHeaderId": 0,
      "dispositionStatus": "P",
      "dispositionType": this.dispoTypedata,
      "documentNumber": this.rgaDetails.rgaNumber,   
      "claimId":  this.rgaDetails.claimId,
      "companyName":  this.rgaDetails.sellingCompanyName,
      "attentionName": this.dispositionForm.value.attn || '',
      "phoneNumber": this.dispositionForm.value.phone || '',
      "faxNumber": this.dispositionForm.value.fax || '',
      "endUserName": this.rgaDetails.endUserName || '',
      "customerNumber":this.rgaDetails.customerNumber,
      "claimReasonCode": this.rgaDetails.claimReasonCode,
      "amountUsd":this.rgaDetails.claimRgaLineDTO[0].claimLineAmountUsd,
      "amountForeign": this.rgaDetails.claimRgaLineDTO[0].claimLineForeignAmountUsd,
      "issuedByUserId": this.userId,
      "emailAddress":this.dispositionForm.value.email || '',
      "specialInstructions": this.specialInstComments,
      "noteTemplateId":this.selectedNote,
      "claimDispositionLineDTO": this.rgaDetails.claimRgaLineDTO.map((line:any) => ({
              "claimDispositionLineId": 0,
              "dispositionLineNumber": this.rgaDetails.claimRgaLineDTO.length,
              "claimLineId": line.claimLineId,  
              "rollNumber": line.rollNumber,
              "styleNumber": line.styleNumber,
              "colorNumber": line.colorNumber,
              "quantity": line.issuedQuantity,
              "widthInFeet": line.issuedWidthInFeet || 0,
              "lengthInFeet": line.issuedLengthInFeet || 0,
               "amountUsd": line.lineAmount,
              "amountForeign": line.lineForeignAmountUsd
      })
    )
  }
  if(type == 'issue'){
    dispositionpayload.dispositionStatus = 'I'
  }
  this.claimdetailservice.dispositionHoldOrIssue(dispositionpayload).subscribe(data=>{
    if(type=='issue'){
      this.toasterService.showToast('success', 'Disposition Issued Successfully');
      this.router.navigate(['/claim-detail', this.rgaDetails.claimNumber]);
     }else{
      this.toasterService.showToast('success', 'Disposition saved As Draft Successfully');
     }
  },
  (error:any) => {
    this.toasterService.showToast('error', 'Failed To Save As Draft');
  })
  }

 public openIssueModal(){
  // console.log(this.selectedLine,'...........saveAsDraftdispo')
  if(this.dispoLineDetails[0].dispoType == 'FIELD DESTROY'){
    const modelDiv = document.getElementById('issue-Modal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.issueModalMsg = 'This line item has RGA, field destroy disposition is not allowed.'
  }else  if(this.dispoLineDetails[0].dispoType == 'LABOR' && this.dispoLineDetails[0].selectedLine.detailType.detailTypeCode == 'LABOR' ){
    const modelDiv = document.getElementById('issue-Modal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.issueModalMsg ='This line item is not of Labor type, labor disposition is not allowed.'
  }else{
    // issue btn API call //
    this.saveAsDraftdispo('issue')
  }
  
   
 }


 public isChecked(line:any){
  return this.dispoLineDetails.some((ele:any)=>ele.lineNumber == line.lineNumber)
 }

 public setLineData(line:any,isChecked:any){
  this.dispoLineDetails = [];
  if(isChecked){
    line.dispoType = this.dispoTypedata
    this.dispoLineDetails.push(line)
  }
  console.log( line.dispoType,'............... line.dispoType')
 }
 
 public openDeleteModal(line:any){
  this.deletelineItems = line;
  const modelDiv = document.getElementById('deleteModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
 }

 public deleteDispo(){

 }

 public totalCalculation(){
  let total = 0;
  if(this.dispositionType == 'CRM'){
    this.selectedLine.forEach((ele:any)=>{
      total = total + ele.selectedLine.unitPriceUsd 
    })
  }else{
    this.selectedLine.forEach((ele:any)=>{
      total = total + ele.unitPriceUsd 
    })
  }
  return total
 
 }
 
}
