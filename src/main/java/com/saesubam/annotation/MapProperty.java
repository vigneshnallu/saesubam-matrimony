package com.saesubam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface EntityColumn.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MapProperty {

    public String keyName() default "";

    /**
     * Type.
     * 
     * @return the string
     */
    public String type() default "";

}
