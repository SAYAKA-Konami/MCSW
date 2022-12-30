package mcsw.offer.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import mcsw.offer.entity.CivilServant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * (CivilServant)表数据库访问层
 *
 * @author Nan
 * @since 2022-12-29 14:23:59
 */
@Mapper
public interface CivilServantDao extends BaseMapper<CivilServant> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<CivilServant> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<CivilServant> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<CivilServant> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<CivilServant> entities);

}

