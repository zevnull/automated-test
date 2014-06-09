package es.s2o.automated.test.core.pages;

import org.openqa.selenium.WebDriver;

import com.typesafe.config.Config;

/**
 * Las clases que implementen la lgica del login deben implementar esta interfaz
 * 
 * @author s2o
 */
public interface ILoginPage {

	/**
	 * <pre>
	 * Implementacin del flujo de login para la urlBaseEntorno. 
	 * Devuelve true/false en funcin de si ha podido hacer login o no.
	 * </pre>
	 * 
	 * @param driver
	 * @param config
	 * @param urlBaseEntorno
	 * @return
	 */
	public abstract boolean login(WebDriver driver, Config config, String urlBaseEntorno);

}