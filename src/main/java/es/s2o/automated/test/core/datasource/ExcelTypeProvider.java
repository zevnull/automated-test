package es.s2o.automated.test.core.datasource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.s2o.automated.test.core.annotation.ExcelDataProvider;
import es.s2o.automated.test.core.utilities.S2oException;

/**
 * <pre>
 * Obtiene datos de tablas definidas en un excel. 
 * Donde una tabla se define por tener en la cabecera celdas combinadas.
 * 	El número de columnas de la tabla será igual al número de celdas combinadas de la cabecera
 * 	El número de filas vendrá dado por encontrar una fila en que ninguna de sus columnas 
 * 	esté informada.
 * 
 * </pre>
 * 
 * @author s2o
 */
public class ExcelTypeProvider {

	private static final Logger LOG = LoggerFactory.getLogger(ExternalDataProvider.class);

	// Nos guradamos la referencia al excel
	private final HashMap<String, Workbook> excelList = new HashMap<String, Workbook>();

	/**
	 * @param suite
	 * @param xlFilePath
	 * @param sheetName
	 * @return
	 * @throws IOException
	 * @throws InvalidFormatException
	 */
	public ExcelTableDataSource getTableData(String suite, ExcelDataProvider dataProvider, final Type[] parameterType) {

		validarDatosAnotacionExcel(dataProvider);

		LOG.info("Obteniendo datos desde el excel {}, pestaña {} y tabla {}", dataProvider.excelPathName(),
				dataProvider.sheetName(), dataProvider.table());

		Workbook workbook = getWorkbook(dataProvider.excelPathName());
		Sheet sheet = workbook.getSheet(dataProvider.sheetName());
		ExcelTableDataSource table = new ExcelTableDataSource(suite, sheet, dataProvider.table(), parameterType);

		return table;
	}

	private Workbook getWorkbook(String xlFilePath) {

		Workbook workbook = excelList.get(xlFilePath);
		try {
			if (workbook == null) {
				File file = new File(getClass().getClassLoader().getResource(xlFilePath).getFile());
				workbook = WorkbookFactory.create(file);
				excelList.put(xlFilePath, workbook);
			}
		} catch (Exception ex) {
			throw new S2oException(null, "Failed to open file for " + xlFilePath, ex);
		}
		return workbook;
	}

	/**
	 * @param testMethod
	 * @param dataProvider
	 * @return
	 */
	private void validarDatosAnotacionExcel(ExcelDataProvider dataProvider) {

		final List<String> errorCollectors = new ArrayList<String>();
		if (dataProvider.excelPathName() == null || dataProvider.excelPathName().isEmpty()) {
			errorCollectors.add("Nombre del fichero excel vacío");
		}
		if (dataProvider.sheetName() == null || dataProvider.sheetName().isEmpty()) {
			errorCollectors.add("No se ha definido la pestaña sobre la que se han de obtener los datos del excel");
		}
		if (dataProvider.table() == null || dataProvider.table().isEmpty()) {
			errorCollectors.add("No se ha definido la tabla del excel");
		}
		if (errorCollectors.size() > 0) {
			throw new S2oException(null, errorCollectors);
		}
	}
}