package com.shawclaimx.ui.workqueue.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.dashboard.pages.Dashboard_Redirection_Page;
import com.shawclaimx.ui.permission.pages.AddRolePage;
import com.shawclaimx.ui.workqueue.pages.WorkQueuePage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 03/14/2024
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class WorkQueueFlowTest extends TestBaseSetup {
	
	private WebDriver driver;
	public BasePage basePage;
	public WorkQueuePage workqueuepage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyWorkQueuePage() throws InterruptedException {
		System.out.println("Redirecting to WorkQueue Flow Functionality details...");
		basePage = new BasePage(driver);
		workqueuepage = new WorkQueuePage(driver);
		workqueuepage.redirectingworkQueueFlow();
		workqueuepage.userGroups();
		workqueuepage.fieldChooser();
		workqueuepage.claimNumber();
		workqueuepage.claimNumberLink();
		Reporter.log("Workqueue happy path automation test passed successfully");
		
	}
}
		
		
		