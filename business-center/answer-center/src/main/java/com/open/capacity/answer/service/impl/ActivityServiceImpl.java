package com.open.capacity.answer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.answer.dao.ActivityMapper;
import com.open.capacity.answer.dao.PrizeOptionsMapper;
import com.open.capacity.answer.dao.WinInformationMapper;
import com.open.capacity.answer.entity.ActivityEntity;
import com.open.capacity.answer.entity.PrizeOptionsEntity;
import com.open.capacity.answer.entity.WinInformationEntity;
import com.open.capacity.answer.service.ActivityService;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.utils.PageUtil;
import com.open.capacity.utils.SysUserUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private PrizeOptionsMapper prizeOptionsMapper;

    @Autowired
    private WinInformationMapper winInformationMapper;

    @Override
    public PageResult<ActivityEntity> queryList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        params.put("orgId", SysUserUtil.getLoginOrgId());
        List<ActivityEntity> list = activityMapper.queryList(params);
        PageInfo<ActivityEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<ActivityEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public PageResult<PrizeOptionsEntity> queryPriceList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        params.put("orgId", SysUserUtil.getLoginOrgId());
        List<PrizeOptionsEntity> list = prizeOptionsMapper.queryPriceList(params);
        PageInfo<PrizeOptionsEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<PrizeOptionsEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public PageResult<WinInformationEntity> queryPriceLogList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        params.put("orgId", SysUserUtil.getLoginOrgId());
        List<WinInformationEntity> list = winInformationMapper.queryPriceLogList(params);
        PageInfo<WinInformationEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<WinInformationEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }


    @Override
    public Result updateStatus(Map<String, Object> params) {
        Integer id = org.apache.commons.collections4.MapUtils.getInteger(params, "id");
        Boolean enabled = org.apache.commons.collections4.MapUtils.getBoolean(params, "enabled");

        ActivityEntity entity = new ActivityEntity();
        entity.setStatus(enabled);
        entity.setUpdateTime(new Date());
        entity.setId(id);
        int i = activityMapper.updateStatus(entity);

        return i > 0 ? Result.succeed(entity, "更新成功") : Result.failed("更新失败");
    }

    @Override
    public Result savePrize(PrizeOptionsEntity entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        entity.setOrgId(SysUserUtil.getLoginOrgId());
        int i = prizeOptionsMapper.insert(entity);
        return i > 0 ? Result.succeed(entity, "添加成功") : Result.failed("添加失败");
    }

    @Override
    public void savePrizeLog(WinInformationEntity entity) {
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        winInformationMapper.insertSelective(entity);
    }

    @Override
    public Result saveOrUpdate(ActivityEntity entity) {
        String question = entity.getTitle();
        if (StringUtils.isBlank(question)) {
            return Result.failed("活动标题不能为空！");
        }

        entity.setUpdateTime(new Date());

        int i = 0;

        if (entity.getId() == null) {
            entity.setCreateTime(new Date());
            entity.setOrgId(SysUserUtil.getLoginOrgId());
            i = activityMapper.insert(entity);
        } else {
            i = activityMapper.updateByPrimaryKey(entity);
        }
        return i > 0 ? Result.succeed(entity, "操作成功") : Result.failed("操作失败");
    }

    @Override
    public void delete(Integer id) {
        prizeOptionsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Result updateLog(Integer id) {
        int i = winInformationMapper.updateLog(id);
        return i > 0 ? Result.succeed(null, "操作成功") : Result.failed("操作失败");
    }
}
