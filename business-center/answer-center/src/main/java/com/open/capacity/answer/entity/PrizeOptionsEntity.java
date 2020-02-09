package com.open.capacity.answer.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PrizeOptionsEntity {
    private Integer id;

    private Integer activityId;

    private Integer type;

    private String name;

    private Integer counts;

    private Integer level;

    private BigDecimal percentage;

    private Date createTime;

    private Date updateTime;

    private String title;
    private String ext;
    private Integer orgId;

}