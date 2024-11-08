package com.shawclaimx.ui.smartcodeadmin.pages;

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
 * @date_created on 07/18/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/19/2024
 * 
 */

public class AddResolutionPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(AddResolutionPage.class);

	public AddResolutionPage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to add Resolution
	public void addResolutionPage() throws InterruptedException {
		Thread.sleep(3000);
		// Clicking Add Button
		driver.findElement(By.xpath("(//button[contains(text(),'Add')])[1]")).click();
		// Entering Resolution name
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='resolutionName']")).sendKeys("Automation_002");
		Assert.assertTrue(true, "Resolution Name is not entered Successfully in the resolution maintenance screen");
		Reporter.log("Resolution Name is entered Successfully in the resolution maintenance screen");
		// Entering Description
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='description']")).sendKeys("Automation testing check_002");
		Assert.assertTrue(true, "DescrIption is not entered Successfully in the resolution maintenance screen");
		Reporter.log("DescrIption is entered Successfully in the resolution maintenance screen");
		// Entering procedureManual
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='procedureManual']")).sendKeys("www.shawinc.com");
		Assert.assertTrue(true, "Procedure Manual Reference is not entered Successfully in the resolution maintenance screen");
		Reporter.log("Procedure Manual Reference is entered Successfully in the resolution maintenance screen");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Add')])[3]")).click();
		Assert.assertTrue(true, "Add button is not clicked Successfully in the resolution maintenance screen");
		Reporter.log("Claim Resolution added successfully");
		

	}

}
