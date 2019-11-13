package study.wyy.mybatisplus.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
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
    private LocalDateTime createTime;
    /**
     * 表示这个字段不是数据库中存在的字段，在插入数据的时候就排除这个字段
     * // 实现这种效果的还有连个方式：使用static关键子或者transient关键子
     * 其中transient关键字表示这个字段不会参与序列化，故此不会插入到数据库
     */
    @TableField(exist = false)
    private String remark;
}
