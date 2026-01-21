package cool.auv.codegeneratorjpa.core.base;

import org.mapstruct.MappingTarget;

import java.util.List;

public interface BaseAutoMapstruct<ENTITY, VM> {

    VM entityToVM(ENTITY entity);

    ENTITY vmToEntity(VM vm);

    void updateEntityFromVM(VM vm, @MappingTarget ENTITY entity);

    List<VM> entityToVM(List<ENTITY> entity);

}
