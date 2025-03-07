package cool.auv.codegeneratorjpa.core.service.javapoet;

import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
import cool.auv.codegeneratorjpa.core.utils.GeneratorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.javapoet.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.Optional;

public class ServiceImplGenerateService {

    private final ProcessingEnvironment processingEnv;

    private final GeneratorContext context;

    public ServiceImplGenerateService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.processingEnv = processingEnv;
        this.context = context;
    }

    public void generateBaseServiceImpl() throws IOException {
        if (!GeneratorUtil.checkGenerate(AutoEntity.GenerateFileType.ServiceImpl, context)) {
            return;
        }
        GeneratorParameter.Name name = context.getName();
        GeneratorParameter.Package pkg = context.getPkg();
        ClassName superInterfaceType = ClassName.get(pkg.getService(), name.getBaseServiceName());
        ClassName entityType = ClassName.get(pkg.getEntity(), name.getEntityName());
        ClassName vmType = ClassName.get(pkg.getVm(), name.getVmName());

        String baseRepositoryVariable = name.getBaseRepositoryVariable();
        String baseMapstructVariable = name.getBaseMapstructVariable();
        Filer filer = processingEnv.getFiler();
        TypeSpec serviceImplClass = TypeSpec.classBuilder(name.getBaseServiceImplName())
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(superInterfaceType)
                .addAnnotation(Service.class)
                .addAnnotation(Primary.class)
                .addAnnotation(GeneratorUtil.buildGeneratedAnnotation())
                .addField(
                        FieldSpec.builder(ClassName.get(pkg.getRepository(), name.getBaseRepositoryName()), baseRepositoryVariable)
                                .addModifiers(Modifier.PRIVATE)
                                .addAnnotation(Autowired.class)
                                .build()
                )
                .addField(
                        FieldSpec.builder(ClassName.get(pkg.getMapstruct(), name.getBaseMapstructName()), baseMapstructVariable)
                        .addModifiers(Modifier.PRIVATE)
                        .addAnnotation(Autowired.class)
                        .build())
                .addMethod(
                        MethodSpec.methodBuilder("save")
                                .addModifiers(Modifier.PUBLIC)
                                .addAnnotation(Transactional.class)
                                .addParameter(ParameterSpec.builder(vmType, name.getVmVariable()).build())
                                .returns(TypeName.VOID)
                                .beginControlFlow("if ($N.getId() == null)", name.getVmVariable())
                                .addStatement("$T $N = $N.vmToEntity($N)", entityType, name.getEntityVariable(), baseMapstructVariable, name.getVmVariable())
                                .addStatement("$N.save($N)", baseRepositoryVariable, name.getEntityVariable())
                                .nextControlFlow("else")
                                .addStatement("$N.findById($N.getId()).ifPresent($N -> $N.updateEntityFromVM($N, $N))",
                                        baseRepositoryVariable, name.getVmVariable(), name.getEntityVariable(), baseMapstructVariable, name.getVmVariable(), name.getEntityVariable())
                                .endControlFlow()
                                .build()
                )
                .addMethod(
                        MethodSpec.methodBuilder("findById")
                                .addModifiers(Modifier.PUBLIC)
                                .addParameter(ParameterSpec.builder(TypeName.LONG, "id").build())
                                .returns(ParameterizedTypeName.get(ClassName.get(Optional.class), vmType))
                                .addStatement("return $N.findById($N).map($N::entityToVm)", baseRepositoryVariable, "id", baseMapstructVariable)
                                .build()
                )
                .addMethod(
                        MethodSpec.methodBuilder("findAll")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(ParameterizedTypeName.get(ClassName.get(Page.class), vmType))
                                .addParameter(ParameterSpec.builder(ClassName.get(Pageable.class), "pageable").build())
                                .addStatement("return $N.findAll($N).map($N::entityToVm)", baseRepositoryVariable, "pageable", baseMapstructVariable)
                                .build()
                )
                .addMethod(
                        MethodSpec.methodBuilder("findPage")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(ParameterizedTypeName.get(ClassName.get(Page.class), vmType))
                                .addParameter(ParameterizedTypeName.get(ClassName.get(Specification.class), entityType), "specification")
                                .addParameter(ClassName.get(Pageable.class), "pageable")
                                .addStatement("return $N.findAll($N, $N).map($N::entityToVm)",
                                        baseRepositoryVariable, "specification", "pageable", baseMapstructVariable)
                                .build()

                )
                .addMethod(
                        MethodSpec.methodBuilder("deleteById")
                                .addModifiers(Modifier.PUBLIC)
                                .returns(TypeName.VOID)
                                .addParameter(ParameterSpec.builder(TypeName.LONG, "id").build())
                                .addStatement("$N.deleteById($N)", baseRepositoryVariable, "id")
                                .build()
                )
                .build();

        JavaFile javaFile = JavaFile.builder(pkg.getService(), serviceImplClass)
                .build();
        String generatedCode = javaFile.toString();
        JavaFileObject sourceFile = filer.createSourceFile(pkg.getServiceImpl() + "." + name.getBaseServiceImplName());
        try ( Writer write = sourceFile.openWriter()) {
            write.write(generatedCode);
        }
    }
}
