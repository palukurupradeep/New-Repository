import { HttpResponse } from '@angular/common/http';
import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { MatSelectChange } from '@angular/material/select';
import { Sort } from '@angular/material/sort';
import { faL } from '@fortawesome/free-solid-svg-icons';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { Observable } from 'rxjs';
import { CustomerDetails } from 'src/app/interfaces/customer.model';
import { userGroups } from 'src/app/interfaces/user-mapping.model';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
interface Claim {
  claimNbr: string;
  invoiceDetail?: string[];
  rgaDetail?: string[];
  crmDetail?: string[];
  invoiceDetailsArray?: InvoiceDetail[];
  rgaDetailsArray?: RgaDetail[];
  crmDetailsArray?: CrmDetail[];
}
interface InvoiceDetail {
  invoiceNumber: string;
  invoiceDate: string;
  invoiceAmount: string;
}

interface RgaDetail {
  rgaNbr: string;
  rgaDate: string;
  rgaAmount: string;
}

interface CrmDetail {
  crmNumber: string;
  crmDate: string;
  crmAmount: string;
  crmReasonCode: string;
}

@Component({
  selector: 'app-search-claim-history',
  templateUrl: './search-claim-history.component.html',
  styleUrls: ['./search-claim-history.component.scss']
})
export class SearchClaimHistoryComponent {

  selectedOption: any = '';
  customerNumber: string = '';
  // customerDetails: CustomerDetails | null = null;
  customerDetails: any = [];
  isCustomerDetailsVisible: boolean = false;
  public claimStatuses: any = [];
  isReasonCodeSelected: boolean = false;
  searchClaimHistory!: FormGroup;
  filteredOptions: any[] = [];
  reasonCode: any;
  selectedClaimReasonCode: any;
  selectedclaimCategory: number = 7;
  filteredReasonCodes: any;
  isPrimarySearchChecked: boolean = false;
  claimsCount: number = 0;
  isCustomerSelected: boolean = false;
  isInvoiceSelected: boolean = false;
  isRgaSelected: boolean = false;
  isCreditMemoSelected: boolean = false;
  isCustomerNumber: boolean = false;
  selectedStatus: any;
  claimStatusValue: string = '';
  isSearchIconClicked: boolean = false;
  primaryValue: any;
  // getCustomerNumber: String;
  customerData: string = '';

  claimsData: any[] = [];
  loading: boolean = false;
  primarySearchDropDown: any;
  primaryStatusValue: string[] = [];
  PrimarySearchValue: string[] = [];
  bsConfig!: Partial<BsDatepickerConfig>;
  fromDateFutureError = false;
  toDateFutureError = false;
  dateRangeError: boolean = false;
  displayCustomerError: boolean = false;
  displayMessageRec: any = {};
  maxCustomerNumberLength = 7;
  isCustomerDetailsValid: boolean = false;
  secondarySearchOptions: any[] = [];
  consumerSearchForm!: FormGroup;
  rgaDetailsArray: { rgaNbr: string; rgaDate: string; rgaAmount: string }[] = [];
  invoiceDetailsArray: { invoiceNumber: string; invoiceDate: string; invoiceAmount: string; }[] = [];
  crmDetailsArray: { crmNumber: string; crmDate: string; crmAmount: string; crmReasonCode: string }[] = [];
  nonPrimaryValueTypes = ['INS', 'CON', 'USR'];
  inputRestrictionType: 'numeric' | 'alphanumeric' = 'numeric';
  showSecondaryInput: boolean = false;
  public countries: any = [];
  public filteredStates: any = [];
  public selectedDailcode: string = '';
  public selectedCountryHasStates: boolean = false;
  public isoDialingCodes: any = [];
  public dialingCodesList: any = [];
  paginatedClaimsData: any[] = [];
  searchIconClicked: boolean = false;
  public managerList: any[] = [];
  public selectedGroupName: number = 1;
  public selectedManagerName: string = '';
  userId!: number;
  public groupList: userGroups[] = [];
  public userGroupList: any = [];
  public selectedGroup!: number;
  claimUserSearchForm!: FormGroup;
  public userCurrentPage: number = 1;
  public allUsersItemsPerPage: number = 50; // Default items per page
  public userItemsPerPage: number = 50; // Default items per page
  public allUsersCurrentPage: number = 1;
  perPageOptions = [50, 100, 250, 500];
  displayedUsers: any;

  constructor(
    private bodyClassService: BodyClassService,
    private initiateClaimService: InitiateClaimService,
    private claimDetailService: ClaimDetailService,
    private userMapping: UserMappingService,
    private fb: FormBuilder) {
    this.searchForm();

    // Configuration for the date picker

    this.bsConfig = {
      showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
      maxDate: new Date(),
    };

  }


  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }


  ngOnInit() {


    // Fetch data for the primary search drop-down and claim statuses

    this.fetchPrimarySearchData();
    this.fetchClaimStatuses();
    this.getCountriesList();
    this.getDialCodesByCountryId();
    this.getGroupsList();
    this.searchForm();
    // Subscribe to valueChanges of primaryValue
    this.searchClaimHistory.get('primaryValue')?.valueChanges.subscribe(() => {
      // Clear the invalid customer number message when the user starts editing
      if (this.displayMessageRec.customerDetails) {
        this.displayMessageRec.customerDetails = '';
      }

      // If the primary search type is 'CUS' and customer details are visible, clear them
      if (this.searchClaimHistory.get('primarySearchType')?.value === 'CUS' && this.isCustomerDetailsVisible) {
        this.isCustomerDetailsVisible = false;
        this.claimsData = [];
        this.claimsCount = 0;
      }

    });



  }
  // Initialize the search form with validation rules
  searchForm() {

    this.searchClaimHistory = this.fb.group({

      primarySearchType: ['', Validators.required],
      primaryValue: ['', [Validators.required, Validators.maxLength(this.maxCustomerNumberLength)]],
      secondarySearchType: [''],
      secondaryValue: ['', Validators.required],
      claimStatus: [''],
      claimInitiationFromDate: [''],
      claimInitiationToDate: [''],
      claimAmountFrom: [''],
      claimAmountTo: ['']
    }, { validator: this.amountsMatch });

    this.consumerSearchForm = this.fb.group({
      firstName: [''],
      lastName: [''],
      companyName: [''],
      addressLine1: [''],
      addressLine2: [''],
      city: [''],
      country: [''],
      state: [''],
      phoneCountryCode: ['+1'],
      phoneNumber: [''],
      phoneExtension: [''],
      serviceType: ['']
    });

    this.claimUserSearchForm = this.fb.group({
      userGroupId: [''],
      userId: [''],
      managerId: ['']
    });


  }


  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public confirmModal() {
    const modelDiv = document.getElementById('confirmModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public closeModal(id: string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }


  onOptionChange(event: MatSelectChange) {
    const selectedOption = event.value;

    // console.log('Selected lookupCode:', selectedOption);
    this.selectedOption = selectedOption;
    this.displayMessageRec.customerDetails = ''
    // Patch the form control value
    this.searchClaimHistory.patchValue({
      primarySearchType: selectedOption
    });

    // Set flags based on the selected option
    if (this.selectedOption === 'CUS') {
      this.isCustomerSelected = true;
      this.resetSearchForm();
      this.resetConsumerForm();
    } else {
      this.isCustomerSelected = false;
      this.customerDetails = null;
      this.isCustomerDetailsVisible = false;
      this.resetSearchForm();
      this.resetConsumerForm();
    }
    if (selectedOption === 'RSN' || selectedOption === 'ROL' || selectedOption === 'STY' || selectedOption === 'IVS' || selectedOption === 'DYE' || selectedOption === 'PLT') {
      this.inputRestrictionType = 'alphanumeric'; // Allow alphanumeric characters
    } else {
      this.inputRestrictionType = 'numeric'; // Restrict to numeric characters
    }

    // Set the default date range
    if (selectedOption !== 'RM' && selectedOption !== 'DVP') {
      const toDate = new Date(); // Current date
      const fromDate = this.calculatePriorDate(180); // 180 days prior
      this.searchClaimHistory.patchValue({
        claimInitiationFromDate: fromDate,
        claimInitiationToDate: toDate
      });
    } else {
      const toDate = new Date(); // Current date
      const fromDate = this.calculatePriorDate(30); // 30 days prior for 'RM' and 'DVP'
      this.searchClaimHistory.patchValue({
        claimInitiationFromDate: fromDate,
        claimInitiationToDate: toDate
      });
    }



    // Fetch secondary search values from backend API
    this.fetchSecondarySearchOptions(selectedOption);

    console.log("Primary option value:", this.selectedOption);
  }

  // Method to calculate the date X days prior to the current date
  calculatePriorDate(days: number): Date {
    const currentDate = new Date();
    currentDate.setDate(currentDate.getDate() - days);
    return currentDate;
  }

  resetSearchForm() {
    this.searchClaimHistory.patchValue({
      // primarySearchType:'',
      primaryValue: '',
      claimStatus: '',
      claimInitiationFromDate: '',
      claimInitiationToDate: '',
      claimAmountFrom: '',
      claimAmountTo: '',
      secondarySearchType: '',
      secondaryValue: ''
    });
    this.claimsData = []; // Clear table data
    this.claimsCount = 0;
  }

  resetConsumerForm() {
    this.consumerSearchForm.patchValue({
      firstName: '',
      lastName: '',
      companyName: '',
      addressLine1: '',
      addressLine2: '',
      city: '',
      country: '',
      state: '',
      phoneCountryCode: '',
      phoneNumber: '',
      phoneExtension: '',
      serviceType: ''
    })
    this.claimUserForm();
    this.claimsData = []; // Clear table data
    this.claimsCount = 0;
  }
  claimUserForm() {
    this.claimUserSearchForm = this.fb.group({
      userGroupId: [''],
      userId: [''],
      managerId: ['']
    });
    this.claimsData = []; // Clear table data
    this.claimsCount = 0;
  }




  // Fetch data for the primary search drop-down
  fetchPrimarySearchData() {
    this.claimDetailService.getPrimarySearch().subscribe(
      data => {
        // console.log("primary search data : ", data);
        this.primarySearchDropDown = data.body;
        console.log("primary search data values", this.primarySearchDropDown);
      },
      error => {
        console.error("primary search error: ", error);
      }
    );

  }

  fetchSecondarySearchOptions(primarySearchType: string) {


    this.claimDetailService.getSecondarySearch(primarySearchType).subscribe(
      (response) => {

        this.secondarySearchOptions = response.body;
        if (this.secondarySearchOptions.length > 0) {
          this.secondarySearchOptions.unshift({ lookupCode: '', lookupDescription: 'Select Secondary Search' });
        }
        console.log("secoundray search option", this.secondarySearchOptions);


      },
      error => {
        console.error('Error fetching secondary search options', error);
      }
    );
  }

  onSecondarySearchTypeChange(event: MatSelectChange) {
    const selectedValue = event.value;
    // Show input field only if a valid secondary search type is selected
    this.showSecondaryInput = selectedValue && selectedValue !== '';

    // Reset the secondary value field
    this.searchClaimHistory.patchValue({
      secondaryValue: ''
    });
  }



  // Prefix customer number with zeros if it's less than 7 digits
  prefixWithZeros(customerNumber: string): string {
    if (customerNumber.length < 7) {
      const numberOfZerosToAdd = 7 - customerNumber.length;
      return '0'.repeat(numberOfZerosToAdd) + customerNumber;
    } else {
      return customerNumber;
    }
  }
  // Set the customer number in the form and update related states
  setCustomerNumber(customerNumber: string): void {
    if (customerNumber.length > 0) {
      // Prefix the number with zeros
      const prefixedNumber = this.prefixWithZeros(customerNumber);
      this.customerData = prefixedNumber;

      // Update the form control value
      this.searchClaimHistory.patchValue({
        primaryValue: this.customerData
      });
      this.displayCustomerError = false; // Clear any previous error message
    }
  }
  // Fetch customer details based on the customer number
  getCustomerData(customerNumber: string): void {
    this.displayCustomerError = false;
    this.displayMessageRec.customerDetails = ''; // Clear previous error message
    console.log("customer details:");
    console.log("customerNumber", customerNumber);

    if (customerNumber) {
      this.isCustomerDetailsVisible = true;
      this.initiateClaimService.getCustomerDetails(customerNumber)
        .subscribe(
          (response) => {
            if (response) {
              this.customerDetails = response;
              this.customerDetails = this.customerDetails[0];
              console.log('Customer Details:', this.customerDetails);
              console.log(this.customerDetails.customerName)
              this.isCustomerDetailsVisible = true;
              this.displayMessageRec.customerDetails = ''; // Clear previous error message
            }
            else {
              // Clear customer details if no valid data is found
              this.customerDetails = null;
              this.isCustomerDetailsVisible = false;
            }

          },
          (error) => {
            console.error('Error fetching customer details:', error);
            // alert('Customer details not found. Please try again.');
            // Clear customer details and hide the details section on error
            this.customerDetails = null;
            this.isCustomerDetailsVisible = false;

            this.displayCustomerError = false;
            this.displayMessageRec.customerDetails = "Customer Number is invalid. Please provide a valid number";

          }
        );
    } else {
      this.isCustomerDetailsVisible = false;
    }
  }
  // Handle search icon click event
  onSearchIconClick(): void {
    this.isSearchIconClicked = true;
    this.searchIconClicked = true;
    const primaryValue = this.searchClaimHistory.get('primaryValue')!.value;
    this.displayMessageRec.customerDetails = ''; // Clear error message before search

    this.fetchCustomerDetails(primaryValue);
  }
  // Fetch customer details based on the primary value
  fetchCustomerDetails(primaryValue: string): void {
    if (this.isSearchIconClicked) {


      const customerNumber = this.prefixWithZeros(primaryValue);

      this.setCustomerNumber(customerNumber);
      this.getCustomerData(customerNumber);
      this.isSearchIconClicked = false;
    }
  }
  // Handle from date change event
  onFromDateChange(event: Date): void {
    // Prevent event from firing again when patching the value
    this.searchClaimHistory.get('claimInitiationFromDate')?.patchValue(event, { emitEvent: false });
    this.fromDateFutureError = event && event > new Date();
    this.checkDateRange();
  }

  onToDateChange(event: Date): void {
    // Prevent event from firing again when patching the value
    this.searchClaimHistory.get('claimInitiationToDate')?.patchValue(event, { emitEvent: false });
    this.toDateFutureError = event && event > new Date();
    this.checkDateRange();
  }
  checkDateRange(): void {
    const fromDate = this.searchClaimHistory.get('claimInitiationFromDate')?.value;
    const toDate = this.searchClaimHistory.get('claimInitiationToDate')?.value;


    if (fromDate && toDate) {
      this.dateRangeError = new Date(fromDate) > new Date(toDate);
    } else {
      this.dateRangeError = false;
    }
  }
  // Format the date to 'MM/DD/YYYY' format
  formatDate(date: Date): string {
    if (!date) return '';
    const d = new Date(date);
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    const year = d.getFullYear();
    return `${month}/${day}/${year}`;
  }

  autoFormatDate(event: any) {
    let input = event.target.value.replace(/\D/g, '').substring(0, 8); // Allow only numbers and limit to 8 characters
    let formatted = input;

    if (input.length > 2) {
      formatted = input.substring(0, 2) + '/' + input.substring(2);
    }
    if (input.length > 4) {
      formatted = formatted.substring(0, 5) + '/' + input.substring(4);
    }
    event.target.value = formatted;
  }

  // Custom validator to ensure claim amounts match
  amountsMatch(group: FormGroup) {
    const from = group.get('claimAmountFrom')?.value;
    const to = group.get('claimAmountTo')?.value;

    if (from && to && parseFloat(from.replace('$', '').replace(',', '')) > parseFloat(to.replace('$', '').replace(',', ''))) {
      return { amountsMismatch: true };
    }

    return null;
  }
  // Getters for form controls
  get claimAmountFrom() {
    return this.searchClaimHistory.get('claimAmountFrom');
  }

  get claimAmountTo() {
    return this.searchClaimHistory.get('claimAmountTo');
  }




  // Final search function to fetch claim history based on form values
  finalSearch() {


    console.log("final search is called");


    const claimUserSearchForm = {
      "userGroupId": this.claimUserSearchForm.get("userGroupId")?.value,
      "userId": this.claimUserSearchForm.get("userId")?.value,
      "managerId": ''
    }

    const consumerSearchPayload = {
      "firstName": this.consumerSearchForm.get("firstName")?.value,
      "lastName": this.consumerSearchForm.get("lastName")?.value,
      "companyName": this.consumerSearchForm.get("companyName")?.value,
      "country": this.consumerSearchForm.get("country")?.value,
      "state": this.consumerSearchForm.get("state")?.value,
      "phoneCountryCode": this.consumerSearchForm.get("phoneCountryCode")?.value,
      "phoneNumber": this.consumerSearchForm.get("phoneNumber")?.value,
      "phoneExtension": this.consumerSearchForm.get("phoneExtension")?.value,
      "serviceType": this.consumerSearchForm.get("serviceType")?.value,
    }


    const searchPayload = {

      "primarySearchType": this.searchClaimHistory.get("primarySearchType")?.value,
      "primaryValue": this.searchClaimHistory.get('primaryValue')?.value,
      "secondarySearchType": this.searchClaimHistory.get("secondarySearchType")?.value,
      "secondaryValue": this.searchClaimHistory.get("secondaryValue")?.value,
      "claimInitiationFromDate": this.formatDate(this.searchClaimHistory.get('claimInitiationFromDate')?.value),
      "claimInitiationToDate": this.formatDate(this.searchClaimHistory.get('claimInitiationToDate')?.value),
      "claimAmountFrom": this.searchClaimHistory.get("claimAmountFrom")?.value,
      "claimAmountTo": this.searchClaimHistory.get("claimAmountTo")?.value,
      "claimStatus": this.searchClaimHistory.get("claimStatus")?.value,
      "endUserInformation": consumerSearchPayload,
      "claimUser": claimUserSearchForm,
      "isPrimarySearchToggle":this.isPrimarySearchChecked

    }


    console.log("payload", searchPayload);
    this.loading = true;
    this.claimDetailService.getSearchClaimHistory(searchPayload)
      .subscribe(
        (data) => {
          this.claimsData = data.body;
          this.claimsCount = this.claimsData.length;
          console.log("SHOW DATA : ", this.claimsData)
          // Process each claim to handle invoice details
          this.claimsData.forEach((claim: Claim) => {
            if (claim.invoiceDetail && claim.invoiceDetail.length > 0) {
              claim.invoiceDetailsArray = this.InvoiceDetails(claim.invoiceDetail);
            } else {
              claim.invoiceDetailsArray = [];
            }

            if (claim.rgaDetail && claim.rgaDetail.length > 0) {
              // console.log("Raw RGA details:", claim.rgaDetail);
              claim.rgaDetailsArray = this.RgaDetails(claim.rgaDetail);
              // console.log("Processed RGA details:", claim.rgaDetailsArray);
            } else {
              claim.rgaDetailsArray = [];
            }
            if (claim.crmDetail && claim.crmDetail.length > 0) {
              // console.log("Raw CRM details:", claim.crmDetail);
              claim.crmDetailsArray = this.CrmDetails(claim.crmDetail);
              // console.log("Processed CRM details:", claim.crmDetailsArray);
            } else {
              claim.crmDetailsArray = [];
            }
          });
          this.updateDisplayedUsers();
          this.loading = false;
          // this.paginateClaimsData(); // Handle initial pagination

        },
        (error) => {
          console.error('Error fetching customer details:', error);
          this.loading = false;
          // alert('Failed to fetch searchClaim details. Please try again.');
        });

  }

  InvoiceDetails(detailsArray: string[]): any[] {
    return detailsArray.map(detail => {
      const [invoiceNumber, invoiceDate, invoiceAmount] = detail.split(',').map(item => item.trim());
      return {
        invoiceNumber: invoiceNumber,
        invoiceDate: invoiceDate,
        invoiceAmount: invoiceAmount
      };
    });
  }

  RgaDetails(detailsArray: string[]): any[] {
    return detailsArray.map(detail => {
      const [rgaNbr, rgaDate, rgaAmount] = detail.split(',').map(item => item.trim());
      return {
        rgaNbr: rgaNbr,
        rgaDate: rgaDate,
        rgaAmount: rgaAmount
      };
    });
  }


  CrmDetails(detailsArray: string[]): any[] {
    return detailsArray.map(detail => {
      const [crmNumber, crmDate, crmAmount, crmReasonCode] = detail.split(',').map(item => item.trim());
      return {
        crmNumber: crmNumber,
        crmDate: crmDate,
        crmAmount: crmAmount,
        crmReasonCode: crmReasonCode
      };
    });
  }


  // Handle checkbox state change event
  onCheckboxChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.isPrimarySearchChecked = input.checked;

    console.log('Checkbox state changed:', this.isPrimarySearchChecked);
  }
  // Handle change event for the claim status select field
  claimStatus(event: MatSelectChange) {
    const selectedStatus = event.value;
    console.log("Selected claim status item:", selectedStatus);
    this.claimStatusValue = selectedStatus;
    console.log("claimStatusValue:", this.claimStatusValue);
    this.searchClaimHistory.patchValue({
      claimStatus: selectedStatus

    });
    this.fetchClaimStatuses();
  }
  // Fetch data for the claim statuses drop-down
  fetchClaimStatuses() {
    this.claimDetailService.getClaimStatus().subscribe(
      data => {
        console.log("Claim statuses data:", data);
        this.claimStatuses = data.body;
        this.claimStatuses.unshift({ claimStatusCode: '', claimStatusDescription: "All" });
        // Set the default value to "All"

        console.log("Claim statuses values:", this.claimStatuses);
      },
      error => {
        console.error("Error fetching claim statuses:", error);
      }
    );
  }




  isSearchButtonDisabled(): boolean {
    const primarySearchType = this.searchClaimHistory.get('primarySearchType')?.value;
    const primaryValueControl = this.searchClaimHistory.get('primaryValue');

    // If no primary search type is selected or the search icon has not been clicked, disable the button
    if (!primarySearchType) return true;

    // Check if the selected primary search type requires a primary value
    const requiresPrimaryValue = !this.nonPrimaryValueTypes.includes(primarySearchType);

    // Disable the button if:
    // 1. The primary value is required but invalid
    // 2. The primary search type is invalid
    // Return true if either condition is met, otherwise false
    return (requiresPrimaryValue && primaryValueControl?.invalid) || this.searchClaimHistory.get('primarySearchType')?.invalid || false;
  }

  confirmReset() {
    const confirmReset = window.confirm("Are you sure you want to reset the form?");

    if (confirmReset) {
      this.resetForm();
    }
  }

  resetForm() {
    this.searchClaimHistory.reset();
    // this.resetSearchForm();

    this.isCustomerDetailsVisible = false;
    this.claimsData = [];
    this.claimsCount = 0;
    this.consumerSearchForm.reset();
    // this.resetConsumerForm();
    window.location.reload(); // Refresh the page
  }



  getGroupsList() {
    this.userMapping.getAllUserGroups().subscribe((data) => {
      this.groupList = data;
      console.log(this.groupList, "asdfghjsedrfcgvbh");

      if (this.groupList.length > 0) {
        this.selectedGroupName = Number(this.groupList[0].userGroupId);  // Convert to number
        this.claimUserSearchForm.patchValue({
          userGroupId: this.selectedGroupName
        });
      }

      this.getManagersList(this.selectedGroupName);
    });

  }

  getUserGroups(userId: number) {
    this.userMapping.findByUserId(userId).subscribe(data => {
      this.userGroupList = data.userGroups;
      if (this.userGroupList.length > 0) {
        this.selectedGroupName = Number(this.userGroupList[0].userGroupId);  // Convert to number
        this.claimUserSearchForm.patchValue({
          userGroupId: this.selectedGroupName
        });
      }
      this.getManagersList(this.selectedGroupName);

    });
  }

  getManagersList(selectedGroupName: number) {
    console.log('Selected Group Name:', selectedGroupName);
    this.selectedGroupName = selectedGroupName; // Ensure it's a number
    this.claimUserSearchForm.patchValue({
      userGroupId: this.selectedGroupName
    });
    console.log(this.selectedGroupName, 'selectedGroupName');
    if (this.selectedGroupName) {
      this.userMapping.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
        this.managerList = data;

      });
    } else {
      this.managerList = []; // If no group is selected, clear the manager list and selection
      this.selectedManagerName = '';
    }
  }

  onManagerSelected(event: MatSelectChange) {
    // this.userId = Number(this.selectedManagerName);
    this.selectedManagerName = event.value;
    this.claimUserSearchForm.patchValue({
      userId: this.selectedManagerName
    });

  }

  public getCountriesList() {
    // Fetch countries and handle them
    this.countries = [];
    this.initiateClaimService.getCountriesList().subscribe(
      (data) => {
        this.countries = data;

        // Optionally modify the countries list
        const usaCountryObject = this.countries[239];
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

        // Set the default country to United States (assuming 239 is the United States' isoCountryId)
        this.consumerSearchForm.patchValue({
          country: 240
        });
        this.getCountryCodeByStates();
      },
      (error) => {
        this.countries = error;
      }
    );
  }
  public getCountryCodeByStates() {
    this.filteredStates = [];
    const selectedCountryId = this.consumerSearchForm.value.country;

    if (!selectedCountryId) {
      return;  // If no country is selected, skip the state fetching
    }

    const selccounty = this.countries.find((coun: any) => coun.isoCountryId == selectedCountryId);

    if (selccounty?.dialCode?.isoCountryDialingCode) {
      this.consumerSearchForm.patchValue({
        phoneCountryCode: "+" + selccounty.dialCode.isoCountryDialingCode,
        phoneNumber: '',
        phoneExtension: ''
      });
      this.selectedDailcode = selccounty.dialCode.isoCountryDialingCode;
    } else {
      this.consumerSearchForm.patchValue({
        phoneCountryCode: "+1",
        phoneNumber: '',
        phoneExtension: ''
      });
      this.selectedDailcode = "+1";
    }

    this.initiateClaimService.getCountryCodeByStates(selectedCountryId).subscribe(
      (data) => {
        // console.log('Fetched states:', data);
        this.selectedCountryHasStates = Array.isArray(data) && data.length > 0;
        const stateControl = this.consumerSearchForm.get('state');

        this.filteredStates = data;
        // console.log('Filtered states:', this.filteredStates);
      },
      (error) => {
        // this.filteredStates = [];
        this.filteredStates = error;
        // console.error('Error fetching states:', error);
      }
    );
  }
  public getSelectedDialCodeByCountryId() {
    this.dialingCodesList.filter((a: any) => {
      if (a.isoCountryDialingCode == this.consumerSearchForm.value.country) {

        this.consumerSearchForm.patchValue({
          phone: `+${a.isoCountryDialingCode}`,

        });
        this.selectedDailcode = a.isoCountryDialingCode;
      }
    });

  }

  public getDialCodesByCountryId() {
    this.isoDialingCodes = [];
    // this.store.dispatch(getCountryCodeData());
    this.initiateClaimService
      .getDialCodesByCountryId(this.consumerSearchForm.value.country)
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

  sortData(sort: Sort) {
    const data = this.claimsData.slice();
    if (!sort.active || sort.direction === '') {
      this.claimsData = data;
      // this.paginateClaimsData();
      this.updateDisplayedUsers();
      return;
    }

    this.claimsData = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'status':
          return this.compare(a.status, b.status, isAsc);
        case 'claimNbr':
          return this.compare(a.claimNbr, b.claimNbr, isAsc);
        case 'claimDate':
          return this.compare(a.claimDate, b.claimDate, isAsc);
        case 'lastActivityDate':
          return this.compare(a.lastActivityDate, b.lastActivityDate, isAsc);
        case 'customerNbr':
          return this.compare(a.customerNbr, b.customerNbr, isAsc);
        // case 'customerName':
        //   return this.compare(a.customerName, b.customerName, isAsc);
        // case 'scRDT':
        //   return this.compare(a.scRDT, b.scRDT, isAsc); // Adjust this to match your data structure
        // case 'invoice':
        //   return this.compare(a.invoice, b.invoice, isAsc); // Adjust this to match your data structure
        // case 'rgaNbr':
        //   return this.compare(a.rgaNbr, b.rgaNbr, isAsc); // Adjust this to match your data structure
        // case 'debitMemo':
        //   return this.compare(a.debitMemo, b.debitMemo, isAsc); // Adjust this to match your data structure
        // case 'creditMemo':
        //   return this.compare(a.creditMemo, b.creditMemo, isAsc); // Adjust this to match your data structure
        // case 'amount':
        //   return this.compare(a.amount, b.amount, isAsc);
        case 'claimReason':
          return this.compare(a.claimReason, b.claimReason, isAsc);
        // case 'consumerName':
        //   return this.compare(a.consumerName, b.consumerName, isAsc);
        case 'state':
          return this.compare(a.state, b.state, isAsc);
        default:
          return 0;
      }
    });
    // this.paginateClaimsData();
  }

  compare(a: number | string, b: number | string, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  onPreviousUserPageClick(): void {
    if (this.userCurrentPage > 1) {
      this.userCurrentPage--;
      this.updateDisplayedUsers();
    }
  }

  getUserPageNumbers(): number[] {
    const totalItems = this.claimsData.length;
    // Handle no data scenario
    if (totalItems === 0) {
      this.userCurrentPage = 1;
      return [1];
    }
    const totalPages = Math.ceil(totalItems / this.userItemsPerPage);
    const maxDisplayedPages = 5; // Number of pages to display
    let startPage: number, endPage: number;
    if (totalPages <= maxDisplayedPages) {
      // Total pages less than max displayed pages
      startPage = 1;
      endPage = totalPages;
    } else {
      if (this.userCurrentPage <= 3) {
        startPage = 1;
        endPage = maxDisplayedPages;
      } else if (this.userCurrentPage + 2 >= totalPages) {
        startPage = totalPages - 4;
        endPage = totalPages;
      } else {
        startPage = this.userCurrentPage - 2;
        endPage = this.userCurrentPage + 2;
      }
    }
    const pages = Array.from({ length: endPage - startPage + 1 }, (_, index) => startPage + index);
    // Add '...' and last page number if needed
    if (endPage < totalPages) {
      pages.push(-1, totalPages);
    }
    return pages;
  }
  onUserPageNumberClick(page: number): void {
    this.userCurrentPage = page;
    this.updateDisplayedUsers();
  }
  onNextUserPageClick(): void {
    const totalPages = Math.ceil(this.claimsData.length / this.userItemsPerPage);

    if (this.userCurrentPage < totalPages) {
      this.userCurrentPage++;
      this.updateDisplayedUsers();
    }
  }
  onPageUserSizeChange(): void {
    this.userCurrentPage = 1;
    this.updateDisplayedUsers();
  }
  updateDisplayedUsers(): void {
    const totalItems = this.claimsData.length;
    const totalPages = Math.ceil(totalItems / this.userItemsPerPage);

    if (this.userCurrentPage > totalPages) {
      this.userCurrentPage = totalPages;
    }

    const startIndex = (this.userCurrentPage - 1) * this.userItemsPerPage;
    const endIndex = startIndex + this.userItemsPerPage;
    this.displayedUsers = this.claimsData.slice(startIndex, endIndex);
    // console.log('Displayed Users:', this.displayedUsers);
  }


  exportToExcel(): void {

    const claimUserSearchForm = {
      "userGroupId": this.claimUserSearchForm.get("userGroupId")?.value,
      "userId": this.claimUserSearchForm.get("userId")?.value,
      "managerId": ''
    }

    const consumerSearchPayload = {
      "firstName": this.consumerSearchForm.get("firstName")?.value,
      "lastName": this.consumerSearchForm.get("lastName")?.value,
      "companyName": this.consumerSearchForm.get("companyName")?.value,
      "country": this.consumerSearchForm.get("country")?.value,
      "state": this.consumerSearchForm.get("state")?.value,
      "phoneCountryCode": this.consumerSearchForm.get("phoneCountryCode")?.value,
      "phoneNumber": this.consumerSearchForm.get("phoneNumber")?.value,
      "phoneExtension": this.consumerSearchForm.get("phoneExtension")?.value,
      "serviceType": this.consumerSearchForm.get("serviceType")?.value
    }


    const searchPayload = {

      "primarySearchType": this.searchClaimHistory.get("primarySearchType")?.value,
      "primaryValue": this.searchClaimHistory.get('primaryValue')?.value,
      "secondarySearchType": this.searchClaimHistory.get("secondarySearchType")?.value,
      "secondaryValue": this.searchClaimHistory.get("secondaryValue")?.value,
      "claimInitiationFromDate": this.formatDate(this.searchClaimHistory.get('claimInitiationFromDate')?.value),
      "claimInitiationToDate": this.formatDate(this.searchClaimHistory.get('claimInitiationToDate')?.value),
      "claimAmountFrom": this.searchClaimHistory.get("claimAmountFrom")?.value,
      "claimAmountTo": this.searchClaimHistory.get("claimAmountTo")?.value,
      "claimStatus": this.searchClaimHistory.get("claimStatus")?.value,
      "endUserInformation": consumerSearchPayload,
      "claimUser": claimUserSearchForm
    }

    this.claimDetailService.exportToExcel(searchPayload).subscribe(
      (response: HttpResponse<Blob>) => {
        if (response.body) {
          const blob = new Blob([response.body], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' });
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = 'claim-history.xlsx';
          a.click();
          window.URL.revokeObjectURL(url);
        } else {
          console.error('Error: No data received from server.');
        }
      },
      (error) => {
        console.error('Error exporting to Excel', error);
      }
    );
  }



}