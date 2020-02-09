package com.open.capacity.answer.controller;

import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.open.capacity.annotation.log.LogAnnotation;
import com.open.capacity.answer.dao.VideoFilesMapper;
import com.open.capacity.answer.entity.VideoFilesEntity;
import com.open.capacity.answer.service.VideoFilesService;
import com.open.capacity.answer.util.AliyunVodUtils;
import com.open.capacity.commons.PageResult;
import com.open.capacity.commons.Result;
import com.open.capacity.model.system.VideoInfo;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 作者 owen E-mail: 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 * 文件上传 同步oss db双写 目前仅实现了阿里云,七牛云
 * 参考src/main/view/upload.html
 */
@RestController
public class FileController {

    private static Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private VideoFilesService filesService;

    @Autowired
    private VideoFilesMapper videoFilesMapper;

    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @PostMapping("/createUploadVideo")
    public Map<String, Object> createUploadVideo(@ApiIgnore @RequestBody Map<String, Object> params) {

        // 根据titile 和name获取
        return AliyunVodUtils.createUploadVideo(params);
    }

    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @PostMapping("/refreshUploadVideo")
    public Map<String, Object> refreshUploadVideo(@ApiIgnore @RequestBody Map<String, Object> params) {

        return AliyunVodUtils.refreshUploadVideo(params);
    }


    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @PostMapping("/getVideoPlayAuth")
    public Map<String, Object> getVideoPlayAuth(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("authInfoTimeout", 3000);//播放凭证过期时间。取值范围：100~3000。

        return AliyunVodUtils.getVideoPlayAuth(params);
    }

    //************************************************************


    /**
     * 文件上传
     * 根据fileType选择上传方式
     *
     * @param params
     * @return
     * @throws Exception
     */
    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @PostMapping("/uploadBack")
    public VideoFilesEntity upload(@RequestBody Map<String, Object> params) throws Exception {

        VideoFilesEntity entity = new VideoFilesEntity();

        entity.setVideoId((String) params.get("videoId"));
        entity.setVideoSize(String.valueOf((Integer) params.get("videoSize")));
        entity.setVideoName((String) params.get("videoName"));
        entity.setVideoType((String) params.get("videoType"));
        entity.setCreateTime(new Date());
        entity.setUpdateTime(new Date());
        filesService.upload(entity);
        return entity;
    }

    /**
     * 文件删除
     *
     * @param id
     */
    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:del')")
    @DeleteMapping("/files/{id}")
    public Result delete(@PathVariable String id) {

        VideoFilesEntity fileInfo = new VideoFilesEntity();
        fileInfo.setId(Integer.parseInt(id));
        try {
            if (AliyunVodUtils.deleteVideo(id)) {
                filesService.delete(fileInfo);
            }
            return Result.succeed("操作成功");
        } catch (Exception ex) {
            return Result.failed("操作失败");
        }
    }

    /**
     * 文件查询
     *
     * @param params
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasAuthority('thirdUser:query')")
    @GetMapping("/files")
    public PageResult<VideoFilesEntity> findFiles(@RequestParam Map<String, Object> params) throws JsonProcessingException {

        return filesService.findList(params);

    }


    /**
     * 文件标题
     *
     * @return
     * @throws JsonProcessingException
     */
    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @PreAuthorize("hasAuthority('file:query')")
    @GetMapping("/fileTitles")
    public List<VideoFilesEntity> fileTiles() {
        return filesService.fileTiles();
    }

    /**
     * 查詢文件
     *
     * @return
     * @throws JsonProcessingException
     */
    @LogAnnotation(module = "file-center", recordRequestParam = false)
    @ApiOperation(value = "根据视频id查询绑定的视频信息")
    @GetMapping(value = "/file-anon/getVideoInfo")
    public VideoInfo findByVideoId(String videoId, Integer orgId) {
        // 根据id查询信息
        Map<String, Object> params = new HashMap<>();
        params.put("orgId", orgId);
        params.put("videoId", videoId);
        VideoFilesEntity entity = videoFilesMapper.selectByPrimaryKey(params);

        if (entity == null) {
            return null;
        }
        //封装返回值
        VideoInfo result = new VideoInfo();
        result.setVideoId(videoId);
        result.setVideoPath(entity.getVideoPath());
        result.setThumbnailPath(entity.getThumbnailPath());

        //获取视频播放地址
        GetPlayInfoResponse response = AliyunVodUtils.getPlayInfo(videoId, orgId);
        result.setPlayURL(response.getPlayInfoList().get(0).getPlayURL());
        return result;
    }
}
