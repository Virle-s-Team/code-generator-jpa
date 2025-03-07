package cool.auv.codegeneratorjpa.core.vm;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页和排序请求对象
 */
@Data
public class PageSortRequest {

    // 默认分页参数
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    /**
     * 页码，从 0 开始
     */
    private Integer page = DEFAULT_PAGE;

    /**
     * 每页大小
     */
    private Integer size = DEFAULT_SIZE;

    /**
     * 排序规则，格式：字段名 方向（例如 "createtime desc,name asc"）
     * 支持多字段排序，用逗号分隔
     */
    private String sort;

    /**
     * 返回无排序的分页对象
     */
    public Pageable pageable() {
        return PageRequest.of(getSafePage(), getSafeSize());
    }

    /**
     * 返回带排序的分页对象
     */
    public Pageable pageableWithSort() {
        return PageRequest.of(getSafePage(), getSafeSize(), getSort());
    }

    /**
     * 获取排序对象
     */
    public Sort getSort() {
        if (!StringUtils.hasText(sort)) {
            return Sort.unsorted();
        }

        List<Sort.Order> orders = new ArrayList<>();
        String[] sortPairs = sort.split(",");
        for (String pair : sortPairs) {
            String[] parts = pair.trim().split("\\s+");
            if (parts.length == 2) {
                String field = parts[0];
                String direction = parts[1];
                try {
                    orders.add(new Sort.Order(Sort.Direction.fromString(direction), field));
                } catch (IllegalArgumentException e) {
                    // 忽略无效的方向，保持健壮性
                }
            }
        }
        return orders.isEmpty() ? Sort.unsorted() : Sort.by(orders);
    }

    /**
     * 确保页码安全（非负）
     */
    private int getSafePage() {
        return page != null && page >= 0 ? page : DEFAULT_PAGE;
    }

    /**
     * 确保每页大小安全（正数）
     */
    private int getSafeSize() {
        return size != null && size > 0 ? size : DEFAULT_SIZE;
    }
}