package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.javapoet.*;


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

        if (!GeneratorUtil.checkGenerate(AutoEntity.GenerateFileType.Repository, context)) {
            return;
        }

        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        Filer filer = processingEnv.getFiler();
        TypeSpec mapperInterface = TypeSpec.interfaceBuilder(name.getBaseRepositoryName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(AnnotationSpec.builder(Repository.class).build()) // 使用 @Repository 而非 @Mapper
                .addAnnotation(AnnotationSpec.builder(Primary.class).build())
                .addAnnotation(GeneratorUtil.buildGeneratedAnnotation())
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(JpaRepository.class),
                                ClassName.get(pkg.getEntity(), name.getEntityName()),
                                ClassName.get(Long.class)
                        )
                )
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(CrudRepository.class ),
                                ClassName.get(pkg.getEntity(), name.getEntityName()),
                                ClassName.get(Long.class)
                        )
                )
                .addSuperinterface(
                        ParameterizedTypeName.get(
                                ClassName.get(JpaSpecificationExecutor.class),
                                ClassName.get(pkg.getEntity(), name.getEntityName())
                        )
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
