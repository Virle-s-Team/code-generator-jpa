package cool.auv.codegeneratorjpa.core.entity;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.config.AutoProcessorConfiguration;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.javapoet.TypeName;

import javax.lang.model.element.TypeElement;

@Data
@Accessors(chain =true)
public class GeneratorContext {

    String entityClassName;

    EntityAnnotations annotations;

    GeneratorParameter.Name name;

    GeneratorParameter.Package pkg;

    AutoProcessorConfiguration configuration;

    /**
     * Entity 类的 TypeElement，用于获取字段信息
     */
    TypeElement entityElement;


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
                .setEntityElement(element)
                .setAnnotations(new EntityAnnotations(
                        autoEntity
                ))
                .setConfiguration(configuration)
                .setName(name)
                .setPkg(pkg);
    }

    /**
     * 获取 Entity 中 @Id 字段的类型
     *
     * @return ID 字段的 TypeName
     */
    public TypeName getIdTypeName() {
        return GeneratorUtil.getIdTypeName(entityElement);
    }
}

