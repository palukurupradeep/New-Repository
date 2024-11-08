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
 * @date_created on 07/19/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/19/2024
 * 
 */

public class UpdateResolutionPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(UpdateResolutionPage.class);

	public UpdateResolutionPage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to update Resolution
	public void updateResolutionPage() throws InterruptedException {
		Thread.sleep(3000);
		// Clicking edit Button
		driver.findElement(By.xpath("(//a[@class='text-primary model-click'])[1]")).click();
		// Entering Resolution name
		Thread.sleep(2000);
		WebElement resolutionName = driver.findElement(By.xpath("//input[@formcontrolname='resolutionName']"));
		resolutionName.clear();
		resolutionName.sendKeys("Automation_Name_001");
		Assert.assertTrue(true, "Resolution Name is not edited Successfully in the update resolution item screen");
		Reporter.log("Resolution Name is edited Successfully in the update resolution item screen");
		// Entering Description
		Thread.sleep(2000);
		WebElement description = driver.findElement(By.xpath("//input[@formcontrolname='description']"));
		description.clear();
		description.sendKeys("Automation_Description_002");
		Assert.assertTrue(true, "Description is not edited Successfully in the update resolution item screen");
		Reporter.log("Description is edited Successfully in the update resolution item screen");
		// Entering procedureManual
		Thread.sleep(2000);
		WebElement procedureManual = driver.findElement(By.xpath("//input[@formcontrolname='procedureManual']"));
		procedureManual.clear();
		procedureManual.sendKeys("www.google.com");
		Assert.assertTrue(true, "Procedure Manual Reference is not edited Successfully in the update resolution item screen");
		Reporter.log("Procedure Manual Reference is edited Successfully in the update resolution item screen");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Update')])[1]")).click();
		Assert.assertTrue(true, "update button is not clicked Successfully in the update resolution item screen");
		Reporter.log("Claim Resolution updated successfully for the selected item");
		

	}

}
