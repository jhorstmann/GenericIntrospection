package net.jhorstmann.gein.types;

import java.lang.reflect.Array;

public class ArrayPropertyType extends PropertyType {
    private final PropertyType elementType;

    ArrayPropertyType(PropertyType elementType) {
        this.elementType = elementType;
    }
    
    @Override
    public PropertyType getKeyType() {
        return PropertyTypeFactory.INTEGER;
    }

    @Override
    public PropertyType getElementType() {
        return elementType;
    }

    @Override
    public Class<?> getRawType() {
        return getDefaultImpl().getClass();
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
        return false;
    }

    @Override
    public boolean isArray() {
        return true;
    }

    @Override
    public Object getDefaultImpl() {
        return Array.newInstance(elementType.getRawType(), 0);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            final ArrayPropertyType other = (ArrayPropertyType) obj;
            return this.elementType.equals(other.elementType);
        }
    }

    @Override
    public int hashCode() {
        return elementType.hashCode();
    }

    @Override
    public String toString() {
        return elementType.toString() + "[]";
    }
}
