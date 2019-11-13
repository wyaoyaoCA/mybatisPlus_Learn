package study.wyy.mybatisplus.high.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wyaoyao
 * @data 2019-11-09 14:07
 */
@Data
/**
 * 如果数据库表名和实体类的名字不一致，则可以使用@TableName注解标注表名
 * 类似的注解： @TableField("name")
 */
@TableName("user")
public class User {
    /**
     * 主键
     */
    @TableId
    private Long id;

    @TableField("name")
    private String name;

    private Integer age;

    private String email;
    /**
     * 直属上架，自关联
     *
     * mybatis默认就是驼峰命名，数据库字段的名字为manager_id
     *
     */
    private Long managerId;
    /**
     * 创建时间
     */
    @TableField(fill= FieldFill.INSERT)  // 插入时填充
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE) // 更新时填充
    private LocalDateTime updateTime;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 逻辑删除：
     *  0：未删除
     *  1：删除
     */
    @TableLogic
    private Integer deleted;

}
