package es.s2o.automated.test.core.datasource;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 */
public class Types {
	private Types() {
	}

	/**
	 * Checks whether a given type is an enum type.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if this type is a class declared as an enum in the source code, false otherwise
	 */
	public static boolean isEnumType(final Type type) {
		return type instanceof Class<?> && ((Class<?>) type).isEnum();
	}

	/**
	 * Checks whether a given type is a primitive type for the purposes of the TestNG DataBinding framework.
	 * <p>
	 * The following types are considered to be primitive types:
	 * <ul>
	 * <li>All primitive types according to the Java Language Specification and their respective wrapper classes</li>
	 * <li>The {@link String java.lang.String} type</li>
	 * </ul>
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is one of the types considered primitive, false otherwise
	 */
	public static boolean isPrimitiveType(final Type type) {
		if (type instanceof Class<?>) {
			return type == String.class || type == Integer.class || type == int.class || type == Long.class
					|| type == long.class || type == Float.class || type == float.class || type == Double.class
					|| type == double.class || type == Boolean.class || type == boolean.class
					|| type == Character.class || type == char.class;
		}

		return false;
	}

	/**
	 * Checks whether a given type is a single bean type, i.e. a class type, but neither primitive nor an enum.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is a single bean type, false otherwise
	 */
	public static boolean isSingleBeanType(final Type type) {
		return type instanceof Class<?> && !isPrimitiveType(type) && !isEnumType(type);
	}

	/**
	 * Checks whether a given type is a {@link List} of primitives type.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is a list of primitives type, false otherwise
	 * @see #isPrimitiveType(Type)
	 */
	public static boolean isListOfPrimitivesType(final Type type) {
		if (type instanceof ParameterizedType) {
			final ParameterizedType paramType = (ParameterizedType) type;
			if (paramType.getRawType() == List.class && paramType.getActualTypeArguments().length > 0
					&& isPrimitiveType(paramType.getActualTypeArguments()[0])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether a given type is a {@link List} of beans type.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is a list of beans type, otherwise false
	 * @see #isSingleBeanType(Type)
	 */
	public static boolean isListOfBeansType(final Type type) {
		if (type instanceof ParameterizedType) {
			final ParameterizedType paramType = (ParameterizedType) type;
			if (paramType.getRawType() == List.class && paramType.getActualTypeArguments().length > 0
					&& isSingleBeanType(paramType.getActualTypeArguments()[0])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether a given type is a single object type, i.e. either a primitive, enum or single bean type.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is a single object type, false otherwise
	 * @see #isPrimitiveType(Type)
	 * @see #isEnumType(Type)
	 * @see #isSingleBeanType(Type)
	 */
	public static boolean isSingleObjectType(final Type type) {
		return isPrimitiveType(type) || isSingleBeanType(type) || isEnumType(type);
	}

	/**
	 * Checks whether a given type is a {@link List} of objects type, i.e. a list of primitives or a list of beans type.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is a list of objects type, false otherwise
	 * @see #isListOfPrimitivesType(Type)
	 * @see #isListOfBeansType(Type)
	 */
	public static boolean isListOfObjectsType(final Type type) {
		return isListOfPrimitivesType(type) || isListOfBeansType(type);
	}

	/**
	 * Checks whether a given type is a type supported by the TestNG DataBinding framework as a test method parameter
	 * type. Supported types must either be a single object type or a list of objects type.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type is a supported type, false otherwise
	 * @see #isSingleObjectType(Type)
	 * @see #isListOfObjectsType(Type)
	 */
	public static boolean isSupportedType(final Type type) {
		return isSingleObjectType(type) || isListOfObjectsType(type);
	}

	/**
	 * Checks whether the given type requires the name attribute of the {@link TestInput} or {@link TestOutput}
	 * annotation to be set, when used as a test method parameter type.
	 * <p>
	 * Types requiring the name attribute to be set are the primitive and list of primitives types.
	 * 
	 * @param type
	 *            The type to be checked
	 * @return true if and only if the given type requires the name attribute be set, false otherwise
	 * @see TestInput#name()
	 * @see TestOutput#name()
	 * @see #isPrimitiveType(Type)
	 * @see #isListOfPrimitivesType(Type)
	 */
	public static boolean requiresName(final Type type) {
		return isPrimitiveType(type) || isListOfPrimitivesType(type);
	}

}