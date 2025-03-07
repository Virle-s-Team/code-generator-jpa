package cool.auv.codegeneratorjpa.core.utils;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import org.springframework.javapoet.AnnotationSpec;

import javax.annotation.processing.Generated;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Properties;
import java.util.Set;

public class GeneratorUtil {

    private static final Properties SYSTEM_PROPS = System.getProperties();

    public static AnnotationSpec buildGeneratedAnnotation() {
        return AnnotationSpec.builder(Generated.class)
                .addMember("value", "$S", GeneratorUtil.generatorName())
                .addMember("date", "$S", GeneratorUtil.generationTimestamp())
                .addMember("comments", "$S", GeneratorUtil.buildComments())
                .build();
    }

    public static boolean checkGenerate(AutoEntity.GenerateFileType type, GeneratorContext context) {
        Set<AutoEntity.GenerateFileType> MAPPER_GENERATION_TYPES =
                EnumSet.of(type, AutoEntity.GenerateFileType.All);

        AutoEntity.GenerateFileType[] generateFileTypes = context.getAnnotations().autoEntity().generateFileType();
        if (Arrays.stream(generateFileTypes).noneMatch(MAPPER_GENERATION_TYPES::contains)) {
            return false;
        }
        return true;
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
}
