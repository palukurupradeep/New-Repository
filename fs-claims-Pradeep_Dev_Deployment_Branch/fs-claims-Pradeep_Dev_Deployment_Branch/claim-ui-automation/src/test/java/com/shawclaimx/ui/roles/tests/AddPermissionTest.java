package com.shawclaimx.ui.roles.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.permission.pages.AddPermissionPage;
import com.shawclaimx.ui.usermanagement.pages.UserListPage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class AddPermissionTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public AddPermissionPage addpermissionpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test (priority=1, groups={"Functional Testing"})
	public void verifyPermissionPage() throws InterruptedException {
		System.out.println("AddPermission Functionality details...");
		basePage = new BasePage(driver);
		addpermissionpage = new AddPermissionPage(driver);
		addpermissionpage.addPermissionButton();
		addpermissionpage.permissionCode();
		addpermissionpage.permissionDescription();
		addpermissionpage.nextButton();
		addpermissionpage.addPermission();
		addpermissionpage.saveButton();
		Reporter.log("The PermissionPage functionality test Passed Successfully");
	
	}
	
	@AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
	}
	
}