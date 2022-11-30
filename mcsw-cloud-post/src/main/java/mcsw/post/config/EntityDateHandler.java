package mcsw.post.config;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @apiNote 借助Mybatis-plus横切实体类插入时关于创建时间和更新时间的功能
 * @author wu nan
 * @since  2022/11/28
 **/
@Component
public class EntityDateHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        setFieldValByName("createTime", DateTime.now().toJdkDate(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        setFieldValByName("updateTime", DateTime.now().toJdkDate(), metaObject);
    }
}
