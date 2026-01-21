package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.base.BaseAutoMapstruct;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
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
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        Filer filer = processingEnv.getFiler();
        ClassName entityClass = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName vmClass = ClassName.get(pkg.getVm(), name.getVmName());
        TypeSpec mapstruct = TypeSpec.classBuilder(name.getBaseMapstructName()) // 这里建议去掉 Base 前缀，直接叫 SysRoleMapper
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addAnnotation(AnnotationSpec.builder(ClassName.get("org.mapstruct", "Mapper"))
                        .addMember("componentModel", "$S", "spring")
                        .addMember("collectionMappingStrategy", "$T.$L",
                                ClassName.get("org.mapstruct", "CollectionMappingStrategy"), "TARGET_IMMUTABLE")
                        .build())
                .addSuperinterface(ParameterizedTypeName.get(ClassName.get(BaseAutoMapstruct.class), entityClass, vmClass))
                .build();

        JavaFile javaFile = JavaFile.builder(pkg.getMapstruct(), mapstruct).build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getMapstruct() + "." + name.getBaseMapstructName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }

    }
}
