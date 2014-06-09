package es.s2o.automated.test.core.datasource;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.utilities.S2oException;
import es.s2o.automated.test.core.utilities.AbsisUtils;

/**
 * @author s2o
 */
public class ExcelTableDataSource extends AbstractDataSource {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelTableDataSource.class);

	private static final String CR = "\n";

	private final String suite;
	private final Sheet sheet;
	private final Type[] parameterTypes;
	private int actualRow = 0;
	private int firstCol = 0;
	private int lastCol = 0;

	private static final String MODO_DEMO = "#userDemo";

	public ExcelTableDataSource(String suite, Sheet sheet, String tableName, Type[] parameterTypes) {
		this.suite = suite;
		this.sheet = sheet;
		this.parameterTypes = parameterTypes;

		// Validamos tabla encontrada
		CellRangeAddressBase selectedRegion = getTableHeader(tableName, sheet);
		if (selectedRegion == null) {
			LOG.error("No se encontr la tabla. Recuerda que la tabla se define colocando su nombre en la cabecera y haciendo un merge de varias columnas.");
		} else {
			if (LOG.isDebugEnabled()) {
				int rowNum = selectedRegion.getFirstRow();
				int colIndex = selectedRegion.getFirstColumn();
				Cell cell = sheet.getRow(rowNum).getCell(colIndex);
				String value = (String) cellToType(String.class, cell);
				LOG.debug("Tabla utilizada:" + value);
			}

			actualRow = selectedRegion.getFirstRow() + 2;
			firstCol = selectedRegion.getFirstColumn();
			lastCol = firstCol + parameterTypes.length;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasNext() {
		boolean answer = true;
		Object valorCelda = null;
		Row row = sheet.getRow(actualRow);
		if (row != null) {
			// Si ninguna celda de la siguiente fila de la tabla est informada
			// damos por finalizada la tabla
			int column = firstCol;
			while (valorCelda == null && column < lastCol) {
				Cell cell = sheet.getRow(actualRow).getCell(column);
				valorCelda = cellToType(String.class, cell);
				column++;
			}
		}
		if (valorCelda == null) {
			answer = false;
		}

		return answer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] next() {
		final List<Object> objects = new ArrayList<Object>();

		int cj = 0;
		for (int iCell = firstCol; iCell < lastCol; iCell++) {
			Cell cell = sheet.getRow(actualRow).getCell(iCell);
			Object valueCell = cellToType(parameterTypes[cj], cell);
			objects.add(valueCell);
			cj++;
		}
		actualRow++;

		return objects.toArray();
	}

	private Object cellToType(Type returnType, Cell cell) {
		int type;
		Object result = null;

		if (cell != null) {
			type = cell.getCellType();

			switch (type) {
			case Cell.CELL_TYPE_NUMERIC: // numeric value in Excel
				result = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_FORMULA: // precomputed value based on formula
				result = cell.getNumericCellValue();
				break;
			case Cell.CELL_TYPE_STRING: // String Value in Excel
				result = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_BLANK:
				result = null;
				break;
			case Cell.CELL_TYPE_BOOLEAN: // boolean value
				result = cell.getBooleanCellValue();
				break;
			case Cell.CELL_TYPE_ERROR:
				result = null;
				break;
			default:
				throw new RuntimeException("There is no support for this type of cell");
			}

			if (result != null) {
				if (Types.isListOfPrimitivesType(returnType)) {
					String[] lista = result.toString().split(CR);
					List<String> answerList = Arrays.asList(lista);
					result = answerList;
				} else {
					result = ObjectConverter.convert(result, returnType);
				}
			}

		}

		return result; // ObjectConverter.convert(result, returnType);
	}

	/**
	 * Busca la tabla según el entorno y el tipo de usuaro (demo o no). Posibles tablas ordenadas:
	 * 
	 * <pre>
	 * 	tabla
	 * 	tabla#dsv
	 * 	tabla#pre
	 * 	tabla#pro
	 * 	tabla#test
	 * 	tabla#userDemo
	 * 	tabla#userDemo#dsv
	 * 	tabla#userDemo#pre
	 * 	tabla#userDemo#pro
	 * 	tabla#userDemo#test
	 * </pre>
	 * 
	 * @param tableName
	 * @param sheet
	 * @return
	 */
	private CellRangeAddress getTableHeader(String tableName, Sheet sheet) {
		Config config = AbsisConfigFactory.buildConfig(suite);
		Boolean modoDemo = config.getBoolean(AbsisConstants.MODO_DEMO);
		String env = AbsisUtils.environment();

		CellRangeAddress selectedRegion = null;

		List<ExcelTable> tablesFound = new ArrayList<ExcelTable>();
		List<ExcelTable> tablesNames = new LinkedList<ExcelTable>();
		List<String> errorsList = new LinkedList<String>();

		// Obtenemos todas las tablas
		int index = 0, totalMergedRegions = sheet.getNumMergedRegions();
		while (index < totalMergedRegions) {
			selectedRegion = sheet.getMergedRegion(index);
			// Get the cell from the top left hand corner of this
			int rowNum = selectedRegion.getFirstRow();
			int colIndex = selectedRegion.getFirstColumn();
			Cell cell = sheet.getRow(rowNum).getCell(colIndex);
			String value = (String) cellToType(String.class, cell);
			if (value == null) {
				String error = "Excel con datos incorrectos. La celda[" + (rowNum + 1) + "," + (colIndex + 1)
						+ "] contiene celdas con 'merged' pero sin ningún valor válido!!! (asegurarse eliminar celda)";
				errorsList.add(error);
			} else {
				ExcelTable excelTable = new ExcelTable(value, selectedRegion);
				tablesNames.add(excelTable);
			}
			index++;
		}

		// Validamos que no hayan tablas con valores duplicados
		Set<ExcelTable> errorsSet = findDuplicates(tablesNames);
		if (errorsSet.size() > 0) {
			String errorDup = "Excel con datos incorrectos. Existen varias celdas 'merged' (que se debe utilizar sólo para las cabeceras de las columnas) con los mismos valores:";
			errorsList.add(errorDup + errorsSet.toString());
		}

		// Si ha habido un error paramos el proceso!!!
		if (errorsList.size() > 0) {
			throw new S2oException(null, errorsList);
		}

		// El truco para la optimización es ordenarlo
		Collections.sort(tablesNames);
		for (ExcelTable table : tablesNames) {
			if (table.getTableName().startsWith(tableName)) {
				if (modoDemo == false && table.getTableName().contains(MODO_DEMO)) {
					LOG.trace("Como estamos modoDemo == false y getTableName es de userDemo, no hace falta guardar la tabla");
				} else {
					tablesFound.add(table);
				}

			}
		}

		boolean tableNotFound = true;
		if (tablesFound.size() == 0) {
			// Si no se ha encontrado ninguna tabla saltará el error
			throw new S2oException(
					null,
					"Excel con problemas, no se encontró la tabla "
							+ tableName
							+ ". Recuerda que la tabla se define colocando su nombre en la cabecera y haciendo un merge de varias columnas."
							+ ". Con posible valores: tabla,tabla#dsv,tabla#pre,tabla#pro,tabla#test,tabla#userDemo,tabla#userDemo#dsv,tabla#userDemo#pre,tabla#userDemo#pro,tabla#userDemo#test");
		} else if (tablesFound.size() > 1) {
			// la lista tablesFound viene ordenada

			index = tablesFound.size() - 1;
			// Buscamos la tabla del entorno en el que estamos
			while (tableNotFound && index >= 0) {
				if (tablesFound.get(index).getTableName().contains("#" + env)) {
					selectedRegion = tablesFound.get(index).getSelectedRegion();
					tableNotFound = false;
				}
				index--;
			}

			// Si no había tabla con nuestro entorno, será la de por defecto
			if (tableNotFound && modoDemo) {
				// Si estamos en modoDemo buscamos "#userDemo"
				index = tablesFound.size() - 1;
				while (tableNotFound && index >= 0) {
					if (tablesFound.get(index).getTableName().contains(MODO_DEMO)) {
						selectedRegion = tablesFound.get(index).getSelectedRegion();
						tableNotFound = false;
					}
					index--;
				}
			}
		}

		// Si no hemos obtenido la tabla será la de "por defecto"
		if (tableNotFound) {
			selectedRegion = tablesFound.get(0).getSelectedRegion();
		}

		return selectedRegion;
	}

	private Set<ExcelTable> findDuplicates(Collection<ExcelTable> list) {

		Set<ExcelTable> duplicates = new LinkedHashSet<ExcelTable>();
		Set<String> uniques = new HashSet<String>();

		for (ExcelTable excelTable : list) {
			if (!uniques.add(excelTable.getTableName())) {
				duplicates.add(excelTable);
			}
		}

		return duplicates;
	}

	@Override
	public void forEachRemaining(Consumer<? super Object[]> action) {
		// TODO Auto-generated method stub

	}

	// initElements(new DefaultFieldDecorator(factoryRef), page);

	// public static void initElements(FieldDecorator decorator, Object page) {
	// Class proxyIn = page.getClass();
	// while (proxyIn != Object.class) {
	// proxyFields(decorator, page, proxyIn);
	// proxyIn = proxyIn.getSuperclass();
	// }
	// }
	//
	// private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
	// Field[] fields = proxyIn.getDeclaredFields();
	// for (Field field : fields) {
	// Object value = decorator.decorate(page.getClass().getClassLoader(), field);
	// if (value == null)
	// continue;
	// try {
	// field.setAccessible(true);
	// field.set(page, value);
	// } catch (IllegalAccessException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// }

}