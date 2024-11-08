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
 * @date_created on 08/09/2024
 * @modified by Aravindan Sivanandan
 * @modified on 08/30/2024
 * 
 */

public class ClaimInitiationNegativePage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(ClaimInitiationNegativePage.class);

	public ClaimInitiationNegativePage(WebDriver driver) {
		this.driver = driver;

	}

	// Method to search the empty customer number in the customerNameTextbox
	public void searchEmptyCustomerNumber_NegativeCase() throws InterruptedException {
		Thread.sleep(15000);
		driver.findElement(By.xpath("//input[@formcontrolname='customer']")).sendKeys("0215559");
		WebElement searchbutton = driver.findElement(By.xpath("//i[@class=\"fa fa-search ng-star-inserted\"]"));
		searchbutton.click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//input[@formcontrolname='customer']")).click();
		Thread.sleep(4000);
		driver.findElement(By.xpath("//input[@formcontrolname='customer']")).clear();
		Thread.sleep(10000);
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Customer# cannot be empty')]"));
		// Validate the error message
		String expectedMessage = "Customer# cannot be empty";
		Assert.assertEquals("Empty customer number validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("Customer information is not getting displayed");
	}

	// Method to search the invalid store number in the store number textbox
	public void searchInvalidStoreNumber_NegativeCase() throws InterruptedException {
		Thread.sleep(4000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@formcontrolname='store']")));
		driver.findElement(By.xpath("//input[@formcontrolname='store']")).clear();
		driver.findElement(By.xpath("//input[@formcontrolname='store']")).sendKeys("230514");
		Thread.sleep(4000);
		WebElement searchbutton = driver
				.findElement(By.xpath("(//*[@formcontrolname='store']//ancestor::div//span//i)[1]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", searchbutton);
		// Locate the error message element
		Thread.sleep(8000);
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),' Store number is invalid ')]"));
		// Validate the error message
		String expectedMessage = "Store number is invalid";
		Assert.assertEquals("Invalid store number validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("Store number dialog box not  loaded Successfully for the invalid store number");

	}

	// Method to search the invalid customer number in the customer number textbox
	public void searchInvalidCustomerNumber_NegativeCase() throws InterruptedException {
		Thread.sleep(15000);
		driver.findElement(By.xpath("//input[@formcontrolname='customer']")).clear();
		driver.findElement(By.xpath("//input[@formcontrolname='customer']")).sendKeys("0000000");
		WebElement searchbutton = driver.findElement(By.xpath("//i[@class=\"fa fa-search ng-star-inserted\"]"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", searchbutton);
		// Locate the error message element
		Thread.sleep(8000);
		WebElement errorMessage = driver.findElement(
				By.xpath("//*[contains(text(),' Customer Number is invalid.Please provide a valid number ')]"));
		// Validate the error message
		String expectedMessage = "Customer Number is invalid.Please provide a valid number";
		Assert.assertEquals("Invalid customer number validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("Customer information is not getting displayed for an invalid customer number");
	}

	// Method to valid the continue initiation button for an invalid customer number
	public void continueInitiationValidation_NegativeCase() throws InterruptedException {
		Thread.sleep(8000);

		// Locate the button element
		WebElement button = driver.findElement(By.xpath("//*[contains(text(),' Continue Initiation')]"));

		// Check if the button is disabled
		boolean isButtonDisabled = !button.isEnabled();

		// Validate the button's disabled state
		if (isButtonDisabled) {
			System.out.println("The Continue Initiation button is disabled in the claim initiation page");
		} else {
			System.out.println("The button is enabled in the claim initiation page");

		}

	}
}
