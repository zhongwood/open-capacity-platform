package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.ConsumerInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ConsumerInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ConsumerInfoEntity record);

    int insertSelective(ConsumerInfoEntity record);

    ConsumerInfoEntity selectByPrimaryKey(Integer id);

    List<ConsumerInfoEntity> findList(Map<String, Object> params);

    List<Map<String, Object>> userMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(ConsumerInfoEntity record);

    int updateByPrimaryKey(ConsumerInfoEntity record);


    ConsumerInfoEntity selectByUnionId(String id);
}