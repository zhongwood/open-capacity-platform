package com.open.capacity.utils;

import com.open.capacity.model.system.LoginAppUser;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51 获取用户信息
 */
@Component
public class SysUserUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        SysUserUtil.redisTemplate = redisTemplate;
    }


    /**
     * 获取登陆的 LoginAppUser
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static LoginAppUser getLoginAppUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof OAuth2Authentication) {
            OAuth2Authentication oAuth2Auth = (OAuth2Authentication) authentication;
            authentication = oAuth2Auth.getUserAuthentication();

            if (authentication instanceof UsernamePasswordAuthenticationToken) {
                UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
                return (LoginAppUser) authenticationToken.getPrincipal();
            } else if (authentication instanceof PreAuthenticatedAuthenticationToken) {
                // 刷新token方式
                PreAuthenticatedAuthenticationToken authenticationToken = (PreAuthenticatedAuthenticationToken) authentication;
                return (LoginAppUser) authenticationToken.getPrincipal();

            }
        }

        return null;
    }

    public static Integer getLoginOrgId() {
        return getLoginAppUser().getOrgId();
    }


    /**
     * 获取config
     *
     * @param type
     * @param key
     * @return
     */
    public static String getConfig(String type, String key, Integer orgId) {
        if (null == orgId) {
            orgId = getLoginAppUser().getOrgId();
        }
        String redisKey = type + "_" + key + "_" + orgId;
        if (redisTemplate.opsForValue().get(redisKey) == null) {
            return null;
        }
        return (String) redisTemplate.opsForValue().get(redisKey);
    }
}
