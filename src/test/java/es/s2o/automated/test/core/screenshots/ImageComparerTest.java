package es.s2o.automated.test.core.screenshots;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import es.s2o.automated.test.core.screenshots.ComparisonResult;
import es.s2o.automated.test.core.screenshots.ImageComparer;

@Test
public class ImageComparerTest {

	private static final Logger LOG = LoggerFactory.getLogger(ImageComparerTest.class);

	// la urlHome seria CA.OFI/DemoComponentes
	public void comparerTest() {
		// load the images from file
		BufferedImage b1 = loadJPG("screenshots/s1.jpg");
		BufferedImage b2 = loadJPG("screenshots/s2.jpg");

		// Create a compare object
		ImageComparer ic = new ImageComparer();
		ComparisonResult c = ic.compare(b1, b2);

		LOG.debug("Match:" + c.isMatch());

		if (!c.isMatch()) {
			BufferedImage b3 = c.getChangeIndicator(b1);
			saveJPG(b3, "screenshots/s3.jpg");
		}
		Assert.assertFalse(c.isMatch(), "Verificando imagenes deben ser diferentes");
	}

	// write a buffered image to a jpeg file.
	private void saveJPG(BufferedImage bufferedImage, String filename) {
		try {
			ImageIO.write(bufferedImage, "jpeg", new File(filename));
		} catch (Exception io) {
			System.out.println("File Not Found");
		}

	}

	// read a jpeg file into a buffered image
	private BufferedImage loadJPG(String filename) {
		BufferedImage bi = null;

		try {
			bi = ImageIO.read(getFileFromClassPath(filename));
		} catch (Exception io) {
			System.out.println("File Not Found");
		}

		return bi;
	}

	private File getFileFromClassPath(String filename) {
		File file = null;
		try {
			file = new File(ImageComparerTest.class.getClassLoader().getResource(filename).getFile());
		} catch (Exception ex) {
			LOG.warn("Failed to open config file for " + filename, ex.getMessage());
		}

		return file;
	}

}
