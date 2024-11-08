import {
  AfterViewInit,
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  OnInit,
  Output,
  ViewChild,
  ViewChildren,
} from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormControlName,
  FormGroup,
  Validators,
} from '@angular/forms';
// import { Store } from '@ngrx/store';
import { Observable, fromEvent, merge, debounceTime, tap, concatMap } from 'rxjs';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { GenericValidator } from 'src/app/utils/generic-validator';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';

import {
  ClaimRequest,
  priorClaimPayload,
} from 'src/app/interfaces/claim-dues.model';
// import {
//   ClaimCategoryDetails,
//   CustomerDetails,
//   CustomerStoreNumbersResult,
//   ReasonCodeByCategory,
// } from 'src/app/interfaces/customer.model';
import { Router } from '@angular/router';
import { TableComponent } from 'src/app/shared/table/table.component';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MsalService } from '@azure/msal-angular';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { forkJoin, of } from 'rxjs';
import {  catchError, map } from 'rxjs/operators';
import { ToasterComponent } from '../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { Sort } from '@angular/material/sort';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
let tempVal = '';
let tempArr: any = [];
@Component({
  selector: 'app-initiate-claim',
  templateUrl: './initiate-claim.component.html',
  styleUrls: ['./initiate-claim.component.scss'],
})
export class InitiateClaimComponent implements OnInit, AfterViewInit {
  today: Date = new Date();
  @ViewChild('tableRef')
  tableRef: TableComponent = new TableComponent();
  public isSideNavCollapsed: boolean = true;
  public collapsed = false;
  public screenWidth = 0;
  public recentClaimForm: FormGroup;
  public addEndUserForm!: FormGroup;
  public claimCategories: any = [];
  public reasonCode: any = [];
  public tableHeaders: any[] = [];
  public storeTableHeaders: any[] = [];
  public AddInvoiceTableHeaders: any[] = [];
  public priorClaimTableHeaders: any[] = [];
  public priorClaimTableData: any[] = [];
  public customerNumber: string = '';
  public hasStoreNumber: Boolean = false;
  public selectedCountryHasStates: boolean = false;
  public isoDialingCodes: any = [];
  @ViewChildren(FormControlName, { read: ElementRef }) // Use with the generic validation message class
  public formInputElements!: ElementRef[];
  private genericValidator: GenericValidator;
  public displayMessage: { [key: string]: string } = {};
  public displayMessageRec: { [key: string]: string } = {};
  public validationMessages!: {
    firstName: { required: string };
    this: FormGroup<{
      firstName: FormControl<string | null>;
      middleInitial: FormControl<string | null>;
      lastName: FormControl<string | null>;
      companyName: FormControl<string | null>;
      country: FormControl<string | null>;
      address: FormControl<string | null>;
      addressName: FormControl<string | null>;
      city: FormControl<string | null>;
      state: FormControl<string | null>;
      postalCode: FormControl<string | null>;
      county: FormControl<string | null>;
      phone: FormControl<string | null>;
      business: FormControl<string | null>;
      extension: FormControl<string | null>;
      phoneHome: FormControl<string | null>;
      home: FormControl<string | null>;
      extHome: FormControl<string | null>;
      phoneCell: FormControl<string | null>;
      cell: FormControl<string | null>;
      extCell: FormControl<string | null>;
      email: FormControl<string | null>;
      customer: FormControl<string | null>;
      docNumber: FormControl<string | null>;
      rollNumber: FormControl<string | null>;
    }>;
  };
  public storeListData: any = [];
  public sortStoreListData : any = [];
  public selectedValue: string = '';
  public selectedDataItem: null = null;
  public customerData: any = [];
  public storeSelectedRows: any[] = [];
  public selectedclaimCategory: number = 7;
  public selectedclaimCategoryName: String = '';
  public isButtonDisabled = false;
  public isContinueInitiationDisabled: boolean = false;
  private clickTimeout: any;
  public countries: any = [];
  public filteredStates: any = [];
  public filteredOptions: any[] = [];
  public addInvoiceData: any[] = [];
  public associateAddInvoiceData: any[] = [];
  public recentassociateAddInvoiceData: any[] = [];
  public recentassociateAddInvoiceCheckBoxData: any = [];
  public altInvoiceToggle: boolean | undefined;
  public selectedclaimassociatedtoGlobalaccount: any[] = [];
  public selectedDocType: any;
  public associatedtoGlobalAccountData: any = [
    {
      lookupId: 168,
      lookupCode: '',
    },
  ];
  public docTypeData: any = [
    {
      lookupId: 16,
      lookupCode: 'INV',
    },
  ];
  public selectedDailcode: string = '';
  public selectedresonCode: string = '';
  public showErrorMessage: boolean = false;
  public initiateClaimDetails: any;
  public previousSearchResults: any[] = [];
  @Output() onRowSelectData: EventEmitter<any> = new EventEmitter<any>();
  public invoiceNumberList: string[] = [];
  public orderNumberList: string[] = [];
  public invoiceDateList: string[] = [];
  public bsConfig: Partial<BsDatepickerConfig>;
  public searchInput = '';
  public filteredReasonCodes: any[] = [];
  public showAutocomplete = false;
  public selectedClaimReasonCode!: string;
  @ViewChild('elRef1') dropdownList: ElementRef | undefined;
  public loading = true; // For filtered data
  public searchText: any = {};
  public filteredStoreListData: any[] = []; // Filtered data
  public originalStoreListData: any[] = [];
  public sortDirection: { [key: string]: string } = {};
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public itemsPerPage: number = 5; // Default items per page
  public currentPage: number = 1;
  public selectedRow: any = null;
  public storeRowselected: boolean = false;
  public show404ErrorMessage: boolean = false;
  public showRollNumberError: boolean = false;
  public showCustomerNotFoundError = false;
  public showStoreNumberInvalidError = false;
  public showDropdown: boolean = false;
  public getCustomerNumber: any;
  public isSearchIconClicked = false;
  public name: any = '';
  public username: string = '';
  public selectedInvoicedata : any[] = [];
  public userData: any;
  public isWatchListClaim: boolean=false;
  public reasonDescriptionList: any = [];
  public dialingCodesList: any = []
  public endUserSwitch: any;
  public invoiceRowselected: boolean = true;
  public noteTextArea:any;
  public tempaddEndUserFormValue: any = {};
  public futureDateError: boolean = false;
  public isAddEndUserFormSubmitted: boolean = false;
  public setStoreData: any;
  public noDataAvailable: boolean = false;
  public toggleEndUserFlag: boolean = false;
  public noteText:string = '';
  public noteTextDisable : boolean = true;
   


  public taxAndFreightData:any[]=[];
  public careSurChargeData: any[]=[];
  public invoiceDate:string = '';
  public customerAddressData: any[]=[];
  rdcCity: string = "";
  rdcStateCode: string = "";
  public careSurChargeDesc:string ='';
  public currencyCodeName:string ='';
  public currencyCodeDesignation:string ='';
  public invExchangeRate:number= 1;
  public pricingCurrencyCode: string = '';
  totalInvoiceAmt: number=0;
  primaryCustNumber:string='';
  storeNumber:string='';
  // Method to call tax and freight data
  constructor(
    // private store: Store<{
    //   initiateClaim: CustomerDetails;
    //   store: CustomerStoreNumbersResult;
    //   claimCategory: ClaimCategoryDetails;
    //   reasonCode: ReasonCodeByCategory;
    // }>,
    private bodyClassService: BodyClassService,
    private msalService: MsalService,
    private initialClaimService: InitiateClaimService,
    private inputFormBuilder: FormBuilder,
    private router: Router,
    private snackBar: MatSnackBar,
    private userMappingService: UserMappingService,
    private toasterService: ToasterService,
    private notesTasksService: NotesTasksService,
    public authService:AuthService
  ) {
    const forbiddenCharacters = /[^\w]/;

    this.addEndUserForm = this.inputFormBuilder.group({
      firstName: [
        '',
        [Validators.required, Validators.pattern(forbiddenCharacters)],
      ],
    });

    // Define an instance of the validator for use with this form,
    this.genericValidator = new GenericValidator(); // Defines all of the validation messages for the form.
    this.recentClaimForm = this.inputFormBuilder.group({
      customer: [
        this.customerNumber,
        [Validators.required, Validators.maxLength(7), Validators.minLength(1),Validators.pattern(/^[a-zA-Z0-9]+$/)],
      ], 
      store: [''],
      altInvoice: [false],
      docType: [11],
      docNumber: [''],
      invoiceDate: [''],
      rollNumber: [''],
      claimCategory: [''],
      reasonCode: [''],
      endUser: [false],
      associatedtoGlobalaccount: [''],
      isjobStopped: [false],
      priorityClaim: [false],
      watchList: [false],
      addEndUserObject: [''],
    });

    this.initiateaddEndUserForm();
    this.bsConfig = {
      showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
      maxDate: new Date(),
    };
  }
  
  initiateaddEndUserForm() {
    this.addEndUserForm = this.inputFormBuilder.group({
      firstName: [
        '',
        [
          Validators.required,
          // Validators.minLength(4),
          // Validators.maxLength(20),
          this.firstCharacterValidator(),
        ],
      ],
      middleInitial: ['', 
      [
        this.firstCharacterValidator(),
      ],
    
    
    
    
    ],
      lastName: [
        '',
        [
          Validators.required,
          // Validators.maxLength(20),
          this.firstCharacterValidator(),
        ],
      ],
      companyName: [''],
      country: ['240', [Validators.required]],
      address: [
        '',
        [
          Validators.required,
          Validators.maxLength(200),
        ],
      ],
      addressName: new FormControl(''),
      city: ['', [Validators.required]],
      state: ['', [Validators.required]],
      postalCode: [
        '',
        [Validators.required, ],
      ],
      county: [''],
      phone: ['+1'],
      business: ['' ],
      extension: [''],
      phoneHome: ['+1'],
      home: [''],
      extHome: [''],
      phoneCell: ['+1'],

      cell: [''],
      extCell: [''],
      email: ['', [Validators.email]],
    });
  }

  ngOnInit() {
    const activeAccount = this.msalService.instance.getActiveAccount();
    if (activeAccount) {
      this.name = activeAccount.name;
      this.username = activeAccount.username;
      localStorage.setItem('userName', this.name);
      // console.log('Active account:', activeAccount);
      // console.log('activeAccount.idTokenClaims.oid', activeAccount?.idTokenClaims?.oid)
      if(activeAccount?.idTokenClaims?.oid){
      this.userMappingService.findByUser('ObjectId',activeAccount.idTokenClaims.oid).subscribe(
        (data) => {
          this.userData = data;
          localStorage.setItem('userId', this.userData[0].userId);
        // console.log('user data:', this.userData[0].userId);
      }   );
    }
    } 
    this.addBodyClass();
    this.getClaimCategories();
    this.getAssociateGlobalAccount();
    this.getDocType();
    this.getCountriesList();
    this.getDialCodesByCountryId();
    const element = document.getElementById('myDropdownmenu');
    if (element) {
      element.classList.toggle('show');
    }
  
  } 
  canInitiateClaim(permissionCode: string): boolean {
    return this.authService.hasPermission(permissionCode);
  }

  
  // canInitiateClaim(permissionCode: string): boolean {
  //   console.log('Permission Code:', permissionCode); // Log the permission code being checked
  //   const hasPermission = this.authService.hasPermission(permissionCode);
  //   console.log('Has Permission:', hasPermission); // Log the result of the permission check
  //   return hasPermission;
  // }
  public changeWatchListClaim(value : boolean) {
    this.isWatchListClaim = !this.isWatchListClaim;
  }

  removeE(event: any) {
    console.log(this.recentClaimForm.value.docNumber)
    if (event.which === 101) {
      event.preventDefault();
    }
    //this.recentClaimForm.controls['docNumber'].patchValue(this.recentClaimForm.value.docNumber.replace(/[e\+\-]/gi, ""))
  }
 
  public filterFunction() {
    var input, filter, ul, li, i;
    let a: any | undefined = undefined;
    const element = document.getElementById('myInput');
    if (element) {
      filter = element.innerHTML.toUpperCase();
    }
    // input = document.getElementById("myInput");
    // filter = input.value.toUpperCase();
    const element1 = document.getElementById('myDropdownmenu');
    if (element1) {
      a = element1.getElementsByTagName('a');
    }
    // div = document.getElementById("myDropdownmenu");
    // a = div.getElementsByTagName("a");
    for (i = 0; i < a.length; i++) {
      let txtValue = a[i].textContent || a[i].innerText;
      if (txtValue.toUpperCase().indexOf(filter) > -1) {
        a[i].style.display = '';
      } else {
        a[i].style.display = 'none';
      }
    }
  }
  ngAfterViewInit(): void {
    this.forValidatorMessageProcess();
     this.tableHeaders = [
      'Type',
      'Number',
      'Date',
      'Number',
      'Date',
      'Total $’s',
    ];
    this.AddInvoiceTableHeaders = [
      '',
      'lineNumber',
      'rollNumber',
      'styleNumber',
      'colorNumber',
      'DyeLot',
      'RcsCode',
      'Grade',
      'widthInch',
      'lengthInch',
      'quantity',
      'unitOfMeasure',
      'unitPrice',
      'netAmount',
    ];
  }
  
  ngOnDestroy() {
    // Clear the timeout if the component is destroyed
    if (this.clickTimeout) {
      clearTimeout(this.clickTimeout);
    }
  }

  public forValidatorMessageProcess() {
    //Watch for the blur event from any input element on the form.
    const controlBlurs: Observable<any>[] = this.formInputElements.map(
      (formControl: ElementRef) => fromEvent(formControl.nativeElement, 'blur')
    );

    // Merge the blur event observable with the valueChanges observable
    merge(this.addEndUserForm.valueChanges, ...controlBlurs)
      .pipe(debounceTime(100))
      .subscribe((value) => {
        this.displayMessage = this.genericValidator.processMessages(
          this.addEndUserForm
        );
      });

    // Merge the blur event observable with the valueChanges observable parent form
    merge(this.recentClaimForm.valueChanges, ...controlBlurs)
      .pipe(debounceTime(100))
      .subscribe((value) => {
        this.clearCustomerNoValidation();
        this.displayMessageRec = this.genericValidator.processMessages(
          this.recentClaimForm
        );
      });
  }

  public firstCharacterValidator() {
    return (control: any) => {
      const inputValue = control.value;
      if (inputValue && /^[0-9]/.test(inputValue[0])) {
        // Reset the value to an empty string if the first character is a letter
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
  

  public onInput(event: Event) {
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


  public openModalEnterClaim() {
    // Extract values from the form
    const formValues = this.recentClaimForm.value;
    const payload: priorClaimPayload = {
      customerNumber: formValues.customer,
    };

    if (formValues.docNumber) {
      payload.claimDocumentDTO = this.previousSearchResults.map((item) => {
        // Create a copy of the object without the 'customerNumber' property
        const { customerNumber, ...newItem } = item;
        return newItem;
      });
    }

    if (
      formValues.addEndUserObject.firstName ||
      formValues.addEndUserObject.lastName ||
      formValues.addEndUserObject.companyName ||
      formValues.addEndUserObject.country ||
      formValues.addEndUserObject.address ||
      formValues.addEndUserObject.addressName ||
      formValues.addEndUserObject.city ||
      formValues.addEndUserObject.state ||
      formValues.addEndUserObject.postalCode ||
      formValues.addEndUserObject.county ||
      formValues.addEndUserObject.phone ||
      formValues.addEndUserObject.business ||
      formValues.addEndUserObject.extension ||
      formValues.addEndUserObject.home ||
      formValues.addEndUserObject.extHome ||
      formValues.addEndUserObject.cell ||
      formValues.addEndUserObject.extCell ||
      formValues.addEndUserObject.email
    ) {
      payload.endUserInformationDTO = {
        firstName: formValues.addEndUserObject.firstName,
        // middleInitial: formValues.addEndUserObject.middleInitial,
        lastName: formValues.addEndUserObject.lastName,
        // companyName: formValues.addEndUserObject.companyName,
        // countryId: formValues.addEndUserObject.country,
        addressLine1: formValues.addEndUserObject.address,
        // addressLine2: formValues.addEndUserObject.addressName,
        // city: formValues.addEndUserObject.city,
        // stateId: formValues.addEndUserObject.state,
        // postalCode: formValues.addEndUserObject.postalCode,
        // county: formValues.addEndUserObject.county,
        // businessPhoneDialCodeId: formValues.addEndUserObject.phone,
        businessPhoneNumber: formValues.addEndUserObject.business,
        // businessPhoneExtension: formValues.addEndUserObject.extension,
        homePhoneNumber: formValues.addEndUserObject.home,
        // homePhoneExtension: formValues.addEndUserObject.extHome,
        // cellPhoneDialCodeId: formValues.addEndUserObject.phone,
        cellPhoneNumber: formValues.addEndUserObject.cell,
        // cellPhoneExtension: formValues.addEndUserObject.extCell,
        // emailAddress: formValues.addEndUserObject.email,
      };
    }

    this.initialClaimService.viewPriorClaim(payload).subscribe(
      (data) => {
        // Modify the date format in the response
        const modifiedData = data.map((item: { date: string }) => ({
          ...item,
          date: item.date ? item.date.split('T')[0] : '', // Check if date exists before splitting
        }));

        this.priorClaimTableData = modifiedData;
        this.loading = false; // Set to false to hide the spinner and display the table
        const modelDiv = document.getElementById('modalPriorClaimSummary');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
        }
      },
      (error) => {
        console.log('there is no data')
        this.priorClaimTableData = [];
        this.loading = false; // Set to false to hide the spinner
        this.initiateClaimModal();
      }
    );

   
  }

  public initiateClaimModal() {
    const modelDiv = document.getElementById('modalEnterClaim');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.fetchNoteTemplate('INITNOTE');

  }

  public filterDocuments(array: any[]): any[] {
    const uniqueIds = new Set();
    
    return array.filter(obj => {
      const isDuplicate = uniqueIds.has(obj.invoiceNumber);
      uniqueIds.add(obj.invoiceNumber);
      return !isDuplicate;
    });
  }

  public initiateModal() {
    if (this.isButtonDisabled) {
      return; // Exit if the button is already disabled
    }

    this.isButtonDisabled = true;
   
  //  if (!this.isButtonDisabled) {
      // Disable the button to prevent further clicks
 
      this.isContinueInitiationDisabled = true;
      const totalNetAmount = this.associateAddInvoiceData.reduce(
        (accumulator, currentValue) => {
          // Parse the netAmount as a floating-point number and add it to the accumulator
          return accumulator + parseFloat(currentValue.netAmount);
        },
        0
      );
 
      // Print the total netAmount
      const formValues = this.recentClaimForm.value;
      let categoryCode;
      const matchingCategory = this.claimCategories.find(
        (category: any) =>
          category.claimCategoryId === parseInt(formValues.claimCategory)
      );
 
      if (matchingCategory) {
        categoryCode = matchingCategory.claimCategoryCode;
      } else {
        categoryCode = '';
      }
 
      const payload: ClaimRequest = {
        customerNumber: formValues.customer || '',
        primaryCustNumber: this.primaryCustNumber ||'',
     	storeNumber: this.storeNumber||'',
        claimCategoryCode: matchingCategory
          ? matchingCategory.claimCategoryCode
          : '',
        claimReasonCode: this.selectedClaimReasonCode
          ? this.selectedClaimReasonCode
          : '',
        jobStopped: formValues.isjobStopped,
        priorityClaim: formValues.priorityClaim,
        endUserClaim: formValues.endUser,
        businessMarketIndicatorCode: formValues.associatedtoGlobalaccount,
        exchangeRate:this.invExchangeRate,
        
       // exchangeRate: this.selectedInvoicedata[0]?.exchangeRate ? this.selectedInvoicedata[0]?.exchangeRate : '' , 
        sellingCompany:this.selectedInvoicedata[0]?.selco ? this.selectedInvoicedata[0]?.selco : '',
        territory: this.selectedInvoicedata[0]?.territoryManager ? this.selectedInvoicedata[0]?.territoryManager : '', 
        division:this.selectedInvoicedata[0]?.division ? this.selectedInvoicedata[0]?.division : '',
        region: this.selectedInvoicedata[0]?.region ? this.selectedInvoicedata[0]?.region : '',
        addToWatchList: this.isWatchListClaim,
        createdByUserId:this.userData[0].userId,
        modifiedByUserId:this.userData[0].userId,
        claimDocumentDTOS: [],
        claimNoteDTO: {
            claimNoteText: this.noteTextArea,
            noteTypeCode: "INITNOTE"
        },
    };
    
    // Check if selectedInvoicedata has data
    if (this.selectedInvoicedata[0]) {
        payload.claimAddressDTO = this.customerAddressData;
      
     
              
     
    }
    
    if (formValues.docNumber) {
      payload.claimDocumentDTOS = this.previousSearchResults.map((item) => {
        // Create a copy of the object without the 'customerNumber' property
        const { customerNumber, ...newItem } = item;
        return newItem;
      });
    }
    
    if (this.selectedInvoicedata) {
      let claimdocuments: any = [];
      let cumulativeLineCount = 0; 
      this.invoiceNumberList.map((item, index) => {
        let claimLineinvoices: any = [];
        let claimlines = this.getInvoiceDataByInvoiceNumber(item);
        console.log(claimlines,'claimlines')
     
    
        claimlines.map((line, lineIndex) => {
          if (this.isChecked(line)) {
            // Handle TAX, FRGHT, and CHRGS items

            if (['TAX', 'FRGHT', 'CHRGS', 'MISC CHARGES', 'GST', 'HST', 'TOTAL'].includes(line.detailTypeCode)) {

              if (line.netAmount && line.netAmount !== 0 && line.detailTypeCode !=='TOTAL') {
                cumulativeLineCount++;
              claimLineinvoices.push({
                ...line,
               // lineNumber: '', // Set lineNumber to an empty string
               // detailTypeCode: line.detailTypeCode // Ensure detailTypeCode is retained
                lineNumber: String(cumulativeLineCount).padStart(3, '0'), // Assign lineNumber
                detailTypeCode: line.detailTypeCode // Ensure detailTypeCode is retained
             
              });
            }
            } else {
              // Handle other items
              cumulativeLineCount++;
              claimLineinvoices.push({
                invoiceLineNumber: line.lineNumber,
                lineNumber: String(cumulativeLineCount).padStart(3, '0'),
                documentNumber: line.docNumber,
                rollNumber: line.rollNbr || '',
                styleNumber: line.sellingStyleNbr || '',
                colorNumber: line.colorNbr || '',
                widthInFeet: line.widthFeet || 0,
                lengthInFeet: line.lengthFeet || 0,
                grade: line.grade || '',
                unitPriceUsd: line.unitPrice || '',
                lineAmountUsd: line.netAmount || '',
                dyeLot: line.dyeLot || '',
                quantity: line.quantity || '',
                unitOfMeasureCode: line.unitofMeasure || '',
                rcsCode: line.rcsCode || '',
                claimReasonCode: 'ALL',
                lineAmountForeign: line.lineAmountForeign || 0,
                unitPriceForeign: line.unitPriceForeign || 0,
                pricingCurrencyCode: this.pricingCurrencyCode || '',
                currencyCodeDesignation: this.currencyCodeDesignation || '',
                currencyCodeName: this.currencyCodeName || '',
                exchangeRate:this.invExchangeRate,
                // exchangeRate: taxAndFreightData?.exchangeRate || '',
                sellingCompany: line.selco || '',
                productCode: line.productCode || '',
                inventoryStyle: line.inventoryStyle || '',
                inventoryColor: line.inventoryColor || '',
                manufacturingPlant: line.manufacturingPlant || '',
                detailTypeCode: 'MERCH',
                lineSourceCode: line.lineSourceCode || '',
                lineSourceReference: this.selectedInvoicedata[index]?.invoiceNumber || '',
                lineAddReasonCode: line.lineAddReasonCode || '',
                vendorId: line.vendorId || '',
                claimDetailRecordDTOS: [
                  {
                    detailRecordTypeCode: 'INV',
                    widthInFeet: line.widthFeet || 0,
                    widthInInches: line.widthInch || 0,
                    lengthInFeet: line.lengthFeet || 0,
                    lengthInInches: line.lengthInch || 0,
                    quantity: line.quantity,
                    unitOfMeasure: line.unitofMeasure,
                    amountUsd: line.netAmount || 0,
                    amountForeign: line.lineAmountForeign || 0,
                    unitPriceUsd: line.unitPrice || 0,
                    unitPriceForeign: line.unitPriceForeign || 0,
                    exchangeRate:this.invExchangeRate
                    // exchangeRate: taxAndFreightData?.exchangeRate || '',
                  },
                  {
                    detailRecordTypeCode: 'CLM',
                    widthInFeet: line.widthFeet || 0,
                    widthInInches: line.widthInch || 0,
                    lengthInFeet: line.lengthFeet || 0,
                    lengthInInches: line.lengthInch || 0,
                    quantity: line.quantity || 0,
                    unitOfMeasure: line.unitofMeasure,
                    amountUsd: line.netAmount,
                    amountForeign: line.lineAmountForeign || 0,
                    unitPriceUsd: line.unitPrice,
                    unitPriceForeign: line.unitPriceForeign || 0,
                    exchangeRate:this.invExchangeRate
                    // exchangeRate: taxAndFreightData?.exchangeRate || '',
                  },
                ],
              });
            }
          }
        });
        
    
        if (claimLineinvoices.length > 0) {
         let invNum = this.filterDocuments(this.selectedInvoicedata)
          claimdocuments.push({
            documentNumber:invNum[index]?.invoiceNumber,
            exchangeRate:this.invExchangeRate,
            // exchangeRate: taxAndFreightData?.exchangeRate || '',
            orderNumber: this.selectedInvoicedata[index]?.orderNumber || '',
            purchaseOrderNumber: this.recentClaimForm.value.docType == '15' ? this.recentClaimForm.value.docNumber: this.selectedInvoicedata[index]?.poNumber || '',
            billofLading: this.selectedInvoicedata[index]?.bolNumber || '',
            masterBillofLading: this.selectedInvoicedata[index]?.masterBolNbr || '',
            documentTypeCode: 'INV',
            claimReasonCode: this.selectedClaimReasonCode
              ? this.selectedClaimReasonCode
              : '',
            invoiceDate: this.selectedInvoicedata[index]?.invoiceDate || '',
            rdcCity:this.rdcCity || '',
            rdcStateCode:this.rdcStateCode|| '',
            claimLineDetailDTOS: claimLineinvoices,
          });
        }
      });
    
      if (claimdocuments.length > 0) {
        payload.claimDocumentDTOS = claimdocuments;
      } else {
        // Remove claimDocumentDTOS property if it exists and has no elements
        delete payload.claimDocumentDTOS;
      }
    }
    
    
 
      if (
        formValues.addEndUserObject.firstName ||
        formValues.addEndUserObject.lastName ||
        formValues.addEndUserObject.companyName ||
        formValues.addEndUserObject.country ||
        formValues.addEndUserObject.address ||
        formValues.addEndUserObject.addressName ||
        formValues.addEndUserObject.city ||
        formValues.addEndUserObject.state ||
        formValues.addEndUserObject.postalCode ||
        formValues.addEndUserObject.county ||
        formValues.addEndUserObject.phone ||
        formValues.addEndUserObject.business ||
        formValues.addEndUserObject.extension ||
        formValues.addEndUserObject.home ||
        formValues.addEndUserObject.extHome ||
        formValues.addEndUserObject.cell ||
        formValues.addEndUserObject.extCell ||
        formValues.addEndUserObject.email
      ) {
        payload.endUserInformationDTO = {
          firstName: formValues.addEndUserObject.firstName,
          middleInitial: formValues.addEndUserObject.middleInitial,
          lastName: formValues.addEndUserObject.lastName,
          companyName: formValues.addEndUserObject.companyName,
          countryId: formValues.addEndUserObject.country,
          addressLine1: formValues.addEndUserObject.address,
          addressLine2: formValues.addEndUserObject.addressName,
          city: formValues.addEndUserObject.city,
          stateId: formValues.addEndUserObject.state,
          postalCode: formValues.addEndUserObject.postalCode,
          county: formValues.addEndUserObject.county,
          businessPhoneDialCodeId: formValues.addEndUserObject.phone,
          businessPhoneNumber: formValues.addEndUserObject.business.trim(),
          businessPhoneExtension: formValues.addEndUserObject.extension,
          homePhoneNumber: formValues.addEndUserObject.home.trim(),
          homePhoneDialCodeId: formValues.addEndUserObject.phoneHome,
          homePhoneExtension: formValues.addEndUserObject.extHome,
          cellPhoneNumber: formValues.addEndUserObject.cell.trim(),
          cellPhoneDialCodeId: formValues.addEndUserObject.phone,
          cellPhoneExtension: formValues.addEndUserObject.extCell,
          emailAddress: formValues.addEndUserObject.email,
        };
      }
   
console.log(payload,'payload');
      this.initialClaimService.finalInitiateClaim(payload).subscribe((responseData) => {
       
        const matchingReason = this.reasonCode.find((reason:any) => reason.claimReasonCode === responseData.reasonCode);
              if (matchingReason) {
                responseData.reasonDescription = matchingReason.claimReasonDescription;
              }
        const matchingCategory = this.claimCategories.find((category:any) => category.claimCategoryCode === responseData.claimCategoryCode);
       
        if (matchingCategory) {
          responseData.claimCategoryName = matchingCategory.claimCategoryName;
        } else {
          // console.log("Claim Category not found for claimCategoryId:", responseData.claimCategoryId);
        }
        this.initiateClaimDetails = responseData;
        if(this.authService.hasPermission('INITSKIPCONFIRMATION')) {
          this.router.navigate(['/claim-detail/:responseData.claimId']);
        } 
        else {
          const modelDiv = document.getElementById('initiateclaim');
          if (modelDiv != null) {
            modelDiv.style.display = 'block';
          }
        }
       
       
       
      });
    //}
    this.clickTimeout = setTimeout(() => {
    }, 20000);
  }

  public openModalStopInitiate() {
    const modelDiv = document.getElementById('stopinitiate');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  
  public trimAndUpdate(controlName: string) {
    const control = this.addEndUserForm.get(controlName);
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

  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(
      this.isSideNavCollapsed,
      this.screenWidth
    );
  }

  public getCountriesList() {
    this.countries = [];
    this.initialClaimService.getCountriesList().subscribe(
      (data) => {
        this.countries = data;
       const usaCountryObject = this.countries[239];
        //let jsonArray = JSON.parse(this.countries);
        let isoCountryIdToDelete = 240;
        let newObject = {
          createdByUserId: usaCountryObject.createdByUserId,
          modifiedByUserId: usaCountryObject.modifiedByUserId,
          createdDateTime: usaCountryObject.createdDateTime,
          modifiedDateTime: usaCountryObject.modifiedDateTime,
          isoCountryId: usaCountryObject.isoCountryId,
          isoCountryCode: usaCountryObject.isoCountryCode,
          isoCountryCode3: usaCountryObject.isoCountryCode3,
          isoCountryName: usaCountryObject.isoCountryName,
          statusId: usaCountryObject.statusId,
        };
        let indexToDelete = this.countries.findIndex(
          (item: any) => item.isoCountryId === isoCountryIdToDelete
        );

        // Check if the object with the specified isoCountryId exists in the array
        if (indexToDelete !== -1) {
          // Remove the object at the found index
          this.countries.splice(indexToDelete, 1);
          // Add the new object at the first index
          this.countries.unshift(newObject);
        }
        this.getCountryCodeByStates();
      },
      (error) => {
        this.countries = error;
      }
    );
  }

  public openModalWarning() {
   this.toggleEndUserFlag = true;
    const modelDiv = document.getElementById('modalWarning');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
   
  }

  public openModalInfo() {
    const modelDiv = document.getElementById('modalInfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public openModalError() {
    const modelDiv = document.getElementById('modalError');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public openModalInitiateClaim() {
    this.recentClaimForm
      .get('addEndUserObject')
      ?.patchValue(this.addEndUserForm.value);
    const modelDiv = document.getElementById('initiateclaim');
    const modelDiv1 = document.getElementById('modalEnterClaim');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    if (modelDiv1 != null) {
      modelDiv1.style.display = 'none';
    }
  }
  
  public openModalEndUserInfo() {
    this.initiateaddEndUserForm();
    this.displayMessage = {};
    // if (this.tempaddEndUserFormValue) {
    //   const controls = Object.keys(this.addEndUserForm.controls);
    //   controls.forEach((controlName, index) => {
    //     for (const [key, value] of Object.entries(
    //       this.tempaddEndUserFormValue
    //     )) {
    //       if (key == controlName) {
    //         this.addEndUserForm.get(controlName)?.setValue(value);
    //       }
    //     }
    //   });
    // }
    const modelDiv = document.getElementById('enduserinfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
   
  }
  public isRetrieveButtonEnabled(): boolean {
    const customerNumber = this.recentClaimForm.value.customer;
    const docType = this.recentClaimForm.value.docType;
    const docNumber = this.recentClaimForm.value.docNumber;
    const rollNumber = this.recentClaimForm.value.rollNumber;

    // Check the conditions
    const isCustomerNumberValid = customerNumber && customerNumber.length === 7;
    const isDocTypeValid = docType && (docNumber || rollNumber);

    return isCustomerNumberValid && isDocTypeValid;
  }

  public openModalAddInvoice() {
    const modelDiv = document.getElementById('addinvoice');
    if (modelDiv) {
    
      modelDiv.style.display = 'block';
    }
    this.loading = true; 
    let invoiceDateValue = this.recentClaimForm.value.invoiceDate;

    // Check if invoiceDateValue is empty or not a valid date
    if (!invoiceDateValue || isNaN(Date.parse(invoiceDateValue))) {
      // Set invoiceDateValue to empty string if it's empty or not a valid date
      invoiceDateValue = '';
    } else {
      // If invoiceDateValue is a valid date, convert it to ISO string format
      const invoiceDate = new Date(invoiceDateValue);
      invoiceDateValue = invoiceDate.toISOString().split('T')[0];
    }
    if(this.recentassociateAddInvoiceCheckBoxData.length>0){
      this.recentassociateAddInvoiceData = [...this.recentassociateAddInvoiceCheckBoxData]
    }
   
   const addInvoiceObj = {
      customerNumber: '',
      invoiceNumber: '',
      bolNumber: '',
      poNumber: '',
      orderNumber: '',
      invoiceDate: invoiceDateValue,
      docType: '',
      rollNumber: '',
    };

    addInvoiceObj.customerNumber = this.prefixWithZeros(
      this.recentClaimForm.value.customer
    );
    
    const docDate =invoiceDateValue;// this.recentClaimForm.value.invoiceDate; // Assuming docDate is a Date object or a string in ISO format
    
    const dateOnly = docDate;//new Date(docDate).toISOString().split('T')[0]; // Extract date portion
 
    addInvoiceObj.invoiceDate = dateOnly;
  
   // addInvoiceObj.docDate = this.recentClaimForm.value.docDate;
    addInvoiceObj.rollNumber = this.recentClaimForm.value.rollNumber;
    addInvoiceObj.docType = this.recentClaimForm.value.docType;
    if (this.recentClaimForm.value.docType == '11') {
      addInvoiceObj.invoiceNumber = this.recentClaimForm.value.docNumber;
    } else if (this.recentClaimForm.value.docType == '5') {
      addInvoiceObj.bolNumber = this.recentClaimForm.value.docNumber;
    } else if (this.recentClaimForm.value.docType == '14') {
      addInvoiceObj.orderNumber =this.recentClaimForm.value.docNumber.toString();
    } else {
      addInvoiceObj.poNumber = this.recentClaimForm.value.docNumber;
    }

    if (
      this.previousSearchResults.length === 0 ||
      this.previousSearchResults[0].customerNumber !==
        addInvoiceObj.customerNumber
    ) {
      this.previousSearchResults = []; // Reset if customerNumber changed
    }

    const storedValues = this.addInvoiceData.filter(
      (item) => item.customerNumber === addInvoiceObj.customerNumber
    );

    // Store and display the searched values
    if (
      storedValues.length === 0 ||
      storedValues[0].docType !== addInvoiceObj.docType
    ) {
      storedValues.splice(0, storedValues.length);
      this.previousSearchResults.push({
        customerNumber: addInvoiceObj.customerNumber, // Store customerNumber
        documentTypeId: addInvoiceObj.docType,
        documentNumber:
          addInvoiceObj.invoiceNumber ||
          addInvoiceObj.bolNumber ||
          addInvoiceObj.orderNumber ||
          addInvoiceObj.poNumber,
      });
    }

    // this.store.dispatch(new GetData(addInvoiceObj));
 
 this.initialClaimService.getSearchInvoice(addInvoiceObj).pipe(
  map(searchInvoiceResponse => searchInvoiceResponse.body), // Extract body from HttpResponse
  concatMap((searchInvoiceResponse: any[]) => {
    this.addInvoiceData = []; // Clear addInvoiceData before making the calls

    // Create an array of observables for each item in searchInvoiceResponse
    const observables = searchInvoiceResponse.map(item => {
      const invoiceNumber = item.invoiceNbr;
      const invoiceDate = item.invoiceDate;
      const orderNumber = item.orderNumber;
      const orderDate = item.orderDate;
      const bolNumber = item.bolNumber;
      const poNumber = item.ponbr;
      const CustomerNumber = item.customerNumber;
      

      // Call getAddInvoice for each item
      return this.initialClaimService.getAddInvioce({ invoiceNumber, invoiceDate }).pipe(
        map(response => {
          // Add orderNumber and orderDate to each item in the response
          return response.body.map((invoiceItem: any) => ({
            ...invoiceItem,
            orderNumber,
            orderDate,
            bolNumber,
            poNumber,
            CustomerNumber,
            lineNumber: invoiceItem.lineNbr,
            invoiceNumber: invoiceItem.invoiceNbr,
            invoiceDate: invoiceDate, // Ensure invoiceDate is added here
            // Remove old properties
            lineNbr: undefined,
            invoiceNbr: undefined
          }));
        }),
        catchError(error => {
          console.error('Error fetching add invoice data:', error);
          return of(null); // Return observable of null to continue processing
        })
      );
    });

    // Use forkJoin to wait for all getAddInvoice calls to complete
    return forkJoin(observables);
  })
).subscribe(
  addInvoiceResponses => {
    this.noDataAvailable = false;
    addInvoiceResponses.forEach(response => {
      if (response) { // Check if response is not null
        this.addInvoiceData.push(...response); // Store response in addInvoiceData
        this.addInvoiceData.forEach((ele:any)=>ele.checked = false)
      }
    });
  
    console.log('Final Add Invoice Data:', this.addInvoiceData);
  
    // Clear the lists before populating them with unique values
    this.invoiceNumberList = [];
    this.orderNumberList = [];
    this.invoiceDateList = [];
  
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
  
    if (!this.showCustomerNotFoundError) {
      const modelDiv = document.getElementById('addinvoice');
      if (modelDiv) {
        modelDiv.style.display = 'block';
      }
    }
  
    this.loading = false;
  
    // Use a Set to track processed invoice numbers
    const processedInvoices = new Set<string>();
  
        // Call taxAndFreight and careSurCharge for each unique invoiceNumber
        this.addInvoiceData.forEach(item => {
          if (!processedInvoices.has(item.invoiceNumber)) {
            processedInvoices.add(item.invoiceNumber);
            this.taxAndFreight(item.invoiceNumber, item.invoiceDate).subscribe(taxAndFreightData => {
             
           
             
              if (taxAndFreightData) {
                // Ensure taxAndFreightData is not undefined or empty
this.currencyCodeName = taxAndFreightData[0].currencyCodeName;
this.currencyCodeDesignation= taxAndFreightData[0].currencyCodeDesignation;
this.pricingCurrencyCode = taxAndFreightData[0].pricingCurrencyCode;
this.invExchangeRate =taxAndFreightData[0].exchangeRate;
this.totalInvoiceAmt =taxAndFreightData[0].totalInvoiceAmt;
  const taxAndFreight = taxAndFreightData[0];

  // Ensure taxAndFreight has the necessary properties before accessing them
  if (taxAndFreight) {
    const location = taxAndFreight.rDCLoc.trim().split(',');
if (location.length === 2) {
  this.rdcCity = location[0].trim(); // "CHARLOTTE"
  this.rdcStateCode = location[1].trim(); // "NC"
}
console.log(this.rdcCity,'rdcCity');
console.log(this.rdcStateCode,'rdcStateCode')
    const rdcLocation = taxAndFreight.rDCLoc
    // Create and push the billing address (CBB)
    const billingAddress = {
      addressTypeCode: "CBB",
      firstName: "",
      lastName: "",
      fullName:taxAndFreight.billtoName,
      addressLine1: taxAndFreight.billtoAddr1 || '',
      addressLine2: taxAndFreight.billtoAddr2 || '',
      city: taxAndFreight.billtoCity || '',
      state: taxAndFreight.billtoState || '',
      country:'US',
      //country: taxAndFreight.billtoCounty || '',
      zipCode: taxAndFreight.billtoZip || ''
    };

    this.customerAddressData.push(billingAddress);

    // Create and push the shipping address (CST)
    const shippingAddress = {
      addressTypeCode: "CST",
      firstName: "",
      lastName: "",
      fullName:taxAndFreight.billtoName,
      addressLine1: taxAndFreight.shiptoAddr1 || '',
      addressLine2: taxAndFreight.shiptoAddr2 || '',
      city: taxAndFreight.shiptoCity || '',
      state: taxAndFreight.shiptoState || '',
      country:'US',
     // country: taxAndFreight.shiptoCounty || '', // Assuming this is correct
      zipCode: taxAndFreight.shiptoZip || ''
    };

    this.customerAddressData.push(shippingAddress);

    console.log(this.customerAddressData, 'customerAddressData');
  } 



                const { totalFreightAmt, totalTaxAmt, totalInvoiceAmt } = taxAndFreightData[0];
  
                const freightItem = {
                  lineNumber: '004',
                  invoiceNumber:item.invoiceNumber,
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
                  netAmount:totalFreightAmt || 0,
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
                 // exchangeRates:item.invExchangeRate,
                  exchangeRate:this.invExchangeRate,
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
                    lineNumber: '005',
                    invoiceNumber: item.invoiceNumber,
                    invoiceNbr: item.invoiceNumber,
                    documentNumber: item.invoiceNumber,
                    CustomerNumber: item.CustomerNumber,
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
                    netAmount: totalTaxAmt || 0,
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
                    if (careSurChargeData && careSurChargeData.length > 0) {
                        // Initialize variables to accumulate charges
                        let totalChargeSum = 0;
                        let chargeDescription = '';
                       
                     
                        careSurChargeData.forEach((charge:any) => {
                            totalChargeSum += charge.totalCharge; // Sum the total charges
                            chargeDescription = charge.description.trim(); // Use the description from the last entry
                        });
            
                        console.log('Total Charge Sum:', totalChargeSum);
            
                        const chargeItem = {
                            lineNumber: '006',
                            invoiceNumber: item.invoiceNumber,
                            invoiceNbr: item.invoiceNumber,
                            documentNumber: item.invoiceNumber,
                            CustomerNumber: item.CustomerNumber,
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
                            netAmount: totalChargeSum || 0, // Use the summed total charge
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
            
                    
                    }
                });
                    // Finally, create and push the total item
                    const totalItem = {
                      lineNumber: '009',
                      invoiceNumber: item.invoiceNumber,
                      invoiceNbr: item.invoiceNumber,
                      documentNumber: item.invoiceNumber,
                      CustomerNumber: item.CustomerNumber,
                      rollNumber: '       ',
                      styleNumber: '',
                      colorNumber: '',
                      widthInFeet: '',
                      lengthInFeet: '',
                      grade: '',
                      unitPriceUsd: '0.0',
                      netAmount: totalInvoiceAmt || 0,
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
                      exchangeRate: this.invExchangeRate,
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
                  this.addInvoiceData.push(totalItem);
            }
            



            });
          }
        });
      },
      error => {
        this.loading = false;
    if (error.status === 404) {
      if (error.error.detail === 'Record Not Found') {
        this.noDataAvailable = true;
        this.addInvoiceData = [];
        this.invoiceNumberList=[];
        this.recentClaimForm.patchValue({ invoiceDate: '' });
      } else if (this.recentClaimForm.value.docNumber) {
        this.associateAddInvoiceData = [];
        this.show404ErrorMessage = true;
      } else if (this.recentClaimForm.value.rollNumber) {
        this.showRollNumberError = true;
      }
    } else {
      this.addInvoiceData = [];
    }
      }

    );
  }
  
  
  
  

  public setAddInvoiceData(item: any, isChecked: boolean) {
   
    if (isChecked) {
   //   this.invoiceRowselected = !item.detailTypeCode; // Check if detailTypeCode exists
  console.log(this.invoiceRowselected,'   this.invoiceRowselected')
      if (tempArr.length === 0) {
        tempArr.push(item); // If tempArr is empty, just push the item
      } else {
        const isCustomerNumberMatching = tempArr.some(
          (tempItem: any) => tempItem.CustomerNumber === item.CustomerNumber
        );

        if (isCustomerNumberMatching) {
          tempArr.push(item); // If CustomerNumber matches, push the item
        } else {
          tempArr = []; // If CustomerNumber doesn't match, empty tempArr
          // this.recentassociateAddInvoiceCheckBoxData = [];
          tempArr.push(item); // Then push the item
        }
      }
      item.checked = isChecked

    } else {
  //    this.invoiceRowselected = false;
      const index = tempArr.findIndex(
        (tempItem: any) =>
          tempItem.lineNumber === item.lineNumber &&
          tempItem.invoiceNumber === item.invoiceNumber
      );

      if (index !== -1) {
        tempArr.splice(index, 1); // Remove the item from tempArr when unchecked
      }
      item.checked = isChecked
    }
    const matchingObjects = this.recentassociateAddInvoiceCheckBoxData.some((obj:any) => 
      obj.lineNumber === item.lineNumber &&
          obj.invoiceNumber === item.invoiceNumber &&
          obj?.detailTypeCode === item?.detailTypeCode

    );
    if(isChecked && !matchingObjects){
      this.recentassociateAddInvoiceCheckBoxData.push(item);
    }else if(!isChecked && matchingObjects){
      const index = this.recentassociateAddInvoiceCheckBoxData.findIndex(
        (tempItem: any) =>
          tempItem.lineNumber === item.lineNumber &&
          tempItem.invoiceNumber === item.invoiceNumber &&
          tempItem?.detailTypeCode === item?.detailTypeCode
         
      );
      this.recentassociateAddInvoiceCheckBoxData.splice(index, 1); 
      console.log(matchingObjects); 
      
      
    }
    this.recentassociateAddInvoiceData = tempArr;
    
    this.recentassociateAddInvoiceData = this.recentassociateAddInvoiceData.map((invoiceData) => ({
      ...invoiceData,
      netAmount: parseFloat(invoiceData.netAmount) * parseFloat('1'),
     // netAmount: parseFloat(invoiceData.netAmount) * parseFloat(this.taxAndFreightData[0]?.exchangeRate||''),
    }));
    // console.log(item,'...............item')
    // this.invoiceRowselected = tempArr.length > 0  ? true : false;
    // this.invoiceRowselected
   // this.invoiceRowselected = this.recentassociateAddInvoiceCheckBoxData.length > 0;
   console.log(this.recentassociateAddInvoiceCheckBoxData ,'daathis.recentassociateAddInvoiceCheckBoxData ')
   console.log(tempArr ,'daathis.temparr ')
  
   this.invoiceRowselected = this.recentassociateAddInvoiceData.length === 0;
  // this.invoiceRowselected = this.recentassociateAddInvoiceData.length === 0 || 
  // !this.recentassociateAddInvoiceData.some(item => !item.detailTypeCode || item.detailTypeCode.trim() === '');
  }

  public prefixWithZeros(customerNumber: string): string {
    if (customerNumber.length < 7) {
      const numberOfZerosToAdd = 7 - customerNumber.length;
      const prefixedNumber = '0'.repeat(numberOfZerosToAdd) + customerNumber;
      return prefixedNumber;
    } else {
      return customerNumber;
    }
  }
  public openModalPriorClaimSummary() {
    const modelDiv1 = document.getElementById('addinvoice');
    if (modelDiv1 != null) {
      modelDiv1.style.display = 'none';
    }
  }

  public getClaimCategories() {
    // this.store.dispatch(getClaimCategoriesData());
    this.initialClaimService.getClaimCategories().subscribe(
      (data) => {
        // this.store.dispatch(getClaimCategoriesDataSuccess({ data }));
        this.claimCategories = data;
        this.selectedclaimCategory = this.claimCategories[0].claimCategoryId;
        //   this.selectedclaimCategory = this.claimCategories[0].claimCategoryCode;
        // this.selectedclaimCategoryId= this.claimCategories[0].claimCategoryId;
        this.recentClaimForm.patchValue({
          claimCategory: this.selectedclaimCategory,
        });
      },
      (error) => {
        // this.store.dispatch(getClaimCategoriesDataFailure({ error }));
        this.claimCategories = error;
      }
    );
    this.getReasonCodeByCategory(this.recentClaimForm.value.claimCategory);
  }
  
  public onclaimCategoryChange() {
    this.selectedclaimCategory = this.recentClaimForm.value.claimCategory;
    this.filteredReasonCodes = [];
    this.recentClaimForm.patchValue({
      reasonCode: '',
    });
    this.getReasonCodeByCategory(this.recentClaimForm.value.claimCategory);
  }

  public filterOptions(event: any) {
    if (event.target.value.length > 0) {
      this.showDropdown = true;
    } else {
      this.showDropdown = false;
    }
    const searchTerm = event.target.value.toLowerCase();

    // Filter options based on claimReasonCode or definition
    this.filteredOptions = this.reasonCode.filter(
      (option: any) =>
        option.claimReasonCode.toLowerCase().includes(searchTerm) ||
        option.claimReasonDescription.toLowerCase().includes(searchTerm)
    );
  }

  public selectOption(item: any) {
    this.showDropdown = false;
    this.selectedClaimReasonCode = item.claimReasonCode;
    this.recentClaimForm.patchValue({
      reasonCode: item.claimReasonCode + ' - ' + item.claimReasonDescription,
    });
    this.filteredOptions = this.reasonCode.filter(
      (option: any) =>
        option.claimReasonCode.toLowerCase().includes('') ||
        option.claimReasonDescription.toLowerCase().includes('')
    );
  }

  // Close dropdown on outside click
  @HostListener('document:click', ['$event'])
  onDocumentClick(event: any): void {
    if (
      this.dropdownList &&
      !this.dropdownList.nativeElement.contains(event.target) &&
      this.showDropdown
    ) {
      this.toggleDropdown();
    }
  }

  public getReasonCodeByCategory(val: string) {
    // this.store.dispatch(getReasonCodeByCategoryData());
    this.initialClaimService
      // .getReasonCodeByCategory(this.selectedclaimCategoryId)
      .getReasonCodeByCategory(this.selectedclaimCategory)
      .subscribe((data) => {
          // this.store.dispatch(getReasonCodeByCategorySuccess({ data }));
          this.reasonCode = data;

          // Filter the reason codes based on input with a minimum of 3 characters
          this.filteredReasonCodes = this.reasonCode.filter(
            (item: any) =>
              item.claimReasonCode.toLowerCase().includes(val) ||
              item.claimReasonDescription
                .toLowerCase()
                .includes(val)
          );
          this.showAutocomplete = true;
          this.filteredOptions = this.reasonCode;
        },
        (error) => {
          // this.store.dispatch(getReasonCodeByCategoryFailure({ error }));
          this.reasonCode = error;
        }
      );
      // console.log( 'reasoncodes', this.reasonCode)
  }

  toggleDropdown() {
    // console.log('Toggle Dropdown Clicked. Show Dropdown:', this.showDropdown);
    this.showDropdown = !this.showDropdown;
    if (!this.showDropdown) {
      this.filteredOptions = this.reasonCode.slice(); // Reset filtered options when closing
    }
  }

  public getCustomerDetails(CustomerNumber: string) {
    this.getCustomerNumber = CustomerNumber;
    if (this.isSearchIconClicked) {
      // Check the flag
      CustomerNumber = this.prefixWithZeros(CustomerNumber);
      this.showCustomerNotFoundError = false;

      const currentCustomerValue = this.recentClaimForm?.get('customer')?.value;
      this.setCustomerNumber(CustomerNumber);

      // this.store.dispatch(getCustomerData());
      this.getCustomerData(CustomerNumber);
    }
    this.isSearchIconClicked = false;
  }

  public getCustomerData(CustomerNumber: any) {
    if (CustomerNumber != undefined && CustomerNumber != '') {
      this.initialClaimService.getCustomerDetails(CustomerNumber).subscribe(
        (data) => {
          this.customerData = data;
          // Assuming data is an array and you need the first item
          if (this.customerData.length > 0) {
            this.customerData = this.customerData[0];
            this.primaryCustNumber = this.customerData.primaryCustNumber;
            this.storeNumber = this.customerData.storeNumber;
            console.log(this.primaryCustNumber,'primaryCustNumber');
            console.log(this.storeNumber,'storeNumber')
            // Filter addressType === 'PHYSICAL'
            const physicalAddresses = this.customerData.address.filter(
              (address:any) => address.addressType === 'PHYSICAL'
            );
  
            // Map to the new structure
            this.customerAddressData = physicalAddresses.map((address:any) => ({
              addressTypeCode: "SOA",
              firstName: "",
              lastName: "",
              fullName:this.customerData.customerName||'',
              addressLine1: address.addressLine1 || '',
              addressLine2: address.addressLine2 || '',
              city: address.city || '',
              state: address.stateCode || '',
              country: address. countryCode|| '',
              zipCode: address.zipCode || ''
            }));
  
            console.log(this.customerAddressData, 'customerAddressData');
          } else {
            this.customerAddressData = []; // Handle case where data is empty
          }
          // this.isContinueInitiationDisabled = true;
        },
        (error) => {
          this.customerData = error;
          this.isContinueInitiationDisabled = false;
          if (error.status === 404) {
            this.showCustomerNotFoundError = true;
          }
        }
      );
    }
  }
  
  public setCustomerNumber(customerNumber: String) {
    this.showCustomerNotFoundError = false;
    this.isContinueInitiationDisabled = false;
    if (customerNumber.length > 0) {
      this.customerData = [];
      this.customerData = customerNumber;
      this.recentClaimForm.patchValue({
        customer: this.customerData,
      });
    } else {
      //this.displayCustomerError=true
      // this.displayMessageRec.customer="Customer can not be empty"
    }
  }

  public onEnterKeyPress(event: Event): void {
    event.preventDefault();
  }

  public onEnter(event:any){
    // console.log(this.recentClaimForm.value.reasonCode,'...event')
    let code = this.reasonCode.filter((ele:any)=>ele.claimReasonCode == this.recentClaimForm.value.reasonCode) 
    this.recentClaimForm.patchValue({
     reasonCode:code[0].claimReasonCode + '-' + code[0].claimReasonDescription
    })
    this.filteredOptions = this.reasonCode.filter(
      (option: any) =>
        option.claimReasonCode.toLowerCase().includes('') ||
        option.claimReasonDescription.toLowerCase().includes('')
    );
  }

  public onCustomerNumberChange(newValue: string) {
    const previousCustomerNumber = this.customerData;
    if (
      previousCustomerNumber &&
      previousCustomerNumber !== newValue &&
      (this.associateAddInvoiceData?.length > 0 ||
        this.isAddEndUserFormSubmitted)
    ) {
      // Previous customer number is not empty and has changed, show an alert message
      const modelDiv = document.getElementById('customerModalWarning');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
        this.isAddEndUserFormSubmitted = false;
      }
    }
    // Now update the customer data or perform any other actions as needed
    this.getCustomerDetails(newValue);
  }

  public showWarning() {
    if (
      this.customerData.length !== 0 &&
      (this.associateAddInvoiceData?.length > 0 ||
        this.isAddEndUserFormSubmitted)
    ) {
      const modelDiv = document.getElementById('customerModalWarning');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
        this.isAddEndUserFormSubmitted = false;
      }
    }
  }

  public clearCustomerNoValidation() {
    if(!this.toggleEndUserFlag){
    this.showStoreNumberInvalidError = false;
    this.show404ErrorMessage = false;
    this.showRollNumberError = false;
    this.hasStoreNumber = false;
    if (
      this.recentClaimForm.value.store !== '' ||
      this.recentClaimForm.value.endUser === true
    ) {
      this.hasStoreNumber = true;
    } else {
      this.hasStoreNumber = false;
    }
    if (
      this.addEndUserForm.valid &&
      this.recentClaimForm.value.endUser === true &&
      this.isAddEndUserFormSubmitted
    ) {
      this.isContinueInitiationDisabled = true;
    }
    if(this.recentClaimForm.value.endUser === true){

      this.recentClaimForm.patchValue({customer:null})
      this.recentClaimForm.patchValue({docNumber:null})
      this.associateAddInvoiceData = [];

    }
  }
  }

  public toggleEndUser() {
    // Toggle the flag
    this.toggleEndUserFlag = !this.toggleEndUserFlag;
    this.showStoreNumberInvalidError = false;
    this.show404ErrorMessage = false;
    this.showRollNumberError = false;
    this.hasStoreNumber = false;
    this.noDataAvailable = false;
    if (
      this.recentClaimForm.value.store !== '' ||
      this.recentClaimForm.value.endUser === true
    ) {
      this.hasStoreNumber = true;
    } else {
      this.hasStoreNumber = false;
    }
    // if (
    //   this.addEndUserForm.valid &&
    //   this.recentClaimForm.value.endUser === true &&
    //   this.isAddEndUserFormSubmitted
    // ) {
    //   this.isContinueInitiationDisabled = true;
    // }
   if(this.toggleEndUserFlag === true){
    const modelDiv = document.getElementById('endUserToggleModal');
    if (modelDiv != null) {
      console.log('blocked')
      modelDiv.style.display = 'block';
    }
   }
  }
 


  public trackByStoreNumber(index: number, dataItem: any): number {
    return dataItem.storeNumber;
  }

  public openModalModalStoreInvoice() {
    this.storeListData = [];
    this.showStoreNumberInvalidError = false;
    // this.store.dispatch(getStoreData());
    this.initialClaimService
      .getStoreDetails(this.recentClaimForm.value.store)
      .subscribe(
        (data) => {
          // this.store.dispatch(getStoreDataSuccess({ data }));
          this.storeListData = data;
          this.loading = false;
          this.originalStoreListData = [...this.storeListData];
          //  Display the modal here, inside the success callback
          const modelDiv = document.getElementById('storeinvoice');
          if (modelDiv != null) {
            modelDiv.style.display = 'block';
          }
        },
        (error) => {
          // this.store.dispatch(getStoreDataFailure({ error }));
          this.storeListData = [];
          this.loading = false;
          if (error.status === 404) {
            this.showStoreNumberInvalidError = true;
          }
        }
      );
  }

  public CloseModalEnterClaim() {
    this.isButtonDisabled = false;
    const modelDiv = document.getElementById('modalEnterClaim');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  public CloseModalPriorSummary() {
    const modelDiv = document.getElementById('modalPriorClaimSummary');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public CloseModalInfo() {
    const modelDiv = document.getElementById('modalInfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public CloseModalStopInitiate() {
    const modelDiv = document.getElementById('stopinitiate-Modal-action');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public stopInitiate() {
    const modelDiv = document.getElementById('stopinitiate-Modal-action');
    this.router.navigate(['/dashboard']);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public CloseModalWarning() {
    this.recentClaimForm.patchValue({
      altInvoice: false,
    });

    const modelDiv = document.getElementById('modalWarning');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  public CloseModalError() {
    const modelDiv = document.getElementById('modalError');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public CloseModalInitiateClaim() {
    const claimNumberArray = [this.initiateClaimDetails.claimId]; 
    this.initialClaimService.automaticAssignment(claimNumberArray).subscribe(data=>{
      if(data.status === 200){
        const modelDiv = document.getElementById('initiateclaim');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
      this.router.navigate(['/dashboard']);
    }
      }
    })
   
  }

  public CloseModalEndUserInfo() {
    const modelDiv = document.getElementById('enduserinfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }

  }


 
  public addFormDetails() {
    if (this.addEndUserForm.invalid) {
      return;
    }
    this.isAddEndUserFormSubmitted = true;
    this.tempaddEndUserFormValue = this.addEndUserForm.value;
    this.recentClaimForm.patchValue({
      addEndUserObject: this.addEndUserForm.value,
    });
    if (this.recentClaimForm.value.endUser === true && this.addEndUserForm.valid &&
      this.isAddEndUserFormSubmitted) {
      this.isContinueInitiationDisabled = true;
    }
    else if(this.addEndUserForm.valid &&
       this.isAddEndUserFormSubmitted){
      this.isContinueInitiationDisabled = true;
    }
    const modelDiv = document.getElementById('enduserinfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    const toastType: 'success' | 'error' | 'warning' = 'success'; 
    const message = 'End User Information Added Successfully';
    this.toasterService.showToast(toastType, message);
  }

  public CloseModalPriorClaimSummary() {
    const modelDiv = document.getElementById('priorclaimsummary');
    this.router.navigate(['/dashboard']);

    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public CloseModalAddInvoice() {
    const modelDiv = document.getElementById('addinvoice');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

 
  public addInvoice() {
  
    this.selectedInvoicedata = this.recentassociateAddInvoiceData
    // this.recentassociateAddInvoiceCheckBoxData =
    //   this.recentassociateAddInvoiceData;
console.log(this.selectedInvoicedata,'....................this.selectedInvoicedata')
    this.recentClaimForm.value;
    // Check if this.associateAddInvoiceData is empty
    if (!this.associateAddInvoiceData || !this.associateAddInvoiceData.length) {
      // If empty, assign this.recentassociateAddInvoiceData to this.associateAddInvoiceData
      if (
        this.recentassociateAddInvoiceData &&
        this.recentassociateAddInvoiceData.length > 0
      ) {
        //  this.associateAddInvoiceData = this.sumAndRemoveDuplicates(this.recentassociateAddInvoiceData);
        this.recentassociateAddInvoiceData =
          this.recentassociateAddInvoiceData.map((item) => ({
            ...item,
            docType: parseInt(this.recentClaimForm.value.docType),
            docNumber: this.recentClaimForm.value.docNumber,
            docDate: this.recentClaimForm.value.invoiceDate,
            
          }));

        this.associateAddInvoiceData = this.sumAndRemoveDuplicates(
          this.recentassociateAddInvoiceData
        );
      }
    } else {
      // If not empty, compare CustomerNumber of all objects
      const customerNumber = this.recentClaimForm.value.customer;
      const docType = this.recentClaimForm.value.docType;

      const isCustomerNumberMatching = this.associateAddInvoiceData.some(
        (item) => item.CustomerNumber == customerNumber
      );

      if (isCustomerNumberMatching) {
        this.recentassociateAddInvoiceData =
          this.recentassociateAddInvoiceData.map((item) => ({
            ...item,
            docType: parseInt(this.recentClaimForm.value.docType),
            docNumber: this.recentClaimForm.value.docNumber,
            docDate: this.recentClaimForm.value.invoiceDate,
          }));

        const result = this.sumAndRemoveDuplicates(
          this.recentassociateAddInvoiceData
        );

        for (const item of result) {
          if (typeof item === 'object') {

            const matchingIndex = this.associateAddInvoiceData.findIndex(
              (existingItem) =>
                existingItem.CustomerNumber === item.CustomerNumber &&
                existingItem.invoiceNumber === item.invoiceNumber
            );

            if (matchingIndex !== -1) {
              // Remove the existing item with the same customer and invoice number
              this.associateAddInvoiceData.splice(matchingIndex, 1);
            }

            // Push the new item to this.associateAddInvoiceData
            this.associateAddInvoiceData.push(item);
          }
        }
      } else {
        // If CustomerNumber does not match, clear this.associateAddInvoiceData and push recentassociateAddInvoiceData
        this.associateAddInvoiceData = [];
        this.recentassociateAddInvoiceData =
          this.recentassociateAddInvoiceData.map((item) => ({
            ...item,
            docType: parseInt(this.recentClaimForm.value.docType),
            docNumber: this.recentClaimForm.value.docNumber,
            docDate: this.recentClaimForm.value.invoiceDate,
          }));
        const result = this.sumAndRemoveDuplicates(
          this.recentassociateAddInvoiceData
        );

        for (const item of result) {
          if (typeof item === 'object') {
            const matchingIndex = this.associateAddInvoiceData.findIndex(
              (existingItem) =>
                existingItem.customerNumber === item.customerNumber &&
                existingItem.invoiceNumber === item.invoiceNumber
            );

            if (matchingIndex !== -1) {
              // Remove the existing item with the same customer and invoice number
              this.associateAddInvoiceData.splice(matchingIndex, 1);
            }

            // Push the new item to this.associateAddInvoiceData
            this.associateAddInvoiceData.push(item);
          }
        }
      }
    }
    tempArr = [];
    // this.tableRef.selectedRows = [];
    this.CloseModalAddInvoice();
    console.log(this.associateAddInvoiceData,'associateAddInvoiceData')
    this.isContinueInitiationDisabled = true;
    this.recentClaimForm.patchValue({
      docNumber: ''
    });
  }

 public isChecked(claim: any): boolean {
    if (
      this.recentassociateAddInvoiceCheckBoxData &&
      this.recentassociateAddInvoiceCheckBoxData.length > 0
    ) {
     
      let result =  this.recentassociateAddInvoiceCheckBoxData.some(
        (item: any) =>
          item.lineNumber === claim.lineNumber &&
          item.invoiceNumber === claim.invoiceNumber &&
          item?.detailTypeCode === claim?.detailTypeCode
      );
   //   console.log(result,claim.lineNumber,claim.invoiceNumber,'..............12333')
      return result
    }else{
      return claim.checked;
      
    }
  
   
  }

  // Function to sum netAmount and remove duplicates
  public sumAndRemoveDuplicates(data: any) {
    const invoiceSumMap = new Map();

    for (const invoiceData of data) {
      const invoiceNumber = invoiceData.invoiceNumber;
      const netAmount = parseFloat(invoiceData.netAmount);

      if (!isNaN(netAmount)) {
        if (invoiceSumMap.has(invoiceNumber)) {
          invoiceSumMap.set(
            invoiceNumber,
            invoiceSumMap.get(invoiceNumber) + netAmount
          );
        } else {
          invoiceSumMap.set(invoiceNumber, netAmount);
        }
      }
    }

    const updatedData = Array.from(invoiceSumMap.keys()).map(
      (invoiceNumber) => {
        return {
          ...data.find(
            (item: { invoiceNumber: any }) =>
              item.invoiceNumber === invoiceNumber
          ),
          netAmount: invoiceSumMap.get(invoiceNumber).toFixed(2),
        };
      }
    );

    return updatedData;
  }

  public CloseModalStoreInvoice() {
    // this.recentClaimForm.patchValue({
    //   store: '',
    // });
    const modelDiv = document.getElementById('storeinvoice');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  // Function: Add class to body
  public addBodyClass() {
    const bodyTag = document.body;
    bodyTag.classList.add('my-class');
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  submitForm() {
    // if (this.inputForm.valid) {
    // if(this.addEndUserForm.invalid){
    //  this.recentClaimForm.get('addEndUserObject')?.patchValue(this.addEndUserForm.value);
    //this.inputForm.patchValue('addEndUserObject',JSON.stringify(this.addEndUserForm.value))
    // Form is valid, perform form submission logic
    // console.log(this.recentClaimForm.value);
    // console.log("submitform"+JSON.stringify(this.recentClaimForm.value));
  }
  // } else {
  // Form is invalid, display error messages or handle accordingly
  // }
  // }

  public isNumentered() {
    if (
      (this.addEndUserForm.get('phone')?.value &&
        this.addEndUserForm.get('business')?.value) ||
      (this.addEndUserForm.get('phoneHome')?.value &&
        this.addEndUserForm.get('home')?.value) ||
      (this.addEndUserForm.get('phoneCell')?.value &&
        this.addEndUserForm.get('cell')?.value)
    ) {
      return true;
    } else {
      return false;
    }
  }

  public setCustomerDataFromStoreTable() {
    this.isContinueInitiationDisabled = true;
    this.customerData = [];
    this.customerData = this.setStoreData;
    this.selectedValue =
      this.customerData.customerName +
      ' - ' +
      this.customerData.address +
      ' - ' +
      this.customerData.city;
    this.recentClaimForm.patchValue({
      // store: this.customerData.storeNumber,
      customer: this.customerData.customerNumber,
    });
    this.recentClaimForm.patchValue({
      store: '',
    });

    const modelDiv = document.getElementById('storeinvoice');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public getCountryCodeByStates() {
    this.filteredStates = [];
    let selccounty = this.countries.find((coun: any) => coun.isoCountryId == this.addEndUserForm.value.country)
    // console.log("country with data:"+JSON.stringify(selccounty))
    if(selccounty?.dialCode?.isoCountryDialingCode){
      this.addEndUserForm.patchValue({
        phone: "+"+selccounty.dialCode.isoCountryDialingCode,
        phoneHome: "+"+selccounty.dialCode.isoCountryDialingCode,
        phoneCell:"+"+selccounty.dialCode.isoCountryDialingCode,
      });
      this.selectedDailcode = selccounty.dialCode.isoCountryDialingCode;
    }else{
      this.addEndUserForm.patchValue({
        phone: "+1",
        phoneHome: "+1",
        phoneCell:"+1",
      });
      this.selectedDailcode = "+1";
    }
    
    // console.log("Phone Logs:"+JSON.stringify(this.addEndUserForm.value))
    // this.store.dispatch(getCountryCodeData());
    this.initialClaimService
      .getCountryCodeByStates(this.addEndUserForm.value.country ? this.addEndUserForm.value.country : '240')
       .subscribe(
        (data) => {
          // this.store.dispatch(getCountryStateCodeSuccess({ data }));

          // Check if data is an array and if it's not empty
          this.selectedCountryHasStates =
            Array.isArray(data) && data.length > 0;

          // Update the 'state' form control's validation
          const stateControl = this.addEndUserForm.get('state');
          if (stateControl) {
            if (this.selectedCountryHasStates) {
              stateControl.setValidators([Validators.required]);
            } else {
              stateControl.clearValidators();
            }
            stateControl.updateValueAndValidity();
          }

          this.filteredStates = data;
          // this.getSelectedDialCodeByCountryId();
        },
        (error) => {
          // this.store.dispatch(getCountryStateCodeFailure({ error }));
          this.filteredStates = error;
        }
      )
    
  }
  public getSelectedDialCodeByCountryId(){
    this.dialingCodesList.filter((a: any) => {
      if (a.isoCountryDialingCode == this.addEndUserForm.value.country) {
        
        this.addEndUserForm.patchValue({
          phone: `+${a.isoCountryDialingCode}`,
          phoneHome: `+${a.isoCountryDialingCode}`,
          phoneCell: `+${a.isoCountryDialingCode}`,
        });
        this.selectedDailcode = a.isoCountryDialingCode;
      }
    });

  }

  public validateCountryDialCodeFormat(){
    if((this.addEndUserForm.value.country == 240 
      || this.addEndUserForm.value.country == 42 
      || this.addEndUserForm.value.country == 31) && this.addEndUserForm.value.phoneHome == '+1'){
    return true;
     }else{
      return false;
      }
   }


  public getDialCodesByCountryId() {
    this.isoDialingCodes = [];
    // this.store.dispatch(getCountryCodeData());
    this.initialClaimService
      .getDialCodesByCountryId(this.addEndUserForm.value.country)
      .subscribe(
        (data) => {
          data.forEach((a: any) => {
            this.isoDialingCodes.push(`+${a.isoCountryDialingCode}`);
          });

          this.dialingCodesList = data;

         
          this.isoDialingCodes.sort((a: any, b: any) => {
            const numericA = a.replace(/[^0-9]/g, '');
            const numericB = b.replace(/[^0-9]/g, '');
            return parseInt(numericA) - parseInt(numericB);
          });
        },
        (error) => {
          //this.store.dispatch(getCountryCodeFailure({ error }));
          this.isoDialingCodes = error;
        }
      );
  }

  public toggleAltInvoice() {
  
    if (
      this.associateAddInvoiceData &&
      this.associateAddInvoiceData !== null &&
      Object.keys(this.associateAddInvoiceData).length > 0 &&
      this.recentClaimForm.get('docNumber')?.value !== ''
    ) {
      // The variable is an object and contains values
      this.openModalWarning();
    } else {
      const altInvoiceControl = this.recentClaimForm.get('altInvoice');
      if (altInvoiceControl) {
        this.altInvoiceToggle = altInvoiceControl.value;
        //altInvoiceControl.setValue(!currentValue); // Toggle the value
      }
    }
   // this.endUserToggleBtn();
  }
  
  public endUserToggleBtn(){
  const altInvoiceControl = this.recentClaimForm.get('altInvoice');
  if (altInvoiceControl?.value == true) {
   this.recentClaimForm.patchValue({
    endUser : true
   })
    this.openModalEndUserInfo();
  }else if (!altInvoiceControl?.value){
    this.recentClaimForm.patchValue({
      endUser : false
     })
  }
  }
  public onassociatedtoGlobalaccountChange(field: string) {
    if (field === 'docType') {
      // Update the selectedDocType based on the form control value
      this.selectedDocType = this.recentClaimForm.get('docType')?.value;
      console.log('Selected docTypeId:', this.selectedDocType);
    } else if (field === 'CLM_BUS_MKT_IND') {
      // Handle other fields
      this.recentClaimForm.controls.associatedtoGlobalaccount.setValue(
        this.selectedclaimassociatedtoGlobalaccount
      );
    }
  }

  public getAssociateGlobalAccount() {
    const global_assc_acc = 'CLM_BUS_MKT_IND';
    //  this.store.dispatch(getAssociateGlobalAccountData());
    this.initialClaimService
      .getAssociateGlobalAccount(global_assc_acc)
      .subscribe(
        (data) => {
          // this.store.dispatch(getAssociateGlobalAccountDataSuccess({ data }));
          this.associatedtoGlobalAccountData = data;
          this.selectedclaimassociatedtoGlobalaccount =
            this.associatedtoGlobalAccountData[0]?.lookupCode;
          this.recentClaimForm.patchValue({
            associatedtoGlobalaccount:
              this.selectedclaimassociatedtoGlobalaccount,
          });
        },
        (error) => {
          // this.store.dispatch(getAssociateGlobalAccountDataFailure({ error }));
          this.associatedtoGlobalAccountData = error;
        }
      );
  }
  public jobStopped() {
    if (this.recentClaimForm.value.isjobStopped) {
      this.recentClaimForm.patchValue({
        priorityClaim: true,
      });
    }
  }

  public checkPriorityClaim(){
    if (this.recentClaimForm.value.isjobStopped) {
      this.recentClaimForm.patchValue({
        priorityClaim: true,
      });
    }
  }
 
  public handleRowSelection(event: Event) {
    this.setStoreData = [];
    this.setStoreData = event;
    //this.setCustomerDataFromStoreTable(event);
  }

  public resetInvoice() {
    this.recentClaimForm.patchValue({
      docType: '16',
    });
    this.recentClaimForm.patchValue({
      docNumber: '',
    });

    this.recentClaimForm.patchValue({
      invoiceDate: '',
    });
    this.recentClaimForm.patchValue({
      altInvoice: true,
    });
    this.associateAddInvoiceData = [];
    this.altInvoiceToggle = true;
    const modelDiv = document.getElementById('modalWarning');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.endUserToggleBtn();
  }
  public getDocType() {
    const claim_init_docType = 'CLM_INIT_DOC_TYPE';

    this.initialClaimService
      .getAssociateGlobalAccount(claim_init_docType)
      .subscribe(
        (data) => {
          this.docTypeData = data;
          console.log(this.docTypeData, 'this.docTypeData');

          // Create an array of observables for each lookupCode
          const observables = this.docTypeData.map((docType: any) => {
            return this.initialClaimService.getDocIdByDocumentTypeCode(docType.lookupCode).pipe(
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
              this.recentClaimForm.patchValue({
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
    if(this.recentassociateAddInvoiceCheckBoxData.length > 0){
      const areObjectsPresent = invoiceData.every(invoice => 
        this.recentassociateAddInvoiceCheckBoxData.some((obj:any) =>
          invoice.lineNumber === obj.lineNumber &&
          invoice.invoiceNumber === obj.invoiceNumber &&
          invoice?.detailTypeCode === obj?.detailTypeCode
        )
      );
      console.log(areObjectsPresent,'.........areObjectsPresent')
      return areObjectsPresent
    }else{
      let value =  invoiceData.every(claim => claim.checked == true);
      console.log(value,'....value1')
      return invoiceData.length > 0 && value
    }
   
  }

  // public getInvoiceDataByInvoiceNumber(invoiceNumber: string) {
  //   return this.addInvoiceData.filter(
  //     (item) => item.invoiceNumber === invoiceNumber && item.lineAmountUsd !== 0
  //   );
  // }
  public getInvoiceDataByInvoiceNumber(invoiceNumber: string) {
    return this.addInvoiceData.filter(
      (item) => item.invoiceNumber === invoiceNumber && item.lineAmountUsd !== 0)
      .sort((a, b) => {
        // Ensure the "TOTAL" row is always the last
        if (a.detailTypeCode?.trim() === 'TOTAL') return 1;
        if (b.detailTypeCode?.trim() === 'TOTAL') return -1;
        // For other rows, maintain the original order
        return 0;
      });
  }
  // public getInvoiceData(invoiceNumber:any){
  //   let invoice = this.getInvoiceDataByInvoiceNumber(invoiceNumber)
  //   invoice.forEach((ele:any)=>ele.checked = false)
  //   return invoice;
  // }
  

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

  public deleteRow(index: number): void {
    if (index >= 0 && index < this.associateAddInvoiceData.length) {
      const deletedItem = this.associateAddInvoiceData.splice(index, 1)[0]; // Remove and capture the deleted item
      

      // Filter the recentassociateAddInvoiceCheckBoxData to remove items with the same invoiceNumber
      this.recentassociateAddInvoiceCheckBoxData =
        this.recentassociateAddInvoiceCheckBoxData.filter(
          (item: any) => item.invoiceNumber !== deletedItem.invoiceNumber
        );

      // console.log(this.associateAddInvoiceData); // Log the updated array
      // console.log(this.recentassociateAddInvoiceCheckBoxData, 'checkboz'); // Log the updated recentassociateAddInvoiceCheckBoxData
    }
  }

  public onSearch() {
    // Check if all search inputs are empty
    if (
      !this.searchText.storeNumber &&
      !this.searchText.primaryCustNumber &&
      !this.searchText.customerNumber &&
      !this.searchText.customerName &&
      !this.searchText.addressLine1 &&
      !this.searchText.city
    ) {
      // Reset the storeListData to the original data
      this.storeListData = [...this.originalStoreListData];
    } else {
      // Apply the filtering logic if search input is not empty
      this.storeListData = this.originalStoreListData.filter((store: any) => {
        return (
          (this.searchText.storeNumber === undefined ||
            store.storeNumber
              .toLowerCase()
              .includes(this.searchText.storeNumber.toLowerCase())) &&
          (this.searchText.primaryCustNumber === undefined ||
            store.primaryCustNumber
              .toLowerCase()
              .includes(this.searchText.primaryCustNumber.toLowerCase())) &&
          (this.searchText.customerNumber === undefined ||
            store.customerNumber
              .toLowerCase()
              .includes(this.searchText.customerNumber.toLowerCase())) &&
          (this.searchText.customerName === undefined ||
            store.customerName
              .toLowerCase()
              .includes(this.searchText.customerName.toLowerCase())) &&
          (this.searchText.addressLine1 === undefined ||
            store.address[0]?.addressLine1
              .toLowerCase()
              .includes(this.searchText.addressLine1.toLowerCase())) &&
          (this.searchText.city === undefined ||
            store.address[0]?.city
              .toLowerCase()
              .includes(this.searchText.city.toLowerCase()))
        );
      });
    }
  }



  public restrict_specialChars(event:any) {
    this.genericValidator.restrictSpecialChars(event, event.keyCode);
  }

  public restrictSpecialCharsCompany(event: { preventDefault: () => void; }, keycode: any) {
    let input = String.fromCharCode(keycode);
    // Allow alphanumeric characters and spaces
    if (/[a-zA-Z0-9 ]/.test(input)) {
      return true;
    } else {
      event.preventDefault();
      return false;
    }
  }



  public toggleRowSelection(selectedStore: any) {
    selectedStore.selected = !selectedStore.selected;
    for (const store of this.storeListData) {
      if (store !== selectedStore) {
        store.selected = false;
      }
      if (store == selectedStore) {
        this.setStoreData = selectedStore;
      }
    }
    this.storeRowselected = this.storeListData.some(
      (store: { selected: any }) => store.selected
    );
  }

  sortData(sort: Sort) {
    const data = this.originalStoreListData.slice(); 
  
    if (!sort.active || sort.direction === '') {
      this.storeListData = data;
      return;
    }
  
    this.storeListData = data.sort((a:any, b:any) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'store':
          return this.compare(a.storeNumber, b.storeNumber, isAsc);
        case 'commcust':
          return this.compare(a.primaryCustNumber, b.primaryCustNumber, isAsc);
        case 'customer':
          return this.compare(a.customerNumber, b.customerNumber, isAsc);
          case 'storeName':
            return this.compare(a.customerName, b.customerName, isAsc);
       
            case 'address':
              return this.compare(a.address[0]?.addressLine1, b.address[0]?.addressLine1, isAsc);
       
              // case 'cityStateZip':
              //   return this.compare(a.customerNumber, b.customerNumber, isAsc);
              case 'cityStateZip':
  const addressA = a.address?.[0] || {};
  const addressB = b.address?.[0] || {};
  const formattedAddressA = `${addressA.city}, ${addressA.stateCode}, ${addressA.zipCode}`;
  const formattedAddressB = `${addressB.city}, ${addressB.stateCode}, ${addressB.zipCode}`;
  return this.compare(formattedAddressA, formattedAddressB, isAsc);

             
        default:
          return 0;
      }
    });
  }
  
  
  // Comparison function for sorting
   compare(a: string | number, b: string | number, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  get pagedStoreListData() {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    return this.storeListData.slice(startIndex, endIndex);
  }
  public onItemsPerPageChange() {
    this.currentPage = 1; // Reset to the first page when changing items per page
  }

  public getDisplayStart() {
    return (this.currentPage - 1) * this.itemsPerPage + 1;
  }

  // Get the ending index of the displayed entries
  public getDisplayEnd() {
    const endIndex = this.currentPage * this.itemsPerPage;
    return endIndex > this.storeListData.length
      ? this.storeListData.length
      : endIndex;
  }

  public onPreviousPageClick() {
    if (this.currentPage > 1) {
      this.currentPage--;
    }
  }

  // Function to navigate to the next page
  public onNextPageClick() {
    const maxPage = Math.ceil(this.storeListData.length / this.itemsPerPage);
    if (this.currentPage < maxPage) {
      this.currentPage++;
    }
  }
  public getPageNumbers(): number[] {
    const pageCount = Math.ceil(this.storeListData.length / this.itemsPerPage);
    return Array.from({ length: pageCount }, (_, index) => index + 1);
  }

  // Function to navigate to a specific page
  public onPageNumberClick(page: number) {
    if (page >= 1 && page <= this.getPageNumbers().length) {
      this.currentPage = page;
    }
  }

  public CloseCustomerModalWarning() {
    this.recentClaimForm?.get('customer')?.setValue(this.getCustomerNumber);
    this.getCustomerData(this.getCustomerNumber);
    const modelDiv = document.getElementById('customerModalWarning');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public resetAllFormFields(customerNumber: any) {
    this.getCustomerData(customerNumber);
    this.isContinueInitiationDisabled = false;
    // Get the value of the "customer" field
    const customerValue = this.recentClaimForm?.get('customer')?.value;

    // Reset the form to its default values
    this.recentClaimForm.reset();
    this.initiateaddEndUserForm();
    // Set the "customer" field back to its original value
    this.recentClaimForm?.get('customer')?.setValue(customerValue);
    this.recentClaimForm.patchValue({
      docType: '16',
    });
    this.recentClaimForm.patchValue({
      docNumber: '',
    });

    this.recentClaimForm.patchValue({
      invoiceDate: '',
    });
    this.getClaimCategories();
    this.getAssociateGlobalAccount();
    this.associateAddInvoiceData = [];

    const modelDiv = document.getElementById('customerModalWarning');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    window.location.reload();
  }



  public claimSelfAssignment() {
    this.initialClaimService
      .getClaimAssignment(this.initiateClaimDetails.claimId, Number(localStorage.getItem("userId")))
      .pipe(
        tap((data) => {
          // Navigate upon successful response
          this.router.navigate(['/claim-detail', this.initiateClaimDetails.claimNumber]);
          console.log('Successful response:', data);
        })
      )
      .subscribe(
        () => { },
        (error) => {
          console.error('Error:', error);
          this.router.navigate(['/claim-detail']);
        }
      );
  }

  public omit_special_char(event: { charCode: any }) {
    var k;
    k = event.charCode; //         k = event.keyCode;  (Both can be used)
    return (
      (k > 64 && k < 91) ||
      (k > 96 && k < 123) ||
      k == 8 ||
      k == 32 ||
      (k >= 48 && k <= 57)
    );
  }

  public closeinitiateclaim(){
    this.CloseModalInitiateClaim();
    this.CloseModalPriorClaimSummary();
    this.recentClaimForm.reset();
    this.resetInvoice();

  }

  public navigateToInitiateClaim() {
    const claimNumberArray = [this.initiateClaimDetails.claimId]; 
    this.initialClaimService.automaticAssignment(claimNumberArray).subscribe(data => {
      if (data.status === 200) {
        const modelDiv = document.getElementById('initiateclaim');
        if (modelDiv != null) {
          modelDiv.style.display = 'none';
          this.router.navigate(['/initiateClaim']).then(() => {
            window.location.reload();
          });
        }
      }
    });
  }
  
  
  public automaticAssigClaimDetail() {
    const claimNumberArray = [this.initiateClaimDetails.claimId]; 
    this.initialClaimService.automaticAssignment(claimNumberArray).subscribe(data=>{
      if(data.status === 200){
        const claimNumber = this.initiateClaimDetails.claimNumber;
    this.router.navigate(['/claim-detail', claimNumber]);
      }
    })
  
  }

  cancelEndUserToggleModalModal() {
    this.recentClaimForm.get('endUser')?.setValue(false);
this.toggleEndUserFlag = false;
    const modelDiv = document.getElementById('endUserToggleModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  
  }
  confirmEndUserToggle(){
    this.recentClaimForm.patchValue({customer:null})
    this.recentClaimForm.patchValue({docNumber:null})
    this.customerData = [];
    this.associateAddInvoiceData = [];
    this.displayMessageRec.customer = '';
    const modelDiv = document.getElementById('endUserToggleModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.toggleEndUserFlag = true;
    this.isContinueInitiationDisabled = false;
    this.initiateaddEndUserForm();
    
}

public fetchNoteTemplate(id:string){
  this.notesTasksService.fetchNoteTemplateBynoteTypeCode(id).subscribe((data:any)=>{
  this.noteText = data.body
  this.noteTextArea = data.body.noteTemplateText
  this.noteTextDisable = !data.body.editable
   })
   }
  

public taxAndFreight(invoiceNumber: any, invoiceDate: any) {
  let date = invoiceDate.split('T')[0];
  return this.initialClaimService.getInvoiceHeader(this.customerData.customerNumber, invoiceNumber, date).pipe(
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
  return this.initialClaimService.getInvoiceCharges(this.customerData.customerNumber, invoiceNumber, date).pipe(
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

public checkAllInvoiceData(checkedColumn:any){
if(checkedColumn){

}else{

}
}




}