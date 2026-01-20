package cool.auv.codegeneratorjpa.core.base;

import org.mapstruct.MappingTarget;

import java.util.List;

public abstract class BaseAutoMapstruct<ENTITY, VM> {

    public abstract VM entityToVM(ENTITY entity);

    public abstract ENTITY vmToEntity(VM vm);

    public abstract void updateEntityFromVM(VM vm, @MappingTarget ENTITY entity);

    public abstract List<VM> entityToVM(List<ENTITY> entity);

}
