package com.shawclaimx.ui.notetemplate.tests;

import static org.testng.Assert.assertEquals;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.shawclaimx.ui.dashboard.pages.DashboardValidation;
import com.shawclaimx.ui.dataSetup.UtilitySetup;
import com.shawclaimx.ui.notetemplate.pages.ManageNoteTemplatePage;

public class ManageNoteTemplateTest extends UtilitySetup {
	
	DashboardValidation dashboard;
	ManageNoteTemplatePage manageNoteTemplate;
	
	@Test
	public void TC_001_ValidatingViewNoteTemplatePage() throws Throwable {
		dashboard = new DashboardValidation();
		manageNoteTemplate = new ManageNoteTemplatePage();
		dashboard.goToManageNoteTemplate();
		String actualText = manageNoteTemplate.validateViewNoteTemplatePage();
		Assert.assertEquals(actualText, "View Note Template");
	}
	
	@Test
	public void TC_002_ValidatingAddNoteNoteFunctionality() throws Throwable {
		dashboard = new DashboardValidation();
		manageNoteTemplate = new ManageNoteTemplatePage();
		dashboard.goToManageNoteTemplate();
		manageNoteTemplate.addNoteTemplate();
	}
	
	@Test
	public void TC_003_ValidatingSaveButtonStatus() throws Throwable {
		dashboard = new DashboardValidation();
		manageNoteTemplate = new ManageNoteTemplatePage();
		dashboard.goToManageNoteTemplate();
		manageNoteTemplate.saveButtonStatus();
	}
	
	@Test
	public void TC_004_ValidatingMultipleNoteTemplateWithSameName() throws Throwable {
		dashboard = new DashboardValidation();
		manageNoteTemplate = new ManageNoteTemplatePage();
		dashboard.goToManageNoteTemplate();
		manageNoteTemplate.addNoteTemplate();
		int templateNames = manageNoteTemplate.filter();
		Assert.assertEquals(templateNames, 1);
	}
	
	@Test
	public void TC_005_ValidatingEditNoteTemplateFunctionality() throws Throwable {
		dashboard = new DashboardValidation();
		manageNoteTemplate = new ManageNoteTemplatePage();
		dashboard.goToManageNoteTemplate();
		manageNoteTemplate.addNoteTemplate();
		manageNoteTemplate.filter();
		int editedNoteTemplate = manageNoteTemplate.editNoteTemplate();
		Assert.assertEquals(editedNoteTemplate, 1);
	}
	
	@Test
	public void TC_006_ValidatingDeleteNoteTemplateFunctionality() throws Throwable {
		dashboard = new DashboardValidation();
		manageNoteTemplate = new ManageNoteTemplatePage();
		dashboard.goToManageNoteTemplate();
		manageNoteTemplate.addNoteTemplate();
		manageNoteTemplate.filter();
		Assert.assertEquals(manageNoteTemplate.deleteNoteTemplate(), true);
	}
}
