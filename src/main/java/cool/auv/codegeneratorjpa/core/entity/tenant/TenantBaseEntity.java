package cool.auv.codegeneratorjpa.core.entity.tenant;

import cool.auv.codegeneratorjpa.core.entity.base.BaseEntity;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.TenantId;

@Data
@Accessors(chain = true)
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class TenantBaseEntity extends BaseEntity {
    /**
     * 租户id
     */
    @TenantId
    private String tenantId;

}
