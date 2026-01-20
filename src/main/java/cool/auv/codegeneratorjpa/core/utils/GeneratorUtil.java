package cool.auv.codegeneratorjpa.core.utils;

import org.springframework.javapoet.AnnotationSpec;

import javax.annotation.processing.Generated;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
}
