package cool.auv.codegeneratorjpa.core.config;

import lombok.Data;


@Data
public class AutoProcessorConfiguration {

    private AutoProcessor autoProcessor;

    @Data
    public static class AutoProcessor {
        private EntityConfig entity;
        private VmConfig vm;
        private BodyConfig body;
        private RequestConfig request;
        private MapperConfig repository;
        private MapStructConfig mapstruct;
        private ServiceConfig service;
        private ControllerConfig controller;
    }

    @Data
    public static class EntityConfig {
        private boolean generate;
        private String packageName;
        private String extendFrom;

        public EntityConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class VmConfig {
        private boolean generate;
        private String packageName;

        public VmConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class BodyConfig {
        private boolean generate;
        private String packageName;
        public BodyConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class RequestConfig {
        private boolean generate;
        private String packageName;

        public RequestConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class MapperConfig {
        private boolean generate;
        private String packageName;

        public MapperConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class MapStructConfig {
        private boolean generate;
        private String packageName;

        public MapStructConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class ServiceConfig {
        private boolean generate;
        private String packageName;
        private String implPackageName;
        public ServiceConfig() {
            this.generate = true;
        }
    }

    @Data
    public static class ControllerConfig {
        private boolean generate;
        private String packageName;
        public ControllerConfig() {
            this.generate = true;
        }
    }

}
