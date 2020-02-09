package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class QuestionsVideoRelationEntity {
    private Integer id;

    private String questionLibId;

    private String startVideoSerial;

    private String endVideoSerial;

    private Integer orgId;

    private Timestamp createTime;

    private Timestamp updateTime;

}
