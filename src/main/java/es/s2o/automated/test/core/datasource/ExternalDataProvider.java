package es.s2o.automated.test.core.datasource;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Iterator;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

import es.s2o.automated.test.core.annotation.ExcelDataProvider;

/**
 * <pre>
 * External data provider, esta pensado para poder obtener datos de diferentes fuentes.
 * Inicialmente implemento slo de excel (xlsx/xls)
 * </pre>
 * 
 * @author ssacrist
 */
public class ExternalDataProvider {

	@DataProvider(name = "getData")
	public static Iterator<Object[]> getData(ITestContext contex, Method testMethod) throws Exception {

		String suite = contex.getSuite().getName();

		// A partir del tipo de anotacin sabremos el tipo de fuente de datos que queremos
		// En este caso slo hay un tipo implementado
		ExcelDataProvider dataProvider = testMethod.getAnnotation(ExcelDataProvider.class);

		final Type[] parameterTypes = testMethod.getGenericParameterTypes();
		ExcelTableDataSource table = new ExcelTypeProvider().getTableData(suite, dataProvider, parameterTypes);

		return table;
	}

}
