package com.waiter.mh.model;

/**
 * Created by shiju on 2017/8/12.
 */
public class BoxProdInfo {
    public int getLINE_NO() {
        return LINE_NO;
    }

    public void setLINE_NO(int LINE_NO) {
        this.LINE_NO = LINE_NO;
    }

    public String getPROD_CODE() {
        return PROD_CODE;
    }

    public void setPROD_CODE(String PROD_CODE) {
        this.PROD_CODE = PROD_CODE;
    }

    public String getBOX_CODE() {
        return BOX_CODE;
    }

    public void setBOX_CODE(String BOX_CODE) {
        this.BOX_CODE = BOX_CODE;
    }

    /**
     * 行号数量
     */
    private  int LINE_NO;
    /**
     * 商品编码
     */
    private String PROD_CODE;
    /**
     * 盒子编码
     */
    private String BOX_CODE;

}
