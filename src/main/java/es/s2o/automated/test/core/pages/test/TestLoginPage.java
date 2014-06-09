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
public class TestLoginPage {

	private final WebDriver driver;

	private WebElement login;

	@FindBy(how = How.NAME, using = "tl_password")
	private WebElement tl_password;

	@FindBy(how = How.NAME, using = "login_submit")
	private WebElement login_submit;

	public TestLoginPage(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, login);
	}

	public TestHomePage login() {
		login.sendKeys("admin");
		tl_password.sendKeys("admin");
		login_submit.submit();
		return PageFactory.initElements(driver, TestHomePage.class);
	}

}
