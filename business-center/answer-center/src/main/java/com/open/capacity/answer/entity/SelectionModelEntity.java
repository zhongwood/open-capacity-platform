package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SelectionModelEntity {
    private Integer id;

    private Integer modelType;

    private Timestamp createTime;

    private Timestamp updateTime;

    private String html;

}