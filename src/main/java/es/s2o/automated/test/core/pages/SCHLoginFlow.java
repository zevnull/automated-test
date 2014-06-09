package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.Browser;
import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.model.UsuariBuilder;
import es.s2o.automated.test.core.utilities.S2oException;

public class SCHLoginFlow implements ILoginPage {

	private static final Logger LOG = LoggerFactory.getLogger(SCHLoginFlow.class);

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

		// Introducimos usuario y contraseña
		LOG.info("Accediendo  a página inicial de login");
		SCHLoginStep1Page step1 = PageFactory.initElements(driver, SCHLoginStep1Page.class);

		if (config.getString(AbsisConstants.ENVIRONMENT).contains("pro")) {

			step1.loginStep1PRO(usuari);

		} else {

			SCHLoginStep2Page step2 = step1.loginStep1PREV(usuari);

			if (!step2.isTheActualPage()) {
				throw new S2oException(driver, "Usuario incorrecto, usuario:" + usuari.getUsername());
			}

			// Introducimos ims
			LOG.info("Accediendo a página Pasword IMS");
			step2.loginStep2(usuari);

		}

		// TODO Falta revisar el ID que comprobamos dentro de la página!
		/*
		 * if (!demo.isThisTheActualPage(config.getString(AbsisConstants.POST_LOGIN_VALIDATION))) { throw new
		 * S2oException(driver, "No nos hemos podido logar correctamente"); } else {
		 * ReportManagemer.getInstance().reStartTest(); }
		 */
		return true;

	}
}
