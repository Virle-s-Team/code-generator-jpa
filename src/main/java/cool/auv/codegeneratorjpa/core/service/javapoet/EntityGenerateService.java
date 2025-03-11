//package cool.auv.codegeneratorjpa.core.service.javapoet;
//
//
//import cool.auv.codegeneratorjpa.core.annotation.AutoEntity;
//import cool.auv.codegeneratorjpa.core.annotation.Exclude;
//import cool.auv.codegeneratorjpa.core.config.AutoProcessorConfiguration;
//import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;
//import cool.auv.codegeneratorjpa.core.processors.GeneratorParameter;
//import cool.auv.codegeneratorjpa.core.utils.ContextConfigUtil;
//import jakarta.persistence.criteria.Predicate;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.experimental.Accessors;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.config.BeanDefinition;
//import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
//import org.springframework.core.type.filter.AnnotationTypeFilter;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.javapoet.*;
//
//import javax.annotation.processing.Filer;
//import javax.lang.model.element.*;
//import java.io.IOException;
//import java.lang.reflect.Field;
//import java.nio.file.Paths;
//import java.util.*;
//
//public class EntityGenerateService {
//
//
//    private AutoProcessorConfiguration configuration;
//
//    private GeneratorContext context;
//
//    public EntityGenerateService(ContextConfigUtil contextConfigUtil) {
//        configuration = contextConfigUtil.loadConfig();
//    }
//
//
//    public void generator() {
//        String entityPackage = configuration.getAutoProcessor().getEntity().getPackageName();
//        ClassPathScanningCandidateComponentProvider scanner =
//                new ClassPathScanningCandidateComponentProvider(false);
//
//        // 添加注解过滤器
//        scanner.addIncludeFilter(new AnnotationTypeFilter(AutoEntity.class));
//
//        // 扫描包
//        Set<BeanDefinition> candidates = scanner.findCandidateComponents(entityPackage);
//        candidates.forEach(candidate -> {
//            try {
//                Class<?> clazz = Class.forName(candidate.getBeanClassName());
//                String entityClassName = clazz.getSimpleName();
//                context = GeneratorContext.from(clazz, configuration);
//
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        });
//
//
//    }
//    public void generateVM() throws IOException {
//        GeneratorParameter.Package pkg = context.getPkg();
//        GeneratorParameter.Name name = context.getName();
//        String packageName = pkg.getVm();
//        Class<?> element = context.getElement();
//
//        Field[] fields = element.getDeclaredFields();
//        List<FieldSpec> fieldSpecList = new ArrayList<>();
//        for (Field field : fields) {
//
//        }
//        for (Element enclosedElement : enclosedElements) {
//            if (enclosedElement.getKind() == ElementKind.FIELD) {
//                VariableElement field = (VariableElement) enclosedElement;
//                // 检查是否有 @Exclude 注解
//                Exclude exclude = field.getAnnotation(Exclude.class);
//                if (exclude != null) {
//                    Exclude.ExcludeFile[] excludeFiles = exclude.excludeFile();
//                    if (!checkGenerate(Exclude.ExcludeFile.VM, excludeFiles)) {
//                        generateField(field, fieldSpecList);
//                    }
//                } else {
//                    generateField(field, fieldSpecList);
//                }
//
//            }
//        }
//
//        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
//                .addModifiers(Modifier.PUBLIC)
//                .addAnnotation(Getter.class)
//                .addAnnotation(Setter.class)
//                .addAnnotation(AnnotationSpec.builder(Accessors.class).addMember("chain", "$L", "true").build());
//        typeBuilder.addFields(fieldSpecList);
//
//        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).build();
//        javaFile.writeTo(Paths.get("src/main/java"));
//    }
//
//    private static void generateField(VariableElement field, List<FieldSpec> fieldSpecList) {
//        FieldSpec fieldSpec = FieldSpec.builder(
//                TypeName.get(field.asType()), // 获取字段类型
//                field.getSimpleName().toString(), // 获取字段名
//                Modifier.PRIVATE // 根据需要设置修饰符
//        ).build();
//        fieldSpecList.add(fieldSpec);
//    }
//
//    public void generateBody() throws IOException {
//        GeneratorParameter.Package pkg = context.getPkg();
//        GeneratorParameter.Name name = context.getName();
//        String packageName = pkg.getBody();
//        String className = name.getBodyName();
//        TypeElement typeElement = context.getTypeElement();
//
//        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
//        List<FieldSpec> fieldSpecList = new ArrayList<>();
//        for (Element enclosedElement : enclosedElements) {
//            if (enclosedElement.getKind() == ElementKind.FIELD) {
//                VariableElement field = (VariableElement) enclosedElement;
//                // 检查是否有 @Exclude 注解
//                Exclude exclude = field.getAnnotation(Exclude.class);
//                if (exclude != null) {
//                    Exclude.ExcludeFile[] excludeFiles = exclude.excludeFile();
//                    if (!checkGenerate(Exclude.ExcludeFile.VM, excludeFiles)) {
//                        generateField(field, fieldSpecList);
//                    }
//                } else {
//                    generateField(field, fieldSpecList);
//                }
//
//            }
//        }
//
//        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
//                .addModifiers(Modifier.PUBLIC)
//                .addAnnotation(Getter.class)
//                .addAnnotation(Setter.class)
//                .addAnnotation(AnnotationSpec.builder(Accessors.class).addMember("chain", "$L", "true").build());
//        typeBuilder.addFields(fieldSpecList);
////        String packagePath = packageName.replace('.', '/'); // 将包名转换为路径
////        Path targetPath = Paths.get("src/main/java", packagePath, className + ".java");
////        if (Files.notExists(targetPath)) {
////            JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).build();
////            javaFile.writeTo(Paths.get("src/main/java"));
////        }
//
//
//        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).build();
//        javaFile.writeTo(Paths.get("src/main/java"));
//    }
//
//    public void generateRequest() throws IOException {
//        GeneratorParameter.Package pkg = context.getPkg();
//        GeneratorParameter.Name name = context.getName();
//        String packageName = pkg.getRequest();
//        String className = name.getRequestName();
//        TypeElement typeElement = context.getTypeElement();
//
//        List<? extends Element> enclosedElements = typeElement.getEnclosedElements();
//        List<FieldSpec> fieldSpecList = new ArrayList<>();
//        for (Element enclosedElement : enclosedElements) {
//            if (enclosedElement.getKind() == ElementKind.FIELD) {
//                VariableElement field = (VariableElement) enclosedElement;
//                // 检查是否有 @Exclude 注解
//                Exclude exclude = field.getAnnotation(Exclude.class);
//                if (exclude != null) {
//                    Exclude.ExcludeFile[] excludeFiles = exclude.excludeFile();
//                    if (!checkGenerate(Exclude.ExcludeFile.VM, excludeFiles)) {
//                        generateField(field, fieldSpecList);
//                    }
//                } else {
//                    generateField(field, fieldSpecList);
//                }
//
//            }
//        }
//
//        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(className)
//                .addModifiers(Modifier.PUBLIC)
//                .addAnnotation(Getter.class)
//                .addAnnotation(Setter.class)
//                .addAnnotation(AnnotationSpec.builder(Accessors.class).addMember("chain", "$L", "true").build());
//        typeBuilder.addFields(fieldSpecList);
////        String packagePath = packageName.replace('.', '/'); // 将包名转换为路径
////        Path targetPath = Paths.get("src/main/java", packagePath, className + ".java");
////        if (Files.notExists(targetPath)) {
////            JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).build();
////            javaFile.writeTo(Paths.get("src/main/java"));
////        }
//        typeBuilder.addMethod(
//                MethodSpec.methodBuilder("buildSpecification")
//                        .addModifiers(Modifier.PUBLIC)
//                        .returns(ParameterizedTypeName.get(ClassName.get(Specification.class), ClassName.get(pkg.getEntity(), name.getEntityName())))
//                        .addStatement("""
//                                return (root, query, cb) -> {
//                                     $T predicates = new $T<>();
//
//                                     return cb.and(predicates.toArray(new $T[0]));
//                                }""",
//                                ParameterizedTypeName.get(ClassName.get(List.class), ClassName.get(Predicate.class)), ArrayList.class, ClassName.get(Predicate.class))
//                        .build()
//        );
//
//
//        JavaFile javaFile = JavaFile.builder(packageName, typeBuilder.build()).build();
//        javaFile.writeTo(Paths.get("src/main/java"));
//    }
//
//    public boolean checkGenerate(Exclude.ExcludeFile file, Exclude.ExcludeFile[] excludeFiles) {
//        Set<Exclude.ExcludeFile> MAPPER_GENERATION_TYPES =
//                EnumSet.of(file, Exclude.ExcludeFile.ALL);
//
//        return Arrays.stream(excludeFiles).anyMatch(MAPPER_GENERATION_TYPES::contains);
//    }
//}
