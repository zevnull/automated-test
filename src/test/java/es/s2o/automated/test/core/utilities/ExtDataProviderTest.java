package es.s2o.automated.test.core.utilities;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.annotation.ExcelDataProvider;
import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.utilities.AbsisUtils;

public class ExtDataProviderTest {

	/**
	 * Se prueba una única columana en la tabla
	 * 
	 * @param pNif
	 */
	@Test(dataProviderClass = es.s2o.automated.test.core.datasource.ExternalDataProvider.class, dataProvider = "getData")
	@ExcelDataProvider(excelPathName = "testDemoData.xlsx", table = "testTable", sheetName = "Servicios")
	public void testBuscadorData(String property, String actualResultId, String expectedResult) {
		Config config = AbsisConfigFactory.getConfig();
		if (config.getBoolean(AbsisConstants.MODO_DEMO) && "tst".equals(AbsisUtils.environment())) {
			Assert.assertEquals(actualResultId, "table en tst y demo");
		}
		if (!config.getBoolean(AbsisConstants.MODO_DEMO) && "tst".equals(AbsisUtils.environment())) {
			Assert.assertEquals(actualResultId, "table en tst");
		}
		if (config.getBoolean(AbsisConstants.MODO_DEMO) && !"tst".equals(AbsisUtils.environment())) {
			Assert.assertEquals(actualResultId, "table user demo");
		}
		if (!config.getBoolean(AbsisConstants.MODO_DEMO) && !"tst".equals(AbsisUtils.environment())) {
			Assert.assertEquals(actualResultId, "table por defecto");
		}

	}

	/**
	 * Se prueba un listado como argumento
	 * 
	 * @param pNif
	 * @param listadoString
	 */
	@Test(dataProviderClass = es.s2o.automated.test.core.datasource.ExternalDataProvider.class, dataProvider = "getData")
	@ExcelDataProvider(excelPathName = "testDemoData.xlsx", table = "tagsPage", sheetName = "Canal")
	public void testBuscadorDataList(String pNif, List<String> listadoString) {

		Assert.assertEquals(listadoString.size(), 16);
	}

}
