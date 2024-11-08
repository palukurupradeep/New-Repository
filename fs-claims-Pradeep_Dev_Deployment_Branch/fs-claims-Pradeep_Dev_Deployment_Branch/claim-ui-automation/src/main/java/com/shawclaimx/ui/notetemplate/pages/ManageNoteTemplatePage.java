package com.shawclaimx.ui.notetemplate.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.shawclaimx.ui.dataSetup.UtilitySetup;

public class ManageNoteTemplatePage extends UtilitySetup {
	
	public String time = getTime();
	
	String noteType = " PRIORITY NOTE ";
	String templateName = "Template Name "+time;
	String noteTemplateText = "Note Template Text "+time;
	String auditHistoryTemplateText ="Audit History Template Text "+time;

	String templateNameToEdit = "Template Name Edited "+time;
	String noteTemplateTextToEdit = "Note Template Text Edited "+time;
	String auditHistoryTemplateTextToEdit = "Audit History Template Text Edited "+time;
	
	@FindBy(xpath = "//div[@class='page-head']//h1")
	WebElement pageHeadText;

	@FindBy(xpath = "//button[@data-bs-target='#addnotes']")
	WebElement addNoteTemplateBtn;

	@FindBy(xpath = "//tr[2]//th[2]")
	WebElement noteGroupFilterBox;

	@FindBy(xpath = "//tr[2]//th[3]")
	WebElement noteTypeFilterBox;

	@FindBy(xpath = "//tr[2]//th[4]//input")
	WebElement templateNameFilterBox;

	@FindBy(xpath = "//tr[2]//th[7]")
	WebElement templateTextFilterBox;

	@FindBy(xpath = "(//i[@class='fa-solid fa-pencil'])[2]")
	WebElement editIcon;

	@FindBy(xpath = "(//i[@class='fa-solid fa-trash-can'])[2]")
	WebElement deleteIcon;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//following-sibling::button[@class='btn-close']")
	WebElement closeBtnInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//select")
	WebElement noteTypeDropdownInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='noteGroup']")
	WebElement noteGroupInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='templateName']")
	WebElement templateNameTxtBoxInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//textarea[@formcontrolname='templateText']")
	WebElement noteTemplateTxtBoxInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='editable']")
	WebElement editableToggleBtnInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='default']")
	WebElement defaultToggleBtnInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='manual']")
	WebElement manualToggleBtnInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//textarea[@formcontrolname='auditHistorytemplateText']")
	WebElement auditHistoryTemplateTxtBoxInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//p[@class='text-danger note-temp-manadatory']")
	WebElement mandatoryWarningMsgInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//button[@class='btn btn-secondary']")
	WebElement cancelBtnInAddNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Add Note Template ']//parent::div//following-sibling::div//button[@class='btn btn-primary']")
	WebElement saveBtnInAddNoteTemplate;

	@FindBy(xpath = "//div[text()=' Note Template Added successfully. ']")
	WebElement noteTemplateAddedSuccessToast;
	
	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//following-sibling::button[@class='btn-close']")
	WebElement closeBtnInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//select")
	WebElement noteTypeDropdownInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='editnoteGroup']")
	WebElement noteGroupInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='edittemplateName']")
	WebElement templateNameTxtBoxInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//textarea[@formcontrolname='edittemplateText']")
	WebElement noteTemplateTxtBoxInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='editeditable']")
	WebElement editableToggleBtnInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='editdefault']")
	WebElement defaultToggleBtnInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//input[@formcontrolname='editmanual']")
	WebElement manualToggleBtnInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//textarea[@formcontrolname='editauditHistorytemplateText']")
	WebElement auditHistoryTemplateTxtBoxInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//p[@class='text-danger note-temp-manadatory']")
	WebElement mandatoryWarningMsgInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//button[@class='btn btn-secondary']")
	WebElement cancelBtnInEditNoteTemplate;

	@FindBy(xpath = "//h5[text()=' Edit Note Template ']//parent::div//following-sibling::div//button[@class='btn btn-primary']")
	WebElement updateBtnInEditNoteTemplate;

	@FindBy(xpath = "//div[text()=' Note Template Updated successfully. ']")
	WebElement noteTemplateUpdatedSuccessToast;

	@FindBy(xpath = "//h5[@id='deleteModalLabel']//following-sibling::button[@class='btn-close']")
	WebElement closeBtnInDeleteNoteTemplate;

	@FindBy(xpath = "//h5[@id='deleteModalLabel']//parent::div//following-sibling::div//p")
	WebElement deleteNoteTemplateConfirmationMsg;

	@FindBy(xpath = "//h5[@id='deleteModalLabel']//parent::div//following-sibling::div[@class='modal-footer']//button[@class='btn btn-primary']")
	WebElement deleteBtnInDeleteNoteTemplate;

	@FindBy(xpath = "//h5[@id='deleteModalLabel']//parent::div//following-sibling::div[@class='modal-footer']//button[@class='btn btn-secondary']")
	WebElement cancelBtnInDeleteNoteTemplate; 
	
	@FindBy(xpath = "//div[text()=' Note Template Deleted successfully.. ']")
	WebElement noteTemplateDeletedSuccessToast;
	
	@FindBy(xpath = "//p[text()='NO DATA AVAILABLE']")
	WebElement noDataAvailableAfterDeleteingNoteTemplate;

	public ManageNoteTemplatePage() {
		PageFactory.initElements(driver, this);
	}

	public String validateViewNoteTemplatePage() {
		return extractText(pageHeadText);
	}
	
	public void addNoteTemplate() {
		clickOn(addNoteTemplateBtn);
		selectByVisibleText(noteTypeDropdownInAddNoteTemplate,noteType);
		type(templateNameTxtBoxInAddNoteTemplate, templateName);
		type(noteTemplateTxtBoxInAddNoteTemplate, noteTemplateText);
		clickOn(editableToggleBtnInAddNoteTemplate);
		type(auditHistoryTemplateTxtBoxInAddNoteTemplate, auditHistoryTemplateText);
		clickOn(saveBtnInAddNoteTemplate);
		Assert.assertTrue(noteTemplateAddedSuccessToast.isDisplayed());
	}
	
	public void saveButtonStatus() {
		clickOn(addNoteTemplateBtn);
		selectByVisibleText(noteTypeDropdownInAddNoteTemplate,noteType);
		type(templateNameTxtBoxInAddNoteTemplate, templateName);
		Assert.assertFalse(saveBtnInAddNoteTemplate.isEnabled());
		type(noteTemplateTxtBoxInAddNoteTemplate, noteTemplateText);
		Assert.assertTrue(saveBtnInAddNoteTemplate.isEnabled());
	}
	
	public int filter() throws Throwable {
		Thread.sleep(3000);
		templateNameFilterBox.clear();
		type(templateNameFilterBox, templateName);
		templateNameFilterBox.clear();
		type(templateNameFilterBox, templateName);
		Thread.sleep(3000);
		templateNameFilterBox.sendKeys(Keys.ENTER);
		List<WebElement> templateNames = driver.findElements(By.xpath("//td[text()='"+templateName+"']"));
		return templateNames.size();
	}
	
	public int editNoteTemplate() throws Throwable {
		String editIconXpath="//td[text()='"+templateName+"']//preceding-sibling::td//i[@class='fa-solid fa-pencil']";
		WebElement editIconForDesiredTemplate = driver.findElement(By.xpath(editIconXpath));
		clickOn(editIconForDesiredTemplate);
		selectByVisibleText(noteTypeDropdownInEditNoteTemplate," PRIORITY NOTE ");
		clearText(templateNameTxtBoxInEditNoteTemplate);
		type(templateNameTxtBoxInEditNoteTemplate, templateNameToEdit);
		clearText(noteTemplateTxtBoxInEditNoteTemplate);
		type(noteTemplateTxtBoxInEditNoteTemplate, noteTemplateTextToEdit);
		clickOn(defaultToggleBtnInEditNoteTemplate);
		clearText(auditHistoryTemplateTxtBoxInEditNoteTemplate);
		type(auditHistoryTemplateTxtBoxInEditNoteTemplate, auditHistoryTemplateTextToEdit);
		clickOn(updateBtnInEditNoteTemplate);
		Assert.assertTrue(noteTemplateUpdatedSuccessToast.isDisplayed());
		templateNameFilterBox.clear();
		type(templateNameFilterBox, templateNameToEdit);
		templateNameFilterBox.clear();
		type(templateNameFilterBox, templateNameToEdit);
		Thread.sleep(3000);
		templateNameFilterBox.sendKeys(Keys.ENTER);
		List<WebElement> templateNames = driver.findElements(By.xpath("//td[text()='"+templateNameToEdit+"']"));
		return templateNames.size();
	}
	
	public boolean deleteNoteTemplate() throws Throwable {
		String deleteIconXpath="//td[text()='"+templateName+"']//preceding-sibling::td//i[@class='fa-solid fa-trash-can']";
		WebElement deleteIconForDesiredTemplate = driver.findElement(By.xpath(deleteIconXpath));
		clickOn(deleteIconForDesiredTemplate);
		clickOn(deleteBtnInDeleteNoteTemplate);
		Assert.assertTrue(noteTemplateDeletedSuccessToast.isDisplayed());
		filter();
		return noDataAvailableAfterDeleteingNoteTemplate.isDisplayed();
	}
}