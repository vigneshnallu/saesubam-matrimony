package com.saesubam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface SequenceEntityProperty.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SequenceEntityProperty {

    /**
     * Sequence name.
     *
     * @return the string
     */
    public String sequenceName() default "";

}
