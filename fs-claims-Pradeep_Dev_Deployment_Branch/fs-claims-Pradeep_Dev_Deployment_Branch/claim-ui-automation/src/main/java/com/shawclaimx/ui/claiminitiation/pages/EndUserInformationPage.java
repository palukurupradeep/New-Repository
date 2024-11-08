package com.shawclaimx.ui.claiminitiation.pages;

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

import java.io.IOException;

import com.aventstack.extentreports.model.Log;

/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class EndUserInformationPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(EndUserInformationPage.class);

	public EndUserInformationPage(WebDriver driver) {
		this.driver = driver;

	}

	// to enable the End User Claim or Customer Number Not Available? button
	public void EndUserInformationButton() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='endUserSwitch']")).click();
		Assert.assertTrue(true, "End User Claim or Customer Number Not Available button is not Enabled");
		Reporter.log("End User Claim or Customer Number Not Available button is Enabled");

	}
	
	public void EndUserInformationButtonConfirmation() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[text()=' Confirm ']")).click();
		Assert.assertTrue(true, "Initiate The Claim As End User Claim is confirmed ");
		Reporter.log("Initiate The Claim As End User Claim is confirmed");

	}
	

	// Method to select the AddEditEndUserInformationButton
	public void addEditEndUserInformationButton() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//button[text()=' Add/Edit End User Information ']")).click();
		Assert.assertTrue(true, "AddEditEndUserInformationButton is not Selected");
		Reporter.log("AddEditEndUserInformationButton is Selected");

	}
	// Method to enter the FirstName

	public void enterFirstName() throws InterruptedException {
		Thread.sleep(1000);
		WebElement firstName = driver.findElement(By.xpath("//input[@formcontrolname='firstName']"));
		firstName.clear();
		firstName.sendKeys("Sachin");
		Assert.assertTrue(true, "Firstname not entered successfully");
		Reporter.log("Firstname entered successfully");

	}

	// Method to enter the MiddleName
	public void enterMiddleName() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement middleName = driver.findElement(By.xpath("//input[@formcontrolname='middleInitial']"));
		middleName.clear();
		middleName.sendKeys("M");
		Assert.assertTrue(true, "Middlename not entered successfully");
		Reporter.log("Middlename entered successfully");

	}

	// Method to enter the LastName
	public void enterLastName() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement lastName = driver.findElement(By.xpath("//input[@formcontrolname='lastName']"));
		lastName.clear();
		lastName.sendKeys("Wilson");
		Assert.assertTrue(true, "Lastname not entered successfully");
		Reporter.log("Lastname entered successfully");

	}

	// Method to enter the CompanyName
	public void enterCompanyName() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement companyName = driver.findElement(By.xpath("//input[@formcontrolname='companyName']"));
		companyName.clear();
		companyName.sendKeys("ShawClaimx");
		Assert.assertTrue(true, "Companyname not entered successfully");
		Reporter.log("Companyname entered successfully");

	}

	// Method to enter the emailAddress
	public void enterEmailAddress() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement email = driver.findElement(By.xpath("//input[@formcontrolname='email']"));
		email.clear();
		email.sendKeys("wilsonsachin@shawclaimagent.com");
		Assert.assertTrue(true, "Email Address not entered successfully");
		Reporter.log("Email Address entered successfully");

	}

	// Method to enter the addressLine1
	public void enterAddress1() throws InterruptedException {
		WebElement address = driver.findElement(By.xpath("//input[@formcontrolname='address']"));
		address.clear();
		address.sendKeys("122/23, West Street, Richmond Circle , CubbonPark");
		Assert.assertTrue(true, "Addressline1 not entered successfully");
		Reporter.log("Addressline1 entered successfully");

	}

	// Method to enter the addressLine2
	public void enterAddress2() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement address2 = driver.findElement(By.xpath("//input[@formcontrolname='addressName']"));
		address2.clear();
		address2.sendKeys("MKAGV Township, Flagmand Community");
		Assert.assertTrue(true, "Addressline2 not entered successfully");
		Reporter.log("Addressline2 entered successfully");

	}

	// Method to select the state
	public void selectState() throws InterruptedException {
		//Thread.sleep(3000);
		driver.findElement(By.xpath("//select[@formcontrolname='state']")).click();
		//Thread.sleep(3000);
		Select stateDropdown = new Select(driver.findElement(By.xpath("//select[@formcontrolname='state']")));
		stateDropdown.selectByIndex(03);
		Assert.assertTrue(true, "state is not selected successfully");
		Reporter.log("state is selected successfully");

	}

	// Method to enter the City
	public void enterCity() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement city = driver.findElement(By.xpath("//input[@formcontrolname='city']"));
		city.clear();
		city.sendKeys("Texas");
		Assert.assertTrue(true, "city not entered successfully");
		Reporter.log("city  entered successfully");

	}

	// Method to enter the County
	public void enterCounty() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement county = driver.findElement(By.xpath("//input[@formcontrolname='county']"));
		county.clear();
		county.sendKeys("MGCircle");
		Assert.assertTrue(true, "county not entered successfully");
		Reporter.log("county entered successfully");

	}

	// Method to enter the postalCode
	public void enterPostalCode() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement postalCode = driver.findElement(By.xpath("//input[@formcontrolname='postalCode']"));
		postalCode.clear();
		postalCode.sendKeys("600001");
		Assert.assertTrue(true, "postalcode not entered successfully");
		Reporter.log("postalcode is entered successfully");

	}

	// Method to enter PhoneBusiness
	public void selectPhoneBusiness() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement PhoneBusiness = driver.findElement(By.xpath("//input[@formcontrolname='business']"));
		PhoneBusiness.clear();
		PhoneBusiness.sendKeys("9876790876");
		Assert.assertTrue(true, "phoneBusinessNumber not entered successfully");
		Reporter.log("phoneBusinessNumber entered successfully");
		//Thread.sleep(3000);
		WebElement extension = driver.findElement(By.xpath("//input[@formcontrolname='extension']"));
		extension.clear();
		extension.sendKeys("231");
		Assert.assertTrue(true, "phoneBusinessNumber extension not entered successfully");
		Reporter.log("phoneBusinessNumber extension entered successfully");

	}

	// Method to enter PhoneHome
	public void selectPhoneHome() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement phonehome = driver.findElement(By.xpath("//input[@formcontrolname='home']"));
		phonehome.clear();
		phonehome.sendKeys("8715243522");
		Assert.assertTrue(true, "phoneHomeNumber not entered successfully");
		Reporter.log("phoneHomeNumber entered successfully");

		//Thread.sleep(3000);
		WebElement extension1 = driver.findElement(By.xpath("//input[@formcontrolname='extHome']"));
		extension1.clear();
		extension1.sendKeys("342");
		Assert.assertTrue(true, "phoneHomeNumber extension not entered successfully");
		Reporter.log("phoneHomeNumber extension entered successfully");

	}

	// Method to enter PhoneCell
	public void selectPhoneCell() throws InterruptedException {
		//Thread.sleep(3000);
		WebElement cellnumber = driver.findElement(By.xpath("//input[@formcontrolname='cell']"));
		cellnumber.clear();
		cellnumber.sendKeys("7017623482");
		Assert.assertTrue(true, "PhoneCellNumber not entered successfully");
		Reporter.log("PhoneCellNumber  entered successfully");
		WebElement extensioncell = driver.findElement(By.xpath("//input[@formcontrolname='extCell']"));
		extensioncell.clear();
		extensioncell.sendKeys("103");
		Assert.assertTrue(true, "PhoneCellNumber extension not entered successfully");
		Reporter.log("PhoneCellNumber extension  entered successfully");

	}

	// Method to select Add Button
	public void selectAddButton() throws InterruptedException {
		//Thread.sleep(3000);
		driver.findElement(By.xpath("(//button[text()=' Add '])")).click();
		Assert.assertTrue(true, "ADD button is not clicked successfully");
		Reporter.log("ADD button is  clicked successfully");

	}

	// Method to verify the EnterClaimNotes text in the EnterClaimNotesPage

	public void verifyEnterClaimNotesPage() throws InterruptedException {
		Thread.sleep(2000);
		WebElement claimNotes = driver.findElement(By.xpath("//textarea[@id='exampleFormControlTextarea1']"));
		claimNotes.clear();
		claimNotes.sendKeys("Automation - Claim Initiation through end user information ");
		Assert.assertTrue(true, "Data is not entered successfully in the confidential claim notes text box");
		Reporter.log("Data entered successfully in the confidential claim notes text box");

	}

}
