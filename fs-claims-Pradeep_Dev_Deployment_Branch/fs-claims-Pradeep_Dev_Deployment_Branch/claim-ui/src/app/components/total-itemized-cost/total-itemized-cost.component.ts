import { Component,Input, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ClaimDetailService } from 'src/app/ngrx/store/services/claim-detail.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}


@Component({
  selector: 'app-total-itemized-cost',
  templateUrl: './total-itemized-cost.component.html',
  styleUrls: ['./total-itemized-cost.component.scss'],

})
export class TotalItemizedCostComponent implements OnInit{
  @Input() screenWidth = 0;
  public isSideNavCollapsed: boolean = true;
  public ticCalculationData: any;
  public totalMerchandise: number = 0;
  public totalMerchandiseAmount : any;
  public claimId: any = '';
  public claimNumber:any;
  public laborRateData:any[]= [];
  public unitOFMeasureData: any[]= [];
  public claimLineId:number = 0;
  public estimateAmount:number = 0;

  constructor(
    private bodyClassService: BodyClassService,
    private claimdetailService:ClaimDetailService,
    private route:ActivatedRoute){
      this.route.params.subscribe(params => {
        // Access the claimNumber parameter
        this.claimId = params['claimid'];
        // Load data based on the new claimNumber
       
      });
    }
   

  ngOnInit(){
    this.route.paramMap.subscribe(params => {
      this.claimId = window.history.state.claimId;
     
      this.claimNumber =  window.history.state.claimNumber;
   

      });
  
    this.claimdetailService.ticCalculation( this.claimId,this.claimLineId,this.estimateAmount).subscribe(data =>{
      this.ticCalculationData = data.body
      // if(this.ticCalculationData.marchandiseTotal.length > 1){
      //   this.ticCalculationData.marchandiseTotal.forEach((data:any)=>{
      //     this.totalMerchandise = this.totalMerchandise + data.estimateAmount
      //   })
      // }else{
      //   this.totalMerchandise = this.ticCalculationData.marchandiseTotal[0].estimateAmount
      // }
     
    })
   
  }  
   
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    public openModalView(){
      const modelDiv = document.getElementById('detailstotalcost');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      this.totalMerchandiseAmount =  this.ticCalculationData?.marchandiseTotal.total
    }

    public openModalLabor(){
      const modelDiv = document.getElementById('laborline');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      this.findAllLaborRates();
      this.unitOfMeasure();
    }

    public modalData : any[]=[];
    public openModaldealerCharges(linedetails:any){
      const modelDiv = document.getElementById('dealerCharges');
      if (modelDiv != null) {
        modelDiv.style.display = 'block';
      }
      this.modalData = linedetails.lineDetails
     
    }
    
    
    public closeModal(id:string){
      const modelDiv = document.getElementById(id);
      if (modelDiv != null) {
        modelDiv.style.display = 'none';
      }}

      public changeEstimateAmt(amount:any,index:any){
        // console.log(amount.target.value,'......amount')
        // console.log(index,'......index')
        this.totalMerchandiseAmount = 0;
        let totalAmount = 0
        let arrayData = this.ticCalculationData.marchandiseTotal
        if(arrayData.length > 1 ){
          for (let i = 0; i < arrayData.length; i++) {
            
            if(i == index){
             totalAmount = totalAmount +  amount.target.value

            }
            else{
              totalAmount = totalAmount + arrayData[i].estimateAmount
            }
          }
          this.totalMerchandiseAmount = totalAmount
        }else{
          this.totalMerchandiseAmount = amount.target.value
        }
       
      }

      public findAllLaborRates(){
        this.claimdetailService.findAllLaborRates().subscribe(data=>{
          this.laborRateData = data.body
        })
      }

      public unitOfMeasure(){
        this.claimdetailService.getClaimUnitOfMeasure().subscribe(data=>{
          this.unitOFMeasureData = data
        })
      }
  
}
