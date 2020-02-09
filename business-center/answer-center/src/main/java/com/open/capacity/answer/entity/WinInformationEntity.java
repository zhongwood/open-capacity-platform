package com.open.capacity.answer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WinInformationEntity {
    private Integer id;

    private Integer prizeId;

    private String prizeName;


    private Integer activityId;

    private String prizeCode;

    private String accountId;

    private String userName;

    private String userMobile;

    private String userAddress;

    private Integer status;

    private Date createTime;

    private Date updateTime;
    private Integer orgId;
}