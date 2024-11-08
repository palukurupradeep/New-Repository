package com.shawclaimx.ui.roles.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.permission.pages.AddPermissionNegativeScript;


public class AddPermissionNegativeTest extends TestBaseSetup {

	private WebDriver driver;

	public BasePage basePage;
	public AddPermissionNegativeScript addpermissionnegativescript;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyPermissionPageempty() throws InterruptedException {

		System.out.println("Add Permission with empty details");

		basePage = new BasePage(driver);
		addpermissionnegativescript = new AddPermissionNegativeScript(driver);
        addpermissionnegativescript.addPermissionButton();
		addpermissionnegativescript.nextButtonempty();
	}

	@Test(priority = 2, groups = { "Functional Testing" })
	public void verifyPermissionadd() throws InterruptedException {

		System.out.println("Add Permission details");

		basePage = new BasePage(driver);
		addpermissionnegativescript = new AddPermissionNegativeScript(driver);
        addpermissionnegativescript.permissionCode();
		addpermissionnegativescript.permissionDescription();
		addpermissionnegativescript.nextButton();
		addpermissionnegativescript.addPermission();
		addpermissionnegativescript.saveButton();

	}

	@Test(priority = 3, groups = { "Functional Testing" })
	public void verifyduplicatePermission() throws InterruptedException {

		System.out.println("Add Permission with duplicate details");

		basePage = new BasePage(driver);
		addpermissionnegativescript = new AddPermissionNegativeScript(driver);
		addpermissionnegativescript.permissionCode();
		addpermissionnegativescript.permissionDescription();
		addpermissionnegativescript.nextButton();
		addpermissionnegativescript.duplicatepermission();
		addpermissionnegativescript.backbutton();
		Reporter.log("The PermissionPage functionality test Passed Successfully");

	}

	@AfterClass
	public void tearDown() {

		driver.quit();
	}

}
