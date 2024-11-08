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

public class CustomerPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(CustomerPage.class);

	public CustomerPage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to search the customer number in the customerNameTextbox
	public void searchBarCustomerPage() throws InterruptedException {
		Thread.sleep(15000);
		driver.findElement(By.xpath("//input[@formcontrolname='customer']")).sendKeys("0215559");
		//Thread.sleep(10000);
		WebElement searchbutton = driver.findElement(By.xpath("//i[@class=\"fa fa-search ng-star-inserted\"]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", searchbutton);
		Assert.assertTrue(true, "customerdetails information not loaded Successfully for the selected customer number");
		Reporter.log("customerdetails information loaded Successfully for the selected customer #0215559");

	}

	// Method to select the reason code in the reason code dropdown

	public void selectReasonCode() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='reasonCode']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//mat-option[@role='option'][1]"))
				.click();
		Assert.assertTrue(true, "Reason code (D01 - YARN STREAKS - LENGTH WISE) not selected successfully");
		Reporter.log("Reason code (D01 - YARN STREAKS - LENGTH WISE) selected successfully");

		// Toggle jobstop & Add to watchlist
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='isjobStopped']")).click();
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='watchList']")).click();
		Assert.assertTrue(true, "Jobstop,add to watchlist toggle not selected successfully");
		Reporter.log("Jobstop,add to watchlist toggle selected successfully");

	}

	// Method to click the continue initiation button
	public void continueInitiation() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' Continue Initiation')]")).click();
		Assert.assertTrue(true,
				"Continue Initiation button is not clicked successfully & Prior claim summary screen is not getting displayed");
		Reporter.log(
				"Continue Initiation button is clicked successfully & Prior claim summary screen is  getting displayed");
	}

	// Method to click the continue initiation button in the prior claim summary
	// screen
	public void continueInitiationPrior() throws InterruptedException {
		Thread.sleep(6000);
		driver.findElement(By.xpath("(//*[contains(text(),'Continue Initiation')])[2]")).click();
		Assert.assertTrue(true, "Continue Initiation button is not clicked successfully");
		Reporter.log("Continue Initiation button is clicked successfully in the prior claim summary dialog box");

	}

	// Method to verify the EnterClaimNotes text in the EnterClaimNotesPage

	public void verifyEnterClaimNotesPage() throws InterruptedException {
		Thread.sleep(1000);
		WebElement claimNotes = driver.findElement(By.xpath("//textarea[@id='exampleFormControlTextarea1']"));
		claimNotes.clear();
		claimNotes.sendKeys("Automation - Claim Initiation through customer number");
		Assert.assertTrue(true, "Data not entered successfully in the confidential claim notes text box");
		Reporter.log("Data entered successfully in the confidential claim notes text box");

	}

	// Method to initiate the claim
	public void InitiateClaim() throws InterruptedException {
		Thread.sleep(2000);
		WebElement initiateclaimbutton = driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[1]"));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].click()", initiateclaimbutton);
		Assert.assertTrue(true, "Initiation claim button is not clicked successfully");
		Reporter.log("Claim is successfully initiated after clicking the  Initiation claim button ");
	}

	// Method to CLOSE the claim window
	public void closeInitiateClaim() throws InterruptedException {
		Thread.sleep(2000);
		WebElement closeclaimbutton = driver.findElement(By.xpath("//*[@id=\"initiateclaim\"]/div/div/div[1]/button"));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].click()", closeclaimbutton);
		Assert.assertTrue(true, "close claim button is not clicked successfully");
		Reporter.log("Claim window is successfully closed ");
	}

	// Method to return to initiate claim page
	public void returnInitiateClaimPage() throws InterruptedException {
		Thread.sleep(2000);
		WebElement returninitiateclaim = driver.findElement(By.xpath("//*[@id=\"vertical-menu\"]/ul/li[3]/a/i"));
		JavascriptExecutor js1 = (JavascriptExecutor) driver;
		js1.executeScript("arguments[0].click()", returninitiateclaim);
		Assert.assertTrue(true, "InitiateClaimPage home page is not redirected successfully");
		Reporter.log("InitiateClaimPage home page is redirected successfully ");
	}

}
