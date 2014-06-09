package es.s2o.automated.test.core.driver;

public enum Browser {
	FIREFOX(org.openqa.selenium.firefox.FirefoxDriver.class), IEXPLORER(
			org.openqa.selenium.ie.InternetExplorerDriver.class), CHROME(org.openqa.selenium.chrome.ChromeDriver.class);

	private Class<?> driverClass;

	public Class<?> getDriverClass() {
		return driverClass;
	}

	private Browser(Class<?> driverClass) {
		this.driverClass = driverClass;
	}

	// @JsonCreator
	public static Browser fromJson(String text) {
		return valueOf(text.toUpperCase());
	}
}