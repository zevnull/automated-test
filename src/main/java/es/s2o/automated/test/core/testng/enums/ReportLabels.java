package es.s2o.automated.test.core.testng.enums;

public enum ReportLabels {
	HEADER_TEXT("ATU Graphical Reports for Selenium -  TestNG"), PASS("Passed"), FAIL("Failed"), SKIP("Skipped"), X_AXIS(
			"Run Number"), Y_AXIS("TC Number"), LINE_CHART_TOOLTIP("Test Cases"), ATU_LOGO("atu.jpg"), ATU_CAPTION(
			"<i style=\"float:left;padding-left:20px;font-size:12px\">Reflections of Visionary Minds</i>"), PROJ_LOGO(
			""), PROJ_CAPTION("");

	private String label;

	private ReportLabels(String paramString) {
		setLabel(paramString);
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String paramString) {
		this.label = paramString;
	}
}