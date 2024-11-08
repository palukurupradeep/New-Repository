import { Component, Input, OnInit, SimpleChanges } from '@angular/core';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';

@Component({
  selector: 'app-claim-rate-detail',
  templateUrl: './claim-rate-detail.component.html',
  styleUrls: ['./claim-rate-detail.component.scss']
})
export class ClaimRateDetailComponent implements OnInit{

  @Input()custNum: any = [];
  public claimRateDetails: any = [];
  @Input() claimIsVoid:any = false;

  constructor(private claimService: ClaimDetailService){}

  ngOnInit(): void {
    
  }
  // track On Update the parent input value
  ngOnChanges(changes: SimpleChanges) {
    // console.log('Changes1' + this.claimRateDetails.customerClaimRateDetailDTO)
    if(this.custNum.customerNumber != undefined){
      this.getClaimRateDetails();
    }
  }

  
  getClaimRateDetails(){
    if(!this.claimIsVoid){
    this.claimService.getClaimRateDetails(this.custNum.customerNumber).subscribe(
      data =>{
        this.claimRateDetails = data;
        // console.log('Changes2' + this.claimRateDetails.customerClaimRateDetailDTO)
      },
      error => {
        this.claimRateDetails = [];
      }
    )
    }
  }

  isEmptyObject(obj: any) {
    return (obj && (Object.keys(obj).length === 0));
  }
}
