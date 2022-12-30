package mcsw.offer.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mcsw.offer.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * (Comment)表数据库访问层
 *
 * @author Nan
 * @since 2022-12-29 14:24:13
 */
@Mapper
public interface CommentDao extends BaseMapper<Comment> {

    @ResultMap("CommentMap")
    @Select("select * from mcsw.comment where mcsw_id = #{id}")
    List<Comment> queryByMcswId(@Param("id") String id);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Comment> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Comment> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Comment> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Comment> entities);

}

