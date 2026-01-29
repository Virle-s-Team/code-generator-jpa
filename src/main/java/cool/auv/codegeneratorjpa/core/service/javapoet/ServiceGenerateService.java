package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.base.BaseAutoService;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import org.springframework.javapoet.*;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class ServiceGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public ServiceGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }

    public void generateBaseService() throws IOException {
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        ClassName entityClass = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName vmClass = ClassName.get(pkg.getVm(), name.getVmName());
        ClassName reqClass = ClassName.get(pkg.getRequest(), name.getRequestName());
        TypeName idClass = context.getIdTypeName();
        ParameterizedTypeName superClass = ParameterizedTypeName.get(
                ClassName.get(BaseAutoService.class), entityClass, idClass, reqClass, vmClass);
        Filer filer = processingEnv.getFiler();
        TypeSpec serviceInterface = TypeSpec.interfaceBuilder(name.getBaseServiceName())
                .addAnnotation(GeneratorUtil.buildGeneratedAnnotation())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(superClass)
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
