package com.open.capacity.answer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ConsumerInfoEntity {
    private Integer id;

    private Integer thirdType;

    private String unionId;

    private String nickName;

    private Integer sex;

    private Integer orgId;

    private String mobile;

    private String country;

    private String province;

    private String city;

    private String headImgUrl;

    private String privilege;

    private String address;

    private Date createTime;

    private Date updateTime;

}