import { Component,Input } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Sort } from '@angular/material/sort';
import { Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';

interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-photo-maintenance',
  templateUrl: './photo-maintenance.component.html',
  styleUrls: ['./photo-maintenance.component.scss']
})
export class PhotoMaintenanceComponent {

  @Input() collapsed = false;
  @Input() screenWidth = 0;
  isSideNavCollapsed: boolean = true; 
  claimPhotos: any[]=[];
  public sortclaimPhotos: any[]=[];
  public claimPhotosData: any[]=[];
  userId!:number
  photoDesc: string = '';
  
  selectedFile: File | null = null;
  selectedFileUrl: any;//string | ArrayBuffer | null = null;
  selectedFileData:any
  showOverlay: boolean = false;
  previewedPhotoUrl: string = '';
  previewedPhotoName: string = '';
  editMode: boolean = false;
  photoData: any;
  selectedFileName: any;
  public tabSearchText: any = {};
  public columnSortState: { [key: string]: 'asc' | 'desc' } = {};
  photoId!: number;

  constructor(private bodyClassService: BodyClassService,
    private smartCodeService: SmartCodeService,
    private toasterService: ToasterService,
    private router:Router) {}

    
 ngOnInit(): void {
  this.userId = Number(localStorage.getItem('userId'));
  this.getClaimPhotos()
  //this.getClaimCodePhotos(this.userId)
  
 }

  onToggleSideNav(data: SideNavToggle): void {
    this.screenWidth = data.screenWidth;
    this.isSideNavCollapsed = data.collapsed;
  }

  getBodyClass(): string {
    return this.bodyClassService.getBodyClass(this.isSideNavCollapsed,this.screenWidth);
  }

  // getClaimCodePhotos(userId:number){
  //   this.smartCodeService.getClaimCodePhotos(userId).subscribe(data =>{
  //     if(data.status === 200){
  //       this.claimCodePhotos =data.body
  //       console.log( this.claimCodePhotos,' this.claimPhots')
  //     }
  //   })
  // }
  deleteClaimPhotos(photo: any) {
    this.photoData = photo;
console.log(this.photoData,'photo')
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
    
    console.log('Deleting photo:', photo);
   
  }
    
  cancelDeletePhotoModel(){
    const modelDiv = document.getElementById('deleteModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }

  confirmDeletePhotoModel(){
    console.log('role delte confirmation');
     this.smartCodeService.deleteClaimPhotos(this.photoData.photoId).subscribe((data) => {
    
     
     
      if(data.status == 200){
       
        this.toasterService.showToast('success', 'Claim Photo Deleted Successfully');
     
        this.getClaimPhotos();
      }
     
        },
          (error) => {
      if(error.status == 400){
                this.toasterService.showToast('error', error.error.detail);
        this.getClaimPhotos();
      }
    });
this.cancelDeletePhotoModel();
  }
  // editPhoto(photo: any) {
  //   console.log(photo,'photoedit')
  //   // Populate the modal with the existing values for editing
  //   this.photoDesc = photo.photoDescription;
  //   this.selectedFileName = photo.photoName
  //   this.selectedFileData = photo.photo;  // Assuming 'description' is the property in your photo object
  //  this.photoId = photo.photoId;
  //     this.editMode = true; // Set edit mode to true
  //   this.openClaimPhotosModal();
  // }
  editPhoto(photo: any) {
    console.log(photo, 'photoedit');
    
    // Populate the modal with the existing values for editing
    this.photoDesc = photo.photoDescription;
    this.selectedFileName = photo.photoName;
    this.selectedFileData = photo.photo;
    this.photoId = photo.photoId;
    this.editMode = true; // Set edit mode to true
  
    // Set preview URL for the existing photo
    this.selectedFileUrl = `data:image/jpeg;base64,${photo.photo}`;
  
    this.openClaimPhotosModal();
  }
  

  getClaimPhotos(){
    this.smartCodeService.getClaimPhotos().subscribe(data =>{
      if(data.status === 200){
        this.claimPhotos =data.body
        this.claimPhotosData = data.body
        this.sortclaimPhotos = data.body.slice()
        // console.log( this.claimPhotos,' this.claimPhots')
      }
    })
  }
 
  public openClaimPhotosModal() {
    
    const modelDiv = document.getElementById('claimPhotosModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'block';
    }
  }
  public closeClaimPhotosModal() {
    this.editMode = false;
    this.selectedFileName = '';
    this.selectedFileName = ''; 
    this.photoDesc = ''  
    this.selectedFileData = ''
    const modelDiv = document.getElementById('claimPhotosModal');
    if (modelDiv != null) {
      modelDiv.style.display = 'none';
    }
  }
 
  onFileSelected(event: any) {
    const selectedFile = event.target.files[0];
    if (selectedFile) {
      this.selectedFileData = selectedFile;
      // console.log(this.selectedFileData.name,'filename')
      this.selectedFileName = this.selectedFileData.name;
      // Display preview
      const reader = new FileReader();
      reader.onload = (e) => {
        this.selectedFileUrl = e.target?.result;
      };
      reader.readAsDataURL(selectedFile);
    }
  }
  async savecode(){
    const arrayBuffer = await this.selectedFileData.arrayBuffer();
  const uint8Array = new Uint8Array(arrayBuffer);

  // Convert Uint8Array to a regular array
  const byteArray = Array.from(uint8Array);

  //const base64String = btoa(String.fromCharCode.apply(null, byteArray));

  const payload = {
    createdByUserId: this.userId,
    modifiedByUserId: this.userId,
    photoName: this.selectedFileData.name,
    photoDescription: this.photoDesc,
    photoId:this.photoId,
    photo: byteArray, // send byte array as base64 string
    statusId: 0
  };
  
      console.log(payload,'photopayload')
      //saveorUpdateClaimPhotos
  this.smartCodeService.saveorUpdateClaimPhotos(payload).subscribe(data=>{
    if(data.status === 200) {
      this.toasterService.showToast('success', 'Claim Photo Saved Successfully');
   
    console.log(data ,' data');
    this.getClaimPhotos();
    this.closeClaimPhotosModal();
    }
  })
    }
    // previewPhoto(photo: any) {
     
    //   this.previewedPhotoUrl = 'data:image/jpeg;base64,' + photo.photo; // Add the data: URL scheme
    //   this.previewedPhotoName = photo.photoName;
      
    
    //   this.showOverlay = true;
    // }
  
    // closeOverlay() {
     
    //   this.showOverlay = false;
    // }
    previewPhoto(photo: any) {
      // Open a new window with the image preview
      const newWindow = window.open('', '_blank');
      
      if (newWindow) {
        newWindow.document.write(`
          <html>
            <head>
              <title>${photo.photoName}</title>
            </head>
            <body style="margin: 0; display: flex; justify-content: center; align-items: center; height: 100vh;">
              <img src="data:image/jpeg;base64,${photo.photo}" alt="${photo.photoName}" class="img-fluid">
              <script>
                // Close the window on button click (optional)
                function closeWindow() {
                  window.close();
                }
              </script>
            </body>
          </html>
        `);
      } else {
        // Handle if the new window fails to open
        console.error('Failed to open a new window');
      }
    }
    navToDashboard(){
      this.router.navigate(['/dashboard'])
    }

    onTabSearch() {
      if (!this.tabSearchText.photo && !this.tabSearchText.description 
        ) {
          this.sortclaimPhotos = this.claimPhotosData;
      } else {
        if (
          this.tabSearchText.photo != undefined &&
          this.tabSearchText.photo != ''
        ) {
          this.sortclaimPhotos = this.claimPhotosData.filter((user: any) => {
            if (user.photoName != null) {
              return user.photoName
                .toLowerCase()
                .includes(this.tabSearchText.photo.toLowerCase());
            }
          });
        }
        if (
          this.tabSearchText.description != undefined &&
          this.tabSearchText.description != ''
        ) {
          this.sortclaimPhotos = this.claimPhotosData.filter((user: any) => {
            if (user.photoDescription != null) {
              return user.photoDescription
                .toLowerCase()
                .includes(this.tabSearchText.description.toLowerCase());
            }
          });
        }
       
        
      }
    }

    sortData(sort: Sort) {
      const data = this.claimPhotos.slice(); // Use productCatalog for sorting
    
      if (!sort.active || sort.direction === '') {
        this.sortclaimPhotos = data;
        return;
      }
    
      this.sortclaimPhotos = data.sort((a:any, b:any) => {
        const isAsc = sort.direction === 'asc';
        switch (sort.active) {
          case 'photo':
            return this.compare(a.photoName, b.photoName, isAsc);
          case 'description':
            return this.compare(a.photoDescription, b.photoDescription, isAsc);
          default:
            return 0;
        }
      });
    }
    
    
    // Comparison function for sorting
     compare(a: string | number, b: string | number, isAsc: boolean) {
    return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
    }

    public disableSaveBtn(){
      if(this.selectedFileName != null && this.photoDesc != '' && this.selectedFileData != null){
        return false;
      }else{
        return true;
      }
    }
  
  }
  
