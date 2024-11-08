    export interface ClaimDuesResponseModel {
    label: string;
    text: string;

  }
  
  export interface ClaimDocument {
    documentTypeId: number;
    documentNumber: string;
  }
  
 export interface ClaimLineDetail {
    lineNumber?: number;
    documentNumber: string;
    rollNumber: string;
    styleNumber: string;
    colorNumber: string;
    widthInFeet: number;
    lengthInFeet: number;
    grade: string;
    unitPrice: number;
    lineAmount: number;
    dyeLot: string;
    quantity: number;
    unitOfMeasureCode: string; // Corrected property name (removed extra space at the end)
}

//  export interface EndUserInformationDTO {
//     firstName?: string;
//     middleInitial?: string;
//     lastName?: string;
//     companyName?: string;
//     countryId?: string;
//     addressLine1?: string;
//     addressLine2?: string;
//     city?: string;
//     stateId?: string;
//     postalCode?: string;
//     county?: string;
//     businessPhoneDialCodeId?: string;
//     businessPhoneNumber?: string;
//     businessPhoneExtension?: string;
//     homePhoneNumber?: string;
//     homePhoneExtension?: string;
//     cellPhoneDialCodeId?: string;
//     cellPhoneNumber?: string;
//     cellPhoneExtension?: string;
//     emailAddress?: string;
//   }
  
  export interface PrioClaimEndUserInformationDTO {
    firstName?: string;
    lastName?: string;
    addressLine1?: string;
    homePhoneNumber?: string;
    businessPhoneNumber?:string;
    cellPhoneNumber?:string;
  }
  export interface priorClaimPayload {
    customerNumber: string;
   // claimCategoryId: number;
    //claimReasonId: number;
   // jobStopped: boolean;
   // workStatusId: number;
  //  priorityClaim: boolean;
    //globalAccount: number;
    claimDocumentDTO?: ClaimDocument[];
    endUserInformationDTO?: PrioClaimEndUserInformationDTO;
  }

  export interface initiateClaimPayload {
    customerNumber: string;
     claimCategoryCode: string;
    //claimCategoryId: number;
    // claimReasonId: number;
    jobStopped: boolean;
    //workStatusId: number;
    claimReasonCode: string;
    businessMarketIndicator:number;
    priorityClaim: boolean;
    endUserClaim:boolean;
    // globalAccount: number;
    addToWatchList:boolean;
    // claimAmountUsd: number;
    exchangeRate: string,
    sellingCompany:string,
    territory: string,
    division:string,
    region:string,
    createdByUserId:number;
    modifiedByUserId: number;
    claimDocumentDTO?: ClaimDocument[];
    endUserInformationDTO?: EndUserInformationDTO;
    claimLineDetailDTOS?:ClaimLineDetail[];
  }


 
  
  
  export interface ClaimRequest {
    customerNumber: string;
    primaryCustNumber:string;
    storeNumber:string;
    claimCategoryCode: string;
    claimReasonCode: string;
    jobStopped: boolean;
    priorityClaim: boolean;
    endUserClaim: boolean;
    businessMarketIndicatorCode: string;
    exchangeRate: number;
    sellingCompany: string;
    territory: string;
    division: string;
    region: string;
    addToWatchList: boolean;
    claimDocumentDTOS?: ClaimDocumentDTO[]; // Add this property
    endUserInformationDTO?: EndUserInformationDTO; // Add this property
    createdByUserId: number;
    modifiedByUserId: number;
    claimNoteDTO: {
      claimNoteText: string | any,
      noteTypeCode:string
  };
  claimAddressDTO?:any;
  }
  
  interface ClaimDocumentDTO {
    documentNumber: string;
    exchangeRate: number;
    orderNumber: string;
    purchaseOrderNumber: string;
    billofLading: string;
    masterBillofLading: string;
    documentTypeCode: string;
    claimReasonCode: string;
    claimLineDetailDTOS: ClaimLineDetailDTO[];
  }
  
  interface ClaimLineDetailDTO {
    lineNumber: number;
    documentNumber: string;
    rollNumber: string;
    styleNumber: string;
    colorNumber: string;
    widthInFeet: number;
    lengthInFeet: number;
    grade: string;
    unitPriceUsd: number;
    lineAmountUsd: number;
    dyeLot: string;
    quantity: number;
    unitOfMeasureCode: string;
    rcsCode: string;
    claimReasonCode: string;
    lineAmountForeign: number;
    unitPriceForeign: number;
    pricingCurrencyCode: string;
    currencyCodeDesignation: string;
    currencyCodeName: string;
    exchangeRate: number;
    sellingCompany: string;
    productCode: string;
    inventoryStyle: string;
    inventoryColor: string;
    manufacturingPlant: string;
    detailTypeCode: string;
    lineSourceCode: string;
    lineSourceReference: string;
    lineAddReasonCode: string;
    vendorId: string;
    claimDetailRecordDTOS: ClaimDetailRecordDTO[];
  }
  
  interface ClaimDetailRecordDTO {
    detailRecordTypeCode: string;
    widthInFeet: number;
    widthInInches: number;
    lengthInFeet: number;
    lengthInInches: number;
    quantity: number;
    unitOfMeasure: string;
    amountUsd: number;
    amountForeign: number;
    unitPriceUsd: number;
    unitPriceForeign: number;
    exchangeRate: number;
  }
  
  interface EndUserInformationDTO {
    firstName: string;
    middleInitial: string;
    lastName: string;
    companyName: string;
    countryId: number;
    addressLine1: string;
    addressLine2: string;
    city: string;
    stateId: number;
    postalCode: string;
    county: string;
    businessPhoneDialCodeId: number;
    businessPhoneNumber: string;
    businessPhoneExtension: string;
    homePhoneNumber: string;
    homePhoneDialCodeId: number;
    homePhoneExtension: string;
    cellPhoneNumber: string;
    cellPhoneDialCodeId: number;
    cellPhoneExtension: string;
    emailAddress: string;
  }
  
  
  
  
  
  