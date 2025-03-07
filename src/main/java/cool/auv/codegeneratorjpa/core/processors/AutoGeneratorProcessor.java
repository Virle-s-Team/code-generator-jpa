package cool.auv.codegeneratorjpa.core.processors;

import com.google.auto.service.AutoService;
import cool.auv.codegeneratorjpa.core.config.AutoProcessorConfiguration;
import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.service.javapoet.GeneratorByJavaPoetService;
import cool.auv.codegeneratorjpa.core.utils.ConfigUtil;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import java.io.IOException;
import java.util.Set;

@AutoService(Processor.class)
public class AutoGeneratorProcessor extends AbstractProcessor {

    private AutoProcessorConfiguration configuration;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        configuration = ConfigUtil.loadConfig(processingEnv);
    }


    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Set.of("cool.auv.codegeneratorjpa.core.annotation.AutoEntity");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(AutoEntity.class)) {
            if (element.getKind() == ElementKind.CLASS) {
                // 获取实体类的相关信息
                TypeElement typeElement = (TypeElement) element;
                GeneratorContext context = GeneratorContext.from(typeElement, configuration);
                try {
                    generate(context);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return false;
    }

    private void generate(GeneratorContext context) throws IOException {
        GeneratorByJavaPoetService generator = new GeneratorByJavaPoetService(processingEnv, context);
        if (configuration.getAutoProcessor().getRepository().isGenerate()) {
            generator.generateBaseMapper();
        }
        if (configuration.getAutoProcessor().getService().isGenerate()) {
            generator.generateBaseService();
            generator. generateBaseServiceImpl();
        }
        if (configuration.getAutoProcessor().getMapstruct().isGenerate()) {
            generator.generateBaseMapstruct();
        }
        if (configuration.getAutoProcessor().getController().isGenerate()) {
            generator.generateBaseController();
        }
    }
}
