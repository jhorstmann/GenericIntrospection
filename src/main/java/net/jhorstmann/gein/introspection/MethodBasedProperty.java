package net.jhorstmann.gein.introspection;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class MethodBasedProperty extends PropertyDelegate {

    private WeakReference<Method> readMethod;
    private WeakReference<Method> writeMethod;
    private List<Annotation> annotations;

    MethodBasedProperty(PropertyDescriptor pd) {
        this(pd.getReadMethod(), pd.getWriteMethod(), pd.getName());
    }

    MethodBasedProperty(Method readMethod, Method writeMethod, String name) {
        super(readMethod.getDeclaringClass(), readMethod.getGenericReturnType(), name);
        makeAccessible(readMethod.getDeclaringClass(), readMethod.getModifiers(), readMethod);
        this.readMethod = new WeakReference<Method>(readMethod);
        if (writeMethod != null) {
            makeAccessible(writeMethod.getDeclaringClass(), writeMethod.getModifiers(), writeMethod);
            this.writeMethod = new WeakReference<Method>(writeMethod);
        }
    }

    MethodBasedProperty(Method readMethod, String name) {
        super(readMethod.getDeclaringClass(), readMethod.getGenericReturnType(), name);
        this.readMethod = new WeakReference<Method>(readMethod);
        makeAccessible(readMethod.getDeclaringClass(), readMethod.getModifiers(), readMethod);
    }

    @Override
    public List<? extends Annotation> getAnnotations() {
        if (annotations == null) {
            Annotation[] ra = readMethod.get().getAnnotations();
            Annotation[] wa = writeMethod != null ? writeMethod.get().getAnnotations() : new Annotation[0];
            List<Annotation> list = new ArrayList<Annotation>(ra.length + wa.length);
            list.addAll(Arrays.asList(ra));
            list.addAll(Arrays.asList(wa));
            annotations = Collections.unmodifiableList(list);
        }
        return annotations;
    }

    @Override
    public <A extends Annotation> A getAnnotations(Class<A> annotationClass) {
        A a = readMethod.get().getAnnotation(annotationClass);
        if (a != null) {
            return a;
        } else if (writeMethod != null) {
            return writeMethod.get().getAnnotation(annotationClass);
        } else {
            return null;
        }
    }

    @Override
    public boolean isMutable() {
        return writeMethod != null;
    }

    @Override
    public void setValue(Object bean, Object value) throws InvocationTargetException {
        if (writeMethod == null) {
            throw new IllegalStateException("Property '" + getPropertyName() + "' is immmutable");
        } else {
            Method method = writeMethod.get();
            try {
                method.invoke(bean, value);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException("Write method for property '" + getPropertyName() + "' is not accessible", ex);
            }
        }
    }

    @Override
    public Object getValue(Object bean) throws InvocationTargetException {
        Method method = readMethod.get();
        try {
            return method.invoke(bean);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Read method for property '" + getPropertyName() + "' is not accessible", ex);
        }
    }
}
