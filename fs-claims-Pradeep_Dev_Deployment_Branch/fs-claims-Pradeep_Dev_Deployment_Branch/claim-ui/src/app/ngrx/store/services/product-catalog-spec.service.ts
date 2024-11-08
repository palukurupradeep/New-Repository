import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

//PROCESS-RGA-RECEIPT
export interface plantTableData {
  id: string;           // For the link (e.g., 357627)
  plant: string;       // Plant (e.g., DU)
  receivedDate: string; // Date when it was received (e.g., 2022-05-10)
  status: string;      // Status (e.g., *PARTIAL, RECEIVED)
  width: number;       // Width (e.g., 54)
  rollNumber: number;  // Roll Number (e.g., 173)
  lot: number;         // Lot (e.g., 75)
  quantity: number;    // Quantity (e.g., 0)
}

export interface RaTableData {
  action: string;
  lineNumber: string;
  skuNumber: string;
  pm: string;
  grade: string;
  styleName: string;
  colorName: string;
  trailerNumber: string;
  fromPlant: string;
  pendingQuantity: number;
  receivedQuantity: number;
  sentQuantity: number;
  openQuantity: number;
}

export interface FormData {
  action: string;
  lineNumber: string;
  skuNumber: string;
}

export interface RaFormData {
  sendToPlant: string[];
  quantity: number;
  trailerNumber: string;
  expectedReceiveDate: string;
  comment: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProductCatalogSpecService {

  // Mock data 
  private claimData = {
    claimNo: '938255',
    customerNo: '0058283',
    claimInitiationDate: '08/25/2010',
    customerName: 'Glanz industries inc'
  };

  private productSpecifications = {
    styleName: 'Media',
    styleNumber: 'J0062',
    mfg_StyleNumber: 'M6681',
    sellingCompany: {
      sellingCompanyCode: '64',
      sellingCompanyName: 'Queen Commercial'
    },
    customerReferenceNumber: '102',
    productCode: '20',
    productType: 'Carpet',
    description: 'Graphic Loop',
    size: {
      sizeLength: '144.0',
      sizeBreadth: '200.0'
    }
  };

  private colors = [
    { number: '62102', name: 'TV', checked: false },
    { number: '62202', name: 'Satellite', checked: false },
    { number: '62204', name: 'Magazine', checked: false },
    { number: '62309', name: 'Network', checked: false },
    { number: '62312', name: 'Newspaper', checked: false },
    { number: '62102', name: 'Books', checked: false },
    { number: '62406', name: 'Internet', checked: false },
    { number: '', name: 'No Color', checked: false }
  ];

  private shopOrderData = {
    OrderDate: '2010-05-13',
    CompleteDate: '2010-06-11',
    Style: '24QQ8',
    Color: '49182',
    StdWidth: '144',
    CutLength: '1200',
    GGComp: '0W4G',
    BackingCode: '301'
  };

  private productionRoutingData = [
    { process: 'Tufting', plantCode: 'D4', plantName: 'Dixie harris tufting 185 S.ind Blvd. Calh' },
    { process: 'Off Line Shearing', plantCode: '20', plantName: 'Riverbend' },
    { process: 'Foaming', plantCode: '03', plantName: 'Star three' },
    { process: 'Chroma Jet Printer', plantCode: 'WF', plantName: 'Queen Printing - Calhoun' }
  ];

  private shopOrderRollData = [
    {
      rollNbr: 'T034840',
      createDate: '2010-05-13',
      plantNbr: 'WF',
      creditMemoNbr: '',
      issuedDate: '',
      lineNbr: '',
      reasonCode: ''
    },
    {
      rollNbr: 'T034845',
      createDate: '2010-05-13',
      plantNbr: 'WF',
      creditMemoNbr: '',
      issuedDate: '',
      lineNbr: '',
      reasonCode: ''
    },
    {
      rollNbr: 'I170436',
      createDate: '2010-05-11',
      plantNbr: 'DL',
      creditMemoNbr: '',
      issuedDate: '',
      lineNbr: '',
      reasonCode: ''
    },
    {
      rollNbr: 'DK71898',
      createDate: '2010-06-14',
      plantNbr: 'DL',
      creditMemoNbr: '998720<br>060052<br>998720',
      issuedDate: '2010-11-19<br>2011-04-29<br>2010-06-11',
      lineNbr: '005<br>001<br>001',
      reasonCode: 'D05<br>D05<br>D05'
    }
  ];

  private MfgProductionRoutingData = [
    { process: 'Tufting', plantNo: '13', plantName: 'West Avenue (C\'Ville)' },
    { process: 'Coating', plantNo: '13', plantName: 'West Avenue (C\'Ville)' }
  ];

  private StyleNumberData = [
    { sellingCompany: '64', sellingStyle: '7W449', sellingDescription: 'Seahurst', referenceNbr: '149', status: 'A', brand: '70', brandDescription: 'Ecosoltn Q' },
    { sellingCompany: '64', sellingStyle: 'J0062', sellingDescription: 'Media', referenceNbr: '102', status: 'A', brand: '70', brandDescription: 'Ecosoltn Q' },
    { sellingCompany: '64', sellingStyle: '7W767', sellingDescription: 'Royal Splendor', referenceNbr: '228', status: 'A', brand: '70', brandDescription: 'Ecosoltn Q' },
    { sellingCompany: '64', sellingStyle: '7W7632', sellingDescription: 'Main Force', referenceNbr: '218', status: 'A', brand: '70', brandDescription: 'Ecosoltn Q' }
  ]


// PROCESS-RGA-RECIEPT
private plantsData: string[] = ['DU', 'PQ'];

private mockplantTableData: plantTableData[] = [
  { id: '357627', plant: 'DU', receivedDate: '2022-05-10', status: '*PARTIAL', width: 54, rollNumber: 173, lot: 75, quantity: 0 },
  { id: '363163', plant: '', receivedDate: '2022-05-10', status: 'RECEIVED', width: 0, rollNumber: 27, lot: 0, quantity: 27 },
  { id: '367911', plant: '', receivedDate: '2022-07-21', status: 'RECEIVED', width: 0, rollNumber: 89, lot: 0, quantity: 89 },
  { id: '371052', plant: '', receivedDate: '2023-01-31', status: 'RECEIVED', width: 0, rollNumber: 27, lot: 0, quantity: 27 },
  { id: '372901', plant: '', receivedDate: '2022-08-14', status: 'RECEIVED', width: 0, rollNumber: 14, lot: 0, quantity: 14 },
  { id: '376928', plant: 'TU', receivedDate: '2022-10-24', status: '*PENDING', width: 19, rollNumber: 0, lot: 0, quantity: 0 },
];

private raTableData: RaTableData[] = [
  {
    action: 'Send',
    lineNumber: '001',
    skuNumber: 'UV757 02905 21T',
    pm: 'P',
    grade: 'A',
    styleName: 'CPREMIUM9 30 MIL',
    colorName: 'OPULENCE OAK',
    trailerNumber: 'NIL',
    fromPlant: 'DU',
    pendingQuantity: 44,
    receivedQuantity: 64,
    sentQuantity: 64,
    openQuantity: 0,
  },
  {
    action: '', // No action provided for this row
    lineNumber: '003',
    skuNumber: 'UV757 02905 21T',
    pm: 'P',
    grade: 'A',
    styleName: 'CPREMIUM9 30 MIL',
    colorName: 'OPULENCE OAK',
    trailerNumber: 'NIL',
    fromPlant: 'DU',
    pendingQuantity: 10,
    receivedQuantity: 10,
    sentQuantity: 10,
    openQuantity: 0,
  },
];

private formData: FormData = {
  action: 'Send',
  lineNumber: '001',
  skuNumber: 'UV757 02905 21T',
};

private raFormData: RaFormData = {
  sendToPlant: ['3U', '4B'],  // Plant options
  quantity: 100,              // Mock quantity data
  trailerNumber: 'ABC123',     // Mock trailer number
  expectedReceiveDate: '10/30/2024',  // Mock date
  comment: 'No comments',     // Mock comment
};



  constructor() { }

  // Method to get claim data
  getClaimData(): Observable<any> {
    return of(this.claimData);
  }

  getProductSpecifications(): Observable<{ [key: string]: string }> {
    const modifiedProductSpecifications = {
      ...this.productSpecifications,
      // Interpolate sellingCompanyCode and sellingCompanyName into a single string
      sellingCompany: `${this.productSpecifications.sellingCompany.sellingCompanyCode} - ${this.productSpecifications.sellingCompany.sellingCompanyName}`,
      // Interpolate sizeLength and sizeBreadth into a single string
      size: `${this.productSpecifications.size.sizeLength} X ${this.productSpecifications.size.sizeBreadth}`
    };
    // Send back the modified object with sellingCompany, size as a string
    return of(modifiedProductSpecifications);
  }

  getColors(): Observable<{ number: string, name: string, checked: boolean }[]> {
    return of(this.colors);
  }

  getShopOrderData(): Observable<{ [key: string]: string }> {
    return of(this.shopOrderData);
  }

  getProductionRoutingData(): Observable<{ process: string, plant: string }[]> {
    const modifiedProductionRoutingData = this.productionRoutingData.map(item => ({
      ...item,
      plant: `${item.plantCode} - ${item.plantName}`
    }));

    return of(modifiedProductionRoutingData);
  }

  getShopOrderRollData(): Observable<{ [key: string]: string }[]> {
    return of(this.shopOrderRollData);
  }

  getMfgProductionRoutingData(): Observable<{ process: string, plant: string }[]> {
    const MfgProductionRoutingDatas = this.MfgProductionRoutingData.map(item => ({
      ...item,
      plant: `${item.plantNo} - ${item.plantName}`
    }));
    return of(MfgProductionRoutingDatas);
  }

  getStyleNumberData(): Observable<{ [key: string]: string }[]> {
    return of(this.StyleNumberData);
  }


  // PROCESS-RGA-RECEIPT
  getPlantsData(): Observable<string[]> {
    return of(this.plantsData); // Return the mock data as an observable
  }

  getPlantTableData(): Observable<plantTableData[]> {
    return of(this.mockplantTableData); // Return the mock data as an observable
  }

  getRaTableData(): Observable<RaTableData[]> {
    return of(this.raTableData);
  }

  getFormData(): Observable<FormData> {
    return of(this.formData);
  }

  getRaFormData(): Observable<RaFormData> {
    return of(this.raFormData);
  }

}
