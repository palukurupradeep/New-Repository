package com.shawclaimx.ui.dashboard.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.dashboard.pages.Dashboard_Redirection_Page;
import com.shawclaimx.ui.permission.pages.AddRolePage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 03/11/2024
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class Dashboard_ReDirectionTest extends TestBaseSetup {

	private WebDriver driver;
	public BasePage basePage;
	public Dashboard_Redirection_Page dashboard_redirection_page;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyWorkHistoryWithDashboardPage() throws InterruptedException {
		System.out.println("Redirecting to WorkHistory Functionality details...");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.workHistoryButton();
		Reporter.log("Redirecting to WorkHistory page from dashboard page is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		Reporter.log("dashboard page is again redirected successfully");
		dashboard_redirection_page.workQueueButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	
	@Test(priority = 2,groups= {"Functional Testing"})
	public void openClaimFromDashboard() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using open claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.openClaimsButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through open claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 3,groups= {"Functional Testing"})
	public void agedClaimFromDashboard() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using aged claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.agedClaimsButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through agedClaims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 4,groups= {"Functional Testing"})
	public void priorityClaimFromDashboard() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using priority claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.priorityClaimsButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through priority claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 5,groups= {"Functional Testing"})
	public void debitClaimFromDashboard() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using debit claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.debitClaimsButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through debitClaim is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 6,groups= {"Functional Testing"})
	public void latActivityAgeClaimFromDashboard() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using lastActivityAge claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.lastActivityAgeClaimsButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through latActivityAge Claim is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 7,groups= {"Functional Testing"})
	public void completedreceivedButtonFromDashboard() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using completedreceived claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.completedreceivedButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through completedreceived claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	
	@Test(priority = 8,groups= {"Functional Testing"})
	public void InitiateNotWorkedFromWorkQueue() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using InitiateNotWorked claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.InitiateNotWorked();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through Initiate Not Worked is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	
	@Test(priority = 9,groups= {"Functional Testing"})
	public void ClaimPendedFromWorkQueue() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using ClaimPended claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.ClaimPended();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through ClaimPended claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	
	@Test(priority = 10,groups= {"Functional Testing"})
	public void ReOpenedByUnmergeRequestFromWorkQueue() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using ReOpenedByUnmergeRequest claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.ReOpenedByUnmergeRequest();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through ReOpenedByUnmergeRequest claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	
	@Test(priority = 11,groups= {"Functional Testing"})
	public void ReOpenedFromWorkQueue() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using ReOpened claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.ReOpened();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through ReOpened claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	
	@Test(priority = 12,groups= {"Functional Testing"})
	public void AssignedToISCFromWorkQueue() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using AssignedToISC claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.AssignedToISC();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through AssignedToISC claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 13,groups= {"Functional Testing"})
	public void RemovedFromISCFromWorkQueue() throws InterruptedException{
		System.out.println("Redirecting to WorkQueue page from dashboard page using RemovedFromISC claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.RemovedFromISC();
		Reporter.log("Redirecting to WorkQueue page from dashboard page through RemovedFromISC claims is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}

	@Test(priority = 14,groups= {"Functional Testing"})
	public void claimDetailByWatchList() throws InterruptedException{
		System.out.println("Redirecting to Claim Detail page from dashboard page using Watch List claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.byWatchList();
		Reporter.log("Redirecting to Claim Detail from dashboard page through Watch List is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 15,groups= {"Functional Testing"})
	public void cancelDeleteConfirmationPopup() throws InterruptedException{
		System.out.println("Redirecting to Claim Detail page from dashboard page using Watch List claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.cancelDeleteConfirmation();
		Reporter.log("Redirecting to Claim Detail from dashboard page through Watch List is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}
	@Test(priority = 16,groups= {"Functional Testing"})
	public void claimDetailByWatchListDelete() throws InterruptedException{
		System.out.println("Redirecting to Claim Detail page from dashboard page using Watch List claims");
		basePage = new BasePage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		dashboard_redirection_page.deleteClaimbyWatchList();
		Reporter.log("Redirecting to Claim Detail from dashboard page through Watch List is passed successfully ");
		dashboard_redirection_page.dashboardButton();
		
	}

//	@Test(priority = 17,groups= {"Functional Testing"})
//	public void cancelDeleteConfirmationbyWatchListCustomer() throws InterruptedException{
//		System.out.println("Redirecting to Claim Detail page from dashboard page using WatchListCustomer claims");
//		basePage = new BasePage(driver);
//		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
//		dashboard_redirection_page.cancelDeleteConfirmationbyWatchListCustomer();
//		Reporter.log("Redirecting to Claim Detail from dashboard page through WatchListCustomer is passed successfully ");
//		dashboard_redirection_page.dashboardButton();
//		
//	}
//	@Test(priority = 18,groups= {"Functional Testing"})
//	public void deleteClaimbyWatchListCustomer() throws InterruptedException{
//		System.out.println("Redirecting to Claim Detail page from dashboard page using WatchListCustomer claims");
//		basePage = new BasePage(driver);
//		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
//		dashboard_redirection_page.deleteClaimbyWatchListCustomer();
//		Reporter.log("Redirecting to Claim Detail from dashboard page through WatchListCustomer is passed successfully ");
//		dashboard_redirection_page.dashboardButton();
//		
//	}


}
