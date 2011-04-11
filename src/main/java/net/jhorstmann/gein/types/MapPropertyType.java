package net.jhorstmann.gein.types;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;

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
            return newInstance(clazz);
        }
    }
}
