package com.waiter.mh.model;

/**
 * 盒子商品关系接口提交类
 * Created by shiju on 2017/8/12.
 */

public class RfBoxProdInfo {
    /**
     * 盒子编码
     */
    private String BOX_CODE;
    /**
     * 分拣单编码
     */
    private String PKN_CODE;
    /**
     * 商品编码
     */
    private String PROD_CODE;
    /**
     * 登录用户账号
     */
    private String USER_CODE;

    public String getBOX_CODE() {
        return BOX_CODE;
    }

    public void setBOX_CODE(String BOX_CODE) {
        this.BOX_CODE = BOX_CODE;
    }

    public String getPKN_CODE() {
        return PKN_CODE;
    }

    public void setPKN_CODE(String PKN_CODE) {
        this.PKN_CODE = PKN_CODE;
    }

    public String getPROD_CODE() {
        return PROD_CODE;
    }

    public void setPROD_CODE(String PROD_CODE) {
        this.PROD_CODE = PROD_CODE;
    }

    public String getUSER_CODE() {
        return USER_CODE;
    }

    public void setUSER_CODE(String USER_CODE) {
        this.USER_CODE = USER_CODE;
    }

}
