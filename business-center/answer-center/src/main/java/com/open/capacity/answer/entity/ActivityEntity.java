package com.open.capacity.answer.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
public class ActivityEntity {
    private Integer id;

    private String title;

    private Timestamp startTime;

    private Timestamp endTime;

    private Integer times;

    private String homePagePath;

    private Boolean status;
    private Integer orgId;
    private Date createTime;

    private Date updateTime;


}