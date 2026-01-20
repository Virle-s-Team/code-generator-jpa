package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.base.BaseAutoService;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import org.springframework.javapoet.ClassName;
import org.springframework.javapoet.JavaFile;
import org.springframework.javapoet.ParameterizedTypeName;
import org.springframework.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class ServiceImplGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public ServiceImplGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }

    public void generateBaseServiceImpl() throws IOException {
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        ClassName entityClass = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName reqClass = ClassName.get(pkg.getRequest(), name.getRequestName());
        ClassName vmClass = ClassName.get(pkg.getVm(), name.getVmName());
        ClassName idClass = ClassName.get(Long.class);

        Filer filer = processingEnv.getFiler();
        TypeSpec serviceImplClass = TypeSpec.classBuilder(name.getBaseServiceImplName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(org.springframework.stereotype.Service.class)
                .superclass(ParameterizedTypeName.get(
                        ClassName.get(BaseAutoService.class), entityClass, idClass, reqClass, vmClass))
                .build();

        JavaFile javaFile = JavaFile.builder(pkg.getServiceImpl(), serviceImplClass)
                .build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getServiceImpl() + "." + name.getBaseServiceImplName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }
    }
}
