package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionStatisticsEntity extends QuestionsWithBLOBsEntity {
    private Integer id;

    private Integer questionId;

    private Integer choiceaNum;

    private Integer choicebNum;

    private Integer choicecNum;

    private Integer choicedNum;

    private Integer choiceeNum;

    private Integer wrongNum;

    private Integer rightNum;

    private Integer orgId;

    private String setId;

    private Timestamp createTime;

    private Timestamp updateTime;


}