package com.shawclaimx.ui.workhistory;

import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;



/**
 * @author Anandajothi
 * @date_created on 05/14/2024
 * @modified by Anandajothi
 * @modified on 05/20/2024
 * 
 */
public class WorkHistoryPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(WorkHistoryPage.class);

	public WorkHistoryPage(WebDriver driver) {
		this.driver = driver;

	}

	// To redirect Work history Page
	public void redirectingWorkHistoryFlow() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//*[contains(@routerlink,'work-history')]"))
				.click();
		Assert.assertTrue(true, "Workhistory page is  not redirected successfully");
		Reporter.log(" Workhistory page is redirected successfully");
	}

	// To select usergroups & Users
	public void userGroups() throws InterruptedException {
		Thread.sleep(20000);
		List<WebElement> usergroups = driver.findElements(By.xpath("//mat-select"));
		usergroups.get(0).click();
		driver.findElement(By.xpath("//*[contains(text(),' Sales Tax ')]")).click();		
		Assert.assertTrue(true, "userGroupsDropdown value is not elected successfully for the selected user");
		Reporter.log(" userGroupsDropdown value is selected successfully in the workhistory page");

		
		Thread.sleep(2000);
		usergroups.get(1).click();
		driver.findElement(By.xpath("//*[contains(text(),' Anandajothi K Rajakannu ')]")).click();
		Assert.assertTrue(true, "userDropdown value is not selected successfully for the selected usergroup");
		Reporter.log(" list of claims associated with user is displayed successfully for the selected user");
	}

	
	// Search claim number in Textbox
	public void claimNumber() throws InterruptedException {
		Thread.sleep(6000);
		WebElement claimNumberTextbox = driver.findElement(By.xpath("//thead/tr[2]/th[3]/input[@type='text']"));
		claimNumberTextbox.clear();
		claimNumberTextbox.sendKeys("000975");
		Assert.assertTrue(true,
				"Expected Claim Number in the Claim Number# texbox is not entered successfully in the workhistory page ");
		Reporter.log("Expected Claim Number in the Claim Number# texbox is entered successfully in the workhistory page ");
	}

	// click the expected claim number
	public void claimNumberLink() throws InterruptedException {
		Thread.sleep(1000);
		WebElement claimnumberlink = driver
				.findElement(By.xpath("//table[contains(@class,'table table-striped table-hover')]/tbody/tr/td[3]/a"));
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].click()", claimnumberlink);
		Assert.assertTrue(true, "expected Claim number link is not clicked successfully");
		Reporter.log("expected Claim number link is clicked successfully");

	}
}