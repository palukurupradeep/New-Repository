package com.shawclaimx.ui.smartcodeadmin.tests;

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
import com.shawclaimx.ui.smartcodeadmin.pages.AddResolutionPage;
import com.shawclaimx.ui.smartcodeadmin.pages.UpdateResolutionPage;

/**
 * @author Aravindan Sivanandan
 * @date_created on 07/18/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/18/2024
 * 
 */

public class UpdateResolutionTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public UpdateResolutionPage updateresolutionpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyUpdateResolutionItemPage() throws InterruptedException {
		System.out.println("Update resolution Functionality details...");
		basePage = new BasePage(driver);
		updateresolutionpage = new UpdateResolutionPage(driver);
		updateresolutionpage.updateResolutionPage();
		Reporter.log(" Update resolution item  Functionality Test Passed Successfully") ;
		

	}

}