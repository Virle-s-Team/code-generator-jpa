package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import cool.auv.codegeneratorjpa.core.utils.PaginationUtil;
import cool.auv.codegeneratorjpa.core.utils.ResponseUtil;
import cool.auv.codegeneratorjpa.core.vm.PageSortRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.javapoet.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ControllerGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public ControllerGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }


    public void generateBaseController() throws IOException {
        Filer filer = processingEnv.getFiler();
        if (!GeneratorUtil.checkGenerate(AutoEntity.GenerateFileType.Controller, context)) {
            return;
        }
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        String basePath = context.getAnnotations().autoEntity().basePath();

        AutoEntity.ControllerExclude[] controllerExcludes = context.getAnnotations().autoEntity().controllerExclude();

        TypeSpec.Builder controllerBuilder = TypeSpec.classBuilder(name.getBaseControllerName())
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(RestController.class)
                .addField(
                        FieldSpec.builder(ClassName.get(pkg.getService(), name.getBaseServiceName()), name.getBaseServiceVariable())
                                .addModifiers(Modifier.PROTECTED)
                                .addAnnotation(Autowired.class)
                                .build()
                );
        if (Arrays.stream(controllerExcludes).noneMatch(exclude-> exclude == AutoEntity.ControllerExclude.findById)) {
            MethodSpec findById = MethodSpec.methodBuilder("findById")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(GetMapping.class)
                            .addMember("value", "$S", basePath + "/{id}")
                            .build())
                    .addParameter(
                            ParameterSpec.builder(TypeName.LONG, "id")
                                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                                            .addMember("name", "$S", "id")
                                            .build())
                                    .build()
                    )
                    .returns(ParameterizedTypeName.get(ClassName.get(ResponseEntity.class ), ClassName.get(pkg.getVm(), name.getVmName())))
                    .addStatement("$T<$T> vm = $N.findById(id)", Optional.class, ClassName.get(pkg.getVm(), name.getVmName()), name.getBaseServiceVariable())
                    .addStatement("return $T.wrapOrNotFound(vm)", ClassName.get(ResponseUtil.class))
                    .build();
            controllerBuilder.addMethod(findById);
        }

        if (Arrays.stream(controllerExcludes).noneMatch(exclude-> exclude == AutoEntity.ControllerExclude.findByPage)) {
            MethodSpec findByPage = MethodSpec.methodBuilder("page")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(GetMapping.class)
                            .addMember("value", "$S", basePath + "/findByPage")
                            .build())
                    .addParameter(ParameterSpec.builder(ClassName.get(pkg.getRequest(), name.getRequestName()), name.getRequestVariable()).build())
                    .addParameter(ParameterSpec.builder(ClassName.get(PageSortRequest.class), "pageSortRequest").build())
                    .returns(ParameterizedTypeName.get(ClassName.get(ResponseEntity.class), ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(pkg.getVm(), name.getVmName()))))
                    .addStatement("$T<$T> page = $N.findPage($N.buildSpecification(), pageSortRequest.pageableWithSort())",
                            ClassName.get(Page.class), ClassName.get(pkg.getVm(), name.getVmName()), name.getBaseServiceVariable(), name.getRequestVariable())
                    .addStatement("$T headers = $T.generatePaginationHttpHeaders($T.fromCurrentRequest(), page)",
                            HttpHeaders.class, PaginationUtil.class, ServletUriComponentsBuilder.class)
                    .addStatement("return $T.ok().headers(headers).body(page.getContent())", ResponseEntity.class)
                    .build();
            controllerBuilder.addMethod(findByPage);
        }

        if (Arrays.stream(controllerExcludes).noneMatch(exclude-> exclude == AutoEntity.ControllerExclude.save)) {
            MethodSpec save = MethodSpec.methodBuilder("save")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(PostMapping.class)
                            .addMember("value", "$S", basePath)
                            .build())
                    .addParameter(ParameterSpec.builder(ClassName.get(pkg.getVm(), name.getVmName()), name.getVmVariable()).addAnnotation(RequestBody.class).build())
                    .returns(ParameterizedTypeName.get(ResponseEntity.class, Void.class))
                    .addStatement("$N.save($N)", name.getBaseServiceVariable(), name.getVmVariable())
                    .addStatement("return $T.ok().build()", ResponseEntity.class)
                    .build();

            controllerBuilder.addMethod(save);
        }
        if (Arrays.stream(controllerExcludes).noneMatch(exclude-> exclude == AutoEntity.ControllerExclude.deleteById)) {
            MethodSpec deleteById = MethodSpec.methodBuilder("deleteById")
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(AnnotationSpec.builder(DeleteMapping.class)
                            .addMember("value", "$S", basePath + "/{id}")
                            .build())
                    .addParameter(
                            ParameterSpec.builder(TypeName.LONG, "id")
                                    .addAnnotation(AnnotationSpec.builder(PathVariable.class)
                                            .addMember("name", "$S", "id")
                                            .build())
                                    .build()
                    )
                    .returns(ParameterizedTypeName.get(ResponseEntity.class, Void.class))
                    .addStatement("$N.deleteById(id)", name.getBaseServiceVariable())
                    .addStatement("return $T.ok().build()", ResponseEntity.class)
                    .build();
            controllerBuilder.addMethod(deleteById);
        }

        JavaFile javaFile = JavaFile.builder(pkg.getController(), controllerBuilder.build()).build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getController() + "." + name.getBaseControllerName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }
    }
}
