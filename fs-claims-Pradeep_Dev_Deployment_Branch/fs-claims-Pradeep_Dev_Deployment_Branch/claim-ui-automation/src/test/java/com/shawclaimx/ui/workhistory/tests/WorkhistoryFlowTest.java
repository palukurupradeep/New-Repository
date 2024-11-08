package com.shawclaimx.ui.workhistory.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.workhistory.WorkHistoryPage;


/**
 *  @author Anandajothi
 * @date_created on 05/14/2024
 * @modified by Anandajothi
 * @modified on 05/20/2024
 * 
 */
public class WorkhistoryFlowTest extends TestBaseSetup {
	
	private WebDriver driver;
	public BasePage basePage;
	public WorkHistoryPage workHistoryPage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyWorkHistoryPage() throws InterruptedException {
		System.out.println("Redirecting to WorkHistory Flow Functionality details...");
		basePage = new BasePage(driver);
		workHistoryPage = new WorkHistoryPage(driver);
		workHistoryPage.redirectingWorkHistoryFlow();
		workHistoryPage.userGroups();
		workHistoryPage.claimNumber();
		workHistoryPage.claimNumberLink();
		Reporter.log("Workhistory happy path automation test passed successfully");
		
	}
}
		
		
		