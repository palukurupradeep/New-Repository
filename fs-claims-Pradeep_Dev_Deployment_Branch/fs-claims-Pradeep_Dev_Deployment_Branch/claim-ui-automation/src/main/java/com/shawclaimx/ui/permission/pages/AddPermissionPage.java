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
import org.openqa.selenium.interactions.Actions;
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
public class AddPermissionPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(AddPermissionPage.class);

	public AddPermissionPage(WebDriver driver) {
		this.driver = driver;

	}
	// To redirect Permission Page
		public void redirectPermissionPage() throws InterruptedException {
			Thread.sleep(8000);
			WebElement link_Home = driver.findElement(By.xpath(" //*[@id=\"vertical-menu\"]/ul/li[9]/a/i"));
			link_Home.click();
			Actions builder = new Actions(driver);
			WebElement mouseOverHome = driver.findElement(By.xpath("//*[@id=\"vertical-menu\"]/ul/li[9]/ul/li[3]/a"));
			builder.moveToElement(mouseOverHome).build().perform();
	        Assert.assertTrue(true, "Permisison page is not redirected successfully");
	        Reporter.log("Permission Page  is redirected successfully");
		}

	// To click Add Permission button
	public void addPermissionButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("(//button[contains(text(),' Add Permission ')])[1]")).click();
		Assert.assertTrue(true,"Add Permission button is not clicked successfully in the user l");
		Reporter.log("Add Permission button is clicked successfully in the user l");
	}

	// to enter the Permission Code
	public void permissionCode() throws InterruptedException {
		Thread.sleep(2000);
		WebElement Permissioncode = driver.findElement(By.xpath("//input[@id='permissionName']"));
		Permissioncode.clear();
		Permissioncode.sendKeys("AUTOMATIONPERMISSION639");
		Assert.assertTrue(true,"Permission Code is not  entered successfully");
		Reporter.log("Permission Code is entered successfully");

	}

	// to enter the Permission Description
	public void permissionDescription() throws InterruptedException {
		Thread.sleep(2000);
		WebElement Permissiondescription = driver
				.findElement(By.xpath("//input[@formcontrolname='permissionDescription']"));
		Permissiondescription.clear();
		Permissiondescription.sendKeys("AUTOMATIONPERMISSIONCHECK639");
		Assert.assertTrue(true,"Permission Description is not entered successfully");
		Reporter.log("Permission Description is entered successfully");

	}

	// To click Next button to go to permission set tab
	public void nextButton() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),' Next ')])[1]")).click();
		Assert.assertTrue(true,"Permission set page is not  opened successfully");
		Reporter.log("Permission set page opened successfully");

	}

	// to add permission to the Permission
	public void addPermission() throws InterruptedException {
		Thread.sleep(2000);
		WebElement Permissioncheckbox = driver.findElement(By.xpath("(//input[@type='checkbox'])[1]"));
		Permissioncheckbox.click();
		Assert.assertTrue(true,"First Permission set is not  selected successfully ");
		Reporter.log("First Permission set is selected successfully");
		Thread.sleep(1000);
		WebElement Permissioncheckbox1 = driver.findElement(By.xpath("(//input[@type='checkbox'])[2]"));
		Permissioncheckbox1.click();
		Assert.assertTrue(true,"Second Permission set is not   selected successfully ");
		Reporter.log("Second Permission set is selected successfully");
		Thread.sleep(1000);
		WebElement Permissioncheckbox3 = driver.findElement(By.xpath("(//input[@type='checkbox'])[3]"));
		Permissioncheckbox3.click();
		Assert.assertTrue(true,"Third Permission set is not   selected successfully ");
		Reporter.log("Third Permission set is selected successfully");

	}

	// To click Save button
	public void saveButton() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[contains(text(),' Save ')])[1]")).click();
		Assert.assertTrue(true,"Permission set not  added to this Permission successfully");
		Reporter.log("Permission set added to this Permission successfully");

	}
}