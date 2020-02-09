package com.open.capacity.model.system;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class SysOrg implements Serializable {

    private static final long serialVersionUID = -5886012896705137071L;
    private Long id;
    private String orgName;
    private Boolean enabled;
    @TableField(value = "createTime")
    private Date createTime;
    @TableField(value = "updateTime")
    private Date updateTime;


}
