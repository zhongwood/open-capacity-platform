package com.open.capacity.answer.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.answer.dao.ConsumerInfoMapper;
import com.open.capacity.answer.entity.ConsumerInfoEntity;
import com.open.capacity.answer.service.ThirdUserService;
import com.open.capacity.commons.CodeEnum;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.utils.PageUtil;
import com.open.capacity.utils.SysUserUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ThirdUserServiceImpl implements ThirdUserService {

    @Autowired
    private ConsumerInfoMapper consumerInfoMapper;

    @Override

    public PageResult<ConsumerInfoEntity> findList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(MapUtils.getInteger(params, "page"), MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        params.put("orgId", SysUserUtil.getLoginOrgId());
        List<ConsumerInfoEntity> list = consumerInfoMapper.findList(params);
        PageInfo<ConsumerInfoEntity> pageInfo = new PageInfo<>(list);
        return PageResult.<ConsumerInfoEntity>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public Result userMap(Map<String, Object> param) {
        param.put("orgId", SysUserUtil.getLoginOrgId());
        List<Map<String, Object>> result = consumerInfoMapper.userMap(param);

        return Result.succeedWith(JSON.toJSONString(result, SerializerFeature.UseSingleQuotes), CodeEnum.SUCCESS.getCode(), null);
    }

    @Override
    public void saveUserInfo(ConsumerInfoEntity entity) {
        consumerInfoMapper.insert(entity);
    }
}
