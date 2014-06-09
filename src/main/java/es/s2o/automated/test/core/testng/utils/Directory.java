package es.s2o.automated.test.core.testng.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;

import es.s2o.automated.test.core.testng.enums.ReportLabels;
import es.s2o.automated.test.core.testng.writers.HTMLDesignFilesWriter;
import es.s2o.automated.test.core.utilities.S2oException;

public class Directory {
	public static String indexPageDescription = "Reports Description";

	public static final String ATU_VERSION = "v3.0";
	public static final String CURRENTDir = System.getProperty("user.dir");
	public static final String SEP = System.getProperty("file.separator");
	public static String REPORTSDir = CURRENTDir + SEP + "ATU Reports";
	public static String RESULTSDir = REPORTSDir + SEP + "Results";
	public static String HTMLDESIGNDIRName = "HTML_Design_Files";
	public static String HTMLDESIGNDir = REPORTSDir + SEP + HTMLDESIGNDIRName;
	public static String CSSDIRName = "CSS";
	public static String CSSDir = HTMLDESIGNDir + SEP + CSSDIRName;
	public static String IMGDIRName = "IMG";
	public static String IMGDir = HTMLDESIGNDir + SEP + IMGDIRName;
	public static String JSDIRName = "JS";
	public static String JSDir = HTMLDESIGNDir + SEP + JSDIRName;
	public static String RUNName = "Run_";
	public static String RUNDir = RESULTSDir + SEP + RUNName;
	public static String SETTINGSFile = RESULTSDir + SEP + "Settings.properties";
	public static final char JS_SETTINGS_DELIM = ';';
	public static final String REPO_DELIM = "##@##@##";
	public static final char JS_FILE_DELIM = ',';
	public static final String MENU_LINK_NAME = "Run ";
	public static final String SCREENSHOT_FORMAT = ".PNG";
	private static String logo = null;

	public static void init() {
		if (getCustomProperties() != null) {
			Properties localProperties = new Properties();
			try {
				localProperties.load(new FileReader(getCustomProperties()));
				String str1 = localProperties.getProperty("atu.reports.dir");
				String str2 = localProperties.getProperty("atu.proj.header.text");
				logo = localProperties.getProperty("atu.proj.header.logo");
				String str3 = localProperties.getProperty("atu.proj.description");
				try {
					if ((str2 != null) && (str2.length() > 0))
						ReportLabels.HEADER_TEXT.setLabel(str2);
					if ((str3 != null) && (str3.length() > 0))
						indexPageDescription = str3;
					if ((str1 != null) && (str1.length() > 0)) {
						REPORTSDir = str1;
						RESULTSDir = REPORTSDir + SEP + "Results";
						HTMLDESIGNDIRName = "HTML_Design_Files";
						HTMLDESIGNDir = REPORTSDir + SEP + HTMLDESIGNDIRName;
						CSSDIRName = "CSS";
						CSSDir = HTMLDESIGNDir + SEP + CSSDIRName;
						IMGDIRName = "IMG";
						IMGDir = HTMLDESIGNDir + SEP + IMGDIRName;
						JSDIRName = "JS";
						JSDir = HTMLDESIGNDir + SEP + JSDIRName;
						RUNName = "Run_";
						RUNDir = RESULTSDir + SEP + RUNName;
						SETTINGSFile = RESULTSDir + SEP + "Settings.properties";
					}
				} catch (Exception localException) {
					throw new S2oException(localException.toString());
				}
			} catch (FileNotFoundException localFileNotFoundException) {
				throw new S2oException(null, "The Path for the Custom Properties file could not be found");
			} catch (IOException localIOException) {
				throw new S2oException("Problem Occured while reading the ATU Reporter Config File");
			}
		}
	}

	public static void mkDirs(String paramString) {
		File localFile = new File(paramString);
		if (!localFile.exists())
			localFile.mkdirs();
	}

	public static boolean exists(String paramString) {
		File localFile = new File(paramString);
		return localFile.exists();
	}

	public static void verifyRequiredFiles() {
		init();
		mkDirs(REPORTSDir);
		if (!exists(RESULTSDir)) {
			mkDirs(RESULTSDir);
		}
		if (!exists(HTMLDESIGNDir)) {
			mkDirs(HTMLDESIGNDir);
			mkDirs(CSSDir);
			mkDirs(JSDir);
			mkDirs(IMGDir);
			HTMLDesignFilesWriter.writeCSS();
			HTMLDesignFilesWriter.writeIMG();
			HTMLDesignFilesWriter.writeJS();
		}
		if ((logo != null) && (logo.length() > 0)) {
			String str = new File(logo).getName();
			if (!new File(IMGDir + SEP + str).exists())
				copyImage(logo);
			ReportLabels.PROJ_LOGO.setLabel(str);
		}
	}

	private static void copyImage(String paramString) {
		File localFile = new File(paramString);
		if (!localFile.exists())
			return;
		FileImageInputStream localFileImageInputStream = null;
		FileImageOutputStream localFileImageOutputStream = null;
		try {
			localFileImageInputStream = new FileImageInputStream(new File(paramString));
			localFileImageOutputStream = new FileImageOutputStream(new File(IMGDir + SEP + localFile.getName()));
			int i = 0;
			while ((i = localFileImageInputStream.read()) >= 0)
				localFileImageOutputStream.write(i);
			localFileImageOutputStream.close();
		} catch (Exception localException2) {
		} finally {
			try {
				localFileImageInputStream.close();
				localFileImageOutputStream.close();
				localFile = null;
			} catch (Exception localException4) {
				localFileImageInputStream = null;
				localFileImageOutputStream = null;
				localFile = null;
			}
		}
	}

	private static String getCustomProperties() {
		return System.getProperty("atu.reporter.config");
	}
}