package com.alibaba.fastjson;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeReference<T> {
    protected final Type type;

    protected TypeReference() {
        this.type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected TypeReference(Type... actualTypeArguments) {
        ParameterizedType argType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        Type rawType = argType.getRawType();
        Type[] argTypes = argType.getActualTypeArguments();
        int actualIndex = 0;
        for (int i = 0; i < argTypes.length; i++) {
            if (argTypes[i] instanceof TypeVariable) {
                int actualIndex2 = actualIndex + 1;
                argTypes[i] = actualTypeArguments[actualIndex];
                if (actualIndex2 >= actualTypeArguments.length) {
                    actualIndex = actualIndex2;
                    break;
                }
                actualIndex = actualIndex2;
            }
        }
        this.type = new ParameterizedTypeImpl(argTypes, getClass(), rawType);
    }

    public Type getType() {
        return this.type;
    }
}
