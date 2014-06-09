package es.s2o.automated.test.core.utilities;

import org.testng.Assert;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.utilities.AbsisUtils;

public class AbsisUtilsTest {

	// @Test
	// public void environment() {
	// throw new RuntimeException("Test not implemented");
	// }
	//
	// @Test
	// public void getDateAsStringFormated() {
	// throw new RuntimeException("Test not implemented");
	// }
	//
	// @Test
	// public void getReportsFolder() {
	// throw new RuntimeException("Test not implemented");
	// }
	//
	// @Test
	// public void tableData() {
	// throw new RuntimeException("Test not implemented");
	// }

	@Test
	public void textWithoutQuotes() {
		Assert.assertEquals("\"john\"", AbsisUtils.escapeQuotes("john"));
	}

	@Test
	public void textWithApostrophe() {
		Assert.assertEquals("\"John Mc'Clain\"", AbsisUtils.escapeQuotes("John Mc'Clain"));
	}

	@Test
	public void textWithQuote() {
		Assert.assertEquals("'Cafe \"Rock Cafe\"'", AbsisUtils.escapeQuotes("Cafe \"Rock Cafe\""));
	}

	@Test
	public void textWithQuoteAndApostrophe() {
		Assert.assertEquals("concat(\"A'la cafe \", '\"', \"Rock Cafe\", '\"')",
				AbsisUtils.escapeQuotes("A'la cafe \"Rock Cafe\""));
	}

	@Test
	public void textWithApostropheAndQuote() {
		Assert.assertEquals("concat(\"Cafe \", '\"', \"Rock Cafe\", '\"', \" isn't cool?\")",
				AbsisUtils.escapeQuotes("Cafe \"Rock Cafe\" isn't cool?"));
	}

	@Test
	public void textWithApostropheInsideQuotes() {
		Assert.assertEquals("concat(\"Cafe \", '\"', \"Rock'n'Roll\", '\"')",
				AbsisUtils.escapeQuotes("Cafe \"Rock'n'Roll\""));
	}

	@Test
	public void textWithQuotesInsideApostrophe() {
		Assert.assertEquals("concat(\"The 'I am not \", '\"', \"Oracle\", '\"', \"' approach\")",
				AbsisUtils.escapeQuotes("The 'I am not \"Oracle\"' approach"));
	}
}
