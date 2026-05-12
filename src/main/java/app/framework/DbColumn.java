package app.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DbColumn {
    String name();
    String type();
    boolean primaryKey() default false;
    boolean autoIncrement() default false;
}