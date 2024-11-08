import { Component, EventEmitter, Input, OnInit, Output, TemplateRef, ViewChild } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';

import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ClaimAddresses, ClimDetailsResponce } from 'src/app/interfaces/claim-details.model';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { ActivatedRoute, NavigationExtras, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Location } from '@angular/common';
import { ToasterComponent } from '../toaster/toaster.component';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
let tempVal = '';
import { EndUserClaimDetailComponent } from '../end-user-claim-detail/end-user-claim-detail.component';
import { forkJoin } from 'rxjs';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';
@Component({
  selector: 'app-claim-detail',
  templateUrl: './claim-detail.component.html',
  styleUrls: ['./claim-detail.component.scss']
})
export class ClaimDetailComponent  implements OnInit{
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  public claimsIsVoid = false;
  @ViewChild(EndUserClaimDetailComponent) childComp: EndUserClaimDetailComponent | any;
  public isSideNavCollapsed: boolean = true;
  public customClass = 'customCard';
  public jobStop: boolean = false;
  public watchListToggle: boolean = false;
  public priorityclaimtoggle: boolean = false;
  public isMenuOpened: boolean = false;
  public claimDetails: any = [];
  public claimCustomerDetails: any = [];
  public claimList: any;
  public isactive: boolean = false;
  public isactives: boolean = true;
  public isError: boolean = false;
  public isDataLoaded: boolean = false;
  public userDetails: any = [];
  public managerDetails: any = [];
  public sellingCompanyData: any = [];
  public companyValue: any;
  public claimStatusDescription: string = '';
  public workStatusDescription: string = '';
  public claimNumber: any = '';
  public endUserInfo: any;
  public claimId: any = 0;
  public filteredClaimAddresses!: ClaimAddresses;
  public sellingCompanyvalue: string = '';
  public firstName: string = '';
  public lastName: string = '';
  public email: string = '';
  public phone: string = '';
  public noteTextArea:string = '';
  auditHistroyData: any[]=[];
  auditHistoryUserDetails: any;
  public customerdata:any;
  public contactHeader:boolean = false;
  public contactData:string = '';
  public contactInfo:boolean = false;
  public fetchNote:string = '';
  public countries:any = [];
  public filteredStates:any = [];
  public drpDwRreasonCode:any;
  public overRideErrMsg:any = '';
  
  public  toggleMenu(): void {
    this.isMenuOpened = !this.isMenuOpened;
  }
  public clickOutside(): void {
    this.isMenuOpened = false;
  }
  public modalRef?: BsModalRef;
  public  config = {
    backdrop: false,
    ignoreBackdropClick: false
  };
  public claimContactForm!: FormGroup;
  public priorityNote:any;
  public priorityNoteDisable : boolean = true;
 

  constructor(
    private modalService: BsModalService,
    private bodyClassService: BodyClassService,
    private claimService: ClaimDetailService,
    private userMappingService: UserMappingService,
    private route: ActivatedRoute,
    private snackBar: MatSnackBar,
    private formBuilder: FormBuilder,
    private location: Location,
    private noteTaskService:NotesTasksService,
    private router: Router,
    private toasterservice:ToasterService,
    private initialClaimService:InitiateClaimService,
    public authService: AuthService

  ) { 
    this.route.params.subscribe(params => {
      // Access the claimNumber parameter
      this.claimNumber = params['id'];
      // Load data based on the new claimNumber
      this.getClaimDetails();
    });
    this.initializeForm();
    // this.refreshClaimDetails();
    
  }
  ngOnInit(): void {
    this.userMappingService.fetchRolesAndPermissionByUserId(Number(localStorage.getItem('userId'))).subscribe(data => {
      const userPerData =data;
      
        const roles = userPerData.map((roleData:any) => roleData.roleCode);
    const permissions = userPerData.flatMap((roleData:any) => roleData.permissions);
    console.log(roles,'roles');
    console.log(permissions,'permissions');
   this.authService.setRolesAndPermissions(roles, permissions);
   const rolesfetch = this.authService.getRoles();
const permissionsfetch = this.authService.getPermissions();

console.log('Roles:', rolesfetch);
console.log('Permissions:', permissionsfetch);
  });
  }
  public fetchClaimDetails(claimNumber: string) {
    this.claimNumber = claimNumber;
    this.getClaimDetails();
  }
  public refreshClaimDetails() {
    this.getClaimDetails();
  }
  public getTotalSquareFeet(): number {
    let totalSquareFeet = 0;
  
    if (this.claimDetails && this.claimDetails.claimDocuments) {
      this.claimDetails.claimDocuments.forEach((claimDocument: any) => {
        if (claimDocument.claimLineDetails) {
          totalSquareFeet += claimDocument.claimLineDetails.reduce((sum: number, record: any) => {
            if (record.statusId !== 3 && record.detailType.detailTypeCode === "MERCH") {
              return sum + record.squareFeet;
            }
            return sum;
          }, 0);
        }
      });
    }
  
    return totalSquareFeet;
  }
  

  public getClaimedTotalSquareFeet(): number {
    let claimedTotalSquareFeet = 0;
  
    // Check if claimDetails and claimDocuments exist
    if (this.claimDetails && this.claimDetails.claimDocuments) {
      // Loop through each claimDocument
      this.claimDetails.claimDocuments.forEach((claimDocument: any) => {
        // Check if the claimDocument has claimLineDetails
        if (claimDocument.claimLineDetails) {
          claimDocument.claimLineDetails.forEach((claimLineDetail: any) => {
            // Check if the claimLineDetail meets the required conditions
            if (
              claimLineDetail.statusId !== 3 && 
              claimLineDetail.detailType?.detailTypeCode === "MERCH"
            ) {
              // Check if claimDetailRecords exist
              if (claimLineDetail.claimDetailRecords) {
                claimLineDetail.claimDetailRecords.forEach((claimDetailRecord: any) => {
                  // Check if the detailRecordTypeDescription is 'Claimed'
                  if (claimDetailRecord.detailRecordType?.detailRecordTypeDescription === 'Claimed') {
                    // Add squareFeet to the claimed total
                    claimedTotalSquareFeet += claimLineDetail.squareFeet || 0;
                  }
                });
              }
            }
          });
        }
      });
    }
  
    // Return the claimed total square feet
    return claimedTotalSquareFeet;
  }
  
  

  public getClaimDetails() {
     this.claimService.getClaim(this.claimNumber,Number(localStorage.getItem('userId'))).subscribe(
      (data: any) => {
        this.isDataLoaded = true;
        if (data == null) {
          alert("This claim is not available");
        } else {
          this.claimId = data.claimId;
          this.claimDetails = data;
          this.claimService.setClaimDetails(data);
          this.claimCustomerDetails = data;
          data.claimAddresses.forEach((address: any) => {
            if (address.addressTypeId === 6) {
              this.filteredClaimAddresses = address;
            }
          });
          this.jobStop = data.jobStopped ? data.jobStopped : false;
          this.watchListToggle = data.claimWatchlist && data.claimWatchlist.length && data.claimWatchlist[0].statusId === 1 ? true : false;

         // this.watchListToggle = data.claimWatchlist && data.claimWatchlist.length ? data.claimWatchlist : false;
          
          console.log(this.watchListToggle,'this.watchListToggle');
          this.priorityclaimtoggle = data.priorityClaim;
          if (data.endUserInformation) {
            this.endUserInfo = {
              "endUserInformation": {
                "createdByUserId": data.endUserInformation.createdByUserId,
                "modifiedByUserId": data.endUserInformation.modifiedByUserId,
                "createdDateTime": data.endUserInformation.createdDateTime,
                "modifiedDateTime": data.endUserInformation.modifiedDateTime,
                "endUserInformationId": data.endUserInformation.endUserInformationId,
                "firstName": data.endUserInformation.firstName,
                "middleInitial": data.endUserInformation.middleInitial,
                "lastName": data.endUserInformation.lastName,
                "companyName": data.endUserInformation.companyName,
                "countryId": data.endUserInformation.countryId,
                "addressLine1": data.endUserInformation.addressLine1,
                "addressLine2": data.endUserInformation.addressLine2,
                "city": data.endUserInformation.city,
                "stateId": data.endUserInformation.stateId,
                "postalCode": data.endUserInformation.postalCode,
                "county": data.endUserInformation.county,
                "businessPhoneDialCodeId": data.endUserInformation.businessPhoneDialCodeId,
                "businessPhoneNumber": data.endUserInformation.businessPhoneNumber,
                "businessPhoneExtension": data.endUserInformation.businessPhoneExtension,
                "homePhoneDialCodeId": data.endUserInformation.homePhoneDialCodeId,
                "homePhoneNumber": data.endUserInformation.homePhoneNumber,
                "homePhoneExtension": data.endUserInformation.homePhoneExtension,
                "cellPhoneDialCodeId": data.endUserInformation.cellPhoneDialCodeId,
                "cellPhoneNumber": data.endUserInformation.cellPhoneNumber,
                "cellPhoneExtension": data.endUserInformation.cellPhoneExtension,
                "emailAddress": data.endUserInformation.emailAddress,
                "statusId": data.endUserInformation.statusId
              }
            };
          }
        }
        if(data.claimUserId !== 0 ){
          // console.log('is inside claim')
          this.findByUserId(data.claimUserId);
        }
        
        this.getClaimStatusById(data.claimStatusId);
        // if(this.voidActionUserId){
        //   this.workStatusDescription = this.voidActionUserId.workStatusName
        // }else{
          this.getWorkStatusById(data.workStatusId,this.claimDetails.modifiedByUserId);
        // }
        this.getTotalSquareFeet();
        this.getClaimedTotalSquareFeet();
        this.companyValue = this.claimDetails.sellingCompany;
        this.setDefaultValue();
        this.getCountriesList();
        // this.getMergeClaimdata();
        this.claimService.getAllReasonCodes().subscribe(
          (data: any) => {
            this.claimList = data;
            this.drpDwRreasonCode = data.filter((ele:any)=>ele.claimReasonCode == this.claimDetails?.claimReasonDefinition?.claimReasonCode)[0]?.claimReasonCode
            // this.drpDwRreasonCode = this.claimDetails?.claimReasonDefinition?.claimReasonCode
           
          }
        );
      },
      (error) => {
        // this.snackBar.open('This claim is not available.', 'Close', {
        //   duration: 3000,
        //   verticalPosition: 'top',
        //   panelClass: ['custom-snackbar'], 
        // });
        const toastType: 'success' | 'error' | 'warning' = 'error'; 
    const message = 'This claim is not available.';
    this.toasterservice.showToast(toastType, message);
        
        // this.location.back(); 
      }
    );
  }

  public getCountriesList() {
    this.countries = [];
      this.initialClaimService.getCountriesList().subscribe(
        (data) => {
          this.countries = data;
         
        
          this.getCountryCodeByStates();
        }
      );
    
  }

  public countryName(){
    if(this.countries.length != 0){
      let country = this.countries.filter((ele:any)=>ele.isoCountryId == this.claimDetails.endUserInformation.countryId)
      return country[0].isoCountryCode
    }
    
  }

  public getCountryCodeByStates(){
    this.initialClaimService.getCountryCodeByStates(this.claimDetails.endUserInformation?.countryId ? this.claimDetails.endUserInformation.countryId : 240)
     .subscribe(
      (data) => {
        this.filteredStates = data;
     
      }
    )
  }

  public stateName(){
    if(this.filteredStates.length != 0){
      let states = this.filteredStates.filter((ele:any)=>ele.isoStateId == this.claimDetails.endUserInformation.stateId)
      return states[0]?.isoStateCode
    }
   
  }
  
  public fetchNoteTemplate(){
    this.noteTaskService.fetchNoteTemplateBynoteTypeCode('PRIORITY').subscribe((data:any)=>{
    this.priorityNote = data.body
    // this.noteTextArea = data.body.noteTemplateText
    // this.priorityNoteDisable = !data.body.editable
    })
  }
 
  public setDefaultValue() {
  this.claimService.getAllSellingCompanyData().subscribe(
    (data: any) => {
      this.sellingCompanyData = data;
      this.sellingCompanyData.forEach((company: any) => {
        if (company.sellingCompanyCode == this.claimDetails.sellingCompany) {
          this.sellingCompanyvalue = this.claimDetails.sellingCompany + " - " + company.sellingCompanyName;
          this.companyValue = company.sellingCompanyCode;
        }
      }
    );
    })
  }


  public findByUserId(id: number) {
    this.userMappingService.findByUserId(id).subscribe(
      (data) => {
        this.userDetails = data;
        if (data.userGroups.length > 0) {
          this.getManagerById(data.userId, data.userGroups[0].userGroupId);
        }
      }
    )
  }

  public getManagerById(userId: number, userGroupId: number) {
    this.claimService.getManager(userId, userGroupId).subscribe(
      (data) => {
        this.managerDetails = data;
        console.log(this.managerDetails)
      }
    )
  }

  public getClaimStatusById(id: number) {
    this.claimService.getClaimStatusById(id).subscribe(
      data => {
        this.claimStatusDescription = data.claimStatusDescription;
        if(data.claimStatusDescription == 'Closed' || data.claimStatusDescription == 'Voided' || data.claimStatusDescription == 'Declined' || data.claimStatusDescription == 'Pending'){
          this.claimsIsVoid = true
         }else{
          this.claimsIsVoid = false
         }
      }
    )
  }

  public getWorkStatusById(id: number, userId: number): void {
    this.claimService.getWorkStatusById(id, userId).subscribe(
      data => {
        // Check if the workStatusDescription is 'VOIDED'
        if (data.workStatusDescription === 'VOIDED') {
          // Display workStatusName if workStatusDescription is 'VOIDED'
          this.workStatusDescription = data.workStatusName;
        } else {
          // Otherwise, display workStatusDescription
          this.workStatusDescription = data.workStatusDescription;
        }
      });
  }
  

  public openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, this.config);
  }
  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  public getBodyClass(): string {
   
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }




  public jobStopClaim() {
    if (!this.claimsIsVoid) {
      this.jobStop = !this.jobStop;
     
      if(!this.jobStop){
        // this.priorityclaimtoggle = true;

        this.claimService.jobStopToggle(this.claimId, this.jobStop,Number(localStorage.getItem('userId'))).subscribe((Result: any) => {
        //  this.snackBar.open( 'Job Stop removed from claim', 'Close', {
        //     duration: 3000,
        //     verticalPosition: 'top',
        //     panelClass: ['custom-snackbar'],
        //   });
        const toastType: 'success' | 'error' | 'warning' = 'error'; 
    const message = 'Job Stop removed from claim';
    this.toasterservice.showToast(toastType, message);
         
        })
      }else{
        if(this.priorityclaimtoggle ){
          // this.openPriorityNoteModal();
         
        this.claimService.jobStopToggle(this.claimId, this.jobStop,Number(localStorage.getItem('userId'))).subscribe((Result: any) => {
          // this.snackBar.open( 'Job Stop added to  claim', 'Close', {
          //    duration: 3000,
          //    verticalPosition: 'top',
          //    panelClass: ['custom-snackbar'],
          //  });
          
          const toastType: 'success' | 'error' | 'warning' = 'success'; 
          const message = 'Job Stop added to  claim';
          this.toasterservice.showToast('success', 'Job Stop added to  claim');
         })
       
        }else if(this.jobStop && !this.priorityclaimtoggle){
          this.priorityclaimtoggle = this.jobStop ? true : this.claimDetails.priorityClaim;
          const modelDiv = document.getElementById('priorityclaim');
          if (modelDiv != null) {
            modelDiv.style.display = 'block';
          }
            this.fetchNoteTemplate();
        }
        
      }
   
    }

  }

  public watchListClaim() {
    if (!this.claimsIsVoid) {
      this.watchListToggle = !this.watchListToggle;
      
  const addtoWatchList = this.watchListToggle;
      const payload = {
        "claimId": this.claimId,
        "addtoWatchList": addtoWatchList,
        "userId": Number(localStorage.getItem('userId'))
      };
      this.claimService.watchListToggle(payload).subscribe((Result: any) => {;
        let message: string;
let toastType: 'success' | 'error' | 'warning';

// Determine the message and type based on the condition
if (this.watchListToggle) {
  message = 'Claim added to watch list successfully';
  toastType = 'success'; 
} else {
  message = 'Claim removed from watch list successfully';
  toastType = 'success'; 
}


this.toasterservice.showToast(toastType, message);
      })
    }
  }

  public enableJobstopAndPriority(){
    this.claimService.jobStopToggle(this.claimId, this.jobStop,Number(localStorage.getItem('userId'))).subscribe((Result: any) => {
      
      

      // this.snackBar.open('Job Stop added to claim', 'Close', {
      //   duration: 3000,
      //   verticalPosition: 'top',
      //   panelClass: ['custom-snackbar'],
      // });
      const toastType: 'success' | 'error' | 'warning' = 'success'; 
      const message = 'Job Stop added to claim';
      this.toasterservice.showToast('success', 'Job Stop added to  claim');
      
    })
  }

  public priorityClaimToggle() {
    if (!this.claimsIsVoid) {
      if(this.jobStop && this.priorityclaimtoggle){
        this.priorityclaimtoggle = true;
      }
      else if(!this.jobStop){
        this.priorityclaimtoggle = !this.priorityclaimtoggle;
      }

      if (this.jobStop) {
        this.claimService.jobStopToggle(this.claimId, this.jobStop, Number(localStorage.getItem('userId'))).subscribe((Result: any) => {
          // this.snackBar.open('Job Stop added to claim', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'],
          // });
          const toastType: 'success' | 'error' | 'warning' = 'success'; 
      const message = 'Job Stop added to claim';
      this.toasterservice.showToast('success', 'Job Stop added to  claim');
      
          // Introduce a delay of 2 seconds before calling priorityClaimToggle
          setTimeout(() => {
            this.claimService.priorityClaimToggle(this.claimId, this.priorityclaimtoggle, this.noteTextArea, Number(localStorage.getItem('userId'))).subscribe((Result: any) => {
              const modelDiv = document.getElementById('priorityclaim');
              if (modelDiv != null) {
                modelDiv.style.display = 'none';
              }
              // this.snackBar.open('Priority flag has been set on.', 'Close', {
              //   duration: 3000,
              //   verticalPosition: 'top',
              //   panelClass: ['custom-snackbar'],
              // });
              const toastType: 'success' | 'error' | 'warning' = 'success'; 
      const message = 'Priority flag has been set on.';
      this.toasterservice.showToast('success', 'Priority flag has been set on');
              this.fetchNote = this.noteTextArea;
              this.noteTextArea = '';
             
            });
          }, 1000); // 1000 milliseconds = 2 seconds
        });
      }
      

      
    else{
     
          this.claimService.priorityClaimToggle(this.claimId, this.priorityclaimtoggle ,this.noteTextArea,Number(localStorage.getItem('userId'))).subscribe((Result: any) =>
             {
            // this.closeModal('priorityclaim')
            const modelDiv = document.getElementById('priorityclaim');
            if (modelDiv != null) {
              modelDiv.style.display = 'none';
            }
            // this.snackBar.open('Priority flag has been set on.', 'Close', {
            //   duration: 3000,
            //   verticalPosition: 'top',
            //   panelClass: ['custom-snackbar'],
            // });
            const toastType: 'success' | 'error' | 'warning' = 'success'; 
            const message = 'Priority flag has been set on.';
            this.toasterservice.showToast('success', 'Priority flag has been set on');

            this.fetchNote = this.noteTextArea;
            this.noteTextArea = '';
        })
      }
     
     
    }
  
  }

  public checkNoteText(){
    if(this.noteTextArea.includes('#')){
      this.noteTextArea = this.noteTextArea.replace(/#/g,'')
    }

  }

  public inputValidator(event: any): void {
    const pattern = /[^A-Za-z0-9_@.,/#&+-\s]/g; // Updated pattern to exclude spaces
  
    const currentValue = event.target.value;
    if (pattern.test(currentValue)) {
      event.target.value = currentValue.replace(pattern, "");
    }
  }

 
  public validation(event: any): void {
    // Allows only letters, numbers, period, apostrophe, hyphen, and spaces
    const pattern = /[^A-Za-z0-9.\-'\s]/g; // Excludes invalid characters
    
    let currentValue = event.target.value;
    
    // Remove spaces at the start of the string
    currentValue = currentValue.replace(/^\s+/, "");
  
    // Ensure the first character is a letter (A-Z, a-z)
    if (currentValue.length > 0 && !/^[A-Za-z]/.test(currentValue)) {
      currentValue = currentValue.substring(1); // Remove the first character if it's not a letter
    }
  
    // Replace any characters that do not match the valid pattern
    if (pattern.test(currentValue)) {
      event.target.value = currentValue.replace(pattern, "");
    } else {
      event.target.value = currentValue;  // Update the field with the valid value
    }
    console.log(event.target.value,'.......contact')
  }

  public contactUpdateValidation(){
    if(this.claimContactForm.invalid){
      return true
  }else{
    if(this. claimContactForm.get('firstName')?.value == ' ' ||  
    this.claimContactForm.get('lastName')?.value == ' '){
      return true
    }else{
      return false
    }
  }
}
  

  public trimEmail(event: any): void {
    let inputValue = event.target.value;
    event.target.value = inputValue.trimStart();  // Remove leading spaces
  }
  
  

  public openEndUserDetails() {
    console.log(this.claimsIsVoid, document.getElementById('enduserinfo'), 'data');
   // if (!this.claimsIsVoid) {
    const modelDiv = document.getElementById('enduserinfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
 // }
  }
 public voidActionUserId : any
  public openBtnStatus(data: any) {
    // console.log(data,'..........openBtnStatus')
    this.voidActionUserId = data
     console.log(data,'..........openBtnStatus')
    this.getClaimDetails();
  }
  public openchangeCustomerModal() {
    const modelDiv = document.getElementById('changeCustomerModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public updateContact() {
    const updateContactPayload = {
      "claimAddressId": this.filteredClaimAddresses?.claimAddressId  || this.claimCustomerDetails.claimAddresses[0].claimAddressId,
      "addressTypeId": this.filteredClaimAddresses?.addressTypeId || 6 ,
      "firstName": this.claimContactForm.get('firstName')?.value,
      "lastName": this.claimContactForm.get('lastName')?.value,
      "phoneNumber": this.claimContactForm.get('phone')?.value,
      "emailAddress": this.claimContactForm.get('email')?.value
    };

    this.claimService.updateClaimAddress(updateContactPayload).subscribe(
      (data) => {

        this.refreshClaimDetails();
        // this.snackBar.open('Claim Contact  Updated  Succesfully', 'Close', {
        //   duration: 3000,
        //   verticalPosition: 'top',
        //   panelClass: ['custom-snackbar'], 
        // });
        const toastType: 'success' | 'error' | 'warning' = 'success'; 
            const message = 'Claim Contact  Updated  Succesfully';
            this.toasterservice.showToast('success', 'Claim Contact  Updated  Succesfully');
        this.closeClaimContactModal();

      },
      (error) => {
        // this.snackBar.open('Failed To Update Claim Contact', 'Close', {
        //   duration: 3000,
        //   verticalPosition: 'top',
        //   panelClass: ['custom-snackbar'],
        // });
        const toastType: 'success' | 'error' | 'warning' = 'error'; 
            const message = 'Failed To Update Claim Contact';
            this.toasterservice.showToast(toastType, message);
        this.closeClaimContactModal();
      }
    )
  }

  public openClaimContactModal() {
    if (!this.claimsIsVoid) {
      const modelDiv = document.getElementById('modalClaimContact');
      if (modelDiv != null) {
        let phone = this.filteredClaimAddresses?.phoneNumber != undefined ? this.removeSpecialCharsFromPhoneNumber(this.filteredClaimAddresses?.phoneNumber) : ''
        this.claimContactForm.patchValue({
          firstName: this.filteredClaimAddresses?.firstName || '',
          lastName: this.filteredClaimAddresses?.lastName || '',
          email: this.filteredClaimAddresses?.emailAddress || '',
          phone:phone 
          
        });
        modelDiv.style.display = 'block';
      }
    }
  }

  public removeSpecialCharsFromPhoneNumber(phoneNumber: string): string {
    // Regular expression to remove (), -, and spaces
    return phoneNumber.replace(/[()-\s]/g, '');
  }
  
  public closeClaimContactModal() {
    const modelDiv = document.getElementById('modalClaimContact');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  public initializeForm() {
    this.claimContactForm = this.formBuilder.group({
      firstName: [
        '',
        [
          Validators.required,
          this.firstCharacterValidator(),
        ],
      ],
      lastName: [
        '',
        [
          Validators.required,
          this.firstCharacterValidator(),
        ],
      ],
      email: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]{2,7}$/),
        ],
      ],
      phone: [
        '',
        [
          Validators.required,
          Validators.pattern(/^[0-9]{3}[-]?[0-9]{3}[-]?[0-9]{4}$/),
          this.customValidator()
        ],
      ],

    });
  }

  public firstCharacterValidator() {
    return (control: any) => {
      const inputValue = control.value;
      if (inputValue && /^[0-9]/.test(inputValue[0])) {
        control.setValue('');
        return { firstCharacterIsLetter: true };
      }

      return null;
    };
  }

  private customValidator() {
    return (control: any) => {
      const inputValue = control.value;
      const specialCharPattern = /[A-Za-z!@#$%^&*()_+{}\[\]:;<>,.?~\\//"'=-]/;

      if (inputValue && specialCharPattern.test(inputValue)) {
        control.setValue(tempVal);
        return { containsSpecialCharacter: true };
      }
      tempVal = inputValue;
      return null;
    };
  }

  public claimVoid(data: any) {
    if (data == 'Void' || data == 'Close' || data == 'Decline' || data == 'Pause')  {
      this.claimsIsVoid = true;
    } else {
      this.claimsIsVoid = false;
    }

  }
  public claimratedetailModal() {
    // if (!this.claimsIsVoid) {
      const modelDiv = document.getElementById('claimratedetail');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
    // }
  }

  public claimratedetailCloseModal() {
    const modelDiv = document.getElementById('claimratedetail');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public openAuditHistory(){

this.claimService.fetchAuditHistoryByClaimId(this.claimId).subscribe(data => {
  if (data.status === 200) {
    console.log(data, 'audithitry');
    this.auditHistroyData = data.body;

    const userMappingObservables = this.auditHistroyData.map(entry =>
      this.userMappingService.findByUserId(entry.createdByUserId)
    );

    forkJoin(userMappingObservables).subscribe(userDetails => {
      userDetails.forEach((userDetail, index) => {
        this.auditHistroyData[index].userName = userDetail.firstName + ' ' + userDetail.lastName;
      });
      console.log(this.auditHistroyData,'audithitryaftwr');
    });
  }
});

   
        const modelDiv = document.getElementById('audithistory');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
        }
  
  }
  public closeModal(id:string){
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    if(id == 'priorityclaim'){
      this.jobStop = this.claimDetails.jobStopped ? this.claimDetails.jobStopped : false;
      this.priorityclaimtoggle = this.jobStop ? true : this.claimDetails.priorityClaim;
         
    }
    if(id == 'reasoncodeModal' || id == 'reasoncode2Modal' || id == 'overRideModal'){
      this.drpDwRreasonCode = this.claimDetails?.claimReasonDefinition?.claimReasonCode
    }
  }

  public openInspections(){
  /*   const modelDiv = document.getElementById('inspections');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    } */
      this.router.navigate(['/inspection-screen']);
  }
 

  public openTotalItemizedCost(){
    // const url = this.router.createUrlTree(['/total-itemized-cost/'+ this.claimId ]);
    // window.open(url.toString(), '_blank');
    const navigationExtras: NavigationExtras = {
      state: {
        claimId: this.claimId,
        claimNumber:this.claimNumber,
       
      }
    };
  
      
      this.router.navigate(['/total-itemized-cost'], navigationExtras);
  }
 

 

  public openPriorityNoteModal(){
    if (!this.claimsIsVoid) {
     if(!this.jobStop ){
      if(!this.priorityclaimtoggle){
        const modelDiv = document.getElementById('priorityclaim');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
       
      }
      }else{
        // if(!this.priorityclaimtoggle ){
          this.priorityclaimtoggle = false;
          this.claimService.priorityClaimToggle(this.claimId, this.priorityclaimtoggle ,'',Number(localStorage.getItem('userId'))).subscribe((Result: any) =>
             {

            // this.closeModal('priorityclaim')
            const modelDiv = document.getElementById('priorityclaim');
            if (modelDiv != null) {
              modelDiv.style.display = 'none';
            }

            // this.snackBar.open('Priority flag has been set off.', 'Close', {
            //   duration: 3000,
            //   verticalPosition: 'top',
            //   panelClass: ['custom-snackbar'],
            // });
            const toastType: 'success' | 'error' | 'warning' = 'success'; 
            const message = 'Priority flag has been set off.';
            this.toasterservice.showToast(toastType, message);
        })
      // }
      }
     
    
    this.fetchNoteTemplate();
     }
   
  }
}

public openreasonCodeModal(){
  const modelDiv = document.getElementById('reasoncodeModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}

public validateSelection(areaOfImpact:any){
const validateReasonPayload= 
    {
      "claimId":this.claimId,
      "lineId": 0,
      "reasonCode":'',
      "ruleOfImpactArea":areaOfImpact,
      "addFreightLine":false,
      "customerNumber": "",
      "updateReasonCodeLineItem": false
       }
      this.claimService.validateSmartCodeRules(validateReasonPayload).subscribe(
        (response) => {
          // console.log(response, 'validateReasonData');
          if (response.status === 200) {
           if(response.body.errorMessage){ 
            const message = response.body.errorMessage;
            this.toasterservice.showToast('error', message);
          } else  if(response.body.length = 0){ 
            this.toasterservice.showToast('success', 'message');
          }

          }
          
        });

}

 public changeReasoncode(event:any){
  this.openreasonCodeModal();
}
public confirmClaimReasonCode(){
  if(this.claimDetails.claimStatusId == 1){
    
    const validateReasonPayload= 
    {
      "reasonCode": this.drpDwRreasonCode,
      "claimId": this.claimId,
      "lineDetails": [
        // {
        //   "lineId": 0,
        //   "reasonCode": "string"
        // }
      ],
      "ruleOfImpactArea": 'Add/Edit Claim Reason Code',
      "addFreightLine": false,
      "customerNumber": this.claimDetails.customerNumber,
      "updateReasonCodeLineItem": false
    }
      this.claimService.validateSmartCodeRules(validateReasonPayload).subscribe(
        (response) => {
          // console.log(response, 'validateReasonData');
          if (response.status === 200) {
           if(response.body[0].updatedReasonCode){ 
            this.claimDetails.claimReasonDefinition.claimReasonCode = this.drpDwRreasonCode
            this.toasterservice.showToast('success', 'Reason Code Updated Sucessfully');
            // this.closeModal('reasoncodeModal')
            this.closeReasonCodeModal('reasoncodeModal')
          } 
            else if(response.body[0].override){ 
              const model = document.getElementById('reasoncodeModal');
              if (model != null) {
                model.style.display = 'none';
              }
            this.overRideErrMsg = response.body[0].errorMessage
            const modelDiv = document.getElementById('reasoncode2Modal');
            if (modelDiv != null) {
              modelDiv.style.display = 'block';
            }
         }else{
          this.closeModal('reasoncodeModal')
          const message = response.body[0].errorMessage;
          this.toasterservice.showToast('error', message);
         }

          }
          
        });
   
  }else{
    this.closeModal('reasoncodeModal')
    this.toasterservice.showToast('error','Reason code can not be modified as Claims status is not open');
  }
}

public updateClaimReason(){
  const paylaod = {
    "claimId":this.claimId,
      "reasonCode": this.drpDwRreasonCode ,
      "sellingcompanyCode": '' ,
      "modifiedByUserId": Number(localStorage.getItem('userId'))
   }
   this.claimService.updateSellingCompanyAndReasoCode(paylaod).subscribe(data=>{
    const model = document.getElementById('reasoncode2Modal');
    if (model != null) {
      model.style.display = 'none';
    }
    this.toasterservice.showToast('success', 'Reason Code Updated Sucessfully');
    const modelDiv = document.getElementById('overRideModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
   })
 
}

public updateReasonCode(){
  const validateReasonPayload= 
    {
      "reasonCode": this.drpDwRreasonCode,
      "claimId": this.claimId,
      "lineDetails": [ ],
      "ruleOfImpactArea": 'Add/Edit Claim Reason Code',
      "addFreightLine": false,
      "customerNumber": this.claimDetails.customerNumber,
      "updateReasonCodeLineItem": true
    }
      this.claimService.validateSmartCodeRules(validateReasonPayload).subscribe(
        (response) => {
          // this.closeModal('overRideModal');
         this.closeReasonCodeModal('overRideModal')
          this.toasterservice.showToast('success', 'Reason Code updated Successfully in Line Levels');
          
        },
      error=>{
        this.toasterservice.showToast('error', 'Reason Code Failed');
        this.closeModal('overRideModal')
      });
}

public closeReasonCodeModal(action:string){
  const modelDiv = document.getElementById(action);
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}

public customerDetailsData(event:any){
 this.customerdata = event
 this.contactHeader = true;
}

public contactHeaderData(){
  if(this.contactHeader){
    return true;
  }else{
    if(this.filteredClaimAddresses != null ){
      return true;
    }else{
      return false;
    }
  }
}

public updatingCustomerDetailsData(event:any){
  this.claimService.getClaim(this.claimNumber,Number(localStorage.getItem('userId'))).subscribe(
    (data: any) => {
      data.claimAddresses.forEach((address: any) => {
        if (address.addressTypeId === 6) {
          this.filteredClaimAddresses = address;
         
        }
      });
      if(this.filteredClaimAddresses != null){
        this.contactInfo = false;
      }else{
        this.contactInfo = true;
      }
    })

}
openReceipts(){
  this.router.navigate(['/receipt-rga']);
}

public fetchClaimNotesData(){
  this.fetchNote = 'data'
}

public mergedClaimNumbers: any[] = [];
public selectedDropDownMerge: number = 0;
public mergedClaimNumbersArray(event:any){
//  console.log(event,'...........check event')
this.mergedClaimNumbers = event
}

public navigateToClaimDetail() {
  if (this.selectedDropDownMerge) {
    // Navigate to the claim detail route with the selected claim ID
    this.router.navigate(['/claim-detail', this.selectedDropDownMerge]);
  }
}

getClaimDetailUrl(): string {
  return this.router.createUrlTree(['/claim-detail', this.selectedDropDownMerge]).toString();
}

public  getMergeClaimdata(){
  this.claimService.mergedClaims(this.claimDetails.claimId)
  .subscribe(data => {
    this.mergedClaimNumbers = data;
     
  });
}


public getPrimaryPhoneNumber(info:any,dialCode:any) {
  const formatPhoneNumber = (phoneNumber: string, dialCodeId?: number) => {
    // Remove all non-digit characters from the phone number
    const cleanPhoneNumber = phoneNumber.replace(/\D/g, '');

    // Check if dialCodeId is 1 (for US/Canada)
    if (dialCodeId === 1) {
      // Format number with +1
      return `+1 (${cleanPhoneNumber.slice(0, 3)}) ${cleanPhoneNumber.slice(3, 6)}-${cleanPhoneNumber.slice(6)}`;
    }else{
      return '+'  + dialCodeId + ' ' + phoneNumber
    }

    // Return the number without formatting for other dial codes
    return cleanPhoneNumber;
  };

  // Check priority: cellPhoneNumber -> businessPhoneNumber -> homePhoneNumber
 
    return formatPhoneNumber(info, dialCode);

}



 public updateReasonCodeAndSellingCom(data:any,code:any){
 const paylaod = {
  "claimId":this.claimId,
    "reasonCode": '' ,
    "sellingcompanyCode": '' ,
    "modifiedByUserId": Number(localStorage.getItem('userId'))
 }
 let areaOfImpact = ''
 let message = ''
 if(code == 'reasonCode'){
  paylaod.reasonCode = this.drpDwRreasonCode
  message = ' Reason Code '
  areaOfImpact = 'Add/Edit Claim Reason Code'
 } else{
  paylaod.sellingcompanyCode = this.companyValue
   message = ' Selling Company '
    areaOfImpact = 'Add/Edit Claim Reason Code'
 }
 
 this.claimService.updateSellingCompanyAndReasoCode(paylaod).subscribe(data=>{
  // this.validateSelection(reasoncode,areaOfImpact)
  this.closeModal('reasoncode2Modal')
  this.toasterservice.showToast('success', message + 'Updated Sucessfully');
 })
}

public trimAndUpdate(controlName: string) {
  const control = this.claimContactForm.get(controlName);
  if (control && typeof control.value === 'string') {
    let trimmedValue = control.value.trim();
    if (trimmedValue.length > 64) {
      // Trim the value to a maximum of 64 characters
      trimmedValue = trimmedValue.substring(0, 64);
    }

    // Update the control value without emitting an event
    control.setValue(trimmedValue, { emitEvent: false });
  }
}



}
