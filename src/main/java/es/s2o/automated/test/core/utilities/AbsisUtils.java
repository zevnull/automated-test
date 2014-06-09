package es.s2o.automated.test.core.utilities;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;

/**
 * @author s2o
 */
public class AbsisUtils {

	private static final Logger LOG = LoggerFactory.getLogger(AbsisUtils.class);

	/**
	 * <pre>
	 * Devuelve tabla de string's para un listado de rows de tipo webelement
	 * </pre>
	 * 
	 * @param rows
	 * @return
	 */
	public static String[][] tableData(List<WebElement> rows) {
		String[][] tableaArray = new String[rows.size()][];
		int rown;
		int celln;
		rown = 0;
		for (WebElement row : rows) {
			rown++;
			List<WebElement> cells = row.findElements(By.tagName("td"));
			tableaArray[rown - 1] = new String[cells.size()];
			celln = 0;
			for (WebElement cell : cells) {
				celln++;
				tableaArray[rown - 1][celln - 1] = cell.getText();
			}
		}
		return tableaArray;
	}

	/**
	 * Devuelve la variable de entorno: tst, pre, pro, dsv... Si est puesto "URL" se obtiene de los 3 primeros charts
	 * 
	 * @return
	 */
	public static String environment() {
		Config config = AbsisConfigFactory.getConfig();
		String value = config.getString(AbsisConstants.ENVIRONMENT);
		if ("URL".equals(value)) {
			value = config.getString(AbsisConstants.URL_BASE);
			URI uri;
			try {
				uri = new URI(value);
				value = uri.getHost().substring(0, 3);
			} catch (URISyntaxException e) {
				LOG.error("Error obteniendo valor del entorno a partir de url base: {}", value);
			}

		} else {
			value = value.substring(0, 3);
		}
		return value;
	}

	/**
	 * Devuelve la variable de REPORTS FOLDER
	 * 
	 * @return
	 */
	public static String getReportsFolder() {
		Config config = AbsisConfigFactory.getConfig();
		String value = config.getString(AbsisConstants.SUITE_REPORT);
		return value;
	}

	/**
	 * Especial para tratar con comillas: convierte strings con comillas " en '" -> concat("foo'", '"', "bar")
	 * 
	 * @param toEscape
	 * @return
	 */
	public static String escapeQuotes(String toEscape) {
		// Convert strings with both quotes and ticks into: foo'"bar -> concat("foo'", '"', "bar")
		if (toEscape.indexOf("\"") > -1 && toEscape.indexOf("'") > -1) {
			boolean quoteIsLast = false;
			if (toEscape.indexOf("\"") == toEscape.length() - 1) {
				quoteIsLast = true;
			}
			String[] substrings = toEscape.split("\"");

			StringBuilder quoted = new StringBuilder("concat(");
			for (int i = 0; i < substrings.length; i++) {
				quoted.append("\"").append(substrings[i]).append("\"");
				quoted.append(((i == substrings.length - 1) ? (quoteIsLast ? ", '\"')" : ")") : ", '\"', "));
			}
			return quoted.toString();
		}

		// Escape string with just a quote into being single quoted: f"oo -> 'f"oo'
		if (toEscape.indexOf("\"") > -1) {
			return String.format("'%s'", toEscape);
		}

		// Otherwise return the quoted string
		return String.format("\"%s\"", toEscape);
	}

	/**
	 * Devuelve la fecha en el formato: _yyyyMMdd_HHmmssa (pensado para nombres de ficheros)
	 * 
	 * @return
	 */
	public static String getDateAsStringFormated() {
		DateFormat dateFormat = new SimpleDateFormat("_yyyyMMdd_HHmmssa");
		Calendar cal = Calendar.getInstance();
		String dateFormated = dateFormat.format(cal.getTime());
		return dateFormated;
	}

}
