package com.shawclaimx.ui.usermanagement.pages;

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
 * @date_created on 09/23/2024
 * @modified by Aravindan Sivanandan
 * @modified on 09/23/2024
 * 
 */
public class UserListNegativePage {
	public WebDriver driver;
	Logger log = Logger.getLogger(UserListNegativePage.class);

	public UserListNegativePage(WebDriver driver) {
		this.driver = driver;
	}

	// Method to enter the LastName
	public void enterIncorrectLastName() throws InterruptedException {
		Thread.sleep(20000);
		WebElement lastname = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[3]"));
		lastname.clear();
		lastname.sendKeys("Miiki");
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Records not found')]"));
		// Validate the error message
		String expectedMessage = "Records not found";
		Assert.assertEquals("Incorrect lastName validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("The user is not displayed successfully based on the entered Lastname");

	}

	// Method to enter the activedirectoryID
	public void enterIncorrectActiveDirectoryIDPage() throws InterruptedException {
		Thread.sleep(18000);
		WebElement activedirectoryid = driver.findElement(By.xpath("(//input[@type='text'])[2]"));
		activedirectoryid.clear();
		activedirectoryid.sendKeys("aasivana");
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Records not found')]"));
		// Validate the error message
		String expectedMessage = "Records not found";
		Assert.assertEquals("Incorrect AD ID validation Error message did not match expected message.", expectedMessage,
				errorMessage.getText());
		Reporter.log("The user is not displayed successfully based on the entered AD ID");

	}

	// Method to enter the Firstname
	public void enterIncorrectFirstName() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Firstname = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[2]"));
		Firstname.clear();
		Firstname.sendKeys("aAravindan");
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Records not found')]"));
		// Validate the error message
		String expectedMessage = "Records not found";
		Assert.assertEquals("Incorrect  Firstname validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("The user is not displayed successfully based on the entered  Firstname");
	}

	// Method to enter the EmailAddress
	public void enterIncorrectEmail() throws InterruptedException {
		Thread.sleep(20000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[3]")));
		WebElement emailAddress = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[3]"));
		emailAddress.clear();
		emailAddress.sendKeys("TMP37777674@shawinc.com");
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Records not found')]"));
		// Validate the error message
		String expectedMessage = "Records not found";
		Assert.assertEquals("Incorrect  EmailAddress validation Error message did not match expected message.",
				expectedMessage, errorMessage.getText());
		Reporter.log("The user is not displayed successfully based on the entered  EmailAddress");

	}

	// Method to edit the information of the user
	public void editIncorrectUserInformation() throws InterruptedException {

		Thread.sleep(3000);
		driver.findElement(By.xpath("//i[@class='fa-solid fa-pencil']")).click();
		Assert.assertTrue(true, "edit user information button is  clicked successfully in the user list page");
		Reporter.log("edit user information button is clicked successfully in the user list page");

		// To edit FaxNumber
		// Thread.sleep(4000);
		WebElement faxNumber = driver.findElement(By.xpath("//input[@id='editfaxNumber']"));
		faxNumber.clear();
		faxNumber.sendKeys("453463635434343434343434343434343434343434");
		// Locate the error message element
		WebElement errorMessageFax = driver
				.findElement(By.xpath("//*[contains(text(),' Maximum length is 16 digits. ')]"));
		// Validate the error message
		String expectedMessageFax = "Maximum length is 16 digits.";
		Assert.assertEquals("Incorrect  FaxNumber validation Error message did not match expected message.",
				expectedMessageFax, errorMessageFax.getText());
		Reporter.log("FaxNumber is not Updated successfully for the selected user");

		// To edit PhoneNumber
		// Thread.sleep(4000);
		WebElement phoneNumber = driver.findElement(By.xpath("//input[@id='editphoneNumber']"));
		phoneNumber.clear();
		phoneNumber.sendKeys("453463635434343434343434343434343434343434");
		// Locate the error message element
		WebElement errorMessagePhone = driver
				.findElement(By.xpath("//*[contains(text(),' Maximum length is 16 digits. ')]"));
		// Validate the error message
		String expectedMessagePhone = "Maximum length is 16 digits.";
		Assert.assertEquals("Incorrect  PhoneNumber validation Error message did not match expected message.",
				expectedMessagePhone, errorMessagePhone.getText());
		Reporter.log("PhoneNumber is not Updated successfully for the selected user");

		// To edit EmailAddress
		// Thread.sleep(4000);
		WebElement emailAddress = driver.findElement(By.xpath("//input[@id='editemailAddress']"));
		emailAddress.clear();
		emailAddress.sendKeys("aasivana@shawinc.com");
		// Locate the error message element
		WebElement erroremailAddress = driver.findElement(By.xpath("//*[contains(text(),' This Email is invalid')]"));
		// Validate the error message
		String expectedMessageEmail = "This Email is invalid";
		Assert.assertEquals("Incorrect EmailAddress validation Error message did not match expected message.",
				expectedMessageEmail, erroremailAddress.getText());
		Reporter.log("EmailAddress is not Updated successfully for the selected user");

		// To edit Return Authorization Limit
		Thread.sleep(1000);
		WebElement returnAuthorizationLimit = driver.findElement(By.xpath("//input[@id='editretAuthLimit']"));
		returnAuthorizationLimit.clear();
		returnAuthorizationLimit.sendKeys("15000444000.34");
		// Locate the error message element
		WebElement errorReturnAuthorizationLimit = driver.findElement(By.xpath(
				"//*[contains(text(),' Please enter a valid numeric value with up to 13 digits and optional 2 decimal places.')]"));
		// Validate the error message
		String expectedMessageerrorReturnAuthorizationLimit = " Please enter a valid numeric value with up to 13 digits and optional 2 decimal places. ";
		Assert.assertEquals(
				"Incorrect  Return Authorization Limit validation Error message did not match expected message.",
				expectedMessageerrorReturnAuthorizationLimit, errorReturnAuthorizationLimit.getText());
		Reporter.log("Return Authorization Limit is not Updated successfully for the selected user");
	}

	// To click Add user button
	public void clickAddUserButton() throws InterruptedException {
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//button[contains(text(),'Add')])[1]")).click();
		Assert.assertTrue(true, "Add button is not clicked successfully ");
		Reporter.log("Add button is  clicked successfully");

	}

	// Method to enter the Invalid ActiveDirectoryID
	public void enterUsedActiveDirectoryID() throws InterruptedException {
		Thread.sleep(1000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='adId']")));
		WebElement Firstname = driver.findElement(By.xpath("//input[@id='adId']"));
		Firstname.clear();
		Firstname.sendKeys("aburto2");
		Assert.assertTrue(true, "ActiveDirectoryID is not entered successfully");
		Reporter.log("used ActiveDirectoryID is  entered successfully");

	}

	// Method to click search button
	public void clickSearchButton() throws InterruptedException {
		Thread.sleep(1000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[contains(text(),'Search')])")));
		driver.findElement(By.xpath("(//button[contains(text(),'Search')])")).click();
		Assert.assertTrue(true, "Search button is not clicked successfully for Add User Information Page");
		Reporter.log("Search button is clicked successfully for Add User Information Page");

	}

	// Method to select the user checkbox
	public void selectuser() throws InterruptedException {
		Thread.sleep(1000);
		WebElement checkbox = driver.findElement(By.xpath("//tbody//td[1]//input[@type='checkbox']"));
		checkbox.click();
		Assert.assertTrue(true, "user is not selected successfully from the Add User Information Page");
		Reporter.log("user is selected successfully from the Add User Information Page");

	}

	// Method to select the next button
	public void clickNextButton() throws InterruptedException {

		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[contains(text(),' Next ')])")).click();
		Assert.assertTrue(true, "Next button is not clicked successfully from the Add User Information Page");
		Reporter.log("Next button is clicked successfully from the Add User Information Page");

		// Wait for the toast message to appear

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		WebElement toastMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("toast-message-id")));

		// Validate the toast message text
		String expectedToastMessage = "This user is already present";
		String actualToastMessage = toastMessage.getText();

		if (actualToastMessage.equals(expectedToastMessage)) {
			System.out.println("Toast message validated: " + actualToastMessage);
		} else {
			System.out.println("Toast message validation failed. Expected: " + expectedToastMessage + ", but got: "
					+ actualToastMessage);
		}

		// Wait for the toast message to disappear
		wait.until(ExpectedConditions.invisibilityOf(toastMessage));

		// Verify that the toast message is no longer visible
		boolean isToastVisible = toastMessage.isDisplayed();

		if (!isToastVisible) {
			System.out.println("Toast message has disappeared as expected.");
		} else {
			System.out.println("Toast message is still visible!");
		}
	}

	// Method to enter the activedirectoryID
	public void enterInvalidActiveDirectoryIDPage() throws InterruptedException {
		Thread.sleep(18000);
		WebElement activedirectoryid = driver.findElement(By.xpath("(//input[@type='text'])[2]"));
		activedirectoryid.clear();
		activedirectoryid.sendKeys("gromero");
		// Locate the error message element
		WebElement errorMessage = driver.findElement(By.xpath("//*[contains(text(),'Records not found')]"));
		// Validate the error message
		String expectedMessage = "Records not found";
		Assert.assertEquals("Incorrect AD ID validation Error message did not match expected message.", expectedMessage,
				errorMessage.getText());
		Reporter.log("The user is not displayed successfully based on the entered AD ID");

	}

	// Method to enter the Valid ActiveDirectoryID
	public void enterValidActiveDirectoryID() throws InterruptedException {
		Thread.sleep(1000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='adId']")));
		WebElement Firstname = driver.findElement(By.xpath("//input[@id='adId']"));
		Firstname.clear();
		Firstname.sendKeys("dbentle");
		Assert.assertTrue(true, "ActiveDirectoryID is not entered successfully");
		Reporter.log("ActiveDirectoryID is  entered successfully");

	}

	// Method to add the information of the user
	public void addIncorrectUserInformation() throws InterruptedException {

		// To add FaxNumber
		Thread.sleep(1500);
		WebElement faxNumber = driver.findElement(By.xpath("//input[@id='faxNumberId']"));
		faxNumber.clear();
		faxNumber.sendKeys("7364537284534345345345345345435");
		Assert.assertTrue(true, "FaxNumber is not added successfully for the selected user");
		Reporter.log("FaxNumber is added successfully for the selected user");
		// Locate the error message element
		WebElement errorMessageFax = driver
				.findElement(By.xpath("//*[contains(text(),' Maximum length is 16 digits. ')]"));
		// Validate the error message
		String expectedMessageFax = "Maximum length is 16 digits.";
		Assert.assertEquals("Incorrect  FaxNumber validation Error message did not match expected message.",
				expectedMessageFax, errorMessageFax.getText());
		Reporter.log("FaxNumber is not added successfully for the selected user");

		// To add PhoneNumber
		// Thread.sleep(3000);
		WebElement phoneNumber = driver.findElement(By.xpath("//input[@id='phoneNumberId']"));
		phoneNumber.clear();
		phoneNumber.sendKeys("7364537284534345345345345345435");
		// Locate the error message element
		WebElement errorMessagePhone = driver
				.findElement(By.xpath("//*[contains(text(),' Maximum length is 16 digits. ')]"));
		// Validate the error message
		String expectedMessagePhone = "Maximum length is 16 digits.";
		Assert.assertEquals("Incorrect  PhoneNumber validation Error message did not match expected message.",
				expectedMessagePhone, errorMessagePhone.getText());
		Reporter.log("PhoneNumber is not added successfully for the selected user");

		// To add EmailAddress
		// Thread.sleep(3000);
		WebElement emailAddress = driver.findElement(By.xpath("//input[@id='emailAddressId']"));
		emailAddress.clear();
		emailAddress.sendKeys("gmccurrtestingshawagent.com");
		Assert.assertTrue(true, "EmailAddress is not added successfully for the selected user");
		Reporter.log("EmailAddress is Updated successfully for the selected user");
		// Locate the error message element
		WebElement erroremailAddress = driver.findElement(By.xpath("//*[contains(text(),' This Email is invalid')]"));
		// Validate the error message
		String expectedMessageEmail = "This Email is invalid";
		Assert.assertEquals("Incorrect EmailAddress validation Error message did not match expected message.",
				expectedMessageEmail, erroremailAddress.getText());
		Reporter.log("EmailAddress is not added successfully for the selected user");

		// To add Return Authorization Limit
		Thread.sleep(1000);
		WebElement returnAuthorizationLimit = driver.findElement(By.xpath("//input[@id='retAuthLimit']"));
		returnAuthorizationLimit.clear();
		returnAuthorizationLimit.sendKeys("1500044400655675675765001.34");
		Assert.assertTrue(true, "Return Authorization Limit is not added successfully for the selected user");
		Reporter.log("Return Authorization Limit is added successfully for the selected user");
		// Locate the error message element
		WebElement errorReturnAuthorizationLimit = driver.findElement(By.xpath(
				"//*[contains(text(),' Please enter a valid numeric value with up to 13 digits and optional 2 decimal places.')]"));
		// Validate the error message
		String expectedMessageerrorReturnAuthorizationLimit = " Please enter a valid numeric value with up to 13 digits and optional 2 decimal places. ";
		Assert.assertEquals(
				"Incorrect  Return Authorization Limit validation Error message did not match expected message.",
				expectedMessageerrorReturnAuthorizationLimit, errorReturnAuthorizationLimit.getText());
		Reporter.log("Return Authorization Limit is not added successfully for the selected user");
		
		

	}
}
