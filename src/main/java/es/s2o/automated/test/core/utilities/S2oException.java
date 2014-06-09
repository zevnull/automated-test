package es.s2o.automated.test.core.utilities;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.s2o.automated.test.core.driver.WebDriverManager;
import es.s2o.automated.test.core.pages.HomePageManager;
import es.s2o.automated.test.core.testng.ReportManagemer;

/**
 * <PRE>
 * Lanza error de runtime. 
 * 	Puede ir acumulando errores (por ejemplo que no se pasen una serie de validaciones)
 * 	Si hay algn error cerramos el navegador
 * 
 * </PRE>
 * 
 * @author s2o
 */
public class S2oException extends RuntimeException {

	private static final Logger LOG = LoggerFactory.getLogger(S2oException.class);

	private static final long serialVersionUID = -4155756999259747645L;

	/**
	 * @param driver
	 *            (admite nulos)
	 * @param pEerrorCollectors
	 * @param ex
	 */
	public S2oException(String pEerrorCollectors) {
		super(pEerrorCollectors);
		HomePageManager.getInstance().setLoginWithError(true);
		LOG.error(pEerrorCollectors);
	}

	/**
	 * @param driver
	 *            (admite nulos)
	 * @param pEerrorCollectors
	 * @param ex
	 */
	public S2oException(String pEerrorCollectors, Exception ex) {
		super(pEerrorCollectors);
		HomePageManager.getInstance().setLoginWithError(true);

		LOG.error(pEerrorCollectors, ex);
	}

	/**
	 * @param driver
	 *            (admite nulos)
	 * @param pEerrorCollectors
	 * @param ex
	 */
	public S2oException(WebDriver driver, String pEerrorCollectors, Exception ex) {
		super(pEerrorCollectors);
		LOG.error(pEerrorCollectors, ex);
		takeCareWebDriver(driver);
	}

	/**
	 * @param driver
	 *            (admite nulos)
	 * @param pEerrorCollectors
	 */
	public S2oException(WebDriver driver, String pEerrorCollectors) {
		super(pEerrorCollectors);
		LOG.error(pEerrorCollectors);
		takeCareWebDriver(driver);
	}

	/**
	 * @param driver
	 *            (admite nulos)
	 * @param pEerrorCollectors
	 */
	public S2oException(WebDriver driver, List<String> pEerrorCollectors) {
		super(pEerrorCollectors.toString());
		for (String errror : pEerrorCollectors) {
			LOG.error(errror);
		}
		takeCareWebDriver(driver);
	}

	/**
	 * @param driver
	 *            (admite nulos)
	 */
	private void takeCareWebDriver(WebDriver driver) {
		if (driver != null) {
			ReportManagemer.getInstance().takeScreenShoot(driver);
			String reportFile = ReportManagemer.getInstance().endTestCase();
			LOG.error(" -------------------------------------- ");
			LOG.error(" ");
			LOG.error("PDF con pantallazo del ERROR:" + reportFile);
			LOG.error(" ");
			LOG.error(" -------------------------------------- ");
			WebDriverManager.getInstance().shutDown();
		}
	}

}