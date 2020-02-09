package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionsEntity {
    private Integer id;

    private Integer questionType;

    private String videoSerial;

    private Integer selectionId;

    private Timestamp createTime;

    private Timestamp updateTime;

}