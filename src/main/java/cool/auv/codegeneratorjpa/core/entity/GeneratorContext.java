package cool.auv.codegeneratorjpa.core.entity;

import cool.auv.codegeneratorjpa.core.config.AutoProcessorConfiguration;
import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.lang.model.element.TypeElement;

@Data
@Accessors(chain =true)
public class GeneratorContext {

    String entityClassName;

    EntityAnnotations annotations;

    GeneratorParameter.Name name;

    GeneratorParameter.Package pkg;

    AutoProcessorConfiguration configuration;


    public static GeneratorContext from(TypeElement element, AutoProcessorConfiguration configuration) {
        AutoEntity autoEntity = element.getAnnotation(AutoEntity.class);
        if (autoEntity == null) {
            throw new IllegalArgumentException("元素必须包含 @AutoEntity 注解");
        }
        String entityClassName = element.getSimpleName().toString();
        GeneratorParameter.Name name = new GeneratorParameter.Name(entityClassName);
        GeneratorParameter.Package pkg = new GeneratorParameter.Package(configuration);
        return new GeneratorContext()
                .setEntityClassName(entityClassName)
                .setAnnotations(new EntityAnnotations(
                        autoEntity
                ))
                .setConfiguration(configuration)
                .setName(name)
                .setPkg(pkg);
    }
}

