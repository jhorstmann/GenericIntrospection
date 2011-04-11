package net.jhorstmann.gein.types;

import net.jhorstmann.gein.types.PropertyType;
import net.jhorstmann.gein.types.PropertyTypeFactory;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyTypeFactoryTest {
    public final List<String> listOfString = new ArrayList<String>();
    public final List<List<String>> listOfListOfString = new ArrayList<List<String>>();

    @Test
    public void testAtomicType() {
        PropertyType type = PropertyTypeFactory.fromType(Integer.TYPE);
        assertTrue(type.isAtomic());
        assertFalse(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(Integer.TYPE, type.getRawType());
    }

    @Test
    public void testListOfString() throws NoSuchFieldException {
        PropertyType type = PropertyTypeFactory.fromType(PropertyTypeFactoryTest.class.getDeclaredField("listOfString").getGenericType());
        assertFalse(type.isAtomic());
        assertTrue(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(List.class, type.getRawType());
        assertEquals(String.class, type.getElementType().getRawType());
    }

    @Test
    public void testListOfListOfString() throws NoSuchFieldException {
        PropertyType type = PropertyTypeFactory.fromType(PropertyTypeFactoryTest.class.getDeclaredField("listOfListOfString").getGenericType());
        assertFalse(type.isAtomic());
        assertTrue(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(List.class, type.getRawType());
        assertEquals(List.class, type.getElementType().getRawType());
        assertEquals(String.class, type.getElementType().getElementType().getRawType());
    }
}
