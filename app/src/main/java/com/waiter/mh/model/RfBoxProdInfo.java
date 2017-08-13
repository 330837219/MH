package com.waiter.mh.model;

/**
 * 盒子商品关系接口提交类
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

    @Override
    public String toString() {
        return "RfBoxProdInfo{" +
                "BOX_CODE='" + BOX_CODE + '\'' +
                ", PKN_CODE='" + PKN_CODE + '\'' +
                ", PROD_CODE='" + PROD_CODE + '\'' +
                ", USER_CODE='" + USER_CODE + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RfBoxProdInfo that = (RfBoxProdInfo) o;

        if (BOX_CODE != null ? !BOX_CODE.equals(that.BOX_CODE) : that.BOX_CODE != null)
            return false;
        if (PKN_CODE != null ? !PKN_CODE.equals(that.PKN_CODE) : that.PKN_CODE != null)
            return false;
        if (PROD_CODE != null ? !PROD_CODE.equals(that.PROD_CODE) : that.PROD_CODE != null)
            return false;
        return USER_CODE != null ? USER_CODE.equals(that.USER_CODE) : that.USER_CODE == null;

    }

    @Override
    public int hashCode() {
        int result = BOX_CODE != null ? BOX_CODE.hashCode() : 0;
        result = 31 * result + (PKN_CODE != null ? PKN_CODE.hashCode() : 0);
        result = 31 * result + (PROD_CODE != null ? PROD_CODE.hashCode() : 0);
        result = 31 * result + (USER_CODE != null ? USER_CODE.hashCode() : 0);
        return result;
    }
}
