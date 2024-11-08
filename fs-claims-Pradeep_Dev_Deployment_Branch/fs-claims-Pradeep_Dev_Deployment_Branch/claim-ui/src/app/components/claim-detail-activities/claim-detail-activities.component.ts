import { Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChange, SimpleChanges, TemplateRef, ViewChild } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BsModalService, BsModalRef } from 'ngx-bootstrap/modal'
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ClaimDetailComponent } from '../claim-detail/claim-detail.component';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { NavigationExtras, Router } from '@angular/router';
import { BlockableUI } from 'primeng/api';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { DatePipe } from '@angular/common';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-claim-detail-activities',
  templateUrl:'./claim-detail-activities.component.html',
  styleUrls: ['./claim-detail-activities.component.scss']
})
export class ClaimDetailActivitiesComponent  implements OnInit {
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  @Input() claimDetails: any;
  @ViewChild('reassignModal') reassignModal!: NgbModalRef;
  @Output() customEvent = new EventEmitter<string>();
  @Output() voidEvent = new EventEmitter<string>();
  @Output() fetchClaimNotesData = new EventEmitter<string>();
  @Output() mergedClaimNumbersArray = new EventEmitter<string[]>();
  @ViewChild('inputMerge') inputMerge!: ElementRef;
  public isSideNavCollapsed: boolean = true;
  public customClass = 'customCard';
  public isMenuOpened: boolean=false;
  public isDataLoaded: boolean = true;
  public declineList: any = [];
  public declineReasonId: string = '';
  public userList: any = [];
  public selectedUserName: string = '';
  public managerList: any = [];
  public filteredManagers: any = [];
  public selectedManagerName: any;
  public mergedClaimNumbers: any[] = [];
  public unMergedClaimNumbers: any[] = [];
  public parentClaimId = 108; 
  public pastDateError: boolean = false;
  public claimNumber: any = '';
  public mergeEnabled: boolean = true;
  public mergeBtn:boolean = true;
  public selectedMerge: any = '';
  public unSelectedMerge: string = '';
  public iscButton: boolean = true;
  public pauseButton: boolean = false;
  public isButtonDisabled: boolean = true;
  public selectedDropDownMerge: number = 0;
  public claimIsVoid:boolean = false;
  public showCloseVoidDeclineButtons: boolean = false;
  public showOtherButtons: boolean = true;
  public showDeclineBtn: boolean = true;
  public showUnMergeBtn: boolean = false;
  public showIscBtn: boolean = false;
  public  userId!: number;
  public noteTemplateText:any = '';
  public noteText:string = '';
  public noteTextDisable : boolean = true;
  public sampleData: any;
  public textAreaData: string ='';
  public sampleRequestForm = this.form.group({
    sampleRequirement: [''],
    sendEmail:[true],
    fedexLevel : [{value:false, disabled:true}]

  })
  public fedExLabel : boolean  = true;
  public prioritynoteTemplateText:string = '';
  public showNote: boolean = false;
  public claimNumberControl = new FormControl();
  public filteredMergeOptions:any;
  cdr: any;
  public searchMergeNum:any
  public inputsearchMergeNum : any
  public traceDate: Date | any;
  public notePauseText: string = '';
  public updateAlldata: any;
  public loading = true; 
  
  public  toggleMenu(): void{
    this.isMenuOpened = !this.isMenuOpened;
  }
  public clickOutside():void{
    this.isMenuOpened=false;
  }
  public modalRef?: BsModalRef;
  @ViewChild('closebutton') closebuttonRef : ElementRef | any;
  
  public config = {
    backdrop: false,
    ignoreBackdropClick: false
  };
  public selectedMergeReasonCode : boolean = false;
  public selectedUserGroup:any
  public selectedUsers:any
  public routeUsersGroupsList: any[] = []
  public usersDataList:any[] = []
  public userName: any;
  public nodeTextField: string = ''
  public evaluateSample: boolean = true;
  public sampleReceivedBtn: boolean = false;
  public samplereviewdBtn: boolean = false;
  public declineReasonNote:any = '';
  public bsConfig: Partial<BsDatepickerConfig>;
  public dispoModalBodymsg:string = '';
  public dispoHeaderMsg:string = '';
  public ClaimDocData: any[] = [];
  public lineItemsDetails: any = [];
  public sellingCompanyData: any = [];

 constructor(
  private modalService: BsModalService, 
  private bodyClassService: BodyClassService,
  private claimService: ClaimDetailService,
  private userMappingService: UserMappingService,
  private snackBar: MatSnackBar,
  private router: Router,
  private notesTasksService:NotesTasksService ,
  private form: FormBuilder,
  private toasterService: ToasterService,
  private datepipe: DatePipe,
  private claimdetailservice:ClaimDetailService
  ) {
    this.bsConfig = {
      showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
      minDate: new Date(), // Set minimum date to today
      maxDate: undefined, // Allow future dates without limit
  };
  }

  ngOnInit(): void {
    this.userId = Number(localStorage.getItem('userId'));
    this.userName = localStorage.getItem('userName');

 this.claimService.claimDetails$.subscribe(claimDetails => {
  // console.log('Claim Details in Child Component:', claimDetails);
  this.claimService.mergedClaims(claimDetails.claimId)
  .subscribe(data => {
    this.mergedClaimNumbers = data;
    if(this.mergedClaimNumbers.length > 0){
      this.showUnMergeBtn = true;
    }
    // console.log(this.mergedClaimNumbers,'mergedClaimNumbers')
  });

});

  }

  ngOnChanges() {
    if (this.claimDetails != undefined && this.claimDetails.length != 0 ) {
      this.sampleBtns();
      let docData = this.claimDetails.claimDocuments
        .filter((doc: any) => doc.documentType.documentTypeCode === "INV" && doc.statusId !== 3) // Filter documents where documentTypeCode is "INV"
        .map((doc: any) => ({
          ...doc,
          claimLineDetails: doc.claimLineDetails.filter((line: any) => line.statusId !== 3) // Filter claimLineDetails where statusId is not 3
        }));
      this.loading = false;
      if (this.ClaimDocData.length == 0) {
        docData.forEach((item: any) => this.ClaimDocData.push(...item.claimLineDetails))
      }
      this.sellingCompany();

    }

  }
  

  public  openModal(template: TemplateRef<any>) {
    this.modalRef = this.modalService.show(template, this.config);
  }
  public  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  public  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  public  getDeclineClaim(){
    const modelDiv = document.getElementById('declainModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.getAllDeclinedReasons();

  }

  public  getPendClaim(){
    const modelDiv = document.getElementById('PendModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public  getRqstInfoClaim(){
    const modelDiv = document.getElementById('RequestInfoModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  

  // for Get list of declained list drop down
  public getAllDeclinedReasons(){
    this.claimService.getAllDeclinedReasons().subscribe(
      (data:any) => {
        this.declineList = data;
      })
      this.showNote = Number(this.declineReasonId) === 39;
  }

 
  public onDeclineReasonChange() {
    // Update the showNote flag based on the selected decline reason
    this.showNote = Number(this.declineReasonId) === 39; // Assuming 'Other' has declineReasonId of 39
 
  }


  // saving Declined Data
  public saveDecline(){
    if( Number(this.declineReasonId ) != 0){
      let updateUrl = 'claimId='+this.claimDetails.claimId+'&action=Decline&userId='+this.userId+'&declineReasonId='+Number(this.declineReasonId)+'&declineReasonText='+this.declineReasonNote
      this.claimService.updateAllClaimStatus(updateUrl).
      subscribe(
        (data) => {
          this.voidEvent.emit('Decline')
          this.customEvent.emit(data);
          // this.toasterService.showToast('success', ' Claim Declined Sucessfully');
          this.dispoModalBodymsg = 'Claim Declined Sucessfully';
          this.dispoHeaderMsg = 'Detail Line For Disposition'
          this.closeModal('declainModal')
          this.openDispostionModal();
        })
     
    } else {
      return;
    }
    
  }

  // getting reassign Users List
  public getReassignUserList(){
     const modelDiv = document.getElementById('reassignModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
    // if (this.selectedUserName != '' && this.selectedUserName != undefined) {
      this.userMappingService.getAllUserGroups('assign').subscribe((data) => {
        // console.log("Group List =>"+JSON.stringify(data));
        this.userList = data;
      });
    // }
    
  }

  // Getting Mangers List after selecting user
  // public getManagersList(){
  //   this.isDataLoaded = false;
  //   if (this.selectedUserName != '' && this.selectedUserName != undefined) {
  //   this.userMappingService
  //       .getManagerById(parseInt(this.selectedUserName))
  //       .subscribe((data) => {
  //         this.isDataLoaded = true;
  //         this.managerList = data;
  //         this.filteredManagers = this.managerList;
  //       });
  //   }
  // }

  public getManagersList(): void {
    this.isDataLoaded = false;
    if (this.selectedUserName != '' && this.selectedUserName != undefined) {
      this.userMappingService.getManagerById(parseInt(this.selectedUserName))
        .subscribe((data) => {
          this.isDataLoaded = true;
          this.managerList = data;
          this.filteredManagers = this.managerList;
        });
    }
  }

  public filterManagers(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const value = inputElement.value;
    
    if (!value) {
      this.filteredManagers = this.managerList;
      return;
    }

    const filterValue = value.toLowerCase();
    this.filteredManagers = this.managerList.filter((manager:any) =>
      (manager.firstName + ' ' + manager.middleInitial + ' ' + manager.lastName).toLowerCase().includes(filterValue)
    );
  }

  public displayManagerName(manager: any): string {
    return manager ? `${manager.firstName} ${manager.middleInitial} ${manager.lastName}` : '';
  }
  public updateAllActions(action: string,hideEmit:string = ''){
    if(action == 'Void'|| action == 'Close'){
      this.voidEvent.emit(action)
    }else{
      this.voidEvent.emit(action)
    }
    let updateUrl = 'claimId='+this.claimDetails.claimId+'&action='+action+'&userId='+this.userId
    if(action == 'AssignedToPricing' || action == 'RemovedFromPricing' ){
      let territoryManagerName = this.claimDetails.territoryManagerName != ''? this.claimDetails.territoryManagerName:'first name'
      updateUrl = updateUrl+'&territoryManagerName='+territoryManagerName
    }
    this.claimService.updateAllClaimStatus(updateUrl).
    subscribe(
      (data) => {
        this.updateAlldata =data
        if(hideEmit != 'hideEmit'){
          this.customEvent.emit(data);
        }
        if(action== 'Close'){
          this.closeModal('closeModalnewTest')
        }
        if(action== 'Void'){
          this.closeModal('voidModalId')
        }
        if(action != 'Pause'){
          this.toasterService.showToast('success', 'Claim '+ action +' succesfully');
        }
        if(action == 'RemovedFromPricing' || action == 'AssignedToPricing'){
          this.removeIsc(action);
        }
        // else if(action == 'unPause' || action == 'Pause'){
        //   // this.pauseBtn(action);
          
        //    }
           if(action == 'Pause'){
            this.showCloseVoidDeclineButtons = true;
            this.showOtherButtons = false;
            this.mergeBtn = false;
            this.iscButton = false;
            this.evaluateSample = false;
            this.openPauseModal()
           }else if(action == 'Reopen'){
            this.showOtherButtons = true;
            this.mergeBtn = true;
            this.iscButton = true;
            this.evaluateSample = false;
           }

      }
    )
    this.closeModal('closeModalnewTest');
    this.closeModal('voidModalId');
  
  }

  public pauseAction(action: string){
    if(this.claimDetails.workStatusId == 8 || this.claimDetails.workStatusId ==14 || this.claimDetails.workStatusId ==37 || this.claimDetails.workStatusId == 43){
       this.toasterService.showToast('error', 'CANNOT PAUSE CLAIM; IT HAS BEEN ROUTED FOR REVIEW.');
    }
    else{
      this.updateAllActions(action);
    }
  }
  public isReopenButtonVisible(): boolean {
    return this.showOtherButtons ||this.claimDetails.claimStatusId != 1 ;
  }
  
  public isReopenButtonEnabled(): boolean {
    return this.claimDetails.claimStatusId == 2 || this.claimDetails.claimStatusId == 5 || this.claimDetails.claimStatusId == 4;
  }
  
  public closeVisible(): boolean{
    return this.claimDetails.claimStatusId == 1 || this.claimDetails.claimStatusId == 3 || this.showCloseVoidDeclineButtons;
  }
  public voidAction():boolean{
    return this.claimDetails.claimStatusId == 2 || this.claimDetails.claimStatusId == 1 || this.showCloseVoidDeclineButtons;
  }


  public reAssign() {
    this.claimService.reAssign(this.claimDetails.claimId,this.userId,this.selectedManagerName.userId).
    subscribe(
      (data:any) => {
        this.customEvent.emit(data);
         this.toasterService.showToast('success', 'Claim Reassigned succesfully.');
        this.selectedUserName = '';
        this.selectedManagerName = '';
      },error=>{
       this.toasterService.showToast('error', 'Claim Reassigned Failed');
      }
      )
      this.closeModal('reassignModal')
  }

  
  public removeIsc(action: string){
    if(action == 'RemovedFromPricing'){
      this.iscButton = true;
      this.showIscBtn = false;
    } else {
      this.iscButton = false;
      this.showIscBtn = true;
    }
  }
 
  public mergeAction(action: string){
    if(action == 'marge'){
      this.mergeBtn = false;
      this.showUnMergeBtn = true;
    } else {
      this.mergeBtn = true;
      this.showUnMergeBtn = false;
    }
  }

  public pauseBtn(action: string){
    if(action == 'unPause'){
      this.pauseButton = false;
    } else {
      this.pauseButton = true;
    }
  }
public checkclaimStatusId:number = 0;
  public unPauseBtnVisible(){
    if( this.claimDetails.claimStatusId === 3){
      return true;
    }else{
      return this.pauseButton;
    }

  }
 

  public mergeClaim(){
    let claimName = this.unMergedClaimNumbers.filter((ele:any)=>ele.claimId == this.selectedMerge)
    const mergePayLoad ={
      "parentClaimId": this.claimDetails.claimId,
      "mergedClaimId": claimName[0].claimId,
      "loggedInUserId": this.userId,
      "comment": this.noteTemplateText,
     
    }
    if(this.claimDetails.claimReasonDefinition.claimReasonCode !== ''){
      this.claimService.merged(mergePayLoad).subscribe(
        data => {
          this.getMergeClaimdata();
          this.mergeAction('marge');
          this.closeModal('mergeModal')
          this.inputMerge.nativeElement.value = '';
          const toastType: 'success' | 'error' | 'warning' = 'success'; 

const message = `Claim Number ${claimName[0].claimNumber} is Merged Successfully with ${this.claimDetails.claimNumber}`;
this.toasterService.showToast(toastType, message);
          this.noteTemplateText = ''
          this.selectedMerge = null
          this.filteredMergeOptions = this.unMergedClaimNumbers; // Reset filtered options
          this.cdr.detectChanges();  // Trigger change detection
          this.fetchClaimNotesData.emit()
        
          
        },
        error=>{
          // this.mergeAction('marge');
        }
      )
      

    }
    else{
      this.closeModal('mergeModal')
      // this.snackBar.open('Reason code is not there', 'Close', {
      //   duration: 3000,
      //   verticalPosition: 'top',
      //   panelClass: ['custom-snackbar'], 
      // });
      this.toasterService.showToast('error', 'Reason code is not there');
    }
   
    
  }

  public  UnMergeClaim(){
    const unMergePayload = {
        "parentClaimId": this.claimDetails.claimId,
        "unMergedClaimId": Number(this.unSelectedMerge),
        "loggedInUserId": this.userId,
        "comment": this.noteTemplateText,
      
    }
    this.claimService.unMerged(unMergePayload).subscribe(
      data => {
        this.mergeAction('unmarge');
        let claimName = this.mergedClaimNumbers.filter((ele:any)=>ele.claimId == this.unSelectedMerge)
        this.closeModal('UnmergeModal')
        this.snackBar.open('Claim Number ' +claimName[0].claimNumber  + ' is Unmerged Sucessfully  with ' + this.claimDetails.claimNumber, 'Close', {
          duration: 3000,
          verticalPosition: 'top',
          panelClass: ['custom-snackbar'],
        });
        this.noteTemplateText = ''
        this.unSelectedMerge = ''
        this.fetchClaimNotesData.emit()
      },
      error=>{
        this.closeModal('UnmergeModal')
        // this.snackBar.open('There is no merge claim details found in that parent and merged ', 'Close', {
        //   duration: 3000,
        //   verticalPosition: 'top',
        //   panelClass: ['custom-snackbar'],
        // });
        this.toasterService.showToast('error', 'There is no merge claim details found in that parent and merged');
      }
    )
   
    
  }

  public openUnMergeModl(){
    const modelDiv = document.getElementById('UnmergeModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  this.getMergeClaimdata();
  this.fetchNoteTemplate('UNMERGECLM');
  }


  public  getMergeClaimdata(){
    this.claimService.mergedClaims(this.claimDetails.claimId)
    .subscribe(data => {
      this.mergedClaimNumbers = data;
         if(this.mergedClaimNumbers.length > 0){
      this.showUnMergeBtn = true;
      this.mergedClaimNumbersArray.emit(this.mergedClaimNumbers)
    }
    });
}

filterMergeOptions(event: any) {
  const searchValue = event.target.value.toLowerCase();
  this.searchMergeNum = searchValue
  this.inputsearchMergeNum = searchValue
  this.filteredMergeOptions = this.unMergedClaimNumbers.filter(option =>
    option.claimNumber.toLowerCase().includes(searchValue)
  );
  if(this.searchMergeNum == ''){
    this.selectedMergeReasonCode = false
  }
}

selectMergeOption(option: any) {
  this.claimNumberControl.setValue(option.claimNumber);
  this.selectedMerge = option.claimId; // Store the selected claimId
  this.searchMergeNum = option.claimId
  this.inputsearchMergeNum = ''
  this.checkClaimData(option.claimId); // Pass the claimId to the checkClaimData method
}

checkClaimData(selectedMergeId: string) {
  const claimRecord = this.unMergedClaimNumbers.find(data => data.claimId === selectedMergeId);
  if (claimRecord?.claimReasonCode === 'ALL') {
    this.selectedMergeReasonCode = true;
  } else {
    this.selectedMergeReasonCode = false;
  }
}


  public getUnmergeClaimdata(){
    const modelDiv = document.getElementById('mergeModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    
    this.claimService.unMergedClaims(this.claimDetails.claimId, this.claimDetails.customerNumber)
    .subscribe(data => {
      this.unMergedClaimNumbers = data;
      this.filteredMergeOptions = this.unMergedClaimNumbers;
    });
    this.fetchNoteTemplate('MERGECLAIM');
  }

  public fetchNoteTemplate(id:string){
    this.notesTasksService.fetchNoteTemplateBynoteTypeCode(id).subscribe((data:any)=>{
    this.nodeTextField = data.body
    this.noteTemplateText = data.body.noteTemplateText
    this.noteTextDisable = !data.body.editable
    })
  }

  public reassignToggle(){
    if(this.selectedUserName && this.selectedManagerName){
      return false;
   }
   else{
    return true;
  }
}

public declineBtn() {
  // Check if the decline reason is "Other" (id 39) and if the note is entered
  if (Number(this.declineReasonId) === 39) {
    return !this.declineReasonNote || this.declineReasonNote.trim() === '';
  }
  // For all other reasons, just check if a valid decline reason is selected
  return !Number(this.declineReasonId);
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

// Function to check if the Close, Void, and Decline buttons should be visible
public closeVoidDeclineButtonsVisible(): boolean {
  return this.showCloseVoidDeclineButtons;
}

// Function to check if the other buttons should be visible
public otherButtonsVisible(): boolean {
  return this.showOtherButtons;
}

public inputValidator(event: any): void {
  const pattern = /[^A-Za-z0-9_@.,/#&+-\s]/g; // Updated pattern to exclude spaces

  const currentValue = event.target.value;
  if (pattern.test(currentValue)) {
    event.target.value = currentValue.replace(pattern, "");
  }
}


public requestSampleModal() {
  if (this.claimDetails.claimReasonDefinition.claimReasonCode === 'ALL') {
    this.toasterService.showToast('error', 'Claim does not have Reason code. Please update the Reason code to request for sample.');
  } else {
    // Filter the claimContact with addressTypeId = 6
    let claimContact = this.claimDetails.claimAddresses.filter((data: any) => data.addressTypeId === 6);
    // Check if claimContact exists and has a valid emailAddress
    let claimContactEmail = claimContact.length > 0 && claimContact[0].emailAddress && claimContact[0].emailAddress.trim() !== '';
    // Check if endUserEmail exists and is not empty
    let endUserEmail = this.claimDetails.endUserInformation?.emailAddress && this.claimDetails.endUserInformation.emailAddress.trim() !== '';

    // Check if at least one valid email exists
    if (claimContactEmail || endUserEmail) {
      const modelDiv = document.getElementById('sampleRequest');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      // Proceed with sample requirement logic modal open
      this.sampleRequirement();
    } else {
      // If neither email is valid, show error message
      this.toasterService.showToast('error', 'The Email ID is not added. Please add the email address.');
    }
  }
}




public openRoutingModal(){
  const modelDiv = document.getElementById('routingModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
  this.userMappingService.getAllUserGroups('route').subscribe((data:any)=>{
    this.routeUsersGroupsList =data
  })
}
public getUsersData(){
  this.userMappingService.getManagerById(this.selectedUserGroup).subscribe(data=>{
    this.usersDataList = data
  })
}

public closeModal(id:string){
  const modelDiv = document.getElementById(id);
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
  if(id == 'reassignModal'){
    this.selectedUserName = ''
    this.selectedManagerName = ''
  }
  if(id == 'declainModal'){
    this.declineReasonId = '';
  }
  if(id == 'mergeModal'){
    this.selectedMerge = ''
    this.noteTemplateText = ''
    this.selectedMergeReasonCode = false
    this.inputMerge.nativeElement.value = '';
  }
  if(id == 'UnmergeModal'){
    this.unSelectedMerge = ''
    this.noteTemplateText = ''
  }
  if(id == 'routingModal'){
    this.selectedUserGroup = ''
    this.selectedUsers = ''
    this.noteText = ''
  }
  this.sampleRequestForm.reset();
  if(id == 'pause-ModalId'){
    this.traceDate = ''
    this.notePauseText = ''
  }
}

public canMerge(): boolean {
  // Check if both dropdown and note text fields are filled
  return this.selectedMerge !== undefined &&  this.selectedMerge != '' &&
   this.noteTemplateText !== undefined && this.noteTemplateText.trim() !== '' &&
   this.searchMergeNum !== undefined &&  this.searchMergeNum != '' ;
}

public canUnMerge(): boolean {
  // Check if both dropdown and note text fields are filled
  return this.unSelectedMerge !== undefined  &&  this.unSelectedMerge != ''&&
   this.noteTemplateText !== undefined && this.noteTemplateText.trim() !== '';
}



public routeNotes(){
  let noteData = this.usersDataList.filter((data:any)=>data.userId == this.selectedUsers)
  let groupCode = this.routeUsersGroupsList.filter((ele:any)=> ele.userGroupId == this.selectedUserGroup )
  const routePayload = {
  "claimId": this.claimDetails.claimId,
  "routedUserName": noteData[0].firstName + '' + noteData[0].middleInitial + '' + noteData[0].lastName,
  "loginUserName":  this.userName,
  "userGroupCode":groupCode[0].userGroupCode,
  "userId": this.userId,
  "note": this.noteText,
  "routedUserId": noteData[0].userId,
  "toRecipients": [
    noteData[0].emailAddress
],
  "ccRecipients":[]
  }


  this.claimService.routed(routePayload).subscribe((data:any)=>{
        // this.snackBar.open(' Claim Routed Successfully', 'Close', {
        //   duration: 3000,
        //   verticalPosition: 'top',
        //   panelClass: ['custom-snackbar'], 
        // });
        this.toasterService.showToast('success', ' Claim Routed Successfully');
    
        this.fetchClaimNotesData.emit()
        this.customEvent.emit(data);
        this.closeModal('routingModal')
        this.customEvent.emit(data);
        this.selectedUserGroup = ''
      this.selectedUsers = ''
      this.noteText = ''
  },error=>{
    // this.snackBar.open(' Claim Routed Failed', 'Close', {
    //   duration: 3000,
    //   verticalPosition: 'top',
    //   panelClass: ['custom-snackbar'], 
    // });
    this.toasterService.showToast('error', ' Claim Routed Failed');
  })
  
}

public routeBtn(){
  if(this.selectedUserGroup != '' && this.selectedUsers != '' && this.noteText != ''){
    return false;
  }else{
    return true;
  }
}

public sampleRequirement(){
  this.claimService.fetchSampleSizeRequirementAndTesting(this.claimDetails.claimReasonDefinition.claimReasonCode).subscribe(data=>{
    this.sampleData = data.body
    
      const sampleRequirement = this.sampleData.sampleSizeRequirement;

      // Update the form control value
      this.sampleRequestForm.patchValue({
        sampleRequirement: sampleRequirement
      });
  })
}
public sampeRequest(){
 // Extract the first email address where addressTypeId is 6
 let claimContactEmail = this.claimDetails.claimAddresses
 .filter((data: any) => data.addressTypeId == 6)
 .map((contact: any) => contact.emailAddress)[0] || '';
  const samplePayload = {
  "claimId": this.claimDetails.claimId,
 "dealerEmail": claimContactEmail,
 "endUserEmail": this.claimDetails?.endUserInformation?.emailAddress || '',
  "sampleRequirement": this.sampleRequestForm.value.sampleRequirement,
 "sendEmail": this.sampleRequestForm.value.sendEmail,
 "generateFedExLable": this.sampleRequestForm.value.fedexLevel,
  "createdByUserId": this.userId,
  "modifiedByUserId": this.userId
  }
  this.claimService.sampleRequest(samplePayload).subscribe((data:any)=>{
    // this.toasterService.showToast('success', 'Sample Requested Succesfully');
     this.dispoModalBodymsg = 'Sample Requested Succesfully';
      this.dispoHeaderMsg = 'Sample Request For Disposition'
    this.openDispostionModal()
    this.evaluateSample = false;
    this.sampleReceivedBtn = true;
    this.customEvent.emit(data);
  },error=>{
    this.toasterService.showToast('error', ' Sample Requested Failed');
  }
 )
  this.closeModal('sampleRequest')
  this.sampleRequestForm.reset();
}


public toggleBtn(){
  // if(this.sampleRequestForm.value.sendEmail == true){
  //   this.sampleRequestForm.get('fedexLevel')?.enable()
  // }else{
  //   this.sampleRequestForm.get('fedexLevel')?.disable()
  // }
}


closeopenModal(){
  const modelDiv = document.getElementById('closeModalnewTest');
 if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}

public openEvaluateModal(){
  const modelDiv = document.getElementById('evaluateSample');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}


openVoidModal(){
  const modelDiv = document.getElementById('voidModalId');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}
public openPauseModal(){
  this.traceDate = new Date(this.updateAlldata.traceDate)
  const modelDiv = document.getElementById('pause-ModalId');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}

public updateSampleReview(action:string){
  const payload = {
    "createdByUserId": this.userId,
    "modifiedByUserId": this.userId,
    "claimId": this.claimDetails.claimId,
    "action": action,
    "noteTemplateText":this.prioritynoteTemplateText || ''
  }
 this.claimService.updateSampleReviewByAction(payload).subscribe((data:any)=>{
  let message = action === 'Approved' ? 'Sample Approved Successfully' : 'Sample Declined Successfully';
  if(action == 'SampleReceived'){
    this.sampleReceivedBtn = false;
    this.samplereviewdBtn = true;
    message = 'Sample Reviewed Successfully'
  } else if(action == 'Approved' || action == 'Declined'){
    this.evaluateSample = true;
    this.sampleReceivedBtn = false;
    this.samplereviewdBtn = false;
  } 
   
  // this.snackBar.open(message, 'Close', {
  //   duration: 3000,
  //   verticalPosition: 'top',
  //   panelClass: ['custom-snackbar'], 
  // });
  this.toasterService.showToast('success', message);
  this.customEvent.emit(data);
 }, error=>{
  // this.snackBar.open('Error updating sample review', 'Close', {
  //   duration: 3000,
  //   verticalPosition: 'top',
  //   panelClass: ['custom-snackbar'], 
  // });
  this.toasterService.showToast('error', 'Error updating sample review');
 })
 this.closeModal('evaluateSample')
}

public sampleBtns(){
  if(this.claimDetails.workStatusId == 13){
    this.evaluateSample = false;
    this.sampleReceivedBtn = true;
    this.samplereviewdBtn = false;
  }
  else if(this.claimDetails.workStatusId == 27){
    this.evaluateSample = false;
    this.sampleReceivedBtn = false;
    this.samplereviewdBtn = true;
  }
  else if(this.claimDetails.workStatusId == 44){
    this.evaluateSample = true;
    this.sampleReceivedBtn = false;
    this.samplereviewdBtn = false;
  }
}

clearInputField() {
  this.inputMerge.nativeElement.value = '';
}

public checkDisableMergeBtn(){
  if( this.selectedMerge !== undefined &&  this.selectedMerge != '' &&
    this.searchMergeNum !== undefined &&  this.searchMergeNum != ''){
  
   if(this.inputsearchMergeNum == undefined ||this.inputsearchMergeNum == '' ){
    return false;
   }else{
    if(this.inputsearchMergeNum.length == 6 && this.filteredMergeOptions?.length > 0){
      const claimRecord = this.unMergedClaimNumbers.find(data => data.claimNumber === this.inputsearchMergeNum);
      if (claimRecord?.claimReasonCode === 'ALL') {
       return true;
      } else {
       return false;
      }
    } else{
      return true;
    }
   }
  }else{
    if(this.inputsearchMergeNum.length == 6 && this.filteredMergeOptions?.length > 0){
      const claimRecord = this.unMergedClaimNumbers.find(data => data.claimNumber === this.inputsearchMergeNum);
      if (claimRecord?.claimReasonCode === 'ALL') {
       return true;
      } else {
       return false;
      }
    } else{
      return true;
    }
  }
}

public onInput(event: Event) {
  console.log(event,'eventdata')
  const inputElement = event.target as HTMLInputElement;
  if(inputElement.value.length == 10){
    const inputDate = new Date(inputElement.value);
    const today = new Date();
    
    // Set today as the start of the current day (00:00:00)
    today.setHours(0, 0, 0, 0);
  
    // Check if the input date is before today (past date)
    if (inputDate < today) {
      // Clear the input value for past dates
      inputElement.value = '';
      this.pastDateError = true;  // Set the error for past dates
    } else {
      // Reset the error message and flag
      this.pastDateError = false;
    }
  }

 
}

  public pauseBtnAction() {
    let formattedDt = this.datepipe.transform(this.traceDate, 'yyyy-MM-dd')
    const payload = {
      "claimId": this.claimDetails.claimId,
      "userId": this.userId,
      "noteText": this.notePauseText,
      "traceDate": formattedDt
    }
    if (formattedDt) {
      payload.traceDate = formattedDt + 'T00:00:00';
    } 
    this.claimService.updatePauseClaimNote(payload).subscribe(data => {
      // this.checkclaimStatusId = data.body.
      this.customEvent.emit();
      this.voidEvent.emit('Pause')
      
      this.closeModal('pause-ModalId')
      this.pauseBtn('unPause');
      this.fetchClaimNotesData.emit()
      this.toasterService.showToast('success', ' Claim Paused Sucessfully');
    },
      error => {
        this.toasterService.showToast('error', ' Claim Paused Failed');
        this.closeModal('pause-ModalId')
      })
  }

  public closeModals(id:string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.router.navigate(['/claim-detail',this.claimDetails.claimNumber]);
  }
  public closelineItemModals(id:any){
    const modelDiv = document.getElementById('lineItem-Modal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.router.navigate(['/claim-detail',this.claimDetails.claimNumber]);
  }

  public openDispostionModal(){
    const modelDiv = document.getElementById('disposition-Modal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public openLineItemsModal(){
    const modelDiv = document.getElementById('lineItem-Modal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public setLineInvoiceData(claim:any,isChecked:boolean){
    if(isChecked && !this.lineItemsDetails.some((item:any)=>item.lineNumber == claim.lineNumber)){

  this.lineItemsDetails.push(claim)
    }else if(!isChecked){
      let index =  this.lineItemsDetails.findIndex((item:any)=>item.lineNumber == claim.lineNumber)
      if(index != -1){
        this.lineItemsDetails.splice(index,1)
      }
    }
    // console.log( this.lineItemsDetails,'.... this.lineItemsDetails')
  }

  public isChecked(claim:any){
    return this.lineItemsDetails.some((item:any)=>item.lineNumber ==
      claim.lineNumber)
       
  }
  public isAllSelected(){
    return this.lineItemsDetails.length == this.ClaimDocData.length
  }

  public toggleSelectAll(isChecked:any){
   this.ClaimDocData.forEach((ele:any)=>{
    this.setLineInvoiceData(ele,isChecked)
   })
  }
  public savePend(){
    this.closeModal('PendModal')
    this.openDispostionModal();
    this.dispoHeaderMsg = 'Pend For Disposition';
    this.dispoModalBodymsg = 'Pend Sucess'
  }

  public saveRequstInfo(){
    this.closeModal('RequestInfoModal')
    this.openDispostionModal();
    this.dispoHeaderMsg = 'Request Information For Disposition';
    this.dispoModalBodymsg = 'Request Information Sucess'
  }

  public navigateToDispoScreen() {
    let type = ''
    if (this.dispoHeaderMsg == 'Detail Line For Disposition') {
      type = 'DECLINE'
    } else if (this.dispoHeaderMsg == 'Sample Request For Disposition') {
      type = 'SAMPLE REQUESTED'
    } else if (this.dispoHeaderMsg == 'Pend For Disposition') {
      type = 'PEND'
    } else if (this.dispoHeaderMsg == 'Request Information For Disposition') {
      type = 'REQUEST INFORMATION'
    }

     const navigationExtras: NavigationExtras = {
      state: {
        selectedDocument: this.lineItemsDetails,
        claimAddresses: this.claimDetails.claimAddresses,
        claimId: this.claimDetails.claimId,
        claimNumber:this.claimDetails.claimNumber,
        customerNumber: this.claimDetails.customerNumber,
        claimDocuments: this.claimDetails.claimDocuments,
        sellingCompanyDetails:this.claimDetails.sellingCompany ,
        endUserInfo:this.claimDetails.endUserInformation == null ? '': this.claimDetails.endUserInformation,
           
        dispositionType:type
      }
    };
  
      
      this.router.navigate(['/prepare-disposition'], navigationExtras);
    
  }

  public sellingCompany(){
    this.claimdetailservice.getAllSellingCompanyData().subscribe(
      (data: any) => {
        this.sellingCompanyData = data.filter((ele:any)=>ele.sellingCompanyCode == this.claimDetails.sellingCompany)
       
        
      })
  }
public actionsObject:any = [
  {actionName:'OPEN',actionValue:'Reopen'},
  // {actionName:'CLOSE',actionValue:'Close'}
]
  public cancelPauseAction(){
    let value = this.actionsObject.filter((ele:any)=>ele.actionName.toLowerCase() == this.updateAlldata.oldClaimStatusName.toLowerCase() )
    this.updateAllActions(value[0].actionValue)
    this.closeModal('pause-ModalId')
    
  }

}
