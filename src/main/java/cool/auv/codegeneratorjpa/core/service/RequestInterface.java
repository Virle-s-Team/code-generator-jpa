package cool.auv.codegeneratorjpa.core.service;

import org.springframework.data.jpa.domain.Specification;

public interface RequestInterface<T> {
    Specification<T> buildSpecification();
}
