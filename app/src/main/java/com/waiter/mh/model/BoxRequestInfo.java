package com.waiter.mh.model;

/**
 * 盒子网点扫描上架,回收提交数据类
 */
public class BoxRequestInfo {
    public String getBOX_CODE() {
        return BOX_CODE;
    }

    public void setBOX_CODE(String BOX_CODE) {
        this.BOX_CODE = BOX_CODE;
    }

    public String getWAREH_CODE() {
        return WAREH_CODE;
    }

    public void setWAREH_CODE(String WAREH_CODE) {
        this.WAREH_CODE = WAREH_CODE;
    }

    public String getUSER_CODE() {
        return USER_CODE;
    }

    public void setUSER_CODE(String USER_CODE) {
        this.USER_CODE = USER_CODE;
    }

    /**
     * 盒子编码
     */
    private String BOX_CODE;
    /**
     * 网点编码
     */
    private String WAREH_CODE;
    /**
     * 操作用户
     */
    private String USER_CODE;


}
