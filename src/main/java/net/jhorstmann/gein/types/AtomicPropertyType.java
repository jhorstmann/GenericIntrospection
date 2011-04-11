package net.jhorstmann.gein.types;

import java.util.HashMap;
import java.util.Map;

final class AtomicPropertyType extends PropertyType {
    private static final Map<Class, Object> DEFAULT_VALUES = new HashMap<Class, Object>();
    static {
        DEFAULT_VALUES.put(Byte.TYPE , Byte.valueOf((byte)0));
        DEFAULT_VALUES.put(Short.TYPE, Short.valueOf((short)0));
        DEFAULT_VALUES.put(Integer.TYPE, Integer.valueOf(0));
        DEFAULT_VALUES.put(Long.TYPE, Long.valueOf(0));
        DEFAULT_VALUES.put(Float.TYPE, Float.valueOf(0));
        DEFAULT_VALUES.put(Double.TYPE, Double.valueOf(0));
        DEFAULT_VALUES.put(Character.TYPE, Character.valueOf((char)0));
    }
    private final Class<?> type;

    AtomicPropertyType(Class<?> type) {
        this.type = type;
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isMap() {
        return false;
    }

    @Override
    public boolean isAtomic() {
        return true;
    }

    @Override
    public PropertyType getKeyType() {
        throw new UnsupportedOperationException("Not a container type");
    }

    @Override
    public PropertyType getElementType() {
        throw new UnsupportedOperationException("Not a container type");
    }

    @Override
    public Class<?> getRawType() {
        return type;
    }

    @Override
    public Object getDefaultImpl() {
        Object result = DEFAULT_VALUES.get(type);

        return result;
    }
}
