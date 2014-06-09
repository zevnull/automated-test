package es.s2o.automated.test.core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class SCHInitialPage {

	private final WebDriver driver;

	public SCHInitialPage(WebDriver pDriver) {
		driver = pDriver;
		PageFactory.initElements(driver, this);
	}

	public boolean isThisTheActualPage(String idValidation) {
		// Si vamos demasiado deprisa puede dar un casque
		By titolAplicacio = By.id(idValidation);
		return WebDriverShortcuts.explicitWait(driver, titolAplicacio);
	}

	public boolean pageContains(String text) {
		return WebDriverShortcuts.isTextPresent(driver, text);
	}

}
