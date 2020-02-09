package com.open.capacity.server.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.open.capacity.commons.PageResult;
import com.open.capacity.server.dao.SysConfigDao;
import com.open.capacity.server.model.SysConfig;
import com.open.capacity.server.service.SysConfigService;
import com.open.capacity.utils.DateUtils;
import com.open.capacity.utils.PageUtil;
import com.open.capacity.utils.SysUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigDao sysConfigDao;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public PageResult<SysConfig> getConfigList(Map<String, Object> params) {
        PageUtil.pageParamConver(params, true);
        //设置分页信息，分别是当前页数和每页显示的总记录数【记住：必须在mapper接口中的方法执行之前设置该分页信息】
        PageHelper.startPage(org.apache.commons.collections.MapUtils.getInteger(params, "page"), org.apache.commons.collections.MapUtils.getInteger(params, "limit"), true);
        PageHelper.clearPage();
        Integer orgId = SysUserUtil.getLoginOrgId();
        params.put("orgId", orgId);
        List<SysConfig> list = sysConfigDao.findList(params);
        PageInfo<SysConfig> pageInfo = new PageInfo<>(list);
        return PageResult.<SysConfig>builder().data(pageInfo.getList()).code(0).count(pageInfo.getTotal()).build();
    }

    @Override
    public SysConfig getConfig(Map<String, Object> params) {
        return sysConfigDao.findOne(params);
    }


    /**
     * 添加服务
     *
     * @param config
     */
    @Override
    public void save(SysConfig config) {
        config.setCreateTime(DateUtils.getTimestamp());
        config.setUpdateTime(DateUtils.getTimestamp());
        sysConfigDao.save(config);
        redisTemplate.opsForValue().set(config.getType() + "_" + config.getKey() + "_" + config.getOrgId(), config.getValue());
    }

    /**
     * 更新服务
     *
     * @param config
     */
    @Override
    public void update(SysConfig config) {
        config.setUpdateTime(DateUtils.getTimestamp());
        sysConfigDao.update(config);
        // 清缓存
        redisTemplate.delete(config.getType() + "_" + config.getKey() + "_" + config.getOrgId());
        redisTemplate.opsForValue().set(config.getType() + "_" + config.getKey() + "_" + config.getOrgId(), config.getValue());
    }

    /**
     * 删除服务
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        SysConfig sysConfig = sysConfigDao.selectSysConfigById(id);
        if (sysConfig != null) {
            sysConfigDao.delete(id);
            redisTemplate.delete(sysConfig.getType() + "_" + sysConfig.getOrgId());
            redisTemplate.opsForValue().set(sysConfig.getType() + "_" + sysConfig.getKey() + "_" + sysConfig.getOrgId(), sysConfig.getValue());
        }
    }
}
