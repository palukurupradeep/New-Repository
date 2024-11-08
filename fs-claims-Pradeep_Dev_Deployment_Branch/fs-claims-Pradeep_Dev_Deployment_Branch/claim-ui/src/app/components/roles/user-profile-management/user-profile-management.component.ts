import {
  Component,
  ElementRef,
  OnInit,
  ViewChildren,
  AfterViewInit,
  ChangeDetectorRef,
  ViewChild,
} from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormControlName,
  FormGroup,
  Validators,
} from '@angular/forms';
import {
  UserRequestObject,
  approvalLimitType,
  selectedGroupObject,
  userApprovalLimit,
  userGroups,
  userMappingFindAllusersDetails,
  UserGroupMap,
  UserGroupDTO,
} from 'src/app/interfaces/user-mapping.model';
import { GenericValidator } from 'src/app/utils/generic-validator';
import { Observable, debounceTime, finalize, fromEvent, merge, switchMap } from 'rxjs';
import { MatTable } from '@angular/material/table';
import { ToasterComponent } from '../../toaster/toaster.component';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { Sort } from '@angular/material/sort';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}
interface User {
  userId: number;
  userGroupId: number;
  managerId: number;
 
}
@Component({
  selector: 'app-user-profile-management',
  templateUrl: './user-profile-management.component.html',
  styleUrls: ['./user-profile-management.component.scss'],
})
export class UserProfileManagementComponent implements OnInit, AfterViewInit {
  public isSideNavCollapsed: boolean = true;
  public collapsed = false;
  public screenWidth = 0;
  public allUsersData: any = [];
  public sortallUsersData: any = [];
  public userData: any = [];
  public sortuserData: any = [];
  public showUserFlag: boolean = false;
  public userInformation: string = '';
  public filterType: any;
  public allUsersItemsPerPage: number = 50; // Default items per page
  public userItemsPerPage: number = 50; // Default items per page
  public allUsersCurrentPage: number = 1;
  public userCurrentPage: number = 1;
  public selectedOption: string = 'default-value';
  public searchText: any = {};
  public originalUsersData: any[] = [];
  public emptyUserData: boolean = false;
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  public columnUserFilterSort: { [key: string]: 'asc' | 'desc' } = {};
  public sortDirection: number = 1; // 1 for ascending, -1 for descending
  public userRowselected: boolean = false;
  public setUserData: any;
  public selEditUser: any;
  public selDeleteUser: any;
  public selStatusUser: any;
  public loading = true; 
  @ViewChild('myTable') myTable!: MatTable<any>;
  public showTable: boolean = false;
  public showTableForm: boolean = false;
  public tableForm!: FormGroup;
  public searcBtn: boolean = false;
  public selectedGroupName: string = '';
  public selectedManagerName: string = '';
  public groupList: userGroups[] = [];
  public searchForm!: FormGroup;
  public viewForm!: FormGroup;
  public editForm!: FormGroup;
  public managerList: any[] = [];
  public saveUser: any;
  public backBtn: boolean = false;
  public recordsData: any = [];
  public groupTable: boolean = false;
  public selectedGroupData = { userGroup: '', managerGroup: '',userGroupId: '', managerId: ''};
  public selectedGroupArray: any[] = [];
  public apprLimitTypes: approvalLimitType[] = [];
  public userApprLimitArray: userApprovalLimit[] = [];
  public userApprLimitObj: any;
  public editSeleGroupArray: any[] = [];
  public edituserGroupsArray: userGroups[] = [];
  public rolesTabData: any = [];
  public userGroupTabData: any[] = [];
  public tabSearchText: any = {};
  public orgUserData: any = [];
  public statusMsg: boolean = false;
  public editSaveUser: any;
  public tempEditFormData: any;
  perPageOptions = [50, 100, 250, 500];
  public UsersRecordsData: any = [];
  public allUsertotalPages: any;
  public editSpinner: boolean = false;
  // @ViewChildren(FormControlName, { read: ElementRef })formInputElements!: ElementRef[];
  @ViewChildren(FormControlName, { read: ElementRef })
  formInputElements!: ElementRef[];
  public displayMessage: { [key: string]: string } = {};
  public displayMessageTableform: { [key: string]: string } = {};
  public validationMessages!: {
    this: FormGroup<{
      firstName: FormControl<string | null>;
      lastName: FormControl<string | null>;
      adId: FormControl<string | null>;
      emailAddress: FormControl<string | null>;
    }>;
  };

  private genericValidator: GenericValidator;
  public childNavValue: boolean = true;
  rowData: any;
  myForm!: FormGroup;
  recordedselectedGroupData: any[]=[];
  saveManagerDataFlag!: boolean;
  editusergroupdropFlag: boolean = false;
  updateUserGrouPopUpFlag: boolean = false;
  existingManagerId: any;
  addButtonDisabled: boolean = true;
  oldAllUsersCurrentPage: number = 1;
  displayedUsers: any;
  prevllUsersCurrentPage: number = 1;
  userGroups: userGroups[] = [];
  userGroupDTO: UserGroupDTO[] = [];
  public searchTextError: boolean = false;
  userSearchType!: string;
  userSearchValue!: string;
  userId!: number;
  isActive: boolean = true;
  formHasChanges: boolean = false;
  constructor(
    private bodyClassService: BodyClassService,
    private userMappingService: UserMappingService,
    private form: FormBuilder,
    private toasterservice: ToasterService,
    private changeDef: ChangeDetectorRef
  ) {
    this.genericValidator = new GenericValidator();
  }

  ngOnInit(): void {
  
    
    this.geAllUsersData(0, 50);
    this.searchForm = this.form.group({
      firstName: [
        '',
        [Validators.maxLength(20), this.firstCharacterValidator()],
      ],
      lastName: [
        '',
        [Validators.maxLength(20), this.firstCharacterValidator()],
      ],
      adId: ['', [Validators.maxLength(32), this.firstCharacterValidator()]],
    });
    this.viewForm = this.form.group({
      viewactiveDirectoryId: [''],
      viewfirstName: [''],
      viewmiddleInitial: [''],
      viewlastName: [''],
      viewemailAddress: [''],
      viewuserName: [''],
      viewfaxNumber: [''],
      viewphoneNumber: [''],
      viewtitle: [''],
      viewuserGroups: [''],
      viewmanagerGroups: [''],
      viewretAuthLimit: [''],
      viewcreditMemoLimit: [''],
      viewinsepctionLimit: [''],
      viewinstalationLimit: [''],
      viewotherChargeLimit: [''],
    });
     this.editForm = this.form.group({
      editactiveDirectoryId: [''],
      editfirstName: [''],
      editmiddleInitial: [''],
      editlastName: [''],
      editemailAddress: ['',[Validators.email]],
      edituserName: [''],
     
      editfaxNumber: ['',[Validators.minLength(4),Validators.maxLength(16),Validators.pattern(/^[0-9]+$/)]],

      editphoneNumber: ['', [Validators.required,Validators.minLength(4),
        Validators.maxLength(16),
        Validators.pattern(/^(?:(?:\+1|1)[ -]?)?\(?[2-9]\d{2}\)?[ -]?\d{3}[ -]?\d{4}$|^(?:(?:\+91|91)?[ -]?)?[789]\d{9}$/),
        Validators.pattern(/^[0-9]*$/) // Only allow numeric characters
      ]],
      edittitle: [''],
      edituserGroups: [''],
      editmanagerGroups: [''],
      editretAuthLimit: [0, [Validators.required,Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
      editcreditMemoLimit: [0, [Validators.required,Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
      editinsepctionLimit: [0, [Validators.required,Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
      editinstalationLimit: [0, [Validators.required,Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
      editotherChargeLimit: [0, [Validators.required,Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
    });
    this.editForm.valueChanges.subscribe(() => {
      this.formHasChanges = this.editForm.dirty;
    });
    this.getGroupsList();
  }

  ngAfterViewInit(): void {
    this.forValidatorMessageProcess();
  }
  // Use with the generic validation message class
  onSubmit() {
    // Handle form submission here
    console.log(this.myForm.value);
  }

  public forValidatorMessageProcess() {
    // console.log(this.formInputElements);
    //Watch for the blur event from any input element on the form.
    const controlBlurs: Observable<any>[] = this.formInputElements.map(
      (formControl: ElementRef) => fromEvent(formControl.nativeElement, 'blur')
    );

    // Merge the blur event observable with the valueChanges observable
    merge(this.searchForm.valueChanges, ...controlBlurs)
      .pipe(debounceTime(100))
      .subscribe((value) => {
        this.displayMessage = this.genericValidator.processMessages(
          this.searchForm
        );
      });
  }

  firstCharacterValidator() {
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

  trimAndUpdate(controlName: string) {
    const control = this.searchForm.get(controlName);
    // const pattern = /^[A-Za-z][A-Za-z0-9.' \-]*$/;
    // const pattern = /[^A-Za-z0-9.'/-\s]/g; 
    const pattern = /^[A-Za-z][A-Za-z0-9.'\-\s]*$/;
    if (control && typeof control.value === 'string') {
      let trimmedValue = control.value.trim();

      if (trimmedValue.length > 64) {
        // Trim the value to a maximum of 64 characters
        trimmedValue = trimmedValue.substring(0, 64);
      }

      if (!pattern.test(trimmedValue)) {
        trimmedValue  = trimmedValue.replace(pattern, "");
      }
      // Update the control value without emitting an event
      control.setValue(trimmedValue, { emitEvent: false });
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
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(
      this.isSideNavCollapsed,
      this.screenWidth
    );
  }
  public openUserModal() {
    const modelDiv = document.getElementById('userModal');
    this.userData = [];
    this.sortuserData = [];
    this.selectedOption = 'default-value';
    this.userInformation = '';
    // if (modelDiv != null) {
    //   modelDiv.style.display = 'block';
    // }
    this.searcBtn = true;
    this.backBtn = true;
    // this.findByUser();
  }

  public closeUserModal() {
    const modelDiv = document.getElementById('userModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.showTable = false;
    this.showTableForm = false;
    this.searcBtn = false;
    this.selectedGroupName = '';
    this.selectedManagerName = '';

    this.searchForm.patchValue({
      firstName: [''],
      lastName: [''],
      adId: [''],
    });
  }

  public geAllUsersData(page: number, pageSize: number) {
    this.loading = true;
    this.userMappingService.getAllUsers(page, pageSize).subscribe(
      (data: any) => {
        this.allUsersData = data.content;
        this.sortallUsersData = data.content.slice()
        this.recordsData = data.totalElements;
        this.allUsertotalPages = data.totalPages - 1;
        this.UsersRecordsData = this.allUsersData.length;
        this.originalUsersData = [...this.allUsersData];
       // this.loading = false;

        console.log('get all users response------>', this.allUsersData);
        console.log('this.UsersRecordsData------>', this.UsersRecordsData);
        console.log(
          'this. this.allUsertotalPages------>',
          this.allUsertotalPages
        );
      },
      (error) => {
        console.log('fetch user details failed: ', error);
      },
      () => {
        this.loading = false; // Set loading to false when the API request completes (regardless of success or failure)
      }
    );
  }

  public onFilterChange(event: any) {
    console.log('chk fiter value----->', event);
    this.filterType = event.target.value;
  }
  public findByUser() {
    let inputVal = '';
    if(this.searchForm.value.firstName){
      inputVal += 'firstName=' + this.searchForm.value.firstName;
      this.userSearchType = 'FirstName';
      this.userSearchValue = this.searchForm.value.firstName;

    }
    if(this.searchForm.value.lastName){
      if(inputVal == ''){
        inputVal += 'lastName=' + this.searchForm.value.lastName;
        this.userSearchType = 'LastName';
        this.userSearchValue = this.searchForm.value.lastName;
      } else {
        inputVal += '&lastName=' + this.searchForm.value.lastName;
        this.userSearchType = 'LastName';
        this.userSearchValue = this.searchForm.value.lastName;
      }
    }
    if(this.searchForm.value.adId){
      if(inputVal == ''){
        inputVal += 'activeDirId=' + this.searchForm.value.adId;
        this.userSearchType = 'ActiveDirectoryId';
        this.userSearchValue = this.searchForm.value.adId;
      } else {
        inputVal += '&activeDirId=' + this.searchForm.value.adId;
        this.userSearchType = 'ActiveDirectoryId';
        this.userSearchValue = this.searchForm.value.adId;
      }
    }
    this.userMappingService
      //.findByUser(this.filterType, this.userInformation)
      .fetchUserFromAD(inputVal)
      .subscribe(
        (data: any) => {
          this.userData = data.slice();
          this.sortuserData = data;
          this.orgUserData = data;
          console.log('find users response------>', this.userData);
          this.showTable = true;
          this.showTableForm = false;
          this.searcBtn = true;
          // this.emptyUserData = this.userData.length == 0 ? true : false;
          this.updateDisplayedUsers();
        },
        (error) => {
          this.userData = [];
          // this.emptyUserData = true;
          console.log(
            'fetch find user details failed: ' + JSON.stringify(this.userData)
          );
        }
      );
      
  }


  sortData(sort: Sort) {
    const data = this.allUsersData.slice(); 
  
    if (!sort.active || sort.direction === '') {
      this.sortallUsersData = data;
      return;
    }
  
    this.sortallUsersData = data.sort((a:any, b:any) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'activeDirectoryId':
          return this.compare(a.activeDirectoryId, b.activeDirectoryId, isAsc);
        case 'firstName':
          return this.compare(a.firstName, b.firstName, isAsc);
        case 'middleInitial':
          return this.compare(a.middleInitial, b.middleInitial, isAsc);
          case 'lastName':
            return this.compare(a.lastName, b.lastName, isAsc);
        case 'emailAddress':
              return this.compare(a.emailAddress, b.emailAddress, isAsc);
           
        default:
          return 0;
      }
    });
  }

  sortUserData(sort: Sort) {
    const data = this.sortuserData.slice(); 
  
    if (!sort.active || sort.direction === '') {
      this.displayedUsers = data;
      return;
    }
  
    this.displayedUsers = data.sort((a:any, b:any) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'activeDirectoryId':
          return this.compare(a.onPremisesSamAccountName, b.onPremisesSamAccountName, isAsc);
        case 'firstName':
          return this.compare(a.givenName, b.givenName, isAsc);
        case 'middleInitial':
          return this.compare(a.middleInitial, b.middleInitial, isAsc);
          case 'lastName':
            return this.compare(a.surname, b.surname, isAsc);
        case 'emailAddress':
              return this.compare(a.userPrincipalName, b.userPrincipalName, isAsc);
           
        default:
          return 0;
      }
    });
  }
  
  
  // Comparison function for sorting
   compare(a: string | number, b: string | number, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }
  onItemsPerPageChange(itemPerPage: boolean) {
    if (itemPerPage) {
      this.allUsersCurrentPage = 1; // Reset to the first page when changing items per page
    } else {
      this.userCurrentPage = 1; // Reset to the first page when changing items per page
    }
  }

  get pagedAllUsersListData() {
    console.log(this.allUsersData, 'data');
    const startIndex =
      (this.allUsersCurrentPage - 1) * this.allUsersItemsPerPage;
    const endIndex = startIndex + this.allUsersItemsPerPage;
    return this.allUsersData.slice(startIndex, endIndex);
  }
  get pagedUsersListData() {
    const startIndex = (this.userCurrentPage - 1) * this.userItemsPerPage;
    const endIndex = startIndex + this.userItemsPerPage;
    // console.log('userData---' + this.userData.slice(startIndex, endIndex));
    return this.userData.slice(startIndex, endIndex);
  }

  // select only one user
  toggleRowSelection(selectedUser: any) {
    selectedUser.selected = !selectedUser.selected;
    for (const user of this.userData) {
      if (user !== selectedUser) {
        user.selected = false;
      }
      if (user == selectedUser) {
        this.setUserData = selectedUser;
      }
    }
    this.userRowselected = this.userData.some(
      (user: { selected: any }) => user.selected
    );
    
  }


  // Add new user
  addSelectedUser() {
    console.log('inside chrome');
    console.log('emailaderrr',this.tableForm.get('emailAddress')?.value)
   
    this.userApprLimitArray = [];
    this.apprLimitTypes.forEach((element) => {
      this.userApprLimitObj = {};
      if (element.approvalLimitTypeCode == 'CHARGES') {
        this.userApprLimitObj = {
          approvalLimitType : element,
          approvalLimit : this.tableForm.value.otherChargeLimit,
          userApprovalLimitId: 0,
        }
        this.userApprLimitArray.push(this.userApprLimitObj);
      } else if (element.approvalLimitTypeCode == 'CREDMEMO') {
        this.userApprLimitObj = {
          approvalLimitType: element,
          approvalLimit: this.tableForm.value.creditMemoLimit,
          userApprovalLimitId: 0,
        }
        this.userApprLimitArray.push(this.userApprLimitObj);
      } else if (element.approvalLimitTypeCode == 'INSPECTION') {
        this.userApprLimitObj = {
          approvalLimitType :element,
          approvalLimit : this.tableForm.value.insepctionLimit,
          userApprovalLimitId: 0,

          }
          this.userApprLimitArray.push(this.userApprLimitObj);
      } else if(element.approvalLimitTypeCode == 'INSTALLATION'){
        this.userApprLimitObj = {
          approvalLimitType :element,
          approvalLimit : this.tableForm.value.instalationLimit,
          userApprovalLimitId: 0,
          }
          this.userApprLimitArray.push(this.userApprLimitObj);
      } else if(element.approvalLimitTypeCode == 'RETAUTH'){
        this.userApprLimitObj = {
          approvalLimitType :element,
          approvalLimit : this.tableForm.value.retAuthLimit,
          userApprovalLimitId: 0,
          }
          this.userApprLimitArray.push(this.userApprLimitObj);
      }
    });
    console.log('Limit Array => ' + JSON.stringify(this.userApprLimitArray));
    this.saveUser = {
      userName:
        this.tableForm.value.userName != undefined
          ? this.setUserData.userName
          : ' ',
      activeDirectoryId:
        this.setUserData.onPremisesSamAccountName != undefined
          ? this.setUserData.onPremisesSamAccountName
          : ' ',
      firstName:
        this.setUserData.givenName != undefined
          ? this.setUserData.givenName
          : ' ',
      middleInitial:
        this.setUserData.middleInitial != undefined
          ? this.setUserData.middleInitial
          : ' ',
      lastName:
        this.setUserData.surname != undefined ? this.setUserData.surname : ' ',
      emailAddress:this.tableForm.get('emailAddress')?.value,
        // this.setUserData.userPrincipalNameclaimId != undefinedaaaass
        //   ? this.setUserData.userPrincipalNameclaimId
        //   : ' ',
      objectId: this.setUserData.id,
      departmentName: '',
      faxNumber:
        this.tableForm.value.faxNumber != undefined
          ? this.tableForm.value.faxNumber 
          : ' ',
      phoneNumber:
        this.tableForm.value.phoneNumber != undefined
          ? this.tableForm.value.phoneNumber
          : ' ',
      title:
        this.tableForm.value.title != undefined ? this.setUserData.title : ' ',
      // userGroups: this.userGroups,
      userApprovalLimit: this.userApprLimitArray,
      // locations: [],
    };
   

   
     this.userMappingService.addUser(this.saveUser).subscribe(
    (data) => {
      if(data.status == 200){
        
        
        console.log(data,'addUserData')
        console.log(this.userGroupDTO,'this.userGroupDTO after call')
        if(this.userGroupDTO.length > 0){
          console.log(data.body.userId,'addUserData...')
         this.saveManagerData(data.body.userId);
        } else {
          this.geAllUsersData(0, 50);
          this.toasterservice.showToast('success', 'User added sucessfully');
    
        
        }
        //this.backToTable();
      } 
    },
    (error) => {
      if(error.status == 400){
        this.toasterservice.showToast('error', 'User already exists');
    
       // this.backToTable()
      }

    });
    this.backToTable();
    // console.log("Final Object => "+JSON.stringify(this.saveUser));
    
  }

  saveManagerData(userId: number) {
    let manObj = {
      userId: userId,
      userGroupDTO: this.userGroupDTO,
    };
    this.userMappingService.addUserGroupMapping(manObj).subscribe((data) => {
      this.geAllUsersData(0, 50);
      this.backToTable();
      this.toasterservice.showToast('success', 'User Added Succesfully');
    
      // console.log('saveManagerData', this.userGroupDTO);
    });
  }

  setTableForm() {
    this.userMappingService.validateUser(this.setUserData.id).subscribe(
      (response) => {
        // Check the response status code
        if (response.status === 200) {
          // Code to execute when the status code is 200
          console.log('Validation successful. Proceeding with the form setup.');
  
          console.log(this.userRowselected, 'userRowselected');
          console.log('Selected User => ' + JSON.stringify(this.setUserData));
          this.showTableForm = true;
          this.showTable = false;
          this.searcBtn = false;
          
          this.tableForm = this.form.group({
            activeDirectoryId: [this.setUserData.onPremisesSamAccountName],
            firstName: [this.setUserData.givenName],
            middleInitial: [this.setUserData.middleInitial],
            lastName: [this.setUserData.surname],
            emailAddress: [this.setUserData.userPrincipalName,[Validators.required, Validators.email]],
            userName: [this.setUserData.userName],
            faxNumber: [this.setUserData.faxNumber,[Validators.pattern(/^[0-9]+$/), Validators.maxLength(16)]],
            phoneNumber: ['', [Validators.required,
              Validators.pattern(/^(?:(?:\+1|1)[ -]?)?\(?[2-9]\d{2}\)?[ -]?\d{3}[ -]?\d{4}$|^(?:(?:\+91|91)?[ -]?)?[789]\d{9}$|^[0-9]*$/),
              Validators.maxLength(16),
            ]],
            
            
            title: [this.setUserData.title],
            userGroups: [''],
            departmentName: [''],
            managerGroups: [''],
            // approvallimittypedescription: [''],
            retAuthLimit: [0, [Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
            creditMemoLimit: [0, [Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
            insepctionLimit: [0, [Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
            instalationLimit:[0, [Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
            otherChargeLimit:[0, [Validators.pattern(/^(0|[1-9]\d{0,12})(\.\d{1,2})?$|^\.\d{1,2}$/)]],
          });
      
          this.tableForm.controls.activeDirectoryId.disable();
          this.tableForm.controls.firstName.disable();
          this.tableForm.controls.lastName.disable();
          this.tableForm.controls.middleInitial.disable();
          this.tableForm.controls.userName.disable();
          this.tableForm.controls.title.disable();
      
        //  this.getGroupsList();
          this.getApprovalLimitTypes();
          this.loadtabFormValidation();
        }
      },
      (error) => {
        
       
  if (error.error.detail === 'User is InActive') {
    const modelDiv = document.getElementById('userInactiveModal');
    if (modelDiv != null) {
        modelDiv.style.display = 'block';
    }
} else if (error.error.detail === 'User already exists') {
    const toastType: 'success' | 'error' | 'warning' = 'error'; 
    const message = 'This user is already present';
    this.toasterservice.showToast(toastType, message);
} else if (error.error.detail === 'User is Deleted') {
  this.isActive = false;
  const modelDiv = document.getElementById('userInactiveModal');
  if (modelDiv != null) {
      modelDiv.style.display = 'block';
  }
}


      
      
        // You may want to show an error message or take other actions
       // this.backToTable();
      }
    );
  }
  
 

  loadtabFormValidation(){
    const controlBlurs: Observable<any>[] = this.formInputElements.map(
      (formControl: ElementRef) => fromEvent(formControl.nativeElement, 'blur')
    );
    merge(this.tableForm.valueChanges, ...controlBlurs)
    .pipe(debounceTime(100))
    .subscribe((value) => {
      this.displayMessageTableform = this.genericValidator.processMessages(
        this.tableForm
      );
    });
  }
  keyPressNumbers(event: any) {
    var charCode = (event.which) ? event.which : event.keyCode;
    // Only Numbers 0-9
    if ((charCode < 48 || charCode > 57)) {
      event.preventDefault();
      return false;
    } else {
      return true;
    }
  }

  keyPress(event: any) {
    const pattern = /[0-9\+\-\(\)\ ]/;
    let inputChar = String.fromCharCode(event.charCode);
    if (event.keyCode != 8 && !pattern.test(inputChar)) {
      event.preventDefault();
    }
  }

 
  showGroupsTable() {
    let curentUserGroupData;
    this.groupTable = true;
    this.selectedGroupData = { userGroup: '', managerGroup: '', userGroupId: '', managerId: '' };
    let obj = { userGroupId: 0, managerId: 0 };
    
    this.groupList.forEach((element) => {
      if (element.userGroupId == this.tableForm.value.userGroups) {
        this.selectedGroupData.userGroup = element.userGroupDescription;
        this.selectedGroupData.userGroupId = element.userGroupId;
        this.userGroups.push(element);
      }
    });
  
    this.managerList.forEach((element) => {
      if (element.userId == this.tableForm.value.managerGroups) {
        this.selectedGroupData.managerGroup =
          element.firstName +
          ' ' +
          element.middleInitial +
          ' ' +
          element.lastName;
        this.selectedGroupData.managerId = element.userId; // Assuming managerId corresponds to userId
      }
    });
  
    // Assuming this.selectedGroupArray, this.userGroupDTO, and this.selectedGroupData are defined in your component

// let curentUserGroupData: any;
let value: boolean = true;

if (this.selectedGroupArray.length > 0) {
  this.selectedGroupArray.forEach((group) => {
    if (group.userGroup === this.selectedGroupData.userGroup) {
      group.managerGroup = this.selectedGroupData.managerGroup;
      value = false;

      this.userGroupDTO.forEach((obj: any) => {
        if (obj.userGroupId === this.tableForm.value.userGroups) {
          obj.managerId = this.tableForm.value.managerGroups;
        }
      });
    }
  });

  if (value) {
    // No matching user group found, so add a new entry
    this.selectedGroupArray.push({ ...this.selectedGroupData });
    curentUserGroupData = { userGroupId: this.tableForm.value.userGroups, managerId: this.tableForm.value.managerGroups };
    this.userGroupDTO.push(curentUserGroupData);
  }
} else {
  // selectedGroupArray is empty, add a new entry
  this.selectedGroupArray.push({ ...this.selectedGroupData });
  curentUserGroupData = { userGroupId: this.tableForm.value.userGroups, managerId: this.tableForm.value.managerGroups };
  this.userGroupDTO.push(curentUserGroupData);
}

console.log(this.selectedGroupArray, 'selectedGroupArray');
console.log(curentUserGroupData, 'curentUserGroupData');

const manObj = {
  userId: this.rowData?.userId,
  userGroupDTO: [
    {
      userGroupId: curentUserGroupData?.userGroupId,
      managerId: curentUserGroupData?.managerId,
    },
  ],
};

console.log(manObj, 'manObjthis.curentUserGroupData');
this.selectedGroupName = '';
this.selectedManagerName= '';
   
  }
  
  getApprovalLimitTypes() {
    this.userMappingService.getApprovalTypes().subscribe((data) => {
      this.apprLimitTypes = data;
    });
  }

  // User Group List
  getGroupsList() {
    this.userMappingService.getAllUserGroups().subscribe((data) => {
      // console.log("Group List =>"+JSON.stringify(data));
      this.groupList = data;
    });
  }

  //  Manager Group List
  public loadSpinner: boolean = false;
  getManagersList() {
    // console.log("List "+JSON.stringify(this.tableForm.value.userGroups));
    if (this.selectedGroupName != '' && this.selectedGroupName != undefined) {
      this.loadSpinner = true;
      this.userMappingService
        .getManagerById(this.tableForm.value.userGroups)
        .subscribe((data) => {
          this.managerList = data;
          this.loadSpinner = false;
        });
    }
  }

  
  onSearch(searchType: string, searchValue: string) {
    console.log('search called',this.searchText.activeDirectoryId)
    
    // Check if all search inputs are empty
    if (
      !this.searchText.activeDirectoryId &&
      !this.searchText.firstName &&
      !this.searchText.lastName &&
      !this.searchText.emailAddress 
    ) {
      // Reset the storeListData to the original data
      // this.allUsersData = [...this.originalUsersData];
      this.allUsersCurrentPage = this.oldAllUsersCurrentPage;
    //  this.mapRoleToUserBasedObPage(this.allUsersCurrentPage, this.allUsersItemsPerPage)
      this.sortallUsersData = this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
      this.searchTextError = false;
    } else {
      console.log('should come isnsied the loop')
      // Check if the search input has a minimum of 3 characters
      if (searchValue.length >= 3) {
        this.userMappingService.findByUser(searchType, searchValue).subscribe(
          (data: any) => {
            this.sortallUsersData = data;
            this.recordsData = this.allUsersData.length;
            this.allUsertotalPages =  this.recordsData ;
            this.oldAllUsersCurrentPage =this.allUsersCurrentPage
            this.allUsertotalPages = Math.ceil(this.allUsertotalPages / this.allUsersItemsPerPage);
            this.allUsersCurrentPage = 1;
            // this.allUsersItemsPerPage = this.recordsData;
            this.searchTextError = false;
            console.log(this.recordsData, 'his.recordsDatalength');
          },
          (error: any) => {
            console.error('Error fetching data:', this.searchTextError);
            this.searchTextError = true;
            this.sortallUsersData = [];
          }
        );
      } else {
        // If the input has less than 3 characters, you might want to handle this case (e.g., display a message).
        console.log('Search input should have a minimum of 3 characters.');
        if(this.sortallUsersData.length == 0){
          this.sortallUsersData = this.originalUsersData;
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

  
  
  onPreviousPageClick(preClick: boolean) {
    console.log('function get called0');
    if (preClick) {
      if (this.allUsersCurrentPage > 1) {
        this.allUsersCurrentPage--;
        this.fetchUsersData(
          this.allUsersCurrentPage,
          this.allUsersItemsPerPage
        ); // Call fetchUsersData for previous page
      }
    } else {
      if (this.userCurrentPage > 1) {
        this.userCurrentPage--;
        this.findByUser();
        // Add similar logic if needed for userCurrentPage
      }
    }
  }

  onNextPageClick(nextClick: boolean) {
    console.log('function get called1');
    
     
        console.log('function get called1 inside');
        const maxPage = Math.ceil(this.userData.length / this.userItemsPerPage);
        if (this.userCurrentPage < maxPage) {
          this.userCurrentPage++;
          this.findByUser();
          
        }
    
  
   
  }

  getPageNumbers(pageNo: boolean): number[] {
    // console.log('getting total oage number');
    if (pageNo) {
      // console.log('inside if');
      // console.log('this.recordsData',this.recordsData);
      const pageCount = Math.ceil(this.recordsData / this.allUsersItemsPerPage);
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    } else {
      // console.log('inside else');
      const pageCount = Math.ceil(this.userData.length / this.userItemsPerPage);
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    }
  }

  onPageNumberClick(page: number, pageClick: boolean) {
    console.log('function get called3click');

    if (pageClick) {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.allUsersCurrentPage = page;
        this.fetchUsersData(page - 1, 50); // Fetch data for the selected page
      }
    } else {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.userCurrentPage = page;
        this.findByUser(); // If required, fetch data for a different user-based page here
      }
    }
  }

  fetchUsersData(page: number, pageSize: number) {
    this.loading = true; // Set loading to true before starting the API request

    this.userMappingService.getAllUsers(page, pageSize).subscribe(
      (data: any) => {
        this.allUsersData = data.content;
        this.recordsData = data.totalElements;
        console.log('get all users response------>', this.allUsersData);
      },
      (error) => {
        console.log('fetch user details failed: ', error);
      },
      () => {
        this.loading = false; // Set loading to false when the API request completes (regardless of success or failure)
      }
    );
  }

  // Delete Model
  // openDeleteModal(user: any) {
  //   this.selDeleteUser = user;
  // }


  openDeleteModal(user:any) {
    this.selDeleteUser = user;
    const modelDiv = document.getElementById('deleteModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'block';
        }
  }
  closeModal(){
    const modelDiv = document.getElementById('deleteModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'none';
        }
  }

  hideModel(modelId: string) {
    const delId = document.getElementById(modelId);
    if (delId != null) delId.style.visibility = 'hidden';
  }

  confirmDeleteModel() {
    this.userMappingService
      .deleteUserById(this.selDeleteUser.userId)
      .subscribe((data) => {
        this.hideModel('deleteModal');
        this.geAllUsersData(0, 50);
        const modelDiv = document.getElementById('deleteModal');
        if (modelDiv != null) {
          modelDiv.style.display = 'none';
        }
        this.toasterservice.showToast('success', 'User  Deleted Successfully');
    
      });
  }

  
  public refUserId: number = 0;
  refreshUser(user:any){
    this.refUserId = user.userId;
  }

  confirmRefreshModel(){
    this.userMappingService.refreshUser(this.refUserId).subscribe( 
      response => {
          this.toasterservice.showToast('success', 'This record refreshed Successfully');
    
          
          window.location.reload();

        const modelDiv = document.getElementById('refreshModel');
        if (modelDiv != null) {
          modelDiv.style.display = 'none';
        }
      }
      
    );
  }


  openViewModel(user: any) {
    this.userGroupTabData = [];
    this.viewForm.patchValue({
      viewactiveDirectoryId: [user.activeDirectoryId],
      viewfirstName: [user.firstName],
      viewmiddleInitial: [user.middleInitial],
      viewlastName: [user.lastName],
      viewemailAddress: [user.emailAddress],
      viewuserName: [user.userName],
      viewfaxNumber: [user.faxNumber],
      viewphoneNumber: [user.phoneNumber],
      viewtitle: [user.title],
      viewuserGroups: [''],
      viewmanagerGroups: [''],
    });
    if(user.userApprovalLimit != null){
      user.userApprovalLimit.forEach((element: any) => {
        if (element.approvalLimitType.approvallimittypecode == 'CHARGES') {
          this.viewForm.patchValue({
            viewotherChargeLimit: [element.approvalLimit],
          });
        } else if (
          element.approvalLimitType.approvallimittypecode == 'CREDMEMO'
        ) {
          this.viewForm.patchValue({
            viewcreditMemoLimit: [element.approvalLimit],
          });
        } else if (
          element.approvalLimitType.approvallimittypecode == 'INSPECTION'
        ) {
          this.viewForm.patchValue({
            viewinsepctionLimit: [element.approvalLimit],
          });
        } else if (
          element.approvalLimitType.approvallimittypecode == 'INSTALLATION'
        ) {
          this.viewForm.patchValue({
            viewinstalationLimit: [element.approvalLimit],
          });
        } else if (element.approvalLimitType.approvallimittypecode == 'RETAUTH') {
          this.viewForm.patchValue({
            viewretAuthLimit: [element.approvalLimit],
          });
        }
      });
    }
    
    let obj = { user: '', manager: ''};
    // user.userGroups.forEach((element: any) => {
    //   obj.user = element.userGroupDescription;
    //   obj.manager = element.manager.firstName + ' ' + element.manager.middleInitial + ' ' + element.manager.lastName;
    //   this.userGroupTabData.push(obj);
    // });
    user.userGroups.forEach((element: any) => {
     
      // Call getManager to fetch managerId and managerGroup
      this.userMappingService
        .getManager(user.userId, element.userGroupId)
        .subscribe((managerData) => {
          let managerId = managerData.userId;
          let managerGroup = `${managerData.firstName} ${managerData.middleInitial} ${managerData.lastName}`;

          let selectedGroupData = {
            userId: user.userId,
            userGroupId: element.userGroupId,
            userGroup: element.userGroupDescription,
            managerId: managerId,
            managerGroup: managerGroup,
          };
console.log(selectedGroupData,'selectedGroupData')
          this.userGroupTabData.push(selectedGroupData)
         
        });
    });
    console.log(this.userGroupTabData,'this.userGroupTabData')
    this.getRolesTableData(user.userId);
    this.viewForm.disable();
  }

  // Adding selected User to Edit Form
    async openEditModel(user: any) {
    console.log(user,"opendusermodal")
  
 
    
    console.log(this.editSeleGroupArray,'this.editSeleGroupArraysdadsadas');
    console.log(this.groupList,' this.groupList')
    console.log(this.editForm.value.edituserGroups,'editForm.value.edituserGroups');
    
    console.log(this.editForm.value.editmanagerGroups,'editForm.value.editmanagerGroups');
    this.rowData = user;
    // console.log(this.rowData, 'this.rowData');

    this.editForm.patchValue({
      editactiveDirectoryId: user.activeDirectoryId,
      editfirstName: user.firstName,
      editmiddleInitial: user.middleInitial,
      editlastName: user.lastName,
      editemailAddress: user.emailAddress,
      edituserName: user.userName,
      editfaxNumber: user.faxNumber,
      editphoneNumber: user.phoneNumber,
      edittitle: user.title,
      edituserGroups: [''],
      editmanagerGroups: [''],
      editretAuthLimit: (user.userApprovalLimit.find(
        (limit: any) =>
          limit.approvalLimitType.approvallimittypecode === 'RETAUTH'
      )?.approvalLimit || 0) as number,
      editcreditMemoLimit: (user.userApprovalLimit.find(
        (limit: any) =>
          limit.approvalLimitType.approvallimittypecode === 'CREDMEMO'
      )?.approvalLimit || 0) as number,
      editinsepctionLimit: (user.userApprovalLimit.find(
        (limit: any) =>
          limit.approvalLimitType.approvallimittypecode === 'INSPECTION'
      )?.approvalLimit || 0) as number,
      editinstalationLimit: (user.userApprovalLimit.find(
        (limit: any) =>
          limit.approvalLimitType.approvallimittypecode === 'INSTALLATION'
      )?.approvalLimit || 0) as number,
      editotherChargeLimit: (user.userApprovalLimit.find(
        (limit: any) =>
          limit.approvalLimitType.approvallimittypecode === 'CHARGES'
      )?.approvalLimit || 0) as number,
    });
   
 
    if (user.userGroups.length > 0) {
      console.log('inside');
      this.editSeleGroupArray = [];
      this.editUserGroupDTO = [];

      //this.getUserGroupAndManager(user.userId)
      await this.getUserGroupAndManager(user.userId);
      
    }
    // console.log(this.editSeleGroupArray, 'this.editSeleGroupArrayData');
    this.editForm.controls.editactiveDirectoryId.disable();
    this.editForm.controls.editfirstName.disable();
    this.editForm.controls.editlastName.disable();
    // this.editForm.controls.editemailAddress.disable();
    this.editForm.controls.editmiddleInitial.disable();
    // this.editForm.controls.editphoneNumber.disable();
    // this.editForm.controls.editfaxNumber.disable();

    this.editForm.controls.edituserName.disable();
    this.editForm.controls.edittitle.disable();

    //this.getGroupsList();
    this.getApprovalLimitTypes();
    this.getRolesTableData(user.userId);
    this.editForm.patchValue({
      edituserGroups: '', // or null
      editmanagerGroups: '' // or null
  });
  }

  closeEditModel(){
    //this.groupList = [];
    this.editSeleGroupArray = [];
    this.rolesTabData = [];
    this.editForm.reset();
    this.searchText = {};
    this.geAllUsersData(0, 50);
  }

  public editUserGroupDTO: UserGroupDTO[] = [];



addEditTabData() {
  console.log('adds selected usergroups and mangers');
  console.log(this.editForm.value.edituserGroups,'this.editForm.value.edituserGroups');
  console.log(this.editForm.value.editmanagerGroups,'this.editForm.value.editmanagerGroups');
  let obj = { userGroupId: 0, managerId: 0 };
  this.selectedGroupData = { userGroup: '', managerGroup: '',userGroupId: '', managerId: ''  };
// Check if userGroup already exists in editSeleGroupArray
const selectedUserGroupId = this.editForm.value.edituserGroups;
console.log(selectedUserGroupId, 'selectedUserGroupIdsadsad');

// Convert userGroupId to string for comparison (if necessary)
const selectedUserGroupIdString = String(selectedUserGroupId);

// Check if selectedUserGroupId exists in any userGroupId in editSeleGroupArray
const matchedGroup = this.editSeleGroupArray.find(group => String(group.userGroupId) === selectedUserGroupIdString);

if (matchedGroup) {
  // Log existing managerId
  this.existingManagerId =matchedGroup.managerId
  console.log('Existing managerId:', this.existingManagerId);

 
 

  
    this.openGroupDataModal();
  }

 else {
    // Add new entry if userGroup doesn't exist in editSeleGroupArray
    this.editSeleGroupArray.push({
      userGroup: this.selectedGroupData.userGroup,
      managerGroup: this.selectedGroupData.managerGroup,
      userId: this.rowData.userId,
      userGroupId: this.editForm.value.edituserGroups,
      managerId: this.editForm.value.editmanagerGroups
    });
  
    console.log(this.editSeleGroupArray, 'this.editSeleGroupArray');
  
    // Assuming 'obj' is defined somewhere in your code
    obj.userGroupId = this.editForm.value.edituserGroups;
    obj.managerId = this.editForm.value.editmanagerGroups;
  
    this.editUserGroupDTO.push(obj);
  
    console.log('userGroupDTO => ', this.editUserGroupDTO);
    console.log('editSeleGroupArray => ' + JSON.stringify(this.editSeleGroupArray));
  
    const manObj = {
      userId: this.rowData.userId,
      userGroupDTO: [
        {
          userGroupId: this.editForm.value.edituserGroups,
          managerId: this.editForm.value.editmanagerGroups
        }
      ]
    };
  
    console.log(manObj, 'manObj');
    this.editsaveManagerData(manObj);
  }
  

  
}

  editsaveManagerData(userGroupData: any){
   
    this.userMappingService.addUserGroupMapping(userGroupData).subscribe(data => {
      
this.addButtonDisabled = true;
      this.geAllUsersData(0, 50);
      this.getUserGroupAndManager(userGroupData.userId);
      this.editForm.patchValue({
        edituserGroups: '', // or null
        editmanagerGroups: '' // or null
    });
    });
  }

  deleteEditRecord() {
  let user = this.selDeleteUser
    this.userMappingService.deleteUserToUserGroupMapping(this.rowData.userId, user.userGroupId, user.managerId)
      .subscribe(
        (rep) => {
          // Remove the item from this.editSeleGroupArray
          this.editSeleGroupArray = this.editSeleGroupArray.filter(
            (group) => group.userGroupId !== user.userGroupId && group.managerId !== user.managerId
          );
          const modelDiv = document.getElementById('deleteModal');
          if (modelDiv != null) {
            modelDiv.style.display = 'none';
          }
  
          // Show success snackbar upon successful deletion
          const toastType: 'success' | 'error' | 'warning' = 'error'; 
    const message = 'The user is disassociated from the user group.';
    this.toasterservice.showToast(toastType, message);
          // this.snackBar.open('The user is disassociated from the user group.', 'Close', {
          //   duration: 3000,
          //   verticalPosition: 'top',
          //   panelClass: ['custom-snackbar'],
          // });
     
        },
        (error) => {
        }
      );
  }
  
 
  

  
  getEditManagersList(){
    
    console.log('functions got called times', this.editForm.value.edituserGroups)
   
    if(this.editForm.value.edituserGroups != '' && this.editForm.value.edituserGroups != undefined){
      this.editSpinner = true;
     
    
      this.userMappingService.getManagerById(this.editForm.value.edituserGroups).subscribe(
        data => {
          this.managerList = data;
          this.editSpinner = false;
          if(this.editForm.value.edituserGroups != '' && this.editForm.value.editmanagerGroups != ''){
            this.addButtonDisabled = true;
                }
        }
      )
    }

  //  this.addButtonDisabled = !this.editForm.value.edituserGroups && !this.editForm.value.editmanagerGroups;
  }

  editSaveModal() {
    // if(this.editForm.invalid){
    //   return;
    // }
    const bodyTag = document.body;
  bodyTag.style.removeProperty('overflow');
 
interface UserApprovalLimit {
  createdByUserId: string;
  modifiedByUserId: string;
  createdDateTime: string;
  modifiedDateTime: string;

approvalLimitType: {
  approvallimittypeid: string;
  approvallimittypecode: string;
  approvallimittypedescription: string;
  statusid: string;
  createdByUserId: string;
  modifiedByUserId: string;
  createdDateTime: string;
  modifiedDateTime: string;
};
  userApprovalLimitId: number;
userId: number;
approvalLimit: number;
statusId: number;

}

const payload = {
  userId: this.rowData.userId,
  //userName: this.rowData.userName,
  // activeDirectoryId: this.rowData.activeDirectoryId,
  // firstName: this.rowData.firstName,
  // middleInitial: this.rowData.middleInitial,
  // lastName: this.rowData.lastName,
  emailAddress: this.editForm.value.editemailAddress,
  // departmentName: this.rowData.departmentName,
  // title: this.rowData.title,
  phoneNumber: this.editForm.value.editphoneNumber,
  faxNumber: this.editForm.value.editfaxNumber,
  // objectId: this.rowData.objectId,
  //userGroups: this.rowData.userGroups || [],
  // locations: [],
  returnAuthorizationLimit: this.editForm.value.editretAuthLimit,
  creditMemoLimit: this.editForm.value.editcreditMemoLimit,
  inspectionServiceLimit: this.editForm.value.editinsepctionLimit,
  installationServiceLimit: this.editForm.value.editinstalationLimit,
  otherChargesLimit: this.editForm.value.editotherChargeLimit

};


this.userMappingService.updateUser(payload).subscribe(
  (response) => {
    this.searchText = {};
    if (response.status === 200) {
      
      this.editSeleGroupArray= [];
      this.closeEditModel();
      this.handlePostRequestResult(response); 
      this.geAllUsersData(0, 50);
    }
    this.toasterservice.showToast('success', 'Changes are saved successfully');
    },
    
  
  (error) => {
    if (error.status === 400) {
      this.handlePostRequestResult(error);
      this.editSeleGroupArray= [];
      this.geAllUsersData(0, 50);
     // this.editsaveManagerData(this.editSaveUser.userId);
    }
    
  }
);




  }

  handlePostRequestResult(response: any) {
    this.geAllUsersData(0, 50);
    const modelDiv = document.getElementById('editModel');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    // this.snackBar.open(response.status === 200 ? 'User Updated Successfully' : 'System is not able to update the user information. Try again.', 'Close', {
    //   duration: 3000,
    //   verticalPosition: 'top',
    //   panelClass: ['custom-snackbar'],
    // });

  }
    
  addUserhandlePostRequestResult(response: any) {
    this.geAllUsersData(0, 50);
    const modelDiv = document.getElementById('userModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    this.toasterservice.showToast('success', response.status === 200 ? 'User added Successfully' : 'This user is already present');
    
  }

 

  getRolesTableData(userId: number) {
    this.userMappingService
      .fetchRolesAndPermissionByUserId(userId)
      .subscribe((data) => {
        this.rolesTabData = data;
        this.rolesTabData.forEach((role: any) => {
          let value = '';
          for (let permission of role.permissions) {
            if (role.permissions.length == 1) {
              value = permission.permissionDescription;
            } else {
              value = permission.permissionDescription + ', ' + value;
            }
          }
          role.users = value;
        });
      });
  }


  openStatusModel(event: Event, user: any) {
    // Prevent the default behavior of the click event
    event.preventDefault();
    if(user.statusId === 1){
      this.statusMsg = true;
    }
    else{
      this.statusMsg =false;
    }

    // Fetch the user data or perform any other desired actions
    console.log('Fetching user data without changing checkbox state:', user);
    this.selStatusUser = user;
  }

  

  confirmStatus() {
    console.log(this.selStatusUser.statusId, 'statusid');

    this.selStatusUser.statusId = this.selStatusUser.statusId === 1 ? 2 : 1;
    console.log(this.selStatusUser.statusId, 'statusidafter');
    this.userMappingService
      .changeUserStatus(this.selStatusUser.userId, this.selStatusUser.statusId)
      .subscribe((data) => {
        console.log(data);
        // this.geAllUsersData(1, 50);
      });
    const message = this.selStatusUser.statusId === 1
  ? 'User Activated Successfully'
  : 'User Deactivated Successfully';

const toastType = this.selStatusUser.statusId === 1 ? 'success' : 'error';

// Trigger the toast with the dynamic message and type
this.toasterservice.showToast(toastType, message);
  
    // const message =
    //   this.selStatusUser.statusId === 1
    //     ? 'User Activated Successfully'
    //     : 'User Deactivated Successfully';
    //     this.toasterservice.showToast('success', 'const message');
    // this.snackBar.open(message, 'Close', {
    //   duration: 3000,
    //   verticalPosition: 'top',
    //   panelClass: ['custom-snackbar'],
    // });
    
    const modelDiv = document.getElementById('statusModal');
    const bodyTag = document.body;
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
      bodyTag.style.removeProperty('overflow');
    }
  }
  statusTogglecancel: boolean = false;

  cancelStatus() {
    const modelDiv = document.getElementById('statusModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  backToTable() {
    this.backBtn = false;
    this.showTable = false;
    this.showTableForm = false;
    this.searcBtn = false;
    this.selectedGroupName = '';
    this.selectedManagerName = '';
    this.userCurrentPage = 1;
    this.userRowselected = false;

    this.searchForm.patchValue({
      firstName: '',
      lastName: '',
      adId: '',
    });
  }

  deleteRec(user: any) {
    console.log('calling from addUser deleteRec', user);
  
    // Delete from userGroups array
    this.userGroups = this.userGroups.filter(
      (group) => group.userGroupDescription !== user.userGroup
    );
  
    // Delete from selectedGroupArray based on both userGroupId and managerId
    this.selectedGroupArray = this.selectedGroupArray.filter(
      (group) => !(group.userGroup === user.userGroup && group.managerId === user.managerId)
    );
  }
  
  editManagersList() {
    // console.log("List "+JSON.stringify(this.tableForm.value.userGroups));
    if (this.selectedGroupName != '' && this.selectedGroupName != undefined) {
      this.userMappingService
        .getManagerById(this.viewForm.value.viewuserGroups)
        .subscribe((data) => {
          this.managerList = data;
        });
    }
  }

  viewGroupsTable() {
    this.groupList.forEach((element) => {
      if (element.userGroupId == this.viewForm.value.viewuserGroups) {
        // this.selectedGroupData.userGroup = element.userGroupDescription;
        this.userGroupTabData.push(element);
      }
    });
    // this.managerList.forEach(element => {
    //   if(element.userId == this.tableForm.value.managerGroups){
    //     this.selectedGroupData.managerGroup = element.firstName + " " + element.middleInitial + " " + element.lastName;
    //   }
    // });
    // this.selectedGroupArray.push(this.selectedGroupData);
  }

  onTabSearch() {
    if (!this.tabSearchText.actId && !this.tabSearchText.lastName && !this.tabSearchText.emailAddress
      && !this.tabSearchText.firstName) {
      this.userData = this.orgUserData;
      this.userCurrentPage =this.prevllUsersCurrentPage;
    } else {
      if (
        this.tabSearchText.actId != undefined &&
        this.tabSearchText.actId != ''
      ) {
        console.log('ActId');
        this.userData = this.orgUserData.filter((user: any) => {
          if (user.onPremisesSamAccountName != null) {
            return user.onPremisesSamAccountName
              .toLowerCase()
              .includes(this.tabSearchText.actId.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.lastName != undefined &&
        this.tabSearchText.lastName != ''
      ) {
        console.log('Last name');
        this.userData = this.orgUserData.filter((user: any) => {
          if (user.surname != null) {
            return user.surname
              .toLowerCase()
              .includes(this.tabSearchText.lastName.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.emailAddress != undefined &&
        this.tabSearchText.emailAddress != ''
      ) {
        console.log('Email address');
        this.userData = this.orgUserData.filter((user: any) => {
          if (user.userPrincipalName != null) {
            return user.userPrincipalName
              .toLowerCase()
              .includes(this.tabSearchText.emailAddress.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.firstName != undefined &&
        this.tabSearchText.firstName != ''
      ) {
        console.log('first name');
        this.userData = this.orgUserData.filter((user: any) => {
          if (user.givenName != null) {
            return user.givenName
              .toLowerCase()
              .includes(this.tabSearchText.firstName.toLowerCase());
          }
        });
      }
      
    }
    this.prevllUsersCurrentPage =this.userCurrentPage
    this.updateDisplayedUsers();
  }
  onPageSizeChange() {
    console.log(this.allUsersItemsPerPage, 'allUsersItemsPerPage');
    console.log(this.allUsersCurrentPage, 'this.allUsersCurrentPage');
    // Call fetchUsersData with the updated page size
    this.fetchUsersData(1, this.allUsersItemsPerPage); // Assuming you want to start from the first page
  }

  onUsersPreviousPageClick() {
    if (this.allUsersCurrentPage > 1) {
      this.allUsersCurrentPage--;
    }

    this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  onUsersNextPageClick() {
    // Assume you have the total number of pages available in a variable totalPages
    if (this.allUsersCurrentPage < this.allUsertotalPages) {
      this.allUsersCurrentPage++;
    }
    // Fetch data for the new page
    this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  onUsersPageNumberClick(page: number | string) {
    if (typeof page === 'number' && page !== this.allUsersCurrentPage) {
      this.allUsersCurrentPage = page;
      this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
    } else {
      // Handle the case when '...' is clicked (optional)
      console.log('Ellipsis clicked or other handling for non-numeric pages.');
    }
  }

  onUsersPageSizeChange() {
    // Reset current page to 1 when changing the page size
    this.allUsersCurrentPage = 1;

    // Fetch data for the new page size
    this.geAllUsersData(this.allUsersCurrentPage, this.allUsersItemsPerPage);
  }

  getUsersPageNumbers(): number[] {
    return Array.from(
      { length: this.allUsertotalPages },
      (_, index) => index + 1
    );
  }

 
  displayedPageNumbers(): (number | string)[] {
    if (this.allUsersData.length === 0) {
      return [1];
    }
  
    const totalPages = this.allUsertotalPages;
    const visiblePages = 5; // Adjust the number of visible pages as needed
    const startPage = Math.max(
      1,
      this.allUsersCurrentPage - Math.floor(visiblePages / 2)
    );
    const endPage = Math.min(totalPages, startPage + visiblePages - 1);
  
    let pages: (number | string)[] = Array.from(
      { length: endPage - startPage + 1 },
      (_, index) => startPage + index
    );
  
    // Add ellipsis and last page number if necessary
    if (endPage < totalPages) {
      pages = [...pages, '...', totalPages];
    }
  
    // Add a link to the first page if the last page is reached
    if (this.allUsersCurrentPage === totalPages && totalPages > visiblePages) {
      pages = [1, '...', ...pages];
    }
  
    return pages;
  }
  
  openGroupDataModal() {
  
    console.log('modal got called')
    const modelDiv = document.getElementById('GroupData');
    if (modelDiv != null) {
     
      modelDiv.style.display = 'block';
    }
   
  }
  

  confirmGroupData() {
   
  
  
    this.userMappingService.deleteUserToUserGroupMapping(this.rowData.userId, this.editForm.value.edituserGroups, this.existingManagerId)
      .pipe(
        switchMap((deleteResponse) => {
          console.log(deleteResponse, 'deleteResponse');
          if (deleteResponse && deleteResponse.status === 200) {
            const manObj = {
              userId: this.rowData.userId,
              userGroupDTO: [
                {
                  userGroupId: this.editForm.value.edituserGroups,
                  managerId: this.editForm.value.editmanagerGroups
                }
              ]
            };
            return this.userMappingService.addUserGroupMapping(manObj);
          } else {
            throw new Error('Error deleting user group mapping or status is not 200.');
          }
        })
      )
      .subscribe(
        (addResponse) => {
          // Check if the add operation was successful
          if (addResponse) {
              this.editForm.patchValue({
      edituserGroups: '', // or null
      editmanagerGroups: '' // or null
  });
            this.geAllUsersData(0, 50);
            this.getUserGroupAndManager(this.rowData.userId);
            this.toasterservice.showToast('success', 'User Groups added Successfully');
   
            // console.log('saveManagerData', this.userGroupDTO);
           this.updateUserGrouPopUpFlag = true;
          } else {
            // Handle the case where addUserGroupMapping was not successful
            console.error('Error adding user group mapping.');
          }
        },
        (addError) => {
          // Handle the error in addUserGroupMapping
          console.error('Error adding user group mapping:', addError);
        }
      );
    
     this.cancelUpdate();
  }
  
  
  cancelUpdate() {
    console.log("got called cancedl")
    // Your logic to handle the cancellation
    const modelDiv = document.getElementById('GroupData');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
  async getUserGroupAndManager(userId: number): Promise<void> {
    const data = await this.userMappingService.getUserGroupAndManager(userId).toPromise();

    this.editSeleGroupArray = data.userGroupDTO;
  //  console.log(this.editSeleGroupArray, 'this.editSeleGroupArrayData');
  
}
  
onUserPageNumberClick(page: number): void {
  this.userCurrentPage = page;
  this.updateDisplayedUsers();
}

onPreviousUserPageClick(): void {
  if (this.userCurrentPage > 1) {
    this.userCurrentPage--;
    this.updateDisplayedUsers();
  }
}

onNextUserPageClick(): void {
  const totalPages = Math.ceil(this.userData.length / this.userItemsPerPage);
  
  if (this.userCurrentPage < totalPages) {
    this.userCurrentPage++;
    this.updateDisplayedUsers();
  }
}



onPageUserSizeChange(): void {
  this.userCurrentPage = 1;
  this.updateDisplayedUsers();
  }


getUserPageNumbers(): number[] {
  const totalItems = this.userData.length;

  // Handle no data scenario
  if (totalItems === 0) {
    this.userCurrentPage = 1; // Set current page to 1 when there's no data
    return [1]; // Return an array with page 1
  }

  const totalPages = Math.ceil(totalItems / this.userItemsPerPage);
  return Array.from({ length: totalPages }, (_, index) => index + 1);
}



updateDisplayedUsers(): void {
  const totalItems = this.userData.length;
  const totalPages = Math.ceil(totalItems / this.userItemsPerPage);

  if (this.userCurrentPage > totalPages) {
    this.userCurrentPage = totalPages;
  }

  const startIndex = (this.userCurrentPage - 1) * this.userItemsPerPage;
  const endIndex = startIndex + this.userItemsPerPage;
  this.displayedUsers = this.userData.slice(startIndex, endIndex);
  console.log('Displayed Users:', this.displayedUsers);
}

// disable/enable search button
 checkFormValue(): boolean{
  if(this.searchForm.value.firstName.trim() ||
     this.searchForm.value.lastName.trim() ||
      this.searchForm.value.adId){
    return false;
  } else {
    return true;
  }
 }
 cancelUserInactiveModal() {
  const modelDiv = document.getElementById('userInactiveModal');
  if (modelDiv != null) {
    modelDiv.style.display = 'none';
  }
}
confirmUserActivStatus(){
 
  this.userMappingService.findByUser(this.userSearchType, this.userSearchValue).subscribe((data:any)=>{
   
    this.userId=data[0].userId;
    this.userSearchType = '';
    this.userSearchValue = '';
    this.userMappingService
    .changeUserStatus(this.userId,1)
    .subscribe((data) => {
    
      // this.geAllUsersData(1, 50);
    });

 

  const message = 'User Activated Successfully';
   
  this.toasterservice.showToast('success', message);
   
  this.cancelUserInactiveModal();
  this.backToTable();
  })
  //this.setUserData.id


}
 public disableConfirmBtn(){
  return this.editForm.get('editretAuthLimit')?.hasError('pattern') || 
  this. editForm.get('editcreditMemoLimit')?.hasError('pattern') ||
  this.editForm.get('editinsepctionLimit')?.hasError('pattern') ||
  this.editForm.get('editinstalationLimit')?.hasError('pattern') || 
  this.editForm.get('editotherChargeLimit')?.hasError('pattern') ||
  this.editForm.get('editphoneNumber')?.hasError('maxlength') ||
  this.editForm.get('editfaxNumber')?.hasError('maxlength') ||
  this.editForm.get('editfaxNumber')?.hasError('pattern') ||
  this.editForm.get('editemailAddress')?.hasError('email') ||
  this.editForm.get('editcreditMemoLimit')?.invalid || 
  this.editForm.get('editretAuthLimit')?.invalid || 
  this.editForm.get('editinsepctionLimit')?.invalid || 
  this.editForm.get('editinstalationLimit')?.invalid || 
  this.editForm.get('editotherChargeLimit')?.invalid ||
  this.editForm.get('editemailAddress')?.invalid
}

public disableAddBtn(){
  if(this.tableForm.invalid){
  return true;
  }else if(this.selectedGroupArray.length == 0){
  return true;
  }else{
     return false;
  }
}

}
