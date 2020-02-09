package com.open.capacity.answer.service.impl;


import com.open.capacity.answer.dao.UserInfoDao;
import com.open.capacity.answer.entity.UserInfo;
import com.open.capacity.answer.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service("userInfoServiceImpl")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public void addUser(Map<String, String> userInfo) {
        UserInfo user = new UserInfo();

        // 目前只有微信 1
        user.setThirdType(1);
        user.setUnionId(getRealInfo(userInfo.get("openid")));
        user.setNickName(getRealInfo(userInfo.get("nickname")));
        user.setSex(getRealInfo(userInfo.get("sex")) == null ? null : Integer.parseInt(getRealInfo(userInfo.get("sex"))));
        user.setMobile(getRealInfo(userInfo.get("mobile")));
        user.setCountry(getRealInfo(userInfo.get("country")));
        user.setProvince(getRealInfo(userInfo.get("province")));
        user.setCity(getRealInfo(userInfo.get("city")));
        user.setHeadImgUrl(getRealInfo(userInfo.get("headimgurl")));
        user.setPrivilege(getRealInfo(userInfo.get("privilege")));
        user.setAddress(getRealInfo(userInfo.get("address")));
        user.setOrgId(Integer.parseInt(getRealInfo(userInfo.get("orgId"))));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userInfoDao.insert(user);
    }

    public String getRealInfo(String attr) {
        if ("男".equals(attr)) {
            return "1";
        } else if ("女".equals(attr)) {
            return "0";
        }
        return attr == null ? null : attr;

    }
}
