package es.s2o.automated.test.core.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class LOEErrorPage {

	private final WebDriver driver;

	@FindBy(how = How.CLASS_NAME, using = "sinmargin")
	private List<WebElement> errorMessage;

	@FindBy(how = How.ID, using = "lo_contenido")
	private WebElement contenidorGeneral;

	public LOEErrorPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean isThisTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, errorMessage.get(0));
	}

	public boolean pageContains(String text) {
		return WebDriverShortcuts.isTextPresent(driver, text);
	}

	public boolean isTextOnFirstMissagte(String errorText) {
		return errorMessage.get(0).getText().contains(errorText);
	}

	public boolean isTextOnSecondMissagte(String errorText) {
		return errorMessage.get(1).getText().contains(errorText);
	}

	public boolean isTextOnThirdMissagte(String errorText) {
		return errorMessage.get(2).getText().contains(errorText);
	}

	public boolean isTextOnContenidorGeneral(String errorText) {
		return contenidorGeneral.getText().contains(errorText);
	}

}
