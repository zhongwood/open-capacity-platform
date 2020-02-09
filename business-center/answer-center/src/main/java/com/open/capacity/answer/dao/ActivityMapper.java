package com.open.capacity.answer.dao;


import com.open.capacity.answer.entity.ActivityEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ActivityMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ActivityEntity record);

    int insertSelective(ActivityEntity record);

    ActivityEntity selectByPrimaryKey(Integer id);

    ActivityEntity queryActivitySet(Integer id);

    int updateByPrimaryKeySelective(ActivityEntity record);

    int updateByPrimaryKey(ActivityEntity record);


    List<ActivityEntity> queryList(Map<String, Object> param);


    int updateStatus(ActivityEntity record);
}