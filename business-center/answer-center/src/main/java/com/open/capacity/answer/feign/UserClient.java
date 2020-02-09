package com.open.capacity.answer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient("auth-server")
@Component
public interface UserClient {

    @GetMapping(value = "/services/users-anon/getConfig")
    String getConfig(@RequestParam Map<String, Object> params);
}
