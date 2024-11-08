package com.shawclaimx.ui.claimcodemanagement.pages;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClaimCodeManagementNegativePathPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(ClaimCodeManagementPage.class);

	public ClaimCodeManagementNegativePathPage(WebDriver driver) {
		this.driver = driver;
	}

	public void searchClaimReasonCodePageempty() throws InterruptedException {
		
	
		Thread.sleep(7000);
		driver.findElement(By.xpath("//input[@formcontrolname='claimSearch']")).clear();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@formcontrolname='claimSearch']")).sendKeys("AO1");
		driver.findElement(By.xpath("//input[@formcontrolname='claimSearch']")).clear();
		WebElement elementreasoncodeemptys = driver.findElement(By.xpath("(//button[contains(text(),'Save & Continue')])[1]"));

		boolean actualreasonValueemptys = elementreasoncodeemptys.isEnabled();


		if (actualreasonValueemptys)
			System.out.println("Button is enabled");
		else
			System.out.println("Button is disabled-Empty Reason code defintio page passed.");
	}
	
	// Method to search the number in the reasoncodeTextbox
	public void searchClaimReasonCodePage(String claimReasonCode) throws InterruptedException {
		Thread.sleep(7000);
		driver.findElement(By.xpath("//input[@formcontrolname='claimSearch']")).clear();
		driver.findElement(By.xpath("//input[@formcontrolname='claimSearch']")).sendKeys(claimReasonCode);
		Thread.sleep(3000);
		WebElement searchbutton = driver.findElement(By.xpath("//span[@class='fa fa-search']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", searchbutton);
		Assert.assertTrue(true, "reasoncode information not loaded Successfully for the selected reasoncode");
		Reporter.log("reasoncode information loaded Successfully for the selected reasoncode");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='returnEligible']")).click();
	}

	// Method to select claimcategory in the claimcode management page
	public void selectClaimCategory() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@role='combobox'])[2]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' ACCOMMODATION ')]")).click();
		Assert.assertTrue(true, "claim category value value is not selected successfully for the selected reasoncode");
		Reporter.log(" claim category  value is  selected successfully for the selected reasoncode");

	}

	// Method to select visibility scope in the claimcode management page
	public void selectVisibilityScope() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@role='combobox'])[3]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' Both')][1]")).click();
		Assert.assertTrue(true,
				"visibility scope value value is not selected successfully for the selected reasoncode");
		Reporter.log(" visibility scope value is  selected successfully for the selected reasoncode");

	}

	// Method to select type in the claimcode management page
	public void selectType() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@role='combobox'])[4]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' Admin')]")).click();
		Assert.assertTrue(true, "Type value value is not selected successfully for the selected reasoncode");
		Reporter.log(" type value is  selected successfully for the selected reasoncode");

	}

	// Toggle Allowance to keep & Service to keep & return eligible
	public void toggleAllowanceServiceReturn() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='allowanceToKeep']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='serviceTokeep']")).click();
		Assert.assertTrue(true, " Allowance to keep  & Service to keep toggle not selected successfully");
		Reporter.log("Allowance to keep  & Service to keep toggle selected successfully");

	}

	// Method to select return type dropdown value in the claimcode management page
	public void selectReturnType() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@role='combobox'])[5]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' Accomodation')]")).click();
		Assert.assertTrue(true, "Return Type value value is not selected successfully for the selected reasoncode");
		Reporter.log(" Return type value is  selected successfully for the selected reasoncode");

	}

	// toggle distribution compliance button
	public void toggleDistributionCompliance() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='distributionCompliance']")).click();
		Assert.assertTrue(true, " distribution compliancee toggle not selected successfully");
		Reporter.log("distribution compliance toggle selected successfully");
		Thread.sleep(2000);
		WebElement elementsave = driver.findElement(By.xpath("(//button[contains(text(),'Save & Close')])[1]"));

		boolean actualValue = elementsave.isEnabled();

	if (actualValue)
			System.out.println("Button is enabled ");
		else
			System.out.println("Button is disabled. Enter the All the values in reason code defintion page");

	}
	
	
	// Select add new rule in the rule definition tab with empty value
		public void addNewRuleempty() throws InterruptedException {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//button[contains(text(),'Add New Rule')])")).click();
			WebElement addrulesempty = driver.findElement(By.xpath("(//button[contains(text(),'Save & Continue')])[2]"));
			boolean actualValueempty = addrulesempty.isEnabled();

			if (actualValueempty)
				System.out.println("Button is enabled");
			else
				System.out.println("Button is disabled , Enter all values in add rule screen.");
			Thread.sleep(2000);
			driver.findElement(By.xpath("//button[text()='Cancel']")).click();
		}
		
	// Select add new rule in the rule definition tab
	public void addNewRule() throws InterruptedException {
		Thread.sleep(7000);
		driver.findElement(By.xpath("(//button[contains(text(),'Add New Rule')])")).click();
		Thread.sleep(4000);

		driver.findElement(By.xpath("//textarea[@formcontrolname='actionDescription']"))
				.sendKeys("Automation_action_description");
		Assert.assertTrue(true, "actionDescription  text not entered successfully ");
		Reporter.log("actionDescription text entered successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//textarea[@formcontrolname='sucessMessage']"))
				.sendKeys("Automation_Sucesss_Rule_Definition");
		Assert.assertTrue(true, "sucessMessage text not entered successfully ");
		Reporter.log("sucessMessage text entered successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='allowOverride']")).click();
		Assert.assertTrue(true, " allowOverride toggle not selected successfully");
		Reporter.log("allowOverride toggle selected successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='updateworkStatus']")).click();
		Assert.assertTrue(true, " updateworkStatustoggle not selected successfully");
		Reporter.log("updateworkStatus toggle selected successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//textarea[@formcontrolname='workStatusRemarks']"))
				.sendKeys("Automation_WorkStatus_Testing");
		Assert.assertTrue(true, "workStatusRemarks text not entered successfully ");
		Reporter.log("workStatusRemarks text entered successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='approvalRequired']")).click();
		Assert.assertTrue(true, " approvalRequired toggle not selected successfully");
		Reporter.log("approvalRequired toggle selected successfully");
		Thread.sleep(2000);
		WebElement addrulesave = driver.findElement(By.xpath("(//button[contains(text(),'Save & Continue')])[2]"));
		boolean actualValueone = addrulesave.isEnabled();

		if (actualValueone)
			System.out.println("Button is enabled");
		else
			System.out.println("Button is disabled , Enter all values in add rule screen.");
		driver.findElement(By.xpath("//button[text()='Cancel']")).click();
		//button[contains(text(),'Back')]

	}
	
	public void editNewRule() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//button[contains(text(),'Back')])[2]")).click();
		Thread.sleep(7000);
		driver.findElement(By.xpath("(//*[@class='fa-solid fa-pencil'])[5]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//textarea[@formcontrolname='ruleDescription']")).clear();
		WebElement editrulesave = driver.findElement(By.xpath("(//button[contains(text(),'Save & Continue')])[2]"));
		boolean actualValueedit = editrulesave.isEnabled();

		if (actualValueedit)
			System.out.println("Button is enabled");
		else
			System.out.println("Button is disabled , Enter all values in add rule screen.");
		Thread.sleep(4000);
		driver.findElement(By.xpath("//textarea[@formcontrolname='ruleDescription']")).clear();
	Thread.sleep(2000);
	driver.findElement(By.xpath("//textarea[@formcontrolname='ruleDescription']")).sendKeys("Editing Automation_ClaimcodeManagement_Testing");
	Thread.sleep(4000);
driver.findElement(By.xpath("(//button[contains(text(),'Save & Close')])[2]")).click();
Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Skip')])[1]")).click();
	}
	
	// Select possible resolution tab in the rule definition tab
		public void selectPossibleResolution() throws InterruptedException {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//button[contains(text(),' Associate Resolution')])[1]")).click();
			Thread.sleep(4000);
//			driver.findElement(By.xpath("(//tbody//tr[2]//td[1]//input[@type='checkbox'])[2]")).click();
//			Assert.assertTrue(true, "PossibleResolution CHECKBOX is not clicked successfully");
//			Reporter.log("PossibleResolution CHECKBOX selected successfully");
//			Thread.sleep(2000);
			WebElement possibleresolutionsave = driver.findElement(By.xpath("(//button[text()=' Save '])[2]"));
			boolean actualValuesavebtn = possibleresolutionsave.isEnabled();

			if (actualValuesavebtn)
				System.out.println("Button is enabled");
			else
				System.out.println(" Associate Resolution negative is passed");
			//driver.findElement(By.xpath("//button[text()='Cancel']"));
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//button[@class='btn-close'])[7]")).click();
			System.out.println("Associate Resolutions Negative path is passed");

		}
		public void selectPossibleResolutionsave() throws InterruptedException {
			Thread.sleep(4000);
//			driver.findElement(By.xpath("(//button[contains(text(),' Associate Resolution')])[1]")).click();
//			Thread.sleep(4000);
//			driver.findElement(By.xpath("(//tbody//tr[2]//td[1]//input[@type='checkbox'])[2]")).click();
//			Assert.assertTrue(true, "PossibleResolution CHECKBOX is not clicked successfully");
//			Reporter.log("PossibleResolution CHECKBOX selected successfully");
//			Thread.sleep(2000);
//			driver.findElement(By.xpath("(//button[text()=' Save '])[2]")).click();
			//System.out.println("Associate Resolutions is passed");
			Thread.sleep(4000);
			driver.findElement(By.xpath("//button[contains(text(),'Skip/Next')]")).click();
			Thread.sleep(3000);
		}
		public void selectAssociatePhotos() throws InterruptedException {
//			Thread.sleep(4000);
//			driver.findElement(By.xpath("//button[contains(text(),'Skip/Next')]")).click();
//			Thread.sleep(3000);
			
			driver.findElement(By.xpath("(//button[contains(text(),'Associate Photos')])[1]")).click();
			Thread.sleep(4000);
			WebElement photosresolutionsave = driver.findElement(By.xpath("(//button[text()=' Save '])[1]"));
			boolean actualValuephoto = photosresolutionsave.isEnabled();

			if (actualValuephoto)
				System.out.println("Button is disabled");
			else
				System.out.println("Button is enabled");
			//driver.findElement(By.xpath("//button[text()='Cancel']"));
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//button[@class='btn-close'])[1]")).click();
			System.out.println("Photos Resolutions Negative path is passed");

//			driver.findElement(By.xpath("(//button[contains(text(),'Save')])[5]")).click();
		}
		public void selectAssociatePhotossave() throws InterruptedException {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//button[contains(text(),'Associate Photos')])[1]")).click();
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//tbody//tr[2]//td[1]//input[@type='checkbox'])[1]")).click();
			Assert.assertTrue(true, "AssociatePhoto CHECKBOX is not clicked successfully");
			Reporter.log("AssociatePhoto CHECKBOX selected successfully");
			Thread.sleep(2000);
			driver.findElement(By.xpath("(//button[text()=' Save '])[1]")).click();
		}
		public void deletefunctionalty() throws InterruptedException {

			Thread.sleep(4000);
		//driver.findElement(By.xpath("(//img[contains(@src, 'assets/images/ic_delete.svg')])[5]"));
			
			

		driver.findElement(By.xpath("(//table[@class='table table-striped table-hover table-sorting'])[2]//tr[7]//td[1]//img[contains(@src, 'assets/images/ic_delete.svg')]")).click();
			Thread.sleep(4000);
	driver.findElement(By.xpath("//button[text()=' Confirm ']")).click();	
	Thread.sleep(4000);
	driver.findElement(By.xpath("//button[text()='Back/Reset']")).click();
	System.out.println("Photo is deleted");
	Thread.sleep(1000);
	driver.findElement(By.xpath("(//table[@class='table table-striped table-hover table-sorting table-resolution-new'])//tr[7]//td[1]//img[contains(@src, 'assets/images/ic_delete.svg')]")).click();
	Thread.sleep(2000);
	driver.findElement(By.xpath("(//button[text()=' Delete '])[1]")).click();	
	Thread.sleep(4000);
driver.findElement(By.xpath("(//button[text()='Back'])[2]")).click();	
System.out.println("Resoutions  is deleted");
Thread.sleep(4000);
driver.findElement(By.xpath("(//*[@class='fa-solid fa-trash-can'])[5]")).click();
Thread.sleep(4000);
driver.findElement(By.xpath("(//button[text()=' Delete '])[2]")).click();

Thread.sleep(4000);
driver.findElement(By.xpath("(//button[text()='Back'])[1]")).click();
Thread.sleep(4000);
driver.findElement(By.xpath("//button[text()='Save & Close']")).click();
System.out.println("Rule is deleted");
Thread.sleep(4000);
}
 

}
