/*
 * Copyright (c) 2010-2012 Lazery Attack - http://www.lazeryattack.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.s2o.automated.test.core.filedownloader;

import java.net.URI;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

public class FileDownloaderTest {

	private static JettyServer localWebServer;
	private static String webServerURL = "http://localhost";
	private static int webServerPort = 9081;
	private static WebDriver driver;
	private static URI downloadURI200;
	private static URI downloadURI404;

	@BeforeSuite
	public static void start() throws Exception {
		// TODO
		// localWebServer = new JettyServer(webServerPort);
		downloadURI200 = new URI(webServerURL + ":" + webServerPort + "/downloadTest.html");
		downloadURI404 = new URI(webServerURL + ":" + webServerPort + "/doesNotExist.html");
		driver = new HtmlUnitDriver();
	}

	@AfterSuite
	public void closeWebDriver() {
		driver.close();
		try {
			// TODO
			// localWebServer.stopJettyServer();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Test
	// public void statusCode200FromString() {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// try {
	// URI requestUri = new URI(webServerURL + ":" + webServerPort + "/downloadTest.html");
	// downloadHandler.setURI(requestUri);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }
	//
	// @Test
	// public void statusCode404FromString() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// URI requestUri = new URI(webServerURL + ":" + webServerPort + "/doesNotExist.html");
	// downloadHandler.setURI(requestUri);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 404);
	// }
	//
	// @Test
	// public void statusCode200FromURI() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void statusCode404FromURI() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI404);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 404);
	// }
	//
	// @Test
	// public void statusCode200FromURL() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void statusCode404FromURL() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI404);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.GET);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 404);
	// }
	//
	// @Test
	// public void statusCode200FromURLUsingHead() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.HEAD);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void statusCode404FromURLUsingHead() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI404);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.HEAD);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 404);
	// }
	//
	// @Test
	// public void statusCode200FromURLUsingOptions() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.OPTIONS);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void statusCode200FromURLUsingPost() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.POST);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void statusCode405FromURLUsingPut() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.PUT);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 405);
	// }
	//
	// @Test
	// public void statusCode405FromURLUsingTrace() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.TRACE);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 405);
	// }
	//
	// @Test
	// public void statusCode405FromURLUsingDelete() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// downloadHandler.setURI(downloadURI200);
	// downloadHandler.setHTTPRequestMethod(RequestMethod.DELETE);
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 405);
	// }
	//
	// @Test
	// public void downloadAFile() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
	// WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
	// downloadHandler.setURISpecifiedInAnchorElement(downloadLink);
	// File downloadedFile = downloadHandler.downloadFile();
	//
	// Assert.assertTrue(downloadedFile.exists());
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void downloadAnImage() throws Exception {
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
	// WebElement image = driver.findElement(By.id("ebselenImage"));
	// downloadHandler.setURISpecifiedInImageElement(image);
	// File downloadedFile = downloadHandler.downloadFile();
	//
	// Assert.assertTrue(downloadedFile.exists());
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void downloadAFileFollowingRedirects() throws Exception {
	// // TODO modify test page to set a redirect to file download
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
	// WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
	// downloadHandler.setURISpecifiedInAnchorElement(downloadLink);
	// File downloadedFile = downloadHandler.downloadFile();
	//
	// Assert.assertTrue(downloadedFile.exists());
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
	//
	// @Test
	// public void downloadAFileWhilstMimicingSeleniumCookies() throws Exception {
	// // TODO modify test page to require a cookie for download
	// FileDownloader downloadHandler = new FileDownloader(driver);
	// driver.get(webServerURL + ":" + webServerPort + "/downloadTest.html");
	// WebElement downloadLink = driver.findElement(By.id("fileToDownload"));
	// downloadHandler.setURISpecifiedInAnchorElement(downloadLink);
	// downloadHandler.mimicWebDriverCookieState(true);
	// File downloadedFile = downloadHandler.downloadFile();
	//
	// Assert.assertTrue(downloadedFile.exists());
	// Assert.assertEquals(downloadHandler.getLinkHTTPStatus(), 200);
	// }
}