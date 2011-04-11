package net.jhorstmann.gein.types;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

public class PropertyTypeFactory {
    static final AtomicPropertyType OBJECT = new AtomicPropertyType(Object.class);
    static final AtomicPropertyType INTEGER = new AtomicPropertyType(Integer.class);
    static final AtomicPropertyType STRING = new AtomicPropertyType(String.class);

    private PropertyTypeFactory() {
    }

    static boolean isAtomic(Class clazz) {
        return clazz.isPrimitive() || isPrimitiveWrapper(clazz) || clazz == String.class || clazz == BigDecimal.class || clazz == BigInteger.class;
    }

    static boolean isPrimitiveWrapper(Class clazz) {
        return clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Character.class || clazz == Boolean.class || clazz == Float.class || clazz == Double.class;
    }

    static boolean isCollection(Class clazz) {
        return Collection.class == clazz || List.class == clazz || Set.class == clazz || SortedSet.class == clazz;
    }

    static boolean isMap(Class clazz) {
        return Map.class == clazz || SortedMap.class == clazz;
    }

    public static PropertyType fromProperty(Class clazz, String propertyName) throws NoSuchMethodException {
        String methodName = "get" + propertyName.substring(0, 1).toUpperCase(Locale.ENGLISH) + propertyName.substring(1);
        Method method = clazz.getMethod(methodName);
        return fromReturnType(method);
    }

    public static PropertyType fromReturnType(Method method) {
        return fromType(method.getGenericReturnType());
    }

    public static PropertyType fromField(Class clazz, String fieldName) throws NoSuchFieldException {
        Field field = clazz.getDeclaredField(fieldName);
        return fromField(field);
    }

    public static PropertyType fromField(Field field) {
        return fromType(field.getGenericType());
    }

    public static PropertyType fromType(Type type) {
        if (type instanceof Class) {
            Class clazz = (Class)type;
            if (isCollection(clazz)) {
                return new CollectionPropertyType(clazz, OBJECT);
            } else if (isMap(clazz)) {
                return new MapPropertyType(clazz, OBJECT, OBJECT);
            } else if (isAtomic(clazz)) {
                return new AtomicPropertyType(clazz);
            } else {
                return new BeanPropertyType(clazz);
            }
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type rawType = parameterizedType.getRawType();
            if (rawType instanceof Class) {
                Class rawClass = (Class) rawType;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                if (isCollection(rawClass)) {
                    PropertyType elementType = fromType(actualTypeArguments[0]);
                    return new CollectionPropertyType((Class)rawType, elementType);
                } else if (isMap(rawClass)) {
                    PropertyType keyType = fromType(actualTypeArguments[0]);
                    PropertyType elementType = fromType(actualTypeArguments[1]);
                    return new MapPropertyType(rawClass, keyType, elementType);
                } else if (isAtomic(rawClass)) {
                    return new AtomicPropertyType(rawClass);
                } else {
                    return new BeanPropertyType(rawClass);
                }
            } else {
                throw new IllegalStateException("Raw type is not a class");
            }
        } else if (type instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type;
            Type[] bounds = typeVariable.getBounds();
            return fromType(bounds[0]);
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            Type[] lowerBounds = wildcardType.getLowerBounds();
            return fromType(lowerBounds[0]);
        } else {
            throw new IllegalArgumentException("Unknown type '" + type + "'");
        }
    }
}
