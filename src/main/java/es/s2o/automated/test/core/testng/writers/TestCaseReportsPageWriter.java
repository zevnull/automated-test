package es.s2o.automated.test.core.testng.writers;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.testng.ITestResult;
import org.testng.Reporter;

import es.s2o.automated.test.core.testng.enums.Colors;
import es.s2o.automated.test.core.testng.enums.ExceptionDetails;
import es.s2o.automated.test.core.testng.enums.ReportLabels;
import es.s2o.automated.test.core.testng.utils.Directory;
import es.s2o.automated.test.core.testng.utils.Platform;
import es.s2o.automated.test.core.testng.utils.Utils;

public class TestCaseReportsPageWriter extends ReportsPage {
	public static void header(PrintWriter paramPrintWriter, ITestResult paramITestResult) {
		paramPrintWriter
				.println("<!DOCTYPE html>\n\n<html>\n    <head>\n        <title>Pie Charts</title>\n\n        <link rel=\"stylesheet\" type=\"text/css\" href=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/CSS/design.css\" />\n"
						+ "        <link rel=\"stylesheet\" type=\"text/css\" href=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/CSS/jquery.jqplot.css\" />\n"
						+ "\n"
						+ "        <script type=\"text/javascript\" src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/jquery.min.js\"></script>\n"
						+ "        <script type=\"text/javascript\" src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/jquery.jqplot.min.js\"></script>\n"
						+ "        <!--[if lt IE 9]>\n"
						+ "        <script language=\"javascript\" type=\"text/javascript\" src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/excanvas.js\"></script>\n"
						+ "        <![endif]-->\n"
						+ "\n"
						+ "        <script language=\"javascript\" type=\"text/javascript\" src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/JS/jqplot.pieRenderer.min.js\"></script>\n"
						+ "        <script language=\"javascript\" type=\"text/javascript\">"
						+ "$(document).ready(function() {"
						+ " $(\".exception\").hide();"
						+ " $(\"#showmenu\").show();"
						+ " $('#showmenu').click(function() {"
						+ " $('.exception').toggle(\"slide\");"
						+ " });"
						+ " });"
						+ "        </script>"
						+ "    </head>\n"
						+ "    <body>\n"
						+ "        <table id=\"mainTable\">\n"
						+ "            <tr id=\"header\" >\n"
						+ "                <td id=\"logo\">"
						+ "<img src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/IMG/"
						+ ReportLabels.ATU_LOGO.getLabel()
						+ "\" alt=\"Logo\" height=\"80\" width=\"140\"\\> "
						+ "<br/>"
						+ ReportLabels.ATU_CAPTION.getLabel()
						+ "</td>\n"
						+ "                <td id=\"headertext\">\n"
						+ "           "
						+ ReportLabels.HEADER_TEXT.getLabel()
						+ "         \n"
						+ "<div style=\"padding-right:20px;float:right\"><img src=\"../"
						+ getTestCaseHTMLPath(paramITestResult)
						+ "HTML_Design_Files/IMG/"
						+ ReportLabels.PROJ_LOGO.getLabel()
						+ "\" height=\"70\" width=\"140\" /> </i></div>"
						+ "                </td>\n" + "            </tr>");
	}

	public static String getExecutionTime(ITestResult paramITestResult) {
		long l = paramITestResult.getEndMillis() - paramITestResult.getStartMillis();
		if (l > 1000L) {
			l /= 1000L;
			return l + " Sec";
		}
		return l + " Milli Sec";
	}

	private static String getExceptionDetails(ITestResult paramITestResult) {
		String str1 = paramITestResult.getThrowable().toString();
		String str2 = str1;
		if (str1.contains(":"))
			str2 = str1.substring(0, str1.indexOf(":")).trim();
		else
			str1 = "";
		try {
			str2 = getExceptionClassName(str2, str1);
			if (str2.equals("Assertion Error")) {
				if (str1.contains(">")) {
					str2 = str2 + str1.substring(str1.indexOf(":"), str1.lastIndexOf(">") + 1).replace(">", "\"");
					str2 = str2.replace("<", "\"");
				}
				if (paramITestResult.getThrowable().getMessage().trim().length() > 0)
					str2 = paramITestResult.getThrowable().getMessage().trim();
			} else if (str1.contains("{")) {
				str2 = str2 + str1.substring(str1.indexOf("{"), str1.lastIndexOf("}"));
				str2 = str2.replace("{\"method\":", " With ").replace(",\"selector\":", " = ");
			} else if ((str2.equals("Unable to connect Browser")) && (str1.contains("."))) {
				str2 = str2 + ": Browser is Closed";
			} else if (str2.equals("WebDriver Exception")) {
				str2 = paramITestResult.getThrowable().getMessage();
			}
		} catch (ClassNotFoundException localClassNotFoundException) {
		} catch (Exception localException) {
		}
		str2 = str2.replace(">", "\"");
		str2 = str2.replace("<", "\"");
		return str2;
	}

	private static String getExceptionClassName(String paramString1, String paramString2) throws ClassNotFoundException {
		String str = "";
		try {
			str = ExceptionDetails.valueOf(paramString1.trim().replace(".", "_")).getExceptionInfo();
		} catch (Exception localException) {
			str = paramString1;
		}
		return str;
	}

	public static void content(PrintWriter paramPrintWriter, ITestResult paramITestResult, int paramInt) {
		paramPrintWriter
				.println("<td id=\"content\">   \n                    <div class=\"info\">\n                        The following table lists down the Sequential Steps during the Run <br/>\nTestCase Name: <b>"
						+ paramITestResult.getName()
						+ "  :  Iteration "
						+ paramITestResult.getAttribute("iteration").toString()
						+ "</b><br/>"
						+ "                        Time Taken for Executing: <b>"
						+ getExecutionTime(paramITestResult)
						+ "</b> <br/>\n"
						+ "                        Current Run Number: <b>Run "
						+ paramInt
						+ "</b></br>\n"
						+ "Method Type: <b>"
						+ CurrentRunPageWriter.getMethodType(paramITestResult)
						+ "</b></br>" + "                    </div>");
		paramPrintWriter
				.println("<div class=\"chartStyle summary\" style=\"background-color: #646D7E;width: 30%; height: 200px;\">          \n                        <b>Execution Platform Details</b><br/><br/>\n                        <table>\n                            <tr>\n                                <td>O.S</td>\n                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n                                <td>"
						+ Platform.OS
						+ ", "
						+ Platform.OS_ARCH
						+ "Bit, v"
						+ Platform.OS_VERSION
						+ "</td>\n"
						+ "                            </tr>\n"
						+ "                            <tr>\n"
						+ "                                <td>Java</td>\n"
						+ "                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
						+ "                                <td>"
						+ Platform.JAVA_VERSION
						+ "</td>\n"
						+ "                            </tr>\n"
						+ "\n"
						+ "                            <tr>\n"
						+ "                                <td>Hostname</td>\n"
						+ "                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
						+ "                                <td>"
						+ Platform.getHostName()
						+ "</td>\n"
						+ "                            </tr>\n"
						+ "\n"
						+ "                            <tr>\n"
						+ "                                <td>Selenium</td>\n"
						+ "                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
						+ "                                <td>"
						+ Platform.DRIVER_VERSION
						+ "</td>\n"
						+ "                            </tr>\n"
						+ "                        </table>  \n"
						+ "                    </div>\n" + "                   ");
		paramPrintWriter.println(" <div class=\"chartStyle summary\" style=\"background-color: "
				+ getColorBasedOnResult(paramITestResult) + ";margin-left: 20px; height: 200px;width: 30%; \">\n"
				+ "                        <b>Summary</b><br/><br/>\n" + "                        <table>\n"
				+ "                            <tr>\n" + "                                <td>Status</td>\n"
				+ "                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
				+ "                                <td>" + getResult(paramITestResult) + "</td>\n"
				+ "                            </tr>\n" + "                            <tr>\n"
				+ "                                <td>Execution Date</td>\n"
				+ "                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
				+ "                                <td>" + Utils.getCurrentTime() + "</td>\n"
				+ "                            </tr>\n" + "                            \n" + "\n"
				+ "                            <tr>\n" + "                                <td>Browser</td>\n"
				+ "                                <td>&nbsp;&nbsp;:&nbsp;&nbsp;</td>\n"
				+ "                                <td>" + Platform.BROWSER_NAME + "," + Platform.BROWSER_VERSION
				+ "</td>\n" + "                            </tr>\n" + "                        </table> \n"
				+ "                    </div>");

		List<String> localObject1;
		int i;
		String localObject2;
		if (paramITestResult.getStatus() != 3) {
			localObject1 = Reporter.getOutput(paramITestResult);
			paramPrintWriter
					.println("   <div>\n <table class=\"chartStyle\" id=\"tableStyle\" style=\"height:50px; float: left\">\n                            <tr>\n                                <th>S.No</th>\n                                <th>Step Description</th>\n                                <th>Input Value</th>\n                                <th>Expected Value</th>\n                                <th>Actual Value</th>\n                                <th>Time</th>\n                                <th>Line No</th>\n                                <th>Screen shot</th>\n                            </tr>\n                           \n");
			i = 1;
			if (Reporter.getOutput(paramITestResult).size() <= 0) {
				paramPrintWriter.print("<tr>");
				paramPrintWriter.print("<td colspan=\"8\"><b>No Steps Available</b></td>");
				paramPrintWriter.print("</tr>");
			}
			i = 1;
			Iterator<String> localIterator = localObject1.iterator();
			while (localIterator.hasNext()) {
				localObject2 = localIterator.next();

				paramPrintWriter.print("<tr>");
				paramPrintWriter.println("<td>" + i + "</td>");
				paramPrintWriter.print("<td style=\"text-align:left\" colspan=\"8\">" + (String) localObject2
						+ "</td></tr>");
				i++;

			}
			paramPrintWriter.print("\n                        </table>  \n");
		}
		if ((paramITestResult.getParameters().length > 0) || (paramITestResult.getStatus() == 3)
				|| (paramITestResult.getStatus() == 2)) {
			paramPrintWriter
					.println(" <div class=\"chartStyle summary\" style=\"color: black;width: 98%; height: 100%; padding-bottom: 30px;\">          \n");
			if (paramITestResult.getParameters().length > 0) {
				paramPrintWriter.print("<b>Parameters: </b><br/>");
				for (Object localObject3 : paramITestResult.getParameters())
					paramPrintWriter.print("Param: " + localObject3.toString() + "<br/>");
			}

			if (paramITestResult.getStatus() == 2) {
				paramPrintWriter.println("                        <br/><br/><b>Reason for Failure:&nbsp;&nbsp;</b>"
						+ getExceptionDetails(paramITestResult) + "<br/><br/>\n"
						+ "<b id=\"showmenu\">Click Me to Show/Hide the Full Stack Trace</b>"
						+ "<div class=\"exception\">");
				paramITestResult.getThrowable().printStackTrace(paramPrintWriter);
				paramPrintWriter.print("</div>");
			}
			paramPrintWriter.print("                    </div>");
		}
		paramPrintWriter.print("                    </div>\n\n                </td>\n            </tr>");
	}

	private static String getColorBasedOnResult(ITestResult paramITestResult) {
		if (paramITestResult.getStatus() == 1)
			return Colors.PASS.getColor();
		if (paramITestResult.getStatus() == 2)
			return Colors.FAIL.getColor();
		if (paramITestResult.getStatus() == 3)
			return Colors.SKIP.getColor();
		return Colors.PASS.getColor();
	}

	private static String getResult(ITestResult paramITestResult) {
		if (paramITestResult.getStatus() == 1)
			return "Passed";
		if (paramITestResult.getStatus() == 2)
			return "Failed";
		if (paramITestResult.getStatus() == 3)
			return "Skipped";
		return "Unknown";
	}

	public static String getTestCaseHTMLPath(ITestResult paramITestResult) {
		String str = paramITestResult.getAttribute("reportDir").toString();
		str = str.substring(str.indexOf(Directory.RESULTSDir) + (Directory.RESULTSDir.length() + 1));
		String[] arrayOfString = str.replace(Directory.SEP, "##@##@##").split("##@##@##");
		str = "";
		for (int i = 0; i < arrayOfString.length; i++)
			str = str + "../";
		return str;
	}

	public static void menuLink(PrintWriter paramPrintWriter, ITestResult paramITestResult, int paramInt) {
		getTestCaseHTMLPath(paramITestResult);
		paramPrintWriter
				.println("\n            <tr id=\"container\">\n                <td id=\"menu\">\n                    <ul> \n");
		paramPrintWriter.println(" <li class=\"menuStyle\"><a href=\"../" + getTestCaseHTMLPath(paramITestResult)
				+ "index.html\" >Index</a></li>" + "<li style=\"padding-top: 4px;\"><a href=\""
				+ getTestCaseHTMLPath(paramITestResult) + "ConsolidatedPage.html\" >Consolidated Page</a></li>\n");
		if (paramInt == 1)
			paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"" + Directory.RUNName + paramInt
					+ Directory.SEP + "CurrentRun.html\" >" + "Run " + paramInt + " </a></li>\n");
		else
			for (int i = 1; i <= paramInt; i++) {
				if (i == paramInt) {
					paramPrintWriter.println("\n <li style=\"padding-top: 4px;padding-bottom: 4px;\"><a href=\""
							+ Directory.RUNName + i + Directory.SEP + "CurrentRun.html\" >" + "Run " + i
							+ " </a></li>\n");
					break;
				}
				paramPrintWriter.println("\n <li class=\"menuStyle\"><a href=\"" + Directory.RUNName + i
						+ Directory.SEP + "CurrentRun.html\" >" + "Run " + i + " </a></li>\n");
			}
		paramPrintWriter.println("\n                    </ul>\n                </td>\n\n");
	}
}