import { Component,Input } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/ngrx/store/services/auth.service';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { NotesTasksService } from 'src/app/ngrx/store/services/notes-tasks.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';


interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}


@Component({
  selector: 'app-view-note-template',
  templateUrl: './view-note-template.component.html',
  styleUrls: ['./view-note-template.component.scss']
})
export class ViewNoteTemplateComponent {

  
  @Input() collapsed = false;
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true; 
  public addNoteForm = this._formBuilder.group({
    noteType:['',Validators.required],
    noteGroup:['',Validators.required],
    templateName:['',Validators.required],
    templateText:[''],
    editable:[false],
    default:[false],
    manual:[false],
    auditHistorytemplateText:['']
  })

  public editNoteForm = this._formBuilder.group({
    editnoteType:[''],
    editnoteGroup:[''],
    edittemplateName:['',Validators.required],
    edittemplateText:[''],
    editeditable:[false],
    editdefault:[false],
    editmanual:[false],
    editauditHistorytemplateText:['']
  })
  public allNoteTemplates: any = [];
  public noteType:any = [];
  public  userId!: number;
  public notegroupId:any;
  public editnotegroupId:any;
  public editnoteTemplateId:any;
  public deletenoteTemplateId:any;
  public tabSearchText: any = {};
  public allNoteTemplatesData:any;
  public noteListItemsPerPage: number = 50; // Default items per page
  public noteListCurrentPage: number = 1;
  public perPageOptions = [50, 100, 250, 500];
  
  constructor(private bodyClassService: BodyClassService,
    private _formBuilder: FormBuilder,
    private notesTasksService:NotesTasksService,
    private toasterservice: ToasterService,
    public authService: AuthService) {}
    

  ngOnInit(){
    this.userId = Number(localStorage.getItem('userId'));
     this.fetchAllNoteTemplates();
     this.fetchNoteType();
     this.editNoteForm.controls['editnoteGroup'].disable();
     this.addNoteForm.controls['noteGroup'].disable();
   }

   public onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  public getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  public openAddnoteTemp(){
    const modelDiv = document.getElementById('addnotes');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }

  public closeModal(id:string){
    const modelDiv = document.getElementById(id);
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
    if(id == 'addnotes'){
      this.addNoteForm.patchValue({
        noteType : '',
        noteGroup:'',
        templateName:'',
        templateText:'',
        editable:false,
        default:false,
        manual:false,
        auditHistorytemplateText:''
      })
    }
  }

  public openEditTemplate(items:any){
    const modelDiv = document.getElementById('editnotes');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.editNoteForm.patchValue({
    editnoteType: items.noteTypeId,
    editnoteGroup:items.noteGroupName,
    edittemplateName:items.noteTemplateName,
    edittemplateText:items.noteTemplateText,
    editeditable:items.editable,
    editdefault:items.isDefault,
    editauditHistorytemplateText:items.auditHistoryTemplateText,
    editmanual:items.isManual
    })
    let object = this.noteType.filter((note:any)=> note.noteTypeId == this.editNoteForm.value.editnoteType)
    this.editnotegroupId = object.length != 0 ? object[0].noteGroupDTO.noteGroupId : items.noteGroupId
    this.editnoteTemplateId = items.noteTemplateId
  }

  public openDeleteNoteModal(items:any){
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    this.deletenoteTemplateId = items.noteTemplateId
  }

  public fetchAllNoteTemplates(){
    this.notesTasksService.fetchAllNoteTemplates().subscribe((data:any)=>{
    this.allNoteTemplates = data.body
    this.allNoteTemplatesData = data.body
    })
  }
  
  public fetchNoteType(){
    this.notesTasksService.fetchNoteType().subscribe((data:any)=>{
      this.noteType = data.body
    })
  }
 
  public setNoteGroupData(){
    let object = this.noteType.filter((note:any)=> note.noteTypeId == this.addNoteForm.value.noteType)
    this.addNoteForm.patchValue({
      noteGroup:object[0].noteGroupDTO.noteGroupDescription
    })
    // this.addNoteForm.controls['noteGroup'].disable()
    this.notegroupId = object[0].noteGroupDTO.noteGroupId
  }
  
  public setEditNoteGroupData(){
    let object = this.noteType.filter((note:any)=> note.noteTypeId == this.editNoteForm.value.editnoteType)
    this.editNoteForm.patchValue({
      editnoteGroup:object[0].noteGroupDTO.noteGroupDescription
    })
    this.editnotegroupId = object[0].noteGroupDTO.noteGroupId
    // this.editNoteForm.controls['editnoteGroup'].disable()
  }

  public  saveNoteTemplate(){
    const requestPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')),
      "modifiedByUserId": Number(localStorage.getItem('userId')),
      "noteTemplateId": 0,
      "noteTemplateName":  this.addNoteForm.value.templateName,
      "noteTemplateText":  this.addNoteForm.value.templateText,
      "noteGroupId":  this.notegroupId,
      "noteTypeId":Number(this.addNoteForm.value.noteType) ,
      "editable": this.addNoteForm.value.editable,
      "isDefault": this.addNoteForm.value.default,
      "isManual":this.addNoteForm.value.manual,
	    "auditHistoryTemplateText": this.addNoteForm.value.auditHistorytemplateText || ''
    }
    this.notesTasksService.saveNoteTemplate(requestPayload).subscribe((data:any)=>{

      this.closeModal('addnotes')
      this.toasterservice.showToast('success', 'Note Template Added successfully.');
      
     
      this.fetchAllNoteTemplates(); 
 },error=>{ }
    )
  }

  public editSaveTemplate(){
    const requestPayload = {
      "createdByUserId": Number(localStorage.getItem('userId')),
      "modifiedByUserId": Number(localStorage.getItem('userId')),
      "noteTemplateId": this.editnoteTemplateId,
      "noteTemplateName":  this.editNoteForm.value.edittemplateName,
      "noteTemplateText":  this.editNoteForm.value.edittemplateText,
      "noteGroupId":  this.editnotegroupId,
      "noteTypeId":Number(this.editNoteForm.value.editnoteType) ,
      "editable": this.editNoteForm.value.editeditable,
      "isDefault": this.editNoteForm.value.editdefault,
      "isManual": this.editNoteForm.value.editmanual,
	    "auditHistoryTemplateText": this.editNoteForm.value.editauditHistorytemplateText || ''
    }
    this.notesTasksService.saveNoteTemplate(requestPayload).subscribe((data:any)=>{

      this.closeModal('editnotes')
      this.toasterservice.showToast('success', 'Note Template Updated successfully.');
      this.fetchAllNoteTemplates();

    },error=>{ }
    )
  }

  public deleteNoteTemplate(){
    this.notesTasksService.deleteNoteTemplate( this.deletenoteTemplateId).subscribe((data:any)=>{
      this.toasterservice.showToast('success', 'Note Template Deleted successfully..');
     
      this.closeModal('deleteModal')
      this.fetchAllNoteTemplates(); 

    })
  }


  public onTabSearch() {
    if (!this.tabSearchText.groupName && !this.tabSearchText.typeName && !this.tabSearchText.templateName
      && !this.tabSearchText.edit && !this.tabSearchText.default && !this.tabSearchText.templateText 
 ) {
        this.allNoteTemplates = this.allNoteTemplatesData;
    } else {
      if (
        this.tabSearchText.groupName != undefined &&
        this.tabSearchText.groupName != ''
      ) {
        this.allNoteTemplates = this.allNoteTemplatesData.filter((user: any) => {
          if (user.noteGroupName != null) {
            return user.noteGroupName
              .toLowerCase()
              .includes(this.tabSearchText.groupName.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.typeName != undefined &&
        this.tabSearchText.typeName != ''
      ) {
        this.allNoteTemplates = this.allNoteTemplatesData.filter((user: any) => {
          if (user.noteTypeName != null) {
            return user.noteTypeName
              .toLowerCase()
              .includes(this.tabSearchText.typeName.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.templateName != undefined &&
        this.tabSearchText.templateName != ''
      ) {
        this.allNoteTemplates = this.allNoteTemplatesData.filter((user: any) => {
          if (user.noteTemplateName != null) {
            return user.noteTemplateName
              .toLowerCase()
              .includes(this.tabSearchText.templateName.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.edit != undefined &&
        this.tabSearchText.edit != ''
      ) {
        this.allNoteTemplates = this.allNoteTemplatesData.filter((user: any) => {
          if (user.editable != null) {
            return user.editable
              .toLowerCase()
              .includes(this.tabSearchText.edit.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.default != undefined &&
        this.tabSearchText.default != ''
      ) {
        this.allNoteTemplates = this.allNoteTemplatesData.filter((user: any) => {
          if (user.isDefault != null) {
            return user.isDefault
              .toLowerCase()
              .includes(this.tabSearchText.default.toLowerCase());
          }
        });
      }
      if (
        this.tabSearchText.templateText != undefined &&
        this.tabSearchText.templateText != ''
      ) {
        this.allNoteTemplates = this.allNoteTemplatesData.filter((user: any) => {
          if (user.noteTemplateText != null) {
            return user.noteTemplateText
              .toLowerCase()
              .includes(this.tabSearchText.templateText.toLowerCase());
          }
        });
      }
      
    }
  }

  public  onPreviousPageClick(preClick: boolean) {
    if (preClick) {
      if (this.noteListCurrentPage > 1) {
        this.noteListCurrentPage--;
      }
    } else {
      if (this.noteListCurrentPage > 1) {
        this.noteListCurrentPage--;
      }
    }
  }

  public getPageNumbers(pageNo: boolean): number[] {
    if (pageNo) {
      const pageCount = Math.ceil(
        this.allNoteTemplates.length /  this.noteListItemsPerPage
      );
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    } else {
      const pageCount = Math.ceil(this.allNoteTemplates.length / this.noteListItemsPerPage); 
      return Array.from({ length: pageCount }, (_, index) => index + 1);
    }
  }

   // Function to navigate to a specific page
   public  onPageNumberClick(page: number, pageClick: boolean) {
    if (pageClick) {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.noteListCurrentPage = page;
      }
    } else {
      if (page >= 1 && page <= this.getPageNumbers(pageClick).length) {
        this.noteListCurrentPage = page;
      }
    }
  }

   // Function to navigate to the next page
   public onNextPageClick(nextClick: boolean) {
    if (nextClick) {
      const maxPage = Math.ceil(
        this.allNoteTemplates.length / this.noteListItemsPerPage
      );
      if (this.noteListCurrentPage < maxPage) {
        this.noteListCurrentPage++;
      } else {
        const maxPage = Math.ceil(this.allNoteTemplates.length / this.noteListItemsPerPage);
        if (this.noteListCurrentPage < maxPage) {
          this.noteListCurrentPage++;
        }
      }
    }
  }


  public onItemsPerPageChange() {
   this.noteListCurrentPage = 1; // Reset to the first page when changing items per page
 }

 public inputValidator(event: any): void {
  const pattern = /[^A-Za-z0-9_@.,/#&+-\s]/g; // Updated pattern to exclude spaces

  const currentValue = event.target.value;
  if (pattern.test(currentValue)) {
    event.target.value = currentValue.replace(pattern, "");
  }
}


public disableSaveBtn():boolean{
  if(this.addNoteForm.invalid){
    return true;
  }else{
    if(this.addNoteForm.value.templateText != '' || this.addNoteForm.value.auditHistorytemplateText != ''){
      return false;
    }else{
      return true;
    }
  }
}

public disableUpdateBtn(){
  if(this.editNoteForm.invalid){
    return true;
  }else{
    if(this.editNoteForm.value.edittemplateText != '' || this.editNoteForm.value.editauditHistorytemplateText != ''){
      return false;
    }else{
      return true;
    }
  }
}

}
