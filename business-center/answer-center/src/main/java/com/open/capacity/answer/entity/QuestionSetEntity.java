package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionSetEntity {
    private Integer id;

    private String questionLibId;

    private String questionLibName;

    private Integer questionId;

    private Integer questionLevel;

    private String choiceaNext;

    private String choicebNext;

    private String choicecNext;

    private String choicedNext;

    private String choiceeNext;

    private String choiceRightNext;

    private String choiceWrongNext;

    private String defaultNext;

    private Boolean isAbled;

    private Integer orgId;

    private Timestamp createTime;

    private Timestamp updateTime;

}