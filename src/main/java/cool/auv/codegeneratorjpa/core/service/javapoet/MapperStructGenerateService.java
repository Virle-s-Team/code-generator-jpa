package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.mapstruct.BaseMapstruct;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import org.mapstruct.Mapper;
import org.springframework.javapoet.*;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class MapperStructGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public MapperStructGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }

    public void generateBaseMapstruct() throws IOException {

        if (!GeneratorUtil.checkGenerate(AutoEntity.GenerateFileType.MapperStruct, context)) {
            return;
        }
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        Filer filer = processingEnv.getFiler();

        TypeSpec mapstruct = TypeSpec.classBuilder(name.getBaseMapstructName())
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.ABSTRACT)
                .addAnnotation(
                        AnnotationSpec.builder(Mapper.class)
                                .addMember("componentModel", "$S", "spring")
                                .addMember("uses", "$L","{}")
                                .build()
                )
                .addAnnotation(GeneratorUtil.buildGeneratedAnnotation())
                .superclass(ParameterizedTypeName.get(ClassName.get(BaseMapstruct.class),
                        ClassName.get(pkg.getEntity(), name.getEntityName()),
                        ClassName.get(pkg.getVm(), name.getVmName())
                ))
                .build();

        JavaFile javaFile = JavaFile.builder(pkg.getMapstruct(), mapstruct).build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getMapstruct() + "." + name.getBaseMapstructName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }

    }
}
