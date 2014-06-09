package es.s2o.automated.test.core.pages.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * @author s2o
 */
public class CustomFieldsPage {

	private final WebDriver driver;

	private WebElement item_view;

	public CustomFieldsPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isThisTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, item_view);
	}

	public TestHomePage login() {
		// login.sendKeys("admin");
		// tl_password.sendKeys("admin");
		// login_submit.submit();
		return PageFactory.initElements(driver, TestHomePage.class);
	}

}
