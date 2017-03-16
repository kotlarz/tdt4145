package treningsdagbok.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableColumn {
    int length() default 0;
    boolean nullable() default false;
    boolean autoIncrement() default false;
    boolean primaryKey() default false;
    boolean identifier() default false;
    String fieldDefault() default "";
    int precision() default 0;
    int scale() default 0;
    String[] foreignKey() default {};
}