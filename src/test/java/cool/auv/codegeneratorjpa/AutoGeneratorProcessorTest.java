package cool.auv.codegeneratorjpa;

import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubject;
import cool.auv.codegeneratorjpa.core.processors.AutoGeneratorProcessor;

import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

public class AutoGeneratorProcessorTest {

    @Test
    void processor_shouldGenerateAllFiles_forSimpleEntity() {
        // All the input source files for the test
        JavaFileObject entity = JavaFileObjects.forResource("cool/auv/gen/entity/TestEntity.java");
        JavaFileObject vm = JavaFileObjects.forResource("cool/auv/gen/vm/TestEntityVM.java");
        JavaFileObject request = JavaFileObjects.forResource("cool/auv/gen/request/TestEntityRequest.java");

        JavaSourcesSubject.assertThat(entity, vm, request) // Pass all inputs
                .processedWith(new AutoGeneratorProcessor())
                .compilesWithoutError()
                .and()
                .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "cool.auv.gen.repository", "BaseTestEntityRepository.java")
                .and()
                .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "cool.auv.gen.service", "BaseTestEntityService.java")
                .and()
                .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "cool.auv.gen.service.impl", "BaseTestEntityServiceImpl.java")
                .and()
                .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "cool.auv.gen.mapstruct", "BaseTestEntityMapstruct.java")
                .and()
                .generatesFileNamed(StandardLocation.SOURCE_OUTPUT, "cool.auv.gen.controller", "BaseTestEntityController.java");
    }
}
