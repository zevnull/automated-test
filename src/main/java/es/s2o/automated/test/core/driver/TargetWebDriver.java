package es.s2o.automated.test.core.driver;

import java.lang.reflect.Constructor;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Architecture;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.events.EventFiringWebDriverWithSubmit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.listeners.WebDriverFunctionalListener;
import es.s2o.automated.test.core.utilities.S2oException;

/**
 * Gestiona la configuracin del driver
 * 
 * @author s2o
 */
public class TargetWebDriver {

	private static final Logger LOG = LoggerFactory.getLogger(TargetWebDriver.class);

	private final Config config;

	public TargetWebDriver(Config config) {
		this.config = config;
	}

	public WebDriver buildDriver(int instanceDriver) {
		WebDriver driver = null;
		try {
			LOG.info("SO: " + Platform.getCurrent());
			LOG.info("Architecture: " + Architecture.getCurrent());

			Browser browser = getDeclaredBrowser();
			LOG.info("Browser selected: " + browser);

			switch (browser) {
			case IEXPLORER:
				String temp = config.getString(AbsisConstants.WEBDRIVER_BROWSER_IE_PATH);
				// Con IE si abro 2 browsers utilizo los dos servidores de webDriver
				if (instanceDriver > 1) {
					temp = config.getString(AbsisConstants.WEBDRIVER_BROWSER_IE2_PATH);
				}
				LOG.info("Utilizando driver IE:", temp);
				System.setProperty("webdriver.ie.driver", temp);

				temp = config.getString(AbsisConstants.WEBDRIVER_BROWSER_LOG_LEVEL);
				System.setProperty("webdriver.ie.driver.loglevel", temp);

				temp = config.getString(AbsisConstants.WEBDRIVER_BROWSER_LOG_FILE_PATH);
				System.setProperty("webdriver.ie.driver.logfile", temp);

				temp = config.getString(AbsisConstants.WEBDRIVER_BROWSER_NO_DIAGNOSTIC_OUTPUT);
				System.setProperty("webdriver.ie.driver.silent", temp);
				break;
			case CHROME:
				String pathExeChrme = config.getString(AbsisConstants.WEBDRIVER_BROWSER_CHROME_PATH);
				LOG.info("Updating system property with chromeDrive.exe path {}", pathExeChrme);
				System.setProperty("webdriver.chrome.driver", pathExeChrme);
				break;
			default:
				String pathExeFF = config.getString(AbsisConstants.WEBDRIVER_BROWSER_FF_PATH);
				LOG.info("Updating system property with ff.exe path {}", pathExeFF);
				System.setProperty("webdriver.firefox.bin", pathExeFF);
				break;
			}

			LOG.info("Starting driver: {}", browser.getDriverClass());
			final Class<?> driverClass = browser.getDriverClass();
			Constructor<?> constructor = driverClass.getDeclaredConstructor(Capabilities.class);
			driver = (WebDriver) constructor.newInstance(defaultDesiredCapabilities(browser));

			initializeBroserProperties(driver, browser);

		} catch (Exception ex) {
			throw new S2oException(null, "Error iniciando browser", ex);
		}

		EventFiringWebDriverWithSubmit efd = new EventFiringWebDriverWithSubmit(driver);
		efd.register(new WebDriverFunctionalListener());

		LOG.info("Browser started Ok!");
		return efd;
	}

	/**
	 * @param driver
	 */
	private void initializeBroserProperties(WebDriver driver, Browser browser) {
		int secondWaiting = config.getInt(AbsisConstants.IMPLICITLY_WAIT);
		driver.manage().timeouts().implicitlyWait(secondWaiting, TimeUnit.SECONDS);

		// Si es IE maximizar
		if (Browser.IEXPLORER.name().equals(browser.name())) {
			driver.manage().window().maximize();
		}
	}

	private Browser getDeclaredBrowser() {
		Browser result;
		try {
			result = Browser.fromJson(config.getString(AbsisConstants.WEBDRIVER_BROWSER));
		} catch (IllegalArgumentException ex) {
			throw new S2oException(null, "Invalid browser.  Must be one of :" + Browser.values().toString(), ex);
		}
		return result;
	}

	private DesiredCapabilities defaultDesiredCapabilities(Browser browser) {
		DesiredCapabilities capabilities;

		switch (browser) {
		case IEXPLORER:
			capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setCapability(InternetExplorerDriver.INITIAL_BROWSER_URL, AbsisConstants.INITIAL_PAGE);

			// Para pasar el certificado caixa
			capabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);

			// Para que se pueda ejecutar desde un servicio windows
			capabilities.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, false);
			capabilities.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);

			// Para permitir ejecuciones en paralelo
			// Windows Registry HKLM_CURRENT_USER\\Software\\Microsoft\\Internet Explorer\\Main path should contain key
			// TabProcGrowth with 0 value
			capabilities.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
			capabilities.setCapability(InternetExplorerDriver.FORCE_CREATE_PROCESS, true);
			capabilities.setCapability(InternetExplorerDriver.IE_SWITCHES, "-private");

			break;
		case FIREFOX:
			capabilities = DesiredCapabilities.firefox();
			break;
		case CHROME:
			capabilities = DesiredCapabilities.chrome();
			break;
		default:
			capabilities = DesiredCapabilities.htmlUnit();
		}

		capabilities.setJavascriptEnabled(true);
		capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		capabilities.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
		capabilities.setCapability(CapabilityType.TAKES_SCREENSHOT, true);

		Proxy proxy = getProxy();
		capabilities.setCapability(CapabilityType.PROXY, proxy);
		return capabilities;
	}

	/**
	 * @return
	 */
	private Proxy getProxy() {
		ProxyType proxyType = getProxyTypeFromCongif(config);
		LOG.info("Network configuration:{}", proxyType);

		Proxy proxy = new Proxy();
		proxy.setProxyType(proxyType);

		switch (proxyType) {
		case MANUAL:
			String proxyValue = config.getString(AbsisConstants.WEBDRIVER_PROXY_HTTPPROXY);
			LOG.info("Network proxyValue:{}", proxyValue);
			proxy.setHttpProxy(proxyValue);
			proxy.setSslProxy(proxyValue);
			break;
		case PAC:
			String autoconfigUrl = config.getString(AbsisConstants.WEBDRIVER_PROXY_AUTOCONFIGURL);
			LOG.info("Network autoconfigUrl:{}", autoconfigUrl);
			proxy.setProxyAutoconfigUrl(autoconfigUrl);
			break;
		default:
			break;
		}
		return proxy;
	}

	/**
	 * ProxyType: DIRECT, AUTODETECT, MANUAL, PAC, SYSTEM
	 * 
	 * @param config
	 * @return
	 */
	private ProxyType getProxyTypeFromCongif(Config config) {
		ProxyType answer = ProxyType.DIRECT;

		String type = config.getString(AbsisConstants.WEBDRIVER_PROXY_TYPE);

		if (ProxyType.AUTODETECT.name().equals(type)) {
			answer = ProxyType.AUTODETECT;
		} else if (ProxyType.MANUAL.name().equals(type)) {
			answer = ProxyType.MANUAL;
		} else if (ProxyType.PAC.name().equals(type)) {
			answer = ProxyType.PAC;
		} else if (ProxyType.SYSTEM.name().equals(type)) {
			answer = ProxyType.SYSTEM;
		}

		return answer;
	}
}
