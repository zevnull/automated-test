package es.s2o.automated.test.core.listeners;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import es.s2o.automated.test.core.annotation.SeleniumWebDriver;
import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.driver.WebDriverManager;
import es.s2o.automated.test.core.model.UrlBase;
import es.s2o.automated.test.core.pages.HomePageManager;
import es.s2o.automated.test.core.testng.ReportManagemer;
import es.s2o.automated.test.core.utilities.S2oException;

/**
 * <pre>
 * 	Si una clase tiene la anotación {@SeleniumWebDriver}:
 * 		1.- Inicializa la variable asociada a la anotación
 * 		2.- Si hay error añade un pantallazo a los reports de error de testng
 * </pre>
 * 
 * @author s2o
 */
public class WebDriverTestListener implements ITestListener {

	private static final Logger LOG = LoggerFactory.getLogger(WebDriverTestListener.class);

	/**
	 * @see org.testng.ITestListener#onStart(org.testng.ITestContext) <br/>
	 *      Invoked after the test class is instantiated and before any configuration method is called.
	 */
	@Override
	public void onStart(ITestContext paramITestContext) {
		AbsisConfigFactory.initConfig();
		LOG.debug("onStart.............." + paramITestContext.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onFinish(org.testng.ITestContext)
	 */
	@Override
	public void onFinish(ITestContext paramITestContext) {
		LOG.debug("onFinish.............." + paramITestContext.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestStart(org.testng.ITestResult)
	 */
	@Override
	public void onTestStart(ITestResult testResult) {
		LOG.debug("onTestStart:" + testResult.getName());

		// Si hay annotaciones las inicializamos
		checkForWebDriverAnnotation(testResult);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestSuccess(org.testng.ITestResult)
	 */
	@Override
	public void onTestSuccess(ITestResult testResult) {
		// Obtenemos el tipo de url (principal o secundaria)
		List<Field> classFieldList = getWebDriverField(testResult);

		// Solo hacemos foto si hay un único navegador
		if (classFieldList.size() == 1 && classFieldList.get(0) != null
				&& AbsisConfigFactory.getConfig().getBoolean(AbsisConstants.WEBDRIVER_RECORD_ALL)) {
			try {
				WebDriver driver = (WebDriver) classFieldList.get(0).get(testResult.getInstance());
				ReportManagemer.getInstance().takeScreenShoot(driver);
			} catch (Exception exeption) {
				// Si ha habido un error simplemente lo anotamos
				LOG.error("onTestFailure: takeScreenShot", exeption);
				Reporter.log("Error obteniendo imagen del fallo: " + exeption.getMessage());
			}
		}
		LOG.debug("onTestSuccess");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestFailedButWithinSuccessPercentage(org.testng.ITestResult)
	 */
	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult paramITestResult) {
		LOG.debug("onTestFailedButWithinSuccessPercentage");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestFailure(org.testng.ITestResult)
	 */
	@Override
	public void onTestFailure(ITestResult testResult) {
		// Obtenemos el tipo de url (principal o secundaria)
		List<Field> classFieldList = getWebDriverField(testResult);

		// Solo hacemos foto si hay un único navegador
		if (classFieldList.size() == 1 && classFieldList.get(0) != null) {
			try {
				WebDriver driver = (WebDriver) classFieldList.get(0).get(testResult.getInstance());
				ReportManagemer.getInstance().takeScreenShoot(driver);
				ReportManagemer.getInstance().addError(driver.getPageSource());
			} catch (Exception exeption) {
				// Si ha habido un error simplemente lo anotamos
				LOG.error("onTestFailure: takeScreenShot", exeption);
				Reporter.log("Error obteniendo imagen del fallo: " + exeption.getMessage());
			}
		}
		LOG.error("onTestFailure:" + testResult.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.testng.ITestListener#onTestSkipped(org.testng.ITestResult)
	 */
	@Override
	public void onTestSkipped(ITestResult paramITestResult) {
		LOG.debug("onTestSkipped");
	}

	/**
	 * Busca anotación SeleniumWebDriver, si existe lo inicializa en caso de ser necesario y lo redirige a la home
	 * 
	 * @param tr
	 */
	private void checkForWebDriverAnnotation(ITestResult testResult) {

		List<Field> classFieldList = getWebDriverField(testResult);
		if (classFieldList.size() != 1) {
			ReportManagemer.getInstance().dissable();
		}
		for (Field classField : classFieldList) {
			if (classField != null) {
				// Obtenemos el tipo de url (principal o secundaria)
				SeleniumWebDriver args = classField.getAnnotation(SeleniumWebDriver.class);
				UrlBase urlBase = args.urlBase();

				String suiteName = testResult.getTestClass().getXmlTest().getSuite().getName();
				WebDriver driver = getWebDriver(testResult, classField, urlBase, suiteName);

				// Vamos a la home
				HomePageManager.getInstance().goHome(driver, urlBase, suiteName);

				// Iniciamos el contexto del report sobre el test
				String className = testResult.getMethod().getTestClass().getName();
				String methodName = testResult.getName();
				ReportManagemer.getInstance().startTest(className + AbsisConstants.CONCAT_NAME + methodName);
			}
		}

	}

	/**
	 * Si ya está inicializado lo obtenemos de la variable, sino, lo inicializamos.
	 * 
	 * @param tr
	 * @param classField
	 * @param driver
	 * @return
	 */
	private WebDriver getWebDriver(ITestResult tr, Field classField, UrlBase urlBase, String suiteName) {
		WebDriver driver = null;
		try {
			if (classField.get(tr.getInstance()) == null) {
				// Seteamos el driver
				switch (urlBase) {
				case PRINCIPAL:
					driver = WebDriverManager.getInstance().getWebDriver(suiteName);
					break;
				default:
					driver = WebDriverManager.getInstance().getWebDriver2(suiteName);
					break;
				}
				classField.set(tr.getInstance(), driver);
			} else {
				driver = (WebDriver) classField.get(tr.getInstance());
			}
		} catch (Exception exception) {
			throw new S2oException(driver, "Error seteando valor de getWebDriver", exception);
		}
		return driver;
	}

	/**
	 * @param tr
	 * @param suiteName
	 * @param testClass
	 * @throws SecurityException
	 */
	private List<Field> getWebDriverField(ITestResult paramITestResult) throws SecurityException {
		List<Field> answer = new LinkedList<Field>();

		Class<?> testClass = paramITestResult.getTestClass().getRealClass();
		for (Field classField : testClass.getDeclaredFields()) {
			if (classField.isAnnotationPresent(SeleniumWebDriver.class)) {
				classField.setAccessible(true);
				answer.add(classField);
			}
		}

		// Si tiene padre, miramos que tenga también @SeleniumWebDriver
		for (Field classField : testClass.getSuperclass().getDeclaredFields()) {
			if (classField.isAnnotationPresent(SeleniumWebDriver.class)) {
				classField.setAccessible(true);
				answer.add(classField);
			}
		}

		return answer;
	}

}
