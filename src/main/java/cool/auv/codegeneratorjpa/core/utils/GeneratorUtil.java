package cool.auv.codegeneratorjpa.core.utils;

import jakarta.persistence.Id;
import org.springframework.javapoet.AnnotationSpec;
import org.springframework.javapoet.TypeName;

import javax.annotation.processing.Generated;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;

public class GeneratorUtil {

    private static final Properties SYSTEM_PROPS = System.getProperties();

    public static AnnotationSpec buildGeneratedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", GeneratorUtil.generatorName())
                .addMember("date", "$S", GeneratorUtil.generationTimestamp())
                .addMember("comments", "$S", GeneratorUtil.buildComments())
                .build();
    }

    // 获取生成器名称
    static String generatorName() {
        return "cool.auv.codegeneratorjpa.core.processors.AutoGeneratorProcessor";
    }

    static String generatorVersion() {
        return "0.0.1";
    }

    // 生成 ISO 8601 格式时间戳（带时区）
    static String generationTimestamp() {
        return ZonedDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX"));
    }

    // 构建注释信息
    static String buildComments() {
        return String.format(
                "version: %s, compiler: %s, environment: Java %s (%s)",
                generatorVersion(),
                detectCompiler(),
                SYSTEM_PROPS.getProperty("java.version"),
                SYSTEM_PROPS.getProperty("java.vm.name")
        );
    }

    // 检测编译器类型
    private static String detectCompiler() {
        String compiler = SYSTEM_PROPS.getProperty("sun.management.compiler");
        if (compiler != null) {
            return compiler.contains("HotSpot") ? "javac" : compiler;
        }
        return SYSTEM_PROPS.getProperty("java.vm.name");
    }

    /**
     * 从 Entity 类中获取 @Id 字段的类型
     *
     * @param entityElement Entity 类的 TypeElement
     * @return ID 字段的 TypeName，如果未找到 @Id 字段则返回 Long.class
     */
    public static TypeName getIdTypeName(TypeElement entityElement) {
        // 获取所有字段（包括继承的字段）
        List<VariableElement> fields = ElementFilter.fieldsIn(entityElement.getEnclosedElements());

        // 查找带有 @Id 注解的字段
        for (VariableElement field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                TypeMirror fieldType = field.asType();
                return TypeName.get(fieldType);
            }
        }

        // 如果未找到 @Id 注解，尝试从父类查找
        TypeElement currentElement = entityElement;
        while (currentElement.getSuperclass().getKind() != javax.lang.model.type.TypeKind.NONE) {
            TypeMirror superClass = currentElement.getSuperclass();
            currentElement = (TypeElement) ((javax.lang.model.type.DeclaredType) superClass).asElement();
            fields = ElementFilter.fieldsIn(currentElement.getEnclosedElements());
            for (javax.lang.model.element.VariableElement field : fields) {
                if (field.getAnnotation(Id.class) != null) {
                    TypeMirror fieldType = field.asType();
                    return TypeName.get(fieldType);
                }
            }
        }

        // 默认返回 Long 类型
        return TypeName.get(Long.class);
    }
}
