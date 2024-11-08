package com.shawclaimx.ui.dashboard.pages;

import java.time.Duration;
import java.util.List;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
//import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

/**
 * @author Anandajothi
 * @date_created on 03/11/2024
 * @modified by Anandajothi
 * @modified on 03/21/2024
 * 
 */
public class Dashboard_Redirection_Page {
	protected WebDriver driver;
	Logger log = Logger.getLogger(Dashboard_Redirection_Page.class);

	public Dashboard_Redirection_Page(WebDriver driver) {
		this.driver = driver;

	}

	// To redirect Work history Page
	public void workHistoryButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("(//a[@id='alertsDropdown'])[1]")).click();
		Assert.assertTrue(true, "Workhistory page is not redirected successfully");

	}

	// To redirect dashboard Page
	public void dashboardButton() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//*[@id=\"vertical-menu\"]/ul/li[1]/a")).click();
		Assert.assertTrue(true, "dashboard page is not  redirected successfully");

	}

	// To redirect Work queue Page
	public void workQueueButton() throws InterruptedException {
		Thread.sleep(3000);
		driver.findElement(By.xpath("//app-dashboard-card/div/div[1]/div/div/h3")).click();
		Assert.assertTrue(true, "Workqueue page is not  redirected successfully");

	}

	// To redirect Work Queue Page
	public void openClaimsButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//p[text()=' Open Claims ']/../h3")).click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void agedClaimsButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//p[text()=' Aged Claims ']/../h3")).click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void priorityClaimsButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//p[text()=' Priority Claims ']/../h3")).click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void debitClaimsButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//p[text()=' Debit Claims ']/../h3")).click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void lastActivityAgeClaimsButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//p[text()=' Last Activity Age ']/../h3")).click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void completedreceivedButton() throws InterruptedException {
		Thread.sleep(20000);
		driver.findElement(By.xpath("//p[text()=' Completed vs Received ']/../h3")).click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// Open Claims By Work Status

	// To redirect Work Queue Page
	public void InitiateNotWorked() throws InterruptedException {
		Thread.sleep(20000);
		String js_code = "arguments[0].scrollIntoView();";
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,300)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//*[@class='apexcharts-bar-area'][1]"));
		el.click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void ClaimPended() throws InterruptedException {
		Thread.sleep(20000);
		String js_code = "arguments[0].scrollIntoView();";
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,300)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//*[@class='apexcharts-bar-area'][2]"));
		el.click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void ReOpenedByUnmergeRequest() throws InterruptedException {
		Thread.sleep(20000);
		String js_code = "arguments[0].scrollIntoView();";
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,300)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//*[@class='apexcharts-bar-area'][3]"));
		el.click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void ReOpened() throws InterruptedException {
		Thread.sleep(20000);
		String js_code = "arguments[0].scrollIntoView();";
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,300)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//*[@class='apexcharts-bar-area'][4]"));
		el.click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}

	// To redirect Work Queue Page
	public void AssignedToISC() throws InterruptedException {
		Thread.sleep(20000);
		String js_code = "arguments[0].scrollIntoView();";
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,300)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//*[@class='apexcharts-bar-area'][5]"));
		el.click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");


	}

	// To redirect Work Queue Page
	public void RemovedFromISC() throws InterruptedException {
		Thread.sleep(20000);
		String js_code = "arguments[0].scrollIntoView();";
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,300)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//*[@class='apexcharts-bar-area'][6]"));
		el.click();
		Assert.assertTrue(true, "Workqueue page is not redirected successfully");

	}
	
	public void byWatchList() throws InterruptedException {
		Thread.sleep(20000);
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,600)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//h4[text()='Watchlist by Claims']/../div[2]/table/tbody/tr[1]/td[2]"));
		el.click();
		Assert.assertTrue(true, "Claim byWatchList page is not redirected successfully");
		
	}
	public void cancelDeleteConfirmation() throws InterruptedException {
		Thread.sleep(20000);
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,600)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//h4[text()='Watchlist by Claims']/../div[2]/table/tbody/tr[1]/td[6]"));
		el.click();
		Thread.sleep(500);
		List<WebElement> e2 = driver.findElements(By.xpath("//*[text()=' Delete Confirmation ']/../../div[3]/div/div/button[2]"));
		e2.get(0).click();
		Assert.assertTrue(true, "Claim Delete Confirmation pop-up  not canceled");
		
	}
	public void deleteClaimbyWatchList() throws InterruptedException {
		Thread.sleep(20000);
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,600)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//h4[text()='Watchlist by Claims']/../div[2]/table/tbody/tr[1]/td[6]"));
		el.click();
		Thread.sleep(500);
		List<WebElement> e2 = driver.findElements(By.xpath("//*[@id=\"deleteModal\"]/div/div/div[3]/div/div/button[1]"));
		e2.get(0).click();
		Assert.assertTrue(true, "Claim not Deleted");
		
	}
//	
	public void cancelDeleteConfirmationbyWatchListCustomer() throws InterruptedException {
		Thread.sleep(20000);
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,600)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//h4[text()='Watchlist by Customer']/../div[2]/table/tbody/tr[1]/td[4]"));
		el.click();
		Thread.sleep(500);
		List<WebElement> e2 = driver.findElements(By.xpath("//*[text()=' Delete Confirmation ']/../../div[3]/div/div/button[2]"));
		e2.get(2).click();
		Assert.assertTrue(true, "Claim Delete Confirmation pop-up  not canceled");
		
	}
	public void deleteClaimbyWatchListCustomer() throws InterruptedException {
		Thread.sleep(20000);
		((JavascriptExecutor) driver).executeScript("javascript:window.scrollBy(0,600)");
		Thread.sleep(500);
		WebElement el = driver.findElement(By.xpath("//h4[text()='Watchlist by Customer']/../div[2]/table/tbody/tr[1]/td[4]"));
		el.click();
		Thread.sleep(500);
		List<WebElement> e2 = driver.findElements(By.xpath("//*[text()=' Delete Confirmation ']/../../div[3]/div/div/button[1]"));
		e2.get(1).click();
		Assert.assertTrue(true, "Claim not Deleted");
		
	}

}
