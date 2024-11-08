import { DatePipe } from '@angular/common';
import { ChangeDetectorRef, Component, Input } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute } from '@angular/router';
import { BsDatepickerConfig } from 'ngx-bootstrap/datepicker';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
import { UserMappingService } from 'src/app/ngrx/store/services/user-mapping.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.scss'],
})
export class TaskListComponent {
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public addTaskForm = this._formBuilder.group({
    noteType: ['',Validators.required],
    noteTemplate: [''],
    noteGroup: [''],
    confidential: [false],
    noteText: ['',Validators.required],
    traceType: [''],
    traceDate: [''],
    callLog: [false],
  })
  public editTaskForm = this._formBuilder.group({
    editNoteType: ['',Validators.required],
    editNoteTemplate: [''],
    editNoteGroup: [''],
    editConfidential: [false],
    editNoteText: ['',Validators.required],
    editTraceType: [''],
    editTraceDate: [''],
    editCallLog: [false],
  })
  public noteType: any = [];
  public noteTempData: any = [];
  public notegroupId: any;
  public userId!: number;
  public traceData: any = [];
  public bsConfig: Partial<BsDatepickerConfig>;
  public notesAndTasksData: any = [];
  public claimId: any = '';
  public claimisvoid: boolean = false;
  public selectedNoteChips: string[] = []; // Store selected checkboxes as chips
  public selectedView: any;
  public notesFilterData: any[] = [];
  public orgNotesAndTasksData: any = [];
  public userNamesData: any = [];
  public selectedFilters: any[] = [];
  public selectedUsers: any = '';
  public tempEditStoreData: any;
  public oldUser: any = '';
  public confidentialCode : any;
  public selectednoteTemplate : any = '';
  selectedRowdata: any;
  public userDetails: any = [];

  constructor(private bodyClassService: BodyClassService,
    private _formBuilder: FormBuilder,
    private notesTasksService: NotesTasksService,
    private toasterservice: ToasterService,
    private route: ActivatedRoute,
    private datepipe: DatePipe,
    private userMappingService: UserMappingService) {
    this.route.params.subscribe(params => {
      // Access the claimNumber parameter
      this.claimId = params['id'];
      // Load data based on the new claimNumber
      // this.claimisvoid = params['claimisvoid'];
      const value = params['claimisvoid']
      this.claimisvoid = value === "true" ? true : false;
    });
    this.bsConfig = {
      showWeekNumbers: false,
      dateInputFormat: 'MM/DD/YYYY',
      containerClass: 'theme-dark-blue',
      minDate: new Date(), // Set minimum date to today
      maxDate: undefined, // Allow future dates without limit
    };
  }

  ngOnInit() {
    // console.log(this.datepipe.transform('2024/04/23', 'yyyy-MM-dd hh:mm:ssZZZZZ'))
    this.userId = Number(localStorage.getItem('userId'));
    this.findByUserId(this.userId)
    this.fetchNoteType();
    this.addTaskForm.controls['noteGroup'].disable();
    this.editTaskForm.controls['editNoteGroup'].disable();

    this.fetchTraceType();
    this.fetchClaimNotes();
    this.noteGroup();
    this.userNamesForClaimNotes();
  }

  public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
  }

  public openAddTaskModal() {
    if (!this.claimisvoid) {
      const modelDiv = document.getElementById('addtasklist');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
    }
    this.addTaskForm.controls['noteText'].enable();
  }

  public closeModal(id: string) {
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  public fetchNoteType() {
    this.notesTasksService.fetchNoteType().subscribe((data: any) => {
      let filteredValues = data.body.filter((ele:any)=>ele.isManual == true)
      this.noteType = filteredValues
    
    })
  }

  public  setNoteGroupData() {
    let object = this.noteType.filter((note: any) => note.noteTypeId == this.addTaskForm.value.noteType)
    this.addTaskForm.patchValue({
      noteGroup: object[0].noteGroupDTO.noteGroupDescription
    })
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

  public setConfidential(event:any){
   if(this.confidentialCode == 'C'){
     event.preventDefault();
   }
 }

 

  public editNoteGroupData() {
    let object = this.noteType.filter((note: any) => note.noteTypeId == this.editTaskForm.value.editNoteType)
    this.editTaskForm.patchValue({
      editNoteGroup: object[0].noteGroupDTO.noteGroupCode
      
    })
    this.notegroupId = object[0].noteGroupDTO.noteGroupId
    this.fetchNoteTemplateByNoteTypeAndNoteGroup(object[0].noteTypeId, this.notegroupId)
    this.editTaskForm.patchValue({
      editNoteText: ''
    })
    this.editTaskForm.controls['editNoteText'].enable();
  }

  public fetchNoteTemplateByNoteTypeAndNoteGroup(noteTypeId: string, noteGroupId: string) {
    this.notesTasksService.fetchNoteTemplateByNoteTypeAndNoteGroup(noteTypeId, noteGroupId).subscribe((data: any) => {
      this.noteTempData = data.body;
      const isDefault = data.body.filter((ele:any)=>ele.isDefault == true)
    
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

  public setNoteText() {
    let object = this.noteTempData.filter((note: any) => note.noteTemplateId == this.addTaskForm.value.noteTemplate)
    this.addTaskForm.patchValue({
      noteText: object[0].noteTemplateText
    })
    if (!object[0].editable) {
      this.addTaskForm.controls['noteText'].disable();
    }
  }

  public setEditNoteText() {
    let object = this.noteTempData.filter((note: any) => note.noteTemplateId == this.editTaskForm.value.editNoteTemplate)
    this.editTaskForm.patchValue({
      editNoteText: object[0].noteTemplateText
    })
    if (!object[0].editable) {
      this.editTaskForm.controls['editNoteText'].disable();
    }
  }

  public saveNoteTask() {
    let formattedDate = this.datepipe.transform(this.addTaskForm.value.traceDate, 'yyyy-MM-dd');

const savePayLoad: any = {
  "noteTypeId": this.addTaskForm.value.noteType,
  "noteGroupId": this.notegroupId,
  "claimId": Number(this.claimId),
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


    if (this.addTaskForm.value.noteText == undefined) {
      let object = this.noteTempData.filter((note: any) => note.noteTemplateId == this.addTaskForm.value.noteTemplate)
      savePayLoad.claimNoteText = object[0].noteTemplateText
    }
    this.notesTasksService.saveNoteTaskTemplate(savePayLoad).subscribe(data => {
      this.closeModal('addtasklist')
      this.toasterservice.showToast('success', 'Note Task Added successfully.');
          
      this.addTaskForm.patchValue({
        noteType: '',
        noteTemplate: '',
        noteGroup: '',
        confidential: false,
        noteText: '',
        traceType: '',
        traceDate: '',
        callLog: false,
      })
      this.fetchClaimNotes();
    },
      error=>{
        this.toasterservice.showToast('error', 'Note Task Failed.');
      
      }
    )
  }

  public savePendNoteAndTask() {
    const pendPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')),
      "modifiedByUserId": Number(localStorage.getItem('userId')),
      "noteTypeId": Number(this.addTaskForm.value.noteType),
      "noteGroupId": this.notegroupId,
      "claimId": Number(this.claimId),
      "noteTemplateId": Number(this.addTaskForm.value.noteTemplate),
      "lookupCode": this.addTaskForm.value.confidential == true ? 'C' : 'P',
      "claimNoteText": this.addTaskForm.value.noteText
    }
    if (this.addTaskForm.value.noteText == undefined) {
      let object = this.noteTempData.filter((note: any) => note.noteTemplateId == this.addTaskForm.value.noteTemplate)
      pendPayload.claimNoteText = object[0].noteTemplateText
    }
    this.notesTasksService.savePendNoteTaskTemplate(pendPayload).subscribe(data => {
      this.closeModal('addtasklist')
      this.toasterservice.showToast('success', 'pended task has been created');
      
      this.addTaskForm.patchValue({
        noteType: '',
        noteTemplate: '',
        noteGroup: '',
        confidential: false,
        noteText: '',
        traceType: '',
        traceDate: '',
        callLog: false,
      })
      this.fetchClaimNotes();
    },
      error=>{
        this.toasterservice.showToast('error', 'Note Task Failed.');
      
      }
    )
  }



  public openEditNotesTasks(data: any) {
    if (!this.claimisvoid) {
      this.tempEditStoreData = data
      this.fetchNoteTemplateByNoteTypeAndNoteGroup(data.noteType.noteTypeId,data.noteGroup.noteGroupId)
      const modelDiv = document.getElementById('edittasklist');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      let confidential = false
      if(data.confidential == 'P'){
        this.editTaskForm.controls['editConfidential'].disable();
        confidential = false
      }
      if(data.confidential == 'C'){
        this.editTaskForm.controls['editConfidential'].enable();
        confidential = true
      }
      if(data.confidential == 'B'){
        this.editTaskForm.controls['editConfidential'].enable();
        confidential = false;
      }
      // let date = new Date('2024-04-04T06:39:18.000Z')
      let formattedDate = new Date(data?.traceTask?.traceDate).toLocaleDateString('en-US', {
        month: '2-digit',
        day: '2-digit',
        year: 'numeric'
      });
      formattedDate = data?.traceTask?.traceDate ? formattedDate : ''
      this.editTaskForm.patchValue({
        editNoteType: data.noteType.noteTypeId,
        editNoteTemplate: data.noteTemplate.noteTemplateId,
        editNoteGroup: data.noteGroup.noteGroupDescription,
        editConfidential:confidential,
        editNoteText: data.claimNoteText,
        editTraceType: data?.traceTask?.traceType?.traceTypeId,
        editTraceDate: formattedDate,
        editCallLog: data.callLog,
      })
     
    }
  }

  public editSaveNoteTask() {
    let formattedDt = this.datepipe.transform(this.editTaskForm.value.editTraceDate, 'yyyy-MM-dd')
    let object = this.noteType.filter((note: any) => note.noteTypeId == this.editTaskForm.value.editNoteType)
    if (formattedDt != null && formattedDt != undefined) {
      formattedDt = formattedDt + 'T00:00:00'
    } else {
      formattedDt = '';
    }

    const savePayLoad = {
      "claimNoteId": this.tempEditStoreData.claimNoteId,
      "noteTypeId": Number(this.editTaskForm.value.editNoteType),
      "noteGroupId": this.notegroupId || object[0].noteGroupDTO.noteGroupId,
      "claimId": Number(this.claimId),
      "noteTemplateId": Number(this.editTaskForm.value.editNoteTemplate),
      "lookupCode": this.editTaskForm.value.editConfidential == true ? 'C' : 'P',
      "claimNoteText": this.editTaskForm.value.editNoteText,
      "traceTaskId": this.tempEditStoreData?.traceTask?.traceTaskId ,
      "traceTypeId": Number(this.editTaskForm.value.editTraceType) || '' ,
      "traceReference": "",  //this parameter don't have in Ui so sending as empty value//
      "traceActionLink": "",  //this parameter don't have in Ui so sending as empty value//
      "traceDate":formattedDt ,
      "callLog": this.editTaskForm.value.editCallLog,
      "createdByUserId": Number(localStorage.getItem('userId')),
      "modifiedByUserId": Number(localStorage.getItem('userId')),
    }
    if (this.editTaskForm.value.editNoteText == undefined) {
      let object = this.noteTempData.filter((note: any) => note.noteTemplateId == this.editTaskForm.value.editNoteTemplate)
      savePayLoad.claimNoteText = object[0].noteTemplateText
    }
    this.notesTasksService.saveNoteTaskTemplate(savePayLoad).subscribe(data => {
      this.closeModal('edittasklist')
      this.toasterservice.showToast('success', 'Note Task updated successfully.');
      
      this.editTaskForm.patchValue({
        editNoteType: '',
        editNoteTemplate: '',
        editNoteGroup: '',
        editConfidential: false,
        editNoteText: '',
        editTraceType: '',
        editTraceDate: '',
        editCallLog: false,
      })

      this.fetchClaimNotes();
    },
      error=>{
        this.toasterservice.showToast('error', 'Note Task Failed.');
      }
    )
  }

  public fetchTraceType() {
    this.notesTasksService.fetchTraceType().subscribe((data: any) => {
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


  public fetchClaimNotes() {
    this.notesTasksService.fetchClaimNotes(this.claimId).subscribe((data: any) => {
      // Filter out records where statusCode is "Deleted"
      this.notesAndTasksData = data.body
        .filter((note: any) => note.statusCode !== "Deleted")
        .sort((a: any, b: any) => {
          return new Date(b.createdDate).getTime() - new Date(a.createdDate).getTime();
        });
      this.notesAndTasksData.forEach((ele:any)=>{
        let formattedText: string = ele.claimNoteText.replace(/\n/g, '<br>');
        formattedText = formattedText.replace('<', '&lt;').replace('>', '&gt;');
        ele.claimNoteText = formattedText
        // console.log( ele.claimNoteText.length,'...... ele.claimNoteText')
      })
      // Store original data without filtering if needed
      this.orgNotesAndTasksData = data.body.filter((note: any) => note.statusCode !== "Deleted");
    })
   
  }
 
  
  


  public onInput(event: Event) {
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


  public openViewTask(data: any) {
    if (data.claimNoteText.length > 130) {
      this.selectedView = data
      const modelDiv = document.getElementById('viewtask');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }

    }

  }
  public noteGroup() {
    this.notesTasksService.noteGroup().subscribe((data: any) => {
      this.notesFilterData = data.body
    })
  }

  public userNamesForClaimNotes() {
    this.notesTasksService.userNamesForClaimNotes(this.claimId).subscribe((data: any) => {
      this.userNamesData = data.body
    })
  }


  public  onUserSelected(user: any) {
    this.selectedUsers = user
    this.notesAndTasksData = [];
    this.notesAndTasksData = this.orgNotesAndTasksData.filter((ele: any) => ele.createdUserName.split(' ').join('') == user.split(' ').join(''))

    if (this.oldUser != '') {
      const index = this.selectedNoteChips.indexOf(this.oldUser)
      this.selectedNoteChips.splice(index, 1)
      this.selectedNoteChips.push(user)
      this.oldUser = user
    } else {
      this.selectedNoteChips.push(user)
      this.oldUser = user
    }
 }

 public isSelected(noteGroupId: number): boolean {
    return this.selectedFilters.indexOf(noteGroupId) > -1;
  }

  public  toggleSelection(noteGroupId: number, noteGroupDescription: string) {
    const index = this.selectedFilters.indexOf(noteGroupId);
    if (index > -1) {
      this.selectedFilters.splice(index, 1); // Remove the ID if already selected
      this.selectedNoteChips = this.selectedNoteChips.filter((chip: any) => chip !== noteGroupDescription);
    } else {
      this.selectedFilters.push(noteGroupId); // Add the ID if not selected
      this.selectedNoteChips.push(noteGroupDescription);
    }
    this.filterByType();
  }

  public filterByType() {
    if (this.selectedFilters.length != 0) {
      this.notesAndTasksData = [];
      let arrayData = [];
      if (this.selectedUsers != '' && this.selectedUsers != undefined) {
        for (let item of this.selectedFilters) {
          const data = this.orgNotesAndTasksData.filter(
            (ele: any) => ele.noteGroup.noteGroupId == item && ele.createdUserName == this.selectedUsers.split(' ').join(''))
          if (data.length != 0) {
            arrayData.push(data)
          }
        }
      } else {

        for (let item of this.selectedFilters) {
          const data = this.orgNotesAndTasksData.filter((ele: any) => ele.noteGroup.noteGroupId == item)
          if (data.length != 0) {
            arrayData.push(data)
          }
        }
      }
      this.notesAndTasksData = arrayData.flatMap(array => [...array])
    } else {
      this.notesAndTasksData = [];
      this.notesAndTasksData = this.orgNotesAndTasksData;
    }
  }

  public clearChips() {
    this.selectedFilters = [];
    this.selectedNoteChips = [];
    this.notesAndTasksData = [];
    this.notesAndTasksData = this.orgNotesAndTasksData;
  }

  public removeselectedChips(data: any) {
    const index = this.selectedNoteChips.indexOf(data)
    this.selectedNoteChips.splice(index, 1)
    if (this.userNamesData.includes(data)) {
      // const index = this.selectedNoteChips.indexOf(data)
      // this.selectedNoteChips.splice(index,1)
      this.selectedUsers = '';
      // this.filterByType();

    } else {
      const record = this.notesFilterData.filter((item: any) => item.noteGroupDescription == data)
      const index = this.selectedFilters.indexOf(record[0].noteGroupId)
      this.selectedFilters.splice(index, 1)
    }
    this.filterByType();
  }

  public inputValidator(event: any): void {
    const pattern = /[^A-Za-z0-9_@.,/#&+-\s]/g; // Updated pattern to exclude spaces
  
    const currentValue = event.target.value;
    if (pattern.test(currentValue)) {
      event.target.value = currentValue.replace(pattern, "");
    }
  }

  public openDeleteModal(data:any){
    this.selectedRowdata = data
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }


  public deleteNoteandTasks(){
    this.notesTasksService.deleteNotesAndTask(this.selectedRowdata.claimNoteId).subscribe(data=>{
      this.closeModal('deleteModal')
      this.toasterservice.showToast('success', 'Note and Tasks are Deleted Successfully.');
      this.fetchClaimNotes();
    },
  error=>{
    this.toasterservice.showToast('error', 'Note and Tasks are Failed to Delete.');
  })
  }
 
  public findByUserId(id: number) {
    this.userMappingService.findByUserId(id).subscribe(
      (data) => {
        this.userDetails = data;
      //  console.log(this.userDetails,'........this.userDetails')
      }
    )
  }


}
