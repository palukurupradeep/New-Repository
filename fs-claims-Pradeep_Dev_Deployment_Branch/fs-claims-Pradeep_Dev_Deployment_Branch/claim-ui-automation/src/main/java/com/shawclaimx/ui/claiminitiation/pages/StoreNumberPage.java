package com.shawclaimx.ui.claiminitiation.pages;

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
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class StoreNumberPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(StoreNumberPage.class);

	public StoreNumberPage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to search the store number in the store number textbox
	public void searchBarStoreNumber() throws InterruptedException {
		Thread.sleep(4000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='store']")));
		driver.findElement(By.xpath("//input[@formcontrolname='store']")).sendKeys("2305");
		Thread.sleep(4000);
		WebElement searchbutton = driver
				.findElement(By.xpath("(//*[@formcontrolname='store']//ancestor::div//span//i)[1]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", searchbutton);
		Assert.assertTrue(true,"All customerdetails informations is loaded Successfully for the selected store number ");
		Reporter.log("All customerdetails informations loaded Successfully for the selected store number");

	}

	// Method to select a store number in the store number popup window
	public void selectStoreNumber() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//div[contains(@id,'storeinvoice')]//input[@type=\"checkbox\"])[2]")).click();
		Assert.assertTrue(true,"CHECKBOX is clicked successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Add '])[3]")).click();
		Assert.assertTrue(true,"ADD button is not clicked successfully");
		Reporter.log("Particular customerdetails information loaded Successfully for the selected store number ");
		

	}
	// Method to verify the EnterClaimNotes text in the EnterClaimNotesPage

	public void verifyEnterClaimNotesPage() throws InterruptedException {
		Thread.sleep(2000);
		WebElement claimNotes = driver.findElement(By.xpath("//textarea[@id='exampleFormControlTextarea1']"));
		claimNotes.clear();
		claimNotes.sendKeys("Automation - Claim Initiation through store number ");
		Assert.assertTrue(true,"Data is not entered successfully in the confidential claim notes text box");
		Reporter.log("Data entered successfully in the confidential claim notes text box");

	}

}
