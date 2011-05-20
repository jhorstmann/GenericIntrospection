package net.jhorstmann.gein.introspection;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import static java.lang.reflect.Modifier.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.jhorstmann.gein.types.PropertyType;

public abstract class PropertyDelegate {

    private String name;
    private WeakReference<Class> declaringClass;
    private PropertyType propertyType;

    static void makeAccessible(Class<?> declaringClass, int mod, AccessibleObject obj) {
        if (!obj.isAccessible()) {
            if (!isPublic(mod) || isFinal(mod) || !isPublic(declaringClass.getModifiers())) {
                obj.setAccessible(true);
            }
        }
    }

    PropertyDelegate(Class declaringClass, Type type, String name) {
        this(declaringClass, PropertyType.fromType(type), name);
    }

    PropertyDelegate(Class declaringClass, PropertyType type, String name) {
        this.declaringClass = new WeakReference<Class>(declaringClass);
        this.propertyType = type;
        this.name = name;
    }

    public String getPropertyName() {
        return name;
    }

    public Class getDeclaringClass() {
        return declaringClass.get();
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }
    
    public abstract List<? extends Annotation> getAnnotations();
    public abstract <A extends Annotation> A getAnnotations(Class<A> annotationClass);

    public abstract boolean isMutable();
    public abstract void setValue(Object bean, Object value) throws InvocationTargetException;
    public abstract Object getValue(Object bean) throws InvocationTargetException;

    Collection getCollectionValue(Object bean) throws InvocationTargetException {
        PropertyType type = getPropertyType();
        if (type.isCollection()) {
            Object collection = getValue(bean);
            if (collection == null) {
                collection = type.getDefaultImpl();
                setValue(bean, collection);
            }
            return (Collection) collection;
        } else {
            throw new IllegalArgumentException();
        }
    }

    void setCollectionValue(Object bean, Collection collection) throws InvocationTargetException {
        if (isMutable()) {
            setValue(bean, collection);
        } else {
            Collection old = (Collection) getValue(bean);
            if (old == null) {
                throw new IllegalStateException();
            } else {
                old.clear();
                old.addAll(collection);
            }
        }
    }

    Map getMapValue(Object bean) throws InvocationTargetException {
        PropertyType type = getPropertyType();
        if (type.isMap()) {
            Object map = getValue(bean);
            if (map == null) {
                map = type.getDefaultImpl();
                setValue(bean, map);
            }
            return (Map) map;
        } else {
            throw new IllegalArgumentException();
        }
    }

    void setMapValue(Object bean, Map map) throws InvocationTargetException {
        if (isMutable()) {
            setValue(bean, map);
        } else {
            Map old = (Map) getValue(bean);
            if (old == null) {
                throw new IllegalStateException();
            } else {
                old.clear();
                old.putAll(map);
            }
        }
    }

    Object getBeanValue(Object bean) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        PropertyType type = getPropertyType();
        Object value = getValue(bean);
        if (value == null) {
            value = type.getDefaultImpl();
            setValue(bean, value);
        }
        return value;
    }

    void addValue(Object bean, Object value) throws InvocationTargetException {
        getCollectionValue(bean).add(value);
    }

    void putValue(Object bean, String propertyName, Object value) throws InvocationTargetException {
        getMapValue(bean).put(propertyName, value);
    }
}
