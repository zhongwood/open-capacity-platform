package com.open.capacity.answer.dao;

import com.open.capacity.answer.entity.VideoFilesEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoFilesMapper {
    int deleteByPrimaryKey(VideoFilesEntity record);

    int insert(VideoFilesEntity record);

    int insertSelective(VideoFilesEntity record);

    VideoFilesEntity selectByPrimaryKey(Map<String, Object> params);

    int updateByPrimaryKeySelective(VideoFilesEntity record);

    int updateByPrimaryKey(VideoFilesEntity record);

    List<VideoFilesEntity> findList(Map<String, Object> params);

    List<VideoFilesEntity> fileTiles(Map<String, Object> params);

}