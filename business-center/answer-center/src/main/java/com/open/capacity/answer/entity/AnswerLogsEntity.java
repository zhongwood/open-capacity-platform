package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class AnswerLogsEntity extends QuestionsWithBLOBsEntity {
    private Integer id;

    private Integer userId;

    private Integer questionId;

    private Integer answer;
    private Integer orgId;
    private String setId;

    private Timestamp answerTime;


}