package es.s2o.automated.test.core.conf;

/**
 * @author s2o
 */
public interface AbsisConstants {

	public static final String SUITE_REPORT = "suiteReport";
	public static final String INITIAL_PAGE = "about:blank";
	public static final String CERTIFICATE_PAGE = "CertUnknownCA";

	// PAGE PROPERTIES
	public static final String ENVIRONMENT = "pages.environment";
	public static final String URL_BASE = "pages.urlBase";
	public static final String URL_BASE2 = "pages.urlBase2";

	public static final String LOGIN_REQUIRED = "pages.login.required";
	public static final String LOGIN_CLASS = "pages.login.class";
	public static final String LOGIN_PAGE = "pages.login.url";
	public static final String POST_LOGIN_VALIDATION = "pages.login.postLoginIdValidation";

	public static final String LOGOUT = "pages.logout";

	public static final String HOME_PAGE = "pages.urlHome";
	public static final String HOME_PAGE_VALIDATION = "pages.urlHome_validation";

	// USER PROPERTIES
	public static final String MODO_DEMO = "user.modoDemo";

	public static final String USERNAME = "user.username";
	public static final String USERNAME_DC = "user.dc";
	public static final String PASSWORD_IMS = "user.pasword.ims";
	public static final String PASSWORD_AGS = "user.pasword.ags";
	public static final String OFICINA_CONTABLE = "user.oficina_contable";
	public static final String IMS = "user.ims";

	// BROWSER CONFIGURATION
	public static final String WEBDRIVER_BROWSER = "webdriver.browser";
	public static final String WEBDRIVER_NEED_CERT = "webdriver.necesitaCertificado";
	public static final String WEBDRIVER_SHUTDOWN = "webdriver.shutdown";

	// Si queremos ejecucin en paralelo lo ms 'sano' es utilizar los 2 servidores de webdriver
	public static final String WEBDRIVER_BROWSER_IE_PATH = "webdriver.exe.ie";
	public static final String WEBDRIVER_BROWSER_IE2_PATH = "webdriver.exe.ie2";
	public static final String WEBDRIVER_BROWSER_LOG_LEVEL = "webdriver.ie.loglevel";
	public static final String WEBDRIVER_BROWSER_LOG_FILE_PATH = "webdriver.ie.logfile";
	public static final String WEBDRIVER_BROWSER_NO_DIAGNOSTIC_OUTPUT = "webdriver.ie.silent";

	public static final String WEBDRIVER_BROWSER_CHROME_PATH = "webdriver.exe.chrome";
	public static final String WEBDRIVER_BROWSER_FF_PATH = "webdriver.exe.ff";
	public static final String WEBDRIVER_CAPABILITIES_PATH = "webdriver.capabilities";

	public static final String WEBDRIVER_PROXY_TYPE = "webdriver.proxy.type";
	public static final String WEBDRIVER_PROXY_HTTPPROXY = "webdriver.proxy.httpProxy";
	public static final String WEBDRIVER_PROXY_AUTOCONFIGURL = "webdriver.proxy.autoconfigUrl";

	public static final String IMPLICITLY_WAIT = "webdriver.seconds_implicitly_wait";

	public static final String WEBDRIVER_RECORD_ALL = "webdriver.recordAll";

	public static final String SCREENSHOTS_PATH = "webdriver.screenshots_path";

	public static final String RETORNO_CARRO = "\n";
	public static final String CONCAT_NAME = "_";
	
	// Para el cambio del tipoTerminal
	public static final String URL_DEMO_ARQ = "pages.urlDemoArq";
	public static final String TERMINAL_BROWSER = "bro";
	public static final String TERMINAL_TF7 = "tf7";
	
}
