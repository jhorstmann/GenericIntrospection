package net.jhorstmann.gein.types;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

final class CollectionPropertyType extends PropertyType {

    private static final Map<Class, Class> DEFAULT_IMPL = new HashMap<Class, Class>();
    static {
        DEFAULT_IMPL.put(Set.class, HashSet.class);
        DEFAULT_IMPL.put(SortedSet.class, TreeSet.class);
        DEFAULT_IMPL.put(List.class, ArrayList.class);
        DEFAULT_IMPL.put(Collection.class, ArrayList.class);
    }
    private final WeakReference<Class<? extends Collection>> collectionType;
    private final PropertyType elementType;


    CollectionPropertyType(Class<? extends Collection> listType, PropertyType elementType) {
        this.collectionType = new WeakReference<Class<? extends Collection>>(listType);
        this.elementType = elementType;
    }

    @Override
    public PropertyType getKeyType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyType getElementType() {
        return elementType;
    }

    @Override
    public Class<?> getRawType() {
        return collectionType.get();
    }

    @Override
    public boolean isCollection() {
        return true;
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
        return false;
    }

    @Override
    public Collection getDefaultImpl() {
        Class<? extends Collection> clazz = collectionType.get();
        if (SortedSet.class == clazz) {
            return Collections.checkedSet(new TreeSet(), elementType.getRawType());
        } else if (Set.class == clazz) {
            return Collections.checkedSet(new HashSet(), elementType.getRawType());
        } else if (List.class == clazz || Collection.class == clazz) {
            return Collections.checkedList(new ArrayList(), elementType.getRawType());
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
            final CollectionPropertyType other = (CollectionPropertyType) obj;
            return this.collectionType.get().equals(other.collectionType.get()) && this.elementType.equals(other.elementType);
        }
    }

    @Override
    public int hashCode() {
        return collectionType.get().hashCode() * 17 + elementType.hashCode();
    }

    @Override
    public String toString() {
        return collectionType.get().getName() + "<" + elementType.toString() + ">";
    }
}
