package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.QuestionStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface QuestionStatisticsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionStatisticsEntity record);

    int insertSelective(QuestionStatisticsEntity record);

    QuestionStatisticsEntity selectByPrimaryKey(Integer id);

    QuestionStatisticsEntity selectByOrgId(Map<String, Object> params);


    QuestionStatisticsEntity selectByQuestionId(Map<String, Object> params);

    int updateByPrimaryKeySelective(QuestionStatisticsEntity record);

    int updateByPrimaryKey(QuestionStatisticsEntity record);
}