package com.example.demo.util;


import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

import java.util.concurrent.atomic.AtomicInteger;



public class CustomInteger extends AbstractSingleColumnStandardBasicType<CustomInteger> {
    private static final AtomicInteger atomicRefId = new AtomicInteger();

    private Integer value ;

    public CustomInteger() {
        super(VarcharTypeDescriptor.INSTANCE, CustomStringJavaDescriptor.INSTANCE);
        value = atomicRefId.getAndIncrement();
    }

    public CustomInteger(Integer integer) {
        super(VarcharTypeDescriptor.INSTANCE, CustomStringJavaDescriptor.INSTANCE);
        value = integer;
    }

    @Override
    public String getName() {
        return "CustomInteger";
    }


    public void setValue(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
