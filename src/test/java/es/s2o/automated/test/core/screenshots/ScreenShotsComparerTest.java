package es.s2o.automated.test.core.screenshots;

import org.testng.Assert;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.annotation.ExcelDataProvider;
import es.s2o.automated.test.core.screenshots.ScreenShotsComparerTestBase;

/**
 * Simplemente llama al método base pasándole los parámetros correspondientes.<br/>
 * Se debe tener informados los parámetros pages.urlBase y pages.urlBase2<br/>
 * 
 * @author s2o
 */
public class ScreenShotsComparerTest extends ScreenShotsComparerTestBase {

	/**
	 * Verifica que las pantallas son iguales para una url dada entre urlBase y urlBase2
	 * 
	 * @param urlRelativa
	 * @param verificacionPaginaOK
	 * @param ficheroSalida
	 */
	@Test(dataProviderClass = es.s2o.automated.test.core.datasource.ExternalDataProvider.class, dataProvider = "getData")
	@ExcelDataProvider(excelPathName = "testScreenShots.xlsx", table = "testVerificacionScreenShots", sheetName = "Componentes")
	public void testVerificacionScreenShots(String urlRelativa, String verificacionPaginaOK, String ficheroSalida) {

		boolean result = super.testEvaluateImage(urlRelativa, verificacionPaginaOK, ficheroSalida);
		Assert.assertTrue(result, "Verificando url:" + urlRelativa);
	}

}
