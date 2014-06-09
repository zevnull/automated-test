package es.s2o.automated.test.core.screenshots;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import es.s2o.automated.test.core.annotation.SeleniumWebDriver;
import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.model.UrlBase;
import es.s2o.automated.test.core.utilities.WebDriverShortcuts;

/**
 * Test Padre para la comparación de pantallas entre 2 entornos Ya están definidos los campos driver y driver2 para
 * poder ser utilizados por sus hijos
 * 
 * @author s2o
 */
public class ScreenShotsComparerTestBase {

	@SeleniumWebDriver
	protected WebDriver driver;

	@SeleniumWebDriver(urlBase = UrlBase.SECUNDARIA)
	protected WebDriver driver2;

	/**
	 * Navega hasta las url's dadas y las compara. En caso de error guarda pantallazo en log
	 * 
	 * @param urlRelativa
	 *            para realizar la comparación entre 2 entornos
	 * @param verificacionPaginaOK
	 * @param ficheroSalida
	 * @return
	 */
	public boolean testEvaluateImage(String urlRelativa, String verificacionPaginaOK, String ficheroSalida) {

		navigateTo(urlRelativa);
		navigate2To(urlRelativa);

		boolean urlPrincipalOk = WebDriverShortcuts.isTextPresent(driver, verificacionPaginaOK);
		Assert.assertTrue(urlPrincipalOk, "Verificando url:" + AbsisConstants.URL_BASE + urlRelativa);

		boolean urlSecundariaOk = WebDriverShortcuts.isTextPresent(driver2, verificacionPaginaOK);
		Assert.assertTrue(urlSecundariaOk, "Verificando url:" + AbsisConstants.URL_BASE2 + urlRelativa);

		return compareBrowsers(ficheroSalida);
	}

	/**
	 * Compara los dos navegadores y si da error guarda un pantallazo en el log
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean compareBrowsers(String fileName) {
		ImageComparer imageComparer = new ImageComparer();

		// Obtenemos el pantallazo y lo pasamos a jpg
		BufferedImage bufferedImage1 = DriverScreenShoot.takeScreenShot(driver);
		BufferedImage bufferJpg1 = new BufferedImage(bufferedImage1.getWidth(), bufferedImage1.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		bufferJpg1.createGraphics().drawImage(bufferedImage1, 0, 0, Color.WHITE, null);

		// Obtenemos el pantallazo y lo pasamos a jpg
		BufferedImage bufferedImage2 = DriverScreenShoot.takeScreenShot(driver2);
		BufferedImage bufferJpg2 = new BufferedImage(bufferedImage2.getWidth(), bufferedImage2.getHeight(),
				BufferedImage.TYPE_INT_RGB);
		bufferJpg2.createGraphics().drawImage(bufferedImage2, 0, 0, Color.WHITE, null);

		// Comparamos las imagenes y si son diferentes guardamos una imagen con la diferencia
		ComparisonResult comparisonResult = imageComparer.compare(bufferJpg1, bufferJpg2);
		if (comparisonResult.isMatch() == false) {
			BufferedImage bufferedImageResult = comparisonResult.getChangeIndicator(bufferedImage1);
			DriverScreenShoot.saveImageResult(fileName, bufferedImageResult);
		}

		return comparisonResult.isMatch();
	}

	/**
	 * @param urlPage
	 */
	private void navigateTo(String urlPage) {
		WebDriverShortcuts.navigateTo(driver, urlPage);
	}

	private void navigate2To(String url) {
		String urlBase = AbsisConfigFactory.getConfig().getString(AbsisConstants.URL_BASE2);
		driver2.navigate().to(urlBase + url);
	}
}
