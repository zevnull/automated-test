package es.s2o.automated.test.core.screenshots;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Reporter;

import com.google.common.io.Files;
import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.utilities.S2oException;
import es.s2o.automated.test.core.utilities.AbsisUtils;

/**
 * @author s2o
 */
public class DriverScreenShoot {

	private static final Logger LOG = LoggerFactory.getLogger(DriverScreenShoot.class);

	/**
	 * Takes screenshot of current browser window. Stores 2 files: html of page, and image in JPG format.
	 * 
	 * @param driver
	 * @param testResult
	 */
	public static File takeScreenShotToReport(WebDriver driver, String name) {
		try {
			String fileName = generatePath() + name + ".txt";
			File destination = new File(fileName);
			ensureFolderExists(destination);
			String pageSource = driver.getPageSource();
			Files.write(pageSource, destination, Charset.forName("UTF-8"));
			Reporter.log("<div class='suite-section-content'><ul><li><a href='../../" + fileName
					+ "'><b>Show page sourcecode.</b></a></li></ul></div>");
		} catch (Exception ex) {
			LOG.error("Error tomando foto del navegador.", ex);
		}

		File answer = null;
		try {
			BufferedImage screenshot = takeScreenShot(driver);
			saveImageResult(name, screenshot);
		} catch (Exception ex) {
			LOG.error("Error tomando foto del navegador.", ex);
		}

		return answer;
	}

	/**
	 * @param driver
	 * @return
	 */
	public static BufferedImage takeScreenShot(WebDriver driver) {
		BufferedImage bufImg = null;
		byte[] imgBytes = null;
		if (driver instanceof TakesScreenshot) {
			imgBytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
		} else if (driver instanceof RemoteWebDriver) {
			WebDriver remoteDriver = new Augmenter().augment(driver);
			if (remoteDriver instanceof TakesScreenshot) {
				imgBytes = ((TakesScreenshot) remoteDriver).getScreenshotAs(OutputType.BYTES);
			}
		}

		try {
			bufImg = ImageIO.read(new ByteArrayInputStream(imgBytes));
		} catch (Exception ex) {
			throw new S2oException(null, "ImageIO.read", ex);
		}

		return bufImg;
	}

	/**
	 * @param name
	 * @param bufferedImage
	 */
	public static void saveImageResult(String name, BufferedImage bufferedImage) {
		String fileName = generatePath() + name + ".jpg";
		File imageFile = new File(fileName);
		ensureFolderExists(imageFile);
		try {
			// BufferedImage bufferJPG = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
			// BufferedImage.TYPE_INT_RGB);
			// bufferJPG.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
			// ImageIO.write(bufferJPG, "jpeg", imageFile);

			ImageIO.write(bufferedImage, "png", imageFile);
			// La guardamos en el report
			Reporter.log("<img src='../../" + fileName + "' />");
		} catch (Exception ex) {
			throw new S2oException(null, "Error saveImageResult", ex);
		}

	}

	/**
	 * @param name
	 * @return
	 */
	private static String generatePath() {
		// Guardamos el fichero en el fs
		Config config = AbsisConfigFactory.getConfig();
		String folderPath = config.getString(AbsisConstants.SCREENSHOTS_PATH);
		if (folderPath.endsWith(File.separator) == false) {
			folderPath = folderPath + File.separator;
		}
		folderPath = folderPath + AbsisUtils.environment() + File.separator;
		return folderPath;
	}

	private static File ensureFolderExists(File targetFile) {
		File folder = targetFile.getParentFile();
		if (!folder.exists()) {
			if (!folder.mkdirs()) {
				throw new S2oException(null, "Failed to create " + folder);
			}
		}
		return targetFile;
	}

}
