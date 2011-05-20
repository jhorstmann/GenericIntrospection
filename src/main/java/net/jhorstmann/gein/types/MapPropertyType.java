package net.jhorstmann.gein.types;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

final class MapPropertyType extends PropertyType {

    private final WeakReference<Class<? extends Map>> mapClass;
    private final PropertyType keyType;
    private final PropertyType valueType;

    MapPropertyType(Class<? extends Map> mapClass, PropertyType keyType, PropertyType valueType) {
        this.mapClass = new WeakReference<Class<? extends Map>>(mapClass);
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public PropertyType getKeyType() {
        return keyType;
    }

    @Override
    public PropertyType getElementType() {
        return valueType;
    }

    @Override
    public Class<?> getRawType() {
        return mapClass.get();
    }

    @Override
    public boolean isCollection() {
        return false;
    }

    @Override
    public boolean isMap() {
        return true;
    }

    @Override
    public boolean isAtomic() {
        return false;
    }

    @Override
    public Map getDefaultImpl() {
        Class<? extends Map> clazz = mapClass.get();
        if (Map.class == clazz) {
            return Collections.checkedMap(new HashMap(), keyType.getRawType(), valueType.getRawType());
        } else if (SortedMap.class == clazz) {
            return Collections.checkedMap(new TreeMap(), keyType.getRawType(), valueType.getRawType());
        } else {
            return PropertyTypeFactory.newInstance(clazz);
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            final MapPropertyType other = (MapPropertyType) obj;
            return this.mapClass.get().equals(other.mapClass.get()) && this.keyType.equals(other.keyType) && this.valueType.equals(other.valueType);
        }
    }

    @Override
    public int hashCode() {
        return (mapClass.get().hashCode() * 17 + keyType.hashCode()) * 17 + valueType.hashCode();
    }
}
