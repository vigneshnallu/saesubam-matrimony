package com.saesubam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface EntityProperty.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EntityProperty {

    /**
     * Column name.
     *
     * @return the string
     */
    public String columnName() default "";

    /**
     * Entity.
     *
     * @return the string
     */
    public String entity() default "";

    /**
     * Type.
     *
     * @return the string
     */
    public String type() default "";

    /**
     * Key name.
     *
     * @return the string
     */
    public String keyName() default "";

    /**
     * Updatable.
     *
     * @return true, if successful
     */
    public boolean updatable() default true;
}
