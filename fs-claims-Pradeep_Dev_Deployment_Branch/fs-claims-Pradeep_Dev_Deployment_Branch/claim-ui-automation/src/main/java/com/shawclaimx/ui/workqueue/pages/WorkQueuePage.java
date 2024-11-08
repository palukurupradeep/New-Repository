package com.shawclaimx.ui.workqueue.pages;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author AnandaJothi R
 * @date_created on 14/03/2024
 * @modified by AnandaJothi R
 * @modified on 19/09/2024
 * 
 */
public class WorkQueuePage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(WorkQueuePage.class);

	public WorkQueuePage(WebDriver driver) {
		this.driver = driver;

	}

	// To redirect Work queue Page
	public void redirectingworkQueueFlow() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//*[@id=\"vertical-menu\"]/ul/li[2]/a/i"))
				.click();
		Assert.assertTrue(true, "Workqueue page is  not redirected successfully");
		Reporter.log(" Workqueue page is redirected successfully");
	}

	// To select usergroups & Users
	public void userGroups() throws InterruptedException {
		Thread.sleep(5000);
		//Selecting user group 	
		driver.findElements(By.tagName("mat-select")).get(0).click();
		driver.findElements(By.tagName("mat-option")).get(1).click();		
		Assert.assertTrue(true, "userGroupsDropdown value is not elected successfully for the selected user");
		Reporter.log(" userGroupsDropdown value is selected successfully in the workqueue page");
		//Selecting user	
		Thread.sleep(1000);
		driver.findElements(By.tagName("mat-select")).get(1).click();
		driver.findElements(By.tagName("mat-option")).get(2).click();	
		Assert.assertTrue(true, "userDropdown value is not selected successfully for the selected usergroup");
		Reporter.log(" list of claims associated with user is displayed successfully for the selected user");
	}

	// To add columns in the field chooser
	public void fieldChooser() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[@class='btn btn-sm btn-primary'])[2]")).click();
		Assert.assertTrue(true, " Field chooser menu popup opened successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[1]")).click();
		Assert.assertTrue(true, " Add Column button clicked successfully");
	
		
	}

	// Search claim number in Textbox
	public void claimNumber() throws InterruptedException {
		Thread.sleep(6000);
		WebElement claimNumberTextbox = driver.findElement(By.xpath("//thead/tr[2]/th[4]/input[@type='text']"));
		claimNumberTextbox.clear();

		claimNumberTextbox.sendKeys("000975");
	

		Assert.assertTrue(true,
				"Expected Claim Number in the Claim Number# texbox is not entered successfully in the workqueue page ");
		Reporter.log("Expected Claim Number in the Claim Number# texbox is entered successfully in the workqueue page ");
	}

	// click the expected claim number
	public void claimNumberLink() throws InterruptedException {
		Thread.sleep(1000);
		WebElement claimnumberlink = driver
				.findElement(By.xpath("//table[contains(@class,\"table table-striped\")]/tbody/tr[1]/td[4]/a"));
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].click()", claimnumberlink);
		Assert.assertTrue(true, "expected Claim number link is not clicked successfully");
		Reporter.log("expected Claim number link is clicked successfully");

	}
}