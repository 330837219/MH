package com.waiter.mh.utils;

/**
 * Created by shiju on 2017/8/8.
 */

public class Config {
    public static final String CONNECT_ADDRESS = "http://115.29.165.110:8085/RfService.svc/";//接口调用地址
    public static final String ACCOUNT_PASSWORD = "account_password";
    public static final String USER_CODE = "user_code";
    public static final String USER_PASS = "user_pass";
    public static final String STATUS_SUCCESS = "S";//返回结果成功标识
    public static final String STATUS_FAIL = "E";//返回结果失败的标识

    public static final int MSG_SUCCESS = 1;//调用接口成功
    public static final int MSG_EXCEPTION = -1;//调用接口异常

    public static final String SCAN_ACTION = "com.waiter.mh.action.SCAN_CODE_BROADCAST";//扫描二维码发送的广播action
    public static final String START_SCAN = "start_scan";

    public static final int CONTINUOUS_SCAN = 1001;//连续扫描
    public static final int ONCE_SCAN = 1002;//扫描一次

    public static final  String BOX_PREFIX="BX";//盒子条码前缀，为了过滤识别出来的错误乱码
    public static final  String PROD_PREFIX="PD";//商品条码前缀，为了过滤识别出来的错误乱码
    public static final  String WAREH_PREFIX="SH";//网点，仓库条码前缀，为了过滤识别出来的错误乱码
    public static final  String PKN_PREFIX="PK";//分拣单条码前缀，为了过滤识别出来的错误乱码
}
