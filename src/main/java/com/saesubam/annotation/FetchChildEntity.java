package com.saesubam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface FetchChildEntity.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FetchChildEntity {

    /**
     * Parent property names.
     *
     * @return the string
     */
    public String parentPropertyNames();

    /**
     * Child property names.
     *
     * @return the string
     */
    public String childPropertyNames();
}
