package es.s2o.automated.test.core.conf;

import org.testng.Assert;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.utilities.FileFinder;

@Test
public class TestFindFile {
	public void findFileTest() {
		FileFinder fileFinder = new FileFinder();
		Assert.assertNotNull(fileFinder.findFile("file2find.xml"));
	}
}
