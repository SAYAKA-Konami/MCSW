package mcsw.offer.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import mcsw.offer.entity.Master;

/**
 * (Master)表数据库访问层
 *
 * @author Nan
 * @since 2022-12-29 14:21:18
 */
@Mapper
public interface MasterDao extends BaseMapper<Master> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Master> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Master> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Master> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Master> entities);

}

