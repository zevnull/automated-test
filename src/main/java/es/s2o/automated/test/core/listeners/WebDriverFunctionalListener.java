package es.s2o.automated.test.core.listeners;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.AbstractWebDriverEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.Browser;
import es.s2o.automated.test.core.testng.ReportManagemer;

/**
 * @author s2o
 */
public class WebDriverFunctionalListener extends AbstractWebDriverEventListener {

	private static final Logger LOG = LoggerFactory.getLogger(WebDriverFunctionalListener.class);

	/*
	 * URL NAVIGATION | NAVIGATE() & GET()
	 */
	// Prints the URL before Navigating to specific URL "get("http://www.google.com");"
	@Override
	public void beforeNavigateTo(String url, WebDriver driver) {
		LOG.debug("Before Navigating To : " + url + ", my url was: " + driver.getCurrentUrl());

		if (AbsisConfigFactory.getConfig().getBoolean(AbsisConstants.WEBDRIVER_RECORD_ALL)) {
			ReportManagemer.getInstance().takeScreenShoot(driver);
		}
	}

	/*
	 * ON EXCEPTION | SCREENSHOT, THROWING ERROR
	 */
	// Takes screenshot on any Exception thrown during test execution
	@Override
	public void onException(Throwable throwable, WebDriver driver) {
		LOG.error("------------------------------------------------------");
		LOG.error("Caught Exception:" + throwable.getMessage());
		LOG.error("------------------------------------------------------");
		ReportManagemer.getInstance().addError(throwable.getMessage());
		if (driver != null) {
			ReportManagemer.getInstance().takeScreenShoot(driver);
		}
	}

	/*
	 * CLICK | CLICK()
	 */
	// Called before clicking an Element
	@Override
	public void beforeClickOn(WebElement element, WebDriver driver) {
		LOG.debug("Trying to click: '" + element + "'");

		// if (typeof(InternetExplorerDriver) == driver.)
		// {
		// element.SendKeys(Keys.Enter);
		// }
		// else
		// element.sendKeys(org.openqa.selenium.Keys.ENTER);
		if (Browser.IEXPLORER.name().equals(AbsisConfigFactory.getConfig().getString(AbsisConstants.WEBDRIVER_BROWSER))) {
			element.sendKeys("");
			element.sendKeys(org.openqa.selenium.Keys.CONTROL);
		}

		// element.sendKeys(Keys.SPACE);
		// driver.switchTo().window(driver.getWindowHandle());
		if (AbsisConfigFactory.getConfig().getBoolean(AbsisConstants.WEBDRIVER_RECORD_ALL)) {
			// Highlight Elements before clicking
			// for (int i = 0; i < 1; i++) {
			// JavascriptExecutor js = (JavascriptExecutor) driver;
			// js.executeScript("arguments[0].style.color='green';arguments[0].style.border='3px solid green;'",
			// element);
			// }
			ReportManagemer.getInstance().takeScreenShoot(driver);
		}
	}
}
