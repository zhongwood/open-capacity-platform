package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.QuestionsWithBLOBsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionsWithBLOBsEntity record);

    int insertSelective(QuestionsWithBLOBsEntity record);

    QuestionsWithBLOBsEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionsWithBLOBsEntity record);

    int updateByPrimaryKeyWithBLOBs(QuestionsWithBLOBsEntity record);

    int updateByPrimaryKey(QuestionsWithBLOBsEntity record);

    List<QuestionsWithBLOBsEntity> findList(Map<String, Object> params);

}