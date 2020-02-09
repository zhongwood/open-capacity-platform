package com.open.capacity.answer.controller;

import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.answer.dao.ActivityMapper;
import com.open.capacity.answer.entity.ActivityEntity;
import com.open.capacity.answer.entity.WinInformationEntity;
import com.open.capacity.answer.manager.ActivityManager;
import com.open.capacity.answer.manager.AnswerManager;
import com.open.capacity.commons.Result;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class H5Controller {

    @Autowired
    private AnswerManager answerManager;


    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @GetMapping("/h5/getVideo")
    public Result getVideo(@RequestParam Map<String, Object> params) {
        String questionLibId = (String) params.get("questionLibId");
        String setId = (String) params.get("setId");
        Integer orgId = Integer.parseInt((String) params.get("orgId"));
        if (StringUtils.isEmpty(setId)) {
            return answerManager.getFirstVideo(questionLibId, orgId);
        }
        return answerManager.getVideo(setId, questionLibId, orgId);
    }

    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @GetMapping("/h5/getEndVideo")
    public Result getEndVideo(@RequestParam Map<String, Object> params) {
        String questionLibId = (String) params.get("questionLibId");
        Integer orgId = Integer.parseInt((String) params.get("orgId"));
        return answerManager.getEndVideo(questionLibId, orgId);
    }

    @LogAnnotation(module = "answer-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('answer:query')")
    @PostMapping("/h5/saveSelect")
    public void saveSelect(@RequestBody Map<String, Object> params) {
        if (params == null || params.size() == 0) {
            return;
        }
        answerManager.saveUserSelect(params);
    }


    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private ActivityMapper activityMapper;

    /**
     * 抽奖
     * 是否抽中都做记录
     *
     * @param params
     */
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @GetMapping("/h5/draw")
    @PreAuthorize("hasAnyAuthority('activity:saveOrUpdate')")
    public Result draw(@RequestParam Map<String, Object> params) {

        return activityManager.draw(params);
    }


    /**
     * 查找活动配置
     * 返回活动的一些配置信息
     */
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @GetMapping("/h5/queryActivitySet")
    @PreAuthorize("hasAnyAuthority('activity:query')")
    public Result queryActivitySet(@RequestParam Map<String, Object> params) {
        String accountId = org.apache.commons.collections4.MapUtils.getString(params, "accountId");
        Integer orgId = org.apache.commons.collections4.MapUtils.getInteger(params, "orgId");

        if (StringUtils.isEmpty(accountId) || orgId == null) {
            return Result.failed("系统错误！");
        }
        return activityManager.queryActivitySet(accountId, orgId);
    }


    /**
     * 保存用户信息
     *
     * @param entity
     */
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @PostMapping("/h5/savePrizeLog")
    @PreAuthorize("hasAnyAuthority('activity:saveOrUpdate')")
    public Result savePrizeLog(@RequestBody WinInformationEntity entity) {
        return activityManager.savePrizeLog(entity);
    }


    /**
     * 查找活动配置
     * 返回活动的一些配置信息
     */
    @LogAnnotation(module = "activity-center", recordRequestParam = false)
    @GetMapping("/h5/queryActivity")
    @PreAuthorize("hasAnyAuthority('activity:query')")
    public Result queryActivity(@RequestParam Map<String, Object> params) {
        Integer orgId = org.apache.commons.collections4.MapUtils.getInteger(params, "orgId");
        if (orgId == null) {
            return Result.failed("系统错误！");
        }
        params.put("status", 1);
        List<ActivityEntity> list = activityMapper.queryList(params);
        int type = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            ActivityEntity entity = list.get(0);

            if (entity.getTitle().contains("转盘")) {
                type = 1;
            } else if (entity.getTitle().contains("九宫格")) {
                type = 2;
            } else if (entity.getTitle().contains("扭蛋机")) {
                type = 3;
            }
        }
        return Result.succeed("{type:" + type + "}", "系统错误！");
    }
}
