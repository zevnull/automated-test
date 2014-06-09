package es.s2o.automated.test.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import es.s2o.automated.test.core.model.UrlBase;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface SeleniumWebDriver {

	/**
	 * Por defecto es la 0
	 * 
	 * @return
	 */
	UrlBase urlBase() default UrlBase.PRINCIPAL;

}