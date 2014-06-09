package es.s2o.automated.test.core.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;

import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * @author s2o
 */
public class LOELoginStep1Page {

	private final WebDriver driver;

	@FindBy(how = How.CSS, using = "input[type='submit']")
	private List<WebElement> submit;

	private WebElement f1;

	public LOELoginStep1Page(WebDriver driver) {
		this.driver = driver;
	}

	public boolean isTheActualPage() {
		return WebDriverShortcuts.isElementPresent(driver, f1);
	}

	public void loginAsDemo() {
		submit.get(0).submit();
	}

}
