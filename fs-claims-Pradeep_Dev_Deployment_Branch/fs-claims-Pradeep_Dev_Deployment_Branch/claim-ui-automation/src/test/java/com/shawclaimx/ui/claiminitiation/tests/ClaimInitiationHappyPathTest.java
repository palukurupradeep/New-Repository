/**
 * 
 */
package com.shawclaimx.ui.claiminitiation.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.claiminitiation.pages.EndUserInformationPage;
import com.shawclaimx.ui.claiminitiation.pages.InvoicePage;
import com.shawclaimx.ui.claiminitiation.pages.StoreNumberPage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by  Aravindan Sivanandan
 * @modified on 03/24/2024
 * 
 */
public class ClaimInitiationHappyPathTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public StoreNumberPage storenumberpage;
	public InvoicePage invoicenumberpage;
	public EndUserInformationPage enduserinformationpage;
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
		storenumberpage.searchBarStoreNumber();
		storenumberpage.selectStoreNumber();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		//customerpage.continueInitiationPrior();
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
		customerpage.selectReasonCode();
		enduserinformationpage.EndUserInformationButton();		
		enduserinformationpage.EndUserInformationButtonConfirmation();
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
		customerpage.continueInitiation();	
		customerpage.continueInitiationPrior();
		enduserinformationpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		customerpage.closeInitiateClaim();
		customerpage.returnInitiateClaimPage();
		
		Reporter.log("Claim initiation Test  Passed Successfully ");

	}

	@AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
	

