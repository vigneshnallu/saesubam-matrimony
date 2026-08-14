package com.saesubam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface PrimaryKeyJoinEntityProperty.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PrimaryKeyJoinEntityProperty {

    /**
     * Table name.
     *
     * @return the string
     */
    public String tableName() default "";

    /**
     * Column name.
     *
     * @return the string
     */
    public String columnName() default "";

}
