package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class SCHLoginStep2Page {
	private final WebDriver driver;

	@FindBy(how = How.CSS, using = "input[type='submit']")
	private WebElement submit;
	private WebElement tf7_ims;

	public SCHLoginStep2Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, submit);
	}

	public SCHInitialPage loginStep2(Usuari usuari) {

		tf7_ims.clear();
		tf7_ims.sendKeys(usuari.getIms());
		submit.submit();

		return PageFactory.initElements(driver, SCHInitialPage.class);
	}
}
