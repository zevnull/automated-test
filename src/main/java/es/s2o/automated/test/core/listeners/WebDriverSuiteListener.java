/**
 * 
 */
package es.s2o.automated.test.core.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;

import es.s2o.automated.test.core.driver.WebDriverManager;
import es.s2o.automated.test.core.testng.ReportManagemer;

/**
 * @author s2o
 */
public class WebDriverSuiteListener implements ISuiteListener {

	private static final Logger LOG = LoggerFactory.getLogger(WebDriverSuiteListener.class);

	// Si hay algún error en el momento de arranque de la suite lo guardaremos en un pdf especial
	private static final String INITIALIZE_SUITE = "initializeSuite";

	@Override
	public void onStart(ISuite paramISuite) {
		LOG.info("onStart suite............................" + paramISuite.getXmlSuite().getName());
		WebDriverManager.getInstance().initialize(paramISuite.getXmlSuite().getName());
		ReportManagemer.getInstance().initReport();
		ReportManagemer.getInstance().startTest(INITIALIZE_SUITE);
	}

	@Override
	public void onFinish(ISuite paramISuite) {
		LOG.info("onFinish suite............................" + paramISuite.getXmlSuite().getName());
		ReportManagemer.getInstance().endTestCase();
		WebDriverManager.getInstance().shutDown();
	}

}
