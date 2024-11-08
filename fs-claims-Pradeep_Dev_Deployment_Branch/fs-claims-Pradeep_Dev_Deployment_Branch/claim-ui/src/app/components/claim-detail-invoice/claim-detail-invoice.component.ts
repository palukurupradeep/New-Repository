import { HttpResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, OnInit, Output, SimpleChanges, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NavigationExtras, Router } from '@angular/router';
import { select } from '@ngrx/store';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { Observable, catchError, delay, forkJoin, of, tap, throwError } from 'rxjs';
import { ClaimDocuments, ClimDetailsResponce } from 'src/app/interfaces/claim-details.model';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { concatMap, finalize, map } from 'rxjs/operators';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';
import { PrepareCrmComponent } from '../prepare-crm/prepare-crm.component';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
@Component({
  selector: 'app-claim-detail-invoice',
  templateUrl:'./claim-detail-invoice.component.html',
  styleUrls: ['./claim-detail-invoice.component.scss']
})
export class ClaimDetailInvoiceComponent implements OnInit{
  @Output() refreshClaimDetails = new EventEmitter<string>();
  public selectedDocType: any;
    public docTypeData: any = [
    {
      lookupId: 16,
      lookupCode: 'INV',
    },
  ];
  public addInvoiceForm!: FormGroup;
  public futureDateError: boolean = false;
  public previousSearchResults: any[] = [];
  public addInvoiceData: any[] = [];
  public show404ErrorMessage: boolean = false;
  public showRollNumberError: boolean = false;
  public bsConfig: Partial<BsDatepickerConfig>;
  @Input() claimDetails!: ClimDetailsResponce;
  @Input() customerdata: any;
  @Input() claimId:number =0;
  public invoiceNumberList: string[] = [];
  public orderNumberList: string[] = [];
  public invoiceDateList: string[] = [];
  public recentassociateAddInvoiceCheckBoxData: any = [];
  public tempArr: any = [];
  public recentassociateAddInvoiceData: any[] = [];
  public enableDuplicateLineBtn: boolean = false;
  public selectedLineItem: any;
  public claimDocuments: any[] = [];
  public ClaimDocData: any[] = [];
  public orgUserData: any;
  public payloadResponse:any;
  public addpayloadResponse:any;
  public editpayloadResponse:any;
  public selectedLine: any = [];
  public tabSearchText: any = {};
  public enableCreateServiceBtn: boolean = false;
  public selectedClaimLineInfo: any= [];
  public filteredClimLineInfo: any = [];
  public selectedLineInfo: any = [];
  @Input() totalSquareFeet!: number;
  @Input() claimedTotalSquareFeet!: number;
  @Input() claimIsVoid:any = false;
  public totalYards: number = 0;
  public claimedtotalYards: number = 0;
  public totalLineNumberCount: number = 0;
  public labourBtn: boolean=false;
  public labourSucess:any;
  public labourNumber: number = 0;
  public invoiceRowChecked: boolean= true;
  public filteredAllClaimLineDetailsRecords: any[]=[];
  public section1Collapsed: boolean = false;
  public section2Collapsed: boolean = true;
  public addLineForm!: FormGroup;
  public claimList: any;
  public unitOfmeasure:any[]=[]
  public getCategories:any=[]
  public claimLineAddReasonList:any[]=[]
  public payloadObject: any;
  public loadingData: boolean = false;
  public arrowSecTwo: boolean = false;
  public addNewLineButtonText: string = "Add New Line";
  public unmatchedResult: any[]=[];
  public matchedResults: any[]=[];
  public createdObjects: any[]=[];
  public selectedLineInfoPushed: any[] = [];
  public loadLineIemsFlag : boolean = true;
  public newIvoicesList: any[]=[];
  public noNewInvoice: boolean= false;
  public invoiceRetrived: boolean = false;
  public priorClaimTableData :any[]=[];
  public priorClaimloading:boolean = false;
  public recentassociateAddInvoiceDate: any;
  public recentassociateAddInvoiceinvoiceNumber: any;
  public recentassociateAddInvoiceorderNumber: any;
  public recentassociateAddInvoicebolNumber: any;
  public recentassociateAddInvoicemasterBolNbr: any;
  public recentassociateAddInvoicebolNumberpoNumber: any;
  public rgaErrorMessages : any[] =[];
  public selectedLineForAnalyze : any;
  public resolutionsAndPhotosData :any[] = [];
  public showTable:boolean = false;
  public rgaDetails: any[]=[];
  public taxAndFreightData:any[]=[];
  public careSurChargeData: any[]=[];
  public addReasonTypeData: any;
  public linecategoryTypeData: any;
  public claimstatusPriordata : any;
  public serviceTypes: any[]=[];
  public documentsByClaimId: any[]=[];
  public crmDetailsByClaimIdAndCrmNumber: any;
 public selectedServiceTypeId :number= 1;
 public restockFeeData: any = {};
 public showUserName: boolean = true;
 public showInvoice: boolean = false;
 public showCustomer: boolean = false;
 public selectedRga: string = 'No RGA';
 public selectedDebit: string = 'No Debit';
 public associatedRgaNumber: number = 0;
public emailAddress: boolean = false;
public  memoComments: string = '';
public applyMethod: string = 'autoApply';
public autoApply: boolean = true;
public doNotApply: boolean = false;
public manualApply: boolean = false;
public leaveClaimOpen: boolean = true;
public closeClaimWithTasks: boolean = false;
public leaveInspectionOpen: boolean = false;
public removeDispute: boolean = false;
public atkAttempted: boolean = false;
public atkAccepted: boolean = false;
public stkAttempted: boolean = false;
public stkAccepted: boolean = false;
//public claimId: number[] = [];
public claimLevelTotalAmount: number = 0; 
public fetchedDocumnetsByClaimId: any[]=[];
public amountEdited: boolean = false;
isCRMVisible: boolean = false;
formGroup: FormGroup;
public editRestockFeeData: number =0;
public totalLineAmountSum:number = 0;
public restockFeePercentage:number=0;
@ViewChild(PrepareCrmComponent) prepareCrmComponent!: PrepareCrmComponent;
  prepareRGAData: any={};
  rdcLocationCode: string = '';
  filteredClaimList: any[] = [];
  selectedReasonCode: string = '';
  codeSearch: string = '';
  public careSurChargeDesc: string= '';
  public currencyCodeName:string ='';
  public currencyCodeDesignation:string ='';
  public invExchangeRate:number= 1;
  public pricingCurrencyCode: string = '';
  public currencyCode: string = '';
  public currencyName: string = 'UNITED STATES $';
  public maxLineNumber: number= 0;
  totalInvoiceAmt: number=0;
  dummyDocument: boolean= false;
  genricAction: any;
  genricLineDocId: number=0;
  activeDirectoryId:string='';
  unmatchedLines: any[]=[];
  constructor(
    private form: FormBuilder,
    private fb: FormBuilder,
    private intiateClaimService: InitiateClaimService,
    private claimDetailService : ClaimDetailService,
    private snackBar: MatSnackBar,
    private router:Router,
    private smartCodeService:SmartCodeService,
    private toasterService: ToasterService,
    public authService:AuthService,
    public userMappingService: UserMappingService

  ) {
    this.bsConfig = {
      showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
      maxDate: new Date(),
    };
    
    this.formGroup = this.fb.group({
      defectPercent: ['', [Validators.required, Validators.pattern('^[0-9]*$')]]
    });
      }

  ngOnInit(): void {
    // console.log(this.claimedTotalSquareFeet,'claimedTotalSquareFeetngonchanges')
    this.initiateInvoiceForm();
    this.getDocType();
    this.totalYards = Math.abs(this.totalSquareFeet/9);
    this.claimedtotalYards = Math.abs(this.claimedTotalSquareFeet/9);
    this.unitOfMeasures();
    this.getAllReasonCode();
    this.getAllLineCategory();
    this.fetchServiceToKeepType();
    this.addLineForm = this.form.group({
      reasonCode: ['', Validators.required],
      style: ['', Validators.required],
      color: ['', Validators.required],
      roll: ['', Validators.required],
      lineType: [''],
     // lineType: ['', Validators.required],
      grade: ['', Validators.required],
      widthinfeet: ['', Validators.required],
      lengthinfeet: ['', Validators.required],
      widthininch: [''],
      lengthininch: [''],
      dyeLot:['', Validators.required],
      qty: ['', Validators.required],
      uom: ['', Validators.required],
      price: ['', Validators.required],
      amount: ['', Validators.required] ,
      lineAddreason:['', Validators.required],
      claimLineCategory:['', Validators.required]
    });
    const permissionsfetch = this.authService.getPermissions();

   
    console.log('Permissions:', permissionsfetch);
  }

  ngOnChanges(changes: SimpleChanges){
    console.log(this.claimedTotalSquareFeet,'ngoninit claimed totstsqft')
    this.orgUserData = this.claimDetails.claimDocuments;
    if(this.claimDetails.claimDocuments != undefined){
      this.claimDocuments = this.claimDetails.claimDocuments;
      console.log(this.claimDetails.claimDocuments,'this.claimDetails.claimDocuments..')
      console.log(this.claimDetails.claimId,'this.claimDetails.claimId..')
      this.fetchDocumentsByClaimId(this.claimDetails.claimId)
   // Filter claimDocuments where statusId !== 3
// this.ClaimDocData = this.claimDetails.claimDocuments.map(doc => ({
//   ...doc,
//   claimLineDetails: doc.claimLineDetails.filter(line => line.statusId !== 3)
// }));
this.ClaimDocData = this.claimDetails.claimDocuments
  .filter(doc => doc.documentType.documentTypeCode === "INV" && doc.statusId !==3) // Filter documents where documentTypeCode is "INV"
  .map(doc => ({
    ...doc,
    claimLineDetails: doc.claimLineDetails.filter(line => line.statusId !== 3) // Filter claimLineDetails where statusId is not 3
  }));
 

      console.log(this.ClaimDocData,'this.ClaimDocData')
      if (this.ClaimDocData.length === 0) {
        this.maxLineNumber = 0;
    }
      //this.ClaimDocData = this.claimDetails.claimDocuments;
      this.loadClimeLineData();
      this.calculateTotalYards();
      this.calculateClaimedTotalYards();
      this.totalLineNumberCount = this.calculateTotalLineNumberCount(this.claimDocuments);
      console.log(this.maxLineNumber,'maxLineNumber')
    }
    
  }
  calculateTotalYards() {
    // Conversion factor: 1 square yard = 9 square feet
    this.totalYards = this.totalSquareFeet / 9;
  }
  calculateClaimedTotalYards() {
    // Conversion factor: 1 square yard = 9 square feet
    this.claimedtotalYards = this.claimedTotalSquareFeet / 9;
  }
  
  calculateTotalLineNumberCount(claimDocuments: any[]): number {
    let totalCount = 0;

    claimDocuments.forEach(claimDocument => {
                  if (claimDocument.claimLineDetails) {
                    let result = claimDocument.claimLineDetails.filter((item:any )=>item.statusId !=3)

                    totalCount += result.length;
      }
    });
    
    return totalCount;
  }

  // loadClimeLineData(){
  //       this.ClaimDocData.forEach(
  //     document => {
  //       document.claimLineDetails.forEach(
  //         (details: any) => {
  //           details['selected'] = false;
  //         }
  //       )
      
  //     }
  //   );
    
    
  // }
  public toggleSection(section: string): void {
    if (section === 'section1') {
      this.section1Collapsed = !this.section1Collapsed;
      this.section2Collapsed = true;
    } else if (section === 'section2') {
      this.section2Collapsed = !this.section2Collapsed;
      this.section1Collapsed = true;
    }
  }

 public loadClimeLineData() {
    const allClaimLineDetails: any[] = [];
    //const filteredRecords: any[] = [];
  
    this.ClaimDocData.forEach(document => {
    
     
      const currentMaxLineNumber = document.claimLineDetails.reduce((max:any, detail:any) => {
        return detail.lineNumber > max ? detail.lineNumber : max;
      }, 0); // Start with 0
  
      // Update the global maxLineNumber if the currentMaxLineNumber is greater
      if (currentMaxLineNumber > this.maxLineNumber) {
        this.maxLineNumber = currentMaxLineNumber;
      }
      console.log(this.maxLineNumber,'maxLineNumber1')

      this.currencyCode = document.claimLineDetails[0].currencyCodeDesignation;
      this.currencyName = document.claimLineDetails[0].currencyCodeName;
     
 
      // Store claimLineDetails of every document
      allClaimLineDetails.push(...document.claimLineDetails);
  
      // Assign documentNumber to each claimLineDetails object
      document.claimLineDetails.forEach((details: any) => {
        details['selected'] = false;
        details['documentNumber'] = document.documentNumber; // Assign documentNumber
      });
    })
  
    // Filter records with statusId equal to 1
    this.filteredAllClaimLineDetailsRecords.push(...allClaimLineDetails.filter((record: { statusId: number }) => record.statusId === 1));
  
   
  }
  
 
  public refreshInvoiceData(data:any) {
        this.claimDetailService.getClaim(this.claimDetails.claimNumber,Number(localStorage.getItem('userId'))).subscribe(data=>{
      // console.log("Updated Data "+data);
      this.ClaimDocData = data.claimDocuments;
            this.loadClimeLineData();
    })
    this.closeinvoiceEditModel();
    this.refreshClaimDetails.emit();
    // console.log(data)
    this.selectedLine = [];
  }

  public openinvoiceEditModel(){
    const bodyTag = document.body;
    bodyTag.classList.add('edit-modal-activated');
  // console.log('modalfor edit modal')
  const modelDiv = document.getElementById('invoiceEditModel');
  // console.log('modelDiv',modelDiv)
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
  }

} 
public closeinvoiceEditModel(){
  const bodyTag = document.body;
  bodyTag.classList.remove('edit-modal-activated');
    this.refreshClaimDetails.emit();
  this.selectedLine = [];
  const modelDiv = document.getElementById('invoiceEditModel');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }

}

public refreshLabourModelData(){
    // const modelDiv = document.getElementById('invoiceLaboueModel');
    // if (modelDiv != null) {
    //   modelDiv.style.display = 'none';
    // }
    this.closeinvoiceLaboueModel();
    this.refreshClaimDetails.emit();
    this.selectedLine = [];
    this.labourNumber = this.labourSucess.claimLineId;
  }

  public openinvoiceLaboueModel(){
    const modelDiv = document.getElementById('invoiceLaboueModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

 public closeinvoiceLaboueModel(){
    const modelDiv = document.getElementById('invoiceLaboueModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  
  public labourColrChange(){
    if(this.labourSucess != undefined){
      return this.labourSucess.claimLineId
    }
   else{
    return 0;
   }
  }
  
  public toggleRowSelection(selectedUser: any, document: any, event: any) {
    if(!this.claimIsVoid){
            if(event.target.checked == true){
      this.labourBtn = selectedUser.detailType.detailTypeId == 1 && this.claimDetails.claimStatusId ==1 ? true: false;
      this.enableDuplicateLineBtn = true;
      if(selectedUser.detailType.detailTypeCode === "MERCH" && this.claimDetails.claimStatusId ==1) {
        this.enableCreateServiceBtn = true;
      }
      this.labourSucess=selectedUser
      this.selectedLineItem = selectedUser;
      let currentMaxLineNumber = this.maxLineNumber;
     
      this.editpayloadResponse = {
        "createdByUserId": document.createdByUserId,
        "modifiedByUserId": document.modifiedByUserId,
        "claimDocumentId": document.claimDocumentId,
        "documentNumber":  document.documentNumber,
        "exchangeRate": document.exchangeRate||'',
        "orderNumber": document.orderNumber,
        "purchaseOrderNumber": document.purchaseOrderNumber,
        "billofLading": document.billofLading,
        "masterBillofLading": document.masterBillofLading,
        "documentTypeCode": document.documentType.documentTypeCode,
        "claimReasonCode": document.claimReasonDefinition.claimReasonCode,
        "clmId":this.claimId,
        "claimLineDetailDTOS": [
            {
                "createdByUserId": this.selectedLineItem.createdByUserId,
                "modifiedByUserId": this.selectedLineItem.modifiedByUserId,
                "unitOfMeasureCode": this.selectedLineItem.unitOfMeasure.unitOfMeasureCode,
                "rcsCode": this.selectedLineItem.rcsCodes.rcsCode,
                "lineNumber": this.selectedLineItem.lineNumber,
                "rollNumber": this.selectedLineItem.rollNumber,
                "styleNumber": this.selectedLineItem.styleNumber,
                "colorNumber": this.selectedLineItem.colorNumber,
                "claimReasonCode": this.selectedLineItem.claimReasonDefinition.claimReasonCode,
                "grade": this.selectedLineItem.grade,
                "lineAmountUsd": this.selectedLineItem.lineAmountUsd,
                "lineAmountForeign": this.selectedLineItem.lineAmountForeign,
                "unitPriceUsd": this.selectedLineItem.unitPriceUsd,
                "unitPriceForeign": this.selectedLineItem.unitPriceForeign,
                "pricingCurrencyCode": this.selectedLineItem.pricingCurrencyCode,
                "currencyCodeDesignation": this.selectedLineItem.currencyCodeDesignation,
                "currencyCodeName": this.selectedLineItem.currencyCodeName,
                "exchangeRate": this.selectedLineItem.exchangeRate||'',
                "dyeLot": this.selectedLineItem.dyeLot,
                "quantity": this.selectedLineItem.quantity,
                "sellingCompany": this.selectedLineItem.sellingCompany,
                "productCode": this.selectedLineItem.productCode,
                "inventoryStyle": this.selectedLineItem.inventoryStyle,
                "inventoryColor": this.selectedLineItem.inventoryColor,
                "manufacturingPlant": this.selectedLineItem.manufacturingPlant,
                "detailTypeCode": this.selectedLineItem.detailType.detailTypeCode,
                "lineSourceCode": this.selectedLineItem.lineSource.lineSourceCode,
                "lineSourceReference": this.selectedLineItem.lineSourceReference,
                "lineAddReasonCode": this.selectedLineItem.lineAddReasonId,
                "vendorId": "",
                "claimLineId": this.selectedLineItem.claimLineId,
                ...(this.selectedLineItem.claimDetailRecords?.length > 0 && {
                    "claimDetailRecordDTOS": [
                        {
                            "createdByUserId": this.selectedLineItem.claimDetailRecords[0].createdByUserId !== undefined ? this.selectedLineItem.claimDetailRecords[0].createdByUserId : '',
                            "modifiedByUserId": this.selectedLineItem.claimDetailRecords[0].modifiedByUserId,
                            "claimDetailRecordId": this.selectedLineItem.claimDetailRecords[0].claimDetailRecordId,
                            "detailRecordTypeCode": this.selectedLineItem.claimDetailRecords[0].detailRecordType.detailRecordTypeCode,
                            "widthInFeet": this.selectedLineItem.claimDetailRecords[0].widthInFeet,
                            "widthInInches": this.selectedLineItem.claimDetailRecords[0].widthInInches,
                            "lengthInFeet": this.selectedLineItem.claimDetailRecords[0].lengthInFeet,
                            "lengthInInches": this.selectedLineItem.claimDetailRecords[0].lengthInInches,
                            "quantity": this.selectedLineItem.claimDetailRecords[0].quantity,
                            "unitOfMeasure": this.selectedLineItem.claimDetailRecords[0].unitOfMeasure,
                            "amountUsd": this.selectedLineItem.claimDetailRecords[0].amountUsd,
                            "amountForeign": this.selectedLineItem.claimDetailRecords[0].amountForeign,
                            "unitPriceUsd": this.selectedLineItem.claimDetailRecords[0].unitPriceUsd,
                            "unitPriceForeign": this.selectedLineItem.claimDetailRecords[0].unitPriceForeign,
                            "exchangeRate": this.selectedLineItem.claimDetailRecords[0].exchangeRate||'',
                        }
                    ]
                })
            }
        ]
    };
    //payload for duplicate api
      this.payloadResponse = {
    //    "createdByUserId": document.createdByUserId,
     //   "modifiedByUserId": document.modifiedByUserId,
        "claimDocumentId": document.claimDocumentId,
        "documentNumber":  document.documentNumber,
        "exchangeRate": document.exchangeRate||'',
        "orderNumber": document.orderNumber,
        "purchaseOrderNumber": document.purchaseOrderNumber,
        "billofLading": document.billofLading,
        "masterBillofLading": document.masterBillofLading,
        "documentTypeCode": document.documentType.documentTypeCode,
        "claimReasonCode": document.claimReasonDefinition.claimReasonCode,
        "claimLineDetailDTOS": [
            {
             //   "createdByUserId": this.selectedLineItem.createdByUserId,
            //    "modifiedByUserId": this.selectedLineItem.modifiedByUserId,
                "unitOfMeasureCode": this.selectedLineItem.unitOfMeasure.unitOfMeasureCode,
                "rcsCode": this.selectedLineItem.rcsCodes.rcsCode,
                "invoiceLineNumber":this.selectedLineItem.lineNumber,
                "lineNumber": currentMaxLineNumber + 1,
                "rollNumber": this.selectedLineItem.rollNumber,
                "styleNumber": this.selectedLineItem.styleNumber,
                "colorNumber": this.selectedLineItem.colorNumber,
                "claimReasonCode": this.selectedLineItem.claimReasonDefinition.claimReasonCode,
                "grade": this.selectedLineItem.grade,
                "lineAmountUsd": this.selectedLineItem.lineAmountUsd,
                "lineAmountForeign": this.selectedLineItem.lineAmountForeign,
                "unitPriceUsd": this.selectedLineItem.unitPriceUsd,
                "unitPriceForeign": this.selectedLineItem.unitPriceForeign,
                "pricingCurrencyCode": this.selectedLineItem.pricingCurrencyCode,
                "currencyCodeDesignation": this.selectedLineItem.currencyCodeDesignation,
                "currencyCodeName": this.selectedLineItem.currencyCodeName,
                "exchangeRate": this.selectedLineItem.exchangeRate||'',
                "dyeLot": this.selectedLineItem.dyeLot,
                "quantity": this.selectedLineItem.quantity,
                "sellingCompany": this.selectedLineItem.sellingCompany,
                "productCode": this.selectedLineItem.productCode,
                "inventoryStyle": this.selectedLineItem.inventoryStyle,
                "inventoryColor": this.selectedLineItem.inventoryColor,
                "manufacturingPlant": this.selectedLineItem.manufacturingPlant,
                "detailTypeCode": this.selectedLineItem.detailType.detailTypeCode,
                "lineSourceCode": this.selectedLineItem.lineSource.lineSourceCode,
                "lineSourceReference": this.selectedLineItem.lineSourceReference,
                "lineAddReasonCode": this.selectedLineItem.lineAddReasonId,
                "vendorId": "",
                ...(this.selectedLineItem.claimDetailRecords?.length > 0 && {
                    "claimDetailRecordDTOS": [
                        {
                            //"createdByUserId": this.selectedLineItem.claimDetailRecords[0].createdByUserId !== undefined ? this.selectedLineItem.claimDetailRecords[0].createdByUserId : '',
                           // "modifiedByUserId": this.selectedLineItem.claimDetailRecords[0].modifiedByUserId,
                           // "claimDetailRecordId": this.selectedLineItem.claimDetailRecords[0].claimDetailRecordId,
                            "detailRecordTypeCode":'INV',// this.selectedLineItem.claimDetailRecords[0].detailRecordType.detailRecordTypeCode,
                            "widthInFeet": this.selectedLineItem.claimDetailRecords[0].widthInFeet,
                            "widthInInches": this.selectedLineItem.claimDetailRecords[0].widthInInches,
                            "lengthInFeet": this.selectedLineItem.claimDetailRecords[0].lengthInFeet,
                            "lengthInInches": this.selectedLineItem.claimDetailRecords[0].lengthInInches,
                            "quantity": this.selectedLineItem.claimDetailRecords[0].quantity,
                            "unitOfMeasure": this.selectedLineItem.claimDetailRecords[0].unitOfMeasure,
                            "amountUsd": this.selectedLineItem.claimDetailRecords[0].amountUsd,
                            "amountForeign": this.selectedLineItem.claimDetailRecords[0].amountForeign,
                            "unitPriceUsd": this.selectedLineItem.claimDetailRecords[0].unitPriceUsd,
                            "unitPriceForeign": this.selectedLineItem.claimDetailRecords[0].unitPriceForeign,
                            "exchangeRate": this.selectedLineItem.claimDetailRecords[0].exchangeRate||'',
                        },  {
                          //"createdByUserId": this.selectedLineItem.claimDetailRecords[0].createdByUserId !== undefined ? this.selectedLineItem.claimDetailRecords[0].createdByUserId : '',
                         // "modifiedByUserId": this.selectedLineItem.claimDetailRecords[0].modifiedByUserId,
                         // "claimDetailRecordId": this.selectedLineItem.claimDetailRecords[0].claimDetailRecordId,
                          "detailRecordTypeCode":'CLM',// this.selectedLineItem.claimDetailRecords[0].detailRecordType.detailRecordTypeCode,
                          "widthInFeet": this.selectedLineItem.claimDetailRecords[0].widthInFeet,
                          "widthInInches": this.selectedLineItem.claimDetailRecords[0].widthInInches,
                          "lengthInFeet": this.selectedLineItem.claimDetailRecords[0].lengthInFeet,
                          "lengthInInches": this.selectedLineItem.claimDetailRecords[0].lengthInInches,
                          "quantity": this.selectedLineItem.claimDetailRecords[0].quantity,
                          "unitOfMeasure": this.selectedLineItem.claimDetailRecords[0].unitOfMeasure,
                          "amountUsd": this.selectedLineItem.claimDetailRecords[0].amountUsd,
                          "amountForeign": this.selectedLineItem.claimDetailRecords[0].amountForeign,
                          "unitPriceUsd": this.selectedLineItem.claimDetailRecords[0].unitPriceUsd,
                          "unitPriceForeign": this.selectedLineItem.claimDetailRecords[0].unitPriceForeign,
                          "exchangeRate": this.selectedLineItem.claimDetailRecords[0].exchangeRate||'',
                      }
                    ]
                })
            }
        ]
    };
     selectedUser.selected = !selectedUser.selected;
      this.ClaimDocData.forEach(
        document => {
                document.claimLineDetails.forEach(
            (details: any) => {
              // if (details !== selectedUser) {
              //   details.selected = false;
              // }
              if (details == selectedUser) {
                // this.selectedLine = [];
                let obj = {
                  claimDocumentId: document.claimDocumentId,
                  documentNumber: document.documentNumber,
                  exchangeRate:document.exchangeRate,
                  orderNumber: document.orderNumber,
                  purchaseOrderNumber:document.purchaseOrderNumber,
                  billofLading:document.billofLading,
                  masterBillofLading:document.masterBillofLading,
                  claimNumber: this.claimDetails.claimNumber,
                  createdByUserId:this.claimDetails.createdByUserId,
                  modifiedByUserId:this.claimDetails.modifiedByUserId,
                  documentTypeCode:document.documentType.documentTypeCode,
                  claimReasonCode:document.claimReasonDefinition.claimReasonCode,
                  claimLineId:details.claimLineId,
                  lineNumber:details.lineNumber,
                  documentDate:document.documentDate,
                  // claimReasonCode:details.claimReasonDefinition.claimReasonCode,
                  selectedLine: selectedUser
                }
                this.selectedLine.push(obj);
              }
            }
          )
        }
      );
      this.selectedLineForAnalyze = selectedUser;
    } else {
      // this.selectedLine = [];
      const index = this.selectedLine.findIndex((i: any) => i.claimLineId == selectedUser.claimLineId);
      this.selectedLine.splice(index, 1);
    }
  }
    }
 
 public fieldsChange(values:any, details: any):void {
    if(values.currentTarget.checked){
      this.selectedLine.push(details);
    } else {
      const index = this.selectedLine.findIndex((i: any) => i.claimLineId == details.claimLineId);
      this.selectedLine.splice(index, 1);
    }
  }

  public checkDocBtnDisable(permissionCode:string):boolean {

  if(this.authService.hasPermission(permissionCode) && this.selectedLine.length > 0){

   
    return false
  } else {
return true
  }
  
}

  public onTabSearch() {
    if (!this.tabSearchText.document && !this.tabSearchText.date && !this.tabSearchText.amount
      && !this.tabSearchText.order && !this.tabSearchText.po && !this.tabSearchText.bol && 
      !this.tabSearchText.status && !this.tabSearchText.amount) {
        this.claimDetails.claimDocuments = this.orgUserData;
    } else {
      if (
        this.tabSearchText.document != undefined &&
        this.tabSearchText.document != ''
      ) {
        this.claimDocuments = this.orgUserData.filter((user: any) => {
          if (user.documentNumber != null) {
            return user.documentNumber
              .toLowerCase()
              .includes(this.tabSearchText.document.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.date != undefined &&
        this.tabSearchText.date != ''
      ) {
        this.claimDocuments = this.orgUserData.filter((user: any) => {
          if (user.createdDateTime != null) {
            return user.createdDateTime
              .toLowerCase()
              .includes(this.tabSearchText.date.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.amount != undefined &&
        this.tabSearchText.amount != ''
      ) {
        this.claimDocuments = this.orgUserData.filter((user: any) => {
          if (user.amountUsd != null) {
            return user.amountUsd
            .toString()
              .includes(parseFloat(this.tabSearchText.amount));
          }
        });
      }
      if (
        this.tabSearchText.order != undefined &&
        this.tabSearchText.order != ''
      ) {
        this.claimDocuments = this.orgUserData.filter((user: any) => {
          if (user.orderNumber != null) {
            return user.orderNumber
              .toLowerCase()
              .includes(this.tabSearchText.order.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.po != undefined &&
        this.tabSearchText.po != ''
      ) {
        this.claimDocuments = this.orgUserData.filter((user: any) => {
          if (user.purchaseOrderNumber != null) {
            return user.purchaseOrderNumber
              .toLowerCase()
              .includes(this.tabSearchText.po.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.bol != undefined &&
        this.tabSearchText.bol != ''
      ) {
        this.claimDocuments = this.orgUserData.filter((user: any) => {
          if (user.billofLading != null) {
            return user.billofLading
              .toLowerCase()
              .includes(this.tabSearchText.bol.toLowerCase());
          }
        });
      }
      
    }
  }

  

  public initiateInvoiceForm(){
    this.addInvoiceForm = this.form.group({
      customer: [this.claimDetails.customerNumber],
      docType: [''],
      docNumber: ['', Validators.pattern('^-?[0-9]\\d*(\\.\\d{1,2})?$')],
      docDate: [''],
      rollNumber: ['']
    });
  }
  

  public getDocType() {
    const claim_init_docType = 'CLM_INIT_DOC_TYPE';

    this.intiateClaimService
      .getAssociateGlobalAccount(claim_init_docType)
      .subscribe(
        (data) => {
          this.docTypeData = data;
          console.log(this.docTypeData, 'this.docTypeData');

          // Create an array of observables for each lookupCode
          const observables = this.docTypeData.map((docType: any) => {
            return this.intiateClaimService.getDocIdByDocumentTypeCode(docType.lookupCode).pipe(
              map((response) => {
                return {
                  ...docType,
                  docTypeId: response.body // Assuming the response body contains docTypeId
                };
              })
            );
          });

          // Use forkJoin to handle all observables
          forkJoin(observables).subscribe(
            (updatedDocTypes) => {
              this.docTypeData = updatedDocTypes;
              console.log(this.docTypeData, 'updatedDocTypeData');

              // Find the default docTypeId for 'INV'
              const invDocType = this.docTypeData.find((item:any) => item.lookupCode === 'INV');
              if (invDocType) {
                this.selectedDocType = invDocType.docTypeId;
              }

              // Set the form control value
              this.addInvoiceForm.patchValue({
                docType: this.selectedDocType
              });
            },
            (error) => {
              this.docTypeData = [];
              console.error('Error in forkJoin:', error);
            }
          );
        },
        (error) => {
          this.docTypeData = [];
          console.error('Error in getAssociateGlobalAccount:', error);
        }
      );
  }


  onInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const inputDate = new Date(inputElement.value);
  
    // Check if the input date is in the future
    if (inputDate > new Date()) {
      // Clear the input value for future dates
      inputElement.value = '';
  
      // Set the futureDateError flag to display the error message
      this.futureDateError = true;
    } else {
      // Reset the error message and flag
      this.futureDateError = false;
    }
  }

  public  isRetrieveButtonEnabled(): boolean {
    const customerNumber = this.claimDetails.customerNumber;
    const docType = this.addInvoiceForm.value.docType;
    const docNumber = this.addInvoiceForm.value.docNumber;
    const rollNumber = this.addInvoiceForm.value.rollNumber; 
  
    // Check the conditions
    const isCustomerNumberValid = customerNumber && customerNumber.length === 7;
    const isDocTypeValid = docType && (docNumber || rollNumber);
  
    return isCustomerNumberValid && isDocTypeValid;
  }
  openModalAddInvoice() {
    this.invoiceRowChecked = true;
    this.show404ErrorMessage = false;
    this.showRollNumberError = false;
    this.noNewInvoice = false;
    this.loadingData = true;  // Set loading to true before data processing
  
    const addInvoiceObj = {
      customerNumber: '',
      invoiceNumber: '',
      bolNumber: '',
      poNumber: '',
      orderNumber: '',
      docDate: '',
      docType: '',
      rollNumber: '',
    };
  
    addInvoiceObj.customerNumber = this.claimDetails.customerNumber;
    addInvoiceObj.docDate = this.addInvoiceForm.value.docDate;
    addInvoiceObj.rollNumber = this.addInvoiceForm.value.rollNumber;
    addInvoiceObj.docType = this.addInvoiceForm.value.docType;
  
    if (this.addInvoiceForm.value.docType == '11') {
      addInvoiceObj.invoiceNumber = this.addInvoiceForm.value.docNumber;
    } else if (this.addInvoiceForm.value.docType == '5') {
      addInvoiceObj.bolNumber = this.addInvoiceForm.value.docNumber;
    } else if (this.addInvoiceForm.value.docType == '14') {
      addInvoiceObj.orderNumber = this.addInvoiceForm.value.docNumber;
    } else {
      addInvoiceObj.poNumber = this.addInvoiceForm.value.docNumber;
    }
  
    if (
      this.previousSearchResults.length === 0 ||
      this.previousSearchResults[0].customerNumber !== addInvoiceObj.customerNumber
    ) {
      this.previousSearchResults = []; // Reset if customerNumber changed
    }
    const modelDiv = document.getElementById('addinvoices');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
        }
    const storedValues = this.addInvoiceData.filter(
      (item) => item.customerNumber === addInvoiceObj.customerNumber
    );
  
    if (
      storedValues.length === 0 ||
      storedValues[0].docType !== addInvoiceObj.docType
    ) {
      storedValues.splice(0, storedValues.length);
  
      this.previousSearchResults.push({
        customerNumber: addInvoiceObj.customerNumber,
        documentTypeId: addInvoiceObj.docType,
        documentNumber:
          addInvoiceObj.invoiceNumber ||
          addInvoiceObj.bolNumber ||
          addInvoiceObj.orderNumber ||
          addInvoiceObj.poNumber,
      });
    }
  
    this.intiateClaimService.getSearchInvoice(addInvoiceObj).pipe(
      map(searchInvoiceResponse => searchInvoiceResponse.body),
      concatMap((searchInvoiceResponse: any[]) => {
        this.addInvoiceData = [];
  
        const observables = searchInvoiceResponse.map(item => {
          const invoiceNumber = item.invoiceNbr;
          const invoiceDate = item.invoiceDate;
          const orderNumber = item.orderNumber;
          const orderDate = item.orderDate;
          const bolNumber = item.bolNumber;
          const poNumber = item.ponbr;
          const CustomerNumber = item.customerNumber;
  
          return this.intiateClaimService.getAddInvioce({ invoiceNumber, invoiceDate }).pipe(
            map(response => {
              return response.body.map((invoiceItem: any) => ({
                ...invoiceItem,
                orderNumber,
                orderDate,
                invoiceDate,
                bolNumber,
                poNumber,
                CustomerNumber,
               // lineNumber: invoiceItem.lineNbr,
               // invoiceNumber: invoiceItem.invoiceNbr,
              //  invoiceDate: invoiceDate, // Ensure invoiceDate is added here
                // Remove old properties
             //   lineNbr: undefined,
             //   invoiceNbr: undefined
              }));
            }),
            catchError(error => {
              console.error('Error fetching add invoice data:', error);
              return of(null);
            })
          );
        });
  
        return forkJoin(observables);
      }),
      finalize(() => {
        this.loadingData = false; 
        if (this.show404ErrorMessage || this.showRollNumberError || this.noNewInvoice) {
          return;
        }
  
      
      })
    ).subscribe(
      addInvoiceResponses => {
        addInvoiceResponses.forEach(response => {
          if (response) {
            this.addInvoiceData.push(...response);
          }
        });
  
        this.addInvoiceData = this.addInvoiceData.map(item => ({
          ...item,
          lineNumber: item.lineNbr,
          invoiceNumber: item.invoiceNbr,
          lineNbr: undefined,
          invoiceNbr: undefined
        }));
  
        console.log('Final Add Invoice Data final:', this.addInvoiceData);
  
        this.invoiceNumberList = [];
        this.orderNumberList = [];
        this.invoiceDateList = [];
        this.newIvoicesList = [];
  
        this.addInvoiceData.forEach(item => {
          if (!this.invoiceNumberList.includes(item.invoiceNumber)) {
            this.invoiceNumberList.push(item.invoiceNumber);
          }
  
          if (!this.orderNumberList.includes(item.orderNumber)) {
            this.orderNumberList.push(item.orderNumber);
          }
  
          if (!this.invoiceDateList.includes(item.invoiceDate)) {
            this.invoiceDateList.push(item.invoiceDate);
          }
        });
  
        // if (!this.showCustomerNotFoundError) {
        //   const modelDiv = document.getElementById('addinvoice');
        //   if (modelDiv) {
        //     modelDiv.style.display = 'block';
        //   }
        // }
        
        if (this.claimDetails.claimDocuments.length > 0) {
          this.invoiceNumberList.map((item) => {
            const found = this.claimDetails.claimDocuments.some(
              (el) => el.documentNumber === item && el.statusId !== 3
            );
    
            console.log(found, 'found');
    
            if (!found) {
              this.newIvoicesList.push(item);
            }
          });
    
          console.log(this.newIvoicesList, 'this.newInvoicesList');
    
          if (this.newIvoicesList.length > 0) {
            this.noNewInvoice = false;
    
            this.closeAddInvoiceModal();
    
            this.resetForm();
          } else {
            this.noNewInvoice = true;
          }
        } else {
          this.newIvoicesList = this.invoiceNumberList;
        }
    
        this.tempArr = this.recentassociateAddInvoiceCheckBoxData.slice();
  
       // this.loading = false;
  
        const processedInvoices = new Set<string>();
  
        this.addInvoiceData.forEach(item => {
          if (!processedInvoices.has(item.invoiceNumber)) {
            processedInvoices.add(item.invoiceNumber);
  
            this.taxAndFreight(item.invoiceNumber, item.invoiceDate).subscribe(taxAndFreightData => {
              if (taxAndFreightData) {
                this.currencyCodeName =  taxAndFreightData[0].currencyCodeName ? taxAndFreightData[0].currencyCodeName : "UNITED STATES $";
this.currencyCodeDesignation= taxAndFreightData[0].currencyCodeDesignation ? taxAndFreightData[0].currencyCodeDesignation : "USD";
this.pricingCurrencyCode =  taxAndFreightData[0].pricingCurrencyCode ? taxAndFreightData[0].pricingCurrencyCode : "0000";
//                 this.currencyCodeName = taxAndFreightData[0].currencyCodeName;
// this.currencyCodeDesignation= taxAndFreightData[0].currencyCodeDesignation;
// this.pricingCurrencyCode = taxAndFreightData[0].pricingCurrencyCode;
this.invExchangeRate =taxAndFreightData[0].exchangeRate;
this.totalInvoiceAmt =taxAndFreightData[0].totalInvoiceAmt;
                const { totalFreightAmt, totalTaxAmt, totalInvoiceAmt } = taxAndFreightData[0];
  
                const freightItem = {
                  lineNumber: '040',
                  invoiceNumber: item.invoiceNumber,
                  invoiceNbr: item.invoiceNumber,
                  documentNumber: item.invoiceNumber,
                  CustomerNumber:item.CustomerNumber,
                  bolNumber:item.bolNumber,
                  masterBolNbr:item.masterBolNbr,
                  orderNumber:item.orderNumber,
                  orderDate:item.orderDate,
                  invoiceDate:item.invoiceDate,
                  rollNumber: '       ',
                  styleNumber: '',
                  colorNumber: '',
                  widthInFeet: '',
                  lengthInFeet: '',
                  grade: '',
                  unitPriceUsd: '0.0',
                  lineAmountUsd: totalFreightAmt || 0,
                  dyeLot: '            ',
                  quantity: '0',
                  unitOfMeasureCode: '',
                  rcsCode: '',
                  claimReasonCode: '',
                  lineAmountForeign: 0,
                  unitPriceForeign: 0,
                  pricingCurrencyCode: this.pricingCurrencyCode,
                  currencyCodeDesignation: this.currencyCodeDesignation,
                  currencyCodeName: this.currencyCodeName,
                 // exchangeRates: this.invExchangeRate,
                  exchangeRate: this.invExchangeRate,
                  sellingCompany: '',
                  productCode: '',
                  inventoryStyle: '',
                  inventoryColor: '',
                  manufacturingPlant: '',
                  detailTypeCode: 'FRGHT',
                  lineSourceCode: '',
                  lineSourceReference: '',
                  lineAddReasonCode: '',
                  vendorId: '',
                };
             // Create tax item
                const taxItem = {
                  lineNumber: '050',
                  invoiceNumber: item.invoiceNumber,
                  invoiceNbr: item.invoiceNumber,
                  documentNumber: item.invoiceNumber,
                  CustomerNumber:item.CustomerNumber,
                  bolNumber:item.bolNumber,
                  masterBolNbr:item.masterBolNbr,
                  orderNumber:item.orderNumber,
                  orderDate:item.orderDate,
                  invoiceDate:item.invoiceDate,
                  rollNumber: '       ',
                  styleNumber: '',
                  colorNumber: '',
                  widthInFeet: '',
                  lengthInFeet: '',
                  grade: '',
                  unitPriceUsd: '0.0',
                  lineAmountUsd: totalTaxAmt || 0,
                  dyeLot: '            ',
                  quantity: '0',
                  unitOfMeasureCode: '',
                  rcsCode: '',
                  claimReasonCode: '',
                  lineAmountForeign: 0,
                  unitPriceForeign: 0,
                  pricingCurrencyCode: this.pricingCurrencyCode,
                  currencyCodeDesignation: this.currencyCodeDesignation,
                  currencyCodeName: this.currencyCodeName,
                  //exchangeRates: item.exchangeRate,
                  exchangeRate: this.invExchangeRate,
                  sellingCompany: '',
                  productCode: '',
                  inventoryStyle: '',
                  inventoryColor: '',
                  manufacturingPlant: '',
                  detailTypeCode: 'TAX',
                  lineSourceCode: '',
                  lineSourceReference: '',
                  lineAddReasonCode: '',
                  vendorId: '',
                };
                   // Push items to addInvoiceData
                this.addInvoiceData.push(freightItem);
                this.addInvoiceData.push(taxItem);
                   // Now handle careSurCharge
            this.careSurCharge(item.invoiceNumber, item.invoiceDate).subscribe(careSurChargeData => {
              if (careSurChargeData) {
                // this.careSurChargeDesc = careSurChargeData[0].description.trim();                
                // const { totalCharge } = careSurChargeData[0];
                  // Initialize variables to accumulate charges
                  let totalChargeSum = 0;
                  let chargeDescription = '';
                 
               
                  careSurChargeData.forEach((charge:any) => {
                      totalChargeSum += charge.totalCharge; // Sum the total charges
                      chargeDescription = charge.description.trim(); // Use the description from the last entry
                  });
      
                  console.log('Total Charge Sum:', totalChargeSum);
  
                const chargeItem = {
                  lineNumber: '060',
                  invoiceNumber: item.invoiceNumber,
                  invoiceNbr: item.invoiceNumber,
                  documentNumber: item.invoiceNumber,
                  CustomerNumber:item.CustomerNumber,
                  bolNumber:item.bolNumber,
                  masterBolNbr:item.masterBolNbr,
                  orderNumber:item.orderNumber,
                  orderDate:item.orderDate,
                  invoiceDate:item.invoiceDate,
                  rollNumber: '       ',
                  styleNumber: '',
                  colorNumber: '',
                  widthInFeet: '',
                  lengthInFeet: '',
                  grade: '',
                  unitPriceUsd: '0.0',
                  lineAmountUsd: totalChargeSum || 0,
                  dyeLot: '            ',
                  quantity: '0',
                  unitOfMeasureCode: '',
                  rcsCode: '',
                  claimReasonCode: '',
                  lineAmountForeign: 0,
                  unitPriceForeign: 0,
                  pricingCurrencyCode: this.pricingCurrencyCode,
                  currencyCodeDesignation: this.currencyCodeDesignation,
                  currencyCodeName: this.currencyCodeName,
                  exchangeRate: this.invExchangeRate,
                  sellingCompany: '',
                  productCode: '',
                  inventoryStyle: '',
                  inventoryColor: '',
                  manufacturingPlant: '',
                  detailTypeCode: chargeDescription,
                  lineSourceCode: '',
                  lineSourceReference: '',
                  lineAddReasonCode: '',
                  vendorId: '',
                };
  
                this.addInvoiceData.push(chargeItem);
                // Finally, create and push the total item
                const totalInvoiceAmtData = {
                  

                  lineNumber: '009',
                  totalInvoiceAmt:totalInvoiceAmt,
                  invoiceNumber:item.invoiceNumber,
                  invoiceNbr: item.invoiceNumber,
                  documentNumber: item.invoiceNumber,
                  CustomerNumber:item.CustomerNumber,
                  rollNumber: '       ',
                  styleNumber: '',
                  colorNumber: '',
                  widthInFeet: '',
                  lengthInFeet: '',
                  grade: '',
                  unitPriceUsd: '0.0',
                  netAmount:totalInvoiceAmt || 0,
                  lineAmountUsd: totalInvoiceAmt || 0,
                  dyeLot: '            ',
                  quantity: '0',
                  unitOfMeasureCode: '',
                  rcsCode: '',
                  claimReasonCode: '',
                  lineAmountForeign: 0,
                  unitPriceForeign: 0,
                  pricingCurrencyCode: this.pricingCurrencyCode,
                  currencyCodeDesignation: this.currencyCodeDesignation,
                  currencyCodeName: this.currencyCodeName,
                 // exchangeRates:item.invExchangeRate,
                  exchangeRate:this.invExchangeRate,
                  sellingCompany: '',
                  productCode: '',
                  inventoryStyle: '',
                  inventoryColor: '',
                  manufacturingPlant: '',
                  detailTypeCode: 'TOTAL',
                  lineSourceCode: '',
                  lineSourceReference: '',
                  lineAddReasonCode: '',
                  vendorId: '',
                              };
                              // Push total item last
                        this.addInvoiceData.push(totalInvoiceAmtData);
              }
            });

  
  
               
              }
            });
          }
        });
  
        console.log('Updated Add Invoice Data with Tax, Freight, and Charge:', this.addInvoiceData);
      },
      error => {
        if (error.status === 404 && this.addInvoiceForm.value.docNumber) {
          this.show404ErrorMessage = true;
        } else if (error.status === 404 && this.addInvoiceForm.value.rollNumber) {
          this.showRollNumberError = true;
        } else {
          this.addInvoiceData = [];
        }
        this.loadingData = false;  // Set loading to false in case of an error
      }
    );
  }
  
  

  closeModel(){
    this.initiateInvoiceForm();
    this.docTypeData = { lookupId: 16, lookupCode: 'INV' };
   // this.CloseModalAddInvoice('addinvoices');
   
  }

  public CloseModalAddInvoice(id: string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public getUniqueOrderNumbersByInvoiceNumber(invoiceNumber: string): string[] {
    const uniqueOrderNumbers: any[] = [];

    // Filter the addInvoiceData to get unique order numbers for the current invoice number
    this.addInvoiceData.forEach((item) => {
      if (
        item.invoiceNumber === invoiceNumber &&
        !uniqueOrderNumbers.includes(item.orderNumber)
      ) {
        uniqueOrderNumbers.push(item.orderNumber);
      }
    });

    return uniqueOrderNumbers;
  }

  public toggleSelectAll(isChecked: boolean , invoiceNumber:any): void {
    const invoiceData = this.getInvoiceDataByInvoiceNumber(invoiceNumber);
    invoiceData.forEach(claim => {
      if(isChecked){
        if(!claim.checked){
          this.setAddInvoiceData(claim, isChecked);
        }
      }else{
        this.setAddInvoiceData(claim, isChecked);
      }
     
      
    });
    
  }
  
  public isAllSelected(invoiceNumber:any,isChecked:boolean) {
    const invoiceData = this.getInvoiceDataByInvoiceNumber(invoiceNumber).filter((ele:any)=>ele.detailTypeCode != 'TOTAL');
    let value =  invoiceData.every(claim => claim.checked == true);
    return invoiceData.length > 0 && value
  }

  public getInvoiceDataByInvoiceNumber(invoiceNumber: string) {
    return this.addInvoiceData.filter(
      (item) => item.invoiceNumber === invoiceNumber && item.lineAmountUsd !== 0
    );
  }

  
  isChecked(inVoice: any): boolean {
    if (this.filteredAllClaimLineDetailsRecords && this.filteredAllClaimLineDetailsRecords.length > 0) {
      const result = this.filteredAllClaimLineDetailsRecords.some(item => {
        
      // const claimLineNumber = parseInt(inVoice.lineNumber, 10);
       const claimLineNumber = inVoice.lineNumber;
     
        return item.lineNumber === claimLineNumber && item.invoiceNumber ===inVoice.invoiceNumber;
      });
  
      //console.log("Result:", result);
      return result;
    }
    return false;
  }
  
 
  
  

  public setAddInvoiceData(item: any, isChecked: boolean) {
    if (isChecked) {
     // this.invoiceRowChecked = true;
      if (this.tempArr.length === 0) {
        this.tempArr.push(item); // If tempArr is empty, just push the item
      } else {
        const isCustomerNumberMatching = this.tempArr.some(
          (tempItem: any) => tempItem.CustomerNumber === item.CustomerNumber
        );
  
        if (isCustomerNumberMatching) {
          this.tempArr.push(item); // If CustomerNumber matches, push the item
        } else {
          this.tempArr = []; // If CustomerNumber doesn't match, empty tempArr
          this.recentassociateAddInvoiceCheckBoxData = [];
          this.tempArr.push(item); // Then push the item
        }
      }
      item.checked = isChecked
    } else {
   //   this.invoiceRowChecked = false;
      const index = this.tempArr.findIndex(
        (tempItem: any) =>
          tempItem.lineNumber === item.lineNumber &&
          tempItem.invoiceNumber === item.invoiceNumber
      );
  
      if (index !== -1) {
        this.tempArr.splice(index, 1); // Remove the item from tempArr when unchecked
      }
      item.checked = isChecked
    
    }
   // this.invoiceRowChecked = this.tempArr.length > 0  ? true : false;
    console.log(this.tempArr, 'tempArr');
    this.recentassociateAddInvoiceData = this.tempArr;
    this.invoiceRowChecked = this.recentassociateAddInvoiceData.length === 0;
  //  this.invoiceRowChecked = this.recentassociateAddInvoiceData.length === 0 || 
  //  !this.recentassociateAddInvoiceData.some(item => !item.detailTypeCode || item.detailTypeCode.trim() === '');
 
    }
  

    addSelectedInvoice() {
      console.log(this.recentassociateAddInvoiceData, 'add selected');
      this.recentassociateAddInvoiceData = this.recentassociateAddInvoiceData.filter(item => {
        // Condition 1: Exclude items with detailTypeCode = "TOTAL"
        if (item.detailTypeCode === "TOTAL") {
            return false;
        }
        
        // Condition 2: Exclude items with specific detailTypeCode if netAmount is 0
        const chargeTypes = ['FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'];
        if (chargeTypes.includes(item.detailTypeCode) && item.netAmount === 0) {
            return false;
        }
        
        // Keep the item if it doesn't meet any exclusion conditions
        return true;
    });
    console.log(this.recentassociateAddInvoiceData, 'add selectedafter');
      this.closeAddInvoiceModal();
      const groupedDataMap = new Map();
      const claimLineDetailDTOSMap = new Map();
      let currentMaxLineNumber = this.maxLineNumber;

      //let lineNumberCounter = 1;  // Initialize the counter
      this.recentassociateAddInvoiceData.forEach(item => {
        console.log(item,'iteminvdata')
        const customerNumber = item.CustomerNumber;
     this.recentassociateAddInvoiceDate = item.invoiceDate;
     this.recentassociateAddInvoiceinvoiceNumber = item.invoiceNumber;
     this.recentassociateAddInvoicebolNumber = item.bolNumber;
     this.recentassociateAddInvoicemasterBolNbr  = item.masterBolNbr;
     this.recentassociateAddInvoicebolNumberpoNumber  =item.poNumber
     this.recentassociateAddInvoiceorderNumber = item.orderNumber;
        if (!groupedDataMap.has(customerNumber)) {
          groupedDataMap.set(customerNumber, {
            createdByUserId: this.claimDetails.createdByUserId,
            modifiedByUserId: this.claimDetails.createdByUserId,
            claimId: this.claimDetails.claimId,
            claimCategoryCode: this.claimDetails.claimCategory.claimCategoryCode,
            customerNumber: customerNumber,
            claimReasonCode: this.claimDetails.claimReasonDefinition.claimReasonCode,
            endUserClaim: this.claimDetails.endUserClaim,
            jobStopped: this.claimDetails.jobStopped,
            priorityClaim: this.claimDetails.priorityClaim,
            //exchangeRate: this.taxAndFreightData[0].exchangeRate||'',
           exchangeRate: this.invExchangeRate,
            sellingCompany: this.claimDetails.sellingCompany ? this.claimDetails.sellingCompany : item.selco,
            territory: this.claimDetails.territory,
            division: this.claimDetails.division,
            region: item.region,
            businessMarketIndicator: this.claimDetails.businessMarketIndicator ||0,
            addToWatchList: true,
            claimDocumentDTOS: [],
            ...(this.claimDetails.endUserInformation && {
              endUserInformationDTO : {
                  createdByUserId : this.claimDetails?.endUserInformation.createdByUserId,
                  modifiedByUserId : this.claimDetails?.endUserInformation.modifiedByUserId,
                 // createdDateTime : this.claimDetails.endUserInformation.createdDateTime,
                 // modifiedDateTime : this.claimDetails.endUserInformation.modifiedDateTime,
                  endUserInformationId : this.claimDetails.endUserInformation.endUserInformationId,
                  firstName : this.claimDetails.endUserInformation.firstName,
                  middleInitial : this.claimDetails.endUserInformation.middleInitial,
                  lastName : this.claimDetails.endUserInformation.lastName,
                  companyName : this.claimDetails.endUserInformation.companyName,
                  countryId : this.claimDetails.endUserInformation.countryId,
                  addressLine1 : this.claimDetails.endUserInformation.addressLine1,
                  addressLine2 : this.claimDetails.endUserInformation.addressLine2,
                  city : this.claimDetails.endUserInformation.city,
                  stateId : this.claimDetails.endUserInformation.stateId,
                  postalCode : this.claimDetails.endUserInformation.postalCode,
                  county : this.claimDetails.endUserInformation.county,
                  businessPhoneDialCodeId : this.claimDetails.endUserInformation.businessPhoneDialCodeId,
                  businessPhoneNumber : this.claimDetails.endUserInformation.businessPhoneNumber,
                  businessPhoneExtension : this.claimDetails.endUserInformation.businessPhoneExtension,
                  homePhoneDialCodeId : this.claimDetails.endUserInformation.homePhoneDialCodeId,
                  homePhoneNumber : this.claimDetails.endUserInformation.homePhoneNumber,
                  homePhoneExtension : this.claimDetails.endUserInformation.homePhoneExtension,
                  cellPhoneDialCodeId : this.claimDetails.endUserInformation.cellPhoneDialCodeId,
                  cellPhoneNumber : this.claimDetails.endUserInformation.cellPhoneNumber,
                  cellPhoneExtension : this.claimDetails.endUserInformation.cellPhoneExtension,
                  emailAddress : this.claimDetails.endUserInformation.emailAddress,
                  
             }
           })
          });
        }
    
        if (!claimLineDetailDTOSMap.has(item.invoiceNumber)) {
          claimLineDetailDTOSMap.set(item.invoiceNumber, []);
          //lineNumberCounter = 1;  
           

               }
    
        const claimLineDetailDTO = {
          
          ////createdByUserId: this.claimDetails.createdByUserId,
          modifiedByUserId: this.claimDetails.createdByUserId,
         // claimLineId: this.claimDetails.claimId,
          unitOfMeasureCode: item.unitofMeasure||'',
          rcsCode: item.rcsCode||'',
          lineNumber:currentMaxLineNumber + 1,
          // lineNumber: ['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'].includes(item.detailTypeCode) ? '' : currentMaxLineNumber + 1,
         // invoiceLineNumber: Number(item.lineNumber)||'',  
          invoiceLineNumber:['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'].includes(item.detailTypeCode) ? '' : Number(item.lineNumber), 
          bolNumber:item.bolNumber || '',     
          masterBolNbr:item.masterBolNbr ||'',
           orderDate:item.orderDate||'',
           orderNumber:item.orderNumber||'',
           poNumber:item.poNumber||'',
           invoiceDate:item.invoiceDate||'',
          rollNumber: item.rollNbr||'',
          styleNumber: item.sellingStyleNbr||'',
          colorNumber: item.colorNbr||'',
          claimReasonCode: this.claimDetails.claimReasonDefinition.claimReasonCode||'',
          grade: item.grade||'',
          lineAmountUsd: ['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'].includes(item.detailTypeCode) ? Number(item.lineAmountUsd) : Number(item.netAmount),
         lineAmountForeign:Number(item.lineAmountForeign)|| 0,
          unitPriceUsd: Number(item.unitPrice)||0,
          unitPriceForeign: Number(item.unitPriceForeign) || 0,
          pricingCurrencyCode: this.pricingCurrencyCode||'',
          currencyCodeDesignation: this.currencyCodeDesignation||'',
          currencyCodeName: this.currencyCodeName||'',
          exchangeRate:this.invExchangeRate||1,// this.taxAndFreightData[0].exchangeRate||'', //Number(item.exchangeRate)||1,
          dyeLot: item.dyeLot,
          quantity:Number( item.quantity),
          sellingCompany: item.selco ||'',
          productCode: item.productCode,
          inventoryStyle: item.inventoryStyleNbr || '',
          inventoryColor: item.inventoryColorNbr || '',
          manufacturingPlant: item.manufacturingPlant || '',
          detailTypeCode: item.detailTypeCode ? item.detailTypeCode :'MERCH',
          lineSourceCode: item.lineSourceCode || '',
          lineSourceReference:  item.invoiceNumber || '',
          lineAddReasonCode: item.lineAddReasonId || '',
          vendorId: item.vendorId || '',
          claimDetailRecordDTOS: [
            //{
          //   //  createdByUserId: this.claimDetails.createdByUserId,
          //   //  modifiedByUserId: this.claimDetails.createdByUserId,
          //    // claimDetailRecordId: 0,
          //     detailRecordTypeCode: 'INV',
          //     widthInFeet:Number( item.widthFeet)||0,
          //     widthInInches: Number(item.widthInch)||0,
          //     lengthInFeet: Number(item.lengthFeet)||0,
          //     lengthInInches: Number(item.lengthInch)||0,
          //     quantity: Number(item.quantity)||0,
          //     unitOfMeasure: item.unitOfMeasure || '',
          //     amountUsd: Number(item.netAmount)||0,
          //     amountForeign: Number(item.amountForeign) || 0,
          //     unitPriceUsd: Number(item.unitPrice)||0,
          //     unitPriceForeign: Number(item.unitPriceForeign) || 0,
          //     exchangeRate: Number(item.exchangeRate)||1
          //   },
          {
          //  createdByUserId: this.claimDetails.createdByUserId,
          //  modifiedByUserId: this.claimDetails.createdByUserId,
          //  claimDetailRecordId: 0,
            detailRecordTypeCode: 'CLM',
            widthInFeet:Number( item.widthFeet)||0,
            widthInInches: Number(item.widthInch)||0,
            lengthInFeet: Number(item.lengthFeet)||0,
            lengthInInches: Number(item.lengthInch)||0,
            quantity: Number(item.quantity)||0,
            unitOfMeasure: item.unitOfMeasure || '',
            amountUsd: Number(item.netAmount)||0,
            amountForeign: Number(item.amountForeign) || 0,
            unitPriceUsd: Number(item.unitPrice)||0,
            unitPriceForeign: Number(item.unitPriceForeign) || 0,
            //exchangeRate: this.taxAndFreightData[0].exchangeRate||'',
            exchangeRate: Number(item.exchangeRate)||1
          }]
        };
    
        claimLineDetailDTOSMap.get(item.invoiceNumber).push(claimLineDetailDTO);
        //lineNumberCounter++;  // Increment the counter for the next line item
        currentMaxLineNumber++;

      });
    
      groupedDataMap.forEach((groupedData, customerNumber) => {
        claimLineDetailDTOSMap.forEach((claimLineDetailDTOS, invoiceNumber) => {
          if (groupedData.customerNumber === customerNumber) {
            groupedData.claimDocumentDTOS.push({
            //  createdByUserId: this.claimDetails.createdByUserId,
             // modifiedByUserId: this.claimDetails.createdByUserId,
             // claimDocumentId: Number(invoiceNumber),
              documentNumber: invoiceNumber,
              exchangeRate: this.taxAndFreightData[0]?.exchangeRate||1,
             // exchangeRate: Number(claimLineDetailDTOS[0].exchangeRate)||'',
              orderNumber:claimLineDetailDTOS[0].orderNumber ||'',// claimLineDetailDTOS[0].orderNumber || '',
              purchaseOrderNumber:claimLineDetailDTOS[0].poNumber ||'',
              billofLading:claimLineDetailDTOS[0].bolNumber ||'',
              masterBillofLading: claimLineDetailDTOS[0].masterBolNbr || '',
              documentTypeCode: 'INV',//claimLineDetailDTOS[0].documentTypeCode || '',
              claimReasonCode: this.claimDetails.claimReasonDefinition?.claimReasonCode||'',
              invoiceDate: claimLineDetailDTOS[0].invoiceDate||'',//claimLineDetailDTOS[0].invoiceDate || '',
              claimLineDetailDTOS: claimLineDetailDTOS
            });
          }
        });
      });
    
      const transformedAddInvoicePayload = Array.from(groupedDataMap.values());
      const addInvoicePayload = transformedAddInvoicePayload[0];
     
    
    
    console.log(addInvoicePayload,'addInvoicePayload');
    
      console.log(transformedAddInvoicePayload, 'transformedData');
        this.claimDetailService.addInvoice(addInvoicePayload).subscribe(
      (data) => {
        this.CloseModalAddInvoice('addinvoices');
     this.refreshClaimDetails.emit();
    this.getDocType();
   // console.log('opensnackbar')
        
    this.toasterService.showToast('success', 'Invoice added succesfully');
    
      },
    (error) => {
    
        
    this.toasterService.showToast('error', 'Failed to Add Invoice Line Item');
    }
    )
   console.log(this.addpayloadResponse,'payloadresposne');
  
  
    }
    
  
  

  
  



// public rgaPathModal(){

//     let selectedRGA = this.selectedLine
//     let rga = selectedRGA.filter((data:any)=>data.selectedLine.claimReasonDefinition.claimReasonCode == "ALL")
//     this.snackBar.open('Kindly Update The ClaimReasonCode  To Proceed With RGA', 'Close', {
//       duration: 3000,
//       verticalPosition: 'top',
//       panelClass: ['custom-snackbar'], 
//     });
 

//   console.log(this.selectedLine,'lineData')
//   let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];
//   this.selectedLine.forEach((line:any)=>{
//     let obj = {
//       "claimLineId":line.claimLineId ,
//       "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
//       "squareFeet": line.selectedLine.squareFeet,
//       "quantity":line.selectedLine.quantity,
//       "productCode":line.selectedLine.productCode
//     }
//     dtosArray.push(obj)
//   }) 
//   const rgaPayload = {
//     "isContinue": false,
//    "sellingCompanyCode": this.claimDetails.sellingCompany,
//    "claimId": this.claimDetails.claimId,
//    "requestLineDetailsDTOS": dtosArray
//    }
//   //  console.log('.........rgadata',rgaPayload)
//   this.claimDetailService.prepareRGA(rgaPayload).subscribe((data:any)=>{
//     this.rgaErrorMessages = data.errorMessage.split(',')
//     // this.rgaErrorMessages = data.errorMessage
   
//   })

  
//   const modelDiv = document.getElementById('rgapathmodal');
//   if (modelDiv != null) {
//     modelDiv.style.display = 'block';
//   }
// }

// public rgaPathModal() {
//   console.log(this.claimDetails.claimAddresses,'claimDetailsrgapathmodalfuc')
//   let selectedRGA = this.selectedLine;
  
//   // Check if any line has claimReasonCode "ALL"
//   let rga = selectedRGA.filter((data: any) => data.selectedLine.claimReasonDefinition.claimReasonCode === "ALL");

//   // If any line has claimReasonCode "ALL", open the snackbar and return early
//   if (rga.length > 0) {
//     this.snackBar.open('Kindly Update The ClaimReasonCode To Proceed With RGA', 'Close', {
//       duration: 3000,
//       verticalPosition: 'top',
//       panelClass: ['custom-snackbar'], 
//     });
//     return; // Exit the function if the condition is met
//   }

//   // The rest of the code will only run if no line has claimReasonCode "ALL"
//   console.log(this.selectedLine, 'lineData');
//   let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];

//   this.selectedLine.forEach((line: any) => {
//     let obj = {
//       "claimLineId": line.claimLineId,
//       "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
//       "squareFeet": line.selectedLine.squareFeet,
//       "quantity": line.selectedLine.quantity,
//       "productCode": line.selectedLine.productCode
//     };
//     dtosArray.push(obj);
//   });

//   const rgaPayload = {
//     "isContinue": false,
//     "sellingCompanyCode": this.claimDetails.sellingCompany,
//     "claimId": this.claimDetails.claimId,
//     "requestLineDetailsDTOS": dtosArray
//   };

//   this.claimDetailService.prepareRGA(rgaPayload).subscribe((data: any) => {
//     this.rgaErrorMessages = data.errorMessage.split(',');
//   });

//   const modelDiv = document.getElementById('rgapathmodal');
//   if (modelDiv != null) {
//     modelDiv.style.display = 'block';
//   }
// }

public rgaPathModal() {
  console.log(this.claimDetails.claimAddresses, 'claimDetailsrgapathmodalfuc');

  // Find the address with addressTypeId: 6
  const addressWithType6 = this.claimDetails.claimAddresses.find((address: any) => address.addressTypeId === 6);
console.log(addressWithType6,'addressWithType6')
  // Check if emailAddress is empty
  if (addressWithType6 && !addressWithType6.emailAddress.trim()) {
   
    this.toasterService.showToast('error', 'Email Id is not available. Please update the email id to Prepare RGA');
    return; // Exit the function if emailAddress is empty
  }

  // Proceed to check if any line has claimReasonCode "ALL"
  let selectedRGA = this.selectedLine;
  let rga = selectedRGA.filter((data: any) => data.selectedLine.claimReasonDefinition.claimReasonCode === "ALL");

  if (rga.length > 0) {
    
    this.toasterService.showToast('error', 'Line does not have Reason Code, please update the reason code to Prepare RGA');
   
    return; // Exit the function if any line has claimReasonCode "ALL"
  }
  if (this.selectedLine) {
    for (const line of this.selectedLine) {
      console.log(line.selectedLine.claimReasonDefinition.returnEligible, 'returnEligible');
  
      // If returnEligible is false, exit the function immediately
      if (!line.selectedLine.claimReasonDefinition.returnEligible) {
        this.toasterService.showToast('error', 'For this reason code, return is not eligible');
        return; // Exit the function
      }
    }
  }
  
  // If the loop completes without finding any false returnEligible, proceed with the rest of the code
  
  
  

  // The rest of the code will only run if no line has claimReasonCode "ALL"
  console.log(this.selectedLine, 'lineData');
  this.userMappingService.findByUserId(Number(localStorage.getItem('userId'))).subscribe(userDetails => {
    // Assign the activeDirectoryId after successful API response
    this.activeDirectoryId = userDetails.activeDirectoryId;
  
    // Prepare the dtosArray
    let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];
    this.selectedLine.forEach((line: any) => {
      let obj = {
        "claimLineId": line.claimLineId,
        "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
        "squareFeet": line.selectedLine.squareFeet,
        "quantity": line.selectedLine.quantity,
        "productCode": line.selectedLine.productCode
      };
      dtosArray.push(obj);
    });
  
    // Prepare the rgaPayload
    const rgaPayload = {
      "continueFrom": "",
      "claimId": this.claimDetails.claimId,
      "defectOfPercentage": "",
      // "sellingCompanyCode": this.claimDetails.sellingCompany,
      "requestLineDetailsDTOS": dtosArray,
      "loggedInUser": this.activeDirectoryId
    };
  
    // Call prepareRGA only after activeDirectoryId is assigned
    this.prepareRGA(rgaPayload);
  });
  






}
prepareRGA(rgaPayload: any) {
  this.claimDetailService.prepareRGA(rgaPayload).subscribe((data: any) => {
    this.prepareRGAData = data.body;

    if (this.prepareRGAData.rdclocations) {
      this.rdcLocationCode = this.prepareRGAData.rdclocations.locationCode;
      localStorage.setItem('rdcLocationCode', this.rdcLocationCode);
    }

 

    
      if (this.prepareRGAData.codeAndErrorMsgDTOS  && this.prepareRGAData.codeAndErrorMsgDTOS.length > 0) {
        // Display the modal
        const modelDiv = document.getElementById('listofdefectmodal');
        if (modelDiv) {
          modelDiv.style.display = 'block';
        }

        // Extract and join error messages
        this.rgaErrorMessages = this.prepareRGAData.codeAndErrorMsgDTOS
          .map((dto: any) => dto.errorMessage)
          .filter((msg: string) => msg) // Filter out any empty messages
          .map((msg: string) => msg.trim()); // Optional: Trim whitespace

        console.log(this.rgaErrorMessages, 'this.rgaErrorMessages');
      } 
     else {
        
    
      if (this.rdcLocationCode) {
             console.log(this.rdcLocationCode, 'this.rdcLocationCode,,,override');
        
        const navigationExtras: NavigationExtras = {
          state: {
            selectedLine: this.selectedLine,
            rgaDetails: this.rgaDetails,
            claimAddresses: this.claimDetails.claimAddresses,
            claimId: this.claimDetails.claimId,
            claimNumber: this.claimDetails.claimNumber,
            customerNumber: this.claimDetails.customerNumber,
            claimDocuments: this.claimDetails.claimDocuments,
            rdcLocationCode: localStorage.getItem("rdcLocationCode"),
            claimAreaId:this.claimDetails.claimAreaId,
            division:this.claimDetails.division,
            region:this.claimDetails.region,
            sellingCompany:this.claimDetails.sellingCompany
          }
        };
    console.log(this.claimDetails.sellingCompany,'seco')
        console.log(navigationExtras, 'navigationExtras88dd');
    
        
        this.router.navigate(['/prep-rga'], navigationExtras);
      }
    }
  });
}

public listofdefectmodal(){
  console.log(this.formGroup.get('defectPercent')?.value,'defectPercent')
  let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; }[] = [];

  this.selectedLine.forEach((line: any) => {
    let obj = {
      "claimLineId": line.claimLineId,
      "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
      "squareFeet": line.selectedLine.squareFeet,
      "quantity": line.selectedLine.quantity,
      "productCode": line.selectedLine.productCode
    };
    dtosArray.push(obj);
  });
  const rgaPayload = {
    "continueFrom": this.prepareRGAData.errorOccurred,
    "claimId": this.claimDetails.claimId,
    "defectOfPercentage":this.formGroup.get('defectPercent')?.value,
  //  "sellingCompanyCode": this.claimDetails.sellingCompany,
 
    "requestLineDetailsDTOS": dtosArray,
    "loggedInUser": this.activeDirectoryId
  };
  this.prepareRGA(rgaPayload)

}

public closeModal(id:string){
  const modelDiv = document.getElementById(id);
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}

closeOverrideModal(){
  const modelDiv = document.getElementById('listofdefectmodal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
  this.rgaErrorMessages = [];
  this.listofdefectmodal();
  
  if(this.rdcLocationCode){
    console.log(this.rdcLocationCode,'this.rdcLocationCode,,,overide')
      const navigationExtras: NavigationExtras = {
        state: {
          selectedLine: this.selectedLine,
          rgaDetails: this.rgaDetails,
          claimAddresses: this.claimDetails.claimAddresses,
          claimId: this.claimDetails.claimId,
          claimNumber:this.claimDetails.claimNumber,
          customerNumber: this.claimDetails.customerNumber,
          claimDocuments: this.claimDetails.claimDocuments,
          rdcLocationCode:localStorage.getItem("rdcLocationCode"),
          claimAreaId:this.claimDetails.claimAreaId,
          division:this.claimDetails.division,
          region:this.claimDetails.region,
          sellingCompany:this.claimDetails.sellingCompany
        }
      };
  console.log(navigationExtras,'navigationExtras')
      const modelDiv = document.getElementById('listofdefectmodal');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
        this.router.navigate(['/prep-rga'], navigationExtras);
      }
   
    }
}

openDefectPercentModal(){
  if (this.prepareRGAData.codeRuleReturnType !== 'Accomodation' || this.prepareRGAData.codeRuleReturnType !== 'Quality'){
 this.defectpercent(); 
  } else  {
    // const modelDiv = document.getElementById('listofdefectmodal');
       
    // if (modelDiv != null) {
    //   modelDiv.style.display = 'block';
    // }
  }
}
public defectpercent(){
  const modelDiv = document.getElementById('defectpercentmodal');
  if (modelDiv != null) {
    modelDiv.style.display = 'block';
   this.closeModal('rgapathmodal');
  }
}



  openDuplicateLineModel(){
    const modelDiv = document.getElementById('duplicateLineModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
  }
  openServiceLineModel(){
    // const modelDiv = document.getElementById('createServiceModal');
    //   if (modelDiv != null) {
    //     modelDiv.style.display = 'block';
    //   }
  }
  openCreateServiceModel(){
    const modelDiv = document.getElementById('createServiceModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
  }
  cancelDeleteLineItemModel() {
    const modelDiv = document.getElementById('deleteLineItemModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  confirmDeleteLineItemModel() {
    const claimLineIds = this.selectedLine
    ? this.selectedLine.map((item:any) => item.claimLineId)
    : [];
    
    console.log(claimLineIds,'claimLineIds')
  
    this.claimDetailService.deleteLineItem(claimLineIds).subscribe(
      (data) => {
        this.selectedLine = [];
        this.refreshClaimDetails.emit();
        this.cancelDeleteLineItemModel();
       
        this.toasterService.showToast('success', 'Line item deleted successfully');
   
        const index = this.selectedLine.findIndex((i: any) => i.claimLineId == this.selectedLineItem.claimLineId);
        if (index !== -1) {
          this.selectedLine.splice(index, 1);
        }
      },
      (error) => {
        this.refreshClaimDetails.emit();
        this.cancelDeleteLineItemModel();
       
        this.toasterService.showToast('error', 'Failed to delete line item');
      }
    );
  }
  
  deleteLineItem() {
    const modelDiv = document.getElementById('deleteLineItemModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
 
  setLineInfo(event: Event, selectedClaimLineDetails: any) {
    console.log(selectedClaimLineDetails, 'selectedClaimLineDetails123');
    event.stopPropagation();
    this.loadingData = true;
  
    const addInvoiceObj = {
      customerNumber: this.claimDetails.customerNumber,
      invoiceNumber: '',
      bolNumber: '',
      poNumber: '',
      orderNumber: '',
      docDate: '',
      docType: 'INV',
      rollNumber: '',
      invoiceDate: ''
    };
  
    const documentDate = new Date(selectedClaimLineDetails.documentDate);
    const adjustedDate = new Date(documentDate.getTime() - (documentDate.getTimezoneOffset() * 60000));
    addInvoiceObj.invoiceDate = adjustedDate.toISOString().substring(0, 10);
    addInvoiceObj.invoiceNumber = selectedClaimLineDetails.documentNumber;
  
    console.log(addInvoiceObj, 'addInvoiceObj');
  
    this.intiateClaimService.getAddInvioce(addInvoiceObj).pipe(
      delay(1000) // Adjust the delay time as needed
    ).subscribe(
      (data: HttpResponse<any>) => {
        if (data.status === 200) {
          console.log(data, 'data');
          this.addInvoiceData = data.body;
  
          const processedInvoices = new Set<string>();
          const observables = this.addInvoiceData.map(item => {
            if (!processedInvoices.has(item.invoiceNumber)) {
              processedInvoices.add(item.invoiceNumber);
  
              return forkJoin([
                this.taxAndFreight(item.invoiceNbr, item.invoiceDate).pipe(
                  map(taxAndFreightData => {
                    if (taxAndFreightData) {
                      const { totalFreightAmt, totalTaxAmt, totalInvoiceAmt } = taxAndFreightData[0];
          
                      this.currencyCodeName = taxAndFreightData[0].currencyCodeName || "UNITED STATES $";
                      this.currencyCodeDesignation = taxAndFreightData[0].currencyCodeDesignation || "USD";
                      this.pricingCurrencyCode = taxAndFreightData[0].pricingCurrencyCode || "0000";
                      this.invExchangeRate = taxAndFreightData[0].exchangeRate;
          
                      // Create freight item
                      const freightItem = {
                        lineNumber: '',
                        invoiceNumber: item.invoiceNbr,
                        invoiceNbr: item.invoiceNbr,
                        documentNumber: item.invoiceNbr,
                        rollNumber: '       ',
                        styleNumber: '',
                        colorNumber: '',
                        widthInFeet: '',
                        lengthInFeet: '',
                        grade: '',
                        unitPriceUsd: '0.0',
                        lineAmountUsd: totalFreightAmt || 0,
                        dyeLot: '            ',
                        quantity: '0',
                        unitOfMeasureCode: '',
                        rcsCode: '',
                        claimReasonCode: '',
                        lineAmountForeign: 0,
                        unitPriceForeign: 0,
                        pricingCurrencyCode: '0000',
                        currencyCodeDesignation: '',
                        currencyCodeName: '',
                       // exchangeRates: item.exchangeRate,
                        exchangeRate: this.invExchangeRate,
                        sellingCompany: '',
                        productCode: '',
                        inventoryStyle: '',
                        inventoryColor: '',
                        manufacturingPlant: '',
                        detailTypeCode: 'FRGHT',
                        lineSourceCode: '',
                        lineSourceReference: '',
                        lineAddReasonCode: '',
                        vendorId: '',
                      };
          
                      // Create tax item
                      const taxItem = {
                        lineNumber: '',
                        invoiceNumber: item.invoiceNbr,
                        invoiceNbr: item.invoiceNbr,
                        documentNumber: item.invoiceNbr,
                        rollNumber: '       ',
                        styleNumber: '',
                        colorNumber: '',
                        widthInFeet: '',
                        lengthInFeet: '',
                        grade: '',
                        unitPriceUsd: '0.0',
                        lineAmountUsd: totalTaxAmt || 0,
                        dyeLot: '            ',
                        quantity: '0',
                        unitOfMeasureCode: '',
                        rcsCode: '',
                        claimReasonCode: '',
                        lineAmountForeign: 0,
                        unitPriceForeign: 0,
                        pricingCurrencyCode: '0000',
                        currencyCodeDesignation: '',
                        currencyCodeName: '',
                        //exchangeRates: item.exchangeRate,
                        exchangeRate: this.invExchangeRate,
                        sellingCompany: '',
                        productCode: '',
                        inventoryStyle: '',
                        inventoryColor: '',
                        manufacturingPlant: '',
                        detailTypeCode: 'TAX',
                        lineSourceCode: '',
                        lineSourceReference: '',
                        lineAddReasonCode: '',
                        vendorId: '',
                      };
          
                      // Push freight and tax items
                      this.addInvoiceData.push(freightItem, taxItem);
          
                      // Return totalInvoiceAmt to be used in the next observable
                      return totalInvoiceAmt;
                    }
                    return null; // Return null if no data is available
                  }),
                  catchError(error => {
                    console.error('Error in taxAndFreight:', error);
                    return of(null); // Return a fallback observable
                  })
                ),
          
                this.careSurCharge(item.invoiceNbr, item.invoiceDate).pipe(
                  map(careSurChargeData => {
                    if (careSurChargeData) {
                      this.careSurChargeDesc = careSurChargeData[0].description.trim();   
                      const { totalCharge } = careSurChargeData[0];
          
                      // Create surcharge item
                      const chargeItem = {
                        lineNumber: '',
                        invoiceNumber: item.invoiceNbr,
                        invoiceNbr: item.invoiceNbr,
                        documentNumber: item.invoiceNbr,
                        rollNumber: '       ',
                        styleNumber: '',
                        colorNumber: '',
                        widthInFeet: '',
                        lengthInFeet: '',
                        grade: '',
                        unitPriceUsd: '0.0',
                        lineAmountUsd: totalCharge || 0,
                        dyeLot: '            ',
                        quantity: '0',
                        unitOfMeasureCode: '',
                        rcsCode: '',
                        claimReasonCode: '',
                        lineAmountForeign: 0,
                        unitPriceForeign: 0,
                        pricingCurrencyCode: '0000',
                        currencyCodeDesignation: '',
                        currencyCodeName: '',
                        exchangeRate: this.invExchangeRate,
                        sellingCompany: '',
                        productCode: '',
                        inventoryStyle: '',
                        inventoryColor: '',
                        manufacturingPlant: '',
                        detailTypeCode:this.careSurChargeDesc.trim(),
                        lineSourceCode: '',
                        lineSourceReference: '',
                        lineAddReasonCode: '',
                        vendorId: '',
                      };
          
                      // Push the charge item
                      this.addInvoiceData.push(chargeItem);
                    }
                    return null; // Return null if no data is available
                  }),
                  catchError(error => {
                    console.error('Error in careSurCharge:', error);
                    return of(null); // Return a fallback observable
                  })
                ),
              ]).pipe(
                map(([totalInvoiceAmt, _]) => {
                  // Now totalInvoiceAmt is available here
                  if (totalInvoiceAmt) {
                    const totalInvoiceAmtData = {
                       lineNumber: '',
                        totalInvoiceAmt:totalInvoiceAmt,
                        invoiceNumber:item.invoiceNumber,
                        invoiceNbr: item.invoiceNumber,
                        documentNumber: item.invoiceNumber,
                        CustomerNumber:item.CustomerNumber,
                        rollNumber: '       ',
                        styleNumber: '',
                        colorNumber: '',
                        widthInFeet: '',
                        lengthInFeet: '',
                        grade: '',
                        unitPriceUsd: '0.0',
                        netAmount:totalInvoiceAmt || 0,
                        lineAmountUsd: totalInvoiceAmt || 0,
                        dyeLot: '            ',
                        quantity: '0',
                        unitOfMeasureCode: '',
                        rcsCode: '',
                        claimReasonCode: '',
                        lineAmountForeign: 0,
                        unitPriceForeign: 0,
                        pricingCurrencyCode: this.pricingCurrencyCode,
                        currencyCodeDesignation: this.currencyCodeDesignation,
                        currencyCodeName: this.currencyCodeName,
                       // exchangeRates:item.invExchangeRate,
                        exchangeRate:this.invExchangeRate,
                        sellingCompany: '',
                        productCode: '',
                        inventoryStyle: '',
                        inventoryColor: '',
                        manufacturingPlant: '',
                        detailTypeCode: 'TOTAL',
                        lineSourceCode: '',
                        lineSourceReference: '',
                        lineAddReasonCode: '',
                        vendorId: '',
                    };
          
                    // Push totalInvoiceAmtData after chargeItem
                    this.addInvoiceData.push(totalInvoiceAmtData);
                  }
                })
              );
            }
            return of(null); // Return a fallback observable
          });
          
          forkJoin(observables).subscribe(() => {
            console.log(this.addInvoiceData, 'Final addInvoiceData');
            this.loadingData = false;
  
            // Additional processing
            this.invoiceNumberList = [];
            this.orderNumberList = [];
            this.invoiceDateList = [];
            const filteredselctedClaimLineDetails = selectedClaimLineDetails.claimLineDetails.filter(
              (lineDetail: any) => lineDetail.statusId === 1
            );
            console.log(filteredselctedClaimLineDetails, 'filteredselctedClaimLineDetails');
            this.unmatchedResult = this.addInvoiceData.filter((invoice) => {
              // Check if there is no matching lineNumber in filteredselctedClaimLineDetails
              return !filteredselctedClaimLineDetails.some((claimLineDetail: any) => {
                // Matching condition for specific detailTypeCodes
                if (['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'].includes(claimLineDetail.detailType.detailTypeCode)) {
                  return claimLineDetail.detailType.detailTypeCode === invoice.detailTypeCode;
                  //  && claimLineDetail.statusId !== 3
                    
                }
                // Default matching condition for other detailTypeCodes
                return parseInt(claimLineDetail.invoiceLineNumber, 10) === parseInt(invoice.lineNbr, 10)
                  && claimLineDetail.statusId !== 3;
              });
              // return !filteredselctedClaimLineDetails.some(
              //   (claimLineDetail: any) => parseInt(claimLineDetail.lineNumber, 10) === parseInt(invoice.lineNbr, 10) && claimLineDetail.statusId != 3
              // );
            });
            console.log(this.unmatchedResult, 'unmatchedResults');
            // this.matchedResults = this.addInvoiceData.filter((invoice) => {
            //   // Check if there is a matching lineNumber in filteredselctedClaimLineDetails
            //   return filteredselctedClaimLineDetails.some(
            //     (claimLineDetail: any) => parseInt(claimLineDetail.lineNumber, 10) === parseInt(invoice.lineNbr, 10) && claimLineDetail.statusId !== 3
            //   );
            // });
            // console.log(this.matchedResults, 'matchedResults..');
            this.matchedResults = this.addInvoiceData.filter((invoice) => {
              // Check if there is a matching lineNumber in filteredselctedClaimLineDetails
              return filteredselctedClaimLineDetails.some((claimLineDetail: any) => {
                // Matching condition for specific detailTypeCodes
                if (['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'].includes(claimLineDetail.detailType.detailTypeCode)) {
                  return claimLineDetail.detailType.detailTypeCode === invoice.detailTypeCode;
                  //  && claimLineDetail.statusId !== 3
                    
                }
                // Default matching condition for other detailTypeCodes
                return parseInt(claimLineDetail.invoiceLineNumber, 10) === parseInt(invoice.lineNbr, 10)
                  && claimLineDetail.statusId !== 3;
              });
            });
            
            console.log(this.matchedResults, 'matchedResults..');
            this.createdObjects = [];
            this.unmatchedResult.map((lineItem: any, i) => {
              console.log(lineItem, 'lineItem');
  
              const newObject = {
                createdByUserId: selectedClaimLineDetails.createdByUserId || 1,
                modifiedByUserId: selectedClaimLineDetails.modifiedByUserId || 1,
                claimDocId: selectedClaimLineDetails.claimDocumentId || 0,
                lineNumber: lineItem.lineNbr,
                invoiceLineNumber:lineItem.invoiceLineNumber ? lineItem.invoiceLineNumber : lineItem.lineNbr,
                rollNumber: lineItem.rollNbr,
                styleNumber: lineItem.sellingStyleNbr,
                colorNumber: lineItem.colorNbr,
                rcsCodes: {
                  rcsCodeId: lineItem.rcsCodeId || "0",
                  rcsCode: lineItem.rcsCode,
                  statusId: 1,
                },
               // claimReasonDefinition: selectedClaimLineDetails.claimReasonDefinition,
			     claimReasonDefinition: {
    createdByUserId: selectedClaimLineDetails.createdByUserId,
                modifiedByUserId: selectedClaimLineDetails.modifiedByUserId,

    claimReasonId: selectedClaimLineDetails.claimReasonDefinition.claimReasonId,
    claimReasonCode: selectedClaimLineDetails.claimReasonDefinition.claimReasonCode,
    visibilityScope: selectedClaimLineDetails.claimReasonDefinition.visibilityScope,
    reasonTypeGroup: selectedClaimLineDetails.claimReasonDefinition.reasonTypeGroup,
    isDistributionCompliance:selectedClaimLineDetails.claimReasonDefinition.isDistributionCompliance,
    sampleSizeRequirement: selectedClaimLineDetails.claimReasonDefinition.sampleSizeRequirement,
    definition: selectedClaimLineDetails.claimReasonDefinition.definition,
    cause:selectedClaimLineDetails.claimReasonDefinition.cause,
    testing: selectedClaimLineDetails.claimReasonDefinition.testing,
    claimReasonDescription: selectedClaimLineDetails.claimReasonDefinition.claimReasonDescription,
    allowanceToKeep:selectedClaimLineDetails.claimReasonDefinition.allowanceToKeep,
    serviceToKeep: selectedClaimLineDetails.claimReasonDefinition.serviceToKeep,
    returnEligible: selectedClaimLineDetails.claimReasonDefinition.returnEligible,
    codeRuleReturnType: {
      createdByUserId: lineItem.createdByUserId,
                modifiedByUserId: lineItem.modifiedByUserId,

      codeRuleReturnTypeId: selectedClaimLineDetails.claimReasonDefinition.codeRuleReturnType.codeRuleReturnTypeId,
      codeRuleReturnTypeCode:  selectedClaimLineDetails.claimReasonDefinition.codeRuleReturnType.codeRuleReturnTypeCode,
      codeRuleReturnTypeDescription:  selectedClaimLineDetails.claimReasonDefinition.codeRuleReturnType.codeRuleReturnTypeDescription,
      displaySequence:  selectedClaimLineDetails.claimReasonDefinition.codeRuleReturnType.displaySequence,
      statusId:  selectedClaimLineDetails.claimReasonDefinition.codeRuleReturnType.statusId,
    }
  },
                grade: lineItem.grade,
                lineAmountUsd: lineItem.netAmount ?lineItem.netAmount : lineItem.lineAmountUsd,
                lineAmountForeign: lineItem.netAmount || 0,
                unitPriceUsd: lineItem.unitPrice||0,
                unitPriceForeign: lineItem.unitPrice||0,
                pricingCurrencyCode: lineItem.pricingCurrencyCode||0,
                currencyCodeDesignation: lineItem.currencyCodeDesignation||'',
                currencyCodeName: lineItem.currencyCodeName||'',
                exchangeRate: this.invExchangeRate||'',
                dyeLot: lineItem.dyeLot||'',
                quantity: lineItem.quantity||'',
                sellingCompany: lineItem.selco,
                unitOfMeasure: lineItem.unitofMeasure||"",
                squareFeet: selectedClaimLineDetails?.claimLineDetails[i]?.squareFeet||0,
                productCode: selectedClaimLineDetails?.claimLineDetails[i]?.productCode|| '',
                assignedToSalesId: selectedClaimLineDetails?.claimLineDetails[i]?.assignedToSalesId|| '',
                approvedBySalesId: selectedClaimLineDetails?.claimLineDetails[i]?.approvedBySalesId || '',
                salesReviewStatus: selectedClaimLineDetails?.claimLineDetails[i]?.salesReviewStatus || '',
                salesReviewDate: selectedClaimLineDetails?.claimLineDetails[i]?.salesReviewDate || '',
                approvedUnitPrice: selectedClaimLineDetails?.claimLineDetails[i]?.approvedUnitPrice || '',
                salesDiscount: selectedClaimLineDetails?.claimLineDetails[i]?.salesDiscount || '',
                salesOriginId: selectedClaimLineDetails?.claimLineDetails[i]?.salesOriginId || '',
                inventoryStyle: selectedClaimLineDetails?.claimLineDetails[i]?.inventoryStyle || '',
                inventoryColor: selectedClaimLineDetails?.claimLineDetails[i]?.inventoryColor || '',
                manufacturingPlant: selectedClaimLineDetails?.claimLineDetails[i]?.manufacturingPlant || '',
                //detailType: selectedClaimLineDetails?.claimLineDetails[i]?.detailType || '',
				 detailType: {
				 
        detailTypeCode: lineItem.detailTypeCode ? lineItem.detailTypeCode :"MERCH",
       
				 },
				
                lineSource: selectedClaimLineDetails?.claimLineDetails[i]?.lineSource || '',
               // lineSourceReference: selectedClaimLineDetails?.claimLineDetails[i]?.lineSourceReference || '',
			    lineSourceReference: lineItem.invoiceNumber|| '',
                detailStatusType: selectedClaimLineDetails?.claimLineDetails[i]?.detailStatusType || '',
                lineAddReasonId: selectedClaimLineDetails?.claimLineDetails[i]?.lineAddReasonId || '',
                vendorId: selectedClaimLineDetails?.claimLineDetails[i]?.vendorId || '',
                statusId: 1,
                //claimDetailRecords: selectedClaimLineDetails?.claimLineDetails[i]?.claimDetailRecords || '',
				    claimDetailRecords: [
        {
           // "createdByUserId": selectedClaimLineDetails.createdByUserId,
           // "modifiedByUserId": selectedClaimLineDetails.modifiedByUserId,
          
           // "claimDetailRecordId": selectedClaimLineDetails,
            detailRecordType: {
              //  "detailRecordTypeId": "1",
                detailRecordTypeCode: "INV",
               // "detailRecordTypeDescription":selectedClaimLineDetails.detailRecordTypeDescription,
              //  "displaySequence": selectedClaimLineDetails.displaySequence,
               // "statusId": "1",
               // "createdByUserId": selectedClaimLineDetails.createdByUserId
                //"modifiedByUserId": selectedClaimLineDetails.modifiedByUserId,

            },
            widthInFeet: lineItem.widthFeet,
            widthInInches: lineItem.widthInch,
            lengthInFeet: lineItem.lengthFeet,
            lengthInInches: lineItem.lengthInch,
            quantity: lineItem.quantity,
            unitOfMeasure: lineItem.unitofMeasure,
            amountUsd: lineItem.netAmount,
            amountForeign: lineItem.netAmount,
            unitPriceUsd: lineItem.unitPrice,
            unitPriceForeign: lineItem.unitPrice,
            exchangeRate: this.invExchangeRate,
            statusId: 1
        },
        {
           // "createdByUserId": selectedClaimLineDetails.createdByUserId,
           // "modifiedByUserId": selectedClaimLineDetails.modifiedByUserId,

           // "claimDetailRecordId": 2385,
            detailRecordType: {
              //  "detailRecordTypeId": "2",
                detailRecordTypeCode: "CLM",
              //  "detailRecordTypeDescription": "Claimed",
              //  "displaySequence": "2",
                //"statusId": "1",
               // "createdByUserId":selectedClaimLineDetails.createdByUserId,
               // "modifiedByUserId": selectedClaimLineDetails.modifiedByUserId,

            },
            widthInFeet: lineItem.widthFeet,
            widthInInches: lineItem.widthInch,
            lengthInFeet: lineItem.lengthFeet,
            lengthInInches: lineItem.lengthInch,
            quantity: lineItem.quantity,
            unitOfMeasure: lineItem.unitOfMeasure,
            amountUsd: lineItem.netAmount,
            amountForeign: lineItem.netAmount,
            unitPriceUsd: lineItem.unitPrice,
            unitPriceForeign: lineItem.unitPrice,
            exchangeRate: this.invExchangeRate,
            statusId: 1
        }
    ],
                selected: selectedClaimLineDetails?.claimLineDetails[i]?.selected || '',
                documentNumber: lineItem.invoiceNumber || '',
              };
              this.createdObjects.push(newObject);
             })
            // console.log(this.createdObjects, 'Created Objects');
           
            // selectedClaimLineDetails.claimLineDetails = selectedClaimLineDetails.claimLineDetails.filter((claimLineDetail:any) => 
            //   claimLineDetail?.detailType?.detailTypeCode === 'MERCH' && 
            //   claimLineDetail?.lineSource?.lineSourceCode !== 'DUPL'
            // );
            selectedClaimLineDetails.claimLineDetails = selectedClaimLineDetails.claimLineDetails.filter((claimLineDetail: any) => 
              ['MERCH', 'TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'].includes(claimLineDetail?.detailType?.detailTypeCode) && 
              claimLineDetail?.lineSource?.lineSourceCode !== 'DUPL'
            );
            
            
console.log(selectedClaimLineDetails.claimLineDetails,'liness')
            selectedClaimLineDetails.claimLineDetails.push(...this.createdObjects);
            this.selectedClaimLineInfo = selectedClaimLineDetails;
         
            this.filteredClimLineInfo = this.selectedClaimLineInfo.claimLineDetails.filter((obj: any) => {
             
             return obj.detailType.detailTypeCode != 'TOTAL' && obj.lineAmountUsd != 0
             
              
            });
            // console.log(this.filteredClimLineInfo,'...............fitered')





            console.log(this.selectedClaimLineInfo, 'this.selectedClaimLineInfo13');
          });
        } else {
          console.error('Error: Unexpected status code', data.status);
        }
      },
      (error) => {
        this.loadingData = false;
        if (error.status === 404 && this.addInvoiceForm.value.docNumber) {
          this.show404ErrorMessage = true;
          console.log("404 docnumber error doctype");
        } else if (error.status === 404 && this.addInvoiceForm.value.rollNumber) {
          this.showRollNumberError = true;
          console.log("404 docnumber error rollno");
        } else {
          this.addInvoiceData = [];
        }
      }
    );
  
    this.openAddMoreLinesModal();
    this.getAllClaimLineAddReason();
  }
  
  selectedLineRecord(item: any, checked: any) {
    const identifier = item.claimLineId || item.lineNumber || item.detailType.detailTypeCode;
  
    if (checked) {
      if (this.selectedLineInfo.findIndex((a: any) => a.claimLineId === identifier || a.lineNumber === identifier || a.detailType.detailTypeCode === identifier) === -1) {
        this.selectedLineInfo.push(item);
      }
      if (this.matchedResults.findIndex((a: any) => a.claimLineId === identifier || a.lineNumber === identifier || a.detailTypeCode === identifier || a.invoiceNbr === identifier) === -1) {
        this.matchedResults.push(item);
      }
    } else {
      const selectedLineIndex = this.selectedLineInfo.findIndex((a: any) => a.claimLineId === identifier || a.lineNumber === identifier || a.detailType.detailTypeCode === identifier);
      if (selectedLineIndex !== -1) {
        this.selectedLineInfo.splice(selectedLineIndex, 1);
      }
  
      const matchedResultIndex = this.matchedResults.findIndex((a: any) => a.claimLineId === identifier || a.lineNumber === identifier || a.detailTypeCode === identifier || a.invoiceNbr === identifier);
      if (matchedResultIndex !== -1) {
        this.matchedResults.splice(matchedResultIndex, 1);
      }
    }
  }
  

  public togglecheckAll(isChecked:any){
   this.selectedClaimLineInfo.claimLineDetails.forEach((ele:any)=>{
    this.selectedLineRecord(ele,isChecked);
   })
  }

  // public isAllSelectedMoreLines(){
  //   return this.filteredClimLineInfo.length === this.selectedLineInfo.length

  // }
  
  public isAllSelectedMoreLines(): boolean {
   // console.log(this.filteredClimLineInfo, 'filteredClimLineInfo');
   // console.log(this.matchedResults, 'matchedResultslegnth');
    // Component.ts
    if (this.selectedClaimLineInfo && this.selectedClaimLineInfo.claimLineDetails) {
      const filterselectedClaimLineInfo = this.selectedClaimLineInfo.claimLineDetails.filter(
        (detail: any) => detail.detailType.detailTypeCode !== 'TOTAL' && detail.lineAmountUsd !==0
      );
     // console.log(filterselectedClaimLineInfo, "filterdata");
     // console.log(filterselectedClaimLineInfo.length, "filterdataLength");
      if(filterselectedClaimLineInfo.length == this.matchedResults.length){
        return true;
      }
    }
    return false;
  }
 
  checkAllLines() {
    this.unmatchedLines = this.selectedLineInfo.filter((lineInfo: any) => {
      // Return true if it's unmatched
      return !this.isRecordMatched(lineInfo);
    });
  }
addMoreLineItems(selectedClaimDoc:any) {
  console.log(selectedClaimDoc,'selectedClaimDoc')
  console.log(this.selectedLineInfo,'this.selectedLineInfoforapiaddmore')
  this.checkAllLines();
  this.selectedLineInfo = this.unmatchedLines;
  this.selectedLineInfo = this.selectedLineInfo.filter((item:any) => {
    // Condition 1: Exclude items with detailTypeCode = "TOTAL"
    if (item.detailType.detailTypeCode === "TOTAL") {
        return false;
    }
    
    // Condition 2: Exclude items with specific detailTypeCode if netAmount is 0
    const chargeTypes = ['FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST'];
    if (chargeTypes.includes(item.detailType.detailTypeCode) && item.lineAmountUsd === 0) {
        return false;
    }
    
    // Keep the item if it doesn't meet any exclusion conditions
    return true;
});
console.log(this.selectedLineInfo,'this.selectedLineInfoforapiaddmoreafter')
  let claimLineinvoices:any = [];
 this.loadLineIemsFlag = false;
 let currentMaxLineNumber = this.maxLineNumber;
this.selectedLineInfo.map((line:any) => {
  console.log(line,'line')
  currentMaxLineNumber++;
 // String(this.maxLineNumber + 1).padStart(3, '0'),
claimLineinvoices.push(
{
  "createdByUserId": line.createdByUserId,
  "modifiedByUserId": line.modifiedByUserId,
  //"claimLineId":line.claimLineId ||0,
  "unitOfMeasureCode": line?.unitOfMeasure || '',
  "rcsCode": line.rcsCodes.rcsCode,
  "lineNumber":String(currentMaxLineNumber).padStart(3, '0'),
  "invoiceLineNumber": line.lineNumber||'',
  "rollNumber": line.rollNumber || '',
  "styleNumber": line.styleNumber || '',
  "colorNumber": line.colorNumber || '',
  //"claimReasonCode": line.claimReasonDefinition.claimReasonCode,
  "claimReasonCode": 'ALL',
  "grade": line.grade,
  "lineAmountUsd": Number(line.lineAmountUsd) ||0 ,
  "lineAmountForeign": Number(line.lineAmountForeign) || 0,
  "unitPriceUsd": Number(line.unitPriceUsd) || 0,
  "unitPriceForeign": Number(line.unitPriceForeign) || 0,
  "pricingCurrencyCode": line.pricingCurrencyCode ||'',
  "currencyCodeDesignation": line.currencyCodeDesignation ||'',
  "currencyCodeName": line.currencyCodeName || '',
  "exchangeRate": Number(line.exchangeRate)||1,
  "dyeLot": line.dyeLot||'',
  "quantity": Number(line.quantity) || 0,
  "sellingCompany": line.sellingCompany || '',
  "productCode": line.productCode || '',
  "inventoryStyle": line.inventoryStyle || '',
  "inventoryColor": line.inventoryColor||'',
  "manufacturingPlant": line.manufacturingPlant||'',
  "detailTypeCode": line.detailType.detailTypeCode ?line.detailType.detailTypeCode : 'MERCH',
  "lineSourceCode": '',
  "lineSourceReference": line.lineSourceReference || '',
  "lineAddReasonCode": line.lineAddReasonCode || '',
  "vendorId": "",

 "claimDetailRecordDTOS": [
  {
    "createdByUserId": line.createdByUserId,
    "modifiedByUserId": line.modifiedByUserId,
      "claimDetailRecordId": line.claimDetailRecordId,
      "detailRecordTypeCode": 'CLM',
      "widthInFeet": Number(line.claimDetailRecords[0].widthInFeet) || 0,
      "widthInInches": Number(line.claimDetailRecords[0].widthInInches)||0,
      "lengthInFeet": Number(line.claimDetailRecords[0].lengthInFeet) ||0,
      "lengthInInches": Number(line.claimDetailRecords[0].lengthInInches)|| 0,
      "quantity": Number(line.quantity)||0,
     // "unitOfMeasure": line.unitOfMeasure ||'',
     "unitOfMeasure": 0,
      "amountUsd": Number(line.amountUsd)|| 0,
      "amountForeign": Number(line.amountForeign) || 0,
      "unitPriceUsd": Number(line.unitPriceUsd) || 0,
      "unitPriceForeign": Number(line.unitPriceForeign) || 0,
      "exchangeRate": Number(line.exchangeRate)||1
  }
]
}
)


})
  this.payloadResponse = {
    "createdByUserId": selectedClaimDoc.createdByUserId,
    "modifiedByUserId": selectedClaimDoc.modifiedByUserId,
    "claimDocumentId": selectedClaimDoc.claimDocumentId || 0,
    "documentNumber": selectedClaimDoc.documentNumber || '',
    "exchangeRate": selectedClaimDoc.exchangeRate||1,
    "orderNumber": selectedClaimDoc.orderNumber||'',
    "purchaseOrderNumber": selectedClaimDoc.purchaseOrderNumber||'',
    "billofLading": selectedClaimDoc.billofLading || '',
    "masterBillofLading": selectedClaimDoc.masterBillofLading||'',
    "documentTypeCode": selectedClaimDoc.documentType?.documentTypeCode || '',
    //"claimReasonCode": this.selectedLineInfo[0]?.claimReasonDefinition?.claimReasonCode || '',
    "claimReasonCode": 'ALL',
    "claimLineDetailDTOS": claimLineinvoices
};
console.log(this.payloadResponse,'payloadResponseaddmoreliens')
  this.claimDetailService.addMoreLineItems(this.payloadResponse).subscribe((data)=> {
    this.selectedLineInfo = [];
    this.refreshClaimDetails.emit();
    if(this.loadLineIemsFlag === false){
      console.log("insised to remove object")
      const objectsToRemove = this.createdObjects;
      // Remove pushed objects from this.selectedClaimLineInfo.claimLineDetails
      this.selectedClaimLineInfo.claimLineDetails = this.selectedClaimLineInfo.claimLineDetails.filter((lineInfo:any) => {
        return !objectsToRemove.some(obj => 
            obj.lineNumber === lineInfo.lineNumber && obj.invoiceNumber === lineInfo.invoiceNumber && obj.statusId === lineInfo.statusId
        );
      });
      this.selectedClaimLineInfo.claimLineDetails.push(...this.selectedLineInfo)
  }
    const modelDiv = document.getElementById('addMoreLinesModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
      
     
    this.toasterService.showToast('success', 'Line items added succesfully');
      this.loadLineIemsFlag = true;
      this.createdObjects = [];
      this.unmatchedResult = [];
      this.matchedResults = [];
  },
  (error) => {
      
    this.toasterService.showToast('error', 'Failed to add Line items');
  })
  this.closeAddMoreLinesModal();
}

  duplicateLine(){
    this.claimDetailService.getClaim(this.claimDetails.claimNumber,Number(localStorage.getItem('userId'))).subscribe(data=>{
      this.ClaimDocData = data.claimDocuments;
      this.loadClimeLineData();
    })
  }
  
  openAddInvoiceModal(){
    this.show404ErrorMessage = false;
    this.showRollNumberError = false;
    this.noNewInvoice = false;
    if(!this.claimIsVoid){
    this.noNewInvoice = false;
    const modelDiv = document.getElementById('addinvoice');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
  }
  closeAddInvoiceModal(){
   
    const modelDiv = document.getElementById('addinvoice');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    
  }
  resetForm() {
    this.addInvoiceForm.reset();
    this.addInvoiceForm.patchValue({
      docType: '16',
    });
    this.addInvoiceForm.patchValue({
      customer: this.claimDetails.customerNumber,
    });
    
  }

  navigateToRollHistory() {
    // Navigate to the Roll History component/route
    this.router.navigate(['/roll-history']); // Adjust the path as needed
  }


  // getAllClaimLineAddReason(){
  //   this.claimDetailService.getAllClaimLineAddReason().subscribe(data=>{
  //   this.claimLineAddReasonList = data;
  //   this.addReasonTypeData =  data[0].lineAddReasonDescription
  //     // Set the default to "Claims Administrator Option"
  //     const defaultReason = data.find((item:any) => item.lineAddReasonDescription === 'CLAIMS ADMINISTRATOR OPTION');
  //     if (defaultReason) {
  //       this.addReasonTypeData = defaultReason.lineAddReasonCode; // Set the default value
  //     }
  //   });
  // }

  getAllClaimLineAddReason() {
    this.claimDetailService.getAllClaimLineAddReason().subscribe(data => {
      this.claimLineAddReasonList = data;
      
      // Find and set the default to "Claims Administrator Option"
      const defaultReason = data.find((item: any) => item.lineAddReasonDescription === 'CLAIMS ADMINISTRATOR OPTION');
      
      if (defaultReason) {
        this.addReasonTypeData = defaultReason.lineAddReasonCode; // Set the default value
        this.addLineForm.patchValue({
          lineAddreason : defaultReason.lineAddReasonCode
        })
      }
    });
  }
  
  
  getAllLineCategory(){
    this.claimDetailService.getAllLineCategory().subscribe(data=>{
      this.getCategories=data;
      this.linecategoryTypeData =  data[0].detailTypeDescription
    })
  }
  unitOfMeasures(){
    this.claimDetailService.getClaimUnitOfMeasure().subscribe(data=>{
     this.unitOfmeasure= data
    })
  }
  getAllReasonCode(){
    this.claimDetailService.getAllReasonCodes().subscribe(
      (data: any) => {
        this.claimList = data;
      }
    )
  }

  fetchServiceToKeepType(){
    this.claimDetailService.fetchServiceToKeepType().subscribe(data =>{
this.serviceTypes = data.body;

console.log(this.serviceTypes,'this.serviceTypes')
console.log(this.claimDetails,'claimDetailsoninitkeeptype')
    })
  }


  closeAddMoreLinesModal() {
    const bodyTag = document.body;
  bodyTag.classList.remove('edit-modal-activated');
    // console.log(this.loadLineIemsFlag ,'this.loadLineIemsFlag ')
    // Assuming this.createdObjects is an array of objects to be removed
    if(this.loadLineIemsFlag === true){
      // console.log("insised to remove object")
const objectsToRemove = this.createdObjects;
 
// Remove pushed objects from this.selectedClaimLineInfo.claimLineDetails
this.selectedClaimLineInfo.claimLineDetails = this.selectedClaimLineInfo?.claimLineDetails?.filter((lineInfo:any) => {
    return !objectsToRemove.some(obj => 
        obj.lineNumber === lineInfo.lineNumber && obj.invoiceNumber === lineInfo.invoiceNumber && obj.statusId === lineInfo.statusId
    );
});
}
    const modelDiv = document.getElementById('addMoreLinesModal');
    if (modelDiv != null) {
        modelDiv.style.display = 'none';
 
    }
    this.createdObjects = [];
    this.unmatchedResult = [];
    this.matchedResults = [];
    this.addNewLineButtonText = "Add New Line" ;
    this.addLineForm.reset();
}
 
  openAddMoreLinesModal(){
    if(!this.claimIsVoid){
    this.arrowSecTwo = false;
      this.section2Collapsed = false;
    // console.log("addmoreline modal is called")
    if (this.selectedClaimLineInfo && this.selectedClaimLineInfo.claimLineDetails) {
      // Loop through each lineInfo and reset properties
      this.selectedClaimLineInfo.claimLineDetails.forEach((lineInfo:any) => {
          lineInfo.selected = false;
      });
  }
  this.selectedClaimLineInfo = [];
  this.addInvoiceData = [];
  this.matchedResults = [];
  this.unmatchedResult = [];
 
    const modelDiv = document.getElementById('addMoreLinesModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
  console.log(this.selectedClaimLineInfo,'selectedClaimLineInfolength')
 
  }



  highlightLineItem(details: any): string {
    // Check if details, lineSource, and detailType exist before accessing their properties
    if (details?.lineSource?.lineSourceCode === 'ANAL' ||
        details?.detailType?.detailTypeCode === 'LABOR' ||
        details?.detailType?.detailTypeCode === 'SERVC') {
      return '#ACE1AF';
    } else if (details?.lineSource?.lineSourceCode === 'DUPL') {
      return '#FFDB58';
    } else {
      return '';
    }
  }
  
 
  

  isRecordMatched(lineInfo: any): boolean {
    // Check if the lineInfo has a special detailTypeCode (FRGHT, TAX, CHRGS)

    if (['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST', 'TOTAL'].includes(lineInfo.detailType.detailTypeCode)) {

      // Return true if any matchedResult has the same detailTypeCode
      return this.matchedResults.some((invoice) =>
       
        invoice.detailTypeCode === lineInfo.detailType.detailTypeCode
    );
    }
  
    // For regular lines, check based on lineNumber and statusId
    const isMatched = this.matchedResults.some((invoice) =>
      parseInt(lineInfo.invoiceLineNumber, 10) === parseInt(invoice.lineNbr, 10) && lineInfo.statusId !== 3
    );
  
    return isMatched;
  }

  public checkRecord(lineInfo: any) {
    const isMatched = this.matchedResults.some((invoice) =>
      parseInt(lineInfo.invoiceLineNumber, 10) === parseInt(invoice.lineNbr, 10) && lineInfo.statusId !== 3
    );
    if (isMatched) {
      // console.log( '............isMatched',isMatched,lineInfo.detailType.detailTypeCode);
      return isMatched
    } else {
      
      const isPresent = this.selectedLineInfo.some((invoice: any) => 
        Object.keys(lineInfo).every(key => lineInfo[key] === invoice[key])
      );
// console.log( '............1234',isPresent,lineInfo.detailType.detailTypeCode);

      return isPresent;
    }

  }
  

  public openModalPriorClaimSummary() {
    const modelDiv = document.getElementById('modalPriorClaimSummary');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public openModalrestock(){
     // Find the address with addressTypeId: 6
  const addressWithType6 = this.claimDetails.claimAddresses.find((address: any) => address.addressTypeId === 6);
  console.log(addressWithType6,'addressWithType6')
    // Check if emailAddress is empty
    if (addressWithType6 && !addressWithType6.emailAddress.trim()) {
     
      this.toasterService.showToast('error', 'Email Id is not available. Please update the email id to Prepare CRM');
      return; // Exit the function if emailAddress is empty
    }
    console.log(this.claimDetails,'this.claimDetails...')
  console.log(this.claimDetails.claimReasonDefinition?.claimReasonCode,'this.claimDetails.claimReasonDefinition?.claimReasonCode')
    // Proceed to check if any line has claimReasonCode "ALL"
    let claimLevelCRM = this.claimDetails.claimReasonDefinition?.claimReasonCode;
    let crmClaimLevel = claimLevelCRM === "ALL" || claimLevelCRM === '';
    
    // if (crmClaimLevel) {
    //   this.toasterService.showToast('error', 'Claim does not have a valid Reason Code, please update the reason code to Prepare CRM.');
    //   return; // Exit the function if claimReasonCode is "ALL" or an empty string
    // }
    let selectedCRM= this.selectedLine;
    let crm = selectedCRM.filter((data: any) => data.selectedLine.claimReasonDefinition.claimReasonCode === "ALL");
  
    if (crm.length > 0) {
      
      this.toasterService.showToast('error', 'Line does not have Reason Code, please update the reason code to Prepare CRM');
     
      return; // Exit the function if any line has claimReasonCode "ALL"
    }

    
    console.log(this.selectedLine, 'lineData');
    console.log(this.selectedLine[0].selectedLine.detailType.detailTypeCode, 'lineDatadetailTypeCode');
    let dtosArray: { claimLineId: any; reasonCode: any; squareFeet: any; quantity: any; productCode: any; lineAmountUsd: any }[] = [];

this.selectedLine.forEach((line: any) => {
  // Only process lines with detailTypeCode === "MERCH"
  if (line.selectedLine.detailType?.detailTypeCode === "MERCH") {
    let obj = {
      claimLineId: line.claimLineId,
      reasonCode: line.selectedLine.claimReasonDefinition.claimReasonCode,
      squareFeet: line.selectedLine.squareFeet,
      quantity: line.selectedLine.quantity,
      productCode: line.selectedLine.productCode,
      lineAmountUsd: line.selectedLine.lineAmountUsd
    };
    dtosArray.push(obj);
  }
});
  
  
    // this.selectedLine.forEach((line: any) => {
      
    //   let obj = {
    //     "claimLineId": line.claimLineId,
    //     "reasonCode": line.selectedLine.claimReasonDefinition.claimReasonCode,
    //     "squareFeet": line.selectedLine.squareFeet,
    //     "quantity": line.selectedLine.quantity,
    //     "productCode": line.selectedLine.productCode,
    //     "lineAmountUsd":line.selectedLine.lineAmountUsd
    //   };
    //   dtosArray.push(obj);
    // });
   console.log(dtosArray,'dtosArray')
   const smartcodeValidatePayload = {
 "claimId":this.claimDetails.claimId,
    "lineId": dtosArray[0]?.claimLineId,//dtosArray.map(dto => dto.claimLineId),
   "reasonCode":dtosArray[0]?.reasonCode,// dtosArray.map(dto => dto.reasonCode),
   "ruleOfImpactArea": 'Issue Credit Memo',
"updateReasonCodeLineItem": false
    }
    console.log(smartcodeValidatePayload,'smartcodeValidatePayload')
    this.claimDetailService.validateSmartCodeRules(smartcodeValidatePayload).subscribe(
      (response) => {
        console.log(response, 'validateReasonData');
        if (response.status === 200) {
         if(response.body.errorMessage){
        
          this.toasterService.showToast('success', response.body.errorMessage);
        }
        }
      },
      (error) => {
     
        this.toasterService.showToast('error', error.error.detail);
    
      }
    );
    const crmPayload = {
      sellingCompanyCode: this.claimDetails.sellingCompany,
      requestLineDetailsDTOS: dtosArray.map(dto => ({
        claimLineId: dto.claimLineId,
        reasonCode: dto.reasonCode,
      })),
    };
  console.log(crmPayload,'crmPayload')
   this.totalLineAmountSum = dtosArray.reduce((sum, dto) => sum + dto.lineAmountUsd, 0);
  console.log(this.totalLineAmountSum,'totalLineAmountSum')
    this.claimDetailService.restockFee(crmPayload).subscribe(
      data => {
        this.restockFeeData = data.body  ; // Adjust according to the response structure
        // Assuming data.body.restockFeePercentage is in percentage form (e.g., 25 for 25%)
        this.editRestockFeeData = parseFloat((this.totalLineAmountSum * (data.body.restockFeePercentage / 100)).toFixed(2));
        this.restockFeePercentage= this.restockFeeData.restockFeePercentage;
        console.log(this.restockFeePercentage, 'mydatadatrestockFeePercentagea');
        console.log(this.restockFeeData, 'mydatadat..a');
        console.log(this.restockFeeData.codeRuleReturnType,'accom')
        if(this.restockFeeData && this.restockFeeData.codeRuleReturnType === 'Accomodation'){
          const modelDiv = document.getElementById('restock-fee');
          if (modelDiv != null) {
            modelDiv.style.display = 'block';
          }
        }else{
          const navigationExtras: NavigationExtras = {
            state: {
              selectedLine: this.selectedLine,
              claimAddresses: this.claimDetails.claimAddresses,
              claimId: this.claimDetails.claimId,
              claimNumber:this.claimDetails.claimNumber,
              customerNumber: this.claimDetails.customerNumber,
              claimDocuments: this.claimDetails.claimDocuments,
             // editRestockFeeData: '',
              restockFeePercentage : ''
            }
          };
       console.log(navigationExtras,'navigationExtras')
        
            
            this.router.navigate(['/prepare-crm'], navigationExtras);
          
        }
      },
      error => {
        console.error('Error:', error);
      }
    );
    

  }

  public openModalCreditMemo(){
   const modelDiv = document.getElementById('CreditMemoModal');
   if (modelDiv != null) {
     modelDiv.style.display = 'block';
   }
 }

  public prepCRMmodal(){
    
    console.log(this.claimDetails,'claimDetails')
    console.log(this.selectedLine,'selectedLine')
    // const modelDiv = document.getElementById('prepare-CRM');
    // if (modelDiv != null) {
    //   modelDiv.style.display = 'block';
    // }
    
   //this.isCRMVisible = true;
  
   const navigationExtras: NavigationExtras = {
     state: {
       selectedLine: this.selectedLine,
       claimAddresses: this.claimDetails.claimAddresses,
       claimId: this.claimDetails.claimId,
       claimNumber:this.claimDetails.claimNumber,
       customerNumber: this.claimDetails.customerNumber,
       claimDocuments: this.claimDetails.claimDocuments,
       editRestockFeeData: this.editRestockFeeData,
       restockFeePercentage : this.restockFeePercentage
     }
   };
console.log(navigationExtras,'navigationExtras')
 
     
     this.router.navigate(['/prepare-crm'], navigationExtras);
   
  }

  public openModalSample(){
    const modelDiv = document.getElementById('staticBackdropinvoice');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public openAddAssociateDocModal(){
    const modelDiv = document.getElementById('addAssociateDoc');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public requestInspection(){
    // const modelDiv = document.getElementById('request-Inspection');
    // if (modelDiv != null) {
    //   modelDiv.style.display = 'block';
    // }
    this.router.navigate(['/claim-vendor-map']);
  }

  public openClaimVendorMap(){
    this.router.navigate(['/claim-vendor-map']);
  }

  public CloseModalPriorSummary() {
    const modelDiv = document.getElementById('modalPriorClaimSummary');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  getPriorClaims(event: Event,doc: any) {
    event.stopPropagation();
    // this.openModalPriorClaimSummary();
    const modelDiv = document.getElementById('modalPriorClaimSummary');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.priorClaimloading = true;
    this.claimDetailService.getPriorClaimForInvoice(
      doc.documentNumber,
      doc.documentType.documentTypeId,
      this.claimDetails.claimId
    ).subscribe(data => {
      this.priorClaimTableData = data;
      console.log(this.priorClaimTableData ,'this.priorClaimTableData.. ')
      this.priorClaimloading = false;
      // let claimstatusFromPrior = this.priorClaimTableData.claimStatusId
      // this.claimDetailService.getClaimStatusById(this.priorClaimTableData[0].claimStatusId).subscribe(data=>{
      //  this.claimstatusPriordata = data
      // })
    },
    (error) => {
      this.priorClaimloading = false;
  })
 
  }

  addNewLine(){
    console.log(this.selectedClaimLineInfo,'addnewline')
    const addNewPayload = {
      "createdByUserId": this.selectedClaimLineInfo.createdByUserId,
      "modifiedByUserId": this.selectedClaimLineInfo.modifiedByUserId,
      "claimDocumentId": this.selectedClaimLineInfo.claimDocumentId,
      "documentNumber":  this.selectedClaimLineInfo.documentNumber,
      "exchangeRate": this.selectedClaimLineInfo.exchangeRate,
      "orderNumber": this.selectedClaimLineInfo.orderNumber,
      "purchaseOrderNumber": this.selectedClaimLineInfo.purchaseOrderNumber,
      "billofLading": this.selectedClaimLineInfo.billofLading,
      "masterBillofLading": this.selectedClaimLineInfo.masterBillofLading,
      "documentTypeCode": this.selectedClaimLineInfo.documentType.documentTypeCode,
      "claimReasonCode": this.selectedClaimLineInfo.claimReasonDefinition.claimReasonCode,
      "claimLineDetailDTOS": [
          {
              "createdByUserId":  this.selectedClaimLineInfo.createdByUserId,
              "modifiedByUserId":  this.selectedClaimLineInfo.modifiedByUserId,
              "unitOfMeasureCode": this.addLineForm.value.uom,
              "rcsCode": this.selectedClaimLineInfo.claimLineDetails[0].rcsCodes.rcsCode,
              "lineNumber": this.selectedClaimLineInfo.claimLineDetails[0].lineNumber,
              "rollNumber": this.addLineForm.value.roll,
              "styleNumber": this.addLineForm.value.style,
              "colorNumber": this.addLineForm.value.color,
              "claimReasonCode": this.addLineForm.value.reasonCode,
              "grade": this.addLineForm.value.grade,
              "lineAmountUsd": Number(this.addLineForm.value.amount),
              "lineAmountForeign": Number(this.addLineForm.value.amount),
              "unitPriceUsd": Number(this.addLineForm.value.price),
              "unitPriceForeign": Number(this.addLineForm.value.price),
              "pricingCurrencyCode": this.selectedClaimLineInfo.claimLineDetails[0].pricingCurrencyCode,
              "currencyCodeDesignation":  this.selectedClaimLineInfo.claimLineDetails[0].currencyCodeDesignation,
              "currencyCodeName":  this.selectedClaimLineInfo.claimLineDetails[0].currencyCodeName,
              "exchangeRate": this.selectedClaimLineInfo.claimLineDetails[0].exchangeRate,
              "dyeLot": this.addLineForm.value.dyeLot,
              "quantity": this.addLineForm.value.qty,
              "sellingCompany": this.selectedClaimLineInfo.claimLineDetails[0].sellingCompany,
              "productCode":this.selectedClaimLineInfo.claimLineDetails[0].productCode,
              "inventoryStyle": this.selectedClaimLineInfo.claimLineDetails[0].inventoryStyle,
              "inventoryColor": this.selectedClaimLineInfo.claimLineDetails[0].inventoryColor,
              "manufacturingPlant": this.selectedClaimLineInfo.claimLineDetails[0].manufacturingPlant,
              "detailTypeCode": 'MERCH',
              "lineSourceCode": 'ANAL',
             
              "lineSourceReference": this.selectedClaimLineInfo.claimLineDetails[0].lineSourceReference,
              "lineAddReasonCode": this.addLineForm.value.lineAddreason,
              "vendorId": this.selectedClaimLineInfo.claimLineDetails[0].vendorId,
             // ...(this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords.length > 0 && {
                  "claimDetailRecordDTOS": [
                      {
                          "createdByUserId":this.selectedClaimLineInfo.createdByUserId,
                          "modifiedByUserId": this.selectedClaimLineInfo.modifiedByUserId,
                          "claimDetailRecordId":0,//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].claimDetailRecordId,
                          "detailRecordTypeCode": '',//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].detailRecordType.detailRecordTypeCode,
                          "widthInFeet": Number(this.addLineForm.value.widthinfeet),
                          "widthInInches":  Number(this.addLineForm.value.widthininch)|| 0,
                          "lengthInFeet":Number(this.addLineForm.value.lengthinfeet)||0,
                          "lengthInInches":Number(this.addLineForm.value.lengthininch) ||0,
                         "quantity":0,//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].quantity,
                          "unitOfMeasure": '',//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].unitOfMeasure,
                          "amountUsd": 0,//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].amountUsd,
                          "amountForeign": 0,//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].amountForeign,
                          "unitPriceUsd": 0,//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].unitPriceUsd,
                          "unitPriceForeign":0,//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].unitPriceForeign,
                         "exchangeRate":0//this.selectedClaimLineInfo.claimLineDetails[0].claimDetailRecords[0].exchangeRate,
                      }
                  ]
             // })
          }
      ]
  };
  console.log(addNewPayload,'addNewPayload')
    this.claimDetailService.addMoreLineItems(addNewPayload).subscribe(data=> {
      this.refreshClaimDetails.emit();
      // console.log('checking...........'+data)
      
      const modelDiv = document.getElementById('addMoreLinesModal');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }
    
    this.toasterService.showToast('success', 'Line items added succesfully');
    })
    console.log(this.payloadObject,'payloadObject');
    this.addLineForm.reset();
  }

  toggleSectionTwo() {
    const bodyTag = document.body;
    bodyTag.classList.add('edit-modal-activated');
    this.arrowSecTwo = !this.arrowSecTwo;
    this.addNewLineButtonText = this.addNewLineButtonText === "Add New Line" ? "Add More Lines" : "Add New Line";
  }
addNewLineToggle(){
  if(this.addLineForm?.get("lineAddreason")?.value && this.addLineForm?.get("lineAddreason")?.value?.length>0){
  return false;
  }else{
    return true;
  }
  
  
}
getCreditMemos(event: Event,doc: any) {
  event.stopPropagation();
}

navigateToClaim(claimId: string) {
  // Use Angular router to navigate
  this.router.navigate(['/claim', claimId]);
}


  // Function to toggle table visibility
  toggleTemplateVisibility() {
    // console.log('function got called')
    this.showTable = !this.showTable;
    // console.log('funct', this.showTable)
  }
  public openClaimAnalyzeModal(){
    this.fetchResolutionsAndPhotos();
  //   const modelDiv = document.getElementById('ClaimAnalyzeModal');
  //   if (modelDiv != null) {
  //     modelDiv.style.display = 'block';
  // }
}

  public fetchResolutionsAndPhotos(){
    let reasonId = this.selectedLineForAnalyze.claimReasonDefinition.claimReasonId
    this.smartCodeService.fetchResolutionsAndPhotos(reasonId).subscribe(data=>{
      this.resolutionsAndPhotosData = data.body
    })
  }
 
  public taxAndFreight(invoiceNumber: any, invoiceDate: any) {
    let date = invoiceDate.split('T')[0];
    return this.intiateClaimService.getInvoiceHeader(this.claimDetails.customerNumber, invoiceNumber, date).pipe(
      map(data => {
        if (data.body && data.body.length > 0) {
          return [data.body[0]]; // Store the first item as an array
        } else {
          return [];
        }
      }),
      catchError(error => {
        console.error('Error fetching tax and freight data:', error);
        return of([]); // Return an empty array on error
      })
    );
  }
  
  public careSurCharge(invoiceNumber: any, invoiceDate: any) {
    let date = invoiceDate.split('T')[0];
    return this.intiateClaimService.getInvoiceCharges(this.claimDetails.customerNumber, invoiceNumber, date).pipe(
      map(data => {
        if (data.body && data.body.length > 0) {
          return data.body; // Store the first item as an array
        } else {
          return [];
        }
      }),
      catchError(error => {
        console.error('Error fetching care surcharge data:', error);
        return of([]); // Return an empty array on error
      })
    );
  }

  public disableRgaBtn(){
    if(this.selectedLine.length == 0){
      return true;
    }else{
      let selectedRGA = this.selectedLine
      let rga = selectedRGA.filter((data:any)=>data.selectedLine.claimReasonDefinition.claimReasonCode == "ALL")
      if(rga.length > 0){
    return true;  
      }else{
        return false;
      }
    }
  }
  // fetchServiceToKeepType(): Observable<any> {
  //   return this.claimDetailService.fetchServiceToKeepType().pipe(
  //     tap(data => console.log(data, 'ServiceToKeepType')),
  //     map(response => response.body)
  //   );
  // }
  
  fetchDocumentsByClaimId(claimId: number) {
   this.claimDetailService.fetchDocumentsByClaimId(claimId).subscribe(data=>{
    this.fetchedDocumnetsByClaimId = data.body.filter((document: any) => document.status !== 'Deleted');
    console.log(data.body,'documetsData')

   // this.fetchedDocumnetsByClaimId =data.body;
   // this.claimDocuments.push(...this.fetchedDocumnetsByClaimId);

   })
  }
  
  fetchCrmDetailsByClaimIdAndCrmNumber(claimId: number, crmNumber: number): Observable<any> {
    return this.claimDetailService.fetchCrmDetailsByClaimIdAndCrmNumber(claimId, crmNumber).pipe(
      tap(data => console.log(data, 'crmDetailsByClaimIdAndCrmNumber')),
      map(response => response.body)
    );
  }
 
  
  // getTotalAmount(): number {
  //   let totalAmount = 0;
  //   if (this.selectedLine && Array.isArray(this.selectedLine)) {
  //     for (const line of this.selectedLine) {
  //       if (line.selectedLine && typeof line.selectedLine.lineAmountUsd === 'number') {
  //         totalAmount += line.selectedLine.lineAmountUsd;
  //       }
  //     }
  //   }
  //   return totalAmount;
  // }
 

getTotalAmount(): number {
  let totalAmount = 0;
  if (this.selectedLine && Array.isArray(this.selectedLine)) {
    for (const line of this.selectedLine) {
      if (line.selectedLine && typeof line.selectedLine.lineAmountUsd === 'number') {
        totalAmount += line.selectedLine.lineAmountUsd;
      }
    }
  }
  this.claimLevelTotalAmount = totalAmount; // Store the total amount in claimLevelTotalAmount
  //console.log('Claim Level Total Amount:', this.claimLevelTotalAmount); // Log the total amount to the console
  return totalAmount;
}

  

  handleSpecialInstruction(option: string) {
    switch (option) {
      case 'EndUser':
        this.showUserName = true;
        this.showInvoice = false;
        this.showCustomer = false;
        break;
      case 'CustomerChargeback':
        this.showUserName = false;
        this.showInvoice = false;
        this.showCustomer = true;
        break;
      case 'InvoicePOOrder':
        this.showUserName = false;
        this.showInvoice = true;
        this.showCustomer = false;
        break;
      default:
        this.showUserName = false;
        this.showInvoice = false;
        this.showCustomer = false;
        break;
    }
    console.log('showUserName:', this.showUserName);
    console.log('showInvoice:', this.showInvoice);
    console.log('showCustomer:', this.showCustomer);
  }

  get selectedRgaValue(): string {
    return this.selectedRga === 'No RGA' ? '0' : this.selectedRga;
  }

  get selectedDebitValue(): string {
    return this.selectedDebit === 'No Debit' ? '0' : this.selectedDebit;
  }

  // Example method to log values based on your requirement
  logSelections() {
    console.log('Associated RGA Number:', this.associatedRgaNumber);
  }
 

  toggleEmailAddress(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    this.emailAddress = inputElement.checked;
    console.log(this.emailAddress);
  }
  updateApplyMethod(method: string) {
    this.autoApply = method === 'autoApply';
    this.doNotApply = method === 'doNotApply';
    this.manualApply = method === 'manualApply';
    console.log({
      autoApply: this.autoApply,
      doNotApply: this.doNotApply,
      manualApply: this.manualApply
    });
  }
  updateCheckboxState(option: string) {
    this.leaveClaimOpen = option === 'leaveClaimOpen';
    this.closeClaimWithTasks = option === 'closeClaimWithTasks';
    this.leaveInspectionOpen = option === 'leaveInspectionOpen';
    this.removeDispute = option === 'removeDispute';

    console.log({
      leaveClaimOpen: this.leaveClaimOpen,
      closeClaimWithTasks: this.closeClaimWithTasks,
      leaveInspectionOpen: this.leaveInspectionOpen,
      removeDispute: this.removeDispute
    });
  }
 

  toggleCheckboxState(option: string) {
    if (option === 'atkAttempted') {
      this.atkAttempted = !this.atkAttempted;
    } else if (option === 'atkAccepted') {
      this.atkAccepted = !this.atkAccepted;
    } else if (option === 'stkAttempted') {
      this.stkAttempted = !this.stkAttempted;
    } else if (option === 'stkAccepted') {
      this.stkAccepted = !this.stkAccepted;
    }

    console.log({
      atkAttempted: this.atkAttempted,
      atkAccepted: this.atkAccepted,
      stkAttempted: this.stkAttempted,
      stkAccepted: this.stkAccepted
    });
  }

  saveCrmDraft(param:string){
    
    const saveCrmDraftPayload = {
      "createdByUserId": Number(localStorage.getItem("userId")),
      "modifiedByUserId": Number(localStorage.getItem("userId")),
      "claimCrmHeaderId": 0,
      "crmStatusCode": param,
      "claimId": this.claimDetails.claimId,
      "claimCustomerNumber": this.claimDetails.customerNumber,
      "creditCustomerNumber": "",
      "claimReasonCode": this.selectedLine[0].claimReasonCode,
      "amountUsd":this.claimLevelTotalAmount || 0,
      "amountForeign": this.selectedLine[0].selectedLine.lineAmountForeign,
      "preparedByUserId": Number(localStorage.getItem("userId")),
      "currentUserId": Number(localStorage.getItem("userId")),
      "issuedByUserId": Number(localStorage.getItem("userId")),
      "associatedRgaNumber": 0,
      "emailAddress": "",
      "autoApply": this.autoApply,
      "leaveClaimOpen": this.leaveClaimOpen,
      "associatedCcbNumber": "",
      "associatedDebitNumber": "",
      "generalLedgerNumber": "",
      "serviceToKeepTypeId": this.selectedServiceTypeId,
      "claimCrmLineDTO": this.selectedLine.map((line:any) => ({
        "claimCrmLineId": 0,
        "crmLineNumber": line.lineNumber,
        "claimLineId": line.claimLineId,
        "rollNumber": line.selectedLine.rollNumber,
        "styleNumber": line.selectedLine.styleNumber,
        "colorNumber": line.selectedLine.colorNumber,
        "quantity": line.selectedLine.quantity,
        "issuedAmount": 0,
        "widthInFeet": line.selectedLine.widthInFeet || 0,
        "lengthInFeet": line.selectedLine.lengthInFeet || 0,
        "amountUsd": line.selectedLine.lineAmountUsd,
        "amountForeign": line.selectedLine.lineAmountForeign
      }))
    };
    
  console.log(saveCrmDraftPayload,'saveCrmDraftPayload')
  this.claimDetailService.crmHoldOrIssue(saveCrmDraftPayload).subscribe(
    (data) => {
      if (data.status === 200) {
        this.refreshClaimDetails.emit();
       
        this.toasterService.showToast('success', 'CRM saved As Draft Successfully');
        this.closeCrmModal();
        this.closeRestockFeeModal();
      }
    },
    (error) => {
      console.error('Error occurred:', error);
      this.refreshClaimDetails.emit();
     
      this.toasterService.showToast('error', 'Failed To Save As Draft');
      this.closeCrmModal();
      this.closeRestockFeeModal();
    }
  );
  
}
closeCrmModal(){
  const modelDiv = document.getElementById('prepare-CRM');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
}
closeRestockFeeModal(){
  const modelDiv = document.getElementById('restock-fee');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
}
public navigateTomaintDispo(document:any){
  // console.log(this.claimDetails.endUserInformation, '..............invoicedispo')
  // console.log('Document Type Code:', document.documentType.documentTypeCode);
  if(document.documentType.documentTypeCode == 'DSP'){
    const navigationExtras: NavigationExtras = {
      state: {
        selectedLine: this.selectedLineItem,
        claimAddresses: this.claimDetails.claimAddresses,
        claimId: this.claimDetails.claimId,
        claimNumber:this.claimDetails.claimNumber,
        customerNumber: this.claimDetails.customerNumber,
        claimDocuments: this.claimDetails.claimDocuments,
        endUserInfo:this.claimDetails.endUserInformation,
        documentNumber:document.documentNumber,
       
      }
    };
  
      
      this.router.navigate(['/maintain-disposition'], navigationExtras);
  }
 
}
public onassociatedtoGlobalaccountChange(field: string) {
  if (field === 'docType') {
    // Update the selectedDocType based on the form control value
    this.selectedDocType = this.addInvoiceForm.get('docType')?.value;
    console.log('Selected docTypeId:', this.selectedDocType);
  } 
}
handleDocumentClick(event: MouseEvent,document: any): void {
  event.preventDefault();
  if (document?.documentTypeCode === 'RET') {
     // this.navigateToNewViewRGA(document);
     this.claimDetailService.fetchRgaDetailsByClaimIdAndRgaNumber(
      this.claimDetails.claimId, document.documentNumber
    ).subscribe(data => {
      const rgaDetails = data.body;
      localStorage.setItem('setViewEditDAta', JSON.stringify(rgaDetails));
      // console.log(CrmDetails.currentUserId.toString(),'currentID')
      // console.log(CrmDetails.preparedByUserId.toString(),'preparedByUserId')
      // console.log(localStorage.getItem('userId'),'LOGEDIN')
      const curentUserID = rgaDetails.currentUserId.toString();
      const preparedByUserId = rgaDetails.preparedByUserId.toString();
      const loggedUserID = localStorage.getItem('userId');

      console.log(curentUserID, 'curentUserID..');
      console.log(preparedByUserId, 'preparedByUserId..');
      console.log(loggedUserID, 'loggedUserID..');

      if (loggedUserID === preparedByUserId) {
        // this.navigateToNewViewRGA(document);
        const navigationExtras: NavigationExtras = {
          state: {
            claimAddresses: this.claimDetails.claimAddresses,
            claimId: this.claimDetails.claimId,
            claimNumber: this.claimDetails.claimNumber,
            rgaNumber: document.documentNumber,
            rgaHeaderId: document.headerId,
            rgaStatus: document.status,
            claimDocuments: this.claimDetails.claimDocuments,
            customerNumber:this.claimDetails.customerNumber,
            rdcLocationCode: localStorage.getItem('rdcLocationCode'),
               claimAreaId:this.claimDetails.claimAreaId,
            division:this.claimDetails.division,
            region:this.claimDetails.region,
            sellingCompany:this.claimDetails.sellingCompany
          }
        };
        console.log(navigationExtras, "navigationExtras");
        
        this.router.navigate(['/edit-rga'], navigationExtras);
      }
      else if(loggedUserID !==curentUserID){
        const navigationExtras: NavigationExtras = {
          state: {
            claimAddresses: this.claimDetails.claimAddresses,
            claimId: this.claimDetails.claimId,
            claimNumber: this.claimDetails.claimNumber,
            rgaNumber: document.documentNumber,
            rgaHeaderId: document.headerId,
            rgaStatus: document.status,
            claimDocuments: this.claimDetails.claimDocuments,
            customerNumber:this.claimDetails.customerNumber,
            rdcLocationCode: localStorage.getItem('rdcLocationCode'),
            claimAreaId:this.claimDetails.claimAreaId,
            division:this.claimDetails.division,
            region:this.claimDetails.region,
            sellingCompany:this.claimDetails.sellingCompany
          }
        };
        
        this.router.navigate(['/view-rga'], navigationExtras);
      }
      else if(loggedUserID === curentUserID && loggedUserID !== preparedByUserId){
        this.navigateToNewViewRGA(document);
      }
    })
  }else 
  if(document.documentTypeCode == 'DSP'){
    const navigationExtras: NavigationExtras = {
      state: {
        selectedLine: this.selectedLineItem,
        claimAddresses: this.claimDetails.claimAddresses,
        claimId: this.claimDetails.claimId,
        claimNumber:this.claimDetails.claimNumber,
        customerNumber: this.claimDetails.customerNumber,
        claimDocuments: this.claimDetails.claimDocuments,
        endUserInfo:this.claimDetails.endUserInformation,
        documentNumber:document.documentNumber
        // sellingCompanyDetails:this.claimDetails. 
      }
    };
  
      
      this.router.navigate(['/maintain-disposition'], navigationExtras);
  }
  else if (document?.documentTypeCode === 'CRM') {
    this.claimDetailService.fetchCrmDetailsByClaimIdAndCrmNumber(
      this.claimDetails.claimId, document.documentNumber
    ).subscribe(data => {
      const CrmDetails = data.body;
      localStorage.setItem('setViewCrmEditDAta', JSON.stringify(CrmDetails));
     // console.log(CrmDetails.currentUserId.toString(),'currentID')
     // console.log(CrmDetails.preparedByUserId.toString(),'preparedByUserId')
     // console.log(localStorage.getItem('userId'),'LOGEDIN')
      const curentUserID = CrmDetails.currentUserId.toString();
      const preparedByUserId =CrmDetails.preparedByUserId.toString();
      const loggedUserID = localStorage.getItem('userId');
     
      console.log(curentUserID, 'curentUserID..');
      console.log(preparedByUserId, 'preparedByUserId..');
      console.log(loggedUserID, 'loggedUserID..');
  
      if (loggedUserID === preparedByUserId) {
        console.log('come inside')
        const navigationExtras: NavigationExtras = {
          state: {
            claimAddresses: this.claimDetails.claimAddresses,
            claimId: this.claimDetails.claimId,
            claimNumber: this.claimDetails.claimNumber,
            crmNumber: document.documentNumber,
            crmHeaderId: document.crmHeaderId,
            crmStatus: document.status,
            customerNumber:this.claimDetails.customerNumber,
            claimDocuments: this.claimDetails.claimDocuments
          }
        };
        this.router.navigate(['/edit-crm'], navigationExtras);
      } else if(loggedUserID !==curentUserID){
        const navigationExtras: NavigationExtras = {
          state: {
            claimAddresses: this.claimDetails.claimAddresses,
            claimId: this.claimDetails.claimId,
            claimNumber: this.claimDetails.claimNumber,
            crmNumber: document.documentNumber,
            crmHeaderId: document.crmHeaderId,
            crmStatus: document.status,
            customerNumber:this.claimDetails.customerNumber,
            claimDocuments: this.claimDetails.claimDocuments
          }
        };
        this.router.navigate(['/view-crm'], navigationExtras);
       }
       // else if(loggedUserID === curentUserID || loggedUserID !== preparedByUserId){
       
      //   this.navigateToNewViewCRM(document);
       
      // }
       else if(loggedUserID === curentUserID && loggedUserID !== preparedByUserId){
        this.navigateToNewViewCRM(document);
       }
       else {
        console.log('come outside')
        this.navigateToNewViewCRM(document);
      }
    });
  }
  
   
  
}

navigateToNewViewRGA(document: any): void {
  const navigationExtras: NavigationExtras = {
    state: {
     
    
      claimAddresses: this.claimDetails.claimAddresses,
      claimId: this.claimDetails.claimId,
      claimNumber:this.claimDetails.claimNumber,
      rgaNumber : document.documentNumber,
      rgaHeaderId: document.headerId,
      rgaStatus:document.status,
      claimDocuments: this.claimDetails.claimDocuments,
      customerNumber:this.claimDetails.customerNumber,
      rdcLocationCode : localStorage.getItem('rdcLocationCode'),
      claimAreaId:this.claimDetails.claimAreaId,
      division:this.claimDetails.division,
      region:this.claimDetails.region,
      sellingCompany:this.claimDetails.sellingCompany

    
      
    }
  };
  console.log(this.claimDetails.claimAddresses,'claimAddresses')
  console.log(this.claimDetails.claimId,'claimId')
  console.log(this.claimDetails.claimNumber,'claimNumber')
  console.log(document.documentNumber,'documentNumber')
  console.log(navigationExtras,'navigationExtras')
 this.router.navigate(['/maintain-rga'], navigationExtras);

}
navigateToNewViewCRM(document: any): void {
  console.log(document,'document')
  const navigationExtras: NavigationExtras = {
    state: {
      
    
      claimAddresses: this.claimDetails.claimAddresses,
      claimId: this.claimDetails.claimId,
      claimNumber:this.claimDetails.claimNumber,
      crmNumber : document.documentNumber,
      crmHeaderId: document.crmHeaderId,
      crmStatus:document.status,
      claimDocuments: this.claimDetails.claimDocuments,
      customerNumber:this.claimDetails.customerNumber,
     
    //  rdcLocationCode : localStorage.getItem('rdcLocationCode')

    
      
    }
  };
  console.log(this.claimDetails.claimAddresses,'claimAddresses')
  console.log(this.claimDetails.claimId,'claimId')
  console.log(this.claimDetails.claimNumber,'claimNumber')
  console.log(document.documentNumber,'crmNumber')
  console.log(navigationExtras,'navigationExtras')
 this.router.navigate(['/manager-crm'], navigationExtras);

}
validateInput(event: KeyboardEvent) {
  const charCode = event.charCode;
  const charStr = String.fromCharCode(charCode);

  // Allow only numbers (0-9)
  if (!charStr.match(/^[0-9]$/)) {
    event.preventDefault();
  }
}

updateAmount() {
  // Check if the amount was manually edited
  if (!this.amountEdited) {
    const qty = this.addLineForm.get('qty')?.value || 0;
    const price = this.addLineForm.get('price')?.value || 0;
    const amount = qty * price;
    this.addLineForm.get('amount')?.setValue(amount);
  }
}

// Call this method when the amount field is manually edited
onAmountChange() {
  this.amountEdited = true;
}

// Call this method when the qty or price fields are changed
onQtyOrPriceChange() {
  this.amountEdited = false;  // Reset the manual edit flag
  this.updateAmount();        // Recalculate the amount
}

updateRestockFee(newPercentage: number): number {  
  this.editRestockFeeData = parseFloat((this.totalLineAmountSum * (newPercentage / 100)).toFixed(2));
  return this.editRestockFeeData;
}
 
filterClaims() {
  const searchTextLower = this.codeSearch.toLowerCase();
  this.filteredClaimList = this.claimList.filter((claim: any) => {
    return claim.claimReasonCode.toLowerCase().includes(searchTextLower) ||
           claim.claimReasonDescription.toLowerCase().includes(searchTextLower);
  });
}

onSearchInput(event: Event) {
  const inputElement = event.target as HTMLInputElement;
  this.codeSearch = inputElement.value; 
  console.log('codeSearch:', this.codeSearch);
  this.filterClaims(); // This triggers the filter based on the input
}
// openAddGenricLinesModal(action?: string, document?: any){
//   this.genricAction = action ;
//   this.genricLineDocId =document.claimDocumentId;
//   console.log(this.genricAction,'action')
//   console.log(this.genricLineDocId,'documentgenric')
//   this.getAllClaimLineAddReason();
//   const modelDiv = document?.getElementById('addGenricLines');
//   if (modelDiv != null) {
//     modelDiv.style.display = 'block';
//   }
// }
openAddGenricLinesModal(action?: string, customDocument?: any) {
  this.genricAction = action;
  this.genricLineDocId = customDocument?.claimDocumentId;

  console.log(this.genricAction, 'action');
  console.log(this.genricLineDocId, 'documentgenric');

  this.getAllClaimLineAddReason();
  const bodyTag = document.body;
    bodyTag.classList.add('edit-modal-activated');
  const modalDiv = document.getElementById('addGenricLines');
  if (modalDiv != null) {
    modalDiv.style.display = 'block';
  } else {
    console.error('Element with ID "addGenricLines" not found.');
  }
}


closeAddGenricLinesModal(){
  const bodyTag = document.body;
  bodyTag.classList.remove('edit-modal-activated');
  this.addLineForm.reset();
  const modelDiv = document.getElementById('addGenricLines');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}
addGenricLine() {
  console.log(this.genricAction, 'action');
  console.log(this.genricLineDocId, 'documentgenric');
  console.log(this.selectedClaimLineInfo, 'addnewline');

  const addNewPayload = {
    "createdByUserId": Number(localStorage.getItem('userId')),
    "modifiedByUserId": Number(localStorage.getItem('userId')),
    "claimDocumentId": this.genricLineDocId || 0,
    "documentNumber": "",
    "exchangeRate": 1,
    "orderNumber": "",
    "purchaseOrderNumber": "",
    "billofLading": "",
    "masterBillofLading": "",
    "documentTypeCode": "",
    "claimReasonCode":"" ,
    "clmId": this.claimId,
    "claimLineDetailDTOS": [
        {
          "createdByUserId": Number(localStorage.getItem('userId')),
          "modifiedByUserId": Number(localStorage.getItem('userId')),
            "unitOfMeasureCode": this.addLineForm.value.uom,
            "rcsCode": '',
            "invoiceLineNumber":'',
           "lineNumber": this.maxLineNumber + 1,
            "rollNumber": this.addLineForm.value.roll,
            "styleNumber": this.addLineForm.value.style,
            "colorNumber": this.addLineForm.value.color,
            "claimReasonCode": this.addLineForm.value.reasonCode,
            "grade": this.addLineForm.value.grade,
            "lineAmountUsd": Number(this.addLineForm.value.amount),
            "lineAmountForeign": Number(this.addLineForm.value.amount),
            "unitPriceUsd": Number(this.addLineForm.value.price),
            "unitPriceForeign": Number(this.addLineForm.value.price),
            "pricingCurrencyCode": '',
            "currencyCodeDesignation":  '',
            "currencyCodeName":  '',
            "exchangeRate": 1,
            "dyeLot": this.addLineForm.value.dyeLot,
            "quantity": this.addLineForm.value.qty,
            "sellingCompany": '',
            "productCode":'',
            "inventoryStyle": '',
            "inventoryColor": '',
            "manufacturingPlant": '',
            "detailTypeCode": 'MERCH',
            "lineSourceCode": 'ANAL',
           
            "lineSourceReference":'',
            "lineAddReasonCode": this.addLineForm.value.lineAddreason,
            "vendorId": '',
          
                "claimDetailRecordDTOS": [
                    {
                       "createdByUserId": Number(localStorage.getItem('userId')),
                        "modifiedByUserId": Number(localStorage.getItem('userId')),
                        "claimDetailRecordId":0,
                        "detailRecordTypeCode": '',
                        "widthInFeet": Number(this.addLineForm.value.widthinfeet)|| 0,
                        "widthInInches":  Number(this.addLineForm.value.widthininch)|| 0,
                        "lengthInFeet":Number(this.addLineForm.value.lengthinfeet)||0,
                        "lengthInInches":Number(this.addLineForm.value.lengthininch) ||0,
                       "quantity":0,
                        "unitOfMeasure": '',
                        "amountUsd": 0,
                        "amountForeign": 0,
                        "unitPriceUsd": 0,
                        "unitPriceForeign":0,
                       "exchangeRate":0//
                    }
                ]
          
        }
    ]
  };

  console.log(addNewPayload, 'addNewPayload');
  if (this.genricAction === 'addGenericLine') {
    if (this.genricLineDocId) {
      // If genricLineDocId is present, use it for claimDocumentId
      addNewPayload.claimDocumentId = this.genricLineDocId; // Ensure it has the ID
      this.claimDetailService.addMoreLineItems(addNewPayload).subscribe(
        data => {
          this.refreshClaimDetails.emit();
          this.closeAddGenricLinesModal();
          this.toasterService.showToast('success', 'Line items added successfully');
        },
        error => {
          console.error('Error adding more line items:', error); // Log error to console
          this.toasterService.showToast('error', 'Failed to add line items.'); // Show error message
        }
      );
    } 
  } else {
      // If genricLineDocId is not present, pass an empty value
      addNewPayload.claimDocumentId = 0; // Explicitly set to zero
      this.claimDetailService.addGenericDocument(addNewPayload).subscribe(
        data => {
          this.refreshClaimDetails.emit();
          this.closeAddGenricLinesModal();
          this.toasterService.showToast('success', 'Generic document added successfully');
        },
        error => {
          console.error('Error adding generic document:', error); // Log error to console
          this.toasterService.showToast('error', 'Failed to add generic document.'); // Show error message
        }
      );
    // Handle the case where genricAction is undefined or not equal to 'addGenericLine'
    //console.error('genricAction is undefined or not valid'); // Optional logging
    //this.toasterService.showToast('error', 'Invalid action or missing data.');
  }
  
  // Reset form and state
  this.addLineForm.reset();
  this.genricAction = '';
  this.genricLineDocId = 0;
  
}

// getFilteredDocumentsCount(): number {
//   //console.log('777fun got called')
//   let count = 0;
  
//   // Initialize dummyDocument to false at the start
//   this.dummyDocument = false;

//   if (this.claimDetails?.claimDocuments) {
//     count = this.claimDetails.claimDocuments.filter(doc => {
//       // Check if the document meets the criteria
//       if (doc.documentType.documentTypeCode === 'INV' && doc.statusId !== 3) {
//         // Check if documentNumber is "7777777" and set dummyDocument to true
//         if (doc.documentNumber === "7777777") {
//           this.dummyDocument = true;
//         }
//         return true; // Include this document in the count
//       }
//       return false; // Exclude this document
//     }).length;
//   }

//   return count;
// }
getFilteredDocumentsCount(): number {
  // Initialize count and set dummyDocument to false initially
  let count = 0;
  this.dummyDocument = false;

  if (this.claimDetails?.claimDocuments) {
    // Filter out the documents based on the criteria
    const filteredDocuments = this.claimDetails.claimDocuments.filter(doc => {
      return doc.documentType.documentTypeCode === 'INV' && doc.statusId !== 3;
    });

    // Check if "7777777" exists in the filtered documents
    const hasDummyDocument = filteredDocuments.some(doc => doc.documentNumber === "7777777");
    
  
  if (hasDummyDocument) {
      this.dummyDocument = true; // Only set to true if "7777777" is the only document
    }

    // Return the count of filtered documents
    count = filteredDocuments.length;
  }

  return count;
}

handleAddMoreLinesClick(event: Event, document: any) {
 
  if (document.documentNumber === "7777777") {
    console.log('Calling openAddGenricLinesModal');
    this.openAddGenricLinesModal('addGenericLine', document);
  } else {
    console.log('Calling setLineInfo');
    this.setLineInfo(event, document);
  }
}
}
