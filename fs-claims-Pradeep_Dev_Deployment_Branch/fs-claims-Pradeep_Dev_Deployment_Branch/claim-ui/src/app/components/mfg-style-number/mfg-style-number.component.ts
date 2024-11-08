import { Component,Input } from '@angular/core';
import { BodyClassService } from 'src/app/ngrx/store/services/body-class.service';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';
interface SideNavToggle {
  screenWidth: number;
  collapsed: boolean;
}

@Component({
  selector: 'app-mfg-style-number',
  templateUrl: './mfg-style-number.component.html',
  styleUrls: ['./mfg-style-number.component.scss']
})
export class MfgStyleNumberComponent {

  styleNumberData:{ [key: string]: string }[] = [];

  constructor(
    private bodyClassService: BodyClassService,
    private productCatalogSpecService: ProductCatalogSpecService){}
    @Input() screenWidth = 0;
    public isSideNavCollapsed: boolean = true;
    public getBodyClass(): string {
      return this.bodyClassService.getBodyClass(this.isSideNavCollapsed, this.screenWidth);
    }

    public onToggleSideNav(data: SideNavToggle): void {
      this.screenWidth = data.screenWidth;
      this.isSideNavCollapsed = data.collapsed;
    }

    ngOnInit(): void {
      this.getStyleNumberData();
    }

    getStyleNumberData(){
      this.productCatalogSpecService.getStyleNumberData().subscribe((data) => {
        this.styleNumberData = data;
      });
    }
}
