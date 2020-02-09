package com.open.capacity.answer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.answer.entity.ConsumerInfoEntity;
import com.open.capacity.answer.service.ThirdUserService;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@RestController
public class ThirdUserController {


    private static Logger log = LoggerFactory.getLogger(ThirdUserController.class);

    @Autowired
    private ThirdUserService thirdUserService;

    @LogAnnotation(module = "third-user-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @GetMapping("/userList")
    public PageResult<ConsumerInfoEntity> findUserList(@RequestParam Map<String, Object> params) throws JsonProcessingException {

        return thirdUserService.findList(params);

    }


    @LogAnnotation(module = "third-user-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('thirdUser:save')")
    @PostMapping("/saveUserInfo")
    public Result saveUserInfo(@ApiIgnore @RequestBody Map<String, Object> params) {
        ConsumerInfoEntity entity = new ConsumerInfoEntity();

        try {
            thirdUserService.saveUserInfo(entity);
            return Result.succeed("操作成功");
        } catch (Exception ex) {
            return Result.failed("操作失败");
        }
    }

    @LogAnnotation(module = "third-user-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @GetMapping("/userMap")
    public Result userMap(@RequestParam Map<String, Object> params) throws JsonProcessingException {

        return thirdUserService.userMap(params);

    }
}
