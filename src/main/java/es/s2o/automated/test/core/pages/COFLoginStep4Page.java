package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class COFLoginStep4Page {

	private final WebDriver driver;

	private WebElement tf7_ims;
	private WebElement login;

	public COFLoginStep4Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, tf7_ims);
	}

	public COFInitialPage loginStep4(Usuari usuari) {

		tf7_ims.clear();
		tf7_ims.sendKeys(usuari.getIms());

		login.submit();

		return PageFactory.initElements(driver, COFInitialPage.class);
	}
}
