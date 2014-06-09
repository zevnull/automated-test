package es.s2o.automated.test.core.datasource;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This interface serves as the base interface for all data sources. It defines no methods, but binds the type parameter
 * of the {@link Iterator} interface to {@link Object Object[]}.
 * 
 * @author s2o
 */
public interface IDataSource extends Iterator<Object[]> {

	/**
	 * Checks whether this data source has another set of test data.
	 * 
	 * @return <code>true</code>, if and only if this data source has another set of test data, <code>false</code>
	 *         otherwise
	 */
	@Override
	boolean hasNext();

	/**
	 * Returns the next set of test data as an array of {@link Object Objects}. If no next set of test data is available
	 * this method throws a {@link NoSuchElementException}.
	 * 
	 * @return The next set of test data.
	 * @throws NoSuchElementException
	 *             If no next set of test data exists
	 */
	@Override
	Object[] next();
}