package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.javapoet.*;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public class ServiceGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public ServiceGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }

    public void generateBaseService() throws IOException {
        if (!GeneratorUtil.checkGenerate(AutoEntity.GenerateFileType.Service, context)) {
            return;
        }
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        ClassName entityType = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName vmType = ClassName.get(pkg.getVm(), name.getVmName());
        ClassName bodyType = ClassName.get(pkg.getBody(), name.getBodyName());

        Filer filer = processingEnv.getFiler();
        TypeSpec serviceInterface = TypeSpec.interfaceBuilder(name.getBaseServiceName())
                .addAnnotation(GeneratorUtil.buildGeneratedAnnotation())
                .addModifiers(Modifier.PUBLIC)
                .addMethod(
                        MethodSpec.methodBuilder("save")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addParameter(ParameterSpec.builder(vmType, name.getVmVariable()).build())
                                .returns(TypeName.VOID)
                                .build()
                )
                .addMethod(
                        MethodSpec.methodBuilder("findById")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .addParameter(ParameterSpec.builder(TypeName.LONG, "id").build())
                                .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), vmType))
                                .build()
                )
                .addMethod(
                        MethodSpec.methodBuilder("findAll")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(ParameterizedTypeName.get(ClassName.get(Page.class), vmType))
                                .addParameter(ParameterSpec.builder(ClassName.get(Pageable.class), "pageable").build())
                                .build()
                )
                .addMethod(
                        MethodSpec.methodBuilder("findPage")
                                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(ParameterizedTypeName.get(ClassName.get(Page.class), vmType))
                                .addParameter(ParameterizedTypeName.get(ClassName.get(Specification.class), entityType), "specification")
                                .addParameter(ClassName.get(Pageable.class), "pageable")
                                .build()

                )
                .addMethod(
                        MethodSpec.methodBuilder("deleteById").addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                                .returns(TypeName.VOID)
                                .addParameter(ParameterSpec.builder(TypeName.LONG, "id").build())
                                .build()
                )
                .build();

        JavaFile javaFile = JavaFile.builder(pkg.getService(), serviceInterface)
                .build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getService() + "." + name.getBaseServiceName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }
    }
}
