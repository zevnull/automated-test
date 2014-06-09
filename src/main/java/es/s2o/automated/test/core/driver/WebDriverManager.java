package es.s2o.automated.test.core.driver;

import java.util.concurrent.atomic.AtomicInteger;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.pages.HomePageManager;

/**
 * <pre>
 * 	Gestiona la l'estat del navegador i es posiciona a la "home" definida a les properties.
 * 	Si es necessari fa login.
 * </pre>
 * 
 * @author s2o
 */
public class WebDriverManager {

	private static final Logger LOG = LoggerFactory.getLogger(WebDriverManager.class);

	// Nos aseguramos de tener una nica instancia para todos los threads
	private static volatile WebDriverManager instance;

	// Segn el thread tendremos apuntaremos a un browser diferente
	private static final ThreadLocal<WebDriver> webDriver = new ThreadLocal<WebDriver>();
	// Configuracin de la suite
	private static final ThreadLocal<Config> config = new ThreadLocal<Config>();

	// Puedo utilizar dos instancias si queremos utilizar 2 browsers desde un nico thread.
	// Que compartirn la configuracin
	private static final ThreadLocal<WebDriver> webDriver2 = new ThreadLocal<WebDriver>();

	// Llevamos control del nmero de browsers que se abren
	private final AtomicInteger numOfBrowsers = new AtomicInteger(0);

	// Double-checked locking
	public static WebDriverManager getInstance() {
		if (instance == null) {
			synchronized (WebDriverManager.class) {
				if (instance == null) {
					instance = new WebDriverManager();
				}
			}
		}
		return instance;
	}

	/**
	 * <pre>
	 * 	Retorna una instancia apuntant a un browser
	 * </pre>
	 * 
	 * @param suiteName
	 * @return
	 */
	public WebDriver getWebDriver(String suiteName) {

		if (webDriver.get() == null) {
			LOG.info("Construyendo conector getWebDriver");
			startUpWebDriver(suiteName, config, webDriver);
		}

		return webDriver.get();
	}

	/**
	 * Obtiene instancia al browser secundari
	 * 
	 * @return
	 */
	public WebDriver getWebDriver2(String suiteName) {
		if (webDriver2.get() == null) {
			LOG.info("Construyendo conector getWebDriver2");
			startUpWebDriver(suiteName, config, webDriver2);
		}

		return webDriver2.get();
	}

	/**
	 * Slo se arranca una vez, si falla no se vuelve a intentar
	 * 
	 * @param suiteName
	 */
	private void startUpWebDriver(String suiteName, ThreadLocal<Config> localConfig, ThreadLocal<WebDriver> pWebDriver) {
		LOG.info("Construyendo conector web:startUpWebDriver");

		TargetWebDriver target = new TargetWebDriver(localConfig.get());
		pWebDriver.set(target.buildDriver(numOfBrowsers.incrementAndGet()));

		LOG.info("Construyendo conector web");
	}

	/**
	 * Hacemos logout y finalizamos la sesin
	 */
	public void shutDown() {
		try {
			shutDownDriver(webDriver);
			shutDownDriver(webDriver2);
			resetConfig();
		} catch (Exception e) {
			LOG.debug("El navegador ya est cerrado.");
		}
	}

	private void resetConfig() {
		config.set(null);
	}

	/**
	 * 
	 */
	private void shutDownDriver(ThreadLocal<WebDriver> pWebDriver) {
		// Aunque no haya login si puede haber cierre de sesin
		if (pWebDriver.get() != null && config.get().getBoolean(AbsisConstants.WEBDRIVER_SHUTDOWN)) {
			LOG.info("shutDown driver...");
			numOfBrowsers.decrementAndGet();
			HomePageManager.getInstance().logOut(pWebDriver.get());
			pWebDriver.get().close();
			pWebDriver.get().quit();

			pWebDriver.set(null);
		}
	}

	public void initialize(String suiteName) {
		webDriver.set(null);
		config.set(null);
		webDriver2.set(null);
		numOfBrowsers.set(0);
		initConfig(config, suiteName);
	}

	/**
	 * @param suiteName
	 */
	private void initConfig(ThreadLocal<Config> localConfig, String suiteName) {
		if (localConfig.get() == null) {
			localConfig.set(AbsisConfigFactory.buildConfig(suiteName));
		}
	}

}
