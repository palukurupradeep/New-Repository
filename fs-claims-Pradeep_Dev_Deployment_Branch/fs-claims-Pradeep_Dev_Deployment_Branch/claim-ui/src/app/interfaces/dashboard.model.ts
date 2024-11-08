  export interface CustomerTypeRequestPayload {
    cusType: string;
    prodType: string;
    invoiceDate: string;
    size: string;
    discount: string;
    carpetRange: string;
  }
  export interface CustomerTypeResponseModel {
    customerType: string;
    productCategory: string;
    discount: number;
  }
  export interface CustomerWatchlistPayload{
   
      createdByUserId: number,
      modifiedByUserId: number,
      // createdDateTime: string,
      // modifiedDateTime: string,
      customerWatchlistId: number,
      customerNumber: string,
      statusId: number
    
  }
  