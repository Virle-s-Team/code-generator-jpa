package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.base.BaseRepository;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import org.springframework.javapoet.*;
import org.springframework.stereotype.Repository;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class RepositoryGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public RepositoryGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }
    public void generateBaseMapper() throws IOException {
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        ClassName entityClass = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName idClass = ClassName.get(Long.class);

        Filer filer = processingEnv.getFiler();
        TypeSpec mapperInterface = TypeSpec.interfaceBuilder(name.getBaseRepositoryName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Repository.class).build()) // 使用 @Repository
                .addAnnotation(GeneratorUtil.buildGeneratedAnnotation())
                .addSuperinterface(
                        ParameterizedTypeName.get(ClassName.get(BaseRepository.class), entityClass, idClass)
                )
                .build();

        JavaFile javaFile = JavaFile.builder(pkg.getRepository(), mapperInterface)
                .build();
        String generatedCode = javaFile.toString();

        JavaFileObject sourceFile = filer.createSourceFile(pkg.getRepository() + "." + name.getBaseRepositoryName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }
    }

}
