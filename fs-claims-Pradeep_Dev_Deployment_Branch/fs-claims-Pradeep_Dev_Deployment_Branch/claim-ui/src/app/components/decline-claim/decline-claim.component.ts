import { Component } from '@angular/core';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';

@Component({
  selector: 'app-decline-claim',
  templateUrl: './decline-claim.component.html',
  styleUrls: ['./decline-claim.component.scss']
})
export class DeclineClaimComponent {
  public declineList: any;

 constructor(
  private claimService: ClaimDetailService,
 ){

 }
  ngOnInit(): void {
    this.getAllDeclinedReasons()
  }


  getAllDeclinedReasons(){
    this.claimService.getAllDeclinedReasons().subscribe(
      (data:any) => {
        this.declineList = data;
      })
  }

}
