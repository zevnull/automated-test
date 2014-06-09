package es.s2o.automated.test.annotation;

import static org.testng.AssertJUnit.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.annotation.SeleniumWebDriver;
import es.s2o.automated.test.core.listeners.WebDriverSuiteListener;
import es.s2o.automated.test.core.listeners.WebDriverTestListener;

@Test
@Listeners({ WebDriverTestListener.class, WebDriverSuiteListener.class })
public class TestClassDriverDisabled {

	@SeleniumWebDriver
	private WebDriver driver;

	public void assertClassDriverUninitialized() {
		driver.findElement(
				By.cssSelector("li.archivedate.expanded > ul.hierarchy > li.archivedate.expanded > a.toggle > span.zippy"))
				.click();
		driver.findElement(By.cssSelector("li.archivedate.collapsed > a.toggle > span.zippy")).click();
		driver.findElement(
				By.cssSelector("li.archivedate.expanded > ul.hierarchy > li.archivedate.expanded > a.toggle > span.zippy"))
				.click();
		driver.findElement(By.xpath("//div[@id='BlogArchive1_ArchiveList']/ul/li/ul[2]/li/a/span")).click();

		driver.findElement(By.linkText("AndroMDA vs Acceleo (MDA)")).click();
		assertTrue(driver != null);
	}

}
