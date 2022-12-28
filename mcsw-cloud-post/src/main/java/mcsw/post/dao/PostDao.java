package mcsw.post.dao;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import mcsw.post.model.bo.PostBo;
import org.apache.ibatis.annotations.*;
import mcsw.post.entity.Post;
import org.springframework.transaction.annotation.Transactional;

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
    List<Post> selectAllByUserId(@Param("id") Integer userId);

    @ResultMap("PostMap")
    @Select("select * from post where user_id = #{id}")
    IPage<Post> selectAllByUserId(@Param("page")IPage<Post> page, @Param("id") Integer userId);

    @Transactional
    @ResultType(Integer.class)
    @Update("update  post set like = #{likeNum} where id = #{postId}")
    int likeIncrease(@Param("likeNum") Integer likeNum, @Param("postId") Integer postId);

    @Transactional
    void updateLikeNumBatch(@Param("entities")List<Map<String, Object>> entities);

    List<PostBo> queryHomepage(@Param("offset") Long offset, @Param("rows") Long rows);

    /**
     *  根据用户ID查询对应的帖子
     * @param userId 用户ID
     * @return 该用户发布的帖子集合
     */
    List<PostBo> queryFullInfoOfPost(@Param("id") Integer userId);

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

