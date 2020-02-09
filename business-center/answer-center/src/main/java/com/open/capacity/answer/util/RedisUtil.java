package com.open.capacity.answer.util;

import com.open.capacity.answer.feign.UserClient;
import com.open.capacity.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51 获取用户信息
 */
@Component
public class RedisUtil {

    private static RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
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
            return null;
        }
        String redisKey = type + "_" + key + "_" + orgId;
        if (redisTemplate.opsForValue().get(redisKey) == null) {
            //从数据库获取 调用
            UserClient userClient = SpringUtils.getBean(UserClient.class);
            Map<String, Object> params = new HashMap<>();
            params.put("type", type);
            params.put("orgId", orgId);
            params.put("key", key);
            String value = userClient.getConfig(params);
            if (StringUtils.isNotEmpty(value)) {
                redisTemplate.opsForValue().set(redisKey, value);
            } else {
                throw new RuntimeException();
            }
        }
        return (String) redisTemplate.opsForValue().get(redisKey);
    }
}
