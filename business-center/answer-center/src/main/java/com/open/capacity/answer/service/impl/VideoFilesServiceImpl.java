package com.open.capacity.answer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.answer.dao.VideoFilesMapper;
import com.open.capacity.answer.entity.VideoFilesEntity;
import com.open.capacity.answer.service.VideoFilesService;
import com.open.capacity.commons.PageResult;
import com.open.capacity.utils.SysUserUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VideoFilesServiceImpl implements VideoFilesService {

    @Autowired
    private VideoFilesMapper videoFilesMapper;

    @Override
    public void upload(VideoFilesEntity entity) {
        entity.setOrgId(SysUserUtil.getLoginOrgId());
        videoFilesMapper.insert(entity);
    }

    @Override
    public void delete(VideoFilesEntity entity) {
        entity.setOrgId(SysUserUtil.getLoginOrgId());
        videoFilesMapper.deleteByPrimaryKey(entity);
    }

    @Override
    public PageResult<VideoFilesEntity> findList(Map<String, Object> params) {
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        params.put("orgId", SysUserUtil.getLoginOrgId());
        List<VideoFilesEntity> list = videoFilesMapper.findList(params);
        PageInfo<VideoFilesEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<VideoFilesEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public List<VideoFilesEntity> fileTiles() {
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", SysUserUtil.getLoginOrgId());
        return videoFilesMapper.fileTiles(params);
    }
}
