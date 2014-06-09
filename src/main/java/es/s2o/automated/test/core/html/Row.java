package es.s2o.automated.test.core.html;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a row of an html table.
 */
public class Row {

	private final List<Cell> cells = new ArrayList<Cell>();

	public Row() {
	}

	/**
	 * Construct a row from an array of objects which specify the cells of the row. If an object in the array is an
	 * {@link net.sourceforge.jwebunit.html.Cell}, it is directly added to the cells of the row, otherwise an
	 * {@link net.sourceforge.jwebunit.html.Cell} is created from the toString() value of the object and an assumed
	 * colspan of 1.
	 * 
	 * @param rowCells
	 *            objects representing the row's cells.
	 */
	public Row(Object[] rowCells) {
		appendCells(rowCells);
	}

	public void appendCells(Object[] rowCells) {
		for (int i = 0; i < rowCells.length; i++) {
			Object column = rowCells[i];
			if (column instanceof Cell) {
				this.cells.add((Cell) column);
			} else {
				this.cells.add(new Cell(column.toString()));
			}
		}
	}

	public void appendCell(Cell cell) {
		cells.add(cell);
	}

	public void appendCell(String cellText) {
		cells.add(new Cell(cellText));
	}

	public List<Cell> getCells() {
		return cells;
	}

	public int getCellCount() {
		return cells.size();
	}

	public boolean hasText(String text) {
		for (int i = 0; i < getCellCount(); i++) {
			Cell c = getCells().get(i);
			if (c.equals(text))
				return true;
		}
		return false;
	}

}
