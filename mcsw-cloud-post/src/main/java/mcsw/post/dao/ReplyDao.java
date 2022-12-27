package mcsw.post.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mcsw.post.entity.Reply;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * (Reply)表数据库访问层
 *
 * @author Nan
 * @since 2022-11-30 11:06:46
 */
@Mapper
public interface ReplyDao extends BaseMapper<Reply> {

    /**
     * @param entities 每一个Map只包含两个键值对，一个是key为id，值为评论id，另一个是key为likeNum，值为点赞数
     */
    @Transactional
    void updateLikeNumBatch(@Param("entities")List<Map<String, Object>> entities);


    /**
     *  获取有关帖子的评论数
     */
    @ResultType(Integer.class)
    @Select(" select count(*) from mcsw.reply where post_id = #{postId}")
    int countReplyNumOfPost(@Param("postId") Integer postId);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Reply> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Reply> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Reply> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Reply> entities);

}

