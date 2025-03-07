package cool.auv.codegeneratorjpa.core.vm;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Data
public class CommonPageVM {

    private Integer page = 0;

    private Integer size = 10;

    /**
     * properties1,properties2,properties3 Direction
     * example: createtime desc
     */
    private String sort;

    public Pageable pageable(){
        return PageRequest.of(page,size);
    }

    public Pageable pageableWithSort() {
        return PageRequest.of(page,size, getSort());
    }
    public String getSortString() {
        return this.sort;
    }

    public Sort getSort() {
        if (StringUtils.isNotEmpty(this.sort)) {
            String[] s = this.sort.split(" ");

            return Sort.by(Sort.Direction.valueOf(s[1].toUpperCase()), s[0]);
        }
        return Sort.unsorted();
    }
}
