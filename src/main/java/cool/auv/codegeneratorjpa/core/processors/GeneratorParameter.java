package cool.auv.codegeneratorjpa.core.processors;

import cool.auv.codegeneratorjpa.core.config.AutoProcessorConfiguration;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class GeneratorParameter {

    @Data
    public static class Name {

        private String entityName;
        private String entityVariable;
        private String baseEntityName;
        private String baseEntityVariable;

        private String repositoryName;
        private String repositoryVariable;
        private String baseRepositoryName;
        private String baseRepositoryVariable;

        private String serviceName;
        private String serviceVariable;
        private String baseServiceName;
        private String baseServiceVariable;

        private String serviceImplName;
        private String serviceImplVariable;
        private String baseServiceImplName;
        private String baseServiceImplVariable;

        private String controllerName;
        private String controllerVariable;
        private String baseControllerName;
        private String baseControllerVariable;

        private String bodyName;
        private String bodyVariable;
        private String baseBodyVariable;
        private String baseBodyName;

        private String vmName;
        private String vmVariable;
        private String baseVmName;
        private String baseVmVariable;

        private String requestName;
        private String requestVariable;
        private String baseRequestName;
        private String baseRequestVariable;

        private String mapstructName;
        private String mapstructVariable;
        private String baseMapstructName;
        private String baseMapstructVariable;

        public Name(String entityClassName) {

            this.entityName = capitalizeFirstLetter(entityClassName);
            this.entityVariable = lowerFirstLetter(entityClassName);
            this.baseEntityName = generateBaseEntityName(entityClassName);
            this.baseEntityVariable = generateBaseEntityVariable(entityClassName);

            this.repositoryName = generateRepositoryName(entityClassName);
            this.repositoryVariable = generateRepositoryVariable(entityClassName);
            this.baseRepositoryName = generateBaseRepositoryName(entityClassName);
            this.baseRepositoryVariable = generateBaseRepositoryVariable(entityClassName);

            this.serviceName = generateServiceName(entityClassName);
            this.serviceVariable = generateServiceVariable(entityClassName);
            this.baseServiceName = generateBaseServiceName(entityClassName);
            this.baseServiceVariable = generateBaseServiceVariable(entityClassName);

            this.serviceImplName = generateServiceImplName(entityClassName);
            this.serviceImplVariable = generateServiceImplVariable(entityClassName);
            this.baseServiceImplName = generateBaseServiceImplName(entityClassName);
            this.baseServiceImplVariable = generateBaseServiceImplVariable(entityClassName);

            this.controllerName = generateControllerName(entityClassName);
            this.controllerVariable = generateControllerVariable(entityClassName);
            this.baseControllerName = generateBaseControllerName(entityClassName);
            this.baseControllerVariable = generateBaseControllerVariable(entityClassName);

            this.bodyName = generateBodyName(entityClassName);
            this.bodyVariable = generateBodyVariable(entityClassName);
            this.baseBodyName = generateBaseBodyName(entityClassName);
            this.baseBodyVariable = generateBaseBodyVariable(entityClassName);

            this.vmName = generateVmName(entityClassName);
            this.vmVariable = generateVmVariable(entityClassName);
            this.baseVmName = generateBaseVmName(entityClassName);
            this.baseVmVariable = generateBaseVmVariable(entityClassName);

            this.requestName = generateRequestName(entityClassName);
            this.requestVariable = generateRequestVariable(entityClassName);
            this.baseRequestName = generateBaseRequestName(entityClassName);
            this.baseRequestVariable = generateBaseRequestVariable(entityClassName);

            this.mapstructName = generateMapstructName(entityClassName);
            this.mapstructVariable = generateMapstructVariable(entityClassName);
            this.baseMapstructName = generateBaseMapstructName(entityClassName);
            this.baseMapstructVariable = generateBaseMapstructVariable(entityClassName);
        }

        public static String capitalizeFirstLetter(String input) {
            return input.substring(0, 1).toUpperCase() + input.substring(1);
        }

        public static String lowerFirstLetter(String input) {
            return input.substring(0, 1).toLowerCase() + input.substring(1);
        }

        public static String generateBaseEntityName(String entityName) {
            return "Base" +capitalizeFirstLetter(entityName);
        }

        public static String generateBaseEntityVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName);
        }

        public static String generateServiceName(String entityName) {
            return capitalizeFirstLetter(entityName) + "Service";
        }

        public static String generateServiceVariable(String entityName) {
            return lowerFirstLetter(entityName) + "Service";
        }
        public static String generateBaseServiceName(String entityName) {
            return "Base" +capitalizeFirstLetter(entityName) + "Service";
        }

        public static String generateBaseServiceVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "Service";
        }

        public static String generateControllerName(String entityName) {
            return capitalizeFirstLetter(entityName) + "Controller";
        }

        public static String generateControllerVariable(String entityName) {
            return lowerFirstLetter(entityName) + "Controller";
        }

        public static String generateBaseControllerName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "Controller";
        }

        public static String generateBaseControllerVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "Controller";
        }

        public static String generateRepositoryName(String entityName) {
            return capitalizeFirstLetter(entityName) + "Repository";
        }

        public static String generateRepositoryVariable(String entityName) {
            return lowerFirstLetter(entityName) + "Repository";
        }

        public static String generateBaseRepositoryName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "Repository";
        }

        public static String generateBaseRepositoryVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "Repository";
        }

        public static String generateBodyName(String entityName) {
            return capitalizeFirstLetter(entityName) + "Body";
        }

        public static String generateBodyVariable(String entityName) {
            return lowerFirstLetter(entityName) + "Body";
        }

        public static String generateBaseBodyName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "Body";
        }

        public static String generateBaseBodyVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "Body";
        }

        public static String generateVmName(String entityName) {
            return capitalizeFirstLetter(entityName) + "VM";
        }

        public static String generateVmVariable(String entityName) {
            return lowerFirstLetter(entityName) + "VM";
        }

        public static String generateBaseVmName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "VM";
        }

        public static String generateBaseVmVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "VM";
        }

        public static String generateRequestName(String entityName) {
            return capitalizeFirstLetter(entityName) + "Request";
        }

        public static String generateRequestVariable(String entityName) {
            return lowerFirstLetter(entityName) + "Request";
        }

        public static String generateBaseRequestName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "Request";
        }

        public static String generateBaseRequestVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "Request";
        }

        public static String generateServiceImplName(String entityName) {
            return capitalizeFirstLetter(entityName) + "ServiceImpl";
        }

        public static String generateServiceImplVariable(String entityName) {
            return lowerFirstLetter(entityName) + "ServiceImpl";
        }

        public static String generateBaseServiceImplName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "ServiceImpl";
        }

        public static String generateBaseServiceImplVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "ServiceImpl";
        }

        public static String generateMapstructName(String entityName) {
            return capitalizeFirstLetter(entityName) + "Mapstruct";
        }

        public static String generateMapstructVariable(String entityName) {
            return lowerFirstLetter(entityName) + "Mapstruct";
        }

        public static String generateBaseMapstructName(String entityName) {
            return "Base" + capitalizeFirstLetter(entityName) + "Mapstruct";
        }

        public static String generateBaseMapstructVariable(String entityName) {
            return "base" + capitalizeFirstLetter(entityName) + "Mapstruct";
        }

    }

    @Data
    public static class Package {
        private String entity;
        private String controller;
        private String service;
        private String serviceImpl;
        private String repository;
        private String mapstruct;
        private String vm;
        private String body;
        private String request;

        public Package(AutoProcessorConfiguration config) {
            this.entity = "cool.auv.gen.entity";
            this.controller = "cool.auv.gen.controller";
            this.service = "cool.auv.gen.service";
            this.serviceImpl = "cool.auv.gen.service.impl";
            this.repository = "cool.auv.gen.repository";
            this.mapstruct = "cool.auv.gen.mapstruct";
            this.vm = "cool.auv.gen.vm";
            this.body = "cool.auv.gen.body";
            this.request = "cool.auv.gen.request";

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getEntity().getPackageName())) {
                this.entity = config.getAutoProcessor().getEntity().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getController().getPackageName())) {
                this.controller = config.getAutoProcessor().getController().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getService().getPackageName())) {
                this.service = config.getAutoProcessor().getService().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getService().getImplPackageName())) {
                this.serviceImpl = config.getAutoProcessor().getService().getImplPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getRepository().getPackageName())) {
                this.repository = config.getAutoProcessor().getRepository().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getMapstruct().getPackageName())) {
                this.mapstruct = config.getAutoProcessor().getMapstruct().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getRequest().getPackageName())) {
                this.request = config.getAutoProcessor().getRequest().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getVm().getPackageName())) {
                this.vm = config.getAutoProcessor().getVm().getPackageName();
            }

            if (StringUtils.isNotEmpty(config.getAutoProcessor().getBody().getPackageName())) {
                this.body = config.getAutoProcessor().getBody().getPackageName();
            }

        }



    }


}
