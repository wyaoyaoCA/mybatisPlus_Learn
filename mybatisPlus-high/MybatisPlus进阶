MybatisPlus进阶
==

## 1 环境准备
- 创建一个数据库
- 执行sql文件：`db/数据库脚本 - 高级功能.sql`
- 对应的配置文件： `application.yml`
- 启动类`MybatisPlusStudyApplication`
- 在`study.wyy.mybatisplus.high`进行测试

## 2 逻辑删除

### 2.1 准备工作

- Spring配置文件中如下配置
```yml
## 配置
mybatis-plus:
  global-config:
    db-config:
      logic-delete-value: 1 # 配置表示逻辑删除的值
      logic-not-delete-value: 0 # 配置表示逻辑未删除的值
```
- 3.1.1版本之前，还需配置一个bean

```java
@Configuration
public class MybatisPlusConfig {

    //@Bean
    public ISqlInjector sqlInjector(){
        // 已过时，在3.1.1之后不需要配置这个bean，这里就注释掉
        return new LogicSqlInjector();
    }
}

```

- 实体类中，使用`@TableLogic`标注表示逻辑删除的字段

`study.wyy.mybatisplus.high.pojo.User`

### 2.2 测试

`study.wyy.mybatisplus.high.test.DeleteTests`

> 注意
- 删除语句变成了update语句
- select update语句都会给加上WHERE deleted=0这个限制条件，
- 但是如果自定义的sql则需要自己在sql加上，mybatisplus不会自动帮我们加上


## 2 自动填充

数据库表中一般都会有像是createTime和updateTime字段，表示数据创建和更新的时间
这些字段，mybatisplus可以帮助我门自动填充，而不需要我们在插入更新数据的时候指定

### 2.1 准备
- 使用`@TableField(fill= FieldFill.INSERT)`

```java
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
```
- 建立一个填充处理器，实现`com.baomidou.mybatisplus.core.handlers.MetaObjectHandler`

```java
package study.wyy.mybatisplus.high.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 * @author wyaoyao
 * @data 2019-11-11 22:31
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入的时候如何填充
      * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 这里的createTime是实体类的属性名而不是数据库中字段名
        setInsertFieldValByName("createTime", LocalDateTime.now(), metaObject);
        //下面写法也可以
        //setFieldValByName("createTime", LocalDateTime.now(), metaObject)
    }

    /**
     * 更新的时候如何填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        setUpdateFieldValByName("updateTime",LocalDateTime.now(),metaObject);
        //下面写法也可以
        //setFieldValByName("updateTime", LocalDateTime.now(), metaObject)
    }
}

```
- 一定要注入到spring容器中

### 2.2 测试
`study.wyy.mybatisplus.high.FillTests`
