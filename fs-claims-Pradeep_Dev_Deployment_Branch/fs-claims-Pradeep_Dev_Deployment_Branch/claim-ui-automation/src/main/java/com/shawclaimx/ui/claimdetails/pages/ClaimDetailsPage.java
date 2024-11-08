package com.shawclaimx.ui.claimdetails.pages;

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
 * @date_created on 03/11/2024
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */

public class ClaimDetailsPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(ClaimDetailsPage.class);

	public ClaimDetailsPage(WebDriver driver) {
		this.driver = driver;

	}

	// Click work this claim button
	public void worlClaimButton() throws InterruptedException {
		Thread.sleep(4000);
		WebElement workclaimbutton = driver
				.findElement(By.xpath("(//button[contains(text(),' Work this Claim ' )])"));
		JavascriptExecutor js2 = (JavascriptExecutor) driver;
		js2.executeScript("arguments[0].click()", workclaimbutton);
		Assert.assertTrue(true,
				" workclaimbutton is not clicked  successfully & Claim details page is not opened for the initiated claim");
		Reporter.log("Work claim button clicked successfully & Claim details page is opened for the initiated claim");
	}

	// reassigning the claim
	public void reassigningClaim() throws InterruptedException {
		Thread.sleep(3000);
		WebElement reassignbutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[1]"));
		JavascriptExecutor js3 = (JavascriptExecutor) driver;
		js3.executeScript("arguments[0].click()", reassignbutton);
		Assert.assertTrue(true, " reassignbutton is not clicked  successfully in the claim details page");
		Thread.sleep(4000);
		// selecting user group
		driver.findElement(By.xpath("(//*[@role='combobox']/div/div)[5]")).click();
		Thread.sleep(4000);
		// Select userDropdown = new
		// Select(driver.findElement(By.xpath("(//*[@id='mat-option-2558']/span")));
		// userDropdown.selectByIndex(01);
		driver.findElement(By.xpath("//*[contains(text(),' Claims (FS) ')]")).click();
		Assert.assertTrue(true, "usergroup not selected successfully");
		// selecting Administrator
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//*[@role='combobox']/div/div)[7]")).click();
		// Thread.sleep(2000);
		// Select adminDropdown = new
		// Select(driver.findElement(By.xpath("//*[@id='mat-option-2562']/span")));
		// adminDropdown.selectByIndex(01);
		Thread.sleep(4000);
		driver.findElement(By.xpath("//*[contains(text(),' Amy L Phillips ' )]")).click();
		Assert.assertTrue(true, "Administrator not  selected successfully");
		Thread.sleep(1000);
		WebElement reassignbutton2 = driver.findElement(By.xpath("(//button[@class='btn btn-primary mx-2'])[2]"));
		JavascriptExecutor js4 = (JavascriptExecutor) driver;
		js4.executeScript("arguments[0].click()", reassignbutton2);
		Assert.assertTrue(true,
				"claim  is not  reassigned to the selected Administrator successfully in the claim details page");
		Reporter.log("claim is reassigned successfully to the selected Administrator");
	}

	// changing customer number
	public void changingCustomerNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement changebutton = driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[1]"));
		JavascriptExecutor js5 = (JavascriptExecutor) driver;
		js5.executeScript("arguments[0].click()", changebutton);
		Assert.assertTrue(true,
				" changebutton near to the customer number textbox  is not  clicked  successfully in the claim details page");
		Reporter.log(
				"changebutton near to the customer number textbox  is  clicked  successfully in the claim details page");
	}

	// Find the customernametext box using its locator
	public void updateCustomerNumber() throws InterruptedException {
		Thread.sleep(3000);
		WebElement customerNameTextBox1 = driver.findElement(By.xpath("//input[@formcontrolname='customer']"));
		// Enter text into the text box
		Thread.sleep(2000);
		customerNameTextBox1.sendKeys("0081443");
		Assert.assertTrue(true, "Customer number is not entered in the claim details page ");
		Reporter.log("Customer #0081443 is updated in the claim details page");
		// click search button
		Thread.sleep(1000);
		WebElement searchbutton1 = driver.findElement(By.xpath("//i[@class='fa fa-search ng-star-inserted']"));
		JavascriptExecutor js6 = (JavascriptExecutor) driver;
		js6.executeScript("arguments[0].click()", searchbutton1);
		Assert.assertTrue(true, "search button not  clicked");
		Thread.sleep(2000);
		WebElement updatebutton = driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[2]"));
		JavascriptExecutor js7 = (JavascriptExecutor) driver;
		js7.executeScript("arguments[0].click()", updatebutton);
		Assert.assertTrue(true, "customer number is not updated successfully in the claim details page");
		Reporter.log("customer number is  updated successfully in the claim details page");
	}

	// close the claim
	public void closeClaimNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement closebutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[4]"));
		JavascriptExecutor js8 = (JavascriptExecutor) driver;
		js8.executeScript("arguments[0].click()", closebutton);
		Assert.assertTrue(true, " Initiated claim  is not closed  successfully in the claim details page");
		Reporter.log("Initiated claim  is closed  successfully in the claim details page");
	}

	// reopen the claim
	public void reopenClaimNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement closebutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[4]"));
		JavascriptExecutor js8 = (JavascriptExecutor) driver;
		js8.executeScript("arguments[0].click()", closebutton);
		Assert.assertTrue(true, " Initiated claim  is not reopened successfully in the claim details page");
		Reporter.log("Initiated claim  is reopened  successfully in the claim details page");
	}

	// merge claim
	public void mergeClaimNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement mergebutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[8]"));
		JavascriptExecutor js9 = (JavascriptExecutor) driver;
		js9.executeScript("arguments[0].click()", mergebutton);
		// selecting claim number
		Thread.sleep(2000);
		WebElement claimselection = driver.findElement(By.xpath("(//*[@role='combobox']/div/div)[9]"));
		JavascriptExecutor js10 = (JavascriptExecutor) driver;
		js10.executeScript("arguments[0].click()", claimselection);
		Thread.sleep(2000);
		WebElement claimselectionnumber = driver.findElement(By.xpath("//*[contains(text(),' 000952 ' )]"));
		JavascriptExecutor js11 = (JavascriptExecutor) driver;
		js11.executeScript("arguments[0].click()", claimselectionnumber);

		// writing merge note
		Thread.sleep(2000);
		WebElement mergenote = driver.findElement(By.xpath("(//textarea[@type='text'])[1]"));
		mergenote.sendKeys("Claim merged for automation");

		// Merge the claim
		Thread.sleep(2000);
		WebElement mergebutton2 = driver.findElement(By.xpath("(//button[@class='btn btn-primary mx-2'])[3]"));
		JavascriptExecutor js12 = (JavascriptExecutor) driver;
		js12.executeScript("arguments[0].click()", mergebutton2);
		Assert.assertTrue(true, " Initiated claim  is not merged successfully in the claim details page");
		Reporter.log("Initiated claim  is merged  successfully in the claim details page");
	}

	// assign pricing to the claim
	public void pricingClaimNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement pricebutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[9]"));
		JavascriptExecutor js13 = (JavascriptExecutor) driver;
		js13.executeScript("arguments[0].click()", pricebutton);
		Assert.assertTrue(true, " Initiated claim  is assigned to pricing successfully in the claim details page");
		Reporter.log("Initiated claim  is assigned to pricing successfully in the claim details page");
	}

	// Remove pricing to the claim
	public void removePricingClaimNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement pricebutton2 = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[8]"));
		JavascriptExecutor js14 = (JavascriptExecutor) driver;
		js14.executeScript("arguments[0].click()", pricebutton2);
		Assert.assertTrue(true, " Initiated claim  is not removed to pricing successfully in the claim details page");
		Reporter.log("Initiated claim  is removed to pricing successfully in the claim details page");
	}

	// sample request to the claim
	public void sampleRequest() throws InterruptedException {
		Thread.sleep(2000);
		WebElement samplerequestbutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[9]"));
		JavascriptExecutor js15 = (JavascriptExecutor) driver;
		js15.executeScript("arguments[0].click()", samplerequestbutton);
		Assert.assertTrue(true, " Initiated claim  is not requested to sample successfully in the claim details page");
		Reporter.log("Initiated claim  is requested to sample in the claim details page");
	}

	// Decline claim
	public void declineClaimNumber() throws InterruptedException {
		Thread.sleep(2000);
		WebElement declinebutton = driver
				.findElement(By.xpath("(//button[@class='btn btn-primary ng-star-inserted'])[4]"));
		JavascriptExecutor js16 = (JavascriptExecutor) driver;
		js16.executeScript("arguments[0].click()", declinebutton);
		// selecting reason
		Thread.sleep(2000);
		Select declineDropdown = new Select(
				driver.findElement(By.xpath("//*[@id=\"declainModal\"]/div/div/div[2]/form/div/div/select")));
		declineDropdown.selectByIndex(01);

		Thread.sleep(2000);
		WebElement declinebutton2 = driver.findElement(By.xpath("(//button[@class='btn btn-primary mx-2'])[1]"));
		JavascriptExecutor js17 = (JavascriptExecutor) driver;
		js17.executeScript("arguments[0].click()", declinebutton2);
		Assert.assertTrue(true, " Initiated claim  is not declined successfully in the claim details page");
		Reporter.log("Initiated claim  is declined  successfully in the claim details page");

	}

	// Add & Search invoice
	public void searchBarInvoiceNumberPage() throws InterruptedException {
		// adding invoice in claim details page
		Thread.sleep(3000);
		WebElement invoiceselectnumber = driver.findElement(By.xpath("//*[contains(text(),' Add Invoice ' )]"));
		JavascriptExecutor js18 = (JavascriptExecutor) driver;
		js18.executeScript("arguments[0].click()", invoiceselectnumber);
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@formcontrolname='docNumber']")).sendKeys("7731075");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Retrieve Invoice '])")).click();
		Assert.assertTrue(true, "Retrieve Invoice button is not clicked successfully");
		Reporter.log("Invoice dialog box loaded Successfully for the selected customer number ");

	}

	// Method to select an invoice in the Invoice dialog box for the selected
	// customer number
	public void selectinvoiceNumber() throws InterruptedException {
		Thread.sleep(6000);
		driver.findElement(By.xpath("(//input[@id='index' and @type='checkbox'])[1]")).click();
		Assert.assertTrue(true, "CHECKBOX is clicked successfully");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Add '])[1]")).click();
		Assert.assertTrue(true, "Add button under invoice dialog box is not clicked successfully");
		Reporter.log("Particular invoice  information loaded Successfully for the selected customer number ");

	}

	// Method to select a line item of invoice & duplication in the Invoice dialog
	// box for the selected
	// customer number
	public void selectDuplicatelineiteminvoiceNumber() throws InterruptedException {
		Thread.sleep(6000);
		driver.findElement(By.xpath("//div[3]/button[2]/i")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//label[@for='styled-checkbox-00']")).click();
		Assert.assertTrue(true, "LINE ITEM INVOICE CHECKBOX is not clicked successfully");
		Reporter.log("LINE ITEM INVOICE CHECKBOX is clicked successfully ");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Duplicate '])[1]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Duplicate '])[2]")).click();
		Assert.assertTrue(true, "line item is not duplicated  successfully");
		Reporter.log("line item is duplicated  successfully ");

	}

	// Method to select a line item of invoice & delete in the Invoice dialog box
	// for the selected
	// customer number
	public void deleteLineIteminvoiceNumber() throws InterruptedException {
		Thread.sleep(6000);
		driver.findElement(By.xpath("//div[3]/button[2]/i")).click();
		Thread.sleep(3000);
		driver.findElement(By.xpath("//label[@for='styled-checkbox-00']")).click();
		Assert.assertTrue(true, "LINE ITEM INVOICE CHECKBOX is clicked successfully");
		Reporter.log("LINE ITEM INVOICE CHECKBOX is clicked successfully ");
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Delete '])[1]")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[text()=' Confirm '])[1]")).click();
		Assert.assertTrue(true, "line item is not deleted successfully");
		Reporter.log("line item is deleted  successfully ");

	}

}
