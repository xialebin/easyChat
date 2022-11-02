package com.app.common.annotation;

import java.lang.annotation.*;

@Target(value = {ElementType.FIELD,ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
public @interface Hello {
    String value();
}
