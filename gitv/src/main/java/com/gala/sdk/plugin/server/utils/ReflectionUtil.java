package com.gala.sdk.plugin.server.utils;

public class ReflectionUtil {
    public static IMethodHolder create(Object instance, String methodName, Class<?>... params) {
        return new ObjectMethodHolder(instance, methodName, params);
    }
}
