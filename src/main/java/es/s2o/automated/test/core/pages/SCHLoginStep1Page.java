package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class SCHLoginStep1Page {
	private final WebDriver driver;

	@FindBy(how = How.XPATH, using = "//input[@type=\"submit\"]")
	private WebElement submit;

	private WebElement j_username;
	private WebElement j_password;

	public SCHLoginStep1Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, submit);
	}

	public SCHLoginStep2Page loginStep1PREV(Usuari usuari) {

		j_username.clear();
		j_username.sendKeys(usuari.getUsername());

		j_password.clear();
		j_password.sendKeys(usuari.getPasswordIMS());
		submit.submit();

		return PageFactory.initElements(driver, SCHLoginStep2Page.class);
	}

	public SCHInitialPage loginStep1PRO(Usuari usuari) {

		j_username.clear();
		j_username.sendKeys(usuari.getUsername());

		j_password.clear();
		j_password.sendKeys(usuari.getPasswordIMS());
		submit.submit();

		return PageFactory.initElements(driver, SCHInitialPage.class);
	}
}
