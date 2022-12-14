package mcsw.account.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import mcsw.account.entity.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * (User)表数据库访问层
 *
 * @author Nan
 * @since 2022-11-26 16:04:16
 */
@Mapper
public interface UserDao extends BaseMapper<User> {

    @ResultType(Integer.class)
    @Select("select count(*) from user where name = #{name}")
    int countByName(String name);
    @ResultType(Integer.class)
    @Select("select count(*) from user where account = #{account}")
    int countByAccount(@Param("account") String account);

    @ResultMap("UserMap")
    @Select("select * from user where account = #{account}")
    User selectByAccountUser(@Param("account") String account);


    @Transactional
    void updateOne(User user);

    @Transactional
    @Update("update user set passwd = #{passwd} where account = ${account}")
    void updatePasswd(@Param("account")String account, @Param("passwd") String passwd);


/**
* 批量新增数据（MyBatis原生foreach方法）
*
* @param entities List<User> 实例对象列表
* @return 影响行数
*/
int insertBatch(@Param("entities") List<User> entities);

/**
* 批量新增或按主键更新数据（MyBatis原生foreach方法）
*
* @param entities List<User> 实例对象列表
* @return 影响行数
* @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
*/
int insertOrUpdateBatch(@Param("entities") List<User> entities);

}

