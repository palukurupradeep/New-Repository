import { DatePipe } from '@angular/common';
import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Route, Router } from '@angular/router';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

@Component({
  selector: 'app-claim-task-view',
  templateUrl: './claim-task-view.component.html',
  styleUrls: ['./claim-task-view.component.scss']
})
export class ClaimTaskViewComponent {
  public addTaskForm = this._formBuilder.group({
    noteType:['',Validators.required],
    noteTemplate:[''],
    noteGroup:[''],
    confidential:[false],
    noteText:['',Validators.required],
    traceType:[''],
    traceDate:[''],
    callLog:[false],
    
  })
  public noteType:any = [];
  public noteTempData: any = [];
  public notegroupId:any;
  public userId!: number;
  public traceData:any = [];
  @Input() claimDetails!: any;
  @Input() claimIsVoid:any = false;
  public claimIdData:any;
  public notesAndTasksData:any = [];
  public bsConfig: Partial<BsDatepickerConfig>;
  public selectedView:any;
  public confidentialCode : any;
  public selectednoteTemplate : any = '';
  public selectednoteType: any = '';
  // @Output() fetchClaimNotesData = new EventEmitter<string[]>();
  @Input() fetchClaimNoteToDetails = '';

  constructor( private _formBuilder: FormBuilder,
    private notesTasksService:NotesTasksService ,
    private snackBar: MatSnackBar,
    private router:Router,
    private toasterService: ToasterService,
    private datepipe: DatePipe){
      this.bsConfig = {
        showWeekNumbers: false,
        dateInputFormat: 'MM/DD/YYYY',
        containerClass: 'theme-dark-blue',
        minDate: new Date(), // Set minimum date to today
        maxDate: undefined, // Allow future dates without limit
    };
     }

  ngOnInit(){
    this.fetchNoteType();
    this.addTaskForm.controls['noteGroup'].disable();
    this.fetchTraceType();
   
  }
  routeToTaskList(){
    // if(!this.claimIsVoid){
    const url = this.router.createUrlTree(['/task-list',  this.claimIdData,this.claimIsVoid])
    window.open(url.toString(), '_blank') 
    // }  
  }

   // track On Update the parent input value
   ngOnChanges(changes: SimpleChanges) {
    if(this.claimDetails.claimId != undefined && this.fetchClaimNoteToDetails == ''){
      this.claimIdData = this.claimDetails.claimId
      if(this.notesAndTasksData.length == 0){
        this.fetchClaimNotes();
      }
      // this.fetchClaimNotes();
    }
    if(this.fetchClaimNoteToDetails != ''){
      this.fetchClaimNotes();
    }
   
   
  }

  openAddTaskModal(){
    if(!this.claimIsVoid){
    const modelDiv = document.getElementById('addtasklist');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
  this.addTaskForm.controls['noteText'].enable();
  }
  closeModal(id:string){
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  fetchNoteType(){
    this.notesTasksService.fetchNoteType().subscribe((data:any)=>{
      let filteredValues = data.body.filter((ele:any)=>ele.isManual == true)
      this.noteType = filteredValues
      // this.noteType = data.body
      this.selectednoteType = data.body[0].noteTypeId
    })
  }
  setNoteGroupData(){
    let object = this.noteType.filter((note:any)=> note.noteTypeId == this.addTaskForm.value.noteType)
    this.addTaskForm.patchValue({
      noteGroup:object[0].noteGroupDTO.noteGroupDescription
    })
    // this.addNoteForm.controls['noteGroup'].disable()
    this.notegroupId = object[0].noteGroupDTO.noteGroupId
    this.fetchNoteTemplateByNoteTypeAndNoteGroup(object[0].noteTypeId, this.notegroupId)
    this.addTaskForm.patchValue({
      noteText: ''
     })
     this.addTaskForm.controls['noteText'].enable();
     this.confidentialCode = object[0].lookupCode

     if(object[0].lookupCode == 'P'){
      this.addTaskForm.controls['confidential'].disable();
      this.addTaskForm.patchValue({
        confidential:false
      })
     }
     else  if(object[0].lookupCode == 'C'){
      this.addTaskForm.controls['confidential'].enable();
      this.addTaskForm.patchValue({
        confidential:true
      })
     }
     else  if(object[0].lookupCode == 'B'){
      this.addTaskForm.controls['confidential'].enable();
      this.addTaskForm.patchValue({
        confidential:true
      })
     }
  }
 
  setConfidential(event:any){
    if(this.confidentialCode == 'C'){
      event.preventDefault();
    }
  }


  fetchNoteTemplateByNoteTypeAndNoteGroup(noteTypeId: string, noteGroupId: string){
    this.notesTasksService.fetchNoteTemplateByNoteTypeAndNoteGroup(noteTypeId,noteGroupId).subscribe((data:any)=>{
      this.noteTempData = data.body

      const isDefault = data.body.filter((ele:any)=>ele.isDefault == true)
      // console.log(isDefault)
      if(isDefault.length != 0){
        this.selectednoteTemplate = isDefault[0].noteTemplateName
        this.addTaskForm.patchValue({
          noteText : isDefault[0].noteTemplateText,
          noteTemplate : isDefault[0].noteTemplateId
         })
        //  this.addTaskForm.controls['noteTemplate'].enable();
      }
    })

  }

  setNoteText(){
    let object = this.noteTempData.filter((note:any)=>note.noteTemplateId == this.addTaskForm.value.noteTemplate)
       this.addTaskForm.patchValue({
        noteText:object[0].noteTemplateText
       })
       if(!object[0].editable){
        this.addTaskForm.controls['noteText'].disable();
       }
  }

  saveNoteTask(){
    let formattedDate = this.datepipe.transform(this.addTaskForm.value.traceDate, 'yyyy-MM-dd');

const savePayLoad: any = {
  "noteTypeId": this.addTaskForm.value.noteType,
  "noteGroupId": this.notegroupId,
  "claimId": Number(this.claimIdData),
  "noteTemplateId": this.addTaskForm.value.noteTemplate,
  "lookupCode": this.addTaskForm.value.confidential === true ? 'C' : 'P',
  "claimNoteText": this.addTaskForm.value.noteText,
  "traceTaskId": 0, // default value for save
  "traceTypeId": this.addTaskForm.value.traceType || '',
  "traceReference": "", // this parameter doesn't exist in UI, sending as an empty value
  "traceActionLink": "", // this parameter doesn't exist in UI, sending as an empty value
  "callLog": this.addTaskForm.value.callLog,
  "createdByUserId": Number(localStorage.getItem('userId')),
  "modifiedByUserId": Number(localStorage.getItem('userId')),
};

// Conditionally add traceDate if it exists
if (formattedDate) {
  savePayLoad.traceDate = formattedDate + 'T00:00:00';
} 

// Optionally, handle traceTypeId similarly if it's required
if (this.addTaskForm.value.traceType) {
  savePayLoad.traceTypeId = this.addTaskForm.value.traceType;
}


    if(this.addTaskForm.value.noteText == undefined){
      let object = this.noteTempData.filter((note:any)=>note.noteTemplateId == this.addTaskForm.value.noteTemplate)
      savePayLoad.claimNoteText = object[0].noteTemplateText
    }
    this.notesTasksService.saveNoteTaskTemplate(savePayLoad).subscribe(data=>{
      this.closeModal('addtasklist')
      this.toasterService.showToast('success', 'Note Task Added successfully.');
      this.addTaskForm.patchValue({
        noteType: '',
        noteTemplate:'',
        noteGroup:'',
        confidential:false,
        noteText:'',
        traceType:'',
        traceDate:'',
        callLog:false,
      })
      this.fetchClaimNotes();
    },
    // error=>{
    //   this.snackBar.open('"Note Task Failed."', 'Close', {
    //     duration: 3000,
    //     verticalPosition: 'top',
    //     panelClass: ['custom-snackbar'], 
    //   });
    // }
    )
  }

  savePendNoteAndTask(){
    const pendPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')) ,
      "modifiedByUserId":  Number(localStorage.getItem('userId')),
      "noteTypeId": this.addTaskForm.value.noteType,
      "noteGroupId": this.notegroupId,
      "claimId": Number(this.claimIdData),
      "noteTemplateId": this.addTaskForm.value.noteTemplate,
      "lookupCode":  this.addTaskForm.value.confidential == true ? 'C':'P',
      "claimNoteText":  this.addTaskForm.value.noteText
  }
  this.notesTasksService.savePendNoteTaskTemplate(pendPayload).subscribe(data=>{
    this.closeModal('addtasklist')
      this.toasterService.showToast('success', 'pended task has been created');
      
      this.addTaskForm.patchValue({
        noteType: '',
        noteTemplate:'',
        noteGroup:'',
        confidential:false,
        noteText:'',
        traceType:'',
        traceDate:'',
        callLog:false,
      })
      this.fetchClaimNotes();
  },
   // error=>{
    //   this.snackBar.open('"Note Task Failed."', 'Close', {
    //     duration: 3000,
    //     verticalPosition: 'top',
    //     panelClass: ['custom-snackbar'], 
    //   });
    // }
  )
  }

  fetchTraceType(){
    this.notesTasksService.fetchTraceType().subscribe((data:any)=>{
      this.traceData = data.body
    })
  }

  public setCalenderDate(){
    let trace = this.traceData.filter((data:any)=> data.traceTypeId == this.addTaskForm.value.traceType)
    if(trace[0].traceTypeDays != 0){
      let currentDate =  new Date()
      currentDate.setDate(currentDate.getDate() + trace[0].traceTypeDays)
      let formattedDate = this.datepipe.transform(currentDate,'MM/dd/yyyy')
      this.addTaskForm.patchValue({
        traceDate : formattedDate
      })
    }else{
      let formattedDate = this.datepipe.transform(new Date(),'MM/dd/yyyy')
      this.addTaskForm.patchValue({
        traceDate : formattedDate
      })
    }
  }


 
  fetchClaimNotes(){
    this.notesTasksService.fetchClaimNotes(this.claimIdData).subscribe((data:any)=>{
      let claimData = data.body

    if(claimData.length > 5 ){
      let records = claimData.slice(-5)
      this.notesAndTasksData = records.reverse()

    }else{
      this.notesAndTasksData = claimData.reverse()
    }
    this.notesAndTasksData.forEach((ele:any)=>{
      let formattedText: string = ele.claimNoteText.replace(/\n/g, '<br>');
      formattedText = formattedText.replace('<', '&lt;').replace('>', '&gt;');
      ele.claimNoteText = formattedText
    })
   
    })
  }

  

  onInput(event: Event) {
    const inputElement = event.target as HTMLInputElement;
    const inputDate = new Date(inputElement.value);

    // Check if the input date is in the past
    if (inputDate < new Date()) {
        // Clear the input value for past dates
        inputElement.value = '';
        
        
    } else {
        // Reset the error message and flag
        // this.pastDateError = false;
    }
}

  openViewTask(data:any){
    if(data.claimNoteText.length>30){
      this.selectedView = data
      const modelDiv = document.getElementById('viewtask');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
   }
   
   }
   
   public inputValidator(event: any): void {
    const pattern = /[^A-Za-z0-9_@.,/#&+-\s]/g; // Updated pattern to exclude spaces
  
    const currentValue = event.target.value;
    if (pattern.test(currentValue)) {
      event.target.value = currentValue.replace(pattern, "");
    }
  }
  
  // inputValidator(event: any) {
  //   // Adjust the regex pattern to allow new lines and special characters like parentheses
  //   const allowedPattern = /^[ A-Za-z0-9_@.,/#&()+-\n\r]*$/; // Added \n and \r for newlines
  //   const inputChar = String.fromCharCode(event.charCode);
  
  //   if (!allowedPattern.test(inputChar)) {
  //     event.preventDefault();
  //   }
  // }
  
}
