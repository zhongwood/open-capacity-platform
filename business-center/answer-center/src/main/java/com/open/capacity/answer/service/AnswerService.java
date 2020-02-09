package com.open.capacity.answer.service;

import com.open.capacity.answer.entity.QuestionSetEntity;
import com.open.capacity.answer.entity.QuestionsWithBLOBsEntity;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;

import java.util.Map;

public interface AnswerService {


    PageResult<QuestionsWithBLOBsEntity> findList(Map<String, Object> param);


    PageResult<QuestionSetEntity> setList(Map<String, Object> param);

    void saveQuestion(QuestionsWithBLOBsEntity entity);

    Result saveOrUpdate(QuestionsWithBLOBsEntity entity);

    Result bindVideo(QuestionsWithBLOBsEntity entity);

    Result updateEnabled(Map<String, Object> param);


    Result queryStatic(String questionTitle, String setId);


    Result queryQuestionStatic(String questionLibName);

}
