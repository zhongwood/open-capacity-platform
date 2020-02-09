package com.open.capacity.answer.manager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.open.capacity.answer.dao.ActivityMapper;
import com.open.capacity.answer.dao.PrizeOptionsMapper;
import com.open.capacity.answer.dao.WinInformationMapper;
import com.open.capacity.answer.entity.ActivityEntity;
import com.open.capacity.answer.entity.PrizeOptionsEntity;
import com.open.capacity.answer.entity.WinInformationEntity;
import com.open.capacity.answer.util.GenSerial;
import com.open.capacity.answer.util.LotteryUtil;
import com.open.capacity.answer.util.Prize;
import com.open.capacity.answer.vo.PrizeOptionsVo;
import com.open.capacity.commons.CodeEnum;
import com.open.capacity.commons.Result;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ActivityManager {

    @Autowired
    PrizeOptionsMapper prizeOptionsMapper;
    @Autowired
    ActivityMapper activityMapper;
    @Autowired
    WinInformationMapper winInformationMapper;

    /**
     * 抽奖
     *
     * @return
     */
    public Result draw(Map<String, Object> params) {
        //活动id
        Integer activityId = org.apache.commons.collections4.MapUtils.getInteger(params, "activityId");
        Integer orgId = org.apache.commons.collections4.MapUtils.getInteger(params, "orgId");
        String accountId = org.apache.commons.collections4.MapUtils.getString(params, "accountId");

        //校验活动信息是否过期
        ActivityEntity activityEntity = activityMapper.selectByPrimaryKey(activityId);

        if (activityEntity == null) {
            return Result.failed("活动不存在！");
        }
        if (!activityEntity.getStatus()) {
            return Result.failed("活动已下线！");
        }
        //验证活动是否失效
        if (!isEffectiveDate(new Date(), activityEntity.getStartTime(), activityEntity.getEndTime())) {
            return Result.failed("活动已过期！");
        }
        //去数据查该会员是否已经抽过了
        WinInformationEntity winInformation = new WinInformationEntity();
        winInformation.setAccountId(accountId);
        winInformation.setActivityId(activityId);
        winInformation.setOrgId(orgId);
        int count = winInformationMapper.selectCount(winInformation);
        if (count >= activityEntity.getTimes()) {
            return Result.failed("您的抽奖机会已经用光了哦！");
        }
        //先从数据库中获取该活动配置的奖项信息
        List<PrizeOptionsEntity> list = prizeOptionsMapper.selectByActivityId(activityId);

        if (CollectionUtils.isEmpty(list)) {
            return Result.failed("系统错误");
        }
        //抽奖
        Prize prize;
        List<Prize> prizes = new ArrayList<>();
        for (PrizeOptionsEntity entity : list) {
            prize = new Prize();
            prize.setPrizeId(entity.getId());
            prize.setProbability(entity.getPercentage());
            prize.setQuantity(entity.getCounts());
            prizes.add(prize);
        }
        int prizeId = LotteryUtil.lottery(prizes);

        // 保存用户获奖信息
        WinInformationEntity winInformationEntity = new WinInformationEntity();
        winInformationEntity.setAccountId(accountId);
        winInformationEntity.setActivityId(activityId);
        winInformationEntity.setOrgId(orgId);
        winInformationEntity.setCreateTime(new Date());
        winInformationEntity.setUpdateTime(new Date());
        winInformationEntity.setStatus(1);
        //获取中奖兑换码
        String code = GenSerial.generateNewCode(1, 10);
        winInformationEntity.setPrizeCode(code);
        PrizeOptionsVo vo = new PrizeOptionsVo();
        for (PrizeOptionsEntity entity : list) {
            if (prizeId == entity.getId()) {
                winInformationEntity.setPrizeId(entity.getId());
                winInformationEntity.setPrizeName(entity.getTitle());
                //获奖了
                vo.setActivityId(entity.getActivityId());
                vo.setId(entity.getId());
                vo.setName(entity.getName());
                vo.setLevel(entity.getLevel());
                vo.setType(entity.getType());
            }
        }
        // 奖品减库存
        prizeOptionsMapper.updateCount(winInformationEntity.getPrizeId());
        //保存到抽奖记录表
        int id = winInformationMapper.insertSelective(winInformationEntity);
        vo.setWinId(winInformationEntity.getId());
        return Result.succeedWith(JSON.toJSONString(vo, SerializerFeature.UseSingleQuotes), CodeEnum.SUCCESS.getCode(), null);
    }

    /**
     * 查询配置信息
     *
     * @param accountId
     * @return
     */
    public Result queryActivitySet(String accountId, Integer orgId) {

        ActivityEntity activityEntity = activityMapper.queryActivitySet(orgId);
        if (activityEntity == null) {
            return Result.failed("系统错误！");
        }

        //查询剩余抽奖次数
        WinInformationEntity winInformation = new WinInformationEntity();
        winInformation.setAccountId(accountId);
        winInformation.setActivityId(activityEntity.getId());
        winInformation.setOrgId(orgId);
        int count = winInformationMapper.selectCount(winInformation);
        if (count >= activityEntity.getTimes()) {
            return Result.failed("您的抽奖机会已经用光了哦！");
        }
        //获取配置
        List<PrizeOptionsEntity> list = prizeOptionsMapper.selectByActivityId(activityEntity.getId());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", activityEntity.getId());
        jsonObject.put("startTime", String.valueOf(activityEntity.getStartTime()));
        jsonObject.put("endTime", String.valueOf(activityEntity.getEndTime()));
        jsonObject.put("leftTimes", activityEntity.getTimes() - count);
        JSONObject sonObject;
        JSONArray jsonArray = new JSONArray();
        for (PrizeOptionsEntity entity : list) {
            sonObject = new JSONObject();
            sonObject.put("id", entity.getId());
            sonObject.put("name", entity.getName());
            sonObject.put("level", entity.getLevel());
            jsonArray.add(sonObject);
        }
        jsonObject.put("props", jsonArray);

        return Result.succeedWith(jsonObject, CodeEnum.SUCCESS.getCode(), null);
    }


    /**
     * 保存用户输入的信息
     *
     * @param entity
     * @return
     */
    public Result savePrizeLog(WinInformationEntity entity) {
        int i = winInformationMapper.updateWinInformation(entity);
        if (i == 0) {
            return Result.failed("用户信息不存在！");
        }
        PrizeOptionsEntity prizeOptionsEntity = prizeOptionsMapper.queryOne(entity.getPrizeId());
        if (prizeOptionsEntity == null) {
            return Result.failed("系统错误！");
        }

        String priceCode = winInformationMapper.selectCode(entity.getId());
        return Result.succeedWith("{priceCode:'" + priceCode + "',name:'" + prizeOptionsEntity.getName() + "',type:'" + prizeOptionsEntity.getType() + "',adress:'" + prizeOptionsEntity.getExt() + "'}", CodeEnum.SUCCESS.getCode(), null);
    }


    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime   当前时间
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return
     * @author jqlin
     */
    private boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {

    }
}
