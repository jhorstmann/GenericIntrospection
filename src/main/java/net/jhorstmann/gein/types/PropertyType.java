package net.jhorstmann.gein.types;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import net.jhorstmann.gein.introspection.ReflectException;

public abstract class PropertyType {
    public abstract boolean isCollection();
    public abstract boolean isMap();
    public abstract boolean isAtomic();
    public abstract Class<?> getRawType();
    public abstract PropertyType getKeyType();
    public abstract PropertyType getElementType();
    public abstract Object getDefaultImpl();

    static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getConstructor();
            try {
                return constructor.newInstance();
            } catch (InstantiationException ex) {
                throw new ReflectException(ex);
            } catch (IllegalAccessException ex) {
                throw new ReflectException(ex);
            } catch (IllegalArgumentException ex) {
                throw new ReflectException(ex);
            } catch (InvocationTargetException ex) {
                Throwable cause = ex.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                } else {
                    throw new ReflectException(cause);
                }
            }
        } catch (NoSuchMethodException ex) {
            throw new ReflectException(ex);
        } catch (SecurityException ex) {
            throw new ReflectException(ex);
        }
    }
}
