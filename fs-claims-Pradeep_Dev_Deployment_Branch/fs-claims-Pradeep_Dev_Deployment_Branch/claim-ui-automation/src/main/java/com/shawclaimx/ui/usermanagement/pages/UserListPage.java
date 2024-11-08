package com.shawclaimx.ui.usermanagement.pages;

import java.time.Duration;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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
public class UserListPage {
	protected WebDriver driver;
	Logger log = Logger.getLogger(UserListPage.class);
	

	public UserListPage(WebDriver driver) {
		this.driver = driver;

	}
	// To redirect User Page
			public void redirectUserPage() throws InterruptedException {
				Thread.sleep(8000);
				WebElement link_Home = driver.findElement(By.xpath(" //*[@id=\"vertical-menu\"]/ul/li[9]/a/i"));
				link_Home.click();
				Actions builder = new Actions(driver);
				WebElement mouseOverHome = driver.findElement(By.xpath("//*[@id=\"vertical-menu\"]/ul/li[9]/ul/li[1]/a"));
				builder.moveToElement(mouseOverHome).build().perform();
		        Assert.assertTrue(true, "User page is not redirected successfully");
		        Reporter.log("User Page  is redirected successfully");
			}

	// Method to enter the activedirectoryID
	public void enterActiveDirectoryIDPage() throws InterruptedException {
		Thread.sleep(18000);
		WebElement activedirectoryid = driver.findElement(
				By.xpath("(//input[@type='text'])[2]"));
		activedirectoryid.clear();
		activedirectoryid.sendKeys("asivana");
		Assert.assertTrue(true,"ActiveDirectoryID is not  entered successfully");
		Reporter.log("The user is displayed successfully based on the entered ActiveDirectoryID");

	}
	
	
	// Method to enter the Firstname
	public void enterFirstName() throws InterruptedException {
		Thread.sleep(1000);
		WebElement Firstname = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[2]"));
		Firstname.clear();
		Firstname.sendKeys("Aravindan");
		Assert.assertTrue(true,"FirstName is not entered successfully");
		Reporter.log("The user is displayed successfully based on the entered firstname");
	}

	// Method to enter the LastName
	public void enterLastName() throws InterruptedException {
		Thread.sleep(20000);
		WebElement lastname = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[3]"));
		lastname.clear();
		lastname.sendKeys("Sivanandan");
		Assert.assertTrue(true,"LastName is not  entered successfully");
		Reporter.log("The user is displayed successfully based on the entered Lastname");

	}

	// Method to enter the EmailAddress
	public void enterEmail() throws InterruptedException {
		Thread.sleep(20000);
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
		wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[3]")));
		WebElement emailAddress = driver.findElement(
				By.xpath("(//input[@class='search-input ng-untouched ng-pristine ng-valid'][@type='text'])[3]"));
		emailAddress.clear();
		emailAddress.sendKeys("TMP7777674@shawinc.com");
		Assert.assertTrue(true,"Email Address is not entered successfully");
		Reporter.log("The user is displayed successfully based on the entered emailaddress");

	}

	// Method to perform the activation of the user for the deactivated user
	public void activatingUser() throws InterruptedException {
		Thread.sleep(2000);
		WebElement enablebutton = driver.findElement(
				By.xpath("//input[@class='form-check-input mt-2 model-click position-relative new-bottom']"));
		enablebutton.click();

		// click the confirm button
		Thread.sleep(1000);
		WebElement Confirmbutton = driver.findElement(By.xpath("(//button[contains(text(),'Confirm')])[2]"));
		Confirmbutton.click();
		Assert.assertTrue(true,"activating user is not performed successfully");
		Reporter.log("activating user is performed successfully");
	}

	// Method to perform the deactivation of the user for the activated user
	public void deactivatingUser() throws InterruptedException {
		WebElement disablebutton = driver.findElement(
				By.xpath("//input[@class='form-check-input mt-2 model-click position-relative new-bottom']"));
		disablebutton.click();

		// click the confirm button
		Thread.sleep(1000);
		WebElement Confirmbutton = driver.findElement(By.xpath("(//button[contains(text(),'Confirm')])[2]"));
		Confirmbutton.click();
		Assert.assertTrue(true,"Deactivating user is not performed successfully");
		Reporter.log("Deactivating user is performed successfully");

	}

	// Method to edit the information of the user
	public void editUserInformation() throws InterruptedException {

		Thread.sleep(3000);
		driver.findElement(By.xpath("//i[@class='fa-solid fa-pencil']")).click();
		Assert.assertTrue(true,"edit user information button is  clicked successfully in the user list page");
		Reporter.log("edit user information button is clicked successfully in the user list page");

		// To edit FaxNumber
		//Thread.sleep(4000);
		WebElement faxNumber = driver.findElement(By.xpath("//input[@id='editfaxNumber']"));
		faxNumber.clear();
		faxNumber.sendKeys("453463635");
		Assert.assertTrue(true,"FaxNumber is not  Updated successfully for the selected user");
		Reporter.log("FaxNumber is Updated successfully for the selected user");

		// To edit PhoneNumber
		//Thread.sleep(4000);
		WebElement phoneNumber = driver.findElement(By.xpath("//input[@id='editphoneNumber']"));
		phoneNumber.clear();
		phoneNumber.sendKeys("8897696896");
		Assert.assertTrue(true,"PhoneNumber is not Updated successfully for the selected user");
		Reporter.log("PhoneNumber is Updated successfully for the selected user");

		// To edit EmailAddress
		//Thread.sleep(4000);
		WebElement emailAddress = driver.findElement(By.xpath("//input[@id='editemailAddress']"));
		emailAddress.clear();
		emailAddress.sendKeys("asivana@shawinc.com");
		Assert.assertTrue(true,"EmailAddress is not Updated successfully for the selected user");
		Reporter.log("EmailAddress is Updated successfully for the selected user");

		// To Edit/select User groups and Managers
		Thread.sleep(6000);
		driver.findElement(By.xpath("//*[@id='mat-select-value-1']")).click();
		Thread.sleep(6000);
		driver.findElement(By.xpath("//*[contains(text(),' Claims (FS) ')]")).click();
		//Select userGroupsDropdown = new Select(driver.findElement(By.xpath("//select[@formcontrolname='edituserGroups']")));
		//userGroupsDropdown.selectByIndex(2);
		Assert.assertTrue(true,"userGroupsDropdown value is not   selected successfully for the selected user");
		Reporter.log("userGroupsDropdown value is selected successfully for the selected user");
		Thread.sleep(6000);
		driver.findElement(By.xpath("//*[@id='mat-select-value-3']")).click();
		Thread.sleep(2000);
		//Select managerDropdown = new Select(				driver.findElement(By.xpath("//select[@formcontrolname='editmanagerGroups']")));
		//managerDropdown.selectByIndex(2);
		driver.findElement(By.xpath("//*[contains(text(),' Anandajothi K Rajakannu ' )]")).click();
		Assert.assertTrue(true,"ManagerDropdown value is selected successfully for the selected user");
		Reporter.log("ManagerDropdown value is selected successfully for the selected user");

		// To click Add button
		Thread.sleep(4000);
		driver.findElement(By.xpath("(//button[contains(text(),'Add')])[2]")).click();
		Assert.assertTrue(true,"Add button is not  clicked successfully for usergroups and managers selections");
		Reporter.log("Add button is clicked successfully for usergroups and managers selections");

		// To edit Return Authorization Limit
		Thread.sleep(1000);
		WebElement returnAuthorizationLimit = driver.findElement(By.xpath("//input[@id='editretAuthLimit']"));
		returnAuthorizationLimit.clear();
		returnAuthorizationLimit.sendKeys("15000444000.34");
		Assert.assertTrue(true,"Return Authorization Limit is Updated successfully for the selected user");
		Reporter.log("Return Authorization Limit is Updated successfully for the selected user");

		// To edit Return Credit Memo Limit
		//Thread.sleep(1000);
		WebElement creditMemoLimit = driver.findElement(By.xpath("//input[@id='editcreditMemoLimit']"));
		creditMemoLimit.clear();
		creditMemoLimit.sendKeys("15000444000.34");
		Assert.assertTrue(true,"creditMemoLimit is not  Updated successfully for the selected user");
		Reporter.log("creditMemoLimit is Updated successfully for the selected user");

		// To edit Inspection and Service Limit
		//Thread.sleep(4000);
		WebElement inspectionServiceLimit = driver.findElement(By.xpath("//input[@id='editinsepctionLimit']"));
		inspectionServiceLimit.clear();
		inspectionServiceLimit.sendKeys("15000444000.34");
		Assert.assertTrue(true,"inspection & ServiceLimit is  Updated successfully for the selected user");
		Reporter.log("inspection & ServiceLimit is Updated successfully for the selected user");

		// To edit Installation and Service Limit
		//Thread.sleep(4000);
		WebElement installationServiceLimit = driver.findElement(By.xpath("//input[@id='editinstalationLimit']"));
		installationServiceLimit.clear();
		installationServiceLimit.sendKeys("15000444000.34");
		Assert.assertTrue(true,"installationServiceLimit is not   updated successfully");
		Reporter.log("installationServiceLimit is updated successfully");

		// To edit Other Charges Limit
		//Thread.sleep(4000);
		WebElement otherChargesLimit = driver.findElement(By.xpath("//input[@id='editotherChargeLimit']"));
		otherChargesLimit.clear();
		otherChargesLimit.sendKeys("15000444000.34");
		Assert.assertTrue(true,"otherChargesLimit is not Updated successfully for the selected user");
		Reporter.log("otherChargesLimit is Updated successfully for the selected user");

		// click the confirm button in the edit user information form
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//button[contains(text(),'Confirm')])[1]")).click();
		Assert.assertTrue(true,"User is not  Edited Sucessfully");
		Reporter.log("User is Edited Sucessfully");

	}

	// click the sync button in the userlist page

	public void clickSyncButton() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("(//i[@class='fa-solid fa-sync'])[1]")).click();
		Assert.assertTrue(true,"Sync button is not   clicked successfully in the userlist window");
		Reporter.log("Sync button is clicked successfully in the userlist window");

		// click the confirm button in sync window

		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[contains(text(),'Confirm')])[3]")).click();
		Assert.assertTrue(true,"Confirm button is not clicked successfully in the sync popup window");
		Reporter.log("Confirm button is clicked successfully in the sync popup window");
		
	}

	// click the view user information button in the userlist page
	public void clickViewUserInformationButton() throws InterruptedException {
		Thread.sleep(2000);
		driver.findElement(By.xpath("//i[@class='fa-solid fa-eye']")).click();
		Assert.assertTrue(true,"View User Information button is  not clicked successfully in the userlist window");
		Reporter.log("View User Information page displayed successfully in the userlist window");
		Thread.sleep(1000);
		driver.findElement(By.xpath("(//button[@class='btn-close'])[3]")).click();
		Assert.assertTrue(true,"View User Information dialog box is not  closed uccessfully ");
		Reporter.log("View User Information dialog box is closed successfully");
		
	}
	
	// To click Add user button
		public void clickAddUserButton() throws InterruptedException {
			Thread.sleep(4000);
			driver.findElement(By.xpath("(//button[contains(text(),'Add')])[1]")).click();
			Assert.assertTrue(true,"Add button is not clicked successfully ");
			Reporter.log("Add button is  clicked successfully");

		}
		
		// Method to enter the ActiveDirectoryID
		public void enterActiveDirectoryID() throws InterruptedException {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@id='adId']")));
			WebElement Firstname = driver.findElement(By.xpath("//input[@id='adId']"));
			Firstname.clear();
			Firstname.sendKeys("adesmon");
			Assert.assertTrue(true,"ActiveDirectoryID is not entered successfully");
			Reporter.log("ActiveDirectoryID is  entered successfully");

		}

		// Method to click search button
		public void clickSearchButton() throws InterruptedException {
			Thread.sleep(1000);
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(120));// 2 minutes
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("(//button[contains(text(),'Search')])")));
			driver.findElement(By.xpath("(//button[contains(text(),'Search')])")).click();
			Assert.assertTrue(true,"Search button is not clicked successfully for Add User Information Page");
			Reporter.log("Search button is clicked successfully for Add User Information Page");

		}

		// Method to select the user checkbox
		public void selectuser() throws InterruptedException {
			Thread.sleep(1000);
			WebElement checkbox = driver.findElement(By.xpath("//tbody//td[1]//input[@type='checkbox']"));
			checkbox.click();
			Assert.assertTrue(true,"user is not selected successfully from the Add User Information Page");
			Reporter.log("user is selected successfully from the Add User Information Page");

		}

		// Method to select the next button
		public void clickNextButton() throws InterruptedException {
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//button[contains(text(),' Next ')])")).click();
			Assert.assertTrue(true,"Next button is not clicked successfully from the Add User Information Page");
			Reporter.log("Next button is clicked successfully from the Add User Information Page");

		}

		// Method to add the information of the user
		public void addUserInformation() throws InterruptedException {
			// To add FaxNumber
			Thread.sleep(1500);
			WebElement faxNumber = driver.findElement(By.xpath("//input[@id='faxNumberId']"));
			faxNumber.clear();
			faxNumber.sendKeys("736453728");
			Assert.assertTrue(true,"FaxNumber is not added successfully for the selected user");
			Reporter.log("FaxNumber is added successfully for the selected user");

			// To add PhoneNumber
			//Thread.sleep(3000);
			WebElement phoneNumber = driver.findElement(By.xpath("//input[@id='phoneNumberId']"));
			phoneNumber.clear();
			phoneNumber.sendKeys("9863562232");
			Assert.assertTrue(true,"PhoneNumber is not added successfully for the selected user");
			Reporter.log("PhoneNumber is added successfully for the selected user");

			// To edit EmailAddress
			//Thread.sleep(3000);
			WebElement emailAddress = driver.findElement(By.xpath("//input[@id='emailAddressId']"));
			emailAddress.clear();
			emailAddress.sendKeys("adesmontesting@shawagent.com");
			Assert.assertTrue(true,"EmailAddress is not Updated successfully for the selected user");
			Reporter.log("EmailAddress is Updated successfully for the selected user");

			// To Add/select User groups and Managers
			Thread.sleep(3000);
			driver.findElement(By.xpath("//*[@id='mat-select-value-5']")).click();
			Thread.sleep(3000);
			WebElement usergroupbutton2 =driver.findElement(By.xpath("(//*[@role='option']/span)[1]"));
			JavascriptExecutor js4 = (JavascriptExecutor) driver;
			js4.executeScript("arguments[0].click()", usergroupbutton2);
			//Select userGroupsDropdown = new Select(driver.findElement(By.xpath("//select[@formcontrolname='edituserGroups']")));
			//userGroupsDropdown.selectByIndex(2);
			Assert.assertTrue(true,"userGroupsDropdown value is not   selected successfully for the selected user");
			Reporter.log("userGroupsDropdown value is selected successfully for the selected user");
			Thread.sleep(3000);
			driver.findElement(By.xpath("//*[@formcontrolname='managerGroups']/div/div")).click();
			Thread.sleep(3000);
			driver.findElement(By.xpath("(//*[@role='option']/span)[5]")).click();
			//Select managerDropdown = new Select(				driver.findElement(By.xpath("//select[@formcontrolname='editmanagerGroups']")));
			//managerDropdown.selectByIndex(2);
			Assert.assertTrue(true,"ManagerDropdown value is selected successfully for the selected user");
			Reporter.log("ManagerDropdown value is selected successfully for the selected user");

			// To click Add button
			Thread.sleep(1000);
			driver.findElement(By.xpath("(//button[contains(text(),'Add')])")).click();
			Assert.assertTrue(true,"Add button is not clicked successfully for usergroups and managers selections");
			Reporter.log("Add button is clicked successfully for usergroups and managers selections");
			

			// To add Return Authorization Limit
			Thread.sleep(1000);
			WebElement returnAuthorizationLimit = driver.findElement(By.xpath("//input[@id='retAuthLimit']"));
			returnAuthorizationLimit.clear();
			returnAuthorizationLimit.sendKeys("1500044400001.34");
			Assert.assertTrue(true,"Return Authorization Limit is not added successfully for the selected user");
			Reporter.log("Return Authorization Limit is added successfully for the selected user");

			// To add Return Credit Memo Limit
			//Thread.sleep(3000);
			WebElement creditMemoLimit = driver.findElement(By.xpath("//input[@id='creditMemoLimit']"));
			creditMemoLimit.clear();
			creditMemoLimit.sendKeys("1500044400001.34");
			Assert.assertTrue(true,"creditMemoLimit is not added successfully for the selected user");
			Reporter.log("creditMemoLimit is added successfully for the selected user");

			// To add Inspection and Service Limit
			//Thread.sleep(3000);
			WebElement inspectionServiceLimit = driver.findElement(By.xpath("//input[@id='insepctionLimit']"));
			inspectionServiceLimit.clear();
			inspectionServiceLimit.sendKeys("1500044400001.34");
			Assert.assertTrue(true,"inspection & ServiceLimit is not added successfully for the selected user");
			Reporter.log("inspection & ServiceLimit is added successfully for the selected user");

			// To add Installation and Service Limit
			//Thread.sleep(3000);
			WebElement installationServiceLimit = driver.findElement(By.xpath("//input[@id='instalationLimit']"));
			installationServiceLimit.clear();
			installationServiceLimit.sendKeys("1500044400001.34");
			Assert.assertTrue(true,"installation & ServiceLimit is not added successfully for the selected user");
			Reporter.log("installation & ServiceLimit is added successfully for the selected user");

			// To add Other Charges Limit
			//Thread.sleep(3000);
			WebElement otherChargesLimit = driver.findElement(By.xpath("//input[@id='otherChargeLimit']"));
			otherChargesLimit.clear();
			otherChargesLimit.sendKeys("1500044400001.34");
			Assert.assertTrue(true,"otherChargesLimit is not Updated successfully for the selected user");
			Reporter.log("otherChargesLimit is Updated successfully for the selected user");

			// To click Add button
			//Thread.sleep(3000);
			driver.findElement(By.xpath("(//button[contains(text(),'Add')])[2]")).click();
			Assert.assertTrue(true,"Add button is not clicked successfully ");
			Reporter.log("Add button is clicked successfully");
			Reporter.log("User list Functionality is passed successfully");
			
		}
}
