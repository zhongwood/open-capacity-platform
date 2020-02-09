package com.open.capacity.answer.service;

import com.open.capacity.answer.entity.ConsumerInfoEntity;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;

import java.util.Map;

public interface ThirdUserService {

    PageResult<ConsumerInfoEntity> findList(Map<String, Object> param);

    Result userMap(Map<String, Object> param);

    void saveUserInfo(ConsumerInfoEntity entity);

}
