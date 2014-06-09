package es.s2o.automated.test.core.html;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an expected table for comparison with an actual html table.
 */
public class Table {

	private final List<Row> rows = new ArrayList<Row>();

	/**
	 * Construct a table without providing any contents; they can be appended subsequently.
	 */
	public Table() {
	}

	/**
	 * Construct a table from a two dimensional array of objects. Each object's string value will be used with a colspan
	 * of 1, unless an object is an {@link net.sourceforge.jwebunit.html.Cell}, in which case its defined value and
	 * colspan are used.
	 * 
	 * @param values
	 *            two-dimensional array representing table cells.
	 */
	public Table(Object[][] values) {
		appendRows(values);
	}

	/**
	 * Append any number of rows, represented by a two dimensional array of objects. Each object's string value will be
	 * used with a colspan of 1, unless an object is an {@link net.sourceforge.jwebunit.html.Cell}, in which case its
	 * defined value and colspan are used.
	 * 
	 * @param newExpectedValues
	 *            two-dimensional array representing expected table cells.
	 */
	public void appendRows(Object[][] newExpectedValues) {
		for (int i = 0; i < newExpectedValues.length; i++) {
			rows.add(new Row(newExpectedValues[i]));
		}
	}

	/**
	 * Append another table's rows.
	 * 
	 * @param table
	 *            table whose rows are to be appended.
	 */
	public void appendRows(Table table) {
		rows.addAll(table.getRows());
	}

	/**
	 * Append a single expected row.
	 * 
	 * @param row
	 *            row to be appended.
	 */
	public void appendRow(Row row) {
		rows.add(row);
	}

	public int getRowCount() {
		return getRows().size();
	}

	public List<Row> getRows() {
		return rows;
	}

	public boolean hasText(String text) {
		for (int i = 0; i < getRowCount(); i++) {
			Row row = getRows().get(i);
			if (row.hasText(text))
				return true;
		}
		return false;
	}

}
