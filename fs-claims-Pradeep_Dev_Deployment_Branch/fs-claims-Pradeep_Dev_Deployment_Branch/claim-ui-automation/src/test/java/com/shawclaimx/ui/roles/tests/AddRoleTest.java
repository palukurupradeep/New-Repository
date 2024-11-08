package com.shawclaimx.ui.roles.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;

import com.shawclaimx.ui.permission.pages.AddRolePage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class AddRoleTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public AddRolePage addrolepage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyRolePage() throws InterruptedException {
		System.out.println("Rolepage Functionality details...");
		basePage = new BasePage(driver);
		addrolepage = new AddRolePage(driver);
		addrolepage.addRoleButton();
		addrolepage.addRoleCode();
		addrolepage.roleDescription();
		addrolepage.nextButton();
		addrolepage.addPermissionRole();
		addrolepage.addUserrole();
		addrolepage.saveButton();
		addrolepage.roleList();
		addrolepage.rolecodeText();
		addrolepage.editRole();
		addrolepage.addusereditRole();
		Reporter.log("The Role Page functionality test Passed Successfully");

	}

	@AfterClass
	public void tearDown() {
		// Close the browser
		if (driver != null) {
			driver.quit();
		}
	}

}