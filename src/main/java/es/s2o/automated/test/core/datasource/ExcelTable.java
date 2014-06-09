package es.s2o.automated.test.core.datasource;

import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelTable implements Comparable<Object> {

	private final String tableName;

	private final CellRangeAddress selectedRegion;

	public ExcelTable(String tableName, CellRangeAddress selectedRegion) {
		this.tableName = tableName;
		this.selectedRegion = selectedRegion;
	}

	public String getTableName() {
		return tableName;
	}

	public CellRangeAddress getSelectedRegion() {
		return selectedRegion;
	}

	@Override
	public String toString() {
		return tableName;
	}

	@Override
	public int compareTo(Object paramT2) {
		ExcelTable p2 = (ExcelTable) paramT2;
		return getTableName().compareTo(p2.getTableName());
	}

}