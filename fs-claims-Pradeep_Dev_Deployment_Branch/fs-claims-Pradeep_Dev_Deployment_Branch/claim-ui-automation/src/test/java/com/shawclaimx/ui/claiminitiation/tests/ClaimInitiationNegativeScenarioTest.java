/**
 * 
 */
package com.shawclaimx.ui.claiminitiation.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.ClaimInitiationNegativePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.claiminitiation.pages.EndUserInformationPage;
import com.shawclaimx.ui.claiminitiation.pages.InvoicePage;
import com.shawclaimx.ui.claiminitiation.pages.StoreNumberPage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 08/09/2024
 * @modified by Aravindan Sivanandan
 * @modified on 08/09/2024
 * 
 */
public class ClaimInitiationNegativeScenarioTest extends TestBaseSetup {
	private WebDriver driver;

	public ClaimInitiationNegativePage claiminitiationnegativepage;
	public BasePage basePage;

	@BeforeClass
	public void Setup() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifysearchClaimInitiationNegativePageTestPage() throws InterruptedException {

		System.out.println("verifysClaimInitiationNegativeCaseTestPage Functionality details...");
		basePage = new BasePage(driver);
		claiminitiationnegativepage = new ClaimInitiationNegativePage(driver);
		Thread.sleep(4000);
		claiminitiationnegativepage.searchEmptyCustomerNumber_NegativeCase();
		Thread.sleep(4000);
		claiminitiationnegativepage.searchInvalidCustomerNumber_NegativeCase();
		Thread.sleep(4000);
		claiminitiationnegativepage.searchInvalidStoreNumber_NegativeCase();
		Thread.sleep(4000);
		claiminitiationnegativepage.continueInitiationValidation_NegativeCase();

	}

	@AfterClass
	public void tearDown() {
		// Close the browser
		if (driver != null) {
			driver.quit();
		}
	}

}
