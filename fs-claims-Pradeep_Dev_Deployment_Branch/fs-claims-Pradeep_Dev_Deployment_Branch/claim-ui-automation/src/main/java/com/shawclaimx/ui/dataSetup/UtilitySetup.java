package com.shawclaimx.ui.dataSetup;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;

import com.shawclaimx.ui.utils.Utils;

public class UtilitySetup extends Utils {
	@BeforeMethod
	public void start() throws Exception {
		launchBrowser(readProperty("browser"));
		getApplication(readProperty("url"));
	}

	@DataProvider
	public Object[][] getExcelData() throws Exception {
		Object[][] data = dataReader(sheetName);
		return data;
	}

	@AfterMethod
	public void close() throws Exception {
		driver.quit();
	}
}