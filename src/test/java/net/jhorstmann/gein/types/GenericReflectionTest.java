package net.jhorstmann.gein.types;

import java.lang.reflect.GenericArrayType;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenericReflectionTest<O, S extends String, I extends Integer> {

    private static Field getField(Class<?> clazz, String name) {
        try {
            return clazz.getField(name);
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException(ex);
        } catch (SecurityException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static PropertyDescriptor getProperty(Class<?> clazz, String name) {
        try {
            BeanInfo info = Introspector.getBeanInfo(clazz);
            for (PropertyDescriptor pd : info.getPropertyDescriptors()) {
                if (name.equals(pd.getName())) {
                    return pd;
                }
            }
            throw new RuntimeException("Property 'genericList' not found");
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }
    public List<String> stringList;

    @Test
    public void testGenericField() {
        Field field = getField(GenericReflectionTest.class, "stringList");
        Type type = field.getGenericType();
        assertNotNull(type);
        assertTrue("Type should be a ParameterizedType", type instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertEquals(List.class, parameterizedType.getRawType());
        Type[] actualTypeArguements = parameterizedType.getActualTypeArguments();
        assertNotNull(actualTypeArguements);
        assertEquals(1, actualTypeArguements.length);
        assertEquals(String.class, actualTypeArguements[0]);
    }
    public List<String>[] array;

    @Test
    public void testGenericArrayField() {
        Field field = getField(GenericReflectionTest.class, "array");
        Type type = field.getGenericType();
        assertNotNull(type);
        assertTrue("Type should be a GenericArrayType", type instanceof GenericArrayType);
        GenericArrayType genericType = (GenericArrayType) type;
        Type componentType = genericType.getGenericComponentType();
        assertNotNull(componentType);
        assertTrue("ComponentType should be a ParameterizedType", componentType instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) componentType;
        Type[] actualTypeArguements = parameterizedType.getActualTypeArguments();
        assertNotNull(actualTypeArguements);
        assertEquals(1, actualTypeArguements.length);
        assertEquals(String.class, actualTypeArguements[0]);
    }

    public List<String> getStringList() {
        return stringList;
    }

    @Test
    public void testStringListProperty() {
        PropertyDescriptor pd = getProperty(GenericReflectionTest.class, "stringList");
        Type returnType = pd.getReadMethod().getGenericReturnType();
        assertNotNull(returnType);
        assertTrue("Parameter should be a ParameterizedType", returnType instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) returnType;
        assertEquals(List.class, parameterizedType.getRawType());
        Type[] actualTypeArguements = parameterizedType.getActualTypeArguments();
        assertNotNull(actualTypeArguements);
        assertEquals(1, actualTypeArguements.length);
        assertEquals(Class.class, actualTypeArguements[0].getClass());
        assertEquals(String.class, actualTypeArguements[0]);
    }
    public List<List<String>> nestedParameterizedList;

    @Test
    public void testNestedParameterizedList() {
        Field field = getField(GenericReflectionTest.class, "nestedParameterizedList");
        Type type = field.getGenericType();
        assertNotNull(type);
        assertTrue("Type should be a ParameterizedType", type instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertEquals(List.class, parameterizedType.getRawType());
        Type[] actualTypeArguements = parameterizedType.getActualTypeArguments();
        assertNotNull(actualTypeArguements);
        assertEquals(1, actualTypeArguements.length);

        assertTrue("Type argument should be a ParameterizedType", actualTypeArguements[0] instanceof ParameterizedType);
        ParameterizedType nestedParameterizedType = (ParameterizedType) actualTypeArguements[0];
        assertEquals(List.class, nestedParameterizedType.getRawType());
        Type[] nestedActualTypeArguements = nestedParameterizedType.getActualTypeArguments();
        assertNotNull(nestedActualTypeArguements);
        assertEquals(1, nestedActualTypeArguements.length);
        assertEquals(String.class, nestedActualTypeArguements[0]);
    }
    public List<S> genericList;

    @Test
    public void testGenericList() {
        Field field = getField(GenericReflectionTest.class, "genericList");
        Type type = field.getGenericType();
        assertNotNull(type);
        assertTrue("Type should be a ParameterizedType", type instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertEquals(List.class, parameterizedType.getRawType());
        Type[] actualTypeArguements = parameterizedType.getActualTypeArguments();
        assertNotNull(actualTypeArguements);
        assertEquals(1, actualTypeArguements.length);

        assertTrue("Type argument should be a Type TypeVariable", actualTypeArguements[0] instanceof TypeVariable);
        TypeVariable typeVariable = (TypeVariable) actualTypeArguements[0];
        assertEquals("S", typeVariable.getName());
        Type[] bounds = typeVariable.getBounds();
        assertEquals(1, bounds.length);
        assertEquals(String.class, bounds[0]);
    }
    public List<List<I>> nestedGenericList;

    @Test
    public void testNestedGenericList() {
        Field field = getField(GenericReflectionTest.class, "nestedGenericList");
        Type type = field.getGenericType();
        assertNotNull(type);
        assertTrue("Type should be a ParameterizedType", type instanceof ParameterizedType);
        ParameterizedType parameterizedType = (ParameterizedType) type;
        assertEquals(List.class, parameterizedType.getRawType());
        Type[] actualTypeArguements = parameterizedType.getActualTypeArguments();
        assertNotNull(actualTypeArguements);
        assertEquals(1, actualTypeArguements.length);

        assertTrue("Type argument should be a ParameterizedType", actualTypeArguements[0] instanceof ParameterizedType);
        ParameterizedType nestedParameterizedType = (ParameterizedType) actualTypeArguements[0];
        assertEquals(List.class, nestedParameterizedType.getRawType());
        Type[] nestedActualTypeArguements = nestedParameterizedType.getActualTypeArguments();
        assertNotNull(nestedActualTypeArguements);
        assertEquals(1, nestedActualTypeArguements.length);

        assertTrue("Nested type argument should be a TypeVariable", nestedActualTypeArguements[0] instanceof TypeVariable);
        TypeVariable typeVariable = (TypeVariable) nestedActualTypeArguements[0];
        assertEquals("I", typeVariable.getName());
        Type[] bounds = typeVariable.getBounds();
        assertEquals(1, bounds.length);
        assertEquals(Integer.class, bounds[0]);
    }
}
