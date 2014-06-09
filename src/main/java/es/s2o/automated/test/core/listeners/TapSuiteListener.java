package es.s2o.automated.test.core.listeners;

import java.util.List;

import org.tap4j.ext.testng.listener.TapListenerSuite;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.xml.XmlSuite;

/**
 * <pre>
 * Extiendo TapListenerSuite bsicamente para poner esta clase en 
 * automated-test-core/src/main/resources/META-INF/services/org.testng.ITestNGListener
 * y as no tener que definir el listener
 * </pre>
 * 
 * * @author s2o
 */
public class TapSuiteListener extends TapListenerSuite implements IReporter {

	@Override
	public void generateReport(List<XmlSuite> paramList, List<ISuite> paramList1, String paramString) {
		super.generateReport(paramList, paramList1, paramString);
	}
}
