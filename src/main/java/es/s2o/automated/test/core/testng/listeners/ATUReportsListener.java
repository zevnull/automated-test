package es.s2o.automated.test.core.testng.listeners;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

import es.s2o.automated.test.core.testng.utils.Directory;
import es.s2o.automated.test.core.testng.writers.ConsolidatedReportsPageWriter;
import es.s2o.automated.test.core.testng.writers.CurrentRunPageWriter;
import es.s2o.automated.test.core.testng.writers.HTMLDesignFilesJSWriter;
import es.s2o.automated.test.core.testng.writers.IndexPageWriter;
import es.s2o.automated.test.core.testng.writers.TestCaseReportsPageWriter;
import es.s2o.automated.test.core.utilities.S2oException;

public class ATUReportsListener implements ITestListener, ISuiteListener {
	int runCount = 0;
	List<ITestResult> passedTests = new ArrayList<ITestResult>();
	List<ITestResult> failedTests = new ArrayList<ITestResult>();
	List<ITestResult> skippedTests = new ArrayList<ITestResult>();

	@Override
	public void onFinish(ITestContext paramITestContext) {
	}

	@Override
	public void onStart(ITestContext paramITestContext) {
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult paramITestResult) {
	}

	@Override
	public void onTestFailure(ITestResult paramITestResult) {
		this.failedTests.add(paramITestResult);
	}

	@Override
	public void onTestSkipped(ITestResult paramITestResult) {
		createReportDir(paramITestResult);
		this.skippedTests.add(paramITestResult);
	}

	@Override
	public void onTestStart(ITestResult paramITestResult) {
	}

	@Override
	public void onTestSuccess(ITestResult paramITestResult) {
		this.passedTests.add(paramITestResult);
	}

	public static void createReportDir(ITestResult paramITestResult) {
		String str = getReportDir(paramITestResult);
		Directory.mkDirs(str);
		Directory.mkDirs(str + Directory.SEP + "img");
	}

	public static String getReportDir(ITestResult paramITestResult) {

		String str1 = paramITestResult.getTestContext().getSuite().getName();
		String str2 = paramITestResult.getTestContext().getCurrentXmlTest().getName();
		String str3 = paramITestResult.getTestClass().getName().replace(".", Directory.SEP);
		String str4 = paramITestResult.getMethod().getMethodName();
		str4 = str4 + "_Iteration" + (paramITestResult.getMethod().getCurrentInvocationCount() + 1);
		String str5 = Directory.RUNDir + Directory.SEP + str1 + Directory.SEP + str2 + Directory.SEP + str3
				+ Directory.SEP + str4;
		paramITestResult.setAttribute("iteration",
				Integer.valueOf(paramITestResult.getMethod().getCurrentInvocationCount() + 1));
		paramITestResult.setAttribute("reportDir", str5);
		return str5;
	}

	@Override
	public void onFinish(ISuite paramISuite) {
		try {
			// String str1 = SettingsFile.get("passedList") + this.passedTests.size() + ';';
			// String str2 = SettingsFile.get("failedList") + this.failedTests.size() + ';';
			// String str3 = SettingsFile.get("skippedList") + this.skippedTests.size() + ';';
			// SettingsFile.set("passedList", str1);
			// SettingsFile.set("failedList", str2);
			// SettingsFile.set("skippedList", str3);
			// HTMLDesignFilesJSWriter.lineChartJS(str1, str2, str3, this.runCount);
			// HTMLDesignFilesJSWriter.barChartJS(str1, str2, str3, this.runCount);
			HTMLDesignFilesJSWriter.pieChartJS(this.passedTests.size(), this.failedTests.size(),
					this.skippedTests.size(), this.runCount);
			generateIndexPage();
			paramISuite.setAttribute("endExecution", Long.valueOf(System.currentTimeMillis()));
			long l = ((Long) paramISuite.getAttribute("startExecution")).longValue();
			generateConsolidatedPage();
			generateCurrentRunPage(l, System.currentTimeMillis());
			startReportingForPassed(this.passedTests);
			startReportingForFailed(this.failedTests);
			startReportingForSkipped(this.skippedTests);
			ConfigurationListener.startConfigurationMethodsReporting(this.runCount);
		} catch (Exception localException) {
			throw new S2oException(null, "onFinish", localException);
		}
	}

	@Override
	public void onStart(ISuite paramISuite) {
		try {
			paramISuite.setAttribute("startExecution", Long.valueOf(System.currentTimeMillis()));
			Directory.verifyRequiredFiles();
			// TODO
			Directory.RUNDir += this.runCount;
			Directory.mkDirs(Directory.RUNDir);
			Directory.mkDirs(Directory.RUNDir + Directory.SEP + paramISuite.getName());
			Iterator<XmlTest> localIterator = paramISuite.getXmlSuite().getTests().iterator();
			while (localIterator.hasNext()) {
				XmlTest localXmlTest = localIterator.next();
				Directory.mkDirs(Directory.RUNDir + Directory.SEP + paramISuite.getName() + Directory.SEP
						+ localXmlTest.getName());
			}
		} catch (Exception localException) {
			throw new IllegalStateException(localException);
		}
	}

	public void generateIndexPage() {
		PrintWriter localPrintWriter = null;
		try {
			localPrintWriter = new PrintWriter(Directory.REPORTSDir + Directory.SEP + "index.html");
			IndexPageWriter.header(localPrintWriter);
			IndexPageWriter.content(localPrintWriter, Directory.indexPageDescription);
			IndexPageWriter.footer(localPrintWriter);
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			try {
				localPrintWriter.close();
			} catch (Exception localException3) {
				localPrintWriter = null;
			}
		}
	}

	public void generateCurrentRunPage(long paramLong1, long paramLong2) {
		PrintWriter localPrintWriter = null;
		try {
			localPrintWriter = new PrintWriter(Directory.RUNDir + Directory.SEP + "CurrentRun.html");
			CurrentRunPageWriter.header(localPrintWriter);
			CurrentRunPageWriter.menuLink(localPrintWriter, 0);
			CurrentRunPageWriter.content(localPrintWriter, this.passedTests, this.failedTests, this.skippedTests,
					ConfigurationListener.passedConfigurations, ConfigurationListener.failedConfigurations,
					ConfigurationListener.skippedConfigurations, this.runCount, paramLong1, paramLong2);
			CurrentRunPageWriter.footer(localPrintWriter);
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			try {
				localPrintWriter.close();
			} catch (Exception localException3) {
				localPrintWriter = null;
			}
		}
	}

	public void generateConsolidatedPage() {
		PrintWriter localPrintWriter = null;
		try {
			localPrintWriter = new PrintWriter(Directory.RESULTSDir + Directory.SEP + "ConsolidatedPage.html");
			ConsolidatedReportsPageWriter.header(localPrintWriter);
			ConsolidatedReportsPageWriter.menuLink(localPrintWriter, this.runCount);
			ConsolidatedReportsPageWriter.content(localPrintWriter);
			ConsolidatedReportsPageWriter.footer(localPrintWriter);
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} finally {
			try {
				localPrintWriter.close();
			} catch (Exception localException3) {
				localPrintWriter = null;
			}
		}
	}

	public void startReportingForPassed(List<ITestResult> paramList) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			ITestResult localITestResult = localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
				TestCaseReportsPageWriter.footer(localPrintWriter);
			} catch (FileNotFoundException localFileNotFoundException) {
				localFileNotFoundException.printStackTrace();
			} finally {
				try {
					localPrintWriter.close();
				} catch (Exception localException3) {
					localPrintWriter = null;
				}
			}
		}
	}

	public void startReportingForFailed(List<ITestResult> paramList) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			ITestResult localITestResult = localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
				TestCaseReportsPageWriter.footer(localPrintWriter);
			} catch (FileNotFoundException localFileNotFoundException) {
				localFileNotFoundException.printStackTrace();
			} finally {
				try {
					localPrintWriter.close();
				} catch (Exception localException3) {
					localPrintWriter = null;
				}
			}
		}
	}

	public void startReportingForSkipped(List<ITestResult> paramList) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			ITestResult localITestResult = localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, this.runCount);
				TestCaseReportsPageWriter.footer(localPrintWriter);
			} catch (FileNotFoundException localFileNotFoundException) {
				localFileNotFoundException.printStackTrace();
			} finally {
				try {
					localPrintWriter.close();
				} catch (Exception localException3) {
					localPrintWriter = null;
				}
			}
		}
	}
}