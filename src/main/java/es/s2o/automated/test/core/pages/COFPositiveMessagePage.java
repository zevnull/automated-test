package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class COFPositiveMessagePage {

	private final WebDriver driver;

	@FindBy(how = How.CLASS_NAME, using = "missatge-feedback-positiu")
	private WebElement mensajePositivo;

	@FindBy(how = How.ID, using = "contenidor-general")
	private WebElement contenidorGeneral;

	public COFPositiveMessagePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean isThisTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, mensajePositivo);
	}

	public boolean pageContains(String text) {
		return WebDriverShortcuts.isTextPresent(driver, text);
	}

	public boolean isTextOnMissagte(String text) {
		return mensajePositivo.getText().contains(text);
	}

	public boolean isTextOnContenidorGeneral(String errorText) {
		return contenidorGeneral.getText().contains(errorText);
	}
}
