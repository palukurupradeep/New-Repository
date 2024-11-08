import { Injectable } from '@angular/core';
import { BehaviorSubject, of } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class ProductCatalogService {
  private productCatalogSubject = new BehaviorSubject<any[]>([]);

  productCatalog$: Observable<any[]> = this.productCatalogSubject.asObservable();

  private inventoryDataSubject = new BehaviorSubject<any[]>([]);
  inventoryDetail$:Observable<any[]> = this.inventoryDataSubject.asObservable();

  // Mock data 
  private claimData = {
    claimNo: '938255',
    customerNo: '0058283',
    claimInitiationDate: '08/25/2010',
    customerName: 'Glanz industries inc'
  };



  private productOptions = [
    { value: 'Carpet', label: 'Carpet' },
    { value: 'Hardwood', label: 'Hardwood' },
    { value: 'Vinyl', label: 'Vinyl' },
    { value: 'Laminate', label: 'Laminate' },
    { value: 'Tile', label: 'Tile' },
    { value: 'Stone', label: 'Stone' },
  ];

  private productCatalogResults = [
    {
      styleNumber: 'J0062',
      styleName: 'Media',
      product: 'Carpet',
      sellingCompanyCode: '64',
      sellingCompany: 'Queen Commercial',
      status: 'Active',
      custRefNumber: 'CRN001'
    },
    {
      styleNumber: 'J0063',
      styleName: 'Majestic',
      product: 'Carpet',
      sellingCompanyCode: '64',
      sellingCompany: 'Queen Commercial',
      status: 'Active',
      custRefNumber: 'CRN003'
    },
    {
      styleNumber: 'X1043',
      styleName: 'Classic',
      product: 'Hardwood',
      sellingCompanyCode: '32',
      sellingCompany: 'Empire Flooring',
      status: 'Inactive',
      custRefNumber: 'CRN002'
    },
    {
      styleNumber: 'C2034',
      styleName: 'Elegance',
      product: 'Vinyl',
      sellingCompanyCode: '21',
      sellingCompany: 'Vinyl World',
      status: 'Active',
      custRefNumber: 'CRN004'
    },
    {
      styleNumber: 'J0071',
      styleName: 'Opulence',
      product: 'Carpet',
      sellingCompanyCode: '55',
      sellingCompany: 'Luxury Floors',
      status: 'Active',
      custRefNumber: 'CRN005'
    },
    {
      styleNumber: 'H4587',
      styleName: 'Oakwood',
      product: 'Hardwood',
      sellingCompanyCode: '33',
      sellingCompany: 'Wood Creations',
      status: 'Active',
      custRefNumber: 'CRN006'
    },
    {
      styleNumber: 'V8912',
      styleName: 'Rustic',
      product: 'Vinyl',
      sellingCompanyCode: '21',
      sellingCompany: 'Vinyl World',
      status: 'Inactive',
      custRefNumber: 'CRN007'
    },
    {
      styleNumber: 'L9903',
      styleName: 'Chic',
      product: 'Laminate',
      sellingCompanyCode: '78',
      sellingCompany: 'Laminate Pros',
      status: 'Active',
      custRefNumber: 'CRN008'
    },
    {
      styleNumber: 'C2048',
      styleName: 'Modern',
      product: 'Carpet',
      sellingCompanyCode: '55',
      sellingCompany: 'Luxury Floors',
      status: 'Inactive',
      custRefNumber: 'CRN009'
    }
  ];

  private plantInformation = {
    plantManager: 'George Campbell',
    qualityManager: 'Rhonda Ryan',
    qualityManagerPhoneNumber: '7708787583',
    address1: 'HWY 61',
    address2: 'PO BOX 429',
    cityStateZip: 'Cartersville, GA 30120',
    county: '',
    country: 'USA',
    phoneNumber: '7703878750'
  };



  constructor() { }

  // Method to get claim data
  getClaimData(): Observable<any> {
    return of(this.claimData);
  }

  // Method to reset product catalog
  resetProductCatalog(): void {
    this.productCatalogSubject.next([]);  // Reset catalog to empty array
  }

  resetInventoryDetail(): void {
    this.inventoryDataSubject.next([]);  
  }


  // Method to fetch product options
  getProductOptions(): Observable<any[]> {
    return of(this.productOptions);
  }

  // Method to handle find button click and pass payload
  searchProductSpecifications(payload: any): Observable<any> {
    console.log('Service Payload received:', payload);
    //  return mock data

    // Filter the product catalog based on the payload
    const filteredResults = this.productCatalogResults.filter(product => {
      let matches = true;

      if (payload.product && product.product.toLowerCase() !== payload.product.toLowerCase()) {
        matches = false;
      }
      if (payload.styleNumber && product.styleNumber !== payload.styleNumber) {
        matches = false;
      }
      if (payload.styleName && product.styleName.toLowerCase() !== payload.styleName.toLowerCase()) {
        matches = false;
      }
      if (payload.custRefNumber && product.custRefNumber !== payload.custRefNumber) {
        matches = false;
      }

      return matches;
    });


    this.productCatalogSubject.next(filteredResults);
    console.log(filteredResults, "filteredResults")

    return of(filteredResults);
  }

  getPlantInformation(): Observable<{ [key: string]: string }> {
    return of(this.plantInformation);
  }

  getInventoryData(payload: any): Observable<any[]> {
    console.log('Payload received:', payload);

    // Mock data representing inventory
    const mockData = [
      { plant: 'Plant1', rollCode: 'RC001', width: '50', length: '100', rollNumber: '12345', dyelot: 'Dye01', grp: 'GrpA', finishDate: '2024-09-25', rackLocation: 'Rack1', status: 'Available', orderDate: '2024-09-01' },
      { plant: 'Plant2', rollCode: 'RC002', width: '60', length: '150', rollNumber: '54321', dyelot: 'Dye02', grp: 'GrpB', finishDate: '2024-09-26', rackLocation: 'Rack2', status: 'Pending', orderDate: '2024-08-30' },
      { plant: 'Plant3', rollCode: 'RC003', width: '55', length: '120', rollNumber: '67890', dyelot: 'Dye03', grp: 'GrpC', finishDate: '2024-09-27', rackLocation: 'Rack3', status: 'Shipped', orderDate: '2024-09-15' },
      { plant: 'Plant4', rollCode: 'RC004', width: '65', length: '130', rollNumber: '13579', dyelot: 'Dye04', grp: 'GrpD', finishDate: '2024-09-28', rackLocation: 'Rack4', status: 'Available', orderDate: '2024-09-01' },
      { plant: 'Plant5', rollCode: 'RC005', width: '70', length: '160', rollNumber: '24680', dyelot: 'Dye05', grp: 'GrpE', finishDate: '2024-09-29', rackLocation: 'Rack5', status: 'Pending', orderDate: '2024-09-15' }
    ];

    // Filter data based on Order Date (if provided in the payload)
    const filteredData = mockData.filter(item => item.orderDate === payload.orderDate);
    this.inventoryDataSubject.next(filteredData);
    console.log(filteredData, "filteredResults")
    return of(filteredData); // Return filtered data
  }

  

  // Mock data for color options
  getColorOptions(): Observable<string[]> {
    const colors = ['Color1', 'Color2', 'Color3', 'Color4'];
    return of(colors);
  }

  // Mock data for location options
  getLocationOptions(): Observable<string[]> {
    const locations = ['Location1', 'Location2', 'Location3', 'Location4'];
    return of(locations);
  }


}
