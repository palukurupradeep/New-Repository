package com.shawclaimx.ui.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportShawClaim {
	public static ExtentReports getReportObject() {
		ExtentReports extent=new ExtentReports();
		String path= System.getProperty("user.dir")+"/Extent_Reports/index.html";
		ExtentSparkReporter reporter=new ExtentSparkReporter(path);
		reporter.config().setReportName("Shaw Claim 3D Application");
		reporter.config().setDocumentTitle("Shaw Claim X");
		
		extent.attachReporter(reporter);
		extent.setSystemInfo("Performed By : ", "QA Testing Team");
		return extent;
	}
}
