package com.shawclaimx.ui.addtasknotes.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claimdetails.pages.ClaimDetailsPage;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.dashboard.pages.Dashboard_Redirection_Page;
import com.shawclaimx.ui.notetemplate.pages.AddNoteTemplatePage;
import com.shawclaimx.ui.permission.pages.AddRolePage;
import com.shawclaimx.ui.tasks.pages.AddTaskNotesPage;
import com.shawclaimx.ui.workqueue.pages.WorkQueuePage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 07/10/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/10/2024
 * 
 */
public class PendTaskTest extends TestBaseSetup {
	
	private WebDriver driver;
	public BasePage basePage;
	public AddNoteTemplatePage addnewtemplatepage;
	public CustomerPage customerpage;
	public ClaimDetailsPage claimdetailspage;
	public AddTaskNotesPage addtasknotespage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyAddTaskNotesPage() throws InterruptedException {
		System.out.println("Pend task WorkFlow Functionality details...");
		basePage = new BasePage(driver);
		basePage = new BasePage(driver);
		customerpage = new CustomerPage(driver);
		claimdetailspage = new ClaimDetailsPage(driver);
		addtasknotespage = new AddTaskNotesPage(driver);
		Thread.sleep(6000);
		customerpage.searchBarCustomerPage();
		customerpage.selectReasonCode();
		customerpage.continueInitiation();
		//customerpage.continueInitiationPrior();
		customerpage.verifyEnterClaimNotesPage();
		customerpage.InitiateClaim();
		claimdetailspage.worlClaimButton();
		Thread.sleep(2000);
		addtasknotespage.AddTaskNotesPageFlow();
		addtasknotespage.PendTaskNotesPageFlow();
		
	}
}
		
		
		