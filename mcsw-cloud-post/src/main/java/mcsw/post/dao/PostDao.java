package mcsw.post.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.*;
import mcsw.post.entity.Post;

/**
 * (Post)表数据库访问层
 *
 * @author Nan
 * @since 2022-11-30 10:58:03
 */
@Mapper
public interface PostDao extends BaseMapper<Post> {

    @ResultMap("PostMap")
    @Select("select * from post where user_id = #{id}")
    IPage<Post> selectAllByUserId(IPage<?> page, @Param("user_id") Integer userId);


    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Post> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Post> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Post> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Post> entities);

}

