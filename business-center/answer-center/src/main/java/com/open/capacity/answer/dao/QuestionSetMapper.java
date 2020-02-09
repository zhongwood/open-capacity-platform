package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.QuestionSetEntity;
import com.open.capacity.answer.vo.QuestionSetVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface QuestionSetMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(QuestionSetEntity record);

    int insertSelective(QuestionSetEntity record);

    QuestionSetEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(QuestionSetEntity record);

    int updateEnabled(QuestionSetEntity record);

    int updateByPrimaryKey(QuestionSetEntity record);

    List<QuestionSetEntity> findList(Map<String, Object> params);


    List<QuestionSetEntity> findListByName(Map<String, Object> params);


    List<QuestionSetVO> findListByLibId(Map<String, Object> params);


    List<QuestionSetVO> querySetById(Map<String, Object> params);

    QuestionSetVO getAbleSetById(Map<String, Object> params);

    int updateSetLink(QuestionSetEntity entity);

    int saveSet(@Param("list") List<QuestionSetEntity> list);

    QuestionSetVO getFirstAbleSet();
}