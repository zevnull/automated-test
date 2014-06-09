package es.s2o.automated.test.core.datasource;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public final class ObjectConverter {

	// Init ---------------------------------------------------------------------------------------

	private static final Map<String, Method> CONVERTERS = new HashMap<String, Method>();

	static {
		// Preload converters.
		Method[] methods = ObjectConverter.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getParameterTypes().length == 1) {
				// Converter should accept 1 argument. This skips the convert() method.
				CONVERTERS.put(method.getParameterTypes()[0].getName() + "_" + method.getReturnType().toString(),
						method);
			}
		}
	}

	private ObjectConverter() {
		// Utility class, hide the constructor.
	}

	// Action -------------------------------------------------------------------------------------

	/**
	 * Convert the given object value to the given class.
	 * 
	 * @param from
	 *            The object value to be converted.
	 * @param to
	 *            The type class which the given object should be converted to.
	 * @return The converted object value.
	 * @throws NullPointerException
	 *             If 'to' is null.
	 * @throws UnsupportedOperationException
	 *             If no suitable converter can be found.
	 * @throws RuntimeException
	 *             If conversion failed somehow. This can be caused by at least an ExceptionInInitializerError,
	 *             IllegalAccessException or InvocationTargetException.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T convert(Object from, Type to) {

		// Null is just null.
		if (from == null) {
			return null;
		}

		// // Can we cast? Then just do it.
		// if (to.isAssignableFrom(from.getClass())) {
		// return to.cast(from);
		// }
		// java.lang.String_class java.lang.String

		// Lookup the suitable converter.
		String converterId = from.getClass().getName() + "_" + to.toString();
		Method converter = CONVERTERS.get(converterId);
		if (converter == null) {
			return (T) from;
			// throw new UnsupportedOperationException("Cannot convert from " + from.getClass().getName() + " to " + to
			// + ". Requested converter does not exist.");
		}

		// Convert the value.
		try {
			return ((T) converter.invoke(to, from));
		} catch (Exception e) {
			throw new RuntimeException("Cannot convert from " + from.getClass().getName() + " to " + to
					+ ". Conversion failed with " + e.getMessage(), e);
		}
	}

	// Converters ---------------------------------------------------------------------------------

	/**
	 * Converts Integer to Boolean. If integer value is 0, then return FALSE, else return TRUE.
	 * 
	 * @param value
	 *            The Integer to be converted.
	 * @return The converted Boolean value.
	 */
	public static Boolean integerToBoolean(Integer value) {
		return value.intValue() == 0 ? Boolean.FALSE : Boolean.TRUE;
	}

	/**
	 * Converts Boolean to Integer. If boolean value is TRUE, then return 1, else return 0.
	 * 
	 * @param value
	 *            The Boolean to be converted.
	 * @return The converted Integer value.
	 */
	public static Integer booleanToInteger(Boolean value) {
		return value.booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0);
	}

	/**
	 * Converts Double to BigDecimal.
	 * 
	 * @param value
	 *            The Double to be converted.
	 * @return The converted BigDecimal value.
	 */
	public static BigDecimal doubleToBigDecimal(Double value) {
		return new BigDecimal(value.doubleValue());
	}

	/**
	 * Converts BigDecimal to Double.
	 * 
	 * @param value
	 *            The BigDecimal to be converted.
	 * @return The converted Double value.
	 */
	public static Double bigDecimalToDouble(BigDecimal value) {
		return new Double(value.doubleValue());
	}

	/**
	 * Converts Integer to String.
	 * 
	 * @param value
	 *            The Integer to be converted.
	 * @return The converted String value.
	 */
	public static String integerToString(Integer value) {
		return value.toString();
	}

	/**
	 * Converts String to Integer.
	 * 
	 * @param value
	 *            The String to be converted.
	 * @return The converted Integer value.
	 */
	public static Integer stringToInteger(String value) {
		return Integer.valueOf(value);
	}

	/**
	 * Converts Boolean to String.
	 * 
	 * @param value
	 *            The Boolean to be converted.
	 * @return The converted String value.
	 */
	public static String booleanToString(Boolean value) {
		return value.toString();
	}

	/**
	 * Converts String to Boolean.
	 * 
	 * @param value
	 *            The String to be converted.
	 * @return The converted Boolean value.
	 */
	public static Boolean stringToBoolean(String value) {
		return Boolean.valueOf(value);
	}

	// You can implement more converter methods here.

}