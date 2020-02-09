package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.QuestionsVideoRelationEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface QuestionsVideoRelationMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionsVideoRelationEntity questionsVideoRelationEntity);

    QuestionsVideoRelationEntity selectByPrimaryKey(Integer id);


    QuestionsVideoRelationEntity selectByQuestionId(Map<String, Object> params);

    int updateByPrimaryKeySelective(QuestionsVideoRelationEntity questionsVideoRelationEntity);

    int updateByPrimaryKey(QuestionsVideoRelationEntity questionsVideoRelationEntity);
}