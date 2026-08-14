package com.saesubam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The Interface FormulaEntityProperty.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormulaEntityProperty {

    /**
     * Column name.
     *
     * @return the string
     */
    public String tableName() default "";

    /**
     * Join type.
     *
     * @return the string
     */
    public String joinType() default "INNER JOIN";

    /**
     * Join column name.
     *
     * @return the string
     */
    public String joinColumnName() default "";

    /**
     * Column name.
     *
     * @return the string
     */
    public String columnName() default "";

    /**
     * Alias name.
     *
     * @return the string
     */
    public String aliasName() default "";

    /**
     * Condition.
     *
     * @return the string
     */
    public String condition() default "";

}
