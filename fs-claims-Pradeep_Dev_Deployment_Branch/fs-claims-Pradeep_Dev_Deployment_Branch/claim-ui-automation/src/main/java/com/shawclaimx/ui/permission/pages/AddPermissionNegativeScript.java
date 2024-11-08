package com.shawclaimx.ui.permission.pages;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;


public class AddPermissionNegativeScript {

	protected WebDriver driver;

	public AddPermissionNegativeScript(WebDriver driver) {
		this.driver = driver;
	}

	// To click Add Permission button
	public void addPermissionButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("(//button[contains(text(),' Add Permission ')])[1]")).click();

	}

	// To click Next button to go to permission set tab
	public void nextButtonempty() throws InterruptedException {
		Thread.sleep(2000);
		WebElement elementnextbutton = driver.findElement(By.xpath("(//button[contains(text(),' Next ')])[1]"));

		boolean actualValue = elementnextbutton.isEnabled();

		if (actualValue)
			System.out.println("Button is enabled ");
		else
			System.out.println("Button is disabled. Enter the All the values in reason code defintion page");

	}

	// to enter the Permission Code
	public void permissionCode() throws InterruptedException {
		Thread.sleep(2000);
		WebElement Permissioncode = driver.findElement(By.xpath("//input[@id='permissionName']"));
		Permissioncode.clear();
		Permissioncode.sendKeys("AUTOMATIONPERMISSION3011");
		System.out.println("Permisson code entered ");
	}

	// to enter the Permission Description
	public void permissionDescription() throws InterruptedException {
		Thread.sleep(2000);
		WebElement Permissiondescription = driver
				.findElement(By.xpath("//input[@formcontrolname='permissionDescription']"));
		Permissiondescription.clear();
		Permissiondescription.sendKeys("AUTOMATIONPERMISSIONCHECK3011");
		System.out.println("Permisson description entered ");

	}

	// To click Next button and move to next page
	public void nextButton() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),' Next ')])[1]")).click();

	}

	// To add permission to the Permission
	public void addPermission() throws InterruptedException {
		Thread.sleep(2000);
		WebElement Permissioncheckbox = driver.findElement(By.xpath("(//input[@type='checkbox'])[1]"));
		Permissioncheckbox.click();

		Thread.sleep(1000);
		WebElement Permissioncheckbox1 = driver.findElement(By.xpath("(//input[@type='checkbox'])[2]"));
		Permissioncheckbox1.click();

		Thread.sleep(1000);
		WebElement Permissioncheckbox3 = driver.findElement(By.xpath("(//input[@type='checkbox'])[3]"));
		Permissioncheckbox3.click();

	}

	// To click Save button
	public void saveButton() throws InterruptedException {
		Thread.sleep(5000);
		driver.findElement(By.xpath("(//button[contains(text(),' Save ')])[1]")).click();
		

	}

	// To check the duplicate permission is not allowed
	public void duplicatepermission() throws InterruptedException {
		Thread.sleep(2000);

		WebElement toaster = driver.findElement(By.xpath("//div[contains(text(),' Permissions already exists ')]"));

		String toasterMessage = toaster.getText();

		String expectedMessage = " Permissions already exists ";
		if (toasterMessage.equals(expectedMessage)) {
			System.out.println("Toaster message validation passed!");
		} else {
			System.out.println("Toaster message validation passed! Expected: " + expectedMessage);
		}

	}

	public void backbutton() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//i[contains(@class,'fas fa-arrow-left arrow-left-style')]")).click();
	}

}

