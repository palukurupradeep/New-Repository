package com.shawclaimx.ui.notetemplate.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.dashboard.pages.Dashboard_Redirection_Page;
import com.shawclaimx.ui.notetemplate.pages.AddNoteTemplatePage;
import com.shawclaimx.ui.permission.pages.AddRolePage;
import com.shawclaimx.ui.workqueue.pages.WorkQueuePage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 07/01/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/02/2024
 * 
 */
public class AddNewNoteTemplateTest extends TestBaseSetup {
	
	private WebDriver driver;
	public BasePage basePage;
	public AddNoteTemplatePage addnewtemplatepage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyNoteTemplatePage() throws InterruptedException {
		System.out.println("Redirecting to Add new note template  WorkFlow Functionality details...");
		basePage = new BasePage(driver);
		addnewtemplatepage = new AddNoteTemplatePage(driver);
	    addnewtemplatepage.AddNoteTemmplateFlow();
	    addnewtemplatepage.enterNoteType();
		
		Reporter.log("Add new note template  happy path automation test passed successfully");
		
	}
}
		
		
		