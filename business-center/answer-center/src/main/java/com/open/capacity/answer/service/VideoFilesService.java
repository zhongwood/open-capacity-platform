package com.open.capacity.answer.service;

import com.open.capacity.answer.entity.VideoFilesEntity;
import com.open.capacity.commons.PageResult;

import java.util.List;
import java.util.Map;

/**
 * service
 */
public interface VideoFilesService {

    void upload(VideoFilesEntity entity);

    void delete(VideoFilesEntity entity);

    PageResult<VideoFilesEntity> findList(Map<String, Object> params);

    List<VideoFilesEntity> fileTiles();

}
