export interface Resolutionpayload{
   
        createdByUserId: number,
        modifiedByUserId: number,
        claimReasonId: number,
        resolutionId: number
     
}
export interface addReasonDefinitionPayload{
        createdByUserId: number,
        modifiedByUserId: number,
        claimReasonCode: string,
        claimReasonId: number,
        claimReasonDescription: string,
        claimCategoryCode: string,
        visibilityScope: number,
        reasonTypeGroup: number,
        isDistributionCompliance: boolean,
        sampleSizeRequirement: string,
        definition: string,
        cause: string,
        testing: string,
        statusId: number,
        allowanceToKeep: boolean,
        serviceToKeep: boolean,
        returnEligible: boolean,
        codeRuleReturnTypeCode: string
      }
      export interface updateReasonDefinitionPayload{
        createdByUserId: number,
        modifiedByUserId: number,
        claimReasonCode: string,
        claimReasonId: number,
        claimReasonDescription: string,
        claimCategoryCode: string,
        visibilityScope: number,
        reasonTypeGroup: number,
        isDistributionCompliance: boolean,
        sampleSizeRequirement: string,
        definition: string,
        cause: string,
        testing: string,
        statusId: number,
        allowanceToKeep: boolean,
        serviceToKeep: boolean,
        returnEligible: boolean,
        codeRuleReturnTypeCode: string
        }
        export interface RuleDefinition{
                createdByUserId: number,
                modifiedByUserId: number,
                codeRuleId: number, 
                claimReasonDefinitionDTO:ClaimReasonDefinitionDTO ,
                codeRuleCategoryDTO:CodeRuleCategoryDTO,
                ruleDescription: string,
                codeRuleTypeDTO:CodeRuleTypeDTO,
                codeRuleLevelDTO:CodeRuleLevelDTO,
                codeRuleImpactAreaDTO:CodeRuleImpactAreaDTO,
                codeRuleActionTypeDTO:CodeRuleActionTypeDTO,
                codeRuleDataTypeDTO:CodeRuleDataTypeDTO,
                actionDescription: string,
                successMessage: string,
                failureMessage: string,
                allowOverride: boolean,
                approvalRequired: boolean,
                updateWorkStatus: boolean,
                workStatusRemarks: string,
                workStatusDTO:WorkStatusDTO,
                codeRuleDataListDTO:CodeRuleDataListDTO
                   
        }
        export interface ClaimReasonDefinitionDTO{
                createdByUserId: number,
                modifiedByUserId: number,
                claimReasonCode: string,
                claimReasonId: number,
                claimReasonDescription: string             
        }
        export interface CodeRuleCategoryDTO{
                codeRuleCategoryId: number,
                codeRuleCategoryCode: string,
                codeRuleCategoryDescription: string  
        }
        export interface CodeRuleTypeDTO {
                codeRuleTypeId: number,
                codeRuleTypeCode: string,
                codeRuleTypeDescription: string
              }
              export interface CodeRuleLevelDTO {
                codeRuleLevelId: number,
                codeRuleLevelCode: string,
                codeRuleLevelDescription: string
              }
              export interface CodeRuleImpactAreaDTO {
                codeRuleImpactAreaid: number,
                codeRuleImpactAreaCode: string,
                codeRuleImpactAreaDescription: string
              }
              export interface CodeRuleActionTypeDTO {
                codeRuleActionTypeid: number,
                codeRuleActionTypeCode: string,
                codeRuleActionTypeDescription: string
              }
              export interface CodeRuleDataTypeDTO {
                codeRuleDataTypeid: number,
                codeRuleDataTypeCode: string,
                codeRuleDataTypeDescription: string
              }
              export interface WorkStatusDTO {
                workStatusId: 0,
                workStatusCode: string,
                workStatusDescription: string
              }
              export interface CodeRuleDataListDTO {
                dataListId: 0,
                dataListValue: string
              }     

        