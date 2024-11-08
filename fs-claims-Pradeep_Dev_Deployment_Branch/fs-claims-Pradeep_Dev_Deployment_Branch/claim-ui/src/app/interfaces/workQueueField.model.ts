export interface WorkQueueField {
    createdByUserId: number;
    modifiedByUserId: number;
    createdDateTime: string;
    modifiedDateTime: string;
    workQueueFieldId: number;
    workQueueFieldName: string;
    workQueueFieldDescription: string;
    displaySequence: number;
    displayByDefault: boolean;
    statusId: number;
  }
  
  export interface WorkQueuePayload {
    createdByUserId: number;
    modifiedByUserId: number;
    workQueueField: WorkQueueField[];
  }
  