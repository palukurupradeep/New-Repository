package com.shawclaimx.ui.claimdetails.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claimdetails.pages.ClaimDetailsPage;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.claiminitiation.pages.InvoicePage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 03/11/2024
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */

public class ClaimDetailsTest2 extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public InvoicePage invoicenumberpage;
	public ClaimDetailsPage claimdetailspage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyClaimDetailsActionLevelIconsTest() throws InterruptedException {
		System.out.println("ClaimDetailsTestWithCustomerNumber Functionality details...");
		basePage = new BasePage(driver);
		customerpage = new CustomerPage(driver);
		invoicenumberpage = new InvoicePage(driver);
		claimdetailspage = new ClaimDetailsPage(driver);
		Thread.sleep(6000);
		customerpage.searchBarCustomerPage();
		invoicenumberpage.searchBarInvoiceNumberPage();
		invoicenumberpage.selectinvoiceNumber();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		customerpage.continueInitiationPrior();
		invoicenumberpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		claimdetailspage.worlClaimButton();	
		claimdetailspage.changingCustomerNumber();
		claimdetailspage.updateCustomerNumber();
		claimdetailspage.mergeClaimNumber();
		claimdetailspage.pricingClaimNumber();
		claimdetailspage.removePricingClaimNumber();
		claimdetailspage.sampleRequest();
		claimdetailspage.closeClaimNumber();
		claimdetailspage.reopenClaimNumber();
		claimdetailspage.declineClaimNumber();
		customerpage.returnInitiateClaimPage();
		Reporter.log(" Claim details Part 2 Functionality Test Passed Successfully") ;
		

	}

	@AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}