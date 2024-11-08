/**
 * 
 */
package com.shawclaimx.ui.claims.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claimdetails.pages.ClaimDetailsPage;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.claiminitiation.pages.EndUserInformationPage;
import com.shawclaimx.ui.claiminitiation.pages.InvoicePage;
import com.shawclaimx.ui.claiminitiation.pages.StoreNumberPage;
import com.shawclaimx.ui.dashboard.pages.Dashboard_Redirection_Page;
import com.shawclaimx.ui.permission.pages.AddPermissionPage;
import com.shawclaimx.ui.permission.pages.AddRolePage;
import com.shawclaimx.ui.usermanagement.pages.UserListPage;
import com.shawclaimx.ui.workqueue.pages.WorkQueuePage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/24/2024
 * 
 */
public class ClaimsHappyPathTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public StoreNumberPage storenumberpage;
	public InvoicePage invoicenumberpage;
	public EndUserInformationPage enduserinformationpage;
	public ClaimDetailsPage claimdetailspage;
	public Dashboard_Redirection_Page dashboard_redirection_page;
	public UserListPage userlistpage;
	public WorkQueuePage workqueuepage;
	public AddRolePage addrolepage;
	public AddPermissionPage addpermissionpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyClaimInitiationTestPage() throws InterruptedException {
		System.out.println("ClaimInitiationTestWithCustomerNumber Functionality details...");
		basePage = new BasePage(driver);
		customerpage = new CustomerPage(driver);
		storenumberpage = new StoreNumberPage(driver);
		enduserinformationpage = new EndUserInformationPage(driver);
		invoicenumberpage = new InvoicePage(driver);
		claimdetailspage = new ClaimDetailsPage(driver);
		dashboard_redirection_page = new Dashboard_Redirection_Page(driver);
		userlistpage = new UserListPage(driver);
		workqueuepage = new WorkQueuePage(driver);
		addrolepage = new AddRolePage(driver);
		addpermissionpage = new AddPermissionPage(driver);
		customerpage.searchBarCustomerPage();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		customerpage.continueInitiationPrior();
		customerpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		customerpage.closeInitiateClaim();
		customerpage.returnInitiateClaimPage();
		storenumberpage.searchBarStoreNumber();
		storenumberpage.selectStoreNumber();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		customerpage.continueInitiationPrior();
		storenumberpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		customerpage.closeInitiateClaim();
		customerpage.returnInitiateClaimPage();
		customerpage.searchBarCustomerPage();
		invoicenumberpage.searchBarInvoiceNumberPage();
		invoicenumberpage.selectinvoiceNumber();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		customerpage.continueInitiationPrior();
		invoicenumberpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		customerpage.closeInitiateClaim();
		customerpage.returnInitiateClaimPage();
		enduserinformationpage.EndUserInformationButton();
		enduserinformationpage.addEditEndUserInformationButton();
		enduserinformationpage.enterFirstName();
		enduserinformationpage.enterMiddleName();
		enduserinformationpage.enterLastName();
		enduserinformationpage.enterCompanyName();
		enduserinformationpage.enterEmailAddress();
		enduserinformationpage.enterAddress1();
		enduserinformationpage.enterAddress2();
		enduserinformationpage.selectState();
		enduserinformationpage.enterCity();
		enduserinformationpage.enterCounty();
		enduserinformationpage.enterPostalCode();
		enduserinformationpage.selectPhoneBusiness();
		enduserinformationpage.selectPhoneHome();
		enduserinformationpage.selectPhoneCell();
		enduserinformationpage.selectAddButton();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		customerpage.continueInitiationPrior();
		enduserinformationpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		customerpage.closeInitiateClaim();
		customerpage.returnInitiateClaimPage();
		Reporter.log("Claim initiation Test through customer number Passed Successfully ");
		customerpage.searchBarCustomerPage();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		customerpage.continueInitiationPrior();
		customerpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		claimdetailspage.worlClaimButton();
		claimdetailspage.reassigningClaim();
		claimdetailspage.changingCustomerNumber();
		claimdetailspage.updateCustomerNumber();
		claimdetailspage.closeClaimNumber();
		claimdetailspage.reopenClaimNumber();
		customerpage.returnInitiateClaimPage();
		dashboard_redirection_page.dashboardButton();
		Reporter.log("dashboard page is again redirected successfully");
		dashboard_redirection_page.workHistoryButton();
		Reporter.log("Redirecting to WorkHistory page from dashboard page is passed successfully ");
		dashboard_redirection_page.workQueueButton();
		Reporter.log("Redirecting to WorkQueue page from dashboard page is passed successfully ");
		userlistpage.redirectUserPage();
		userlistpage.enterActiveDirectoryIDPage();
		userlistpage.activatingUser();
		userlistpage.deactivatingUser();
		userlistpage.editUserInformation();
		userlistpage.enterActiveDirectoryIDPage();
		userlistpage.clickSyncButton();
		userlistpage.enterActiveDirectoryIDPage();
		userlistpage.clickViewUserInformationButton();
		userlistpage.clickAddUserButton();
		userlistpage.enterActiveDirectoryID();
		userlistpage.clickSearchButton();
		userlistpage.selectuser();
		userlistpage.clickNextButton();
		userlistpage.addUserInformation();
		Reporter.log("The userlistpage functionality test Passed Successfully");
		workqueuepage.redirectingworkQueueFlow();
		workqueuepage.userGroups();
		workqueuepage.fieldChooser();
		workqueuepage.claimNumber();
		workqueuepage.claimNumberLink();
		Reporter.log("Workqueue happy path automation test passed successfully");
		addrolepage = new AddRolePage(driver);
		addrolepage.redirectRolePage();
		addrolepage.addRoleButton();
		addrolepage.addRoleCode();
		addrolepage.roleDescription();
		addrolepage.nextButton();
		addrolepage.addPermissionRole();
		addrolepage.addUserrole();
		addrolepage.saveButton();
		addrolepage.roleList();
		addrolepage.rolecodeText();
		addrolepage.editRole();
		addrolepage.addusereditRole();
		Reporter.log("The Role Page functionality test Passed Successfully");
		addpermissionpage.redirectPermissionPage();
		addpermissionpage.addPermissionButton();
		addpermissionpage.permissionCode();
		addpermissionpage.permissionDescription();
		addpermissionpage.nextButton();
		addpermissionpage.addPermission();
		addpermissionpage.saveButton();
		Reporter.log("The PermissionPage functionality test Passed Successfully");

		Reporter.log("Shawclaims overall functionaliies Test Passed Successfully ");

	}

	@AfterClass
	public void tearDown() {
		// Close the browser
		if (driver != null) {
			driver.quit();
		}
	}
}
