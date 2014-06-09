package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * @author s2o
 */
public class COFErrorPage {

	private final WebDriver driver;

	private WebElement error;

	// Vigilar: Es probable que algunas pantallas lo tengan con div.missatge-error. Crear nueva p�gina si es necesario.
	@FindBy(how = How.CLASS_NAME, using = "missatge-error")
	private WebElement missatgeError;

	@FindBy(how = How.ID, using = "contenidor-general")
	private WebElement contenidorGeneral;

	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Datos")
	private WebElement datosTecnicosLink;

	public COFErrorPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public boolean isThisTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, error);
	}

	public boolean pageContains(String text) {
		return WebDriverShortcuts.isTextPresent(driver, text);
	}

	/**
	 * @param errorText
	 * @return
	 */
	public boolean isTextOnMissagte(String errorText) {
		return missatgeError.getText().contains(errorText);
	}

	/**
	 * @param errorText
	 * @return
	 */
	public boolean isTextOnContenidorGeneral(String errorText) {
		return contenidorGeneral.getText().contains(errorText);
	}

	public COFErrorPage showDatosTecnicos() {
		datosTecnicosLink.click();
		return this;
	}
}
