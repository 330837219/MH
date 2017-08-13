package com.waiter.mh.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口调用返回结果类
 * Created by shiju on 2017/8/8.
 */

public class ResponseInfo<T> {

    /**
     * 调用接口返回结果，S:成功，E：失败
     */
    private String Status;
    /**
     * 成功或者失败描述
     */
    private String Description;
    /**
     * 返回数据
     */
    public List<T> Datas = new ArrayList<T>();

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<T> getDatas() {
        return Datas;
    }

    public void setDatas(List<T> datas) {
        Datas = datas;
    }

    @Override
    public String toString() {
        return "ResponseInfo{" +
                "Status='" + Status + '\'' +
                ", Description='" + Description + '\'' +
                ", Datas=" + Datas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResponseInfo<?> that = (ResponseInfo<?>) o;

        if (Status != null ? !Status.equals(that.Status) : that.Status != null) return false;
        if (Description != null ? !Description.equals(that.Description) : that.Description != null)
            return false;
        return Datas != null ? Datas.equals(that.Datas) : that.Datas == null;

    }

    @Override
    public int hashCode() {
        int result = Status != null ? Status.hashCode() : 0;
        result = 31 * result + (Description != null ? Description.hashCode() : 0);
        result = 31 * result + (Datas != null ? Datas.hashCode() : 0);
        return result;
    }
}
