package com.open.capacity.model.system;

import lombok.Data;

import java.io.Serializable;

@Data
public class VideoInfo implements Serializable {

    private static final long serialVersionUID = 5427769142519925377L;
    // 视频id
    private String videoId;

    //视频路径
    private String videoPath;

    //视频缩略图路径
    private String thumbnailPath;

    private String playURL;


    //鉴权信息
    private String authInfo;


}
