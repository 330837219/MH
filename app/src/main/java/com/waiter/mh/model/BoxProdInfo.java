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
    public String getSCAN_TIME() {
        return SCAN_TIME;
    }

    public void setSCAN_TIME(String SCAN_TIME) {
        this.SCAN_TIME = SCAN_TIME;
    }
    /**
     * 扫描时间
     */
    private String SCAN_TIME;
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

    @Override
    public String toString() {
        return "BoxProdInfo{" +
                "SCAN_TIME='" + SCAN_TIME + '\'' +
                ", LINE_NO=" + LINE_NO +
                ", PROD_CODE='" + PROD_CODE + '\'' +
                ", BOX_CODE='" + BOX_CODE + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoxProdInfo that = (BoxProdInfo) o;

        if (LINE_NO != that.LINE_NO) return false;
        if (SCAN_TIME != null ? !SCAN_TIME.equals(that.SCAN_TIME) : that.SCAN_TIME != null)
            return false;
        if (PROD_CODE != null ? !PROD_CODE.equals(that.PROD_CODE) : that.PROD_CODE != null)
            return false;
        return BOX_CODE != null ? BOX_CODE.equals(that.BOX_CODE) : that.BOX_CODE == null;

    }

    @Override
    public int hashCode() {
        int result = SCAN_TIME != null ? SCAN_TIME.hashCode() : 0;
        result = 31 * result + LINE_NO;
        result = 31 * result + (PROD_CODE != null ? PROD_CODE.hashCode() : 0);
        result = 31 * result + (BOX_CODE != null ? BOX_CODE.hashCode() : 0);
        return result;
    }
}
