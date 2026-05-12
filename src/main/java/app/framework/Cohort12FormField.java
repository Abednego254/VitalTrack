package app.framework;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cohort12FormField {

    String label() default "Label Name";

    String name() default "";

    String placeholder() default "";

    boolean required() default true;

}
