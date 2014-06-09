package es.s2o.automated.test.annotation;

import static org.testng.AssertJUnit.assertTrue;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.annotation.SeleniumWebDriver;
import es.s2o.automated.test.core.listeners.WebDriverSuiteListener;
import es.s2o.automated.test.core.listeners.WebDriverTestListener;

@Test
@Listeners({ WebDriverTestListener.class, WebDriverSuiteListener.class })
public class TestClassDriverDisabled2 {

	@SeleniumWebDriver
	private WebDriver driver;

	public void assertClassDriverUninitialized2() {
		assertTrue(driver != null);
	}
}
