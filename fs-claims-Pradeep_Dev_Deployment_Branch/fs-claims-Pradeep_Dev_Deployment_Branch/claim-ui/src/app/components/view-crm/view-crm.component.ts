import { Component, Input } from '@angular/core';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { Observable, forkJoin, map, tap } from 'rxjs';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-view-crm',
  templateUrl: './view-crm.component.html',
  styleUrls: ['./view-crm.component.scss']
})
export class ViewCrmComponent  {
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  crmDetailsByClaimIdAndCrmNumber: any[]=[];
  crmLineDetails: any[]=[];
  claimCrmHeaderId: number=0;
  crmDetails: any;
  crmNumber!:number;
  claimId:number = 0
  uniqueDocuments: any[]=[];
  claimAddresses: any[]=[];
claimAddressesData: any[]=[];
  claimDocuments: any[]=[];
  filteredDocuments: any[]=[];
  isAnyLineSelected = false;
  claimNumber: string='';
  customerNumber: string='';
  selectedLine: any = []
  public sellingCompanyData: any = [];
  public endUserInfo:any;
  crmHeaderId!: number;
  soldToAddress: any;
  shipToAddress: any;
  public noteTextArea: string = '';
  public noteTextDisable: boolean = true;
  public isButtonDisabled = false;
  crmAction: string = '';
  userName:string ='';
  currentDate: string='';
  saveAndIssuedData: any;
  constructor(
    private bodyClassService: BodyClassService,private claimDetailService:ClaimDetailService,
    private router: Router,
    private activatedroute: ActivatedRoute,
   private route: ActivatedRoute,
   private toasterService: ToasterService,
  private claimdetailservice:ClaimDetailService) {
    const today = new Date();
    this.currentDate = today.toLocaleDateString("en-US"); // Formats as MM/DD/YYYY
  }
   


  ngOnInit(): void {
    this.userName = localStorage.getItem("userName") ?? "";

    this.route.paramMap.subscribe(params => {
      this.selectedLine = window.history.state.selectedLine;
      console.log( this.selectedLine,'...........selectedLine viewcrm')
      this.claimAddresses = window.history.state.claimAddresses;
      this.claimId = window.history.state.claimId;
      this.claimDocuments= window.history.state.claimDocuments;
     this.claimNumber =window.history.state.claimNumber;
      this.crmNumber = window.history.state.crmNumber;
      this.endUserInfo =  window.history.state.endUserInfo;
      this.crmNumber =window.history.state.crmNumber;
      this.crmHeaderId =window.history.state.crmHeaderId;
      console.log(this.claimId,'claimId')
      this.viewCRMmodal(this.crmNumber,this.claimId);
      console.log(window.history.state.claimAddresses,'...........claimAddresses')
    
      this.soldToAddress = this.claimAddresses.find(
        (address: any) => address.addressTypeId === 14
      );
    });
    this.shipToAddress = this.claimAddresses.find(
      (address: any) => address.addressTypeId === 2
    );
  
    this.sellingCompany();
  }


  public viewCRMmodal(crmNumber:number,claimId:number){
  
//  this.claimDetailService.fetchCrmDetailsByClaimIdAndCrmNumber(claimId, crmNumber).subscribe(data => {
   
     this.crmDetails = JSON.parse(localStorage.getItem('setViewCrmEditDAta') || '{}');
     this.crmHeaderId = this.crmDetails.claimCrmHeaderId;
     this.customerNumber = this.crmDetails.claimCustomerNumber;
    console.log('Fetched CRM Details:', this.crmDetails);
   // this.claimCrmHeaderId=this.crmDetails.claimCrmHeaderId;

    // Reset the crmDetailsByClaimIdAndCrmNumber array and push the fetched data
    this.crmDetailsByClaimIdAndCrmNumber = [];
    this.crmDetailsByClaimIdAndCrmNumber.push(this.crmDetails);

    // Extract claimCrmLineDTO and check if it exists
    if (this.crmDetails && Array.isArray(this.crmDetails.claimCrmLineDTO)) {
        this.crmLineDetails = this.crmDetails.claimCrmLineDTO;
        console.log(this.crmLineDetails,'crmLineDetails........---------')
    } else {
        // Log an error message if claimCrmLineDTO is not found
        console.error('claimCrmLineDTO not found or is not an array');
        this.crmLineDetails = [];
    }

    // Log the extracted claimCrmLineDTO
    console.log('Extracted claimCrmLineDTO:', this.crmLineDetails);
//});
  
  }
  fetchServiceToKeepType(): Observable<any> {
    return this.claimDetailService.fetchServiceToKeepType().pipe(
      tap(data => console.log(data, 'ServiceToKeepType')),
      map(response => response.body)
    );
  }
  
  fetchDocumentsByClaimId(claimId: number): Observable<any> {
    return this.claimDetailService.fetchDocumentsByClaimId(claimId).pipe(
      tap(data => console.log(data, 'documentsByClaimId')),
      map(response => response.body)
    );
  }
  
  
  getTotalAmount(): number {
    let totalAmount = 0;
    if (this.crmLineDetails) {
      for (const line of this.crmLineDetails) {
        totalAmount += line.amountUsd; // Adjust calculation based on your data structure
      }
    }
    return totalAmount;
  }

    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }
  
    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public closeModal(id:string){
      const modelDiv = document.getElementById(id);
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
      if(id == 'CRM-issued'){
        this.router.navigate(['/claim-detail', this.claimNumber]);
      }
    }
  
    public crmIssuedmodal(status: string){
      console.log(this.crmDetails,'crmDetails...sabeorissue')
      console.log(this.crmHeaderId,'crmHeaderId')
      const saveCrmIssuePayload = {
        "createdByUserId": this.crmDetails.createdByUserId,
        "modifiedByUserId": this.crmDetails.modifiedByUserId,
        "claimCrmHeaderId": this.crmHeaderId,
        "crmStatusCode": status,
        "claimId": this.crmDetails.claimId,
        "claimCustomerNumber": this.crmDetails.claimCustomerNumber,
        "creditCustomerNumber":this.crmDetails.creditCustomerNumber,
        "claimReasonCode":   this.crmDetails.claimReasonCode ,                    
        "amountUsd":this.crmDetails.amountUsd || 0,
        "amountForeign": this.crmDetails.amountForeign || 0,
        "preparedByUserId":this.crmDetails.preparedByUserId,
        "currentUserId": this.crmDetails.currentUserId,
        "issuedByUserId": this.crmDetails.issuedByUserId,
        "associatedRgaNumber": this.crmDetails.associatedRgaNumber,
        "emailAddress":this.crmDetails.emailAddress,
        "showUserName":this.crmDetails.showUserName,
      "showInvoice" :this.crmDetails.showInvoice,
     "showCustomer" :this.crmDetails.showCustomer,
        "autoApply": this.crmDetails.autoApply,
        "doNotApply": this.crmDetails.doNotApply,
        "manualApply": this.crmDetails.manualApply,
        "leaveClaimOpen": this.crmDetails.leaveClaimOpen,
        "closeClaimWithTasks": this.crmDetails.closeClaimWithTasks,
        "leaveInspectionOpen": this.crmDetails.leaveInspectionOpen,
        "removeDispute": this.crmDetails.removeDispute,
        "atkAttempted": this.crmDetails.atkAttempted,
        "atkAccepted": this.crmDetails.atkAccepted,
        "stkAttempted": this.crmDetails.stkAttempted,
        "stkAccepted": this.crmDetails.stkAccepted,
        "associatedCcbNumber": this.crmDetails.associatedCcbNumber,
        "associatedDebitNumber": this.crmDetails.associatedDebitNumber,
        "generalLedgerNumber": this.crmDetails.generalLedgerNumber,
        "serviceToKeepTypeId": this.crmDetails.serviceToKeepTypeId,
         "comments": this.crmDetails.comments,
        "claimCrmLineDTO": this.crmLineDetails.map((line:any) => ({
          "claimCrmLineId": line.claimCrmLineId,
          "crmLineNumber": line.crmLineNumber,
          "claimLineId": line.claimLineId,
          "rollNumber": line.rollNumber,
          "styleNumber": line.styleNumber,
          "colorNumber": line.colorNumber,
          "quantity": line.quantity,
          //"issuedAmount": line.lineAmountUsd,
          "widthInFeet": line.widthInFeet || 0,
          "lengthInFeet": line.lengthInFeet || 0,
          "amountUsd": line.lineAmountUsd,
          "amountForeign": line.lineAmountForeign,
          "invoiceLineNumber":line.invoiceLineNumber,
          "lot":line.lot,
              "type":line.type,
               "grade":line.grade,
            "unitPriceUsd":line.unitPriceUsd,
            "reasonCode": line.reasonCode,
        }))
      };
      console.log(saveCrmIssuePayload,'saveCrmIssuePayload')
      // this.claimDetailService.creditMemoValidation(this.crmHeaderId, Number(localStorage.getItem("userId")),this.customerNumber,false).subscribe(
      //   (data) => {
         // console.log(data, 'actionData');
          
          // Call crmHoldOrIssue only after creditMemoValidation succeeds
          this.claimDetailService.crmHoldOrIssue(saveCrmIssuePayload).subscribe(
            (issueData) => {
               this.saveAndIssuedData = issueData.body;
              localStorage.removeItem('crmSelectedLine');
        localStorage.removeItem('setViewCrmEditDAta');
              console.log(issueData, 'issueData');
              const toastType: 'success' | 'error' | 'warning' = 'success'; 
          const message = 'CRM is successfully issued';
          this.toasterService.showToast(toastType, message);
          // this.router.navigate(['/claim-detail', this.claimNumber]);
              const modelDiv = document.getElementById('CRM-issued');
              if (modelDiv != null) {
                modelDiv.style.display = 'block';
              }
            },
            (error) => {
             
              const toastType: 'success' | 'error' | 'warning' = 'error'; 
              const message = 'CRM could not be issued. Try again.';
              this.toasterService.showToast(toastType, message);
             
            }
          );
      //   },
      //   (error) => {
      //     const toastType: 'success' | 'error' | 'warning' = 'error'; 
      //     const message = 'Error during creditMemoValidation. Try again.';
      //     this.toasterService.showToast(toastType, message);
      //     console.error('Error during creditMemoValidation:', error);
        
      //   }
      // );
          
    
    
    }
    navigateToEditCrm() {
      console.log(this.claimId, 'this.claimId');
      console.log(this.crmNumber, 'this.crmNumber');
      const navigationExtras: NavigationExtras = {
        state: {
        //  selectedLine:  this.selectedLine,
        //  claimAddresses: this.claimAddresses,
          
          claimId: this.claimId,
          crmNumber: this.crmNumber,
          crmHeaderId:this.crmHeaderId,
          claimNumber:this.claimNumber,
          customerNumber: this.customerNumber,
          claimDocuments : this.claimDocuments,
          
        }
      };
    
      this.router.navigate(['/edit-crm'], navigationExtras);
   
    }
    

    // updateCrmDetailsByAction(action:string){
    //   this.claimDetailService.updateCrmDetailsByAction(this.crmHeaderId,action,Number(localStorage.getItem("userId"))).subscribe(data=>{
    //     console.log(data,'actionData')
    //     const toastType: 'success' | 'error' | 'warning' = 'success'; 
    //     const message = `CRM ${action} successfully`;
    //             this.toasterService.showToast(toastType, message);
    //     this.router.navigate(['/claim-detail', this.claimNumber]);
    //   })
    // }
    // creditMemoValidation(){
    //   this.claimDetailService.creditMemoValidation(this.crmHeaderId, Number(localStorage.getItem("userId")),this.customerNumber).subscribe(data=>{
    //     console.log(data,'actionData')
    //   })
    // }
    navigateToClaimDetail(){
      this.router.navigate(['/claim-detail', this.claimNumber]);
    }

    openPrepareDisposition(){
   
   const navigationExtras: NavigationExtras = {
    state: {
      selectedLine: this.selectedLine,
      claimAddresses: this.claimAddresses,
      claimId: this.claimId,
      claimNumber:this.claimNumber,
      customerNumber: this.customerNumber,
      claimDocuments: this.claimDocuments,
      sellingCompanyDetails:this.sellingCompanyData ,
      endUserInfo:this.endUserInfo,
      crmNumber:this.crmNumber,
       dispositionType:'CRM'
    }
  };

    
    this.router.navigate(['/prepare-disposition'], navigationExtras);
  
     
    }

    public sellingCompany(){
      this.claimdetailservice.getAllSellingCompanyData().subscribe(
        (data: any) => {
          this.sellingCompanyData = data.filter((ele:any)=>ele.sellingCompanyCode == this.selectedLine[0].selectedLine.sellingCompany)
         
          
        })
    }
    updateCrmDetailsByAction(action: string) {
      const modelDiv = document.getElementById('modalEnterClaim');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      this.crmAction = action;}
    public CloseModalEnterClaim() {
      this.isButtonDisabled = false;
      const modelDiv = document.getElementById('modalEnterClaim');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
    }
   
    updatecrm()
    {
      if(this.crmHeaderId){
      this.claimDetailService.updateCrmDetailsByAction(this.crmHeaderId, this.crmAction, Number(localStorage.getItem("userId")), this.noteTextArea).subscribe(data => {
        console.log(data, 'actionData')
        const toastType: 'success' | 'error' | 'warning' = 'success';
        const message = `CRM ${this.crmAction} successfully`;
        this.toasterService.showToast(toastType, message);
        this.router.navigate(['/claim-detail', this.claimNumber]);
      })
    } else {
      this.router.navigate(['/claim-detail', this.claimNumber]);
    }
    }
}
