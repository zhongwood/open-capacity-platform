package com.open.capacity.server.controller;

import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.server.dto.ClientDto;
import com.open.capacity.server.model.SysConfig;
import com.open.capacity.server.model.SysService;
import com.open.capacity.server.service.SysConfigService;
import com.open.capacity.server.service.SysServiceService;
import com.open.capacity.utils.SysUserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: [gitgeek]
 * @Date: [2018-08-23 16:20]
 * @Description: [ ]
 * @Version: [1.0.0]
 * @Copy: [com.zzg]
 */
@RestController
@Api(tags = "服务模块api")
@RequestMapping("/services")
public class SysServiceController {

    @Autowired
    private SysServiceService sysServiceService;


    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 查询所有服务
     *
     * @return
     */
    @PreAuthorize("hasAuthority('service:get/service/findAlls')")
    @ApiOperation(value = "查询所有服务")
    @GetMapping("/findAlls")
    public PageResult<SysService> findAlls() {
        List<SysService> list = sysServiceService.findAll();

        return PageResult.<SysService>builder().data(list).code(0).count((long) list.size()).build();
    }

    /**
     * 获取服务以及顶级服务
     *
     * @return
     */
    @ApiOperation(value = "获取服务以及顶级服务")
    @GetMapping("/findOnes")
    @PreAuthorize("hasAuthority('service:get/service/findOnes')")
    public PageResult<SysService> findOnes() {
        List<SysService> list = sysServiceService.findOnes();
        return PageResult.<SysService>builder().data(list).code(0).count((long) list.size()).build();
    }

    /**
     * 删除服务
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('service:delete/service/{id}')")
    @ApiOperation(value = "删除服务")
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        try {
            sysServiceService.delete(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failed("操作失败");
        }
        return Result.succeed("操作成功");
    }

    @PreAuthorize("hasAnyAuthority('service:post/saveOrUpdate')")
    @ApiOperation(value = "新增服务")
    @PostMapping("/saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SysService service) {
        try {
            if (service.getId() != null) {
                sysServiceService.update(service);
            } else {
                sysServiceService.save(service);
            }
            return Result.succeed("操作成功");
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failed("操作失败");
        }
    }

    @ApiOperation(value = "根据clientId获取对应的服务")
    @GetMapping("/{clientId}/services")
    public List<Map<String, Object>> findServicesByclientId(@PathVariable Long clientId) {
        Set<Long> clientIds = new HashSet<Long>();

        //初始化应用
        clientIds.add(clientId);

        List<SysService> clientService = sysServiceService.findByClient(clientIds);
        List<SysService> allService = sysServiceService.findAll();
        List<Map<String, Object>> authTrees = new ArrayList<>();

        Map<Long, SysService> clientServiceMap = clientService.stream().collect(Collectors.toMap(SysService::getId, SysService -> SysService));

        for (SysService sysService : allService) {
            Map<String, Object> authTree = new HashMap<>();
            authTree.put("id", sysService.getId());
            authTree.put("name", sysService.getName());
            authTree.put("pId", sysService.getParentId());
            authTree.put("open", true);
            authTree.put("checked", false);
            if (clientServiceMap.get(sysService.getId()) != null) {
                authTree.put("checked", true);
            }
            authTrees.add(authTree);
        }

        return authTrees;
    }

    @PostMapping("/granted")
    public Result setMenuToClient(@RequestBody ClientDto clientDto) {
        sysServiceService.setMenuToClient(clientDto.getId(), clientDto.getServiceIds());

        return Result.succeed("操作成功");
    }


    @PostMapping("/saveOrUpdateConfig")
    @ApiOperation(value = "保存或者修改应用")
    @PreAuthorize("hasAuthority('sys:role:saveOrUpdate')")
    public Result saveOrUpdate(@RequestBody SysConfig config) {
        try {
            config.setOrgId(SysUserUtil.getLoginOrgId());

            if (config.getId() != null) {
                sysConfigService.update(config);
            } else {
                sysConfigService.save(config);
            }
            return Result.succeed("操作成功");
        } catch (Exception exq) {
            exq.printStackTrace();
            return Result.failed("操作失败");
        }
    }


    /**
     * 删除服务
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('sys:role:saveOrUpdate')")
    @ApiOperation(value = "删除服务")
    @DeleteMapping("/deleteConfig/{id}")
    public Result deleteConfig(@PathVariable Long id) {
        try {
            sysConfigService.delete(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failed("操作失败");
        }
        return Result.succeed("操作成功");
    }


    @GetMapping("/getConfigList")
    @ApiOperation(value = "配置列表")
    @PreAuthorize("hasAuthority('sys:role:query')")
    public PageResult<SysConfig> getConfigList(@RequestParam Map<String, Object> params) {
        return sysConfigService.getConfigList(params);
    }

    @GetMapping("/users-anon/getConfig")
    @ApiOperation(value = "配置列表")
    public String getConfig(@RequestParam Map<String, Object> params) {
        SysConfig config = sysConfigService.getConfig(params);
        if (config == null) {
            return null;
        }
        return config.getValue();
    }
}
