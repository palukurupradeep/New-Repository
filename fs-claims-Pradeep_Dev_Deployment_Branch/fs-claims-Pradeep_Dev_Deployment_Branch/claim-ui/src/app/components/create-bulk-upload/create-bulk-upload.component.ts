import { Component,ElementRef,Input, OnInit, ViewChild } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { MatButtonModule } from "@angular/material/button";
import * as XLSX from 'xlsx';
import { BulkUploadService } from 'src/app/ngrx/store/services/bulk-upload.service';
import { DatePipe } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-create-bulk-upload',
  templateUrl: './create-bulk-upload.component.html',
  styleUrls: ['./create-bulk-upload.component.scss']
})
export class CreateBulkUploadComponent implements OnInit {
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true; 
  @ViewChild('fileInput') fileInput!: ElementRef;
  selectedFileName: string = '';
  hasHeader: boolean = true;
  isFileSelected: boolean = false;
    shouldCheckHeader: boolean = false;
  uploadedXlData: any[] = [];
  customerrRollUp:boolean = false;
  deliverCreditMemo:boolean =  false
  batchDescription: string = '';
  validationErrors: string[] = [];
  fileLoaded: boolean = false;
  orginanleUploadedXlData:any[]=[];
  searchText: any = {};
  userId!:number;
  public groupList: any[] = [];
  public selectedGroupName: number = 1;
  public selectedManagerName: string = '';
  public managerList: any[] = [];
  initialSelectedManagerName: string = '';
  selectedGroupId!: number;
  constructor(private bodyClassService: BodyClassService,
    private bulkUploadService:BulkUploadService,
    private datePipe: DatePipe,
    private snackBar: MatSnackBar,
    private router: Router,
    private userMapping: UserMappingService,
    private toasterService: ToasterService) {}

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }
  onFileSelected(event: any) {
    const fileInput = this.fileInput.nativeElement;
    const file = fileInput.files[0];
  
    if (file) {
      this.selectedFileName = file.name;
      this.isFileSelected = true;
  
      // Check if the file has headers by analyzing the first row
      // this.checkFileHasHeaders(file).then(result => {
      //   this.hasHeader = result;
      //   console.log(this.hasHeader,'this.hasHeader')
      // });
    } else {
      this.selectedFileName = '';
      this.isFileSelected = false;
      this.shouldCheckHeader = false;
    }
  }
  

  // async checkFileHasHeaders(file: File): Promise<boolean> {
  //   return new Promise<boolean>((resolve) => {
  //     const reader = new FileReader();
  
  //     reader.onload = (event) => {
  //       const content = (event.target as FileReader).result as string;
  //       const lines = content.split('\n');
  
  //       const requiredColumnNames = [
  //         'PB Payee CustomerNo',
  //         'Invoice Number',
  //         'Claims Invoice Date',
  //         'Style Number',
  //         'Color Number',
  //         'Reason Code',
  //         'Credit Amount Due',
  //         'Nice Uniq ID'
  //       ];
  
  //       const headers = lines[0].split(',').map(header => header.trim().toLowerCase());
  
  //       const hasRequiredHeaders = requiredColumnNames.every(column =>
  //         headers.includes(column.toLowerCase())
  //       );
  
  //       resolve(hasRequiredHeaders);
  //     };
  
  //     reader.readAsText(file);
  //   });
  // }
  
  
  

  discardFile() {
    this.fileInput.nativeElement.value = ''; // Clear the selected file
    this.selectedFileName = '';
    this.isFileSelected = false;
    this.shouldCheckHeader = false;
  }

 
  loadDataFromSpreadsheet() {
    this.fileLoaded = true
    console.log(this.shouldCheckHeader, 'this.shouldCheckHeader)');
  
    const fileInput = this.fileInput.nativeElement;
    const file = fileInput.files[0];
  
    if (file) {
      const reader = new FileReader();
  
      reader.onload = (e: any) => {
        const content = e.target.result;
        const workbook = XLSX.read(content, { type: 'binary' });
  
        // Assuming the first sheet in the workbook is the relevant one
        const sheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[sheetName];
  
        // Convert worksheet to JSON with explicit type annotation
        const jsonData: { [key: string]: any }[] = XLSX.utils.sheet_to_json(worksheet, { header: 1 });
  
        let claimTotal = 0; // Variable to store the total claim amount
  
        const mappedData = jsonData.slice(this.hasHeader ? 1 : 0).map((row: any, index: number) => {
          const lineNumber = this.hasHeader ? index + 1 : index + 1;
          const mappedRow: { [key: string]: any } = {};
  console.log(row,'row2')
          // Map other fields
          mappedRow['LineNumber'] = lineNumber;
         // mappedRow['COMMENTS'] = row[0];
          mappedRow['CUSTOMER_NUMBER'] = row[0];
          mappedRow['INVOICE_NUMBER'] = row[1];
         // mappedRow['INVOICE_DATE'] = row[2];
         const rawInvoiceDate = row[2];  // Rename variable here
         if (typeof rawInvoiceDate === 'number') {
           // Excel date serial number to Date
           const date = new Date(Date.UTC(1900, 0, rawInvoiceDate - 1));
           mappedRow['INVOICE_DATE'] = date.toLocaleDateString('en-US');
         } else {
           mappedRow['INVOICE_DATE'] = rawInvoiceDate;
         }
          mappedRow['STYLE_NUMBER'] = row[3];
          mappedRow['COLOR_NUMBER'] = row[4];
          mappedRow['REASON_CODE'] = row[5];
          mappedRow['AMOUNT'] = row[6];
          mappedRow['INCENTIVE_ID'] = row[7];
        //  mappedRow['ASSOC_INVOICE'] = row[9];
        //  mappedRow['ASSOC_INV_DATE'] = row[10];
         // mappedRow['QUANTITY'] = row[11];
          mappedRow['COMMENTS'] = row[8];
          // mappedRow['UOM'] = row[12];
          // mappedRow['UNIT_PRICE'] = row[13];
          // mappedRow['LIMIT'] = row[14];
  
          // Validation errors for the current row
       //  const validationErrors: string[] = [];
  //Comment validation
const comments = (row[8] ?? "").trim();

if (comments === "") {
    this.validationErrors.push("Claim note cannot be spaces");
}

if (comments.length > 150) {
    this.validationErrors.push("Claim note cannot exceed 150 characters");
}

  
          // Customer Number validation
          // const customerNumber = row[1];
          // if (typeof customerNumber !== 'string') {
          //   this.validationErrors.push("Customer Number must be a numeric");
          // } else {
          //   if (customerNumber.length !== 7) {
          //     this.validationErrors.push("Customer Number must be seven characters");
          //   } else if (isNaN(Number(customerNumber))) {
          //     this.validationErrors.push("Customer Number must be numeric");
          //   }
          // }
          // Customer Number validation
const customerNumber = row[0].toString(); // assuming row is the data object
if (isNaN(Number(customerNumber))) {
    this.validationErrors.push("Customer Number must be numeric");
} else {
  
    if (customerNumber.length !== 7) {
       this.validationErrors.push("Customer Number must be seven characters");
    }
}

  
          // Invoice Number validation
          const invoiceNumber = row[1].toString();
          if (isNaN(Number(invoiceNumber))) {
            this.validationErrors.push("Invoice Number must be a numeric");
          } else {
            if (invoiceNumber.length !== 7) {
              this.validationErrors.push("Invoice Number must be seven characters");
            } 
           
          }
  
          // Invoice Date validation
          // const invoiceDate = row[2];
          // if (!/^(\d{2}\/\d{2}\/\d{4}|\d{4}-\d{2}-\d{2})$/.test(invoiceDate)) {
          //   this.validationErrors.push("Invoice date is invalid");
          // }
  
          // Style Number validation
const styleNumber = row[3].toString() || ""; // Using default value "" if row[4] is undefined
if (styleNumber.length === 0) {
    this.validationErrors.push("Style number is required");
}
if (styleNumber.length > 5) {
    this.validationErrors.push("Style number must be less than 5 characters");
}
   // Color Number validation
   const colorNumber = row[4].toString();
   if (colorNumber.length > 5) {
    this.validationErrors.push("Color # must be five characters or less");
   }
  
          // Reason Code validation
          const reasonCode = row[5].toString();
          if (reasonCode.length !== 3) {
            this.validationErrors.push("Reason code must be three characters");
          }
  
          // Amount validation
          const amount = parseFloat(row[7]);
          claimTotal += amount;
          if (amount <= 0) {
            this.validationErrors.push("Amount must be > 0.00");
          }
  
       
  
//           // Associated Invoice Number validation
//           const assocInvoice = row[9].toString(); // assuming row is the data object
// if (isNaN(Number(assocInvoice))) {
//     this.validationErrors.push("assocInvoice must be numeric");
// } else {
//   console.log(customerNumber,'customerNumberhere')
//   if(customerNumber){
//     if (customerNumber.length !== 7) {
//         this.validationErrors.push("assocInvoice must be seven characters");
//     }
//   }
// }
         
  
          // // Associated Invoice Date validation
          //    // Invoice Date validation
          //    const assocInvDate = row[10];
          //    if (!/^(\d{2}\/\d{2}\/\d{4}|\d{4}-\d{2}-\d{2})$/.test(assocInvDate)) {
          //      this.validationErrors.push("Invoice date is invalid");
          //    }
         
  
          // // Quantity validation
          // const quantity = parseFloat(row[11]);
          // if (quantity <= 0) {
          //   this.validationErrors.push("Quantity must be > 0");
          // }
  
//   // Unit of Measure (UOM) validation
// const uom: string = (row[12] || "").toString().trim();
// if (uom !== "BOX" && uom !== ""){
//   this.validationErrors.push("UOM must be BOX or blank");
// }

          // Unit Price validation
          // const unitPrice = parseFloat(row[13]);
          // if (unitPrice <= 0) {
          //   this.validationErrors.push("Price must be > 0.0000");
          // }
  
          // Throw an error if there are validation errors
          if (this.validationErrors.length > 0) {
            mappedRow['validation_errors'] = this.validationErrors.join('<br>');
          }
  
          return mappedRow;
        });
  
        this.uploadedXlData = mappedData;
        this.orginanleUploadedXlData =mappedData;
        console.log('Data converted to JSON:', mappedData);
      };
  
      reader.readAsBinaryString(file);
    } else {
      console.error('No file selected.');
    }
  }
  
  createBatch(){
    console.log(this.uploadedXlData,'this.uploadedXlData')
    const payloadArray = [{
      userGroupId: this.selectedGroupId,
      //userGroupName: "241",
      batchStatusCode: "U",
      batchDescription: this.batchDescription,
      customerRollUp: this.customerrRollUp,
      deliverCreditMemo: this.deliverCreditMemo,
      printBatch: false,
      createdByUserId: localStorage.getItem('userId'),
      processedByUserId: localStorage.getItem('userId'),
   
      claimBatchDetailsDTOS: this.uploadedXlData.map(item => ({
        customerNumber: item.CUSTOMER_NUMBER,
        claimReasonCode: item.REASON_CODE,
        invoiceNumber: item.INVOICE_NUMBER,
        invoiceDate: this.formatDate(item.INVOICE_DATE),///'2024-01-30',//item.INVOICE_DATE,
        styleNumber: item.STYLE_NUMBER,
        colorNumber: item.COLOR_NUMBER,
        claimAmountUsd: item.AMOUNT,
        claimNote: item.COMMENTS,
        //claimId: 2615,
       creditMemoId: 0,
        incentiveId: item.INCENTIVE_ID,
     //   associateInvoiceNumber: item.INVOICE_NUMBER,
     //   associateInvoiceDate:this.formatDate(item.ASSOC_INV_DATE),//'2024-01-30',// item.INVOICE_DATE,
      //  quantity: item.QUANTITY,
      //  unitOfMeasureCode: item.UOM,
       // unitPriceUsd: item.UNIT_PRICE,
        createdByUserId: localStorage.getItem('userId'),
        processedByUserId: localStorage.getItem('userId'),
      }))
  
  }]
    
    // Now, payloadArray is an array of objects, each representing a payload for an item in this.uploadedXlData
    
console.log(payloadArray,'payloadArray');

   
    this.bulkUploadService.createBatch(payloadArray).subscribe(
      (data) => {
        this.toasterService.showToast('success', ' Bulk Uploaded succesfully');
        this.router.navigate(['/bulk-upload'])
      
      },
      (error) => {
        this.toasterService.showToast('error', 'Failed to load Bulk Upload');
      
      }
    )
  }

  shouldShowDiv(): boolean {
    return this.uploadedXlData.every(item => !item.validation_errors);
  } 
  
  
  

  
  onSearch() {
    console.log('function onSearch called');
    console.log('this.searchText', this.searchText);
    // Check if all search inputs are empty
    if (!this.searchText.lineNumber && !this.searchText.comments && !this.searchText.customer && !this.searchText.invoice) {
      // Reset the uploadedXlData to the original data
      this.uploadedXlData = [...this.orginanleUploadedXlData];
    } else {
      // Filter the data based on the search criteria
      console.log("inside else");
      const filteredData = this.orginanleUploadedXlData.filter((store: any) => {
        console.log(store, 'store');
        console.log(store.LineNumber.toString().toLowerCase(), 'loggg');
        console.log(this.searchText.lineNumber?.toLowerCase(), 'search loggg');
        return (
          (store.LineNumber?.toString().toLowerCase()?.includes(this.searchText.lineNumber?.toLowerCase())) &&
          (store.COMMENTS?.toString().toLowerCase()?.includes(this.searchText.comments?.toLowerCase())) &&
          (store.CUSTOMER_NUMBER?.toString().toLowerCase()?.includes(this.searchText.customer?.toLowerCase())) &&
          (store.INVOICE_NUMBER?.toString().toLowerCase()?.includes(this.searchText.invoice?.toLowerCase()))
        );
        
      });
  
      console.log('originalUploadedXlData Objects:', this.orginanleUploadedXlData);
      // Log the matched objects
      console.log('Matched Objects:', filteredData);
  
      // Update the uploadedXlData with the filtered results
      this.uploadedXlData = filteredData;
    }
  }
  
  
  
  
  

  private formatDate(rawDate: string): string {
    console.log(rawDate,'rawDate') //5/29/2024 rawDate
    // Assuming item.INVOICE_DATE is in 'DD/MM/YYYY' format
    const [month, day, year] = rawDate.split('/');
    const formattedMonth= month.padStart(2,'0');
    const formattedDay= day.padStart(2,'0');
    const formattedDate = `${year}-${formattedMonth}-${formattedDay}`;
    return formattedDate;
  }
  ngOnInit(): void {
   
  
      console.log(this.bodyClassService.selectedManagerName$,'selectedManagerNameincreatebulk')
      this.userId =Number(localStorage.getItem("userId"));
      this.getGroupsList();
  }

  getGroupsList() {
    this.userMapping.getAllUserGroups().subscribe((data) => {
      // console.log("Group List =>"+JSON.stringify(data));
      this.groupList = data;
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
  
    this.getManagersList();
    });
      this.userId = Number(localStorage.getItem('userId'));
    console.log(this.userId, 'Retrieved userId from localStorage');
  
  }
 


  getManagersList() {
    console.log(this.selectedGroupName,'selectedGroupName');
    if (this.selectedGroupName) {
        this.userMapping.getManagerById(Number(this.selectedGroupName)).subscribe((data) => {
            this.managerList = data;
           // this.userId = Number(localStorage.getItem('userId'));
            // Check if managerList contains manager of logged in user
            const loggedInUserManager = this.managerList.find(manager => manager.userId === this.userId);
         
            if (loggedInUserManager) {
              console.log(loggedInUserManager,'loggedInUserManageronLoad')
                this.selectedManagerName = loggedInUserManager.userId; // Set the selected manager to logged in user's manager
                this.initialSelectedManagerName = loggedInUserManager.userId; 
               console.log(this.selectedManagerName,'this.selectedManagerNameonload')
               // this.iniitalSelectedManagerName = `${loggedInUserManager.firstName} ${loggedInUserManager.middleInitial} ${loggedInUserManager.lastName}`;
           
              //  this.onManagerSelected();
              } else {
                this.selectedManagerName = ''; // Clear selection if logged in user's manager is not found
            console.log(this.selectedManagerName ,'this.selectedManagerName ')
              }
        });
    } else {
        this.managerList = []; // If no group is selected, clear the manager list and selection
        this.selectedManagerName = '';
    }
}
  
onManagerSelected() {
  this.userId = Number(this.selectedManagerName);
  //this.userId = Number(this.selectedManager.userId);
  localStorage.setItem('userId', (this.userId).toString())
  // console.log(this.selectedManager, 'selectedManager userId');
}
// getSelectedGroupName(): string {
//   const selectedGroup = this.groupList.find(group => group.userGroupId === this.selectedGroupName);
//   return selectedGroup ? selectedGroup.userGroupDescription : '';
// }
getSelectedGroupName(): string {
  const selectedGroup = this.groupList.find(group => group.userGroupId === this.selectedGroupName);
  
  if (selectedGroup) {
    this.selectedGroupId = selectedGroup.userGroupId; // Store userGroupId in a variable
    return selectedGroup.userGroupDescription;
  }

  return '';
}


getSelectedManagerName(): string {
  const selectedManager = this.managerList.find(manager => manager.userId === this.selectedManagerName);
  return selectedManager 
    ? `${selectedManager.firstName} ${selectedManager.middleInitial} ${selectedManager.lastName}` 
    : '';
}

}