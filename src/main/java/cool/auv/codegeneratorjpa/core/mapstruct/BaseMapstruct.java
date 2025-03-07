package cool.auv.codegeneratorjpa.core.mapstruct;

import org.mapstruct.MappingTarget;

import java.util.List;

public abstract class BaseMapstruct<E, V>  {

    public abstract V entityToVm(E entity);

    public abstract E vmToEntity(V vm);

    public abstract void updateEntityFromVM(V vm, @MappingTarget E entity);

    public abstract List<V> entityToVm(List<E> entity);

    public abstract void copyEntity(@MappingTarget E target, E source);
}
