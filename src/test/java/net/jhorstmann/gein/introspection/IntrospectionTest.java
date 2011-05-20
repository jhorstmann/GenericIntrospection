package net.jhorstmann.gein.introspection;

import java.lang.reflect.InvocationTargetException;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import java.util.List;
import java.util.Map;
import net.jhorstmann.gein.types.PropertyType;
import net.jhorstmann.gein.types.TypeToken;
import org.junit.Test;

public class IntrospectionTest {
    public static class TestItem<T> {
        private T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
    
    public static class TestBean {
        private String string;
        private int integer;
        private List<TestItem<String>> items;

        public int getInteger() {
            return integer;
        }

        public void setInteger(int integer) {
            this.integer = integer;
        }

        public List<TestItem<String>> getItems() {
            return items;
        }

        public void setItems(List<TestItem<String>> items) {
            this.items = items;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
    
    public static class TestStruct {
        public String string;
        public int integer;
        public List<TestItem<String>> items;
    }
    
    private void introspect(Class<?> clazz, Map<String, PropertyDelegate> properties) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        Object obj = clazz.newInstance();
        PropertyDelegate stringProperty = properties.get("string");
        assertNotNull(stringProperty);
        assertEquals("string", stringProperty.getPropertyName());
        assertEquals(PropertyType.fromType(String.class), stringProperty.getPropertyType());
        stringProperty.setValue(obj, "string");
        assertEquals("string", stringProperty.getValue(obj));

        PropertyDelegate intProperty = properties.get("integer");
        assertNotNull(intProperty);
        assertEquals("integer", intProperty.getPropertyName());
        assertEquals(PropertyType.fromType(Integer.TYPE), intProperty.getPropertyType());
        intProperty.setValue(obj, 123);
        assertEquals(123, intProperty.getValue(obj));
        
        PropertyDelegate itemsProperty = properties.get("items");
        assertNotNull(itemsProperty);
        assertEquals("items", itemsProperty.getPropertyName());
        assertEquals(PropertyType.fromToken(new TypeToken<List<TestItem<String>>>(){}), itemsProperty.getPropertyType());
    }
    
    @Test
    public void introspectBean() throws Exception {
        Map<String, PropertyDelegate> properties = Introspection.introspectProperties(TestBean.class);
        assertNotNull(properties);
        assertEquals(3, properties.size());
        
        introspect(TestBean.class, properties);
    }
    
    @Test
    public void introspectStruct() throws Exception {
        Map<String, PropertyDelegate> properties = Introspection.introspectFields(TestStruct.class);
        assertNotNull(properties);
        assertEquals(3, properties.size());
        
        introspect(TestStruct.class, properties);
    }
}
