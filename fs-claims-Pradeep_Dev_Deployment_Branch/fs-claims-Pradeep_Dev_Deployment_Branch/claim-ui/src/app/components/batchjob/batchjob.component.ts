


import { DatePipe, formatDate } from '@angular/common';
import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import {
  FormArray,
  FormBuilder,
  FormControl,
  FormControlName,
  FormGroup,
  Validators,
} from '@angular/forms';
import { BatchjobService } from 'src/app/ngrx/store/services/batchjob.service';
import { MatSort } from '@angular/material/sort';

import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';



interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-batchjob',
  templateUrl: './batchjob.component.html',
  styleUrls: ['./batchjob.component.scss']
})
export class BatchjobComponent implements OnInit, AfterViewInit {

  @ViewChild(MatSort) sort!: MatSort;
  // customSort: { active: string; direction: string } = { active: '', direction: '' };

  constructor(
    private el: ElementRef,
    private bodyClassService: BodyClassService,
    private form: FormBuilder,
    private datePipe: DatePipe,
    private batchjobservice: BatchjobService
  ) { }


  public childNavValue: boolean = true;
  public isSideNavCollapsed: boolean = true;
  public collapsed = false;
  public screenWidth = 0;


  jobForm!: FormGroup;
  isEditMode: boolean = false;
  isViewMode: boolean = false;
  // public backBtn: boolean = false;
  public searchBtn: boolean = false;
  public showTable: boolean = false;


  loading: boolean = true;

  checkedDays: string[] = [];

  jobs: any = [];
  filteredJobs: any = [];
  searchJobName: string = '';
  searchFrequency: string = '';
  showDailyOptions = false;
  showWeeklyOptions = false;
  showMonthlyOptions = false;
  showYearlyOptions = false;

  selectedWeeklyDay: string | null = null;

  // Variables for selections
  selectedDays: { [key: string]: boolean } = {};
  selectedMonths: { [key: string]: boolean } = {};


  minuteOptions: string[] = Array.from({ length: 60 }, (_, i) => i.toString().padStart(2, '0'));
  hourOptions: string[] = Array.from({ length: 24 }, (_, i) => i.toString().padStart(2, '0'));

  daysOfWeek = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
  monthsOfYear = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];

  timeOptions: string[] = [];
  selectedJob: any[] = [];
  currentJobId: number | null = null;
  currentConfigurationKey: string = '';

  //pages
  paginatedJobs: any[] = []; // Jobs to display for current page
  currentPage: number = 1;
  totalPages: number = 0;
  totalItems: number = 0;
  itemsPerPage: number = 10; // Default page size
  perPageOptions: number[] = [5, 10, 20];


  bsConfig = {
    showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
    adaptivePosition: true
  };
  ngOnInit() {

    this.fetchJobs();
    this.jobForm = this.form.group({
      jobName: ['', Validators.required],
      jobDescription: [''],
      status: [''],
      alertEmail: ['', [Validators.required, Validators.email]],
      executionResult: [''], // Only for view mode
      error: [''],           // Only for view mode
      frequency: ['noOption'],
      schedule: ['noSchedule'], // Updated from 'time' to 'schedule'
      weeklyOccurrence: this.form.array([]), // For weekly selected days
      monthlyOccurrence: [''],
      yearlyOccurrence: [''],
      yearlyStartDate: [null],
      monthlyStartDate: [null],
      hour: [''],
      minute: [''],
    });
    


    // this.generateTimeSlots();
    if (this.isEditMode && this.jobs) {
      this.setFormValues(this.jobs);
    }
  }
  ngAfterViewInit() {
    // Assign MatSort values to custom sort object
    this.sort.active = this.sort.active;
    this.sort.direction = this.sort.direction;
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

  // Calculate total pages based on total items and items per page
  calculateTotalPages(): void {
    this.totalPages = Math.ceil(this.totalItems / this.itemsPerPage);
  }

  // Paginate filtered jobs
  paginateJobs(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedJobs = this.filteredJobs.slice(startIndex, endIndex); // Use filtered jobs for pagination
  }

  // Go to the previous page
  onJobsPreviousPageClick(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.paginateJobs();
    }
  }

  // Go to the next page
  onJobsNextPageClick(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.paginateJobs();
    }
  }

  // Go to a specific page
  onJobsPageNumberClick(page: number): void {
    this.currentPage = page;
    this.paginateJobs();
  }

  // Handle page size change
  onJobsPageSizeChange(): void {
    this.calculateTotalPages();
    this.currentPage = 1; // Reset to the first page when page size changes
    this.paginateJobs();
  }

  // Display page numbers dynamically
  displayedPageNumbers(): number[] {
    return Array.from({ length: this.totalPages }, (_, index) => index + 1);
  }
  fetchJobs(): void {
    this.batchjobservice.getJobs().subscribe(
      (data: any[]) => {
        this.jobs = data; // Assign the data from API to jobs array
        this.filteredJobs = this.jobs;
        this.filterJobs();
        console.log(data);

        this.sortData(this.sort?.active, this.sort?.direction);
        this.loading = false; // Stop loading spinner once data is fetched
      },
      (error) => {
        console.error('Error fetching jobs:', error); // Handle error
        this.loading = false; // Stop loading spinner even if error occurs
      }
    );
  }

  closeForm() {
    this.resetForm();
    this.isViewMode = false;
    this.isEditMode = false;

  }
  
  // Function to filter jobs based on search term
  filterJobs(): void {
    const searchJobName = this.searchJobName.toLowerCase();
    const searchFrequency = this.searchFrequency.toLowerCase();
  
    // Ensure at least 3 characters are entered in either search field before proceeding
    if (searchJobName.length >= 3 || searchFrequency.length >= 3) {
      // Proceed with filtering only if there are at least 3 characters
      this.filteredJobs = this.jobs
        .filter((job: any) =>
          (searchJobName === '' || job.jobName.toLowerCase().startsWith(searchJobName)) &&
          (searchFrequency === '' || job.jobFrequency.toLowerCase().startsWith(searchFrequency))
        )
        .sort((a: any, b: any) => {
          if (a.jobName.toLowerCase().startsWith(searchJobName) && !b.jobName.toLowerCase().startsWith(searchJobName)) {
            return -1;
          }
          if (!a.jobName.toLowerCase().startsWith(searchJobName) && b.jobName.toLowerCase().startsWith(searchJobName)) {
            return 1;
          }
          return 0;
        });

        
  
      // // Update totalItems, calculate total pages, and paginate
      this.totalItems = this.filteredJobs.length;
      this.calculateTotalPages();
      this.paginateJobs();
    } else {
      this.sortData(this.sort?.active, this.sort?.direction);
      // If input is less than 3 characters, reset to show all jobs
      // this.filteredJobs = this.jobs; 
      this.totalItems = this.filteredJobs.length;
      this.calculateTotalPages();
      this.paginateJobs();
      
    }
  }

  sortData(active: string, direction: string) {
    console.log(active,"........----");
    console.log(direction,".........---");

    const data = this.filteredJobs.slice(); 
    
    if (!active || direction === '') {
      this.filteredJobs = this.jobs.slice(); // Reset to original data
      this.totalItems = this.filteredJobs.length; // Update total items
      this.calculateTotalPages(); // Recalculate total pages
      this.paginateJobs(); // Paginate the original jobs
      return;
    }
  
    this.filteredJobs = data.sort((a: { jobName: string | number; jobFrequency: string | number; }, b: { jobName: string | number; jobFrequency: string | number; }) => {
      const isAsc = direction === 'asc';
      switch (active) {
        case 'jobName':
          return this.compare(a.jobName, b.jobName, isAsc);
        case 'frequency':
          return this.compare(a.jobFrequency, b.jobFrequency, isAsc);
        default:
          return 0;
      }
    });
    this.totalItems = this.filteredJobs.length; // Update total items
  this.calculateTotalPages(); // Recalculate total pages
  this.paginateJobs(); // Paginate the sorted jobs
    
  }
  
  // Comparison function for sorting
  
  compare(a: string | number, b: string | number, isAsc: boolean) {
    // If both values are strings, convert them to lowercase for case-insensitive comparison
    if (typeof a === 'string' && typeof b === 'string') {
      a = a.toLowerCase();
      b = b.toLowerCase();
    }
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
  }
  
  onKeyPress(event: Event): void {
    // Cast the event to KeyboardEvent
    const keyboardEvent = event as KeyboardEvent;
  
    // Prevent the input of space character (key code 32)
    if (keyboardEvent.keyCode === 32) {
      keyboardEvent.preventDefault();
    }
  }


  addJob() {
    if (this.jobForm.invalid) {
      console.error('Form is invalid');
      this.jobForm.markAllAsTouched(); // Marks all fields as touched to trigger validation messages
      return;
    }
    // Fetch values from the form first
    console.log(this.jobForm.value);
    const newJob = {
      jobId: '',
      jobName: this.jobForm.get('jobName')?.value || '',
      jobDescription: this.jobForm.get('jobDescription')?.value || '',
      jobFrequency: this.jobForm.get('frequency')?.value || '',
      alertEmail: this.jobForm.get('alertEmail')?.value || '',
      configurationKey: '',
      configurationValue: this.generateCronExpression(this.jobForm.value),
      statusId: this.jobForm.get('status')?.value ? 1 : 2
    };

    console.log(newJob); // Log the job details
    this.batchjobservice.addJob(newJob).subscribe(
      (response) => {
        console.log('Job added successfully', response);
        this.fetchJobs();
      },
      (error) => {
        console.error('Error adding job', error);
        // Handle error response (e.g., show an error message)
      }
    );

  }


  saveChanges(): void {
    // Check if form is valid
    if (this.jobForm.valid) {
      const formValue = this.jobForm.value;

      // Construct the payload from form values
      const payload = {
        jobId: this.isEditMode ? this.currentJobId : '',
        jobName: formValue.jobName,
        jobDescription: formValue.jobDescription,
        jobFrequency: formValue.frequency, // Frequency will be a string like 'Daily', 'Monthly', etc.
        alertEmail: formValue.alertEmail,
        configurationKey: this.currentConfigurationKey || '',
        configurationValue: this.generateCronExpression(formValue), // Use a method to generate a cron string based on the schedule
        statusId: formValue.status // Convert form control value to numeric status
      };
      console.log(payload);
      // Send the payload to the backend via your service
      this.batchjobservice.updateJob(payload).subscribe(
        (response) => {
          console.log('Job saved successfully:', response);
          this.fetchJobs();
        },
        (error) => {
          console.error('Error saving job:', error);
          // Handle error scenario
        }
      );
    } else {
      // If form is invalid, show validation errors or feedback
      console.error('Form is not valid!');
    }
  }




  // Example of calculating next job date for yearly frequency
  calculateNextJobDate(startDate: Date): Date {
    const nextYearDate = new Date(startDate);
    nextYearDate.setFullYear(startDate.getFullYear() + 1);
    return nextYearDate;
  }
  generateTimeSlots() {
    for (let hour = 0; hour < 24; hour++) {
      for (let minute = 0; minute < 60; minute += 30) {
        // Convert minute to string, with leading '0' if less than 10
        const minuteStr = minute < 10 ? '0' + minute : minute;

        // Determine if the hour is AM or PM and convert to 12-hour format
        const period = hour < 12 ? 'AM' : 'PM';
        let hour12 = hour % 12;
        hour12 = hour12 === 0 ? 12 : hour12;  // Convert 0 hour to 12 (midnight or noon)

        // Push the formatted time string (e.g., "02:30 PM") into the timeOptions array
        this.timeOptions.push(`${hour12}:${minuteStr} ${period}`);
      }
    }
  }
  

  onFrequencyChange(value: string) {

    this.resetOptions();
    switch (value) {
      case 'Daily':
        this.showDailyOptions = true;
        break;
      case 'Weekly':
        this.showWeeklyOptions = true;
        break;
      case 'Monthly':
        this.showMonthlyOptions = true;
        break;
      case 'Yearly':
        this.showYearlyOptions = true;
        break;
      default:
        break;
    }
  }

  resetOptions() {
    this.showDailyOptions = false;
    this.showWeeklyOptions = false;
    this.showMonthlyOptions = false;
    this.showYearlyOptions = false;
  }

  setFormValues(data: any) {
    this.jobForm.patchValue({
      jobName: data.jobName,
      jobDescription: data.jobDescription,
      status: data.status,
      scheduleJobForm: {
        frequency: data.frequency,
        time: data.time,
        weeklyOccurrence: data.weeklyOccurrence || [], // Set days for weekly occurrence
        monthlyOccurrence: data.monthlyOccurrence || '',
        yearlyOccurrence: data.yearlyOccurrence || ''
      }
    }
    );
  }

  resetForm() {
    // Resetting the entire form, including nested form group
    this.jobForm.reset();
    this.resetOptions();
    this.clearWeeklyOccurrence();
  }



  // Helper function to set weeklyOccurrence FormArray values
  setWeeklyOccurrence(selectedDays: string[]) {
    const weeklyOccurrence = this.jobForm.get('weeklyOccurrence') as FormArray;
    weeklyOccurrence.clear();  // Clear any previous selection
    this.daysOfWeek.forEach(day => {
      // Add a FormControl for each day with a checked status based on the selectedDays array
      weeklyOccurrence.push(new FormControl(selectedDays.includes(day)));
    });
  }

  // Helper function to clear weeklyOccurrence FormArray when resetting the form
  clearWeeklyOccurrence() {
    const weeklyOccurrence = this.jobForm.get('weeklyOccurrence') as FormArray;
    if (weeklyOccurrence) {
      weeklyOccurrence.clear();  // Clear all selected days in FormArray
      // Re-initialize with unchecked (false) FormControl if necessary
      this.daysOfWeek.forEach(() => {
        weeklyOccurrence.push(new FormControl(false));
      });
    }
  }
  getWeek(day: string): FormControl {
    const weeklyOccurrence = this.jobForm.get('weeklyOccurrence') as FormArray;
    const index = this.daysOfWeek.indexOf(day);
    return weeklyOccurrence.at(index) as FormControl;
  }

  setViewMode(jobData: any): void {
    this.isViewMode = true;
    this.isEditMode = false;

    if (jobData) {


      const cronSchedule = this.convertCronToSchedule(jobData.jobConfiguration?.configurationvalue);
      this.jobForm.patchValue({
        jobName: jobData.jobName || '',
        jobDescription: jobData.jobDescription || '',
        alertEmail: jobData.alertEmail || '',
        executionResult: jobData.executionResult || '', // Only if relevant in edit mode
        error: jobData.errorMessage || '', // Only if relevant in edit mode
        status: jobData.statusId || false, // Make sure status is set
        frequency: cronSchedule.frequency || 'noOption',
        monthlyStartDate: cronSchedule.startDate,
        hour:cronSchedule.hour||'00',
        minute:cronSchedule.minute||'00'


      });

      console.log(cronSchedule.frequency, '------------')
      

      if (cronSchedule.frequency === 'Yearly') {
        this.jobForm.patchValue({
          yearlyStartDate: cronSchedule.startDate
        });
      } else if (cronSchedule.frequency === 'Monthly') {
        this.jobForm.patchValue({
          monthlyStartDate: cronSchedule.startDate
        });
      } else {
        // Clear the fields if not relevant
        this.jobForm.patchValue({
          monthlyStartDate: '',
          yearlyStartDate: ''
        });
      }
      if (cronSchedule.frequency === 'Weekly' && Array.isArray(cronSchedule.daysOfWeek)) {
        const dayMap: { [key: string]: string } = {
          Sun: 'sun',
          Mon: 'mon',
          Tue: 'tue',
          Wed: 'wed',
          Thu: 'thu',
          Fri: 'fri',
          Sat: 'sat',
        };

        // Map cron days to lowercase day names, with validation
        this.checkedDays = cronSchedule.daysOfWeek
          .map((day: string) => dayMap[day] || null)
          .filter((day: string | null) => day !== null); // Remove invalid days

        console.log(this.checkedDays, "Checked Days for View Mode");
      }
      console.log(cronSchedule.frequency);

    }

    this.jobForm.disable();



  }

  setEditMode(jobData?: any): void {
    this.isViewMode = false;
    this.isEditMode = true;

    this.checkedDays = [];
    this.currentJobId = jobData.jobId;
    this.currentConfigurationKey = jobData.jobConfiguration.configurationkey;

    if (jobData) {
      this.selectedJob.push(jobData);

      const cronSchedule = this.convertCronToSchedule(jobData.jobConfiguration?.configurationvalue);

      if (cronSchedule.frequency === 'Weekly' && Array.isArray(cronSchedule.daysOfWeek)) {
        const dayMap: { [key: string]: string } = {
          Sun: 'sun',
          Mon: 'mon',
          Tue: 'tue',
          Wed: 'wed',
          Thu: 'thu',
          Fri: 'fri',
          Sat: 'sat',
        };

        // Map cron days to lowercase day names, with validation
        this.checkedDays = cronSchedule.daysOfWeek
          .map((day: string) => dayMap[day] || null)
          .filter((day: string | null) => day !== null); // Remove invalid days

        console.log(this.checkedDays, "Checked Days for View Mode");
      }
      //condition for monthly
      if (cronSchedule.frequency === 'Monthly') {
        this.jobForm.patchValue({
          monthlyStartDate: cronSchedule.startDate || '',
        });
      } else {
        // Clear the field if not "Monthly"
        this.jobForm.patchValue({
          monthlyStartDate: '',
        });
      }
      // Handling Yearly Frequency
      if (cronSchedule.frequency === 'Yearly') {
        this.jobForm.patchValue({
          yearlyStartDate: cronSchedule.startDate || '',
        });
      } else {
        // Clear the field if not "Yearly"
        this.jobForm.patchValue({
          yearlyStartDate: '',
        });
      }

      this.jobForm.patchValue({
        jobName: jobData.jobName || '',
        jobDescription: jobData.jobDescription || '',
        status: jobData.statusId || '2',
        alertEmail: jobData.alertEmail || '',
        executionResult: jobData.executionResult || '', // Only if relevant in edit mode
        error: jobData.errorMessage || '', // Only if relevant in edit mode
        frequency: cronSchedule.frequency || 'noOption',
        hour:cronSchedule.hour||'00',
        minute:cronSchedule.minute||'00'

      });

      console.log(jobData.statusId, '========');


      this.jobForm.enable();
    } else {
      this.jobForm.reset();
    }
  }


  onAddJob(): void {
    this.isViewMode = false;
    this.isEditMode = false;
    this.jobForm.enable();


    this.resetForm();
  }

  getDayFromDate(dateString: string): string {
    const date = new Date(dateString);
    return date.getDate() +this.getOrdinalSuffix(date.getDate());
}

// Extract the day and month from a date for yearly display
getDayAndMonthFromDate(dateString: string): string {
    const date = new Date(dateString);
    const day = date.getDate();
    const month = date.toLocaleString('default', { month: 'long' });
    return `${day}${this.getOrdinalSuffix(day)} ${month}`;
}

// Helper to get the ordinal suffix (st, nd, rd, th) for a day
getOrdinalSuffix(day: number): string {
    if (day > 3 && day < 21) return 'th'; // for 11-20
    switch (day % 10) {
        case 1: return 'st';
        case 2: return 'nd';
        case 3: return 'rd';
        default: return 'th';
    }
}


  getFormattedDate(controlName: string): string | null {
    const date = this.jobForm.get(controlName)?.value;
    if (date) {
      // Format the date as MM/DD/YYYY
      return this.formatDate(new Date(date));  // Use the formatDate function to ensure MM/DD/YYYY format
    }
    return null;
  }



  // Method to handle date change event
  onDateChange(event: any, frequencyType: 'Yearly' | 'Monthly'): void {
    const selectedDate = event;
    console.log("Selected Date from DatePicker:", selectedDate);
    if (selectedDate) {
      const formattedDate = this.formatDate(new Date(selectedDate));  // Ensure the date is in the right format

      if (frequencyType === 'Yearly') {
        this.jobForm.patchValue({
          yearlyStartDate: formattedDate  // Update form with formatted date for yearly occurrence
        });

      } else if (frequencyType === 'Monthly') {
        this.jobForm.patchValue({
          monthlyStartDate: formattedDate  // Update form with formatted date for monthly occurrence
        });
      }
    }
  }

  // Optional: Method to auto-format the date while typing
  autoFormatDate(event: any): void {
    let inputValue = event.target.value;
    if (inputValue.length === 2 || inputValue.length === 5) {
      event.target.value = inputValue + '/';
    }
  }

  // Format date as MM/DD/YYYY
  formatDate(date: Date): string {
    const month = date.getMonth() + 1; // Months are zero-based, so we add 1
    const day = date.getDate();
    const year = date.getFullYear();
    return `${month < 10 ? '0' + month : month}/${day < 10 ? '0' + day : day}/${year}`;
  }

  convertCronToSchedule(cronExpression: string): any {
    if (!cronExpression || typeof cronExpression !== 'string') {
      console.error('Invalid cron expression:', cronExpression);
      return {
        frequency: 'noOption',
        hour: null,
        minute: null,
        startDate: null
      };
    }
  
    const cronParts = cronExpression.split(' ');
    if (cronParts.length !== 6) {
      console.error('Invalid cron format, expected 6 parts:', cronExpression);
      return {
        frequency: 'noOption',
        hour: null,
        minute: null,
        startDate: null
      };
    }
  
    const [seconds, minutes, hours, dayOfMonth, month, dayOfWeek] = cronParts;
  
    // Parse hour and minute as strings and pad them to 2 digits
    const hour = hours.padStart(2, '0');
    const minute = minutes.padStart(2, '0');
  
    const currentYear = new Date().getFullYear();
    const currentMonth = (new Date().getMonth() + 1).toString().padStart(2, '0');
  
    // Daily schedule
    if (dayOfMonth === '*' && month === '*' && dayOfWeek === '*') {
      return { frequency: 'Daily', hour, minute, startDate: null };
    }
    // Weekly schedule
    else if (dayOfMonth === '*' && month === '*' && dayOfWeek !== '*') {
      const daysOfWeekIndices = dayOfWeek.split(',');
  
      // Map cron day numbers to day names
      const dayMap: { [key: number]: string } = {
        0: 'Sunday', 1: 'Monday', 2: 'Tuesday', 3: 'Wednesday',
        4: 'Thursday', 5: 'Friday', 6: 'Saturday'
      };
      const daysOfWeekNames = daysOfWeekIndices.map(index => this.getDayOfWeekName(index));
  
      return {
        frequency: 'Weekly',
        hour,
        minute,
        daysOfWeek: daysOfWeekNames,
        startDate: null
      };
    }
    // Monthly schedule
    else if (dayOfMonth !== '*' && month === '*' && dayOfWeek === '*') {
      const startDate = `${currentMonth}/${dayOfMonth.padStart(2, '0')}/${currentYear}`;
      return { frequency: 'Monthly', hour, minute, dayOfMonth, startDate };
    }
    // Yearly schedule
    else if (dayOfMonth !== '*' && month !== '*' && dayOfWeek === '*') {
      const startDate = `${month.padStart(2, '0')}/${dayOfMonth.padStart(2, '0')}/${currentYear}`;
      return { frequency: 'Yearly', hour, minute, startDate };
    }
  
    return { frequency: 'noOption', hour: null, minute: null, startDate: null };
  }

  generateCronExpression(formValue: any): string {
    const frequency = formValue.frequency;
    let cronExpression = '';
  
    // Extract hour and minute directly from form value without AM/PM conversion
    const hour = parseInt(formValue.hour, 10); // Assume hour is already in 24-hour format
    const minute = formValue.minute.toString().padStart(2, '0'); // Convert minute to string before using padStart()
  
    // Format hours to ensure two digits
    const formattedHour = hour.toString().padStart(2, '0');
    const formattedMinute = minute.toString().padStart(2, '0');
  
    console.log(formattedHour);
    console.log(formattedMinute);
  
    switch (frequency) {
      case 'Daily':
        cronExpression = `0 ${formattedMinute} ${formattedHour} * * *`;
        break;
  
      case 'Weekly':
        if (this.checkedDays && this.checkedDays.length > 0) {
          // Correct day mapping from names to cron day numbers
          const dayMap: { [key: string]: number } = {
            sun: 0, mon: 1, tue: 2, wed: 3, thu: 4, fri: 5, sat: 6
          };
  
          // Map the checked days to cron numbers and filter out undefined
          const days = this.checkedDays
            .map((day: string) => dayMap[day.toLowerCase()])
            .filter((day) => day !== undefined) // Ensure no invalid values
            .join(',');
  
          console.log(days, 'Weekly Days');
          cronExpression = `0 ${formattedMinute} ${formattedHour} * * ${days}`;
        } else {
          cronExpression = `0 ${formattedMinute} ${formattedHour} * * 0`; // Default to Sunday if no days are selected
        }
        break;
  
      case 'Monthly':
        let monthlyStartDate = new Date(formValue.monthlyStartDate);
        if (!isNaN(monthlyStartDate.getTime())) {
          const dayOfMonth = monthlyStartDate.getDate();
          cronExpression = `0 ${formattedMinute} ${formattedHour} ${dayOfMonth} * *`;
        } else {
          cronExpression = `0 ${formattedMinute} ${formattedHour} 1 * *`; // Default to 1st day of the month
        }
        break;
  
      case 'Yearly':
        let yearlyStartDate = new Date(formValue.yearlyStartDate);
        if (!isNaN(yearlyStartDate.getTime())) {
          const dayOfMonth = yearlyStartDate.getDate();
          const month = yearlyStartDate.getMonth() + 1; // getMonth() is 0-based, so we add 1
          cronExpression = `0 ${formattedMinute} ${formattedHour} ${dayOfMonth} ${month} *`;
        } else {
          cronExpression = `0 ${formattedMinute} ${formattedHour} 1 1 *`; // Default to January 1st
        }
        break;
  
      default:
        cronExpression = '0 * * * * *'; // Default cron pattern
    }
  
    return cronExpression;
  }


  // Helper function to convert dayOfWeek number to name
  getDayOfWeekName(dayIndex: string): string {
    const cronToDayMap: { [key: number]: string } = {
      0: 'Sun',
      1: 'Mon',
      2: 'Tue',
      3: 'Wed',
      4: 'Thu',
      5: 'Fri',
      6: 'Sat'
    };

    const day = parseInt(dayIndex, 10);  // Convert string to number

    // Return day name if the parsed day is valid
    return cronToDayMap[day] || 'Invalid day';
  }

  toggleStatus(event: any, job: any): void {
    const isChecked = event.target.checked;

    job.statusId = isChecked ? 1 : 2;

    // Optionally, update the job on the server
    this.batchjobservice.updateJob(job).subscribe(() => {
      // Refetch job list after update (if needed)
      this.fetchJobs();
    });

    console.log(`Job ID: ${job.jobId}, New Status: ${job.statusId}`);
  }
  toggleStatusForm(event: any): void {
    const isChecked = event.target.checked;

    // Update the jobForm status value based on the toggle's checked state
    this.jobForm.patchValue({
      status: isChecked ? 1 : 2
    });

    // Log the updated form value
    console.log(`New Status in Form: ${this.jobForm.get('status')?.value}`);
  }


  onDaySelectionChange(event: any, day: string): void {
    // Lowercase the day to ensure consistency
    const dayLowerCase = day.toLowerCase();

    // Clear the checkedDays array and only add the newly selected day
    this.checkedDays = [dayLowerCase]; // Only keep the selected day

    console.log(this.checkedDays, 'Selected day');
  }

}
