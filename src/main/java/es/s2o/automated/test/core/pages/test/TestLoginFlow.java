package es.s2o.automated.test.core.pages.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.pages.ILoginPage;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

public class TestLoginFlow implements ILoginPage {
	private static final Logger LOG = LoggerFactory.getLogger(TestLoginFlow.class);

	@Override
	public boolean login(WebDriver driver, Config config, String urlBaseEntorno) {
		String login = urlBaseEntorno + config.getString(AbsisConstants.LOGIN_PAGE);
		LOG.info("Starting login to {}", login);

		driver.navigate().to(login);

		if (config.getBoolean(AbsisConstants.WEBDRIVER_SHUTDOWN) == false) {
			WebDriverShortcuts.executeJS(driver, "alert('hola');");
		}

		TestLoginPage step1 = PageFactory.initElements(driver, TestLoginPage.class);
		step1.login();

		// TODO
		return true;
	}

}
