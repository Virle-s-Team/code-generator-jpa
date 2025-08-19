package cool.auv.gen.request;

import cool.auv.codegeneratorjpa.core.service.RequestInterface;
import cool.auv.gen.entity.TestEntity;
import org.springframework.data.jpa.domain.Specification;
import java.util.ArrayList;
import jakarta.persistence.criteria.Predicate;

public class TestEntityRequest implements RequestInterface<TestEntity> {
    private String name;
    private String email;

    @Override
    public Specification<TestEntity> buildSpecification() {
        return (root, query, cb) -> {
            java.util.List<Predicate> predicates = new ArrayList<>();
            // Add predicate logic here if needed for more advanced tests
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
