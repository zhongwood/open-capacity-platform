package com.open.capacity.sysenum;

public enum SystemConfigEnum {

    WECHAT_APPID("WECHAT", "mpAppId", "微信appid"),

    WECHAT_APPSECRET("WECHAT", "mpAppSecret", "微信secret"),

    ALIYUN_OSS_ACCESSKEY("VOD", "accessKey", "阿里accessKey"),

    ALIYUN_OSS_ACCESSKEYSECRET("VOD", "accessKeySecret", "阿里accessKeySecret"),

    ALIYUN_OSS_REGIONID("VOD", "regionId", "阿里regionId"),

    ALIYUN_OSS_DOMAIN("VOD", "domain", "阿里domain");

    private String type;
    private String key;

    private SystemConfigEnum(String type, String key, String desc) {
        this.type = type;
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
