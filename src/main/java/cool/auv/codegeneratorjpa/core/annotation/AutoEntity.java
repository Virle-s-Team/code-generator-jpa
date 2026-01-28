package cool.auv.codegeneratorjpa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AutoEntity {
    String basePath();

    String docTag() default "";

    boolean enableMapperAnnotation() default true;
}
