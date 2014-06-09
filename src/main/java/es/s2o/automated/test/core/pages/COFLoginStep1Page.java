package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.Browser;
import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * @author s2o
 */
public class COFLoginStep1Page {

	private final WebDriver driver;

	private WebElement j_userSignOn;
	private WebElement j_userSignOnDC;
	private WebElement signon;

	// Abrir en modo demostracin
	@FindBy(how = How.PARTIAL_LINK_TEXT, using = "Abrir en modo")
	private WebElement modoMemo;

	public COFLoginStep1Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, signon);
	}

	public COFInitialPage loginAsDemo(Config config) {

		String code = "document.getElementById('signon_form_tf7_demo').value = 'true';";
		code = code + "function showModoDemo(msg,dur){var el = document.createElement('div');"
				+ "el.setAttribute('style','position:absolute;top:50%;left:50%;background-color:white;');"
				+ "el.innerHTML = msg;"
				+ "setTimeout(function(){el.parentNode.removeChild(el);},dur);document.body.appendChild(el);}";
		code = code + " showModoDemo('Modo demo',500);";

		WebDriverShortcuts.executeJSWithNoReturn(driver, code);

		if (Browser.IEXPLORER.name().equals(config.getString(AbsisConstants.WEBDRIVER_BROWSER))) {
			WebDriverShortcuts.waitAMomentPlease();
		}

		code = "document.getElementById('login').submit();return true;";
		WebDriverShortcuts.executeJSWithNoReturn(driver, code);

		if (Browser.IEXPLORER.name().equals(config.getString(AbsisConstants.WEBDRIVER_BROWSER))) {
			WebDriverShortcuts.waitAMomentPlease();
		}

		return PageFactory.initElements(driver, COFInitialPage.class);
	}

	public COFLoginStep2Page loginStep1(Usuari usuari) {

		j_userSignOn.clear();
		j_userSignOn.sendKeys(usuari.getUsername());

		j_userSignOnDC.clear();
		j_userSignOnDC.sendKeys(usuari.getUsernameDC());
		signon.submit();

		return PageFactory.initElements(driver, COFLoginStep2Page.class);
	}
}
