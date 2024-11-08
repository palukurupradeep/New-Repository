import {

  Component,

  ElementRef,



  EventEmitter,



  Input,



  Output,



  ViewChildren,

} from '@angular/core';
import {

  FormBuilder,
  FormControl,
  FormControlName,
  FormGroup,
  Validators,
} from '@angular/forms';

import { GenericValidator } from 'src/app/utils/generic-validator';

import { Store } from '@ngrx/store';



import { MatSnackBar } from '@angular/material/snack-bar';
import { InitiateClaimService } from 'src/app/ngrx/store/services/initiate-claim.service';
import { ClaimCategoryDetails, CustomerDetails, CustomerStoreNumbersResult, ReasonCodeByCategory } from 'src/app/interfaces/customer.model';
import { Observable, debounceTime, fromEvent, merge } from 'rxjs';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';



@Component({
  selector: 'app-end-user-claim-detail',
  templateUrl: './end-user-claim-detail.component.html',
  styleUrls: ['./end-user-claim-detail.component.scss']
})
export class EndUserClaimDetailComponent {
  @Input() claimId: any;
  @Input() endUserData: any;
  public addEndUserForm!: FormGroup;
  @Output() refreshClaimDetails = new EventEmitter<string>();
  //public displayCustomerError:Boolean=true;
  @ViewChildren(FormControlName, { read: ElementRef }) // Use with the generic validation message class
  formInputElements!: ElementRef[];
  @Input() claimIsVoid: any = false;

  public isContinueInitiationDisabled: boolean = false;
  public recentClaimForm: FormGroup;
  public dialingCodesList: any = []
  public filteredStates: any = [];
  public selectedCountryHasStates: boolean = false;
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
      phoneHome: FormControl<string | null>;
      home: FormControl<string | null>;
      extHome: FormControl<string | null>;
      email: FormControl<string | null>;
      customer: FormControl<string | null>;
      docNumber: FormControl<string | null>;
      rollNumber: FormControl<string | null>;
    }>;
  };

  public countries: any = [];
  public isoDialingCodes: any = [];
  public selectedDailcode: string = '';
  isAddEndUserFormSubmitted: boolean = false
  tempaddEndUserFormValue: any;
  customerNumber: any;

  constructor(
    private store: Store<{
      initiateClaim: CustomerDetails;
      store: CustomerStoreNumbersResult;
      claimCategory: ClaimCategoryDetails;
      reasonCode: ReasonCodeByCategory;
    }>,
    private inputFormBuilder: FormBuilder,
    private initialClaimService: InitiateClaimService,
    private toasterService: ToasterService
  ) {
    const forbiddenCharacters = /[^\w]/;
    this.recentClaimForm = this.inputFormBuilder.group({
      customer: [
        this.customerNumber,
        [Validators.required, Validators.maxLength(7), Validators.minLength(1)],
      ],
      store: [''],
      altInvoice: [false],
      docType: [16],
      docNumber: [''],
      docDate: [],
      rollNumber: [''],
      claimCategory: [''],
      reasonCode: [''],
      endUser: [false],
      associatedtoGlobalaccount: [168],
      isjobStopped: [false],
      priorityClaim: [false],
      watchList: [false],
      addEndUserObject: [''],
    });
    //this.addEndUserForm = this.inputFormBuilder.group({
    //  firstName: [
    //    '',
    //   [Validators.required, Validators.pattern(forbiddenCharacters)],
    //  ],
    //});

    // Define an instance of the validator for use with this form,
    this.genericValidator = new GenericValidator(); // Defines all of the validation messages for the form.




  }

  initiateaddEndUserForm() {
    this.addEndUserForm = this.inputFormBuilder.group({
      firstName: [this.endUserData?.firstName ],
      middleInitial: [this.endUserData?.middleInitial],
      lastName: [this.endUserData?.lastName ],
      companyName: [this.endUserData?.companyName],
      country: [this.endUserData?.countryId],
      address: [ this.endUserData?.addressLine1],
      addressName: [this.endUserData?.addressLine2],
      city: [
        this.endUserData?.city],
      state: [this.endUserData?.stateId],
      postalCode: [ this.endUserData?.postalCode ],
      county: [this.endUserData?.county],
      phone: [this.endUserData?.businessPhoneDialCodeId ? '+' + this.endUserData?.businessPhoneDialCodeId:''],
      business: [this.endUserData?.businessPhoneNumber],
      extension: [''],
      phoneHome: [this.endUserData?.homePhoneDialCodeId ? '+' + this.endUserData?.homePhoneDialCodeId:''],
      home: [this.endUserData?.homePhoneNumber],
      extHome: [''],
      phoneCell: [this.endUserData?.cellPhoneDialCodeId ? '+' + this.endUserData?.cellPhoneDialCodeId:''],
      cell: [this.endUserData?.cellPhoneNumber],
      extCell: [''],
      email: [this.endUserData?.emailAddress],
    });
    // this.getCountriesList();
  }



  ngOnChanges() {
   
    
    if(this.endUserData){
      this.initiateaddEndUserForm();
      this.getCountryCodeByStates();
    }
  }

  ngOnInit() {
    this.addEndUserForm = this.inputFormBuilder.group({
      firstName: ['',[ Validators.required ]],
      middleInitial: ['', [Validators.maxLength(2)]],
      lastName: ['', [Validators.required ]],
      companyName: [''],
      country: [240,  [Validators.required]],
      address: ['',[ Validators.required ]],
      addressName: [''],
      city: ['',  [Validators.required]],
      state: ['', [Validators.required]],
      postalCode: ['',[Validators.required]],
      county: [''],
      phone: ['+1'],
      business: [''],
      extension: [''],
      phoneHome: ['+1'],
      home: [''],
      extHome: [''],
      phoneCell: ['+1'],
      cell: [''],
      extCell: [''],
      email: ['', [Validators.email]],
    });
    this.getCountriesList();
    this.getCountryCodeByStates();
    this.getDialCodesByCountryId();

  }
  public getCountryCodeByStates() {
    this.filteredStates = [];
    let selccounty = this.countries.find((coun: any) => coun.isoCountryId == this.addEndUserForm.value.country)
    // console.log("country with data:"+JSON.stringify(selccounty))
    if (selccounty?.dialCode?.isoCountryDialingCode) {
      this.addEndUserForm.patchValue({
        phone: "+" + selccounty.dialCode.isoCountryDialingCode,
        phoneHome: "+" + selccounty.dialCode.isoCountryDialingCode,
        phoneCell: "+" + selccounty.dialCode.isoCountryDialingCode,
      });
      this.selectedDailcode = selccounty.dialCode.isoCountryDialingCode;
    } else {
      this.addEndUserForm.patchValue({
        phone: "+1",
        phoneHome: "+1",
        phoneCell: "+1",
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
          console.log( this.filteredStates,' this.filteredStates')
          console.log(this.addEndUserForm.value.country,'....insidestate');
        },
        (error) => {
          // this.store.dispatch(getCountryStateCodeFailure({ error }));
          this.filteredStates = error;
        }
      );
  }
  getSelectedDialCodeByCountryId() {
    this.dialingCodesList.filter((a: any) => {
      if (a.isoCountryDialingCode == this.addEndUserForm.value.country) {

        this.addEndUserForm.patchValue({
          phone: `+${a.isoCountryDialingCode}`,
          phoneHome: `+${a.isoCountryDialingCode}`,
          phoneCell: `+${a.isoCountryDialingCode}`,
        });
        // this.addEndUserForm.patchValue({
        //   phone: "+"+a.isoCountryDialingCode,
        //   phoneHome: "+"+a.isoCountryDialingCode,
        //   phoneCell:"+"+a.isoCountryDialingCode,
        // });
        this.selectedDailcode = a.isoCountryDialingCode;
        console.log("Phone Logs:" + JSON.stringify(this.addEndUserForm.value))
      }
    });

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

  trimAndUpdate(controlName: string) {
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

  public getCountriesList() {
    this.countries = [];
    this.initialClaimService.getCountriesList().subscribe(
      (data: any) => {
        this.countries = data;
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
        }
        let indexToDelete = this.countries.findIndex((item: any) => item.isoCountryId === isoCountryIdToDelete);

        // Check if the object with the specified isoCountryId exists in the array
        if (indexToDelete !== -1) {
          // Remove the object at the found index
          this.countries.splice(indexToDelete, 1);
          // Add the new object at the first index
          this.countries.unshift(newObject);
        }
        // this.addEndUse rForm.get('country')?.setValue('240'); // Set default selected country
        // this.getCountryCodeByStates();
        // this.getDialCodesByCountryId();
        //         this.addEndUserForm.patchValue({
        //   business: '',
        //   home: '',
        //   cell: '',
        //   state: '',
        // });
      },
      (error) => {
        this.countries = error;
      }
    );
  }
  public CloseModalEndUserInfo() {
    const modelDiv = document.getElementById('enduserinfo');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public addFormDetails() {
    if (!this.claimIsVoid) {
      // console.log(
      //   'Rec Add Form data: ' + JSON.stringify(this.addEndUserForm.value)
      // );
      const userIdString = localStorage.getItem('userId');
      const userId = userIdString ? parseInt(userIdString, 10) : null;

      let payload = {}

      if (this.endUserData && this.endUserData.endUserInformationId) {
        // payload= {
        //   'claimId': this.claimId,
        //   'endUserInformation':{
        //     "createdByUserId": this.endUserData ? this.endUserData.createdByUserId : userId,
        //     "modifiedByUserId": this.endUserData ? this.endUserData.modifiedByUserId : userId,
        // "firstName": this.addEndUserForm.value.firstName,
        // "lastName": this.addEndUserForm.value.lastName,
        // "companyName": this.addEndUserForm.value.companyName ? this.addEndUserForm.value.companyName : '',
        // "addressLine1": this.addEndUserForm.value.address,
        // "addressLine2": this.addEndUserForm.value.addressNam ? this.addEndUserForm.value.addressNam : '',
        // "businessPhoneNumber": this.addEndUserForm.value.business ? this.addEndUserForm.value.business.trim() : '',
        // "homePhoneNumber": this.addEndUserForm.value.home ? this.addEndUserForm.value.home.trim() : '',
        // "cellPhoneNumber": this.addEndUserForm.value.cell ? this.addEndUserForm.value.cell.trim() : '',
        // "endUserInformationId":  this.endUserData.endUserInformationId ,
        // "middleInitial": this.addEndUserForm.value.middleInitial && this.addEndUserForm.value.middleInitial !== '\u0000' ? this.addEndUserForm.value.middleInitial:'',
        // "countryId": this.endUserData && this.endUserData.countryId !== 0? this.endUserData.countryId: parseInt(this.addEndUserForm.value.country),
        // "city": this.addEndUserForm.value.city,
        // "stateId": this.endUserData && this.endUserData.stateId !== 0 ? this.endUserData.stateId: parseInt(this.addEndUserForm.value.state),
        // "postalCode": this.addEndUserForm.value.postalCode,
        // "county": this.addEndUserForm.value.county ?this.addEndUserForm.value.county : '',
        // "businessPhoneDialCodeId": this.endUserData ? this.endUserData.businessPhoneDialCodeId : 1,
        // "businessPhoneExtension": this.endUserData ?this.endUserData.businessPhoneExtension : '',
        // "homePhoneDialCodeId": this.endUserData ?this.endUserData.homePhoneDialCodeId : 1,
        // "homePhoneExtension": this.endUserData ?this.endUserData.homePhoneExtension : '',
        // "cellPhoneDialCodeId": this.endUserData ? this.endUserData.cellPhoneDialCodeId : 1,
        // "cellPhoneExtension": this.endUserData ? this.endUserData.cellPhoneExtension: '',
        // "emailAddress":this.addEndUserForm.value.email ? this.addEndUserForm.value.email: '',
        // }
        // }
        payload = {
          'claimId': this.claimId,
          'endUserInformation': {
            "createdByUserId": this.endUserData ? this.endUserData.createdByUserId : userId,
            "modifiedByUserId": this.endUserData ? this.endUserData.modifiedByUserId : userId,
            "firstName": this.addEndUserForm.value.firstName,
            "lastName": this.addEndUserForm.value.lastName,
            "companyName": this.addEndUserForm.value.companyName ? this.addEndUserForm.value.companyName : '',
            "addressLine1": this.addEndUserForm.value.address,
            "addressLine2": this.addEndUserForm.value.addressNam ? this.addEndUserForm.value.addressNam : '',

            // Trimming all spaces (including spaces inside the string)
            "businessPhoneNumber": this.addEndUserForm.value.business ? this.addEndUserForm.value.business.replace(/\s+/g, '') : '',
            "homePhoneNumber": this.addEndUserForm.value.home ? this.addEndUserForm.value.home.replace(/\s+/g, '') : '',
            "cellPhoneNumber": this.addEndUserForm.value.cell ? this.addEndUserForm.value.cell.replace(/\s+/g, '') : '',

            "endUserInformationId": this.endUserData.endUserInformationId,
            "middleInitial": this.addEndUserForm.value.middleInitial && this.addEndUserForm.value.middleInitial !== '\u0000' ? this.addEndUserForm.value.middleInitial : '',
            "countryId": parseInt(this.addEndUserForm.value.country),
            // "countryId": this.endUserData && this.endUserData.countryId !== 0 ? this.endUserData.countryId : parseInt(this.addEndUserForm.value.country),
            "city": this.addEndUserForm.value.city,
             "stateId":parseInt(this.addEndUserForm.value.state),
            // "stateId": this.endUserData && this.endUserData.stateId !== 0 ? this.endUserData.stateId : parseInt(this.addEndUserForm.value.state),
            "postalCode": this.addEndUserForm.value.postalCode,
            "county": this.addEndUserForm.value.county ? this.addEndUserForm.value.county : '',
            "businessPhoneDialCodeId": this.addEndUserForm.value.phone,
            "businessPhoneExtension": this.endUserData ? this.endUserData.businessPhoneExtension : '',
            "homePhoneDialCodeId": this.addEndUserForm.value.phoneHome,
            "homePhoneExtension": this.endUserData ? this.endUserData.homePhoneExtension : '',
            "cellPhoneDialCodeId": this.addEndUserForm.value.phoneCell,
            "cellPhoneExtension": this.endUserData ? this.endUserData.cellPhoneExtension : '',
            "emailAddress": this.addEndUserForm.value.email ? this.addEndUserForm.value.email : ''
          }
        };

      }
      else {
        payload = {
          'claimId': this.claimId,
          'endUserInformation': {
            "createdByUserId": this.endUserData ? this.endUserData.createdByUserId : userId,
            "modifiedByUserId": this.endUserData ? this.endUserData.modifiedByUserId : userId,
            "firstName": this.addEndUserForm.value.firstName,
            "lastName": this.addEndUserForm.value.lastName,
            "companyName": this.addEndUserForm.value.companyName ? this.addEndUserForm.value.companyName : '',
            "addressLine1": this.addEndUserForm.value.address,
            "addressLine2": this.addEndUserForm.value.addressNam ? this.addEndUserForm.value.addressNam : '',
            "businessPhoneNumber": this.addEndUserForm.value.business ? this.addEndUserForm.value.business.trim() : '',
            "homePhoneNumber": this.addEndUserForm.value.home ? this.addEndUserForm.value.home.trim() : '',
            "cellPhoneNumber": this.addEndUserForm.value.cell ? this.addEndUserForm.value.cell.trim() : '',
            "middleInitial": this.addEndUserForm.value.middleInitial && this.addEndUserForm.value.middleInitial !== '\u0000' ? this.addEndUserForm.value.middleInitial : '',
            // "countryId": this.endUserData && this.endUserData.countryId !== 0 ? this.endUserData.countryId : parseInt(this.addEndUserForm.value.country),
            "countryId":parseInt(this.addEndUserForm.value.country),
            "city": this.addEndUserForm.value.city,
            "stateId": this.filteredStates.length == 0 ? '' : parseInt(this.addEndUserForm.value.state),
            // "stateId": this.endUserData && this.endUserData.stateId !== 0 ? this.endUserData.stateId : parseInt(this.addEndUserForm.value.state),
            "postalCode": this.addEndUserForm.value.postalCode,
            "county": this.addEndUserForm.value.county ? this.addEndUserForm.value.county : '',
            "businessPhoneDialCodeId": this.addEndUserForm.value.phone,
            "businessPhoneExtension": this.endUserData ? this.endUserData.businessPhoneExtension : '',
            "homePhoneDialCodeId": this.addEndUserForm.value.phoneHome,
            "homePhoneExtension": this.endUserData ? this.endUserData.homePhoneExtension : '',
            "cellPhoneDialCodeId": this.addEndUserForm.value.phoneCell,
            "cellPhoneExtension": this.endUserData ? this.endUserData.cellPhoneExtension : '',
            "emailAddress": this.addEndUserForm.value.email ? this.addEndUserForm.value.email : '',
          }
        }
      }
      // console.log(payload);
      this.initialClaimService.endUserInformation(payload).subscribe(data => {
        console.log(data);
        if (data) {
          this.refreshClaimDetails.emit();
          // this.endUserData = data;
          this.toasterService.showToast('success', 'End User Information Added Successfully');

        }
        else {
          this.toasterService.showToast('error', 'End User Information Updation Failed');
        }

      });

      if (this.addEndUserForm.invalid) {
        return;
      }
      this.isAddEndUserFormSubmitted = true
      this.tempaddEndUserFormValue = this.addEndUserForm.value;
      //alert(JSON.stringify(this.tempaddEndUserFormValue))
      this.recentClaimForm.patchValue({
        addEndUserObject: this.addEndUserForm.value,
      });
      if (this.recentClaimForm.value.endUser === true) {
        this.isContinueInitiationDisabled = true;

      }
      const modelDiv = document.getElementById('enduserinfo');
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }

    }
  }
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

  }
  ngAfterViewInit(): void {
    this.forValidatorMessageProcess();
  }

  public checkNumValidate() {
    // console.log(this.addEndUserForm.value.business == '' &&
    //   this.addEndUserForm.value.home == '' &&
    //   this.addEndUserForm.value.cell == '','.........enduser')
    return (this.addEndUserForm.value.business == '' || this.addEndUserForm.value.business == null) &&
      (this.addEndUserForm.value.home == '' ||  this.addEndUserForm.value.home == null )&&
      (this.addEndUserForm.value.cell == '' ||  this.addEndUserForm.value.cell == null)
  }
}

