package com.open.capacity.server.dao;

import com.open.capacity.server.model.SysConfig;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysConfigDao {

    @Insert("insert into sys_config( `type`, `key`, `value`, `data_type`, `org_id`,`remark`, `create_time`, `update_time`) "
            + "values (#{type}, #{key} , #{value} , #{dataType}, #{orgId},#{remark}, #{createTime}, #{updateTime})")
    int save(SysConfig config);

    int update(SysConfig config);

    @Delete("delete from sys_config where id = #{id}")
    int delete(Long id);

    List<SysConfig> findByType(SysConfig config);

    List<SysConfig> findList(Map<String, Object> params);

    SysConfig findOne(Map<String, Object> params);

    @Delete("select * from sys_config where id = #{id}")
    SysConfig selectSysConfigById(Long id);
}
