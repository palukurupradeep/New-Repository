package com.shawclaimx.ui.workqueue.pages;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.shawclaimx.ui.dashboard.pages.DashboardValidation;
import com.shawclaimx.ui.dataSetup.UtilitySetup;

public class WorkQueueValidation extends UtilitySetup {

	DashboardValidation dashboard;

	@FindBy(xpath = "//button[text()=' Field Chooser ']")
	WebElement fieldChooserBtn;

	@FindBy(xpath = "//div[text()='Work Status']/parent::div//button")
	WebElement workStatusDeleteIcon;

	@FindBy(xpath = "//button[text()=' + Add Column ']")
	WebElement addColumnBtn;

	@FindBy(xpath = "//select[contains(@class,'form-control form-select ng-')]")
	WebElement addDropdown;

	@FindBy(xpath = "//button[text()='Ok']")
	WebElement okBtn;

	@FindBy(xpath = "(//table//tr//input)[16]")
	WebElement workStatusFilter;

	public WorkQueueValidation() {
		PageFactory.initElements(driver, this);
	}

	public int listOfClaimsInWorkQueue() {
		List<WebElement> allClaimNumbers = driver.findElements(By.xpath("//td[@class='pl-15 ng-star-inserted']//a"));
		int listOfClaimsInWorkQueue = allClaimNumbers.size();
		System.out.println("List of claims in Work Queue page is " + listOfClaimsInWorkQueue);
		return listOfClaimsInWorkQueue;
	}

	public void workStatusValidationWithDashboard(String groupName, String userName) throws Throwable {
		dashboard = new DashboardValidation();
		String returnValueFromDashboard = dashboard.returnValueFromWidgets;
		if (!returnValueFromDashboard.equalsIgnoreCase(" No Open Claims Available ")) {
			dashboard.goToWorkQueuePage();
			dashboard.selectingUserGroupFromDropDown(groupName);
			dashboard.selectingUserFromDropDown(userName);
			clickOn(fieldChooserBtn);
			clickOn(workStatusDeleteIcon);
			clickOn(addColumnBtn);
			Select select = new Select(addDropdown);
			select.selectByVisibleText(" Work Status ");
			clickOn(okBtn);
			Thread.sleep(5000);
			clickOn(workStatusFilter);
			LinkedHashMap<String, Integer> map = DashboardValidation.getMapValue();
			System.out.println("************* COUNT OF OPEN CLAIMS IN WORK QUEUE PAGE BY WORK STATUS FILTER *************");
			for (String key : map.keySet()) {
				workStatusFilter.sendKeys(Keys.CONTROL + "A" + Keys.BACK_SPACE);
				type(workStatusFilter, key);
				workStatusFilter.sendKeys(Keys.ENTER);
				Integer countOfCaimsInDashboardByWorkStatus = map.get(key);
				Thread.sleep(8000);
				List<WebElement> listOfFilteredClaims = driver.findElements(By.xpath("//td[text()=' " + key + " ']"));
				Integer countOfClaimsInWorkQueueByWorkStatus = listOfFilteredClaims.size();
				System.out.println(
						"List of " + key + " claims in Work Queue page is " + countOfClaimsInWorkQueueByWorkStatus);
				Assert.assertEquals(countOfClaimsInWorkQueueByWorkStatus, countOfCaimsInDashboardByWorkStatus);
			}
		
		} else {
			Assert.assertTrue(true);
		}
	}
}
