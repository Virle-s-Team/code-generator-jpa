package cool.auv.codegeneratorjpa.core.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.util.Optional;

public interface BaseAutoService<E, ID extends Serializable, REQ, VM> {
    Page<VM> findPage(Specification<E> spec, Pageable pageable);

    Optional<VM> findById(ID id);

    VM save(VM vm);

    VM update(ID id, VM vm);

    void deleteById(ID id);
}
