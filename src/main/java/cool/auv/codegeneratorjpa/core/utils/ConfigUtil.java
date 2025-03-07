package cool.auv.codegeneratorjpa.core.utils;


import cool.auv.codegeneratorjpa.core.config.AutoProcessorConfiguration;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.StandardLocation;
import java.io.InputStream;
import java.util.Optional;

public class ConfigUtil {

    private static ProcessingEnvironment processingEnv = null;
//    public static AutoProcessorConfiguration loadConfig(ProcessingEnvironment processingEnv) {
//        ConfigUtil.processingEnv = processingEnv;
//
//        return loadConfigFromContext()
//                .or(ConfigUtil::loadConfigFromUIContext)
//                .or(ConfigUtil::loadConfigFromUIDefaultContext)
//                .orElseGet(ConfigUtil::loadConfigFromCurrent);
//    }
    public static AutoProcessorConfiguration loadConfig(ProcessingEnvironment processingEnv) {
        ConfigUtil.processingEnv = processingEnv;

        return loadConfigFromContext()
                .orElseGet(ConfigUtil::loadConfigFromCurrent);
    }

    private static Optional<AutoProcessorConfiguration> loadConfigFromContext() {
        try {
            InputStream inputStream = processingEnv.getFiler()
                    .getResource(StandardLocation.CLASS_PATH, "", "generator.yml")
                    .openInputStream();
            if (inputStream != null) {
                AutoProcessorConfiguration configuration = YamlUtil.loadYamlAsObject(inputStream, AutoProcessorConfiguration.class);
                return Optional.of(configuration);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private static Optional<AutoProcessorConfiguration> loadConfigFromUIContext() {
        try {
            InputStream inputStream = processingEnv.getFiler()
                    .getResource(StandardLocation.CLASS_PATH, "", "generator-ui.yml")
                    .openInputStream();
            if (inputStream != null) {
                AutoProcessorConfiguration configuration = YamlUtil.loadYamlAsObject(inputStream, AutoProcessorConfiguration.class);
                return Optional.of(configuration);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private static Optional<AutoProcessorConfiguration> loadConfigFromUIDefaultContext() {
        try {
            InputStream inputStream = processingEnv.getFiler()
                    .getResource(StandardLocation.CLASS_PATH, "", "generator-ui-default.yml")
                    .openInputStream();
            if (inputStream != null) {
                AutoProcessorConfiguration configuration = YamlUtil.loadYamlAsObject(inputStream, AutoProcessorConfiguration.class);
                return Optional.of(configuration);
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    private static AutoProcessorConfiguration loadConfigFromCurrent() {
        try {
            InputStream inputStream = processingEnv.getFiler()
                    .getResource(StandardLocation.CLASS_PATH, "", "generator-default.yml")
                    .openInputStream();
            return YamlUtil.loadYamlAsObject(inputStream, AutoProcessorConfiguration.class);
        } catch (Exception e) {
            return new AutoProcessorConfiguration();
        }
    }
}
