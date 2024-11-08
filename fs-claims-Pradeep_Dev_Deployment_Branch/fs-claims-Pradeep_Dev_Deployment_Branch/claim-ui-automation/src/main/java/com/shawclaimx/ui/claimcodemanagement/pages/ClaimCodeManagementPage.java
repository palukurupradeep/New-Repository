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

/**
 * @author Aravindan Sivanandan
 * @date_created on 06/08/2024
 * @modified by Aravindan Sivanandan
 * @modified on 06/10/2024
 * 
 */

public class ClaimCodeManagementPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(ClaimCodeManagementPage.class);

	public ClaimCodeManagementPage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to search the number in the reasoncodeTextbox
	public void searchClaimReasonCodePage() throws InterruptedException {
		Thread.sleep(6000);
		driver.findElement(By.xpath("//input[@formcontrolname='claimSearch']")).sendKeys("A09");
		Thread.sleep(3000);
		WebElement searchbutton = driver.findElement(By.xpath("//span[@class='fa fa-search']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", searchbutton);
		Assert.assertTrue(true, "reasoncode information not loaded Successfully for the selected reasoncode");
		Reporter.log("reasoncode information loaded Successfully for the selected reasoncode");

	}

	// Method to select claimcategory in the claimcode management page
	public void selectClaimCategory() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@role='combobox'])[2]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' SHAW DEFECT ')]")).click();
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

		// toggle return eligible button
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='returnEligible']")).click();
		Assert.assertTrue(true, " returnEligible toggle not selected successfully");
		Reporter.log("returnEligible toggle selected successfully");
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
		driver.findElement(By.xpath("(//button[contains(text(),'Save & Continue')])[1]")).click();

	}

	// Select add new rule in the rule definition tab
	public void addNewRule() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Add New Rule')])")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//textarea[@formcontrolname='ruleDescription']"))
				.sendKeys("Automation_ClaimcodeManagement_Testing");
		Assert.assertTrue(true, "rule definition  text not entered successfully ");
		Reporter.log(" rule definition text entered successfully");
		Thread.sleep(2000);
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
		driver.findElement(By.xpath("(//button[contains(text(),'Save & Continue')])[2]")).click();

	}

	// Select possible resolution tab in the rule definition tab
	public void selectPossibleResolution() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//button[contains(text(),' Associate Resolution')])[1]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//tbody//tr[2]//td[1]//input[@type='checkbox'])[2]")).click();
		Assert.assertTrue(true, "PossibleResolution CHECKBOX is not clicked successfully");
		Reporter.log("PossibleResolution CHECKBOX selected successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Save')])[6]")).click();
	}

	// Select associate photos tab in the rule definition tab
	public void selectAssociatePhotos() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//button[contains(text(),'Associate Photos')])[1]")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//tbody//tr[2]//td[1]//input[@type='checkbox'])[1]")).click();
		Assert.assertTrue(true, "AssociatePhoto CHECKBOX is not clicked successfully");
		Reporter.log("AssociatePhoto CHECKBOX selected successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Save')])[5]")).click();
	}

}
