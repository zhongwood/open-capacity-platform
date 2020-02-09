package com.open.capacity.answer.controller;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.answer.dao.UserInfoDao;
import com.open.capacity.answer.entity.UserInfo;
import com.open.capacity.answer.service.UserInfoService;
import com.open.capacity.answer.util.RedisUtil;
import com.open.capacity.commons.CodeEnum;
import com.open.capacity.commons.Result;
import com.open.capacity.sysenum.SystemConfigEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信鉴权中心
 */
@RestController
public class WechatAuthController {

    private static Logger logger = LoggerFactory.getLogger(WechatAuthController.class);


    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private UserInfoService userInfoService;

    @LogAnnotation(module = "third-auth-center", recordRequestParam = false)
    @PostMapping("/auth/wechatcode")
    public Result getRequestCodeUrl(@RequestParam Map<String, Object> params) {
        Integer orgId = Integer.parseInt((String) params.get("orgId"));
        String setId = (String) params.get("setId");
        if (orgId == null) {
            return Result.failed("orgId错误！请联系管理员");
        }
        String url = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
                RedisUtil.getConfig(SystemConfigEnum.WECHAT_APPID.getType(), SystemConfigEnum.WECHAT_APPID.getKey(), orgId), (String) params.get("redirectUrl") + "?setId=" + setId + "%26orgId=" + orgId, "snsapi_userinfo", "xxxx_state");
        return Result.succeedWith(url, CodeEnum.SUCCESS.getCode(), null);

    }

    @LogAnnotation(module = "third-auth-center", recordRequestParam = false)
    @PostMapping("/auth/wechat")
    public Result auth(@RequestParam Map<String, Object> params) {
        Integer orgId = Integer.parseInt((String) params.get("orgId"));
        if (orgId == null) {
            return Result.failed("orgId错误！请联系管理员");
        }
        String code = (String) params.get("code");
        Map<String, String> result = getUserInfoAccessToken(code, orgId);//通过这个code获取access_token
        String openId = result.get("openid");
        if (StringUtils.isNotEmpty(openId)) {
            Map<String, String> userInfo = getUserInfo(result.get("access_token"), openId, orgId);//使用access_token获取用户信息
            return Result.succeedWith(userInfo, CodeEnum.SUCCESS.getCode(), null);
        }
        return Result.succeedWith(null, CodeEnum.ERROR.getCode(), "授权失败！");
    }

    public Map<String, String> getUserInfoAccessToken(String code, Integer orgId) {
        JsonObject object = null;
        Map<String, String> data = new HashMap();
        try {
            String url = String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                    RedisUtil.getConfig(SystemConfigEnum.WECHAT_APPID.getType(), SystemConfigEnum.WECHAT_APPID.getKey(), orgId), RedisUtil.getConfig(SystemConfigEnum.WECHAT_APPSECRET.getType(), SystemConfigEnum.WECHAT_APPSECRET.getKey(), orgId), code);
            logger.info("request accessToken from url: {}", url);
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String tokens = EntityUtils.toString(httpEntity, "utf-8");
            Gson token_gson = new Gson();
            object = token_gson.fromJson(tokens, JsonObject.class);
            logger.info("request accessToken success. [result={}]", object);
            data.put("openid", object.get("openid").toString().replaceAll("\"", ""));
            data.put("access_token", object.get("access_token").toString().replaceAll("\"", ""));
        } catch (Exception ex) {
            logger.error("fail to request wechat access token. [error={}]", ex);
        }
        return data;
    }

    public Map<String, String> getUserInfo(String accessToken, String openId, Integer orgId) {
        Map<String, String> data = new HashMap();
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        JsonObject userInfo = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(httpEntity, "utf-8");
            Gson token_gson = new Gson();
            userInfo = token_gson.fromJson(response, JsonObject.class);
            data.put("openid", userInfo.get("openid").toString().replaceAll("\"", ""));
            data.put("nickname", userInfo.get("nickname").toString().replaceAll("\"", ""));
            data.put("city", userInfo.get("city").toString().replaceAll("\"", ""));
            data.put("province", userInfo.get("province").toString().replaceAll("\"", ""));
//            data.put("mobile", (String) userInfo.get("mobile").replaceAll("\"", ""));
            data.put("country", userInfo.get("country").toString().replaceAll("\"", ""));
            data.put("headimgurl", userInfo.get("headimgurl").toString().replaceAll("\"", ""));
            data.put("orgId", String.valueOf(orgId));


            //先去查询一下
            UserInfo params = new UserInfo();
            params.setOrgId(orgId);
            params.setUnionId(userInfo.get("openid").toString().replaceAll("\"", ""));
            UserInfo info = userInfoDao.getClient(params);
            if (info != null) {
                return data;
            }
            userInfoService.addUser(data);
        } catch (Exception ex) {
            logger.error("fail to request wechat user info. [error={}]", ex);
        }
        return data;
    }
}
