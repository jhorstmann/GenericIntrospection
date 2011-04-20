package net.jhorstmann.gein.types;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

public class PropertyTypeFactoryTest {
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
        PropertyType type = PropertyTypeFactory.fromToken(new TypeToken<List<String>>(){});
        assertFalse(type.isAtomic());
        assertTrue(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(List.class, type.getRawType());
        assertEquals(String.class, type.getElementType().getRawType());
    }

    @Test
    public void testListOfListOfString() throws NoSuchFieldException {
        PropertyType type = PropertyTypeFactory.fromToken(new TypeToken<List<List<String>>>(){});
        assertFalse(type.isAtomic());
        assertTrue(type.isCollection());
        assertFalse(type.isMap());
        assertEquals(List.class, type.getRawType());
        assertEquals(List.class, type.getElementType().getRawType());
        assertEquals(String.class, type.getElementType().getElementType().getRawType());
    }
}
