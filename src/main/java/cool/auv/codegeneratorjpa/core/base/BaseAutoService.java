package cool.auv.codegeneratorjpa.core.base;

import cool.auv.codegeneratorjpa.core.exception.AppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Optional;

public interface BaseAutoService<E, ID extends Serializable, REQ, VM> {
    Page<VM> findPage(Specification<E> spec, Pageable pageable) throws AppException;

    Optional<VM> findById(ID id) throws AppException;

    VM save(VM vm) throws AppException;

    VM update(ID id, VM vm) throws AppException;

    void deleteById(ID id) throws AppException;
}
