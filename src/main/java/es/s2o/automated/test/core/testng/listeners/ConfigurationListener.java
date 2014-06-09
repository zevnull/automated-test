package es.s2o.automated.test.core.testng.listeners;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.testng.IConfigurationListener2;
import org.testng.ITestResult;

import es.s2o.automated.test.core.testng.utils.Directory;
import es.s2o.automated.test.core.testng.writers.TestCaseReportsPageWriter;

public class ConfigurationListener implements IConfigurationListener2 {
	static List<ITestResult> passedConfigurations = new ArrayList<ITestResult>();
	static List<ITestResult> failedConfigurations = new ArrayList<ITestResult>();
	static List<ITestResult> skippedConfigurations = new ArrayList<ITestResult>();

	@Override
	public void onConfigurationFailure(ITestResult paramITestResult) {
		failedConfigurations.add(paramITestResult);
	}

	@Override
	public void onConfigurationSkip(ITestResult paramITestResult) {
		ATUReportsListener.createReportDir(paramITestResult);
		skippedConfigurations.add(paramITestResult);
	}

	@Override
	public void onConfigurationSuccess(ITestResult paramITestResult) {
		passedConfigurations.add(paramITestResult);
	}

	public static void startConfigurationMethodsReporting(int paramInt) {
		startReportingForPassedConfigurations(passedConfigurations, paramInt);
		startReportingForFailedConfigurations(failedConfigurations, paramInt);
		startReportingForSkippedConfigurations(skippedConfigurations, paramInt);
	}

	private static void startReportingForPassedConfigurations(List<ITestResult> paramList, int paramInt) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			ITestResult localITestResult = localIterator.next();
			String str = null;
			str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, paramInt);
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

	private static void startReportingForFailedConfigurations(List<ITestResult> paramList, int paramInt) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			ITestResult localITestResult = localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, paramInt);
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

	private static void startReportingForSkippedConfigurations(List<ITestResult> paramList, int paramInt) {
		PrintWriter localPrintWriter = null;
		Iterator<ITestResult> localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			ITestResult localITestResult = localIterator.next();
			String str = localITestResult.getAttribute("reportDir").toString();
			try {
				localPrintWriter = new PrintWriter(str + Directory.SEP + localITestResult.getName() + ".html");
				TestCaseReportsPageWriter.header(localPrintWriter, localITestResult);
				TestCaseReportsPageWriter.menuLink(localPrintWriter, localITestResult, 0);
				TestCaseReportsPageWriter.content(localPrintWriter, localITestResult, paramInt);
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

	@Override
	public void beforeConfiguration(ITestResult paramITestResult) {
		ATUReportsListener.createReportDir(paramITestResult);
	}
}