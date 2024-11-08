package com.shawclaimx.ui.claimdetails.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.shawclaimx.ui.dashboard.pages.DashboardValidation;
import com.shawclaimx.ui.dataSetup.UtilitySetup;

public class ClaimDetailsValidation extends UtilitySetup {

	DashboardValidation dashboard;

	@FindBy(xpath = "//div[@class='card-box-10 py-20 px-20 d-flex flex-wrap align-items-center inactive-card']//h4")
	WebElement claimNumber;

	public ClaimDetailsValidation() {
		PageFactory.initElements(driver, this);
	}

	public String claimNumberInClaimDetailsPage() throws Throwable {
		dashboard = new DashboardValidation();
		String returnValueFromDashboard = dashboard.returnValueFromWidgets;
		String claimsWatchlistAlertMsg = "No claims on your watchlist.";
		String overDueAlertMsg = "No overdue claims.";
		String ClaimsDueAlertMsg = "No claims due for today.";
		Thread.sleep(3000);
		String claimNumberInClaimDetail = null;
		if (returnValueFromDashboard.equalsIgnoreCase(claimsWatchlistAlertMsg)
				|| returnValueFromDashboard.equalsIgnoreCase(overDueAlertMsg)
				|| returnValueFromDashboard.equalsIgnoreCase(ClaimsDueAlertMsg)) {
			claimNumberInClaimDetail = returnValueFromDashboard;
		} else {
			claimNumberInClaimDetail = extractText(claimNumber).substring(1);
			System.out.println("The Claim Number in Claim Details Page is " + claimNumberInClaimDetail);
		}
		return claimNumberInClaimDetail;
	}
}
