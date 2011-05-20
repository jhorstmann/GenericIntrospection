package net.jhorstmann.gein.types;

import java.lang.reflect.Type;

public abstract class PropertyType {
    public abstract boolean isCollection();
    public abstract boolean isMap();
    public abstract boolean isAtomic();
    public abstract boolean isArray();
    public abstract Class<?> getRawType();
    public abstract PropertyType getKeyType();
    public abstract PropertyType getElementType();
    public abstract Object getDefaultImpl();
    @Override
    public abstract boolean equals(Object other);
    @Override
    public abstract int hashCode();
    
    public static PropertyType fromType(Type type) {
        return PropertyTypeFactory.fromType(type);
    }

    public static PropertyType fromToken(TypeToken token) {
        return token.getType();
    }
}
