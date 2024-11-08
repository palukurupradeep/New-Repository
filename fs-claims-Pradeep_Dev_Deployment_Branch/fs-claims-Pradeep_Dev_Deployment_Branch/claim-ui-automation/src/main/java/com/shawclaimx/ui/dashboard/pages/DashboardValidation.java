package com.shawclaimx.ui.dashboard.pages;

import java.io.FileOutputStream;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.shawclaimx.ui.dataSetup.UtilitySetup;

public class DashboardValidation extends UtilitySetup {

	public static LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
	public static String returnValueFromWidgets;

	@FindBy(xpath = "//a[contains(text(),' Welcome ')]")
	WebElement userName;

	@FindBy(xpath = "//a[@routerlink='/dashboard']")
	WebElement dashboardIcon;

	@FindBy(xpath = "//a[@routerlink='/workQueue']")
	WebElement workQueueIcon;
	
	@FindBy(xpath = "//i[contains(@class,'fa-solid fa-code sidenav-link-icon')]//parent::a")
	WebElement smartCodeIcon; 
	
	@FindBy(xpath = "//a[@routerlink='/view-note-template']")
	WebElement manageNoteTemplateSideNavMenu;

	@FindBy(xpath = "//a[@routerlink='/widget-config']")
	WebElement userPreference;

	@FindBy(xpath = "//button[text()='Apply']")
	WebElement applyBtnInUserPref;

	@FindBy(xpath = "//h1[@class='mb-0' and text()='Dashboard']")
	WebElement dashboardText;

	@FindBy(xpath = "(//mat-select[contains(@id,'mat-select-')])[1]")
	WebElement userGroupDropDown;

	@FindBy(xpath = "//span[text()=' Sales Tax ']")
	WebElement salesTaxUserGroup;

	@FindBy(xpath = "(//mat-select[contains(@id,'mat-select-')])[2]")
	WebElement userDropDown;

	@FindBy(xpath = "//div[@id='mat-select-2-panel']//span[text()=' Anandajothi K Rajakannu ']")
	WebElement anandajothiUser;

	@FindBy(xpath = "//p[text()=' Open Claims ']/preceding-sibling::h3/span")
	WebElement openClaims;

	@FindBy(xpath = "//p[text()=' Aged Claims ']/preceding-sibling::h3/span")
	WebElement agedClaims;

	@FindBy(xpath = "//p[text()=' Aged Claims ']/input")
	WebElement daysOfAgedClaims;

	@FindBy(xpath = "//p[text()=' Priority Claims ']/preceding-sibling::h3/span")
	WebElement priorityClaims;

	@FindBy(xpath = "//p[text()=' Last Activity Age ']/preceding-sibling::h3/span")
	WebElement lastActivityAge;

	@FindBy(xpath = "//p[text()=' Last Activity Age ']/input")
	WebElement daysOfLastActivityAge;

	@FindBy(xpath = "//p[text()=' Debit Claims ']/preceding-sibling::h3/span")
	WebElement debitClaims;

	@FindBy(xpath = "//p[text()=' Completed vs Received ']/preceding-sibling::h3/span")
	WebElement completedVsReceived;

	@FindBy(xpath = "//div[@class='card p-2' and text()=' No Open Claims Available ']")
	WebElement alertMsgForOpenClaims;

	@FindBy(xpath = "//div[@class='apexcharts-tooltip apexcharts-theme-light apexcharts-active']//div//span[@class='apexcharts-tooltip-text-y-value']")
	WebElement individualGraphCount;

	@FindBy(xpath = "//h4[text()='Watchlist by Claims']/parent::div//a")
	WebElement watchListByClaims;

	@FindBy(xpath = "//td[text()='No claims on your watchlist.']")
	WebElement alertMsgInClaimsWatchlist;

	@FindBy(xpath = "//div[contains(text(),' You can not delete others watchclaim details ')]")
	WebElement errorMsgDeleteOthersWatchClaim;

	@FindBy(xpath = "//div[contains(text(),' Claim Deleted from Watchlist Successfully ')]")
	WebElement delCnfMsgWatchList;

	@FindBy(xpath = "//td[text()='No customers on your watchlist.']")
	WebElement alertMsgInCustomerWatchlist;

	@FindBy(xpath = "//td[text()='No overdue claims.']")
	WebElement alertMsgInOverdueWidget;

	@FindBy(xpath = "//td[text()='No claims due for today.']")
	WebElement alertMsgInClaimsDueWidget;

	@FindBy(xpath = "(//button[@class='btn btn-primary' and text()=' Delete '])[1]")
	WebElement deleteButton;

	@FindBy(xpath = "//simple-snack-bar//div[contains(text(),' Customer Removed from Watchlist Successfully')]")
	WebElement cutomerDelToasterMsg;

	public DashboardValidation() {
		PageFactory.initElements(driver, this);
	}

	public void validatingDashboardPage() {
		Assert.assertEquals(dashboardText.getText(), "Dashboard");
	}

	public void goToWorkQueuePage() {
		clickOn(workQueueIcon);
	}
	
	public void goToManageNoteTemplate() throws Throwable {
		Thread.sleep(15000);
		mouseHover(smartCodeIcon);
		clickOn(manageNoteTemplateSideNavMenu);
	}

	public void selectingUserGroupFromDropDown(String groupName) throws Exception {
		Thread.sleep(15000);
		clickOn(userGroupDropDown);
		clickOn(driver.findElement(By.xpath("//span[text()='" + groupName + "']")));
	}

	public void selectingUserFromDropDown(String userName) throws Exception {
		Thread.sleep(3000);
		clickOn(userDropDown);
		clickOn(driver.findElement(By.xpath("//div[contains(@id,'mat-select-')]//span[text()=' " + userName + " ']")));
		Thread.sleep(3000);
	}

	public int openClaimsCountInDashboard() throws Exception {
		Thread.sleep(15000);
		int openClaimCount = Integer.valueOf(extractText(openClaims));
		System.out.println("Open claims count in Dashboard page is " + openClaimCount);
		clickOn(openClaims);
		return openClaimCount;
	}

	public int openClaimsCountInDashboardForWorkStatus() throws Throwable {
		Thread.sleep(15000);
		int openClaimCount = Integer.valueOf(extractText(openClaims));
		System.out.println("Open claims count in Dashboard page is " + openClaimCount);
		return openClaimCount;
	}

	public int agedClaimsCountInDashboard() throws Exception {
		Thread.sleep(15000);
		int agedClaimCount = Integer.valueOf(extractText(agedClaims));
		System.out.println("Aged claims count in Dashboard page is " + agedClaimCount);
		clickOn(agedClaims);
		return agedClaimCount;
	}

	public int agedClaimsCountForRespectiveDaysInDashboard(String enterDays) throws Exception {
		Thread.sleep(15000);
		daysOfAgedClaims.clear();
		daysOfAgedClaims.sendKeys(enterDays);
		daysOfAgedClaims.sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		int agedClaimCount = Integer.valueOf(extractText(agedClaims));
		System.out.println("Aged claims count for " + enterDays + " days in Dashboard page is " + agedClaimCount);
		clickOn(agedClaims);
		return agedClaimCount;
	}

	public void compareAgedClaimsWithdays(String enterDays) throws Exception {
		Thread.sleep(15000);
		int agedClaimByDefault = Integer.valueOf(extractText(agedClaims));
		System.out.println("Aged claims count by default in Dashboard page is " + agedClaimByDefault);
		daysOfAgedClaims.clear();
		daysOfAgedClaims.sendKeys(enterDays);
		daysOfAgedClaims.sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		int agedClaimCount = Integer.valueOf(extractText(agedClaims));
		System.out.println("Aged claims count for " + enterDays + " days in Dashboard page is " + agedClaimCount);
	}

	public int priorityClaimsCountInDashboard() throws Exception {
		Thread.sleep(15000);
		int priorityClaimCount = Integer.valueOf(extractText(priorityClaims));
		System.out.println("Open claims count in Dashboard page is " + priorityClaimCount);
		clickOn(priorityClaims);
		return priorityClaimCount;
	}

	public int lastActivityAgeClaimsCountInDashboard() throws Exception {
		Thread.sleep(15000);
		int lastActivityAgeClaimsCountClaimCount = Integer.valueOf(extractText(lastActivityAge));
		System.out
				.println("Last activity age claims count in Dashboard page is " + lastActivityAgeClaimsCountClaimCount);
		clickOn(lastActivityAge);
		return lastActivityAgeClaimsCountClaimCount;
	}

	public int lastActivityAgeClaimsCountForRespectiveDaysInDashboard(String enterDays) throws Exception {
		Thread.sleep(15000);
		daysOfLastActivityAge.clear();
		daysOfLastActivityAge.sendKeys(enterDays);
		daysOfLastActivityAge.sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		int lastActivityAgeClaimsCountClaimCount = Integer.valueOf(extractText(lastActivityAge));
		System.out.println("Last activity aged claims count for " + enterDays + " days in Dashboard page is "
				+ lastActivityAgeClaimsCountClaimCount);
		clickOn(lastActivityAge);
		return lastActivityAgeClaimsCountClaimCount;
	}

	public void compareLastActivityAgeClaimsWithdays(String enterDays) throws Exception {
		Thread.sleep(15000);
		int lastActivityAgeClaimByDefault = Integer.valueOf(extractText(lastActivityAge));
		System.out.println(
				"Last activity aged claims count by default in Dashboard page is " + lastActivityAgeClaimByDefault);
		daysOfLastActivityAge.clear();
		daysOfLastActivityAge.sendKeys(enterDays);
		daysOfLastActivityAge.sendKeys(Keys.ENTER);
		Thread.sleep(5000);
		int lastActivityAgeClaimsCountClaimCount = Integer.valueOf(extractText(lastActivityAge));
		System.out.println("Last activity aged claims count for " + enterDays + " days in Dashboard page is "
				+ lastActivityAgeClaimsCountClaimCount);
	}

	public int debitClaimsCountInDashboard() throws Exception {
		Thread.sleep(15000);
		int debitClaimsCount = Integer.valueOf(extractText(debitClaims));
		System.out.println("Debit claims count in Dashboard page is " + debitClaimsCount);
		clickOn(debitClaims);
		return debitClaimsCount;
	}

	public int completedVsReceivedInDashboard() throws Exception {
		Thread.sleep(15000);
		int completedVsReceivedCount = Integer.valueOf(extractText(completedVsReceived));
		System.out.println("completed Vs Received count in Dashboard page is " + completedVsReceivedCount);
		clickOn(completedVsReceived);
		Thread.sleep(3000);
		List<WebElement> dashboardElement = driver.findElements(By.xpath("//h1[@class='mb-0' and text()='Dashboard']"));
		int dashboardTextStatus = dashboardElement.size();
		return dashboardTextStatus;
	}

	public void enablingAllCheckboxesInUserPreference() throws Throwable {
		Thread.sleep(15000);
		clickOn(userPreference);
		List<WebElement> cardClaimsList = driver.findElements(By.xpath("//input[@type='checkbox']"));
		for (WebElement e : cardClaimsList) {
			if (!e.isEnabled()) {
				driver.findElement(By.xpath("//input[@type='checkbox']")).click();
			}
		}
	}

	public void disablingDesiredCheckboxInUserPreference(String cardName) throws Throwable {
		Thread.sleep(15000);
		clickOn(userPreference);
		List<WebElement> cardClaimsList = driver.findElements(By.xpath("//label[@class='col-sm-3 col-form-label']"));
		for (WebElement e : cardClaimsList) {
			String text = e.getText();
			if (text.equals(cardName)) {
				driver.findElement(By.xpath("//label[text()='" + cardName + "']/parent::div//input")).click();
				break;
			}
		}
		Thread.sleep(2500);
		clickOn(applyBtnInUserPref);
	}

	public int checkingWidgetsInDashboard(String widgetName) throws Throwable {
		Thread.sleep(5000);
		String widgetsXpath = "//p[text()=' " + widgetName + " ']";
		List<WebElement> widget = driver.findElements(By.xpath(widgetsXpath));
		int size = widget.size();
		disablingDesiredCheckboxInUserPreference(widgetName);
		return size;

	}

	public int countOfOpenClaimsByWorkStatusIn2DChart() throws Exception {
		String graphXpath = "//*[local-name()='svg']//*[name()='g' and @class='apexcharts-series']//*[name()='path']";
		List<WebElement> barList = driver.findElements(By.xpath(graphXpath));
		int totalGraphCount = 0;
		for (WebElement e : barList) {
			mouseHover(e);
			Integer individualCount = Integer.valueOf(extractText(individualGraphCount));
			totalGraphCount = totalGraphCount + individualCount;
		}
		System.out.println("The count of open claims by work status in 2D chart is " + totalGraphCount);
		return totalGraphCount;
	}

	public String countOfIndividualClaimsByStatus() throws Throwable {
		Thread.sleep(8000);
		String noOfOpenClaims = openClaims.getText();
		if (noOfOpenClaims.equals("0")) {
			returnValueFromWidgets = " No Open Claims Available ";
			System.out.println("The Count open claims is " + noOfOpenClaims);
		} else {
			returnValueFromWidgets = noOfOpenClaims;
			String graphXpath = "//*[local-name()='svg']//*[name()='g' and @class='apexcharts-series']//*[name()='path']";
			String statusTextXpath = "//*[local-name()='svg']//*[name()='g' and @class='apexcharts-yaxis-texts-g apexcharts-xaxis-inversed-texts-g']//*[name()='title']";
			List<WebElement> barList = driver.findElements(By.xpath(graphXpath));
			List<WebElement> statusList = driver.findElements(By.xpath(statusTextXpath));
			System.out
					.println("************* COUNT OF OPEN CLAIMS IN DASHBOARD PAGE BY EACH WORK STATUS *************");
			for (int i = 0; i < barList.size(); i++) {
				mouseHover(barList.get(i));
				Integer individualCount = Integer.valueOf(extractText(individualGraphCount));
				String statusText = extractText(statusList.get(i));
				map.put(statusText, individualCount);
				System.out.println(statusText + " = " + individualCount);
			}
		}
		return returnValueFromWidgets;

	}

	public static LinkedHashMap<String, Integer> getMapValue() {
		return map;
	}

	public boolean userNameValidation() {
		boolean displayed = userName.isDisplayed();
		System.out.println(userName.getText());
		return displayed;
	}

	public String clickOnClaimNumberInWatchlistByClaims() {
		returnValueFromWidgets = null;
		String claimNumberInWatchlistByClaimsGenericXpath = "//h4[text()='Watchlist by Claims']/parent::div//a[@class='claim-link']";
		List<WebElement> allClaimNumbersInWatchlistByClaims = driver
				.findElements(By.xpath(claimNumberInWatchlistByClaimsGenericXpath));
		int max = allClaimNumbersInWatchlistByClaims.size();
		int min = 0;
		if (max != 0) {
			int claimNumberIndex = getRandomNumberUsingNextInt(min, max);
			int position = claimNumberIndex + 1;
			WebElement desiredClaimNumberFromWatchlistByClaims = allClaimNumbersInWatchlistByClaims
					.get(claimNumberIndex);
			returnValueFromWidgets = extractText(desiredClaimNumberFromWatchlistByClaims);
			System.out.println("The Claim number clicked in Watchlist by Customer widget at position " + position
					+ " is " + returnValueFromWidgets);
			clickOn(desiredClaimNumberFromWatchlistByClaims);
		} else {
			boolean noClaimsMsgStatus = alertMsgInClaimsWatchlist.isDisplayed();
			Assert.assertTrue(noClaimsMsgStatus);
			returnValueFromWidgets = alertMsgInClaimsWatchlist.getText();
			System.out.println(returnValueFromWidgets);

		}
		return returnValueFromWidgets;
	}

	public void countOfCustomersInWatchlistByCustomer() {
		String watchlistByCustomerGenericXpath = "//h4[text()='Watchlist by Customer']/parent::div//a";
		List<WebElement> allWatchlistByCustomer = driver.findElements(By.xpath(watchlistByCustomerGenericXpath));
		int size = allWatchlistByCustomer.size();
		if (size != 0) {
			System.out.println("The Count of Customer in Watchlist By Customer widget is " + size);
		} else {
			boolean noCustomerMsgStatus = alertMsgInCustomerWatchlist.isDisplayed();
			Assert.assertTrue(noCustomerMsgStatus);
			System.out.println(alertMsgInCustomerWatchlist.getText());
		}
	}

	public String clickOnClaimNumberInOverdue() {
		returnValueFromWidgets = null;
		String claimNumberInOverdueGenericXpath = "//h4[text()='overdue']/parent::div//a[@class='claim-link']";
		List<WebElement> allClaimNumbersInOverdue = driver.findElements(By.xpath(claimNumberInOverdueGenericXpath));
		int max = allClaimNumbersInOverdue.size();
		int min = 0;
		if (max != 0) {
			int claimNumberIndex = getRandomNumberUsingNextInt(min, max);
			int position = claimNumberIndex + 1;
			WebElement desiredClaimNumberFromOverdue = allClaimNumbersInOverdue.get(claimNumberIndex);
			returnValueFromWidgets = extractText(desiredClaimNumberFromOverdue);
			System.out.println("The Claim Number clicked in Overdue widget at position " + position + " is "
					+ returnValueFromWidgets);
			clickOn(desiredClaimNumberFromOverdue);
		} else {
			boolean noOverdueMsgStatus = alertMsgInOverdueWidget.isDisplayed();
			Assert.assertTrue(noOverdueMsgStatus);
			returnValueFromWidgets = alertMsgInOverdueWidget.getText();
			System.out.println(returnValueFromWidgets);
		}
		return returnValueFromWidgets;
	}

	public String clickOnClaimNumberInClaimsDue() {
		returnValueFromWidgets = null;
		String claimNumberInClaimDueGenericXpath = "//h4[text()='Claims Due']/parent::div//a[@class='claim-link']";
		List<WebElement> allClaimNumbersInClaimDue = driver.findElements(By.xpath(claimNumberInClaimDueGenericXpath));
		int max = allClaimNumbersInClaimDue.size();
		int min = 0;
		if (max != 0) {
			int claimNumberIndex = getRandomNumberUsingNextInt(min, max);
			int position = claimNumberIndex + 1;
			WebElement desiredClaimNumberFromClaimDue = allClaimNumbersInClaimDue.get(claimNumberIndex);
			returnValueFromWidgets = extractText(desiredClaimNumberFromClaimDue);
			System.out.println("The Claim number clicked in claims due widget at position " + position + " is "
					+ returnValueFromWidgets);
			clickOn(desiredClaimNumberFromClaimDue);
		} else {
			boolean noClaimDueMsgStatus = alertMsgInClaimsDueWidget.isDisplayed();
			Assert.assertTrue(noClaimDueMsgStatus);
			returnValueFromWidgets = alertMsgInClaimsDueWidget.getText();
			System.out.println(returnValueFromWidgets);
		}
		return returnValueFromWidgets;
	}

	public void createExcel() throws Throwable {
		List<WebElement> elements = driver.findElements(By.xpath("//mat-option[contains(@id,'mat-option-')]//span"));
		LinkedList<String> list = new LinkedList<String>();
		for (int i = 0; i < elements.size(); i++) {
			String text = elements.get(i).getText();
			list.add(i, text);
			System.out.println(list.get(i));
		}
		int size = list.size();
		XSSFWorkbook workBook = new XSSFWorkbook();
		XSSFSheet sheet = workBook.createSheet("WriteData");

		for (int r = 0; r < size; r++) {
			XSSFRow row = sheet.createRow(r);
			XSSFCell cell = row.createCell(1);
			Object value = list.get(r);
			if (value instanceof String)
				cell.setCellValue((String) value);
			if (value instanceof Integer)
				cell.setCellValue((Integer) value);
			if (value instanceof Boolean)
				cell.setCellValue((Boolean) value);
		}
		String filePath = System.getProperty("user.dir") + "/src/test/resources/UserList.xlsx";
		FileOutputStream fos = new FileOutputStream(filePath);
		workBook.write(fos);
		workBook.close();
	}

	public void deleteClaimsInWatchlistByClaimsWidget() throws Throwable {
		String widgetGenericXpath = "//h4[text()='Watchlist by Claims']/parent::div//i";
		String claimNumbersGenericXpathInWidget = "//h4[text()='Watchlist by Claims']/parent::div//a[@class='claim-link']";
		List<WebElement> allClaimsInDesiredWidget = driver.findElements(By.xpath(widgetGenericXpath));
		List<WebElement> allClaimNumbersInDesiredWidget = driver
				.findElements(By.xpath(claimNumbersGenericXpathInWidget));
		int max = allClaimsInDesiredWidget.size();
		int min = 0;
		if (max != 0) {
			int claimNumberIndex = getRandomNumberUsingNextInt(min, max);
			int position = claimNumberIndex + 1;
			String claimNumberFromDesiredWidget = extractText(allClaimNumbersInDesiredWidget.get(claimNumberIndex));
			WebElement desiredClaimToDeleteFromWidget = allClaimsInDesiredWidget.get(claimNumberIndex);
			clickOn(desiredClaimToDeleteFromWidget);
			clickOn(deleteButton);
			List<WebElement> delErrMsgForOtherUserClaims = driver
					.findElements(By.xpath("//div[contains(text(),' You can not delete others watchclaim details ')]"));
			if (delErrMsgForOtherUserClaims.size() == 1) {
				System.out.println(errorMsgDeleteOthersWatchClaim.getText());
				
			} else {
				List<WebElement> deletedClaimNumberInDesiredWidget = driver
						.findElements(By.xpath("//h4[text()='Watchlist by Claims']/parent::div//a[text()='"
								+ claimNumberFromDesiredWidget + "']"));
				int deletedClaimNumberInDesiredWidgetSize = deletedClaimNumberInDesiredWidget.size();
				Assert.assertEquals(deletedClaimNumberInDesiredWidgetSize, 0);
				System.out.println("The Claim number " + claimNumberFromDesiredWidget + " at position " + position
						+ " is removed successfully from Watchlist by Claims");
			}
		} else {
			boolean noClaimsMsgStatus = alertMsgInClaimsWatchlist.isDisplayed();
			Assert.assertTrue(noClaimsMsgStatus);
			System.out.println(alertMsgInClaimsWatchlist.getText());
		}
	}

	public void deleteClaimsInWatchlistByCustomerWidget() throws Throwable {
		String deleteGenericXpath = "//h4[text()='Watchlist by Customer']/parent::div//i";
		List<WebElement> allDeleteIcons = driver.findElements(By.xpath(deleteGenericXpath));
		int max = allDeleteIcons.size();
		int min = 0;
		if (max != 0) {
			int customerNumberIndex = getRandomNumberUsingNextInt(min, max);
			int position = customerNumberIndex + 1;
			String customerNumber = extractText(driver.findElement(By.xpath(
					"(//h4[text()='Watchlist by Customer']/parent::div//i)[" + position + "]/ancestor::tr//td")));
			String customerName = extractText(driver.findElement(By.xpath(
					"(//h4[text()='Watchlist by Customer']/parent::div//i)[" + position + "]/ancestor::tr//td[2]")));
			WebElement desiredCustomerToDelete = allDeleteIcons.get(customerNumberIndex);
			clickOn(desiredCustomerToDelete);
			clickOn(driver.findElement(By.xpath(
					"//p[text()='Are you sure want to delete Customer WatchList?']/ancestor::div[@class='modal-body']//following-sibling::div//button[text()=' Delete ']")));
			Thread.sleep(3000);
			boolean toasterMsgStatus = cutomerDelToasterMsg.isDisplayed();
			Assert.assertTrue(toasterMsgStatus);
			System.out.println("The Customer number " + customerNumber + " with customer name " + customerName
					+ " is deleted successfully from watchlist by customer widget");
		} else {
			boolean noCustomerMsgStatus = alertMsgInCustomerWatchlist.isDisplayed();
			Assert.assertTrue(noCustomerMsgStatus);
			System.out.println(alertMsgInCustomerWatchlist.getText());
		}
	}

}