package com.open.capacity.answer.service;

import com.open.capacity.answer.entity.ActivityEntity;
import com.open.capacity.answer.entity.PrizeOptionsEntity;
import com.open.capacity.answer.entity.WinInformationEntity;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;

import java.util.Map;

public interface ActivityService {

    PageResult<ActivityEntity> queryList(Map<String, Object> param);

    PageResult<PrizeOptionsEntity> queryPriceList(Map<String, Object> param);

    PageResult<WinInformationEntity> queryPriceLogList(Map<String, Object> param);

    public Result updateStatus(Map<String, Object> params);

    public Result savePrize(PrizeOptionsEntity entity);

    public void savePrizeLog(WinInformationEntity entity);

    Result saveOrUpdate(ActivityEntity entity);

    public void delete(Integer id);


    public Result updateLog(Integer id);
}
