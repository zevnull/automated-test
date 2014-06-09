package es.s2o.automated.test.core.model;

import java.util.LinkedList;
import java.util.List;

/**
 * @author s2o
 */
public class AbsisReport {

	private String reportName;

	// Los campos deben estar inicializados
	private List<Object> data = new LinkedList<Object>();

	/**
	 * @return
	 */
	public String getReportName() {
		return reportName;
	}

	/**
	 * @param reportName
	 */
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

}
