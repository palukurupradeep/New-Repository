import { Component, EventEmitter, Output } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { MsalService } from '@azure/msal-angular';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
import { ToasterService } from 'src/app/ngrx/store/services/toaster.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  @Output() fetchClaimDetails = new EventEmitter<string>();
  //name: string | null ='';
  name:any
  claimNumber: string ='';

  constructor(public router: Router,
     private msalService: MsalService,
    private claimService:ClaimDetailService,
    private toasterService: ToasterService) {

  }

  ngOnInit() {
    console.log('calling in header')
    console.log(localStorage.getItem('userName'), 'localstorageususerName');
    this.name = localStorage.getItem('userName');

  //  if (!this.name) {
      const activeAccount = this.msalService.instance.getActiveAccount();
      console.log(activeAccount,'activeAccount')
      if (activeAccount) {
        // console.log(activeAccount,'activeAccount')
        this.name = activeAccount.name;
       
       // this.name = activeAccount.username; // Change this line
        console.log(this.name,'this.nameheaderelse')
        localStorage.setItem('userName', this.name.toString()); // Change this line
      }
    //}
    
  }
  formatClaimNumber(claimNumber: string): string {
    // Ensure the claim number is always 6 digits long
    return claimNumber.padStart(6, '0');
  }
  fetchClaimDetail() {
    const formattedClaimNumber = this.formatClaimNumber(this.claimNumber);
    
    this.claimService.getClaim(formattedClaimNumber,Number(localStorage.getItem('userId'))).subscribe(
      (data: any) => {
        // this.router.navigate(['/claim-detail', this.claimNumber]);
        const url = this.router.serializeUrl(
          this.router.createUrlTree(['/claim-detail', formattedClaimNumber])
        );
        window.open(url, '_blank');
      },
      (error) => {
        this.toasterService.showToast('error', 'This claim is not available.');
      }
    )
   
  }

  searchByname(txtName:string){
    if(txtName != ''){
      window.open('https://connections.shawinc.com/home?aliaspath=%2fhome&searchtext=' + txtName+ '&searchmode=allwords','_blank')
    }
    
  }
 

  onInputChange(): void {
      // Trim the claimNumber to remove leading and trailing spaces
      this.claimNumber = this.claimNumber.trim();
  }
  public inputValidator(event: any) {
    //console.log(event.target.value);
    const pattern = /^[ A-Za-z0-9_@./#&+-]*$/;   
    //let inputChar = String.fromCharCode(event.charCode)
    if (!pattern.test(event.target.value)) {
      event.target.value = event.target.value.replace(/[^A-Za-z0-9_@./#&+-]/g, "");
      // invalid character, prevent input

    }
  }
}
