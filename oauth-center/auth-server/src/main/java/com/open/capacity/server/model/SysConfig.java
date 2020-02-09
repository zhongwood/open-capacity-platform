package com.open.capacity.server.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class SysConfig {
    private Long id;
    private String type;
    private String key;
    private String value;
    private String dataType;
    private Integer orgId;
    private Integer enabled;
    private String remark;
    private Timestamp createTime;
    private Timestamp updateTime;
}
