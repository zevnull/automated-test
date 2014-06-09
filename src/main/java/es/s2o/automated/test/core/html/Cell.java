package es.s2o.automated.test.core.html;


/**
 * Represents a cell of an html table - a string value spanning an indicated amount of columns.
 */
public class Cell {

	private final int colspan;

	private final int rowspan;

	private final String value;

	/**
	 * Construct a cell with a default colspan/rowspan of 1.
	 * 
	 * @param value
	 *            text expected within the cell.
	 */
	public Cell(String value) {
		this(value, 1, 1);
	}

	/**
	 * Construct a cell with a specified colspan.
	 * 
	 * @param value
	 *            text expected within the cell.
	 * @param colspan
	 *            number of columns the cell is expected to span.
	 * @param rowspan
	 *            number of rows the cell is expected to span.
	 */
	public Cell(String value, int colspan, int rowspan) {
		this.value = value;
		this.colspan = colspan;
		this.rowspan = rowspan;
	}

	/**
	 * @return the colspan for this cell.
	 */
	public int getColspan() {
		return colspan;
	}

	/**
	 * @return the rowspan for this cell.
	 */
	public int getRowspan() {
		return rowspan;
	}

	/**
	 * @return the text for the cell.
	 */
	public final String getValue() {
		return value;
	}

	/**
	 * Check if the current cell contains given text.
	 * 
	 * @param text
	 *            given text.
	 * @return true if the current cell contains given text.
	 */
	public boolean equals(String text) {
		return this.getValue().equals(text);
	}

}
