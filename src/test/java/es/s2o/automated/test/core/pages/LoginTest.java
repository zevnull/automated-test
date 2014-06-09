package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.annotation.SeleniumWebDriver;
import es.s2o.automated.test.core.pages.test.CustomFieldsPage;
import es.s2o.automated.test.core.pages.test.TestHomePage;

@Test
public class LoginTest {

	@SeleniumWebDriver
	private WebDriver driver;

	public void loginHomeTest() {
		// WebDriverShortcuts.switchToFrame(driver, "mainframe");
		CustomFieldsPage customPage = new TestHomePage(driver).goToCustomFields();
		Assert.assertTrue(customPage.isThisTheActualPage(), "Validando serviciosHome");
	}

}
