package com.shawclaimx.ui.permission.pages;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Aravindan Sivanandan
 * @date_created on 09/25/2024
 * @modified by Aravindan Sivanandan
 * @modified on 09/26/2024
 * 
 */
public class AddRoleNegativePage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(AddRoleNegativePage.class);

	public AddRoleNegativePage(WebDriver driver) {
		this.driver = driver;

	}

	// To redirect Role Page
	public void redirectRolePage() throws InterruptedException {
		Thread.sleep(8000);
		WebElement link_Home = driver.findElement(By.xpath(" //*[@id=\"vertical-menu\"]/ul/li[9]/a/i"));
		link_Home.click();
		Actions builder = new Actions(driver);
		WebElement mouseOverHome = driver.findElement(By.xpath("//*[@id=\"vertical-menu\"]/ul/li[9]/ul/li[2]/a"));
		builder.moveToElement(mouseOverHome).build().perform();
		Assert.assertTrue(true, "Role page is not redirected successfully");
		Reporter.log("Role Page  is redirected successfully");
	}

	// To click Add role button
	public void addRoleButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("(//button[contains(text(),'Add')])[1]")).click();
		Assert.assertTrue(true, "Add role button is not clicked successfully");
		Reporter.log("Add role button is clicked successfully");
	}

	// to enter the Role Code
	public void addexistingRoleCode() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Rolecode = driver.findElement(By.xpath("//input[@id='roleName']"));
		Rolecode.clear();
		Rolecode.sendKeys("BugTester");
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Role already exists')]"));
		// Validate the error message
		String expectedMessage = "Role already exists";
		Assert.assertEquals("Existing Role Code validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("The Existing Role Code is entered successfully ");
	}

	

	// To click Next button
	public void nextButton() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[1]")).click();
		Assert.assertTrue(true, "Role is not created succuessfully");
		Reporter.log("Role created succuessfully");
	}

	// To go back to role list home page
	public void roleList() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//i[@class='fas fa-arrow-left arrow-left-style ng-star-inserted'])[1]")).click();
		Assert.assertTrue(true, "Navigated to  role list home page is not  done successfully");
		Reporter.log("Navigated to  role list home page successfully");
	}

	// Find Role code using its locator
	public void rolecodeText() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Rolecodetextbox = driver.findElement(By.xpath("(//th[2]//input[@type='text'])[1]"));

		// Enter the created role code
		String expectedRolecode = "AUTOMATIONROLE222";
		Rolecodetextbox.clear();
		Rolecodetextbox.sendKeys(expectedRolecode);
		Reporter.log("The incorrect Role Code is entered successfully & Validation is done ");
	}

	
}