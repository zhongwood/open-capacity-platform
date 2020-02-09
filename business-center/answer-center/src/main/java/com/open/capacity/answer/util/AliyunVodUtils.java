package com.open.capacity.answer.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import com.open.capacity.sysenum.SystemConfigEnum;
import com.open.capacity.utils.SysUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 阿里云vod工具类
 */
public class AliyunVodUtils {

    private static Logger log = LoggerFactory.getLogger(AliyunVodUtils.class);

    /**
     * 初始化客户端
     */
    public static DefaultAcsClient initVodClient(Integer orgId) {
        if (null == orgId) {
            orgId = SysUserUtil.getLoginOrgId();
        }
        String accessKey = RedisUtil.getConfig(SystemConfigEnum.ALIYUN_OSS_ACCESSKEY.getType(), SystemConfigEnum.ALIYUN_OSS_ACCESSKEY.getKey(), orgId);
        String accessKeySecret = RedisUtil.getConfig(SystemConfigEnum.ALIYUN_OSS_ACCESSKEYSECRET.getType(), SystemConfigEnum.ALIYUN_OSS_ACCESSKEYSECRET.getKey(), orgId);
        String regionId = RedisUtil.getConfig(SystemConfigEnum.ALIYUN_OSS_REGIONID.getType(), SystemConfigEnum.ALIYUN_OSS_REGIONID.getKey(), orgId);
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKey, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }

    /**
     * 获取视频上传地址和凭证
     */
    public static HashMap<String, Object> createUploadVideo(Map<String, Object> param) {
        DefaultAcsClient client = initVodClient(null);

        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(param.get("title").toString());
        request.setFileName(param.get("fileName").toString());
        HashMap<String, Object> map = new HashMap<>();
        try {
            CreateUploadVideoResponse response = client.getAcsResponse(request);
            map.put("Code", "0");
            map.put("VideoId", response.getVideoId());
            map.put("UploadAddress", response.getUploadAddress());
            map.put("UploadAuth", response.getUploadAuth());
            map.put("RequestId", response.getRequestId());
        } catch (ClientException e) {
            e.printStackTrace();
            map.put("Code", "1");
            map.put("ErrorMessage", e.getLocalizedMessage());
        }

        return map;
    }

    /**
     * 刷新视频上传凭证
     */
    public static Map<String, Object> refreshUploadVideo(Map<String, Object> param) {
        DefaultAcsClient client = initVodClient(null);
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId(param.get("videoId").toString());

        Map<String, Object> map = new HashMap<>();
        try {
            RefreshUploadVideoResponse response = client.getAcsResponse(request);
            map.put("Code", "0");
            map.put("VideoId", response.getVideoId());
            map.put("UploadAddress", response.getUploadAddress());
            map.put("UploadAuth", response.getUploadAuth());
        } catch (ClientException e) {
            e.printStackTrace();
            map.put("Code", "1");
            map.put("ErrorMessage", e.getLocalizedMessage());
        }
        return map;
    }

    /*获取播放凭证函数*/
    public static Map<String, Object> getVideoPlayAuth(Map<String, Object> param) {
        DefaultAcsClient client = initVodClient(null);
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        request.setVideoId(param.get("videoId").toString());
        request.setAuthInfoTimeout(Long.valueOf(param.get("authInfoTimeout").toString()));
        Map<String, Object> map = new HashMap<>();
        try {
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);
            map.put("Code", "0");
            map.put("PlayAuth", response.getPlayAuth());
            map.put("RequestId", response.getRequestId());
            map.put("VideoId", response.getVideoMeta().getVideoId());
            map.put("Title", response.getVideoMeta().getTitle());
            map.put("CoverUrl", response.getVideoMeta().getCoverURL());
        } catch (ClientException e) {
            e.printStackTrace();
            map.put("Code", "1");
            map.put("ErrorMessage", e.getLocalizedMessage());
        }
        return map;
    }


    /**
     * 获取视频信息
     *
     * @param videoId
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getVideoInfo(String videoId) throws Exception {

        DefaultAcsClient client = initVodClient(null);
        Map<String, Object> map = new HashMap<>();
        try {
            GetVideoInfoRequest request = new GetVideoInfoRequest();
            request.setVideoId(videoId);
            GetVideoInfoResponse response = client.getAcsResponse(request);
            map.put("title", response.getVideo().getTitle());
            map.put("description", response.getVideo().getDescription());
            map.put("size", response.getVideo().getSize());
            map.put("coverUrl", response.getVideo().getCoverURL());
        } catch (Exception e) {
            e.printStackTrace();
            map.put("Code", "1");
            map.put("ErrorMessage", e.getLocalizedMessage());
        }
        return map;
    }


    /*获取播放地址函数*/
    public static GetPlayInfoResponse getPlayInfo(String videoId, Integer orgId) {
        try {
            DefaultAcsClient client = initVodClient(orgId);
            GetPlayInfoRequest request = new GetPlayInfoRequest();
            request.setVideoId(videoId);
            return client.getAcsResponse(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除视频
     *
     * @param videoId
     * @return
     * @throws Exception
     */
    public static boolean deleteVideo(String videoId) throws Exception {

        DefaultAcsClient client = initVodClient(null);
        try {

            DeleteVideoRequest request = new DeleteVideoRequest();
            //支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoId);
            DeleteVideoResponse response = client.getAcsResponse(request);
            if (!videoId.equals(response.getRequestId())) {
                return false;
            }
            return true;
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
