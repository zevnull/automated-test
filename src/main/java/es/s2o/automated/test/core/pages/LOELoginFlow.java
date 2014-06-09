package es.s2o.automated.test.core.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.Browser;
import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.model.UsuariBuilder;

/**
 * @author s2o
 */
public class LOELoginFlow implements ILoginPage {

	private static final Logger LOG = LoggerFactory.getLogger(LOELoginFlow.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.s2o.automated.test.core.pages.ILoginPage#login(org.openqa.selenium.WebDriver,
	 * com.typesafe.config.Config, java.lang.String)
	 */
	@Override
	public boolean login(WebDriver driver, Config config, String urlBaseEntorno) {

		Usuari usuari = UsuariBuilder.configUserBuilder(config);

		String login = urlBaseEntorno + config.getString(AbsisConstants.LOGIN_PAGE);
		LOG.info("Starting login to {}", login);
		driver.navigate().to(login);

		// Validar si es IE para saltar el https y maximizar
		if (Browser.IEXPLORER.name().equals(config.getString(AbsisConstants.WEBDRIVER_BROWSER))) {
			driver.navigate().to("javascript:document.getElementById('overridelink').click();");
		}

		LOG.info("Accediendo  a página inicial de login");
		LOELoginStep1Page step1 = PageFactory.initElements(driver, LOELoginStep1Page.class);

		// Ponemos el usuario
		if (usuari.isModoDemo()) {
			WebElement demo = driver.findElement(By.xpath("//input[@name=\"isDemo\"]"));
			demo.clear();
			demo.sendKeys("true");

			Assert.assertTrue(step1.isTheActualPage(), "No se ha podido acceder a la página de login");
		} else {
			WebElement demo = driver.findElement(By.xpath("//input[@name=\"isDemo\"]"));
			demo.clear();
			demo.sendKeys("false");

			WebElement ims = driver.findElement(By.xpath("//input[@name=\"ims\"]"));
			ims.clear();
			ims.sendKeys(usuari.getIms());
		}

		// WebElement ip = driver.findElement(By.xpath("//input[@name=\"ip\"]"));
		// ip.clear();
		// ip.sendKeys("217.148.66.27");

		WebElement botonEjecutar = driver.findElement(By.xpath("//input[@value=\"Execute\"]"));
		botonEjecutar.submit();
		// https://loint.s2o.es/CA.LOE/demoWeb/003/001/001/index.tf7
		// TODO
		return true;
	}
}
