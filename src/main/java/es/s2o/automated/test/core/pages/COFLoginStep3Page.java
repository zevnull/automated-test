package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class COFLoginStep3Page {

	private final WebDriver driver;

	private WebElement j_username;
	private WebElement j_usernameDC;
	private WebElement j_password;
	private WebElement oficinaContable;
	private WebElement empresaContable;
	private WebElement baceptar;

	public COFLoginStep3Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, oficinaContable);
	}

	public COFLoginStep4Page loginStep3(Usuari usuari) {

		j_username.clear();
		j_username.sendKeys(usuari.getUsername());

		j_usernameDC.clear();
		j_usernameDC.sendKeys(usuari.getUsernameDC());

		j_password.clear();
		j_password.sendKeys(usuari.getPasswordAGS());

		Select select = new Select(empresaContable);
		select.selectByVisibleText("CaixaBank");

		oficinaContable.clear();
		oficinaContable.sendKeys(usuari.getOficinaContable());

		baceptar.submit();

		return PageFactory.initElements(driver, COFLoginStep4Page.class);
	}
}
