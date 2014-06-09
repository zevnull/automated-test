package es.s2o.automated.test.core.utilities;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FileDownloader {
	public static FileDownloader instance = new FileDownloader();

	public File download(WebDriver driver, WebElement element) throws IOException {
		String fileToDownloadLocation = element.getAttribute("href");
		if (fileToDownloadLocation.trim().isEmpty()) {
			throw new IllegalArgumentException("The element does not have href attribute");
		}

		HttpResponse response = executeHttpRequest(driver, fileToDownloadLocation);

		File downloadedFile = prepareTargetFile(fileToDownloadLocation, response);

		return saveFileContent(response, downloadedFile);
	}

	protected HttpResponse executeHttpRequest(WebDriver driver, String fileToDownloadLocation) throws IOException {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(fileToDownloadLocation);
		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, mimicCookieState(driver));

		return httpClient.execute(httpGet, localContext);
	}

	protected File prepareTargetFile(String fileToDownloadLocation, HttpResponse response) throws MalformedURLException {
		return new File(AbsisUtils.getReportsFolder(), getFileName(fileToDownloadLocation, response));
	}

	protected String getFileName(String fileToDownloadLocation, HttpResponse response) throws MalformedURLException {
		for (Header header : response.getAllHeaders()) {
			String fileName = getFileNameFromContentDisposition(header.getName(), header.getValue());
			if (fileName != null) {
				return fileName;
			}
		}

		System.out.println("DOWNLOAD HEADERS:");
		for (Header header : response.getAllHeaders()) {
			System.out.println(header.getName() + '=' + header.getValue());
		}

		return new URL(fileToDownloadLocation).getFile().replaceFirst("/|\\\\", "");
	}

	protected String getFileNameFromContentDisposition(String headerName, String headerValue) {
		if ("Content-Disposition".equalsIgnoreCase(headerName)) {
			Matcher regex = Pattern.compile(".*filename=\"?([^\"]*)\"?.*").matcher(headerValue);
			return regex.matches() ? regex.replaceFirst("$1") : null;
		}
		return null;
	}

	protected BasicCookieStore mimicCookieState(WebDriver driver) {
		Set<Cookie> seleniumCookieSet = driver.manage().getCookies();
		BasicCookieStore mimicWebDriverCookieStore = new BasicCookieStore();
		for (Cookie seleniumCookie : seleniumCookieSet) {
			mimicWebDriverCookieStore.addCookie(duplicateCookie(seleniumCookie));
		}

		return mimicWebDriverCookieStore;
	}

	protected BasicClientCookie duplicateCookie(Cookie seleniumCookie) {
		BasicClientCookie duplicateCookie = new BasicClientCookie(seleniumCookie.getName(), seleniumCookie.getValue());
		duplicateCookie.setDomain(seleniumCookie.getDomain());
		duplicateCookie.setSecure(seleniumCookie.isSecure());
		duplicateCookie.setExpiryDate(seleniumCookie.getExpiry());
		duplicateCookie.setPath(seleniumCookie.getPath());
		return duplicateCookie;
	}

	protected File saveFileContent(HttpResponse response, File downloadedFile) throws IOException {
		try {
			FileUtils.copyInputStreamToFile(response.getEntity().getContent(), downloadedFile);
		} finally {
			response.getEntity().getContent().close();
		}

		return downloadedFile;
	}
}
