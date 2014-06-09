package es.s2o.automated.test.core.utilities;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;

/**
 * Resumen utilizadades para interactuar con el navegador
 * 
 * @author s2o
 */
public class WebDriverShortcuts {

	/**
	 * <pre>
	 *  Hace un driver.navigate().to()
	 *  donde la url que se le pasa es relativa y obtiene la url base de la configuración.
	 * </pre>
	 * 
	 * @param driver
	 * @param url
	 */
	public static void navigateTo(WebDriver driver, String url) {
		String urlBase = AbsisConfigFactory.getConfig().getString(AbsisConstants.URL_BASE);
		driver.navigate().to(urlBase + url);
	}

	/**
	 * Obtiene un WebElement de la página a partir de su id
	 * 
	 * @param driver
	 * @param id
	 * @return
	 */
	public WebElement getElementByID(WebDriver driver, String id) {
		return driver.findElement(By.id(id));
	}

	/**
	 * @param driver
	 * @param textPresent
	 * @return
	 */
	public static boolean isAjaxTextPresent(WebDriver driver, String textPresent) {
		boolean answer = true;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			By xpath = By.xpath("//*[contains(.,'" + textPresent + "')]");
			wait.until(ExpectedConditions.presenceOfElementLocated(xpath));
			// wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(.,'" + textPresent +
			// "')]")));
		} catch (Exception e) {
			answer = false;
		}
		return answer;
	}

	/**
	 * @param driver
	 * @param textPresent
	 * @return
	 */
	public static boolean isAjaxElementClicablePresent(WebDriver driver, WebElement webElement) {
		boolean answer = true;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(webElement));
		} catch (Exception e) {
			answer = false;
		}
		return answer;
	}

	/**
	 * <pre>
	 * Verifica que un elemento esté presente según su id. 
	 * Si queremos verificar que NO esté presente utilizar {@isElementNotPresent}
	 * </pre>
	 * 
	 * @param driver
	 * @param webElement
	 * @return
	 */
	public static boolean isIdPresent(WebDriver driver, String idElement) {
		boolean exists = true;

		try {
			By cert = By.id(idElement);
			driver.findElement(cert);
		} catch (Exception e) {
			exists = false;
		}

		return exists;
	}

	/**
	 * <pre>
	 * Verifica que un elemento esté presente. 
	 * Si queremos verificar que NO esté presente utilizar {@isElementNotPresent}
	 * </pre>
	 * 
	 * @param driver
	 * @param webElement
	 * @return
	 */
	public static boolean isElementPresent(WebDriver driver, WebElement webElement) {
		boolean exists = true;

		try {
			webElement.getTagName();
		} catch (Exception e) {
			exists = false;
		}

		return exists;
	}

	/**
	 * <pre>
	 * Si queremos verificar que un elemento no está presente,
	 * utilizamos este método que no tiene una espera prolongada.
	 * </pre>
	 * 
	 * @param webElement
	 * @return
	 */
	public static boolean isElementNotPresent(WebDriver driver, WebElement webElement) {
		boolean exists = false;

		driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

		try {
			webElement.getTagName();
		} catch (Exception e) {
			exists = true;
		}
		int secondWaiting = AbsisConfigFactory.getConfig().getInt(AbsisConstants.IMPLICITLY_WAIT);
		driver.manage().timeouts().implicitlyWait(secondWaiting, TimeUnit.SECONDS);

		return exists;
	}

	/**
	 * @param text
	 * @return
	 */
	public static boolean isTextPresent(WebDriver driver, String textPresent) {
		boolean exists = true;

		try {
			By xpath = By.xpath("//*[contains(.,'" + textPresent + "')]");
			driver.findElement(xpath);
			// driver.findElement(By.xpath("//*[contains(.,'" + textPresent + "')]"));
		} catch (Exception e) {
			exists = false;
		}

		return exists;
	}

	/**
	 * <pre>
	 * Si queremos verificar que un texto no está presente,
	 * utilizamos este método que no tiene una espera prolongada.
	 * </pre>
	 * 
	 * @param webElement
	 * @return
	 */
	public static boolean isTextNotPresent(WebDriver driver, String textPresent) {
		boolean notExists = false;

		driver.manage().timeouts().implicitlyWait(100, TimeUnit.MILLISECONDS);

		try {
			By xpath = By.xpath("//*[contains(.,'" + textPresent + "')]");
			driver.findElement(xpath);
		} catch (Exception e) {
			notExists = true;
		}
		int secondWaiting = AbsisConfigFactory.getConfig().getInt(AbsisConstants.IMPLICITLY_WAIT);
		driver.manage().timeouts().implicitlyWait(secondWaiting, TimeUnit.SECONDS);

		return notExists;
	}

	/**
	 * @param text
	 * @return
	 */
	public static boolean isTextPresentAjax(WebDriver driver, String textPresent) {
		boolean exists = true;

		try {
			// By.xpath("//*[contains(.,'" + textPresent + "')]")
			By xpath = By.xpath("//*[contains(.,'" + textPresent + "')]");
			WebDriverWait wait = new WebDriverWait(driver, 10);
			wait.until(ExpectedConditions.presenceOfElementLocated(xpath));
		} catch (Exception exep) {
			exists = false;
		}

		return exists;
	}

	/**
	 * Devuelve el texto del body de la página
	 * 
	 * @return
	 */
	public String getPageText(WebDriver driver) {
		try {
			return driver.findElement(By.tagName("body")).getText();
		} catch (Exception e) {
			// Maybe this is not HTML
			return driver.getPageSource();
		}
	}

	/**
	 * Devuelve el código fuente de la página (puede que no funcione si hay peticiones AJAX ejecutandose)
	 * 
	 * @return
	 */
	public String getPageSource(WebDriver driver) {
		return driver.getPageSource();
	}

	/**
	 * Para setear valores a un campo hidden se debe hacer por JS
	 * 
	 * @param driver
	 * @param inputName
	 * @param text
	 */
	public void setHiddenField(WebDriver driver, String inputName, String text) {
		WebElement element = driver.findElement(By.name(inputName));
		((JavascriptExecutor) driver).executeScript("arguments[0].value=" + AbsisUtils.escapeQuotes(text), element);
	}

	/**
	 * <pre>
	 * Ejecuta código JS que retorna valores,
	 * sobre el valor de retorno se tendrá que hacer cast:
	 * (String)retorn, (Long)retorn...
	 * </pre>
	 * 
	 * @param driver
	 * @param code
	 * @return
	 */
	public static Object executeJS(WebDriver driver, String code) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		return js.executeScript(code);
	}

	/**
	 * Ejecuta código JS sin esperar retorno
	 * 
	 * @param driver
	 * @param code
	 */
	public static void executeJSWithNoReturn(WebDriver driver, String code) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript(code);
	}

	/**
	 * Obtiene el texto de un mensaje de alerta
	 * 
	 * @param driver
	 * @return
	 */
	public static String getAlertText(WebDriver driver) {
		Alert alert = driver.switchTo().alert();
		return alert.getText();
	}

	/**
	 * Acepta el mensaje de alerta
	 * 
	 * @param driver
	 */
	public static void acceptAlert(WebDriver driver) {
		driver.switchTo().alert().accept();
	}

	/**
	 * No acepta un mensaje de alerta
	 * 
	 * @param driver
	 */
	public static void dismissAlert(WebDriver driver) {
		Alert alert = driver.switchTo().alert();
		alert.dismiss();
	}

	/**
	 * Enviamos el peticiones a un mensaje de alerta
	 * 
	 * @param driver
	 * @param str
	 */
	public static void sendKeysAlert(WebDriver driver, String str) {
		Alert alert = driver.switchTo().alert();
		alert.sendKeys(str);
	}

	/**
	 * Sobre una página con frames, nos posicionamos en el frame que toca
	 * 
	 * @param driver
	 * @param name
	 */
	public static void switchToFrame(WebDriver driver, String name) {
		driver.switchTo().frame(name);
	}

	/**
	 * Sobre una página con frames nos posicionamos sobre el root
	 * 
	 * @param driver
	 */
	public static void switchToRoot(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * Verifica la existencia de una ventana a partir de su nombre
	 * 
	 * @param driver
	 * @param windowName
	 * @return
	 */
	public boolean hasWindow(WebDriver driver, String windowName) {
		// Save current handle
		String current = driver.getWindowHandle();
		try {
			driver.switchTo().window(windowName);
			driver.switchTo().window(current);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Verifica la existencia de una ventana a partir de su título
	 * 
	 * @param driver
	 * @param windowTitle
	 * @return
	 */
	public boolean hasWindowByTitle(WebDriver driver, String windowTitle) {
		// Save current handle
		String current = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
			if (driver.getTitle().equals(windowTitle)) {
				driver.switchTo().window(current);
				return true;
			}
		}
		driver.switchTo().window(current);
		return false;
	}

	/**
	 * Se posiciona sobre una ventana a partir de su nombre
	 * 
	 * @param driver
	 * @param windowName
	 */
	public void gotoWindow(WebDriver driver, String windowName) {
		driver.switchTo().window(windowName);
	}

	/**
	 * Se posiciona sobre una ventana a partir de su título
	 * 
	 * @param driver
	 * @param title
	 */
	public void gotoWindowByTitle(WebDriver driver, String title) {
		// Save current handle
		String current = driver.getWindowHandle();
		for (String handle : driver.getWindowHandles()) {
			driver.switchTo().window(handle);
			if (title.equals(driver.getTitle())) {
				return;
			}
		}
		driver.switchTo().window(current);
		// Igual me paso lanzando una excepcion!!!
		throw new S2oException(driver, "No window found with title [" + title + "]");
	}

	/**
	 * Se posiciona sobre la ventana principal
	 * 
	 * @param driver
	 */
	public void gotoRootWindow(WebDriver driver) {
		driver.switchTo().defaultContent();
	}

	/**
	 * @param driver
	 * @return
	 */
	private int getWindowCount(WebDriver driver) {
		return driver.getWindowHandles().size();
	}

	/**
	 * Cierra la ventana (que hace frio!)
	 * 
	 * @param driver
	 */
	public void closeWindow(WebDriver driver) {
		driver.close();
		if (getWindowCount(driver) > 0) {
			driver.switchTo().window(driver.getWindowHandles().iterator().next());
		}
	}

	/**
	 * Función especifica para AJAX
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 */
	public static boolean explicitWait(WebDriver driver, By locator) {
		boolean answer = true;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			answer = false;
		}
		return answer;
	}

	/**
	 * Función especifica para AJAX
	 * 
	 * @param driver
	 * @param locator
	 * @return
	 */
	public static WebElement findElementByAjax(WebDriver driver, By locator) {
		WebElement answer = null;
		try {
			WebDriverWait wait = new WebDriverWait(driver, 60);
			answer = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch (Exception e) {
			answer = null;
		}
		return answer;
	}

	/**
	 * <pre>
	 * Esperamos explicitamente por 1 segundo.
	 * Hace un Thread.sleep, así que no utilizar
	 * si no sabemos lo que estamos haciendo.
	 * (Se trataría de un "último recurso")
	 * </pre>
	 * 
	 * @param locator
	 */
	public static void waitAMomentPlease() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}

	/**
	 * <pre>
	 * Esperamos explicitamente por 1 segundo.
	 * Hace un Thread.sleep, así que no utilizar
	 * si no sabemos lo que estamos haciendo.
	 * (Se trataría de un "último recurso")
	 * </pre>
	 * 
	 * @param locator
	 */
	public static void waitAMomentPlease(long miliseconds) {
		try {
			Thread.sleep(miliseconds);
		} catch (Exception e) {
		}
	}

	/**
	 * Busca en la página un determinado patrón (texto). Utilizado para encontrar links cuyo texto visible contiene
	 * acentos.
	 * 
	 * @param pattern
	 * @return
	 */
	public static WebElement findLinkByPattern(WebDriver driver, Pattern pattern) {
		WebElement found = null;
		List<WebElement> invLink = driver.findElements(By.tagName("a"));
		for (int i = 0; i < invLink.size(); i++) {
			String actual = invLink.get(i).getText();
			Matcher mat = pattern.matcher(actual);
			if (mat.find()) {
				found = invLink.get(i);
				break;
			}
		}
		return found;
	}

	/**
	 * Busca en una parte de la página un determinado patrón (texto). Utilizado para encontrar links cuyo texto visible
	 * contiene acentos.
	 * 
	 * @param container
	 * @param pattern
	 * @return
	 */
	public static WebElement findLinkByPatternAndContainer(WebElement container, Pattern pattern) {
		WebElement found = null;
		List<WebElement> invLink = container.findElements(By.tagName("a"));
		for (int i = 0; i < invLink.size(); i++) {
			String actual = invLink.get(i).getText();
			Matcher mat = pattern.matcher(actual);
			if (mat.find()) {
				found = invLink.get(i);
				break;
			}
		}
		return found;
	}
	
	/**
	 * Inyecta el tipoTerminal tf7 en el userProfile
	 * 
	 */
	public static void setTF7Terminal(WebDriver driver) {
		String urlDemoArq = AbsisConfigFactory.getConfig().getString(AbsisConstants.URL_DEMO_ARQ);
		String urlDemo = AbsisConfigFactory.getConfig().getString(AbsisConstants.HOME_PAGE);
		navigateTo(driver, urlDemoArq);
		WebElement key = driver.findElement(By.id("key"));
		WebElement value = driver.findElement(By.id("value"));
		WebElement button = driver.findElement(By.id("button1"));
		key.clear();
		key.sendKeys("tipoTerminal");
		value.clear();
		value.sendKeys(AbsisConstants.TERMINAL_TF7);
		button.click();
		navigateTo(driver, urlDemo);
	}

	 /**
	 * 
	 * @param driver
	 * @param name
	 */
	public static void switchToFrame(WebDriver driver, WebElement name) {
			driver.switchTo().frame(name);
	}
}
