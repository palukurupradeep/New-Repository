import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { ProductCatalogSpecService } from 'src/app/ngrx/store/services/product-catalog-spec.service';

@Component({
  selector: 'app-product-catalog-spec-view-colors',
  templateUrl: './product-catalog-spec-view-colors.component.html',
  styleUrls: ['./product-catalog-spec-view-colors.component.scss']
})
export class ProductCatalogSpecViewColorsComponent implements OnInit{

  colors: { number: string, name: string, checked: boolean }[] = [];
  sortedColors: { number: string, name: string, checked: boolean }[] = [];

  constructor(private productCatalogSpecService: ProductCatalogSpecService) {}

  ngOnInit(): void {
    this.getColors();
  }

  getColors(): void {
    this.productCatalogSpecService.getColors()
      .subscribe((data: { number: string, name: string, checked: boolean }[]) => {
        this.colors = data;
        this.sortedColors = this.colors.slice();  // Set initial sorted data
      });
  }

  checkInventory(): void {
    const checkedColors = this.sortedColors.filter(color => color.checked);
    console.log('Checked Colors:', checkedColors);
  }

  sortData(sort: Sort): void {
    const data = this.colors.slice();  // Use the original colors array for sorting

    if (!sort.active || sort.direction === '') {
      this.sortedColors = data;  // If no sorting is active, reset to the original order
      return;
    }

    this.sortedColors = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'number':
          return compare(a.number, b.number, isAsc);
        case 'name':
          return compare(a.name, b.name, isAsc);
        default:
          return 0;
      }
    });
  }

}

// Comparison function for sorting strings
function compare(a: string | number, b: string | number, isAsc: boolean): number {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
