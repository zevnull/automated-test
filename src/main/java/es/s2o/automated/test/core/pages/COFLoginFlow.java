package es.s2o.automated.test.core.pages;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.Browser;
import es.s2o.automated.test.core.model.Environment;
import es.s2o.automated.test.core.model.Usuari;
import es.s2o.automated.test.core.model.UsuariBuilder;
import es.s2o.automated.test.core.utilities.S2oException;
import es.s2o.automated.test.core.utilities.AbsisUtils;

/**
 * @author s2o
 */
public class COFLoginFlow implements ILoginPage {

	private static final Logger LOG = LoggerFactory.getLogger(COFLoginFlow.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see es.s2o.automated.test.core.pages.ILoginPage#login(org.openqa.selenium.WebDriver,
	 * com.typesafe.config.Config, java.lang.String)
	 */
	@Override
	public boolean login(WebDriver driver, Config config, String urlBaseEntorno) {
		boolean loginOk = false;

		Usuari usuari = UsuariBuilder.configUserBuilder(config);

		String login = urlBaseEntorno + config.getString(AbsisConstants.LOGIN_PAGE);
		LOG.info("Starting login to {}", login);
		driver.navigate().to(login);

		// Validar si es IE para saltar el https y maximizar
		if (Browser.IEXPLORER.name().equals(config.getString(AbsisConstants.WEBDRIVER_BROWSER))
				&& driver.getPageSource().indexOf(AbsisConstants.CERTIFICATE_PAGE) > 0) {
			driver.navigate().to("javascript:document.getElementById('overridelink').click();");
		}

		COFLoginStep1Page step1 = PageFactory.initElements(driver, COFLoginStep1Page.class);
		if (step1.isTheActualPage() == false) {
			List<String> erroresPosible = new LinkedList<String>();
			erroresPosible.add("No se ha podido acceder a la página de login." + login);
			erroresPosible.add("............Posibles problemas:");
			erroresPosible.add("............¿Certificado Caixa en IE? Ver parámetro: webdriver.necesitaCertificado");
			erroresPosible.add("............¿Se necesita proxy? Ver parámetro: webdriver.proxy.httpProxy");
			erroresPosible.add("............¿Entorno caido? Conectate manualmente para verificarlo.");
			throw new S2oException(driver, erroresPosible);
		}

		// Ponemos el usuario
		COFInitialPage demo;
		if (usuari.isModoDemo()) {
			LOG.info("Accediendo en modo memo");
			demo = step1.loginAsDemo(config);
		} else {
			LOG.info("Accediendo con el usuario:" + usuari.getUsername());
			COFLoginStep2Page step2 = step1.loginStep1(usuari);
			if (step2.isTheActualPage() == false) {
				throw new S2oException(driver, "Usuario incorrecto, usuario:" + usuari.getUsername());
			}

			LOG.info("Accediendo a página Pasword IMS");
			// Ponemos contraseña IMS
			COFLoginStep3Page step3 = step2.loginStep2(usuari);
			if (step3.isTheActualPage() == false) {
				throw new S2oException(driver, "Usuario: " + usuari.getUsername()
						+ " con Pasword IMS incorrecto, password:" + usuari.getPasswordIMS());
			}

			LOG.info("Accediendo a página Pasword AGS");
			// Ponemos constraseña AGS
			COFLoginStep4Page step4 = step3.loginStep3(usuari);

			// Si estamos en PRO el flujo es distinto
			if (Environment.PRO.getValue().equals(AbsisUtils.environment())) {
				demo = PageFactory.initElements(driver, COFInitialPage.class);
			} else {
				if (step4.isTheActualPage() == false) {
					throw new S2oException(driver, "Usuario: " + usuari.getUsername()
							+ " con Pasword AGS incorrecto, password:" + usuari.getPasswordAGS());
				}
				LOG.info("Accediendo a página final ims");
				// Ponemos ims
				// Si hay problemas volvemos al paso 3, donde pone la contraseña AGS
				demo = step4.loginStep4(usuari);
			}
		}

		// Validación login OK
		if (demo.isThisTheActualPage(config.getString(AbsisConstants.POST_LOGIN_VALIDATION))) {
			loginOk = true;
		}

		return loginOk;
	}
}
