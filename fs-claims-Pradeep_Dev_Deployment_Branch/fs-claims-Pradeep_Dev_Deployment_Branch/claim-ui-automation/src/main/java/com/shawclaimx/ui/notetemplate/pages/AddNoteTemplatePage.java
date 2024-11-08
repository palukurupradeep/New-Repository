package com.shawclaimx.ui.notetemplate.pages;

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
 * @date_created on 07/01/2024
 * @modified by Aravindan Sivanandan
 * @modified on 07/02/2024
 * 
 */
public class AddNoteTemplatePage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(AddNoteTemplatePage.class);

	public AddNoteTemplatePage(WebDriver driver) {
		this.driver = driver;

	}

	public void AddNoteTemmplateFlow() throws InterruptedException {
		// Click add new template button
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//button[contains(text(),' Add Note Template')])")).click();
		Assert.assertTrue(true, "add new template button is  not clicked successfully");
		Reporter.log(" add note template button is clicked successfully");
		// Select Note type
		Thread.sleep(1500);
		Select noteTypeDropdown = new Select(driver.findElement(By.xpath("//select[@formcontrolname='noteType']")));
		noteTypeDropdown.selectByIndex(2);
		Assert.assertTrue(true, "noteTypeDropdown value is not   selected successfully for the selected user");
		Reporter.log("noteTypeDropdown value is selected successfully for the selected user");
		// To add FaxNumber
		Thread.sleep(1500);
		// to add template name
		WebElement templateName = driver.findElement(By.xpath("//input[@formcontrolname='templateName']"));
		templateName.clear();
		templateName.sendKeys("Automation testing for Template Name");
		Assert.assertTrue(true, "templateName is not entered successfull for the selected notetype");
		Reporter.log("templateName is entered successfully for the selected notetype");
		// to add Note Template Text
		Thread.sleep(1500);
		WebElement noteTemplateText = driver.findElement(By.xpath("//*[@formcontrolname='templateText']"));
		noteTemplateText.clear();
		noteTemplateText.sendKeys("Automation testing for noteTemplateText");
		Assert.assertTrue(true, "noteTemplateText is not entered successfully for the selected notetype");
		Reporter.log("noteTemplateText is entered successfully for the selected notetype");
		// toggle editable default manual buttons
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='editable']")).click();
		Assert.assertTrue(true, " editable toggle not selected successfully");
		Reporter.log("editable toggle selected successfully");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='default']")).click();
		Assert.assertTrue(true, " default toggle not selected successfully");
		Reporter.log("default toggle selected successfully");
		Thread.sleep(1000);
		driver.findElement(By.xpath("//input[@formcontrolname='manual']")).click();
		Assert.assertTrue(true, " manual toggle not selected successfully");
		Reporter.log("manual toggle selected successfully");
		// to add Audit History Template Text
		Thread.sleep(1500);
		WebElement auditHistoryText = driver.findElement(By.xpath("//*[@formcontrolname='auditHistorytemplateText']"));
		auditHistoryText.clear();
		auditHistoryText.sendKeys("Automation testing for auditHistoryTextText");
		Assert.assertTrue(true, "auditHistoryText is not entered successfully for the selected notetype");
		Reporter.log("auditHistoryText is entered successfully for the selected notetype");
		// Click save button
		Thread.sleep(2000);
		driver.findElement(By.xpath("(//button[contains(text(),'Save')])")).click();
		Assert.assertTrue(true, "save button is  not clicked successfully");
		Reporter.log(" save button is clicked successfully");
		
	}
	
	// Method to enter the Note type
	public void enterNoteType() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Notetype = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[2]"));
		Notetype.clear();
		Notetype.sendKeys("INITIATION NOTES");
		Assert.assertTrue(true,"NoteType is not entered successfully");
		Reporter.log("The NoteType is displayed successfully based on the entered value");
	}


}