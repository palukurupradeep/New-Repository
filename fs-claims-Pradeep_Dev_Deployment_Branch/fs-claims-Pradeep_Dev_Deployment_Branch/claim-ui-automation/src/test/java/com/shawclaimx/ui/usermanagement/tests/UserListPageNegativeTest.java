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
import com.shawclaimx.ui.usermanagement.pages.UserListNegativePage;
import com.shawclaimx.ui.usermanagement.pages.UserListPage;
/**
 * @author Aravindan Sivanandan
 * @date_created on 09/23/2024
 * @modified by Aravindan Sivanandan
 * @modified on 09/23/2024
 * 
 */
public class UserListPageNegativeTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public UserListNegativePage userlistnegativepage;
	public UserListPage userlistpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test (priority=1, groups={"Functional Testing"})
	public void verifyUserNegativeListPage() throws InterruptedException {
		System.out.println("UserNegativelistpage Functionality details...");
		basePage = new BasePage(driver);
		userlistnegativepage = new UserListNegativePage(driver);
		userlistnegativepage.enterIncorrectActiveDirectoryIDPage();
		userlistpage.enterActiveDirectoryIDPage();
		userlistnegativepage.editIncorrectUserInformation();
		userlistnegativepage.clickAddUserButton();
		userlistnegativepage.enterUsedActiveDirectoryID();
		userlistnegativepage.clickSearchButton();
		userlistnegativepage.selectuser();
		userlistnegativepage.clickNextButton();
		userlistnegativepage.enterInvalidActiveDirectoryIDPage();
		userlistnegativepage.enterValidActiveDirectoryID();
		userlistnegativepage.addIncorrectUserInformation();
		
		Reporter.log("The userlistpage negtive scenario functionality test Passed Successfully");
	
	}
	
	@AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
	}
	
}