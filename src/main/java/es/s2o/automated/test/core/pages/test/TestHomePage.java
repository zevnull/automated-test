package es.s2o.automated.test.core.pages.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * @author s2o
 */
public class TestHomePage {

	private final WebDriver driver;

	@FindBy(how = How.LINK_TEXT, using = "Define Custom Fields")
	WebElement customFields;

	public TestHomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, customFields);
	}

	public CustomFieldsPage goToCustomFields() {
		customFields.click();
		return PageFactory.initElements(driver, CustomFieldsPage.class);
	}
}
