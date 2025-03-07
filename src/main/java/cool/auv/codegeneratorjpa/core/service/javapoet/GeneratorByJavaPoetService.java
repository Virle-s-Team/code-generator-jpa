package cool.auv.codegeneratorjpa.core.service.javapoet;


import cool.auv.codegeneratorjpa.core.entity.GeneratorContext;

import javax.annotation.processing.ProcessingEnvironment;
import java.io.IOException;

public class GeneratorByJavaPoetService {


    private final RepositoryGenerateService repositoryGenerateService;

    private final ControllerGenerateService controllerGenerateService;

    private final ServiceGenerateService serviceGenerateService;

    private final ServiceImplGenerateService serviceImplGenerateService;

    private final MapperStructGenerateService mapperStructGenerateService;

    public GeneratorByJavaPoetService(ProcessingEnvironment processingEnv, GeneratorContext context) {
        this.repositoryGenerateService = new RepositoryGenerateService(processingEnv, context);
        this.controllerGenerateService = new ControllerGenerateService(processingEnv, context);
        this.serviceGenerateService = new ServiceGenerateService(processingEnv, context);
        this.serviceImplGenerateService = new ServiceImplGenerateService(processingEnv, context);
        this.mapperStructGenerateService = new MapperStructGenerateService(processingEnv, context);
    }


    public void generateBaseController() throws IOException {
        controllerGenerateService.generateBaseController();
    }


    public void generateBaseService() throws IOException {
        serviceGenerateService.generateBaseService();
    }

    public void generateBaseServiceImpl() throws IOException {
        serviceImplGenerateService.generateBaseServiceImpl();
    }


    public void generateBaseMapstruct() throws IOException {
        mapperStructGenerateService.generateBaseMapstruct();
    }

    public void generateBaseMapper() throws IOException {
        repositoryGenerateService.generateBaseMapper();
    }

}
