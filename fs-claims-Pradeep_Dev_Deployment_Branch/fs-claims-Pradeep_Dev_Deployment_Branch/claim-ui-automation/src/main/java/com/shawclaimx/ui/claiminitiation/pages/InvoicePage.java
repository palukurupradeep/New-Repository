package com.shawclaimx.ui.claiminitiation.pages;

import java.time.Duration;
import java.util.List;

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
public class InvoicePage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(InvoicePage.class);

	public InvoicePage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to search InvoiceNumber

	public void searchBarInvoiceNumberPage() throws InterruptedException {
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
		driver.findElement(By.xpath("(//button[text()=' Add '])[2]")).click();
		Assert.assertTrue(true, "Add button under invoice dialog box is not clicked successfully");
		Reporter.log("Particular invoice  information loaded Successfully for the selected customer number ");

	}

	// Method to verify the EnterClaimNotes text in the EnterClaimNotesPage

	public void verifyEnterClaimNotesPage() throws InterruptedException {
		Thread.sleep(2000);
		WebElement claimNotes = driver.findElement(By.xpath("//textarea[@id='exampleFormControlTextarea1']"));
		claimNotes.clear();
		claimNotes.sendKeys("Automation - Claim Initiation through invoice number ");
		Assert.assertTrue(true, "Data entered is not successfully in the confidential claim notes text box");
		Reporter.log("Data entered is entered successfully in the confidential claim notes text box");

	}

}
