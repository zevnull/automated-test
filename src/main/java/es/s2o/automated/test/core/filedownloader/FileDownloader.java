package es.s2o.automated.test.core.filedownloader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.s2o.automated.test.core.conf.AbsisConfigFactory;
import es.s2o.automated.test.core.conf.AbsisConstants;
import es.s2o.automated.test.core.utilities.S2oException;

public class FileDownloader {

	private static final Logger LOG = LoggerFactory.getLogger(FileDownloader.class);

	/**
	 * Download a file from the specified URI
	 * 
	 * @param fileURI
	 * @param postParams
	 * @return
	 * @throws Exception
	 */
	public File downloadFileFromPost(URI fileURI, Map<String, String> postParams) {

		HttpResponse fileToDownload = null;
		try {
			fileToDownload = getHTTPResponse(null, fileURI, postParams);
		} catch (NumberFormatException | KeyManagementException | NullPointerException | NoSuchAlgorithmException
				| KeyStoreException | IOException e) {
			S2oException s2oException = new S2oException("Error obteniendo fichero", e);
			throw s2oException;
		}

		return copyToTmpFile(fileToDownload);
	}

	/**
	 * Download a file from the specified URI with the Selenium Driver cookies
	 * 
	 * @return File
	 * @throws Exception
	 */
	public File downloadFileFromSeleniumWithPost(WebDriver driver, WebElement anchorElement,
			Map<String, String> postParams) {
		HttpResponse fileToDownload = null;
		try {
			fileToDownload = getHTTPResponse(driver, getURISpecifiedInAnchorElement(anchorElement), postParams);
		} catch (NumberFormatException | KeyManagementException | NullPointerException | NoSuchAlgorithmException
				| KeyStoreException | IOException | URISyntaxException e) {
			S2oException s2oException = new S2oException("Error obteniendo fichero", e);
			throw s2oException;
		}

		return copyToTmpFile(fileToDownload);
	}

	/**
	 * Download a file from the specified URI with the Selenium Driver cookies
	 * 
	 * @return File
	 * @throws Exception
	 */
	public File downloadFileFromSeleniumWithPost(WebDriver driver, String url, Map<String, String> postParams) {
		HttpResponse fileToDownload = null;
		try {
			fileToDownload = getHTTPResponse(driver, new URI(url), postParams);
		} catch (NumberFormatException | KeyManagementException | NullPointerException | NoSuchAlgorithmException
				| KeyStoreException | IOException | URISyntaxException e) {
			S2oException s2oException = new S2oException("Error obteniendo fichero", e);
			throw s2oException;
		}

		return copyToTmpFile(fileToDownload);
	}

	/**
	 * @param driver
	 * @param fileURI
	 * @param postParams
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 * @throws NumberFormatException
	 */
	private HttpResponse getHTTPResponse(WebDriver driver, URI fileURI, Map<String, String> postParams)
			throws IOException, NullPointerException, NumberFormatException, KeyManagementException,
			NoSuchAlgorithmException, KeyStoreException {

		HttpClient httpclient = getHttpClient();

		// Clear down the local cookie store every time to make sure
		// we don't have any left over cookies influencing the test
		BasicHttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, null);
		if (driver != null) {
			localContext.setAttribute(HttpClientContext.COOKIE_STORE, mimicCookieState(driver.manage().getCookies()));
		}

		// Parametros Psot
		HttpRequestBase requestBase = RequestMethod.POST.getRequestMethod();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		for (Map.Entry<String, String> header : postParams.entrySet()) {
			// requestBase.addHeader(header.getKey(), header.getValue());
			nameValuePairs.add(new BasicNameValuePair(header.getKey(), header.getValue()));

		}
		requestBase.setURI(fileURI);

		try {

			((HttpPost) requestBase).setEntity(new UrlEncodedFormEntity(nameValuePairs));
		} catch (UnsupportedEncodingException usee) {
			LOG.error("Could not encode the nameVaulePairs.", usee);
		}

		for (Header header : requestBase.getAllHeaders()) {
			LOG.info(header.getName() + ": " + header.getValue());
		}
		LOG.info("Sending " + requestBase.toString());
		return httpclient.execute(requestBase, localContext);
	}

	/**
	 * @return
	 * @throws NumberFormatException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private HttpClient getHttpClient() throws NumberFormatException, KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException {
		SSLConnectionSocketFactory socketFactory = trustInAllServers();
		RedirectStrategy redirectStrategy = new LaxRedirectStrategy();
		String proxyValue = AbsisConfigFactory.getConfig().getString(AbsisConstants.WEBDRIVER_PROXY_HTTPPROXY);
		String[] proxyValues = proxyValue.split(":");
		HttpHost proxy = new HttpHost(proxyValues[0], Integer.valueOf(proxyValues[1]));
		HttpClient httpclient = HttpClientBuilder.create().setRedirectStrategy(redirectStrategy).setProxy(proxy)
				.setSSLSocketFactory(socketFactory).build();
		return httpclient;
	}

	/**
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws KeyManagementException
	 */
	private SSLConnectionSocketFactory trustInAllServers() throws KeyManagementException, NoSuchAlgorithmException,
			KeyStoreException {
		final TrustStrategy trustStrategy = new TrustStrategy() {
			@Override
			public boolean isTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
				return chain.length == 1;
			}

		};
		final KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
		final SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, trustStrategy).build();

		final SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslcontext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		return socketFactory;
	}

	/**
	 * Load in all the cookies WebDriver currently knows about so that we can mimic the browser cookie state
	 * 
	 * @param seleniumCookieSet
	 *            Set&lt;Cookie&gt;
	 */
	private BasicCookieStore mimicCookieState(Set<Cookie> seleniumCookieSet) {
		BasicCookieStore copyOfWebDriverCookieStore = new BasicCookieStore();
		for (Cookie seleniumCookie : seleniumCookieSet) {
			BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(),
					seleniumCookie.getValue());
			duplicateCookie.setDomain(seleniumCookie.getDomain());
			duplicateCookie.setSecure(seleniumCookie.isSecure());
			duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
			duplicateCookie.setPath(seleniumCookie.getPath());
			copyOfWebDriverCookieStore.addCookie(duplicateCookie);
		}

		return copyOfWebDriverCookieStore;
	}

	/**
	 * Perform an HTTP Status Check upon/Download the file specified in the href attribute of a WebElement
	 * 
	 * @param anchorElement
	 *            Selenium WebElement
	 * @throws URISyntaxException
	 * @throws Exception
	 */
	private URI getURISpecifiedInAnchorElement(WebElement anchorElement) throws URISyntaxException {
		URI fileURI = null;
		if (anchorElement.getTagName().equals("a")) {
			fileURI = new URI(anchorElement.getAttribute("href"));
		} else if (anchorElement.getTagName().equals("img")) {
			fileURI = new URI(anchorElement.getAttribute("src"));
		} else {
			S2oException s2oException = new S2oException("You have not specified an <img> element!");
			throw s2oException;
		}
		return fileURI;
	}

	/**
	 * @param fileToDownload
	 * @return
	 * @throws S2oException
	 */
	private File copyToTmpFile(HttpResponse fileToDownload) throws S2oException {
		File downloadedFile = null;
		try {
			downloadedFile = File.createTempFile("download", ".tmp");
			FileUtils.copyInputStreamToFile(fileToDownload.getEntity().getContent(), downloadedFile);
			fileToDownload.getEntity().getContent().close();
		} catch (Exception exeption) {
			S2oException s2oException = new S2oException("Error copiando fichero", exeption);
			throw s2oException;
		}
		return downloadedFile;
	}
}
