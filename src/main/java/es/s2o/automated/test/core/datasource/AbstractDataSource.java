package es.s2o.automated.test.core.datasource;


/**
 * Clase que se le pasa como Iterator<Object[]> al dataprovider de testng
 * 
 * @author s2o
 */
public abstract class AbstractDataSource implements IDataSource {

	/**
	 * @throws UnsupportedOperationException
	 *             Test data cannot be removed therefore trying to do so results in this exception being thrown.
	 */
	@Override
	public final void remove() {
		throw new UnsupportedOperationException();
	}
}