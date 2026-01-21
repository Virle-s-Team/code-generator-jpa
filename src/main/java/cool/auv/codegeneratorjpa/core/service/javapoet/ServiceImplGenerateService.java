package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.base.AbstractAutoService;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.javapoet.*;

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

        // 避让注解
        ClassName serviceBase = ClassName.get(pkg.getService(), name.getBaseServiceName());

        AnnotationSpec conditionalAnno = AnnotationSpec.builder(ConditionalOnMissingBean.class)
                .addMember("value", "$T.class", serviceBase)
                // 关键：告诉 Spring，我们要比对的是这个基类所携带的泛型参数
                .build();

        Filer filer = processingEnv.getFiler();
        TypeSpec serviceImplClass = TypeSpec.classBuilder(name.getBaseServiceImplName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(org.springframework.stereotype.Service.class)
                .addAnnotation(conditionalAnno)
                .superclass(ParameterizedTypeName.get(
                        ClassName.get(AbstractAutoService.class), entityClass, idClass, reqClass, vmClass))
                .addSuperinterface(ClassName.get(pkg.getService(), name.getBaseServiceName()))
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
