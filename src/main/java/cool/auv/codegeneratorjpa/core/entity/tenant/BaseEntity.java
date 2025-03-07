//package cool.auv.codegeneratorjpa.core.entity.tenant;
//
//import com.baomidou.mybatisplus.annotation.FieldFill;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableLogic;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import lombok.Data;
//import lombok.experimental.Accessors;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
//@Data
//@Accessors(chain = true)
//public class BaseEntity implements Serializable {
//    /**
//     * 租户id
//     */
//    @TableField(value = "tenant_id", fill = FieldFill.INSERT)
//    private String tenantId;
//
//    /**
//     * 创建人
//     */
//    @TableField(value = "create_by", fill = FieldFill.INSERT)
//    private String createBy;
//
//    /**
//     * 创建时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @TableField(value = "create_time", fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
//
//    /**
//     * 更新人
//     */
//    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
//    private String updateBy;
//
//    /**
//     * 更新时间
//     */
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updateTime;
//
//    /**
//     * 查询字符
//     */
//    @TableField("search_text")
//    private String searchText;
//
//    /**
//     * 删除标识
//     */
//    @TableField("is_deleted")
//    @TableLogic
//    private Boolean isDeleted = false;
//}
