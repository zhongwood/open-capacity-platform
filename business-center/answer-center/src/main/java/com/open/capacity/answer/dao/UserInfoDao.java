package com.open.capacity.answer.dao;


import com.open.capacity.answer.entity.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserInfoDao {

    @Insert("insert into consumer_info(third_type,union_id,nick_name,sex,mobile,country,province,city,head_img_url,privilege,address,create_time,update_time) "
            + "values( #{thirdType},#{unionId},#{nickName},#{sex},#{mobile},#{country},#{province},#{city},#{headImgUrl},#{privilege},#{address},#{createTime},#{updateTime})")
    int insert(UserInfo userInfo);


    @Select("select * from consumer_info t where t.union_id = #{unionId} and t.org_id = #{orgId}")
    UserInfo getClient(UserInfo userInfo);
}
