package com.app.common.annotation;

import java.lang.reflect.Method;
import java.util.HashMap;

public class Test {


    @Hello("hello")
    public static void main(String[] args) throws NoSuchMethodException {

        Class cls = Test.class;

        Method method = cls.getMethod("main",String[].class);


        new HashMap<>();
        Hello hello = method.getAnnotation(Hello.class);
    }




}
