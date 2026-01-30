package cool.auv.codegeneratorjpa.core.base;

import cool.auv.codegeneratorjpa.core.exception.AppException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Optional;

@Transactional(readOnly = true)
public abstract class AbstractAutoService<E, ID extends Serializable, REQ, VM> implements BaseAutoService<E, ID, REQ, VM> {

    @Autowired
    protected BaseRepository<E, ID> repository;

    @Autowired
    protected BaseAutoMapstruct<E, VM> mapstruct;

//    protected AbstractAutoService(BaseRepository<E, ID> repository, BaseAutoMapstruct<E, VM> mapstruct) {
//        this.repository = repository;
//        this.mapstruct = mapstruct;
//    }

    @Override
    public Page<VM> findPage(Specification<E> spec, Pageable pageable) {
        return repository.findAll(spec, pageable).map(mapstruct::entityToVM);
    }

    @Override
    public Optional<VM> findById(ID id) {
        return repository.findById(id).map(mapstruct::entityToVM);
    }

    @Transactional
    @Override
    public VM save(VM vm) {
        E entity = mapstruct.vmToEntity(vm);
        E saved = repository.save(entity);
        return mapstruct.entityToVM(saved);
    }


    @Transactional
    @Override
    public VM update(ID id, VM vm) {
        E managedEntity = repository.findById(id)
                .orElseThrow(() -> new AppException("Entity not found"));
        mapstruct.updateEntityFromVM(vm, managedEntity);
        return mapstruct.entityToVM(repository.save(managedEntity));
    }

    @Transactional
    @Override
    public void deleteById(ID id) {
        repository.deleteById(id);
    }

}
