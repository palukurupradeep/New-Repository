import { Component,Input, OnInit, ViewChild } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import {FormBuilder, Validators} from '@angular/forms';
import {MatStepper} from '@angular/material/stepper';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { ClaimCategory } from 'src/app/interfaces/claim-details.model';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { Sort } from '@angular/material/sort';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
// Define an interface for the sorting state
interface ColumnSortState {
  [key: string]: 'asc' | 'desc';
}

@Component({
  selector: 'app-claim-code-management',
  templateUrl: './claim-code-management.component.html',
  styleUrls: ['./claim-code-management.component.scss'],
  
})
export class ClaimCodeManagementComponent implements OnInit {

  claimResonCodeForm = this._formBuilder.group({
    claimSearch: [''],
    claimReasonselect: [''],
    definition: ['',Validators.required],
    cause: ['',Validators.required],
    testing: ['',Validators.required],
    samSizeReq: ['',Validators.required],
    claimCategory: [''],
    visibilityScope: ['',Validators.required],
    type: ['',Validators.required],
    allowanceToKeep: [false],
    serviceTokeep: [false],
    returnEligible: [false],
    returnType: [''],
    distributionCompliance: [false]
    });

    ruleDefinitionGroup = this._formBuilder.group({
    ruleDescription:['', Validators.required],
    ruleCategory:['', Validators.required],
    ruleLevel:['', Validators.required],
    dataType:['', Validators.required],
    dataList:[''],
    sucessMessage:[''],
    allowOverride:[false],
    updateworkStatus:[false],
    workStatus:[''],
    ruleType:['', Validators.required],
    areaofImpact:['', Validators.required],
    actionType:['', Validators.required],
    actionDescription:['', Validators.required],
    failureMessage:[''],
    approvalRequired:[false],
    workStatusRemarks:['']


  });
  possibleResolutionFormGroup = this._formBuilder.group({
    thirdCtrl: ['', Validators.required],
  });

  @Input() collapsed = false;
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true; 
  public claimPhotos: any[]=[];
  public  resolutions: any[] = [];
  public  sortedResolutions: any[] = [];
  public claimResolutions:any[] = [];
  public claimCategories: ClaimCategory[] = [];
  public reasonCodes: any[] = [];
  public selectedClaimCategory: string = '';
  public selectedReasonCode: string = '';
  public showFormFields: boolean = false;
  public selectedReturnType: string = '';
  public returnTypes: any[] = [];
  public returnCategories: any[] =[];
  public orgClaimData: any[] = [];
  public ruleLevels: any[] = [];
  public ruleTypes: any[] = [];
  public selectedResolution: any = {};
  public searchIconDis: boolean = true;
  public codeResolutionId!: number;
  public ruleDefinitionList : any[] = []; 
  public sortedruleDefinitionList : any[] = [];
  public workStatus: any = [];
  public claimCodePhotos:any[]=[];
  public sortclaimCodePhotos:any[]=[];
  public userId!: number;
  public selectedClaimPhoto:any;
  public deleteResourceById : number = 0;
  public deletePhotoId : number = 0;
  public selectedClaimcategory: string = '';
  public resolutionDataTypes: any[] = [];
  public ruleImpactAreaCode: any[] = [];
  public ruleActionTypes: any[] = [];
  public tempClaimSearchData: any 
  //@ViewChild('stepper') stepper!: MatStepper;
  public reasoncodeArray:any[] = [];
  public errorMessage:any
  public reasoncodeIsPresent:any =[];
  public claimReasoncodePayload!:any
  public updateClaimPayload!:any  
  public datalist:any = []; 
  public editPayLoad: string = ''
  public tempStoreReasoncode: any;
  public storeCodeRuleId:number = 0;
  public columnSortStates: { [key: string]: ColumnSortState } = {
    ruleDefinition: {},
    resolution: {},
   
  };
  public typeList:any=[];
  public visibleList:any=[];
  public tabSearchText: any = {description:'',category:'',type:'',level:'',areaOfImpact:'',actionType:''};
  public orgruleDefinitionList:any
  public stepperReasonCode: any;
  public stepperClaimReasondescription: any;
  public stepperclaimReasonId : any ;
  public photoData: any;
  public newChip: string = '';
  public checkStatus:boolean = false;
  public returnEligibleStatus:boolean = false;
  public checkClaimSearch: boolean = false;
  public claimCategoryData:any = 'select claim';
  public visibility: any;
  public lookUpType: any;
  public returnTypeData: any;
  public selectReasonCode: any;
  public ruleCategoryData:any;
  public ruleLevelData: any;
  public dataTypeData: any;
  public ruleTypeData: any;
  public areaofImpactData: any;
  public actionTypeData: any;
  public workStatusData: any;
  public resetReasonCode : boolean = false;
  public selectedRowData : boolean = false;
  isLinear = true;
  // isSecondStepEnabled: boolean = false;
  // isThirdStepEnabled:boolean = false;
  // isFourthStepEnabled:boolean = false;
  @ViewChild('stepper') stepper!: MatStepper;

  step1Completed: boolean = false;
  step2Completed: boolean = false;
  step3Completed: boolean = false;
  step4Completed: boolean = false;
 

  constructor(private _formBuilder: FormBuilder, 
    private bodyClassService: BodyClassService,
    private smartCodeService:SmartCodeService,
    private initiateclaimservice:InitiateClaimService,
    private claimService: ClaimDetailService,
    private snackBar: MatSnackBar,
    private toasterService: ToasterService,
    private router:Router
    ) {}
    
    ngOnInit(){
      console.log(this.claimCategoryData,'claimCategoryData')
      this.userId = Number(localStorage.getItem('userId'));
      // this.getResolutions();
      this.getCrmReasons();
      this.getClaimCategories();
      this.getClaimPhotos();
      this.getCodeRuleReturnType();
      this.getRuleCategory();
      this.getRuleTypes();
      this.getRulelevels();
      // this.getClaimCodePhotos();
    
      this.getCodeRuleDataType();
      this.getCodeRuleImpactArea();
      this.getCodeRuleActionType();
      // this.getCodeRuleByReasonCodeAndUserId();
      this.getWorkStatus();
      this.getLookupTypeForTypeField();
      this.getLookupTypeForVisibilityScopeField();
      this.getAllClaimResolutions();
    }

    public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
 
  completeStep1() {
    // Logic to complete Step 1
    this.step1Completed = true;
    setTimeout(() => this.stepper.next(), 0); // Use setTimeout to ensure next() is called in the next tick
  }

  completeStep2() {
    // Check if Step 1 is completed before allowing access to Step 2
    if (this.step1Completed) {
      // Logic to complete Step 2
      this.step2Completed = true;
      setTimeout(() => this.stepper.next(), 0); // Use setTimeout to ensure next() is called in the next tick
    }
  }

  completeStep3() {
    // Check if Step 2 is completed before allowing access to Step 3
    if (this.step2Completed) {
      // Logic to complete Step 3
      this.step3Completed = true;
      setTimeout(() => this.stepper.next(), 0); // Use setTimeout to ensure next() is called in the next tick
    }
  }


  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  public getClaimPhotos(){
    this.smartCodeService.getClaimPhotos().subscribe(data =>{
      if(data.status === 200){
        this.claimPhotos =data.body
        console.log( this.claimPhotos,' this.claimPhots')
      }
    })
  }

  public  deleteCodePhotos(id: number) {
      this.deletePhotoId = id;
      const modelDiv = document.getElementById('deletecodeModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
     
    }
      
    public  cancelDeletePhotoModel(){
      const modelDiv = document.getElementById('deletecodeModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
    }
  
    public  confirmDeletePhotoModel(){
       this.smartCodeService.deleteCodePhotos(this.deletePhotoId).subscribe((data) => {
      
        if(data.status == 200){
          // this.snackBar.open('Claim Photo Deleted Successfully', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'], 
          // });
          const toastType: 'success' | 'error' | 'warning' = 'success'; 
 const message = 'Claim Photo Deleted Successfully';
 this.toasterService.showToast(toastType, message);
          this.getClaimCodePhotos();
        }
       
          },
            (error) => {
        if(error.status == 400){
          // this.snackBar.open('Failed To Delete Claim Photo', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'], 
          // });
          this.toasterService.showToast('error', 'Failed To Delete Claim Photo.');
          this.getClaimCodePhotos();
        }
      });
  this.cancelDeletePhotoModel();
    }


    public getClaimCategories(): void {
    this.initiateclaimservice.getClaimCategories()
      .subscribe((data: any) => {
        this.claimCategories = data.filter((data: any)=>data.claimCategoryName != 'UNKNOWN')
        // this.claimCategoryData = data[0].claimCategoryCode;
      });
  }
  
 
  public getCrmReasons(){
  this.initiateclaimservice.getCrmReasons().subscribe((data:any)=>{
  this.reasonCodes = data.body;
  // this.selectReasonCode = data.body[0].code
  });
 }

 public checkMaxLength(){
  this.searchIconDis = this.claimResonCodeForm.value.claimSearch?.length == 3 ? false : true;
  this.checkClaimSearch = false;
 }

// Getting the data of findByClaimReasonCode //
public claimCodeSearch(){
  this.step1Completed =false;
  this.step2Completed = false;
  this.step3Completed = false;
  this.step4Completed = false;
    // if(this.resetReasonCode){
    //   this.claimCategoryData = this.claimCategories[0].claimCategoryCode;
    //   this.visibility = this.visibleList[0].lookupId;
    //   this.lookUpType = this.typeList[0].lookupId;
    //   this.returnTypeData =  this.returnTypes[0].codeRuleReturnTypeCode
    // }
    this.checkClaimSearch = false;
    if(this.claimResonCodeForm.value.claimSearch != '' && 
       this.claimResonCodeForm.value.claimSearch?.length == 3){
        this.claimService.findByClaimReasonCode(String(this.claimResonCodeForm.value.claimSearch)).subscribe(
          (data:any)=>{
            if(data.body != null){
              let visibilityData = this.visibleList.filter((element:any)=>element.lookupId == data.body.visibilityScope )
              let typeData = this.typeList.filter((element:any)=>element.lookupId == data.body.reasonTypeGroup )
              this.claimResonCodeForm.patchValue({
                definition: data.body.definition,
                testing: data.body.testing,
                cause: data.body.cause,
                samSizeReq: data.body.sampleSizeRequirement,
                distributionCompliance:data.body.isDistributionCompliance,
                returnEligible:data.body.returnEligible,
                visibilityScope: visibilityData.length != 0 ? visibilityData[0].lookupId : '',
                type:typeData.length != 0 ? typeData[0].lookupId : '' ,
                allowanceToKeep: data.body.allowanceToKeep,
                serviceTokeep: data.body.serviceToKeep
              });
              this.visibility = visibilityData[0].lookupId
              this.lookUpType = typeData[0].lookupId
              this.selectReasonCode = data.body.claimReasonCode.toUpperCase() 
              this.claimCategoryData =data.body.claimCategory.claimCategoryCode
              this.selectReasonCode = data.body.claimReasonCode.toUpperCase() 
              this.selectedClaimcategory =data.body.claimCategory.claimCategoryName
              this.tempClaimSearchData = data.body
              this.returnTypeData = data.body.codeRuleReturnType.codeRuleReturnTypeCode
            }
            else{
              this.claimResonCodeForm.patchValue({
                definition:'',
                testing: '',
                cause: '',
                samSizeReq: '',
                distributionCompliance: false,
                returnEligible: false,
                visibilityScope:  '',
                type: '' ,
                allowanceToKeep: false,
                serviceTokeep: false
              });
              this.selectReasonCode = '' 
              this.selectedClaimcategory = ''
              this.tempClaimSearchData = data.body
              this.checkClaimSearch = true;
            }
           })
         }
  }

  public returnEligibleStatusChange(){
    if(this.claimResonCodeForm.value.returnEligible){
      this.returnEligibleStatus = true;
    }
    else{
      this.returnEligibleStatus = false;
    }
  }

  public  getCodeRuleReturnType(){
    this.smartCodeService.getCodeRuleReturnType().subscribe(
      (response: any) => {
        this.returnTypes = response.body;
        // this.returnTypeData =  response.body[0].codeRuleReturnTypeCode
      },
      (error) => {
      }
    );
  }


  public  saveAndContinueclaimresCode(){
    if(this.claimResonCodeForm.value.returnEligible){
      let status = this.claimResonCodeForm.value.returnType
      if(status == '' || status == undefined){
       return  
      }
     }
    //  let claimReasnId = this.tempClaimSearchData.claimReasonCode
    if (typeof this.tempClaimSearchData !== 'undefined' && this.tempClaimSearchData !== null){
      // if the reasocode already existed update the reasocode//
      this.updateClaimPayload = {
        createdByUserId : Number(localStorage.getItem('userId')),
       modifiedByUserId : Number(localStorage.getItem('userId')),
       claimReasonCode :  this.tempClaimSearchData.claimReasonCode.toUpperCase(),
       claimReasonId :  this.tempClaimSearchData.claimReasonId,
       claimReasonDescription : this.tempClaimSearchData.claimReasonDescription,
       claimCategoryCode : this.tempClaimSearchData.claimCategory.claimCategoryCode,
       visibilityScope : this.claimResonCodeForm.value.visibilityScope,
       reasonTypeGroup : this.tempClaimSearchData.reasonTypeGroup,
       isDistributionCompliance :  this.tempClaimSearchData.isDistributionCompliance,
       sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
       definition : this.claimResonCodeForm.value.definition,
       cause : this.claimResonCodeForm.value.cause,
       testing : this.claimResonCodeForm.value.testing,
       statusId : this.tempClaimSearchData.codeRuleReturnType.statusId,
       allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
       serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
       returnEligible : this.claimResonCodeForm.value.returnEligible,
      //  codeRuleReturnTypeCode : this.tempClaimSearchData.codeRuleReturnType.codeRuleReturnTypeCode
      codeRuleReturnTypeCode:this.returnTypeData 
     }
     this.smartCodeService.updateReasonDefinition(this.updateClaimPayload).subscribe(data=>{
      this.completeStep1();
      this.toasterService.showToast('success', 'Reason code is inserted/updated successfully');
      this.stepperReasonCode=data.body.claimReasonCode;
      this.stepperClaimReasondescription=data.body.claimReasonDescription
      this.stepperclaimReasonId = data.body.claimReasonId
      this.getCodeRuleByReasonCodeAndUserId();
      
      })
    }
   
    else if(this.reasoncodeIsPresent.length != 0){
      this.updateClaimPayload = {
        createdByUserId : Number(localStorage.getItem('userId')),
       modifiedByUserId : Number(localStorage.getItem('userId')),
       claimReasonCode : this.claimResonCodeForm.value.claimReasonselect,
       claimReasonId :  this.reasoncodeIsPresent[0].claimReasonId,
       claimReasonDescription : this.reasoncodeIsPresent[0].claimReasonDescription,
       claimCategoryCode : this.reasoncodeIsPresent[0].claimCategory.claimCategoryCode,
       visibilityScope : this.claimResonCodeForm.value.visibilityScope,
       reasonTypeGroup : this.reasoncodeIsPresent[0].reasonTypeGroup,
       isDistributionCompliance :  this.reasoncodeIsPresent[0].isDistributionCompliance,
       sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
       definition : this.claimResonCodeForm.value.definition,
       cause : this.claimResonCodeForm.value.cause,
       testing : this.claimResonCodeForm.value.testing,
       statusId : this.reasoncodeIsPresent[0].codeRuleReturnType.statusId,
       allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
       serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
       returnEligible : this.claimResonCodeForm.value.returnEligible,
      //  codeRuleReturnTypeCode : this.reasoncodeIsPresent[0].codeRuleReturnType.codeRuleReturnTypeCode
      codeRuleReturnTypeCode:this.returnTypeData 
     }
     this.smartCodeService.updateReasonDefinition(this.updateClaimPayload).subscribe(data=>{
      this.completeStep1();
    this.toasterService.showToast('success', 'Reason code is inserted/updated successfully');
     this.stepperReasonCode=data.body.claimReasonCode;
        this.stepperClaimReasondescription=data.body.claimReasonDescription
        this.stepperclaimReasonId = data.body.claimReasonId
        this.getCodeRuleByReasonCodeAndUserId();
    
     })
    }
    else if((typeof this.tempClaimSearchData == 'undefined' && this.tempClaimSearchData == null) || this.reasoncodeIsPresent.length == 0){
      // If reasoncode not found in DB adding new record//
      let description
      if(this.claimResonCodeForm.value.claimReasonselect != ''){

        description = this.reasonCodes.filter(code=>code.code ==  this.claimResonCodeForm.value.claimReasonselect)
       description = description[0].description
      }
      else{
        description= ''
       }
       let code
       if(this.claimResonCodeForm.value.claimSearch != ''){
        code = this.claimResonCodeForm.value.claimSearch
       }
       else if(this.claimResonCodeForm.value.claimReasonselect != ''){
          code = this.claimResonCodeForm.value.claimReasonselect
       }
      this.claimReasoncodePayload = {
         createdByUserId : Number(localStorage.getItem('userId')),
        claimReasonCode : code,
        claimReasonDescription : description,
        claimCategoryCode : this.claimResonCodeForm.value.claimCategory,
        visibilityScope : this.claimResonCodeForm.value.visibilityScope,
        reasonTypeGroup : this.claimResonCodeForm.value.type,
        isDistributionCompliance :  this.claimResonCodeForm.value.distributionCompliance,
        sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
        definition : this.claimResonCodeForm.value.definition,
        cause : this.claimResonCodeForm.value.cause,
        testing : this.claimResonCodeForm.value.testing,
        statusId :'1',
        allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
        serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
        returnEligible : this.claimResonCodeForm.value.returnEligible,
        codeRuleReturnTypeCode : this.claimResonCodeForm.value.returnType
    
      }
      this.smartCodeService.addReasonDefinition(this.claimReasoncodePayload).subscribe((data:any)=>{
        this.completeStep1() ;
        this.toasterService.showToast('success', 'Reason code is inserted successfully');
        this.stepperReasonCode=data.body.claimReasonCode;
        this.stepperClaimReasondescription=data.body.claimReasonDescription
        this.stepperclaimReasonId = data.body.claimReasonId
        this.getCodeRuleByReasonCodeAndUserId();
        this.resetReasonCode = false;
       
        },
        error=>{
          // this.snackBar.open('System could not process the request now. Try again.', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'], 
          // });
          this.toasterService.showToast('error', 'System could not process the request now. Try again');
          this.errorMessage = error.error.detail
          const modelDiv = document.getElementById('updateModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
        })
    }
  }
  
  public saveAndCloseclaimresCode(){
    if(this.claimResonCodeForm.value.returnEligible){
      let status = this.claimResonCodeForm.value.returnType
      if(status == '' || status == undefined){
       return  
      }
     }
    //  let claimReasnId = this.tempClaimSearchData.claimReasonCode
    if (typeof this.tempClaimSearchData !== 'undefined' && this.tempClaimSearchData !== null){
      // if the reasocode already existed update the reasoncode//
      this.updateClaimPayload = {
        createdByUserId : Number(localStorage.getItem('userId')),
       modifiedByUserId : Number(localStorage.getItem('userId')),
       claimReasonCode : this.tempClaimSearchData.claimReasonCode.toUpperCase() ,
       claimReasonId :  this.tempClaimSearchData.claimReasonId,
       claimReasonDescription : this.tempClaimSearchData.claimReasonDescription,
       claimCategoryCode : this.tempClaimSearchData.claimCategory.claimCategoryCode,
       visibilityScope : this.claimResonCodeForm.value.visibilityScope,
       reasonTypeGroup : this.tempClaimSearchData.reasonTypeGroup,
       isDistributionCompliance :  this.tempClaimSearchData.isDistributionCompliance,
       sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
       definition : this.claimResonCodeForm.value.definition,
       cause : this.claimResonCodeForm.value.cause,
       testing : this.claimResonCodeForm.value.testing,
       statusId : this.tempClaimSearchData.codeRuleReturnType.statusId,
       allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
       serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
       returnEligible : this.claimResonCodeForm.value.returnEligible,
      //  codeRuleReturnTypeCode : this.tempClaimSearchData.codeRuleReturnType.codeRuleReturnTypeCode
      codeRuleReturnTypeCode:this.returnTypeData 
   
     }
     this.smartCodeService.updateReasonDefinition(this.updateClaimPayload).subscribe(data=>{
      this.toasterService.showToast('success', 'Reason code is inserted/updated successfully');
     this.router.navigate(['/dashboard'])
      })
    }
   
    else if(this.reasoncodeIsPresent.length != 0){
      this.updateClaimPayload = {
        createdByUserId : Number(localStorage.getItem('userId')),
       modifiedByUserId : Number(localStorage.getItem('userId')),
       claimReasonCode : this.claimResonCodeForm.value.claimReasonselect,
       claimReasonId :  this.reasoncodeIsPresent[0].claimReasonId,
       claimReasonDescription : this.reasoncodeIsPresent[0].claimReasonDescription,
       claimCategoryCode : this.reasoncodeIsPresent[0].claimCategory.claimCategoryCode,
       visibilityScope : this.claimResonCodeForm.value.visibilityScope,
       reasonTypeGroup : this.reasoncodeIsPresent[0].reasonTypeGroup,
       isDistributionCompliance :  this.reasoncodeIsPresent[0].isDistributionCompliance,
       sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
       definition : this.claimResonCodeForm.value.definition,
       cause : this.claimResonCodeForm.value.cause,
       testing : this.claimResonCodeForm.value.testing,
       statusId : this.reasoncodeIsPresent[0].codeRuleReturnType.statusId,
       allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
       serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
       returnEligible : this.claimResonCodeForm.value.returnEligible,
      //  codeRuleReturnTypeCode : this.reasoncodeIsPresent[0].codeRuleReturnType.codeRuleReturnTypeCode
      codeRuleReturnTypeCode:this.returnTypeData 
     }
     this.smartCodeService.updateReasonDefinition(this.updateClaimPayload).subscribe(data=>{
    this.toasterService.showToast('success', 'Reason code is inserted/updated successfully');
     this.router.navigate(['/dashboard'])
     })
    }
    else if((typeof this.tempClaimSearchData == 'undefined' && this.tempClaimSearchData == null) || this.reasoncodeIsPresent.length == 0){
      // If reasoncode not found in DB adding new record//
      const description = this.reasonCodes.filter(code=>code.code ==  this.claimResonCodeForm.value.claimReasonselect)
      this.claimReasoncodePayload = {
         createdByUserId : Number(localStorage.getItem('userId')),
        claimReasonCode : this.claimResonCodeForm.value.claimReasonselect,
        claimReasonDescription : '' ,
        claimCategoryCode : this.claimResonCodeForm.value.claimCategory,
        visibilityScope : this.claimResonCodeForm.value.visibilityScope,
        reasonTypeGroup : this.claimResonCodeForm.value.type,
        isDistributionCompliance :  this.claimResonCodeForm.value.distributionCompliance,
        sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
        definition : this.claimResonCodeForm.value.definition,
        cause : this.claimResonCodeForm.value.cause,
        testing : this.claimResonCodeForm.value.testing,
        statusId :'1',
        allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
        serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
        returnEligible : this.claimResonCodeForm.value.returnEligible,
        codeRuleReturnTypeCode : this.claimResonCodeForm.value.returnType
    
      }
      this.smartCodeService.addReasonDefinition(this.claimReasoncodePayload).subscribe(data=>{
        this.toasterService.showToast('success', 'Reason code is inserted successfully');
       this.router.navigate(['/dashboard'])
       this.resetReasonCode = false;
        },
        error=>{
          this.errorMessage = error.error.detail
          const modelDiv = document.getElementById('updateModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
        })
    }
   
   
  }
  
  public continueToUpdate(){
    if(this.claimResonCodeForm.value.returnEligible){
      let status = this.claimResonCodeForm.value.returnType
      if(status == '' || status == undefined){
       return  
      }
     }
    const description = this.reasonCodes.filter(code=>code.code ==  this.claimResonCodeForm.value.claimReasonselect)
    this.claimReasoncodePayload = {
       createdByUserId : Number(localStorage.getItem('userId')),
      claimReasonCode : this.claimResonCodeForm.value.claimReasonselect,
      claimReasonDescription : description[0].description,
      claimCategoryCode : this.claimResonCodeForm.value.claimCategory,
      visibilityScope : this.claimResonCodeForm.value.visibilityScope,
      reasonTypeGroup : this.claimResonCodeForm.value.type,
      isDistributionCompliance :  this.claimResonCodeForm.value.distributionCompliance,
      sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
      definition : this.claimResonCodeForm.value.definition,
      cause : this.claimResonCodeForm.value.cause,
      testing : this.claimResonCodeForm.value.testing,
      statusId :'1',
      allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
      serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
      returnEligible : this.claimResonCodeForm.value.returnEligible,
      codeRuleReturnTypeCode : this.claimResonCodeForm.value.returnType
  
    }
    this.smartCodeService.updateReasonDefinition(this.claimReasoncodePayload).subscribe(data=>{
      this.toasterService.showToast('success', 'ReasonCode is inserted successfully');
      this.stepper.next();
    })
  }
  public checkSelectedClaimReasoncode(){
    this.checkClaimSearch = false;
    this.claimService.getAllReasonCodes().subscribe(data=>{
      this.reasoncodeIsPresent = [] 
      this.reasoncodeIsPresent = data.filter((obj:any) => obj.claimReasonCode.toLowerCase() === this.claimResonCodeForm.value.claimReasonselect?.toLowerCase());
     if(this.reasoncodeIsPresent.length != 0) // claimreasonrecord present autopopulate to form//
     {
      let visibilityData = this.visibleList.filter((element:any)=>element.lookupId == this.reasoncodeIsPresent[0].visibilityScope )
      let typeData = this.typeList.filter((element:any)=>element.lookupId ==this.reasoncodeIsPresent[0].reasonTypeGroup )
     
      this.claimResonCodeForm.patchValue({
        definition: this.reasoncodeIsPresent[0].definition,
        testing:this.reasoncodeIsPresent[0]. testing,
        cause:this.reasoncodeIsPresent[0].cause,
        samSizeReq: this.reasoncodeIsPresent[0].sampleSizeRequirement,
        claimSearch:this.claimResonCodeForm.value.claimReasonselect,
        distributionCompliance:this.reasoncodeIsPresent[0].isDistributionCompliance,
        returnEligible:this.reasoncodeIsPresent[0].returnEligible,
        visibilityScope: visibilityData.length != 0 ? visibilityData[0].lookupId : '',
        type:typeData.length != 0 ? typeData[0].lookupId : '' ,
        allowanceToKeep:this.reasoncodeIsPresent[0].allowanceToKeep,
        serviceTokeep:this.reasoncodeIsPresent[0].serviceToKeep
      });
      this.visibility = visibilityData[0].lookupId
      this.lookUpType = typeData[0].lookupId
      this.selectReasonCode =this.reasoncodeIsPresent[0].claimReasonCode.toUpperCase() 
      this.claimCategoryData =this.reasoncodeIsPresent[0].claimCategory.claimCategoryCode
      this.returnTypeData = this.reasoncodeIsPresent[0].codeRuleReturnType.codeRuleReturnTypeCode
     }
     else{
      this.claimResonCodeForm.patchValue({
        definition:'',
        testing:'',
        cause:'',
        samSizeReq: '',
        claimSearch:this.claimResonCodeForm.value.claimReasonselect,
        distributionCompliance: false,
        returnEligible: false,
         visibilityScope:  '',
        type: '' ,
        allowanceToKeep: false,
         serviceTokeep: false
      });
     }
     this.tempClaimSearchData = null
    })
  }
  // closing the Modals based on Id//
  public closeModal(id:string){
  const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
}
// if existed reasoncode is there calling the update api for modal//
public updatedReasonCodeModal(){
  if (typeof this.tempClaimSearchData !== 'undefined' && this.tempClaimSearchData !== null){
    // if the reasocode already existed update the reasocode//
    this.updateClaimPayload = {
      createdByUserId : Number(localStorage.getItem('userId')),
     modifiedByUserId : Number(localStorage.getItem('userId')),
     claimReasonCode : this.tempClaimSearchData.claimReasonCode.toUpperCase(),
     claimReasonId :  this.tempClaimSearchData.claimReasonId,
     claimReasonDescription : this.tempClaimSearchData.claimReasonDescription,
     claimCategoryCode : this.tempClaimSearchData.claimCategory.claimCategoryCode,
     visibilityScope : this.claimResonCodeForm.value.visibilityScope,
     reasonTypeGroup : this.tempClaimSearchData.reasonTypeGroup,
     isDistributionCompliance :  this.tempClaimSearchData.isDistributionCompliance,
     sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
     definition : this.claimResonCodeForm.value.definition,
     cause : this.claimResonCodeForm.value.cause,
     testing : this.claimResonCodeForm.value.testing,
     statusId : this.tempClaimSearchData.codeRuleReturnType.statusId,
     allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
     serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
     returnEligible : this.claimResonCodeForm.value.returnEligible,
     codeRuleReturnTypeCode : this.tempClaimSearchData.codeRuleReturnType.codeRuleReturnTypeCode
 
   }
   this.smartCodeService.updateReasonDefinition(this.updateClaimPayload).subscribe(data=>{
    this.toasterService.showToast('success', 'Reason code is inserted/updated successfully');
   this.router.navigate(['/dashboard'])
    })
  }
 
  else if(this.reasoncodeIsPresent.length != 0){
    this.updateClaimPayload = {
      createdByUserId : Number(localStorage.getItem('userId')),
     modifiedByUserId : Number(localStorage.getItem('userId')),
     claimReasonCode : this.tempClaimSearchData.claimReasonCode.toUpperCase(),
     claimReasonId :  this.reasoncodeIsPresent[0].claimReasonId,
     claimReasonDescription : this.reasoncodeIsPresent[0].claimReasonDescription,
     claimCategoryCode : this.reasoncodeIsPresent[0].claimCategory.claimCategoryCode,
     visibilityScope : this.claimResonCodeForm.value.visibilityScope,
     reasonTypeGroup : this.reasoncodeIsPresent[0].reasonTypeGroup,
     isDistributionCompliance :  this.reasoncodeIsPresent[0].isDistributionCompliance,
     sampleSizeRequirement : this.claimResonCodeForm.value.samSizeReq,
     definition : this.claimResonCodeForm.value.definition,
     cause : this.claimResonCodeForm.value.cause,
     testing : this.claimResonCodeForm.value.testing,
     statusId : this.reasoncodeIsPresent[0].codeRuleReturnType.statusId,
     allowanceToKeep : this.claimResonCodeForm.value.allowanceToKeep,
     serviceToKeep : this.claimResonCodeForm.value.serviceTokeep,
     returnEligible : this.claimResonCodeForm.value.returnEligible,
    //  codeRuleReturnTypeCode : this.reasoncodeIsPresent[0].codeRuleReturnType.codeRuleReturnTypeCode
    codeRuleReturnTypeCode:this.returnTypeData 
   }
   this.smartCodeService.updateReasonDefinition(this.updateClaimPayload).subscribe(data=>{
  this.toasterService.showToast('success', 'Reason code is inserted/updated successfully');
   this.router.navigate(['/dashboard'])
   })
  }
}

public saveAndContinueBtnDisable(){
  if (this.checkClaimSearch) {
    return true;
  }
  if(this.claimResonCodeForm.value.returnEligible){
    let status = this.claimResonCodeForm.value.returnType
    if((status == '' || status == undefined) && this.claimResonCodeForm.valid){
     return true;
    }else{
      return false;
    }
   }else{
    if(this.claimResonCodeForm.valid){
      return false;
    }else{
      return true;
    }
   }
}

public  getRuleCategory(){
    this.smartCodeService.getCodeRuleCategory().subscribe((data:any)=>{
     this.returnCategories = data.body;
     this.ruleCategoryData = data.body[0].codeRuleCategoryCode
    

    })
  }
  public getRulelevels(){
    this.smartCodeService.getCodeRuleLevel().subscribe((data:any)=>{
      this.ruleLevels = data.body
      this.ruleLevelData = data.body[0].codeRuleLevelCode
    })
  }
  public  getRuleTypes(){
    this.smartCodeService.getCodeRuleType().subscribe((data:any)=>{
      this.ruleTypes = data.body
      this.ruleTypeData = data.body[0].codeRuleTypeCode
    })
  }

  public getClaimCodePhotos(){
  this.smartCodeService.getClaimCodePhotos(this.stepperclaimReasonId).subscribe(data =>{
    if(data.status === 200){
      this.claimCodePhotos =data.body
      this.sortclaimCodePhotos = data.body.slice()
    }
  })
  
}

public isRowPhotoDisabled(item: any): boolean {
  return this.claimCodePhotos.some(disabledItem => disabledItem.photoId === item.photoId);
}

public openClaimPhotosModal() {
  const modelDiv = document.getElementById('claimPhotosModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}
public closeClaimPhotosModal() {
  const modelDiv = document.getElementById('claimPhotosModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}

public previewPhoto(photo: any) {
  // Open a new window with the image preview
  const newWindow = window.open('', '_blank');
  
  if (newWindow) {
    newWindow.document.write(`
      <html>
        <head>
          <title>${photo.photoName}</title>
        </head>
        <body style="margin: 0; display: flex; justify-content: center; align-items: center; height: 100vh;">
          <img src="data:image/jpeg;base64,${photo.photo}" alt="${photo.photoName}" class="img-fluid">
          <script>
            // Close the window on button click (optional)
            function closeWindow() {
              window.close();
            }
          </script>
        </body>
      </html>
    `);
  } else {
    // Handle if the new window fails to open
  }
}
public onCheckboxChange(photo: any) {
  // Update the selectedPhoto property when a checkbox is checked or unchecked
  this.selectedClaimPhoto = this.selectedClaimPhoto === photo ? null : photo;
 
}

public saveCodePhotos(){
  console.log(this.stepperReasonCode,'stepperReasonCode');
  console.log(this.stepperclaimReasonId,'stepperclaimReasonId')
  const CodePhotosPayload = {
    "createdByUserId": this.selectedClaimPhoto.createdByUserId,
    "modifiedByUserId":this.selectedClaimPhoto.modifiedByUserId,
    "claimReasonDefinitionDTO": {
      "createdByUserId": this.selectedClaimPhoto.modifiedByUserId,
      "modifiedByUserId": this.selectedClaimPhoto.modifiedByUserId,
      "claimReasonCode":this.stepperReasonCode,
      "claimReasonId": this.stepperclaimReasonId,
      "claimReasonDescription":this.stepperClaimReasondescription
    },
    "claimPhotosDTO": {
      "photoId": this.selectedClaimPhoto.photoId,
      "photoName": this.selectedClaimPhoto.photoName,
      "description": this.selectedClaimPhoto.photoDescription
    }
  }
this.smartCodeService.saveCodePhotos(CodePhotosPayload).subscribe(
  (data) => {
    this.completeStep3();
 this.getClaimCodePhotos()
    this.closeClaimPhotosModal();
    this.toasterService.showToast('success', 'Photos Associated successfully');

  },
  (error) => {
    this.toasterService.showToast('error', 'Failed to Associate');
    this.closeClaimPhotosModal();
  }
)
}

public getCodeRuleDataType(){
  this.smartCodeService.getCodeRuleDataType().subscribe((data:any)=>{
    this.resolutionDataTypes = data.body;
    this.dataTypeData =  data.body[0].codeRuleDataTypeCode
  })
}

public fetchDataListByDataType(){
this.smartCodeService.fetchDataListByDataType(String(this.ruleDefinitionGroup.value.dataType)).subscribe(data=>{
  this.datalist = data.body;
 
})
}

public getCodeRuleImpactArea(){
  this.smartCodeService.getCodeRuleImpactArea().subscribe((data:any)=>{
    this.ruleImpactAreaCode = data.body;
    this.areaofImpactData =  data.body[0].codeRuleImpactAreaCode;
  })
}

public getCodeRuleActionType(){
  this.smartCodeService.getCodeRuleActionType().subscribe((data:any)=>{
    this.ruleActionTypes = data.body;
    this.actionTypeData =  data.body[0].codeRuleActionTypeCode;
  })
}

// hiding the formFields section for Edit and Addnewrule//
public toggleFormFields() {
  this.showFormFields = !this.showFormFields;
  if(this.showFormFields){
    this.editPayLoad = 'save'
  }
}

// For the field with formControlName="type"
public getLookupTypeForTypeField() {
const lookupTypeCode = 'CLM_RSN_TYPE'; // Specify the lookupTypeCode for the "type" field
this.smartCodeService.getLookupTypeByCode(lookupTypeCode).subscribe(data => {
  this.typeList = data.body
  // this.lookUpType = data.body[0].lookupId
});
}

// For the field with formControlName="visibilityScope"
public getLookupTypeForVisibilityScopeField() {
const lookupTypeCode = 'CLM_RSN_VISIBILITY'; // Specify the lookupTypeCode for the "visibilityScope" field
this.smartCodeService.getLookupTypeByCode(lookupTypeCode).subscribe(data => {
  this.visibleList = data.body
  // this.visibility = data.body[0].lookupId
});
}

public getCodeRuleByReasonCodeAndUserId(){
  this.smartCodeService.getCodeRuleByReasonCode(this.stepperReasonCode).subscribe(data=>{
   this.ruleDefinitionList = data.body
   this.orgruleDefinitionList=data.body
   this.sortedruleDefinitionList = data.body.slice()
   this.stepper.next();
  })
}
public getWorkStatus(){
  this.smartCodeService.getWorkStatus().subscribe(data=>{
     this.workStatus = data.body ;
     this.workStatusData =  data.body[0].workStatusCode;
  })
}

public editRuleDefinition(rule:any){
  this.tempStoreReasoncode = rule
  this.ruleDefinitionGroup.patchValue({
    ruleDescription:rule.ruleDescription,
    ruleCategory:rule.codeRuleCategoryDTO.codeRuleCategoryCode,
    ruleLevel:rule.codeRuleLevelDTO.codeRuleLevelCode,
    dataType:rule.codeRuleDataTypeDTO.codeRuleDataTypeCode,
    // dataList:rule.,
    sucessMessage:rule.successMessage,
    allowOverride:rule.allowOverride,
    updateworkStatus:rule.updateWorkStatus,
    workStatus:rule.workStatusDTO.workStatusDescription,
    ruleType:rule.codeRuleTypeDTO.codeRuleTypeCode,
    areaofImpact:rule.codeRuleImpactAreaDTO.codeRuleImpactAreaCode,
    actionType:rule.codeRuleActionTypeDTO.codeRuleActionTypeCode,
    actionDescription:rule.actionDescription,
    failureMessage:rule.failureMessage,
    approvalRequired:rule.approvalRequired,
    workStatusRemarks:rule.workStatusRemarks
  })
  this.fetchDataListByDataType();
  this.showFormFields = true;
  this.editPayLoad = 'edit'
}

// saving method//
public saveRuleDefinition(){
  if(this.ruleDefinitionGroup.value.updateworkStatus){
    this.checkStatus = true;
    let status = this.ruleDefinitionGroup.value.workStatus
    let reMarks =this.ruleDefinitionGroup.value.workStatusRemarks
    if(status == '' || status == undefined){
     return  
    }
    if(reMarks == '' || reMarks == undefined){
      return  
     }
    
  }
 const saveRuledefinitionPayload = {
  "createdByUserId": Number(localStorage.getItem('userId')),
  "modifiedByUserId": Number(localStorage.getItem('userId')),
  "codeRuleId": 0,
  "claimReasonDefinitionDTO": {
    "claimReasonCode": this.claimResonCodeForm.value.claimSearch,
  },
  "ruleDescription": this.ruleDefinitionGroup.value.ruleDescription,
  "codeRuleCategoryDTO": {
    "codeRuleCategoryCode":this.ruleDefinitionGroup.value.ruleCategory,
  },
  "codeRuleTypeDTO": {
    "codeRuleTypeCode": this.ruleDefinitionGroup.value.ruleType,
  },
  "codeRuleLevelDTO": {
    "codeRuleLevelCode": this.ruleDefinitionGroup.value.ruleLevel,
  },
  "codeRuleImpactAreaDTO": {
    "codeRuleImpactAreaCode": this.ruleDefinitionGroup.value.areaofImpact,
  },
  "codeRuleActionTypeDTO": {
    "codeRuleActionTypeCode": this.ruleDefinitionGroup.value.actionType,
  },
  "codeRuleDataTypeDTO": {
    "codeRuleDataTypeCode": this.ruleDefinitionGroup.value.dataType,
  },
  "actionDescription": this.ruleDefinitionGroup.value.actionDescription,
  "successMessage": this.ruleDefinitionGroup.value.sucessMessage,
  "failureMessage": this.ruleDefinitionGroup.value.failureMessage,
  "allowOverride": this.ruleDefinitionGroup.value.allowOverride,
  "approvalRequired": this.ruleDefinitionGroup.value.approvalRequired,
  "updateWorkStatus": this.ruleDefinitionGroup.value.updateworkStatus,
  "workStatusDTO": {
    "workStatusCode": this.ruleDefinitionGroup.value.workStatus,
  },
  "workStatusRemarks": this.ruleDefinitionGroup.value.workStatusRemarks,
  "codeRuleDataListDTO": {
    "dataListValue": this.datalist.join()
  }
}
if(this.editPayLoad == 'edit'){
  saveRuledefinitionPayload.codeRuleId = this.tempStoreReasoncode.codeRuleId
   saveRuledefinitionPayload.claimReasonDefinitionDTO = this.tempStoreReasoncode.claimReasonDefinitionDTO
}
this.smartCodeService.saveCodeRules(saveRuledefinitionPayload).subscribe(data=>{
  this.completeStep2()
  const successMessage = this.editPayLoad === 'edit' ? 'updated' : 'added';

  this.toasterService.showToast('success', `Rule is ${successMessage} successfully`);
  this.stepper.next();
 this.getResolutions();
 
})


}
// Edit method//
public saveAndCloseRuleDefinition(){
  if(this.ruleDefinitionGroup.value.updateworkStatus){
    this.checkStatus = true;
    let status = this.ruleDefinitionGroup.value.workStatus
    let reMarks =this.ruleDefinitionGroup.value.workStatusRemarks
    if(status == '' || status == undefined){
     return  
    }
    if(reMarks == '' || reMarks == undefined){
      return  
     }
    
  }
  const saveRuledefinitionPayload = {
    "createdByUserId": Number(localStorage.getItem('userId')),
    "modifiedByUserId": Number(localStorage.getItem('userId')),
    "codeRuleId": 0,
    "claimReasonDefinitionDTO": {
      "claimReasonCode": this.claimResonCodeForm.value.claimSearch,
    },
    "ruleDescription": this.ruleDefinitionGroup.value.ruleDescription,
    "codeRuleCategoryDTO": {
      "codeRuleCategoryCode":this.ruleDefinitionGroup.value.ruleCategory,
    },
    "codeRuleTypeDTO": {
      "codeRuleTypeCode": this.ruleDefinitionGroup.value.ruleType,
    },
    "codeRuleLevelDTO": {
      "codeRuleLevelCode": this.ruleDefinitionGroup.value.ruleLevel,
    },
    "codeRuleImpactAreaDTO": {
      "codeRuleImpactAreaCode": this.ruleDefinitionGroup.value.areaofImpact,
    },
    "codeRuleActionTypeDTO": {
      "codeRuleActionTypeCode": this.ruleDefinitionGroup.value.actionType,
    },
    "codeRuleDataTypeDTO": {
      "codeRuleDataTypeCode": this.ruleDefinitionGroup.value.dataType,
    },
    "actionDescription": this.ruleDefinitionGroup.value.actionDescription,
    "successMessage": this.ruleDefinitionGroup.value.sucessMessage,
    "failureMessage": this.ruleDefinitionGroup.value.failureMessage,
    "allowOverride": this.ruleDefinitionGroup.value.allowOverride,
    "approvalRequired": this.ruleDefinitionGroup.value.approvalRequired,
    "updateWorkStatus": this.ruleDefinitionGroup.value.updateworkStatus,
    "workStatusDTO": {
      "workStatusCode": this.ruleDefinitionGroup.value.workStatus,
    },
    "workStatusRemarks": this.ruleDefinitionGroup.value.workStatusRemarks,
    "codeRuleDataListDTO": {
      "dataListValue": this.datalist.join()
    }
  }
  if(this.editPayLoad == 'edit'){
    saveRuledefinitionPayload.codeRuleId = this.tempStoreReasoncode.codeRuleId
    saveRuledefinitionPayload.claimReasonDefinitionDTO = this.tempStoreReasoncode.claimReasonDefinitionDTO
  }
  this.smartCodeService.saveCodeRules(saveRuledefinitionPayload).subscribe(data=>{
    const successMessage = this.editPayLoad === 'edit' ? 'updated' : 'added';
    this.toasterService.showToast('success', `Rule is ${successMessage} successfully`);
   
  // this.getCodeRuleByReasonCodeAndUserId();
  this.smartCodeService.getCodeRuleByReasonCode(this.stepperReasonCode).subscribe(data=>{
    this.ruleDefinitionList = data.body
    this.orgruleDefinitionList=data.body
  })
  
  }) 
  this.showFormFields = false;
  
}
public cancelRuleDefinition(){
  this.showFormFields = false;
}

public openDeleteRuleModal(id:number){
  this.storeCodeRuleId = id
  const modelDiv = document.getElementById('deleteRuleModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }
}

public deleteCodeRuledefinition(){
 this.smartCodeService.deleteCodeRuleById( this.storeCodeRuleId).subscribe(data=>{
  // this.snackBar.open(' Rule Definition are deleted sucessfully ', 'Close', {
  //   duration: 3000,
  //   verticalPosition: 'top',
  //   panelClass: ['custom-snackbar'], 
  // });
  this.toasterService.showToast('success', 'Rule Definition are deleted sucessfully');
  this.closeModal('deleteRuleModal');
  this.smartCodeService.getCodeRuleByReasonCode(this.stepperReasonCode).subscribe(data=>{
    this.ruleDefinitionList = data.body
    this.orgruleDefinitionList=data.body
    
   })
 })

}


sortRuledefiniton(sort: Sort) {
  const data = this.ruleDefinitionList.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortedruleDefinitionList = data;
    return;
  }

  this.sortedruleDefinitionList = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'description':
        return this.compare(a.ruleDescription, b.ruleDescription, isAsc);
      case 'category':
        return this.compare(a.codeRuleCategoryDTO.codeRuleCategoryDescription, b.codeRuleCategoryDTO.codeRuleCategoryDescription, isAsc);
      case 'type':
        return this.compare(a.codeRuleTypeDTO.codeRuleTypeDescription , b.codeRuleTypeDTO.codeRuleTypeDescription , isAsc);
        case 'level':
          return this.compare(a.codeRuleLevelDTO.codeRuleLevelDescription , b.codeRuleLevelDTO.codeRuleLevelDescription , isAsc);
          case 'areaOfimpact':
            return this.compare(a.codeRuleImpactAreaDTO.codeRuleImpactAreaDescription , b.codeRuleImpactAreaDTO.codeRuleImpactAreaDescription , isAsc);
            case 'actionType':
              return this.compare(a.codeRuleActionTypeDTO.codeRuleActionTypeDescription , b.codeRuleActionTypeDTO.codeRuleActionTypeDescription , isAsc);
             
        default:
        return 0;
    }
  });
}

sortPossibleresolutions(sort: Sort) {
  const data = this.resolutions.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortedResolutions = data;
    return;
  }

  this.sortedResolutions = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'resolutionName':
        return this.compare(a.claimResolution.resolutionName, b.claimResolution.resolutionName, isAsc);
      case 'description':
        return this.compare(a.claimResolution.description, b.claimResolution.description, isAsc);
      case 'procedureManualReference':
        return this.compare(a.claimResolution.procedureManual , b.claimResolution.procedureManual , isAsc);
        
        default:
        return 0;
    }
  });
}

sortPhotos(sort: Sort) {
  const data = this.claimCodePhotos.slice(); // Use productCatalog for sorting

  if (!sort.active || sort.direction === '') {
    this.sortclaimCodePhotos = data;
    return;
  }

  this.sortclaimCodePhotos = data.sort((a:any, b:any) => {
    const isAsc = sort.direction === 'asc';
    switch (sort.active) {
      case 'resolutionName':
        return this.compare(a.photoName, b.photoName, isAsc);
      case 'description':
        return this.compare(a.photoDescription, b.photoDescription, isAsc);
       
        default:
        return 0;
    }
  });
}


// Comparison function for sorting
 compare(a: string | number, b: string | number, isAsc: boolean) {
return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}

public onTabRuleDefinitionSearch(searchText:any,colName:any){
 if(!searchText){
  this.sortedruleDefinitionList = this.orgruleDefinitionList;
 }else{
 switch(colName){
  case 'description':{
    this.sortedruleDefinitionList = this.orgruleDefinitionList.filter((list: any) => {
      if ( list.ruleDescription != null) {
        return list.ruleDescription
          .toLowerCase()
          .includes(searchText.toLowerCase());
      }
    });
    break
  }
  case 'category':{
    this.sortedruleDefinitionList = this.orgruleDefinitionList.filter((list: any) => {
      if (list.codeRuleCategoryDTO.codeRuleCategoryDescription  != null) {
        return list.codeRuleCategoryDTO.codeRuleCategoryDescription 
          .toLowerCase()
          .includes(searchText.toLowerCase());
      }
    });
    break
  }
  case 'type':{
    this.sortedruleDefinitionList = this.orgruleDefinitionList.filter((list: any) => {
      if (list.codeRuleTypeDTO.codeRuleTypeDescription != null) {
        return list.codeRuleTypeDTO.codeRuleTypeDescription
          .toLowerCase()
          .includes(searchText.toLowerCase());
      }
    });
    break
  }
  case 'level':{
    this.sortedruleDefinitionList = this.orgruleDefinitionList.filter((list: any) => {
      if (list.codeRuleLevelDTO.codeRuleLevelDescription != null) {
        return list.codeRuleLevelDTO.codeRuleLevelDescription
          .toLowerCase()
          .includes(searchText.toLowerCase());
      }
    });
    break
  }
  case 'areaOfImpact':{
    this.sortedruleDefinitionList = this.orgruleDefinitionList.filter((list: any) => {
      if (list.codeRuleImpactAreaDTO.codeRuleImpactAreaDescription != null) {
        return list.codeRuleImpactAreaDTO.codeRuleImpactAreaDescription
          .toLowerCase()
          .includes(searchText.toLowerCase());
      }
    });
    break
  }
  case 'actionType':{
    this.sortedruleDefinitionList = this.orgruleDefinitionList.filter((list: any) => {
      if (list.codeRuleActionTypeDTO.codeRuleActionTypeDescription != null) {
        return list.codeRuleActionTypeDTO.codeRuleActionTypeDescription
          .toLowerCase()
          .includes(searchText.toLowerCase());
      }
    });
  }
 }
 }
}

onKeyPress(event: Event): void {
  // Cast the event to KeyboardEvent
  const keyboardEvent = event as KeyboardEvent;

  // Prevent the input of space character (key code 32)
  if (keyboardEvent.keyCode === 32) {
    keyboardEvent.preventDefault();
  }
}


public getResolutions(){
  this.smartCodeService.getClaimCodeResolution(this.stepperclaimReasonId).subscribe(
    (response: any) => {
      this.resolutions = response.body;
      this.sortedResolutions = response.body.slice()
    },
    (error: any) => {
    }
  );
}

public getAllClaimResolutions(){
  this.smartCodeService.getAllClaimResolutions().subscribe(
    (response: any) => {
      this.claimResolutions = response.body;
      this.claimResolutions.forEach((element:any)=>{
        element['selected'] = false;
      })
    },
    (error: any) => {
    }
  );
}



public resolutionRowSelection(selectedRow: any) {
  selectedRow.selected = !selectedRow.selected;
  if(selectedRow.selected){
    for (const user of this.claimResolutions) {
      if (user !== selectedRow) {
        user.selected = false;
      }
      if (user == selectedRow) {
        this. selectedResolution = selectedRow;
      }
    }
    this.selectedRowData = this.claimResolutions.some(
      (user: { selected: any }) => user.selected
    );

  }else{
    this. selectedResolution = {}
    this.selectedRowData = false;
  }
  
}



public enableAssociateResolution():boolean{
  return Object.keys(this.selectedResolution).length === 0
}

public openResloutionModal(){
  const modelDiv = document.getElementById('resolutionModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
}

public isRowDisabled(item: any): boolean {
  return this.resolutions.some(disabledItem => disabledItem.claimResolution.resolutionId === item.resolutionId);
}

public associateResolution(): void {
  const payload = {
"createdByUserId": Number(localStorage.getItem('userId')),
"modifiedByUserId":Number(localStorage.getItem('userId')),
"claimReasonId": this.stepperclaimReasonId,
"resolutionId": this.selectedResolution.resolutionId
  };


  if(Object.keys(this.selectedResolution).length != 0){
    this.smartCodeService.createCodeResolution(payload).subscribe(
      (response: any) => {
      this.toasterService.showToast('success', 'Associate Resolutions are Added Sucessfully');
      this.closeModal('resolutionModal')
       this.getResolutions(); 
    
      // this.stepper.next();
      },
      
      (error: any) => {
        this.toasterService.showToast('error', ' Associate Resolutions are Failed');
      
      }
    );  
  }
  
}


public deleteCodeResolution(): void {
  this.smartCodeService.deleteCodeResolution(this.deleteResourceById).subscribe(
      (response) => {
          // this.snackBar.open(' Resolutions are deleted sucessfully ', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'], 
          // });
          this.toasterService.showToast('success', ' Resolutions are deleted sucessfully ');
          this.closeModal('deleteModal')
          this.getResolutions();
      },
      (error) => {
      }
  );
}

public deleteResourceModel(id:number){
  this.deleteResourceById = id
  const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
}

public backToPrevious(){
  this.resetReasonCode = true;
  this.stepper.previous();
}

public skipToSecondTab(){
  this.stepper.next();
}

// public skipToNextTab(){
//   this.completeStep2();
//   this.stepper.next();
// }

public skipToNextTab(){
  this.completeStep2();
  this.getResolutions();
    this.stepper.next();
}
public skipToFourthStep(){
  this.completeStep3();
  this.getClaimCodePhotos();
  this.stepper.next();
}

public add(chip:string) {
    this.newChip = chip
    if (this.newChip && !this.datalist.includes(this.newChip)) {
      this.datalist.push(this.newChip);
      this.newChip = '';
    }
  }

  public  remove(chip: string) {
    const index = this.datalist.indexOf(chip);
    if (index >= 0) {
      this.datalist.splice(index, 1);
    }
  }

  public statusChange(){
    if(this.ruleDefinitionGroup.value.updateworkStatus){
      this.checkStatus = true;
    }
    else{
      this.checkStatus = false;
    }
  }


  public checkRuleDefinitionStatus():boolean{
    if(this.ruleDefinitionGroup.valid){
      if( this.ruleDefinitionGroup.value.updateworkStatus){
        if(this.ruleDefinitionGroup.value.workStatus != '' 
        && this.ruleDefinitionGroup.value.workStatusRemarks !=''){
         if(this.actionTypeData == 'Message'){
           if(this.ruleDefinitionGroup.value.failureMessage != '' || this.ruleDefinitionGroup.value.sucessMessage != ''){
             return false;
           }else{
             return true;
           }
         }else{
           return false
         }
        } else{
         return true
        }
       }else{
         if(this.actionTypeData == 'Message'){
           if(this.ruleDefinitionGroup.value.failureMessage != '' || this.ruleDefinitionGroup.value.sucessMessage != ''){
             return false;
           }else{
             return true;
           }
         }else{
           return this.ruleDefinitionGroup.invalid
         }
         
       }
    }else{
      return true;
    }
   
  }

  public inputValidator(event: any): void {
    const pattern = /[^A-Za-z0-9_@.,/#&+-\s]/g; // Updated pattern to exclude spaces
  
    const currentValue = event.target.value;
    if (pattern.test(currentValue)) {
      event.target.value = currentValue.replace(pattern, "");
    }
  }


}
