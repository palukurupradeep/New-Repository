export interface saveNoteTemplatePayload{
    createdByUserId: number,
    modifiedByUserId: number,
    noteTemplateId: number,
    noteTemplateName: string,
    noteTemplateText: string,
    noteGroupId: number,
    noteGroupName: string,
    noteTypeId: number,
    noteTypeName: string,
    editable: boolean,
    isDefault: boolean
  }