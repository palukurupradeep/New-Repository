package com.shawclaimx.ui.claimcodemanagement.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claimcodemanagement.pages.ClaimCodeManagementPage;
import com.shawclaimx.ui.claimdetails.pages.ClaimDetailsPage;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 06/07/2024
 * @modified by Aravindan Sivanandan
 * @modified on 06/10/2024
 * 
 */

public class ClaimCodeManagementTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public ClaimCodeManagementPage claimcodemanagementpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementPage() throws InterruptedException {
		System.out.println("ClaimDetailsTestWithCustomerNumber Functionality details...");
		basePage = new BasePage(driver);
		customerpage = new CustomerPage(driver);
		claimcodemanagementpage = new ClaimCodeManagementPage(driver);
		Thread.sleep(6000);
		claimcodemanagementpage.searchClaimReasonCodePage();
		claimcodemanagementpage.selectClaimCategory();
		claimcodemanagementpage.selectVisibilityScope();
		claimcodemanagementpage.selectType();
		claimcodemanagementpage.toggleAllowanceServiceReturn();
		claimcodemanagementpage.selectReturnType();
		claimcodemanagementpage.toggleDistributionCompliance();
		claimcodemanagementpage.addNewRule();
		claimcodemanagementpage.selectPossibleResolution();
		claimcodemanagementpage.selectAssociatePhotos();
		Reporter.log(" Claim details Functionality Test Passed Successfully") ;
		

	}

	@AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}