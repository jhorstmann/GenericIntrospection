package net.jhorstmann.gein.introspection;


import java.util.Collections;
import java.util.Map;

class MapBasedProperty extends PropertyDelegate {
    private static final Class<? extends Map> EMPTY_MAP_CLASS = Collections.emptyMap().getClass();
    private static final Class<? extends Map> IMMUTABLE_MAP_CLASS = Collections.unmodifiableMap(Collections.emptyMap()).getClass();

    MapBasedProperty(Class<? extends Map> declaringClass, Class<?> valueType, String name) {
        super(declaringClass, valueType, name);
    }

    @Override
    public boolean isMutable() {
        Class declaringClass = getDeclaringClass();
        return declaringClass != EMPTY_MAP_CLASS && declaringClass != IMMUTABLE_MAP_CLASS;
    }

    @Override
    public void setValue(Object bean, Object value) {
        setValue(bean, getPropertyName(), value);
    }

    @Override
    public Object getValue(Object bean) {
        return getValue(bean, getPropertyName());
    }

    void setValue(Object bean, String property, Object value) {
        ((Map)bean).put(property, value);
    }

    Object getValue(Object bean, String property) {
        return ((Map)bean).get(property);
    }
}
