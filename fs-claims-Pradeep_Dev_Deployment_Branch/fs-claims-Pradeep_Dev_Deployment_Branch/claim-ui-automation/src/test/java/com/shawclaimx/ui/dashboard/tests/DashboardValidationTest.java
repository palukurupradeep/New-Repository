package com.shawclaimx.ui.dashboard.tests;

import org.junit.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.shawclaimx.ui.claimdetails.pages.ClaimDetailsValidation;
import com.shawclaimx.ui.dashboard.pages.DashboardValidation;
import com.shawclaimx.ui.dataSetup.UtilitySetup;
import com.shawclaimx.ui.workqueue.pages.WorkQueueValidation;

public class DashboardValidationTest extends UtilitySetup {

	DashboardValidation dashboard;
	WorkQueueValidation workQueue;
	ClaimDetailsValidation claimDetails;

	@BeforeTest
	public void declareSheetName() {
		sheetName = "Sheet1";
	}

	@Test
	public void TC_001_SelectingUserGroup() throws Exception {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(" Claims (FS) ");
	}

	@Test(dataProvider = "getExcelData")
	public void TC_002_SelectingUser(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_003_ValidatingOpenClaimsCount(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int openClaimsCountInDashboard = dashboard.openClaimsCountInDashboard();
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(openClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_004_ValidatingAgedClaimsCount(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int openClaimsCountInDashboard = dashboard.agedClaimsCountInDashboard();
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(openClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_004_ValidatingAgedClaimsCountForRespectiveDays(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int agedClaimsCountInDashboard = dashboard.agedClaimsCountForRespectiveDaysInDashboard("10");
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(agedClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_005_ValidatingAgedClaimsCountWithDiffDays(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		dashboard.compareAgedClaimsWithdays("10");
	}

	@Test(dataProvider = "getExcelData")
	public void TC_006_ValidatingPriorityClaimsCount(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int priorityClaimsCountInDashboard = dashboard.priorityClaimsCountInDashboard();
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(priorityClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_007_ValidatingAgeClaimsCount(String groupName, String userName) throws Exception {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int lastActivityAgedClaimsCountInDashboard = dashboard.lastActivityAgeClaimsCountInDashboard();
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(lastActivityAgedClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_008_ValidatingLastActivityAgeClaimsCountForRespectiveDays(String groupName, String userName)
			throws Exception {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int agedClaimsCountInDashboard = dashboard.lastActivityAgeClaimsCountForRespectiveDaysInDashboard("3");
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(agedClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_008_ValidatingLastActivityAgeClaimsCountWithDiffDays(String groupName, String userName)
			throws Exception {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		dashboard.compareLastActivityAgeClaimsWithdays("5");
	}

	@Test(dataProvider = "getExcelData")
	public void TC_009_ValidatingCompletedVsReceived(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int completedVsReceivedInDashboard = dashboard.completedVsReceivedInDashboard();
		Assert.assertEquals(1,completedVsReceivedInDashboard);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_010_ValidatingDebitClaims(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int debitClaimsCountInDashboard = dashboard.debitClaimsCountInDashboard();
		int claimsCountInWorkQueue = workQueue.listOfClaimsInWorkQueue();
		Assert.assertEquals(debitClaimsCountInDashboard, claimsCountInWorkQueue);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_011_ValidationCountOfIndividualClaimByWorkStatus(String groupName, String userName)
			throws Throwable {
		dashboard = new DashboardValidation();
		workQueue = new WorkQueueValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		dashboard.countOfIndividualClaimsByStatus();
		workQueue.workStatusValidationWithDashboard(groupName, userName);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_012_ValidatingOpenClaimsByWorkStatus(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		int openClaimsCountInDashboardForWorkStatus = dashboard.openClaimsCountInDashboardForWorkStatus();
		int countOfOpenClaimsByWorkStatusIn2DChart = dashboard.countOfOpenClaimsByWorkStatusIn2DChart();
		Assert.assertEquals(openClaimsCountInDashboardForWorkStatus, countOfOpenClaimsByWorkStatusIn2DChart);
	}

	@Test
	public void TC_013_ValidatingUserName() {
		dashboard = new DashboardValidation();
		boolean userNameValidation = dashboard.userNameValidation();
		Assert.assertTrue(userNameValidation);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_014_ValidatingClaimNumberInWatchlistByClaims(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		claimDetails = new ClaimDetailsValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		String clickOnClaimNumberInWatchlistByClaims = dashboard.clickOnClaimNumberInWatchlistByClaims();
		String claimNumberInClaimDetailsPage = claimDetails.claimNumberInClaimDetailsPage();
		Assert.assertEquals(clickOnClaimNumberInWatchlistByClaims, claimNumberInClaimDetailsPage);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_014_ValidatingCustomerNumberInWatchlistByCustomer(String groupName, String userName)
			throws Throwable {
		dashboard = new DashboardValidation();
		claimDetails = new ClaimDetailsValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		dashboard.countOfCustomersInWatchlistByCustomer();
	}

	@Test(dataProvider = "getExcelData")
	public void TC_014_ValidatingClaimNumberInOverdue(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		claimDetails = new ClaimDetailsValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		String clickOnClaimNumberInOverdue = dashboard.clickOnClaimNumberInOverdue();
		String claimNumberInClaimDetailsPage = claimDetails.claimNumberInClaimDetailsPage();
		Assert.assertEquals(clickOnClaimNumberInOverdue, claimNumberInClaimDetailsPage);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_014_ValidatingClaimNumberInClaimsDue(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		claimDetails = new ClaimDetailsValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		String clickOnClaimNumberInClaimsDue = dashboard.clickOnClaimNumberInClaimsDue();
		String claimNumberInClaimDetailsPage = claimDetails.claimNumberInClaimDetailsPage();
		Assert.assertEquals(clickOnClaimNumberInClaimsDue, claimNumberInClaimDetailsPage);
	}

	@Test(dataProvider = "getExcelData")
	public void TC_015_ValidatingDeleteButtonInWatchlistByClaimsWidget(String groupName, String userName)
			throws Throwable {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		dashboard.deleteClaimsInWatchlistByClaimsWidget();
	}

	@Test(dataProvider = "getExcelData")
	public void TC_015_ValidatingDeleteButtonInWatchlistByCustomerWidget(String groupName, String userName)
			throws Throwable {
		dashboard = new DashboardValidation();
		dashboard.selectingUserGroupFromDropDown(groupName);
		dashboard.selectingUserFromDropDown(userName);
		dashboard.deleteClaimsInWatchlistByCustomerWidget();
	}

	@Test
	public void TC_016_ValidatingUserPreferenceWidgetsInDashboard() throws Throwable {
		dashboard = new DashboardValidation();// Debit Claims//Aged Claims//Last Activity Age
		String widgetName = "Priority Claims";// Completed vs Received//Open Claims//Priority Claims
		dashboard.disablingDesiredCheckboxInUserPreference(widgetName);
		int widgetStatus = dashboard.checkingWidgetsInDashboard(widgetName);
		Assert.assertEquals(0, widgetStatus);
	}

}
