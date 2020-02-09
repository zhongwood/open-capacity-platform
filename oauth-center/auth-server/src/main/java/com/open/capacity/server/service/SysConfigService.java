package com.open.capacity.server.service;

import com.open.capacity.commons.PageResult;
import com.open.capacity.server.model.SysConfig;

import java.util.Map;

public interface SysConfigService {

    PageResult<SysConfig> getConfigList(Map<String, Object> params);

    SysConfig getConfig(Map<String, Object> params);

    /**
     * 添加服务
     *
     * @param config
     */
    void save(SysConfig config);

    /**
     * 更新服务
     *
     * @param config
     */
    void update(SysConfig config);

    /**
     * 删除服务
     *
     * @param id
     */
    void delete(Long id);
}
