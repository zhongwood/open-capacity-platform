package com.open.capacity.answer.feign;

import com.open.capacity.answer.controller.FileController;
import com.open.capacity.model.system.VideoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient("file-center")
@Component
public class FileClient {

    @Autowired
    FileController fileController;

    /**
     * feign rpc访问远程/users-anon/login接口
     *
     * @param videoId
     * @return
     */
//    @GetMapping(value = "/file-anon/getVideoInfo")
    public VideoInfo findByVideoId(@RequestParam("videoId") String videoId, @RequestParam("orgId") Integer orgId) {
        return fileController.findByVideoId(videoId, orgId);
    }
}
