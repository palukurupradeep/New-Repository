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
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class AddRolePage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(AddRolePage.class);

	public AddRolePage(WebDriver driver) {
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
	public void addRoleCode() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Rolecode = driver.findElement(By.xpath("//input[@id='roleName']"));
		Rolecode.clear();
		Rolecode.sendKeys("AUTOMATIONROLE639");
		Assert.assertTrue(true, "Role Code is not entered successfully");
		Reporter.log("Role Code is entered successfully");
	}

	// to enter the Role Description
	public void roleDescription() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Roledescription = driver.findElement(By.xpath("//input[@id='name']"));
		Roledescription.clear();
		Roledescription.sendKeys("Automationcheck639");
		Assert.assertTrue(true, "Role Description is not entered successfully");
		Reporter.log("Role Description is entered successfully");

	}

	// To click Next button
	public void nextButton() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[1]")).click();
		Assert.assertTrue(true, "Role is not created succuessfully");
		Reporter.log("Role created succuessfully");
	}

	// to add permission to the role
	public void addPermissionRole() throws InterruptedException {
		Thread.sleep(4000);
		WebElement Permissioncheckbox = driver
				.findElement(By.xpath("(//tbody//tr[2]//td[1]//input[@type='checkbox'])[1]"));
		Permissioncheckbox.click();
		Assert.assertTrue(true,
				"First Permission is not selected successfully from the select Permission Page under Add User Functionality");
		Reporter.log(
				"First Permission is selected successfully from the select Permission Page under Add User Functionality");
		Thread.sleep(1000);
		WebElement Permissioncheckbox1 = driver
				.findElement(By.xpath("(//tbody//tr[3]//td[1]//input[@type='checkbox'])[1]"));
		Permissioncheckbox1.click();
		Assert.assertTrue(true,
				"Second Permission is not selected successfully from the select PermissionPage under Add User Functionality");
		Reporter.log(
				"second Permission is selected successfully from the select Permission Page under Add User Functionality");
		Thread.sleep(1000);
		WebElement Permissioncheckbox3 = driver
				.findElement(By.xpath("(//tbody//tr[4]//td[1]//input[@type='checkbox'])[1]"));
		Permissioncheckbox3.click();
		Assert.assertTrue(true,
				"Third Permission is not selected successfully from the select Permission Page under Add User Functionality");
		Reporter.log(
				"Third Permission is selected successfully from the select Permission Page under Add User Functionality");
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@class='btn btn-primary'])[2]")).click();
		Assert.assertTrue(true, "Permission is not  added to Role Successfully ");
		Reporter.log("Permission added to Role Successfully");

	}

	// to add user to the role
	public void addUserrole() throws InterruptedException {
		Thread.sleep(3000);
		WebElement Usercheckbox1 = driver.findElement(By.xpath("(//tbody//tr[1]//td[1]//input[@type='checkbox'])[2]"));
		Usercheckbox1.click();
		Assert.assertTrue(true,
				"First User is not selected successfully from the select Permission Page under Add User Functionality");
		Reporter.log(
				"First User is selected successfully from the select Permission Page under Add User Functionality");

		Thread.sleep(1000);
		WebElement Usercheckbox2 = driver.findElement(By.xpath("(//tbody//tr[3]//td[1]//input[@type='checkbox'])[2]"));
		Usercheckbox2.click();
		Assert.assertTrue(true,
				"Second User is not  selected successfully from the select PermissionPage under Add User Functionality");
		Reporter.log(
				"Second User is selected successfully from the select Permission Page under Add User Functionality");
		Thread.sleep(1000);
		WebElement Usercheckbox3 = driver.findElement(By.xpath("(//tbody//tr[4]//td[1]//input[@type='checkbox'])[2]"));
		Usercheckbox3.click();
		Assert.assertTrue(true,
				"Third User is not  selected successfully from the select Permission Page under Add User Functionality");
		Reporter.log(
				"Third User is selected successfully from the select Permission Page under Add User Functionality");

	}

	// To click Save button
	public void saveButton() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[contains(text(),' Save ')])[1]")).click();
		Assert.assertTrue(true, "User not   added to this Role");
		Reporter.log("User added to this Role succuessfully");
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
		String expectedRolecode = "AUTOMATIONROLE639";
		Rolecodetextbox.clear();
		Rolecodetextbox.sendKeys(expectedRolecode);
		Assert.assertTrue(true, "Expected rolecode is not  entered successfully");
		Reporter.log("Expected rolecode entered successfully");

	}

	// To Click Edit role Button in the role list home page
	public void editRole() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//i[@class='fa-solid fa-pencil'])[1]")).click();
		Assert.assertTrue(true, "edit role button is not  clicked  in the roles list page");
		Reporter.log("edit role button is clicked successfully in the roles list page");
	}

	// Add user through edit role functionality
	public void addusereditRole() throws InterruptedException {
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//tbody//tr[9]//td[1]//input[@type='checkbox'])[1]")).click();
		// click the confirm button in the edit role information form
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[contains(text(),'Confirm')])[1]")).click();
		Assert.assertTrue(true, "User is not  added Sucessfully  through edit role functionality");
		Reporter.log("User is added Sucessfully  through edit role functionality");

	}
}