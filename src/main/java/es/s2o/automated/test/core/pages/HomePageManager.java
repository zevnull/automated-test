package es.s2o.automated.test.core.pages;

import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.Browser;
import es.s2o.automated.test.core.model.UrlBase;
import es.s2o.automated.test.core.testng.ReportManagemer;
import es.s2o.automated.test.core.utilities.S2oException;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * <pre>
 * 	Gestiona posicionar el navegador en la url para la "home" definida en la configuracin.
 * 	Teniendo en cuenta:
 * 	- login: si/no
 * 	- Clase de gestin de login
 * 	- Caractersticas de login: usuario/demo
 * 	- Permite gestionar dos "Home's" diferentes (pensando en atacar a 2 entornos a la vez) @{UrlBase}:
 * 		PRINCIPAL: URL_BASE
 * 		SECUNDARIA: URL_BASE2
 * 
 * </pre>
 * 
 * @author s2o
 */
public class HomePageManager {

	private static final Logger LOG = LoggerFactory.getLogger(HomePageManager.class);

	private static volatile HomePageManager instance;

	// Si ha habido error en un login, no lo volvemos a intentar
	private boolean loginWithError = false;

	// Double-checked locking
	public static HomePageManager getInstance() {
		if (instance == null) {
			synchronized (HomePageManager.class) {
				if (instance == null) {
					instance = new HomePageManager();
				}
			}
		}
		return instance;
	}

	/**
	 * Se posiciona en la home por defecto (URL_BASE)
	 * 
	 * @param driver
	 */
	public void goHome(WebDriver driver) {

		String urlBase = AbsisConfigFactory.getConfig().getString(AbsisConstants.URL_BASE);
		greenGoHome(driver, urlBase);
	}

	/**
	 * Se posiciona en la home que se le pasa por parametro: la principal o secundaria
	 * 
	 * @param driver
	 * @param urlBase
	 * @param suiteName
	 */
	public void goHome(WebDriver driver, UrlBase urlBase, String suiteName) {
		// Si es necesario se inicializa la configuracin
		// y se hace login
		initLogin(driver, urlBase, suiteName);

		String urlBaseEntorno = getUrlBaseEntorno(urlBase);
		greenGoHome(driver, urlBaseEntorno);
	}

	/**
	 * @param driver
	 * @param urlBase
	 * @throws S2oException
	 */
	private void greenGoHome(WebDriver driver, String urlBase) throws S2oException {
		String homePage = urlBase + getConfig().getString(AbsisConstants.HOME_PAGE);
		driver.navigate().to(homePage);

		By locator = By.id(getConfig().getString(AbsisConstants.HOME_PAGE_VALIDATION));

		if (WebDriverShortcuts.explicitWait(driver, locator) == false) {
			throw new S2oException(driver, "No ha sido posible llegar a la home!!!! Validadando encontrar el id:"
					+ getConfig().getString(AbsisConstants.HOME_PAGE_VALIDATION));
		}

		if (Browser.IEXPLORER.name().equals(AbsisConfigFactory.getConfig().getString(AbsisConstants.WEBDRIVER_BROWSER))) {
			driver.navigate().refresh();
			WebDriverShortcuts.waitAMomentPlease(600);
		}

	}

	/**
	 * Obtiene la configuracin definida a partir de los parametros de la VM y de los ficheros
	 * nombreSuite.conf/application.conf/reference.conf
	 * 
	 * @return the config
	 */
	public static Config getConfig() {
		return AbsisConfigFactory.getConfig();
	}

	/**
	 * @param suiteName
	 * @return
	 */
	private void initLogin(WebDriver driver, UrlBase urlBase, String suiteName) {
		if (loginWithError == false) {
			// Si no tenemos configuracin iniciada, quiere decir que no an no hemos hecho login
			String currentUrl = driver.getCurrentUrl();
			if (currentUrl.contains(AbsisConstants.INITIAL_PAGE)) {
				login(driver, urlBase);
			}

		}
	}

	/**
	 * @param urlBase
	 * @param pWwebDriver
	 */
	private void login(WebDriver driver, UrlBase urlBase) {

		boolean loginRequired = AbsisConfigFactory.getConfig().getBoolean(AbsisConstants.LOGIN_REQUIRED);
		if (loginRequired) {
			// Obtenemos la clase de login del classpath
			boolean loginOk = true;
			String claseDeLogin = AbsisConfigFactory.getConfig().getString(AbsisConstants.LOGIN_CLASS);
			try {
				ILoginPage loginFlow = (ILoginPage) Class.forName(claseDeLogin).newInstance();
				loginOk = loginFlow.login(driver, AbsisConfigFactory.getConfig(), getUrlBaseEntorno(urlBase));
			} catch (Exception exception) {
				loginOk = false;
			}
			if (loginOk == false) {
				ReportManagemer.getInstance().addError("Error en login");
				ReportManagemer.getInstance().takeScreenShoot(driver);
				String reportFile = ReportManagemer.getInstance().endTestCase();
				throw new S2oException(driver,
						"Problemas en el login, hemos generado un pdf con la 'pelicula' de lo que ha pasado:"
								+ reportFile);
			} else {
				// Si ha ido bien limpiamos el report para ahorrarnos el login
				ReportManagemer.getInstance().reStartTest();
			}
			LOG.info("Login started Ok");
		} else {
			LOG.info("Direct access to home, no login configuration required.");
		}
	}

	/**
	 * @param config
	 * @return
	 */
	private String getUrlBaseEntorno(UrlBase urlBase) {
		String urlBaseEntorno;

		switch (urlBase) {
		case PRINCIPAL:
			urlBaseEntorno = AbsisConfigFactory.getConfig().getString(AbsisConstants.URL_BASE);
			break;
		default:
			urlBaseEntorno = AbsisConfigFactory.getConfig().getString(AbsisConstants.URL_BASE2);
			break;
		}
		return urlBaseEntorno;
	}

	/**
	 * Cuando finaliza la suite hacemos logout
	 * 
	 * @param webDriver
	 */
	public void logOut(WebDriver webDriver) {
		URL logOut = gelLogOutUrl(webDriver.getCurrentUrl());
		webDriver.navigate().to(logOut);
		LOG.info("logOut Ok");
	}

	private URL gelLogOutUrl(String currentUrl) {
		URL logOutUrl = null;
		try {
			URL actualUrl = new URL(currentUrl);
			logOutUrl = new URL(actualUrl.getProtocol(), actualUrl.getHost(), actualUrl.getPort(), AbsisConfigFactory
					.getConfig().getString(AbsisConstants.LOGOUT));
		} catch (Exception e) {
			// Este error no hace falta propagarlo, slo lo informamos
			LOG.error("Error haciendo logOut para:" + currentUrl);
		}
		return logOutUrl;
	}

	public boolean isLoginWithError() {
		return loginWithError;
	}

	public void setLoginWithError(boolean loginWithError) {
		this.loginWithError = loginWithError;
	}

}
