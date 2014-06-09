package es.s2o.automated.test.core.model;

public enum Environment {
	PRO("pro"), PRE("pre"), TST("tst");

	private final String value;

	private Environment(final String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
