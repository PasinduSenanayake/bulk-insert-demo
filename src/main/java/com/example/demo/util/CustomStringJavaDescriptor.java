package com.example.demo.util;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

public class CustomStringJavaDescriptor extends AbstractTypeDescriptor<CustomInteger> {

    public static final CustomStringJavaDescriptor INSTANCE =
            new CustomStringJavaDescriptor();

    public CustomStringJavaDescriptor() {
        super(CustomInteger.class, ImmutableMutabilityPlan.INSTANCE);
    }

    @Override
    public String toString(CustomInteger value) {
        return value.getValue().toString();
    }

    @Override
    public CustomInteger fromString(String string) {
        return new CustomInteger(Integer.parseInt(string));

    }

    @Override
    public <X> X unwrap(CustomInteger value, Class<X> type, WrapperOptions options) {

        if (value == null) {
            return null;
        }

        if(value.getValue()==null){
            return null;
        }

        if (String.class.isAssignableFrom(type))
            return (X) String.valueOf(value.getValue());

        throw unknownUnwrap(type);
    }

    @Override
    public <X> CustomInteger wrap(X value, WrapperOptions options) {
        if (value == null)
            return null;

        if (value instanceof String) {
            return new CustomInteger(Integer.parseInt((String) value));
        }

        throw unknownWrap(value.getClass());
    }

}