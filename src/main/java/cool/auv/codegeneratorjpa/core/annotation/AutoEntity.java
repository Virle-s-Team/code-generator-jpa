package cool.auv.codegeneratorjpa.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface AutoEntity {
    String basePath();

    ControllerExclude[] controllerExclude() default {};

    GenerateFileType[] generateFileType() default { GenerateFileType.All };

    enum GenerateFileType {
        Controller,
        Service,
        ServiceImpl,
        Repository,
        MapperStruct,
        All
    }

    enum ControllerExclude {
        findById,
        findByPage,
        save,
        deleteById
    }
}
