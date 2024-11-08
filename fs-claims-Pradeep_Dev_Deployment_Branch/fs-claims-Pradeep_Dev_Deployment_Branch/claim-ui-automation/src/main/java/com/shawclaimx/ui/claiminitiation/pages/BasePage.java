package com.shawclaimx.ui.claiminitiation.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
/**
 * @author Aravindan Sivanandan
 * @date_created on 12/28/2023
 * @modified by Aravindan Sivanandan
 * @modified on 03/21/2024
 * 
 */
public class BasePage {
	protected WebDriver driver;
//	private By signInButton = By.linkText("Sign In");

	public BasePage(WebDriver driver) {
	this.driver = driver;
	}

	//Method to retrieve the title of the application 

	public String getPageTitle() {
		String title = driver.getTitle();
		System.out.println(title);
		return title;
	}

	//Method to verify the title of the application 
	public boolean verifyBasePageTitle() {
		String expectedPageTitle = "Shaw Claim Management";
		return getPageTitle().contains(expectedPageTitle);
	}
}