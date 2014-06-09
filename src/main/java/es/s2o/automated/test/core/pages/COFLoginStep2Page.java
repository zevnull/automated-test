package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class COFLoginStep2Page {

	private final WebDriver driver;

	private WebElement j_passwordSignOn;
	private WebElement signon;

	public COFLoginStep2Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver,  j_passwordSignOn);
	}

	public COFLoginStep3Page loginStep2(Usuari usuari) {

		j_passwordSignOn.clear();
		j_passwordSignOn.sendKeys(usuari.getPasswordIMS());

		signon.submit();

		return PageFactory.initElements(driver, COFLoginStep3Page.class);
	}
}
