package com.shawclaimx.ui.claimcodemanagement.tests;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
//import org.testng.Assert;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.shawclaimx.base.TestBaseSetup;
import com.shawclaimx.ui.claimcodemanagement.pages.ClaimCodeManagementNegativePathPage;
import com.shawclaimx.ui.claimcodemanagement.pages.ClaimCodeManagementPage;
import com.shawclaimx.ui.claimdetails.pages.ClaimDetailsPage;
import com.shawclaimx.ui.claiminitiation.pages.BasePage;
import com.shawclaimx.ui.claiminitiation.pages.CustomerPage;

public class ClaimCodeManagementNegativePathTest extends TestBaseSetup {
	private WebDriver driver;

	public BasePage basePage;
	public CustomerPage customerpage;
	public ClaimCodeManagementNegativePathPage claimcodemanagementnegativepathpage;
	public ClaimCodeManagementPage claimcodemanagementpage;

	@BeforeClass
	public void setUp() {
		driver = getDriver();
	}

	@Test(priority = 1, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementReasoncodevalueempty() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management-Reason code Definition Page");
		basePage = new BasePage(driver);

		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);
		Thread.sleep(5000);
		claimcodemanagementnegativepathpage.searchClaimReasonCodePageempty();
	}

	@Test(priority = 2, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementNegativePathReasoncodevalue() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management-Reason code Definition Page");
		basePage = new BasePage(driver);

		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);
		Thread.sleep(6000);
		claimcodemanagementnegativepathpage.searchClaimReasonCodePage("A03");
		claimcodemanagementnegativepathpage.selectClaimCategory();
		claimcodemanagementnegativepathpage.selectVisibilityScope();
		claimcodemanagementnegativepathpage.selectType();
		claimcodemanagementnegativepathpage.toggleAllowanceServiceReturn();
		claimcodemanagementnegativepathpage.selectReturnType();
		claimcodemanagementnegativepathpage.toggleDistributionCompliance();
		System.out.println("Reason code definition negative path is passed ");

	}

	@Test(priority = 3, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementPostivepathReasoncodevalue() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management-Reason code Definition Page");
		basePage = new BasePage(driver);

		claimcodemanagementpage = new ClaimCodeManagementPage(driver);
		Thread.sleep(6000);

		claimcodemanagementnegativepathpage.searchClaimReasonCodePage("A09");

		claimcodemanagementpage.selectClaimCategory();
		claimcodemanagementpage.selectVisibilityScope();
		claimcodemanagementpage.selectType();
		claimcodemanagementpage.toggleAllowanceServiceReturn();
		claimcodemanagementpage.selectReturnType();
		claimcodemanagementpage.toggleDistributionCompliance();
		System.out.println("Reason code is added ");

	}

	@Test(priority = 4, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementAddRuleempty() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management-Add new rule Page");
		basePage = new BasePage(driver);

		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);

		Thread.sleep(6000);
		claimcodemanagementnegativepathpage.addNewRuleempty();
		System.out.println("Add new rule negative path is passed ");

	}

	@Test(priority = 5, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementNegativePathPageaddrule() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management-Add new rule Page");
		basePage = new BasePage(driver);

		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);

		Thread.sleep(6000);
		claimcodemanagementnegativepathpage.addNewRule();
		System.out.println("Add new rule negative path is passed ");
		Thread.sleep(6000);
		claimcodemanagementpage.addNewRule();
		System.out.println("Add new rule is added ");

	}

	@Test(priority = 6, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementeditaddrule() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management-Add new rule Page");
		basePage = new BasePage(driver);

		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);

		Thread.sleep(6000);
		claimcodemanagementnegativepathpage.editNewRule();

		System.out.println(" Rules is edited ");

	}

@Test(priority = 7, groups = { "Functional Testing" })
	public void verifyClaimCodeManagementNegativePathPossibleresolution() throws InterruptedException {
		System.out.println("Redirecting to Claim Code Management -Possible solution.");
		basePage = new BasePage(driver);

		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);

//		Thread.sleep(6000);
//		claimcodemanagementnegativepathpage.selectPossibleResolution();
//		System.out.println("Add new possible resolution negative path is passed ");
	Thread.sleep(6000);
		claimcodemanagementnegativepathpage.selectPossibleResolutionsave();
		System.out.println("Add newpossible resolution is added ");

	}
//
//	@Test(priority = 8, groups = { "Functional Testing" })
//	public void verifyClaimCodeManagementNegativePathPhotoresolution() throws InterruptedException {
//		System.out.println("Redirecting to Claim Code Management -Photos Resolution.");
//	basePage = new BasePage(driver);
//
//		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);
//
//		Thread.sleep(6000);
//		claimcodemanagementnegativepathpage.selectAssociatePhotos();
//		System.out.println("Add new photo resolution negative path is passed ");
//		Thread.sleep(6000);
//		claimcodemanagementnegativepathpage.selectAssociatePhotossave();
//		System.out.println("Add new photo resolution is added ");
//
//	}
//	@Test(priority = 9, groups = { "Functional Testing" })
//	public void verifyClaimCodeManagementNegativePathdelete() throws InterruptedException {
//		System.out.println("Redirecting to Claim Code Management -Delete Resolution.");
//		basePage = new BasePage(driver);
//
//		claimcodemanagementnegativepathpage = new ClaimCodeManagementNegativePathPage(driver);
//
//		Thread.sleep(6000);
//		claimcodemanagementnegativepathpage.deletefunctionalty();
//	}
//	

}