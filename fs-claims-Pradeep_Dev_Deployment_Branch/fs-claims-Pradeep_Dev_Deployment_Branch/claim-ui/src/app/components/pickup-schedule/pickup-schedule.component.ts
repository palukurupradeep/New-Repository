import { Component, Input } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { PickupScheduleService } from 'src/app/ngrx/store/services/pickup-schedule.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-pickup-schedule',
  templateUrl: './pickup-schedule.component.html',
  styleUrls: ['./pickup-schedule.component.scss']
})
export class PickupScheduleComponent {
  claimForm!: FormGroup;
  claimData: any;
  fromDateFutureError: boolean = false;
  toDateFutureError: boolean = false;
  dateRangeError: boolean = false;
  pickupDateFutureError: boolean = false;
  rescheduleDateFutureError: boolean = false;
  bsConfig!: Partial<BsDatepickerConfig>;


  constructor(private bodyClassService: BodyClassService,
    private fb: FormBuilder,
    private pickupScheduleService: PickupScheduleService) {
    // Initialize form with control for claim number
    this.claimForm = this.fb.group({
      claimNumber: [''],
      rganumber: [''],
      pickupDate: [''],
      rescheduleDate: [''],
      scheduleOnRoute: [''],
      quantityOfRGA: ['']
    });

    this.bsConfig = {
      showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
      maxDate: new Date(),
    };

  }

  ngOnInit() {

   
  }

 

  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public openModalnew() {
    const modelDiv = document.getElementById('openModalnew');
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


  fetchClaimDetails() {
    console.log('fetchClaimDetails method triggered');  // Add this log
    const claimNumber = this.claimForm.get('claimNumber')?.value;
    console.log("claimNumber", claimNumber);

    if (claimNumber) {
      console.log('Claim number:', claimNumber);  // Log the claim number
      this.pickupScheduleService.getClaimByNumber(claimNumber).subscribe(data => {
        this.claimData = data;
        console.log('Fetched claim data:', this.claimData);

        this.claimForm.patchValue({
          rganumber:data.rgaNumber,
          pickupDate: data.pickupDate,
          rescheduleDate: data.rescheduleDate,
          quantityOfRGA: data.rgaQuantity,
          scheduleOnRoute: data.routeSchedule


        });
      }, error => {
        console.error('Error fetching claim data :', error);
      });
    } else {
      console.warn('claim number is not provided');
    }

  }

  fetchRgaDetails() {
    const rgaNumber = this.claimForm.get('rganumber')?.value;
    console.log("rgaNumber:", rgaNumber);

    if (rgaNumber) {
      this.pickupScheduleService.getRgaData(rgaNumber).subscribe(data => {
        this.claimData = data;
        console.log('Fetched RGA data:', this.claimData);

        this.claimForm.patchValue({
          claimNumber:data.claimNumber,
          pickupDate: data.pickupDate,
          rescheduleDate: data.rescheduleDate,
          quantityOfRGA: data.rgaQuantity,
          scheduleOnRoute: data.routeSchedule
        });

      }, error => {
        console.error('Error fetching RGA data:', error);  // Handle errors
      });
    } else {
      console.warn('RGA number is not provided');  // Log if no RGA number is entered
    }
  }

  updateClaim(): void {
    if (this.claimForm.valid) {
      const payload = {
        claimNumber: this.claimForm.value.claimNumber,
        rganumber: this.claimForm.value.rganumber,
        pickupDate: this.claimForm.value.pickupDate,
        rescheduleDate: this.claimForm.value.rescheduleDate,
        scheduleOnRoute: this.claimForm.value.scheduleOnRoute,
        quantityOfRGA: this.claimForm.value.quantityOfRGA,
        customerNumber: this.claimData.customerNumber,
        customerName: this.claimData.customerName,
        customerEmail: this.claimData.customerEmail,
        customerPhone: this.claimData.customerPhone,
        claimDate: this.claimData.claimDate,
        rgaDate: this.claimData.rgaDate,
      };
  
      
      console.log('Payload to send:', payload);
      
      
      // this.pickupScheduleService.updateClaim(payload).subscribe(
      //   response => {
      //     console.log('Claim updated successfully:', response);
      //     // Handle successful update response
      //   },
      //   error => {
      //     console.error('Error updating claim:', error);
      //     // Handle error response
      //   }
      // );
    } else {
      console.log('Form is invalid');
      
    }
  }

  // Date change event handler for "From Date"
  onFromDateChange(event: any) {
    const fromDate = new Date(event);
    const today = new Date();
    this.fromDateFutureError = fromDate > today;
    this.checkDateRange();
  }

  // Date change event handler for "To Date"
  onToDateChange(event: any) {
    const toDate = new Date(event);
    const today = new Date();
    this.toDateFutureError = toDate > today;
    this.checkDateRange();
  }

  // Date change event handler for "Date PickUp is Scheduled"
  onPickupDateChange(event: any) {
    const pickupDate = new Date(event);
    const today = new Date();
    this.pickupDateFutureError = pickupDate > today;
  }

  // Date change event handler for "Reschedule Date"
  onRescheduleDateChange(event: any) {
    const rescheduleDate = new Date(event);
    const today = new Date();
    this.rescheduleDateFutureError = rescheduleDate > today;
  }

  // Utility method to check if 'To' date is earlier than 'From' date
  checkDateRange() {
    const fromDate = this.claimForm.get('pickupDate')?.value;
    const toDate = this.claimForm.get('rescheduleDate')?.value;

    if (fromDate && toDate) {
      const fromDateObj = new Date(fromDate);
      const toDateObj = new Date(toDate);
      this.dateRangeError = toDateObj < fromDateObj;
    } else {
      this.dateRangeError = false;
    }
  }

  // Utility method for future date validation
  isFutureDate(date: any): boolean {
    const selectedDate = new Date(date);
    const today = new Date();
    return selectedDate > today;
  }

  // Optional: Method to auto-format the date if needed
  autoFormatDate(event: any) {
    // You can implement logic to format the date string here
    const input = event.target.value;
    console.log('Formatted date:', input);
  }

}
