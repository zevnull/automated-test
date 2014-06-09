package es.s2o.automated.test.core.utilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * Clase interna para la bsqueda de ficheros en carpetas: hay dos tipos de carpetas, las de test o de cdigo fuente.
 * Por defecto lo busca en las carpetas de test
 * </pre>
 * 
 * @author s2o
 */
public class FileFinder {

	private static final Logger LOG = LoggerFactory.getLogger(FileFinder.class);

	private static final String TEST_PATH = "/test-classes/";

	private final Path startPath;
	private final ThreadLocal<File> searchedFile = new ThreadLocal<File>();

	/**
	 * Busca slo en el path de las clase de test
	 */
	public FileFinder() {
		startPath = testPath(TEST_PATH);
	}

	private Path testPath(String pathToSearch) {
		Path startPath = null;
		ClassLoader applicationClassLoader = Thread.currentThread().getContextClassLoader();
		URL[] urls = ((URLClassLoader) applicationClassLoader).getURLs();
		int totalUrls = urls.length;
		int actualUrl = 0;
		while (actualUrl < totalUrls && startPath == null) {
			URL url = urls[actualUrl];
			String urlStr = url.getPath();
			LOG.info(urlStr);
			if (urlStr.endsWith(pathToSearch)) {
				try {
					startPath = Paths.get(url.toURI());
				} catch (Exception exception) {
					throw new S2oException(null, "Error construyendo path:" + pathToSearch, exception);
				}
			}
			actualUrl++;
		}
		if (startPath == null) {
			startPath = Paths.get("src/test/resources");
		}
		return startPath;
	}

	/**
	 * @param fileToFind
	 * @return
	 */
	public File findFile(final String fileToFind) {

		try {
			Files.walkFileTree(startPath, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Path name = file.getFileName();
					if (name != null && name.toString().equals(fileToFind)) {
						searchedFile.set(file.toFile());
						return FileVisitResult.TERMINATE;
					} else
						return FileVisitResult.CONTINUE;

				}
			});
		} catch (Exception exception) {
			throw new S2oException(null, "Error buscando:" + fileToFind, exception);
		}
		return searchedFile.get();
	}

}