package com.open.capacity.answer.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VideoFilesEntity {
    private Integer id;

    private String videoName;

    private String videoId;

    private String videoSize;

    private Date videoTime;

    private String videoPath;

    private String thumbnailPath;

    private Integer videoStatus;

    private String videoType;

    private Integer nextVideo;

    private Integer orgId;

    private Date createTime;

    private Date updateTime;


}