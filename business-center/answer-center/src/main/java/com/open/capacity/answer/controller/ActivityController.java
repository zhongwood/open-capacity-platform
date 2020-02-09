package com.open.capacity.answer.controller;

import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.answer.dao.ActivityMapper;
import com.open.capacity.answer.entity.ActivityEntity;
import com.open.capacity.answer.entity.PrizeOptionsEntity;
import com.open.capacity.answer.entity.WinInformationEntity;
import com.open.capacity.answer.service.ActivityService;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.utils.SysUserUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ActivityController {


    @Autowired
    private ActivityService activityService;

    @Autowired
    private ActivityMapper activityMapper;

    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('activity:query')")
    @GetMapping("/queryList")
    public PageResult<ActivityEntity> queryList(@RequestParam Map<String, Object> params) {

        return activityService.queryList(params);

    }

    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('activity:query')")
    @GetMapping("/queryPriceList")
    public PageResult<PrizeOptionsEntity> queryPriceList(@RequestParam Map<String, Object> params) {

        return activityService.queryPriceList(params);

    }

    @ApiOperation(value = "修改活动状态")
    @GetMapping("/updateStatus")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "活动id", required = true, dataType = "Interger"),
            @ApiImplicitParam(name = "enabled", value = "是否启用", required = true, dataType = "Boolean")
    })
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @PreAuthorize("hasAnyAuthority('activity:saveOrUpdate')")
    public Result updateStatus(@RequestParam Map<String, Object> params) {
        return activityService.updateStatus(params);
    }


    /**
     * 新增or更新
     *
     * @param entity
     * @return
     */
    @PostMapping("/saveOrUpdate")
    @PreAuthorize("hasAnyAuthority('activity:saveOrUpdate')")
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    public Result saveOrUpdate(@RequestBody ActivityEntity entity) {
        return activityService.saveOrUpdate(entity);
    }


    /**
     * 新增or更新
     *
     * @param entity
     * @return
     */
    @PostMapping("/savePrize")
    @PreAuthorize("hasAnyAuthority('activity:saveOrUpdate')")
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    public Result savePrize(@RequestBody PrizeOptionsEntity entity) {
        return activityService.savePrize(entity);
    }

    /**
     * 删除服务
     *
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('activity:saveOrUpdate')")
    @ApiOperation(value = "删除服务")
    @DeleteMapping("delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            activityService.delete(id);

        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.failed("操作失败");
        }
        return Result.succeed("操作成功");
    }

    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('activity:query')")
    @GetMapping("/priceSelect")
    public List<ActivityEntity> priceSelect() {
        Map<String, Object> param = new HashMap<>();
        param.put("orgId", SysUserUtil.getLoginOrgId());
        return activityMapper.queryList(param);

    }


    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('activity:query')")
    @GetMapping("/queryPriceLogList")
    public PageResult<WinInformationEntity> queryPriceLogList(@RequestParam Map<String, Object> params) {

        return activityService.queryPriceLogList(params);

    }


    @PreAuthorize("hasAuthority('activity:saveOrUpdate')")
    @ApiOperation(value = "更新服务")
    @DeleteMapping("updateLog/{id}")
    public Result updateLog(@PathVariable Integer id) {
        return activityService.updateLog(id);

    }
}
