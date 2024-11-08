package com.shawclaimx.ui.usermanagement.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;
import com.shawclaimx.ui.usermanagement.pages.UserListPage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class UserListPageTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public UserListPage userlistpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test (priority=1, groups={"Functional Testing"})
	public void verifyUserListPage() throws InterruptedException {
		System.out.println("Userlistpage Functionality details...");
		basePage = new BasePage(driver);
		userlistpage = new UserListPage(driver);
		userlistpage.enterActiveDirectoryIDPage();
		userlistpage.activatingUser();
		userlistpage.deactivatingUser();
		userlistpage.editUserInformation();	
		userlistpage.enterActiveDirectoryIDPage();
		userlistpage.clickSyncButton();
		userlistpage.enterActiveDirectoryIDPage();;
		userlistpage.clickViewUserInformationButton();
		userlistpage.clickAddUserButton();
		userlistpage.enterActiveDirectoryID();
		userlistpage.clickSearchButton();
		userlistpage.selectuser();
		userlistpage.clickNextButton();
		userlistpage.addUserInformation();		
		Reporter.log("The userlistpage functionality test Passed Successfully");
	
	}
	
	@AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
	}
	
}