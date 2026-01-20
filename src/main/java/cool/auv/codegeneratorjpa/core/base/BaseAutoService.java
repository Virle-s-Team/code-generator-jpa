package cool.auv.codegeneratorjpa.core.base;

import cool.auv.codegeneratorjpa.core.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

public abstract class BaseAutoService<ENTITY, ID extends Serializable, REQ, VM> {

    @Autowired
    protected BaseRepository<ENTITY, ID> repository;

    @Autowired
    protected BaseAutoMapstruct<ENTITY, VM> mapstruct;

    public Page<VM> findPage(Specification<ENTITY> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(mapstruct::entityToVM);
    }

    public Optional<VM> findById(ID id) {
        return repository.findById(id).map(mapstruct::entityToVM);
    }

    @Transactional
    public VM save(VM vm) {
        ENTITY entity = mapstruct.vmToEntity(vm);
        ENTITY saved = repository.save(entity);
        return mapstruct.entityToVM(saved);
    }

    @Transactional
    public VM update(ID id, VM vm) throws AppException {
        ENTITY managedEntity = repository.findById(id)
                .orElseThrow(() -> new AppException("Entity not found"));
        mapstruct.updateEntityFromVM(vm, managedEntity);
        return mapstruct.entityToVM(repository.save(managedEntity));
    }

    @Transactional
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

}
