package es.s2o.automated.test.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 * Annotation para obtener los valores de "@DataProvider" a partir de ficheros excel.
 * 	excelPathName -> path (desde el claspath)
 * 	sheetName (opcional) -> nombre de la pestaa (si no se informa se entiende que es la primera)
 * 	table (opcional) -> tabla definida en el excel (si no se informa se entiende que es el mismo nombre del mtodo de test)
 * </pre>
 * 
 * @author s2o
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface ExcelDataProvider {

	/**
	 * Path al excel desde el classpath aplicativo
	 * 
	 * @return
	 */
	String excelPathName();

	/**
	 * Por defecto es la 0
	 * 
	 * @return
	 */
	String sheetName() default "";

	/**
	 * Por defecto tendr el nombre del mtodo de test
	 * 
	 * @return
	 */
	String table();
}