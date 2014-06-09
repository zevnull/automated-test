package es.s2o.automated.test.core.testng.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.openqa.selenium.internal.BuildInfo;

public class Platform {
	private static BuildInfo driverInfo = new BuildInfo();
	public static final String DRIVER_VERSION = driverInfo.getReleaseLabel();
	public static final String DRIVER_REVISION = driverInfo.getBuildRevision();
	public static final String USER = System.getProperty("user.name");
	public static final String OS = System.getProperty("os.name");
	public static final String OS_ARCH = System.getProperty("os.arch");
	public static final String OS_VERSION = System.getProperty("os.version");
	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static String BROWSER_NAME = "Unknown";
	public static String BROWSER_VERSION = "";

	public static String getHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException localUnknownHostException) {
		}
		return "Unknown";
	}
}