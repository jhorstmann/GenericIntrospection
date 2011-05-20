package net.jhorstmann.gein.introspection;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class FieldBasedProperty extends PropertyDelegate {

    private WeakReference<Field> field;
    private List<Annotation> annotations;

    FieldBasedProperty(Field field) {
        this(field, field.getName());
    }

    FieldBasedProperty(Field field, String name) {
        super(field.getDeclaringClass(), field.getGenericType(), name);
        makeAccessible(field.getDeclaringClass(), field.getModifiers(), field);
        this.field = new WeakReference<Field>(field);
    }

    Field getField() {
        return field.get();
    }

    @Override
    public List<? extends Annotation> getAnnotations() {
        if (annotations != null) {
            Annotation[] arr = getField().getAnnotations();
            annotations = Collections.unmodifiableList(Arrays.asList(arr));
        }
        return annotations;
    }

    @Override
    public <A extends Annotation> A getAnnotations(Class<A> annotationClass) {
        return getField().getAnnotation(annotationClass);
    }

    @Override
    public boolean isMutable() {
        return !Modifier.isFinal(field.get().getModifiers());
    }

    @Override
    public void setValue(Object bean, Object value) {
        try {
            getField().set(bean, value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Field is not accessible", ex);
        }
    }

    @Override
    public Object getValue(Object bean) {
        try {
            return getField().get(bean);
        } catch (IllegalArgumentException ex) {
            throw new IllegalStateException(ex);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Field is not accessible", ex);
        }
    }
}
