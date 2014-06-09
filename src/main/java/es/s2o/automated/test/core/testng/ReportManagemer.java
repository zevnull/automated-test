package es.s2o.automated.test.core.testng;

import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.model.AbsisReport;
import es.s2o.automated.test.core.screenshots.DriverScreenShoot;

/**
 * @author s2o
 */
public class ReportManagemer {

	// Nos aseguramos de tener una nica instancia para todos los threads
	private static volatile ReportManagemer instance;

	private static final ThreadLocal<AbsisReport> s2oReport = new ThreadLocal<AbsisReport>();

	protected static final long SHUTDOWN_TIME = 120;

	ExecutorService executor = Executors.newFixedThreadPool(10);

	/** The multiplication factor for the image. */
	public static float FACTOR = 0.9f;

	private static String ERROR_HEAD = AbsisConstants.RETORNO_CARRO + "---------- Error ----------"
			+ AbsisConstants.RETORNO_CARRO;
	private static String ERROR_END = AbsisConstants.RETORNO_CARRO + "---------- Error Fin ------"
			+ AbsisConstants.RETORNO_CARRO;

	private String folderPath;

	// Double-checked locking
	public static ReportManagemer getInstance() {
		if (instance == null) {
			synchronized (ReportManagemer.class) {
				if (instance == null) {
					instance = new ReportManagemer();
					instance.initExecutor();
				}
			}
		}
		return instance;
	}

	protected void initExecutor() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {

				try {
					executor.shutdown();
					if (!executor.awaitTermination(SHUTDOWN_TIME, TimeUnit.SECONDS)) {
						executor.shutdownNow();
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Inicializamos los valores del report
	 * 
	 * @param name
	 * @return
	 */
	public void initReport() {
		// Guardamos el fichero en el fs
		Config config = AbsisConfigFactory.getConfig();
		folderPath = config.getString(AbsisConstants.SUITE_REPORT);
		if (folderPath.endsWith(File.separator) == false) {
			folderPath = folderPath + File.separator;
		}
	}

	/**
	 * Para ahorrar memoria y tiempo al generar el pdf, nos aseguramos no guardarnos las pantallas de login Este método
	 * se llama después del login ok
	 * 
	 * @param name
	 */
	public void reStartTest() {
		if (s2oReport.get() != null) {
			s2oReport.get().setData(new LinkedList<Object>());
		}
	}

	/**
	 * Inicializamos el contexto del report<br/>
	 * OjO: antes de inicializarlo verificamos que no estemos en el mismo caso de uso.
	 * 
	 * @param name
	 */
	public void startTest(String name) {
		AbsisReport report = s2oReport.get();
		if (s2oReport.get() == null || !report.getReportName().equals(name)) {
			endTestCase();
			// Si es necesario finalizamos el caso de uso anterior...
			AbsisReport newAbsisReport = new AbsisReport();
			newAbsisReport.setReportName(name);
			s2oReport.set(newAbsisReport);
		}
	}

	public void takeScreenShoot(WebDriver driver) {
		if (s2oReport.get() != null) {
			BufferedImage screenShot = DriverScreenShoot.takeScreenShot(driver);
			s2oReport.get().getData().add(screenShot);
		}
	}

	public void addError(String error) {
		if (s2oReport.get() != null) {
			String errorDesc = ERROR_HEAD + error + ERROR_END;
			s2oReport.get().getData().add(errorDesc);
		}
	}

	protected static File ensureFolderExists(File targetFile) {
		File folder = targetFile.getParentFile();
		if (!folder.exists()) {
			System.err.println("Creating folder: " + folder);
			if (!folder.mkdirs()) {
				System.err.println("Failed to create " + folder);
			}
		}
		return targetFile;
	}

	protected BufferedImage applyTransform(BufferedImage bi) {
		int width = (int) (bi.getWidth() * FACTOR);
		int height = (int) (bi.getHeight() * FACTOR);
		java.awt.geom.AffineTransform atx = java.awt.geom.AffineTransform.getScaleInstance(FACTOR, FACTOR);

		BufferedImage displayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		displayImage.createGraphics();
		AffineTransformOp atop = new AffineTransformOp(atx, null);
		return atop.filter(bi, displayImage);
	}

	/**
	 * En este punto estamos seguros que se finaliza el caso de uso
	 */
	public String endTestCase() {
		String fileName = "";
		if (s2oReport.get() != null) {
			// Si hemos generado datos para el report, lo creamos
			if (s2oReport.get().getData().size() > 0) {
				// Los reports los generamos de forma asíncrona
				fileName = s2oReport.get().getReportName();
				PDFGenerator pdfBuilder = new PDFGenerator(fileName, copyList(s2oReport.get().getData()));
				executor.execute(pdfBuilder);
				s2oReport.set(null);
			}
		}

		return folderPath + fileName + ".pdf";
	}

	private List<Object> copyList(List<Object> source) {
		List<Object> dest = new ArrayList<Object>();
		for (Object item : source) {
			dest.add(item);
		}
		return dest;
	}

	private class PDFGenerator implements Runnable {
		private final String fileName;
		private final List<Object> dataList;

		PDFGenerator(String pfileName, List<Object> dataList) {
			this.fileName = pfileName;
			this.dataList = dataList;
		}

		@Override
		public void run() {
			try {
				File destination = new File(folderPath + fileName + ".pdf");
				ensureFolderExists(destination);

				Document document = new Document(PageSize.A3.rotate());
				PdfWriter.getInstance(document, new FileOutputStream(destination));
				document.open();

				for (Object data : dataList) {
					if (data instanceof String) {
						String descrip = (String) data;
						Paragraph paragraph = new Paragraph();
						Chunk chunk = new Chunk(descrip);
						paragraph.add(chunk);
						document.add(paragraph);
					}
					if (data instanceof BufferedImage) {
						BufferedImage bufferedImage = (BufferedImage) data;
						Image imagina = Image.getInstance(bufferedImage, null);
						document.add(imagina);
					}
				}

				document.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void dissable() {
		s2oReport.set(null);
	}

}
