package net.jhorstmann.gein.types;

import java.util.Map;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyTypeFactoryTest {
    @Test
    public void testAtomicType() {
        PropertyType type = PropertyType.fromType(Integer.TYPE);
        assertTrue(type.isAtomic());
        assertFalse(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(Integer.TYPE, type.getRawType());
    }

    @Test
    public void testListOfString() throws NoSuchFieldException {
        PropertyType type = PropertyType.fromToken(new TypeToken<List<String>>(){});
        assertFalse(type.isAtomic());
        assertTrue(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(List.class, type.getRawType());
        assertEquals(String.class, type.getElementType().getRawType());
    }

    @Test
    public void testListOfListOfString() throws NoSuchFieldException {
        PropertyType type = PropertyType.fromToken(new TypeToken<List<List<String>>>(){});
        assertFalse(type.isAtomic());
        assertTrue(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(List.class, type.getRawType());
        assertEquals(List.class, type.getElementType().getRawType());
        assertEquals(String.class, type.getElementType().getElementType().getRawType());
    }
    
    @Test
    public void testAtomicTypeToString() {
        assertEquals("int", PropertyType.fromType(Integer.TYPE).toString());
        assertEquals("char", PropertyType.fromType(Character.TYPE).toString());
        assertEquals("java.lang.Integer", PropertyType.fromType(Integer.class).toString());
        assertEquals("java.lang.Character", PropertyType.fromType(Character.class).toString());
        assertEquals("java.lang.String", PropertyType.fromType(String.class).toString());
    }
    
    @Test
    public void testCollectionTypeToString() {
        assertEquals("java.util.List<java.lang.String>", PropertyType.fromToken(new TypeToken<List<String>>(){}).toString());
    }
    
    @Test
    public void testMapTypeToString() {
        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", PropertyType.fromToken(new TypeToken<Map<String, Integer>>(){}).toString());
    }
}
