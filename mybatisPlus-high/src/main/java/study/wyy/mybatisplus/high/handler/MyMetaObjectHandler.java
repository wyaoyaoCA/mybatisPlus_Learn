package study.wyy.mybatisplus.high.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

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
