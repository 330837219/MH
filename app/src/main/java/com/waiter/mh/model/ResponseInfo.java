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
}
