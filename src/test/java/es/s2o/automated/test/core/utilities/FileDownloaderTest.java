package es.s2o.automated.test.core.utilities;

import org.testng.annotations.Test;

import es.s2o.automated.test.core.utilities.FileDownloader;

public class FileDownloaderTest {
	FileDownloader d = new FileDownloader();

	@Test
	public void extractsFileNameFromHttpHeader() {
		// Assert.assertEquals("statement.xls", d.getFileNameFromContentDisposition("Content-Disposition",
		// "Content-Disposition=attachment; filename=statement.xls"));
		//
		// Assert.assertEquals("statement-40817810048000102279.pdf", d.getFileNameFromContentDisposition(
		// "Content-Disposition", "Content-Disposition=inline; filename=\"statement-40817810048000102279.pdf\""));
		//
		// Assert.assertEquals("selenide-2.6.1.jar",
		// d.getFileNameFromContentDisposition("content-disposition", "attachement; filename=selenide-2.6.1.jar"));
	}
}
