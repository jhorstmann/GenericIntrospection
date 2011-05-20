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
        return PropertyTypeFactory.newInstance(beanClass.get());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            final BeanPropertyType other = (BeanPropertyType) obj;
            return this.beanClass.get().equals(other.beanClass.get());
        }
    }

    @Override
    public int hashCode() {
        return beanClass.get().hashCode();
    }
}
