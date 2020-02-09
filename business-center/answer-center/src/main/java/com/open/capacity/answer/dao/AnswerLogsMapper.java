package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.AnswerLogsEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface AnswerLogsMapper {
    int insert(AnswerLogsEntity record);

    int insertSelective(AnswerLogsEntity record);

    int selectCount(Map<String, Object> params);

}