package es.s2o.automated.test.core.conf;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import es.s2o.automated.test.core.utilities.FileFinder;

/**
 * @author s2o
 */
public class AbsisConfigFactory {

	private static final Logger LOG = LoggerFactory.getLogger(AbsisConfigFactory.class);

	private static final String CONF_EXTENSION = ".conf";
	private static final Map<String, Config> CLASS_CONFIG = new ConcurrentHashMap<String, Config>();

	// Nos guardaremos en una variable local al thread la configuracin actual
	private static final ThreadLocal<Config> config = new ThreadLocal<Config>();
	private static final ThreadLocal<FileFinder> fileFinder = new ThreadLocal<FileFinder>();

	private AbsisConfigFactory() throws IllegalAccessException {
		throw new IllegalAccessException("AbsisConfigFactory is a static utility class that cannot be constructed");
	}

	public static Config getConfig() {
		return config.get();
	}

	public static void initConfig() {
		config.set(ConfigFactory.defaultOverrides().withFallback(ConfigFactory.load()));
	}

	public static Config buildConfig(String suiteName) {
		if (!CLASS_CONFIG.containsKey(suiteName)) {
			LOG.info("Obteniendo configuración de:" + suiteName);
			buildConfigForClass(suiteName);
		}
		config.set(CLASS_CONFIG.get(suiteName));
		return config.get();
	}

	/**
	 * Cuando finaliza una suite reseteamos la configuracin
	 */
	public static void resetConfig() {
		config.set(null);
	}

	private static void buildConfigForClass(String suiteName) {
		File suiteConfigDefined = getFile(suiteName + CONF_EXTENSION);
		Config classConfig;
		if (suiteConfigDefined != null) {
			classConfig = ConfigFactory.defaultOverrides().withFallback(ConfigFactory.parseFile(suiteConfigDefined))
					.withFallback(ConfigFactory.load());
		} else {
			classConfig = ConfigFactory.defaultOverrides().withFallback(ConfigFactory.load());
		}
		CLASS_CONFIG.put(suiteName, classConfig);
	}

	private static File getFile(String suiteName) {
		if (fileFinder.get() == null) {
			FileFinder fileF = new FileFinder();
			fileFinder.set(fileF);
		}
		return fileFinder.get().findFile(suiteName);
	}

}
