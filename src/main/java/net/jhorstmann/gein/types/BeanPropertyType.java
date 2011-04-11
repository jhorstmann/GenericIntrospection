package net.jhorstmann.gein.types;

import java.lang.ref.WeakReference;

final class BeanPropertyType extends PropertyType {
    private final WeakReference<Class> beanClass;

    BeanPropertyType(Class<?> beanClass) {
        this.beanClass = new WeakReference<Class>(beanClass);
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
    public Class<?> getRawType() {
        return beanClass.get();
    }

    @Override
    public PropertyType getKeyType() {
        return PropertyTypeFactory.STRING;
    }

    @Override
    public PropertyType getElementType() {
        throw new UnsupportedOperationException("Not a uniform container type");
    }

    @Override
    public Object getDefaultImpl() {
        return newInstance(beanClass.get());
    }
}
