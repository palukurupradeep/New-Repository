package com.shaw.claims.constant;

public class CommonConstant {
	
	public static final Integer CLAIM_DECLINED_ID = 5;
	public static final Integer CLAIM_PAUSE_ID = 3;
	public static final Integer CLAIM_VOID_ID = 4;
	public static final Integer CLAIM_REOPEN = 1;
	public static final Integer CLAIM_CLOSE = 2;
	public static final Integer CLAIM_DEFAULT_STATUS = 0;
	public static final int ACTIVE = 1;
	public static final Integer SYSTEM_USER_ID = 5;
	public static final String DETAIL_TYPE_CODE_LABOR = "LABOR";
	public static final String DETAIL_TYPE_CODE_SERVICE = "SERVC";
	public static final String DETAIL_TYPE_CODE_MERCH = "MERCH";

	/*work status */
	
	public static final int WORK_STATUS_SENDTOISC = 8;
	public static final int WORK_STATUS_REMOVEFROMISC = 9;
	public static final int WORK_STATUS_SAMPLE_REQUESTED = 13;
	public static final int WORK_STATUS_CLOSED_WITHOUT_CREDIT = 20;
	public static final int WORK_STATUS_REOPEN = 18;
	public static final int WORK_STATUS_PAUSE = 19;
	public static final int WORK_STATUS_VOID = 22;
	public static final int WORK_STATUS_DECLINE = 51; 
	public static final int CRM_APPROVAL_IN_PROCESS = 16;
	
	/* Action buttons */
	public static final String ACTION_DECLINE = "Decline";
	public static final String ACTION_CLOSE = "Close";
	public static final String ACTION_REQUEST_SAMPLE = "Request-Sample";
	public static final String ACTION_ASSIGNED_TO_PRICING = "AssignedToPricing";
	public static final String ACTION_REMOVE_FROM_PRICING= "RemovedFromPricing";
	public static final String ACTION_REOPEN = "Reopen";
	public static final String ACTION_PAUSE = "Pause";
	public static final String ACTION_VOID = "Void";
	public static final Integer DELETED_STATUS_ID = 3;
	public static final Integer INACTIVE = 2;
	
	/*notes and tasks*/
	public static final String NOTES = "Note";
	public static final String TASKS = "Task";
	public static final String INITIATION_NOTES = "INITIATION NOTES";
	public static final String SUBMITTAL_NOTES = "SUBMITTAL NOTES";
	public static final String PRIORITY_NOTE_TEMPLATE = "PRIORITY NOTE TEMPLATE";
	public static final String CLAIM_MERGE = "MERGE CLAIM NOTES";
	public static final String CLAIM_UNMERGE = "UNMERGE CLAIM NOTES";
	public static final String NOTE_TEMPLATE_CLAIM_MERGE = "claimMerge";
	public static final String NOTE_TEMPLATE_CLAIM_UN_MERGE = "claimUnMerge";
	public static final String NOTE_TEMPLATE_CLAIM_INITIATE = "INITNOTE";
	public static final String ASSIGNED_TO_PRICING_TEMPLATE = "ASSIGNED TO PRICING TEMPLATE";
	public static final String REMOVED_FROM_PRICING_TEMPLATE = "REMOVED FROM PRICING TEMPLATE";
	public static final String NOTE_TEMPLATE_CLAIM_SUBMITTAL = "DEALER";
	public static final String CLAIM_SUBMITTAL = "DEALER NOTE";
	public static final String CREDID_MEMO = "CREDIT MEMO"; 
	public static final String ISSUE_RGA_NOTE_TYPE_CODE = "APPROVE";
	public static final String ISSUE_RGA_NOTE_TEMPLATE_NAME = "RGA Template 1";
	public static String RULE_LEVEL_CLAIM = "ClaimLevel";
	public static String NOTE_TYPE_CODE = "OVERRIDE";
	public static String NOTE_TEMPLATE_NAME = "UNKN";

	/* Decision App */

	public static final String ACCOMODATION =  "Accomodation";
	public static final String QUALITY =  "Quality";
	public static final String CLAIM_LEVEL="ClaimLevel";

//	public static final String CLAIM_LEVEL="Claim Level";

	public static final  String CUSTOMER_REF_NO="Customer Ref No";
	public static final String END_USER_INFO="End User Information";
	public static final String SELLING_CMPNY="Selling Company";
	public static final String VENDOR_NAME="Vendor Name";
	public static final String VENDOR_NAME_SHAW="SHAW";
	public static final String PRODUCT_CODE="Product Code";
	public static final String ROUTED_FOR_REVIEW_NOTE_TEMPLATE ="ROUTED FOR REVIEW NOTE TEMPLATE";
	public static final String COUNTRY_CODE="Country Code";
	public static final String STATE = "State";
	public static final String PROVINCE = "Province";
	public static final String RGA = "RGA";
	public static final String RCS_CODE = "RCS Code";
	public static final String RCSCODE_SAMPLE = "S";
	public static final String REASON_CODE = "Reason Code";
	public static final String FREIGHT = "Freight";
	public static final String COMMERCIAL_CARPET = "Commercial Carpet";
	public static final String COMMERCIAL_HARD_SURFACE = "Commercial Hard Surface";
	public static final String RESIDENTIAL_CARPET = "Residential Carpet";
	public static final String RESIDENTIAL_HARD_SURFACE = "Residential Hard Surface";
	public static final String CARPET = "Carpet";
	public static final String HARD_SURFACE = "HardSurface";
	public static final String SOFT = "SOFT";
	public static final String HARD = "HARD";
	public static final String SHAW = "SHAW";
	
	public static final String DECLINE_STATUS_CODE = "D";
	
	public static final String CREDIT_MEMO_CODE = "CREDMEMO";
	public static final String SAMPLE_REQUESTED = "SAMPLE REQUESTED";
	public static final String SAMPLE_RECEIVED = "SAMPLE RECEIVED";
	public static final String SAMPLE_APPROVED = "SAMPLE APPROVED";
	public static final String SAMPLE_DECLINED = "SAMPLE DECLINED";

	// Country code  //
	public static final int UNITED_KINGDOM = 238;
	public static final int CANADA = 41;

	public static final String SEND_TO_LAB = "SEND TO LAB";
	public static final String APPROVAL_REQUIRED_CRM_TEMPLATE1 = "CRM Template 1";


	public static final int SAMPLE_REQUESTED_VALUE = 1;
	public static final int SAMPLE_RECEIVED_VALUE = 2;


	/* TRACE TYPE */
	public static final String ASSIGNED_TO_PRICING_CODE = "049";
	public static final String CLAIM_REASON_CODE_APPROVAL = "016";
	public static final String RETURN_AUTHORIZATION_ISSUED = "010";

	/* Claim Category */
	public static final String CLAIM_CATEGORY_CODE = "PRC";

	/* Approval Limit Type */
	public static final String RGA_APPROVAL_LIMIT_TYPE = "RETAUTH";

	/* USER GROUP CODE */
	public static final String RGA_USER_GROUP_CODE = "RDC";
	public static final String CLM_USER_GROUP_CODE = "CLM";
	/* WORK STATUS */
	public static final String RGA_WORK_STATUS_CODE = "016";

	/* LOOK UP */
	public static final String LOOKUP_CODE = "P";

	/* Area Type */
	public static final String HOME_DEPOT = "HOMEDEPOT";
	public static final String LOWES = "LOWES";
	public static final String LINE_LEVEL = "Line Level";

	/* Address Type */
	public static final String DEALER_ADDRESS_TYPE_CODE = "DLR";
	public static final String ACTION_REQUEST_FOR_INFORMATION = "RequestForInformation";
	public static final int WORK_STATUS_ACTION_REQUEST_FOR_INFORMATION = 47;


}
	