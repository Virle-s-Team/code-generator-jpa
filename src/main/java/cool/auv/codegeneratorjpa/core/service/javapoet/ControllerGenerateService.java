package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.base.AbstractCrudController;
import cool.auv.codegeneratorjpa.core.base.CustomControllerMarker;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.javapoet.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;

public class ControllerGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public ControllerGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }


    public void generateBaseController() throws IOException {
        Filer filer = processingEnv.getFiler();
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        String basePath = context.getAnnotations().autoEntity().basePath();
        ClassName baseControllerClassName = ClassName.get(AbstractCrudController.class);
        ClassName serviceClass = ClassName.get(pkg.getService(), name.getBaseServiceName());
        ClassName entityClass = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName reqClass = ClassName.get(pkg.getRequest(), name.getRequestName());
        ClassName vmClass = ClassName.get(pkg.getVm(), name.getVmName());
        ClassName idClass = ClassName.get(Long.class);

        ClassName customControllerMarker = ClassName.get(CustomControllerMarker.class);

        TypeName superclass = ParameterizedTypeName.get(baseControllerClassName, entityClass, idClass, reqClass, vmClass, serviceClass);
        String docTag = context.getAnnotations().autoEntity().docTag();
        if (StringUtils.isEmpty(docTag)) {
            docTag = String.format("%s controller", name.getEntityName());
        }
        TypeSpec.Builder controllerBuilder = TypeSpec.classBuilder(name.getBaseControllerName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(RestController.class)
                .addAnnotation(AnnotationSpec.builder(RequestMapping.class).addMember("value", "$S", basePath).build())
                .addAnnotation(AnnotationSpec.builder(Tag.class).addMember("name", "$S", docTag).build())
                .addAnnotation(AnnotationSpec.builder(ConditionalOnMissingBean.class)
                        .addMember("value", "$T.class", entityClass)
                        .addMember("parameterizedContainer", "$T.class", customControllerMarker)
                        .build())
                .superclass(superclass);

        JavaFile javaFile = JavaFile.builder(pkg.getController(), controllerBuilder.build()).build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getController() + "." + name.getBaseControllerName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }
    }
}
