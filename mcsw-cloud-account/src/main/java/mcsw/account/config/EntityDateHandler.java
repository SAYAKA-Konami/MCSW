package mcsw.account.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @apiNote 借助Mybatis-plus横切实体类插入时关于创建时间和更新时间的功能
 * @author wu nan
 * @since  2022/11/28
 **/
@Component
public class EntityDateHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
    }
}
