package com.waiter.mh.model;

/**
 * app版本信息类
 */
public class AppVersionInfo {

    public int getVERSION_CODE() {
        return VERSION_CODE;
    }

    public void setVERSION_CODE(int VERSION_CODE) {
        this.VERSION_CODE = VERSION_CODE;
    }

    public String getVERSION_NAME() {
        return VERSION_NAME;
    }

    public void setVERSION_NAME(String VERSION_NAME) {
        this.VERSION_NAME = VERSION_NAME;
    }

    public String getDOWNLOAD_URL() {
        return DOWNLOAD_URL;
    }

    public void setDOWNLOAD_URL(String DOWNLOAD_URL) {
        this.DOWNLOAD_URL = DOWNLOAD_URL;
    }

    public String getVERSION_DESC() {
        return VERSION_DESC;
    }

    public void setVERSION_DESC(String VERSION_DESC) {
        this.VERSION_DESC = VERSION_DESC;
    }

    public String getPACKAGE_NAME() {
        return PACKAGE_NAME;
    }

    public void setPACKAGE_NAME(String PACKAGE_NAME) {
        this.PACKAGE_NAME = PACKAGE_NAME;
    }

    /**
     * 版本编号
     */
    private int VERSION_CODE;
    /**
     * 版本名称
     */
    private String VERSION_NAME;
    /**
     * APP下载地址
     */
    private String DOWNLOAD_URL;
    /**
     * 版本描述
     */
    private String VERSION_DESC;
    /**
     * 包名
     */
    private String PACKAGE_NAME;
}
