package cool.auv.codegeneratorjpa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 暂未启用
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Exclude {

    ExcludeFile[] excludeFile() default {ExcludeFile.ALL};

    enum ExcludeFile {
        VM,
        BODY,
        REQUEST,
        ALL
    }
}
