package com.open.capacity.user.dao;

import com.open.capacity.model.system.SysOrg;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 组织管理
 */
@Mapper
public interface SysOrgDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into sys_org(org_name, createTime, updateTime) "
            + "values(#{orgName}, #{createTime}, #{updateTime})")
    int save(SysOrg sysOrg);

    int updateByOps(SysOrg sysOrg);

    @Select("select * from sys_org t where t.org_name = #{orgName}")
    SysOrg findOrgByName(String orgName);

    @Select("select * from sys_org t where t.id = #{id}")
    SysOrg findById(Long id);

    int count(Map<String, Object> params);

    List<SysOrg> findList(Map<String, Object> params);
}
