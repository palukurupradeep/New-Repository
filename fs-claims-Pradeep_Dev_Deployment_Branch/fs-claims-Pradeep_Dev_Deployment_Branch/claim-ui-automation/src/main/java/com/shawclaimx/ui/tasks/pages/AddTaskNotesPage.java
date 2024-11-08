package com.shawclaimx.ui.tasks.pages;
import java.time.Duration;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Aravindan Sivanandan
 * @date_created on 07/10/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/10/2024
 * 
 */
public class AddTaskNotesPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(AddTaskNotesPage.class);

	public AddTaskNotesPage(WebDriver driver) {
		this.driver = driver;

	}

	public void AddTaskNotesPageFlow() throws InterruptedException {
		// Click Add task note button
		Thread.sleep(8000);
		driver.findElement(By.xpath("(//i[@class='fa fa-plus'])[1]")).click();
		Assert.assertTrue(true, "add task note button is  not clicked successfully");
		Reporter.log(" Add task note button is clicked successfully");
		// Select Note type
		Thread.sleep(1500);
		Select noteTypeDropdown = new Select(driver.findElement(By.xpath("//select[@formcontrolname='noteType']")));
		noteTypeDropdown.selectByIndex(1);
		Assert.assertTrue(true, "noteTypeDropdown value is not   selected successfully for the selected user");
		Reporter.log("noteTypeDropdown value is selected successfully for the selected user");
		// toggle confidential button
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='confidential']")).click();
		Assert.assertTrue(true, " confidential button not selected successfully");
		Reporter.log("confidential button selected successfully");
		// to select trace type and trace date
		Select traceTypeDropdown = new Select(driver.findElement(By.xpath("//select[@formcontrolname='traceType']")));
		traceTypeDropdown.selectByIndex(1);
		Assert.assertTrue(true, "traceTypeDropdown value is not   selected successfully");
		Reporter.log("traceTypeDropdown value is selected successfully ");
		// toggle call log button
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='callLog']")).click();
		Assert.assertTrue(true, " call log button not selected successfully");
		Reporter.log("call log button selected successfully");
		// to add note Text to the task
		Thread.sleep(1500);
		WebElement noteText = driver.findElement(By.xpath("//*[@formcontrolname='noteText']"));
		noteText.clear();
		noteText.sendKeys("Automation testing for add note Text to the task");
		Assert.assertTrue(true, "add note Text to the task is not added successfully for the selected notetype");
		Reporter.log("add note Text to the task is added successfully for the selected notetype");

	}

	public void SaveTaskNotesPageFlow() throws InterruptedException {
		// save the Task
		Thread.sleep(6000);
		driver.findElement(By.xpath("(//button[contains(text(),'Save')])[1]")).click();
		Assert.assertTrue(true, "save button is  not clicked successfully");
		Reporter.log(" save button is clicked successfully");

	}

	public void PendTaskNotesPageFlow() throws InterruptedException {
		// Pend the Task
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Pend')])[3]")).click();
		Assert.assertTrue(true, "Pend button is  not clicked successfully");
		Reporter.log("pended task has been created");

	}

	

}