import { Component, EventEmitter, Input, Output, SimpleChanges } from '@angular/core';
import { ClimDetailsResponce } from 'src/app/interfaces/claim-details.model';
import { SmartCodeService } from 'src/app/ngrx/store/services/smart-code.service';

@Component({
  selector: 'app-analyze-claim',
  templateUrl: './analyze-claim.component.html',
  styleUrls: ['./analyze-claim.component.scss']
})
export class AnalyzeClaimComponent {
  @Input() analyzeClaimdetailsData:any;
  @Input() claimList: any = [];
  @Input() customerdata:any;
  public resolutions: any;
  public claimPhotos: any[]=[];
  @Input() reasonCodeData:any;
  @Input() resolutionsAndPhotosData:any;
  public visibleList:any=[];
  public lookUpCode : string = ''
 

  constructor(
    private smartCodeService : SmartCodeService
  ){}
  
  ngOnInit() {

  }

  ngOnChanges(){
    if(this.reasonCodeData != undefined){
      this.getLookupTypeForVisibilityScopeField();
    }
  }

  // For the field with formControlName="visibilityScope"
public getLookupTypeForVisibilityScopeField() {
  const lookupTypeCode = 'CLM_RSN_VISIBILITY'; // Specify the lookupTypeCode for the "visibilityScope" field
  this.smartCodeService.getLookupTypeByCode(lookupTypeCode).subscribe(data => {
    this.visibleList = data.body
    let reasoncode = this.visibleList.filter((ele:any)=>ele.lookupId == this.reasonCodeData?.claimReasonDefinition?.visibilityScope)
    this.lookUpCode = reasoncode[0].lookupDescription
  });
  }



  public previewPhoto(photo: any) {
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
   
    }
  }
 
}
