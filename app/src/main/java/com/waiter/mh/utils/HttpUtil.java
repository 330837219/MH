package com.waiter.mh.utils;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.waiter.mh.model.RfBoxProdInfo;
import com.waiter.mh.model.BoxRequestInfo;

import java.io.IOException;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by shiju on 2017/7/3.
 */

public class HttpUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType XML = MediaType.parse("application/xml; charset=utf-8");
    private static HttpUtil instance;
    private OkHttpClient client;

    private HttpUtil() {
        client = new OkHttpClient();
    }

    public static HttpUtil getInstance() {
        return instance == null ? instance = new HttpUtil() : instance;
    }

    //网络通信回调接口
    public interface ResultCallback {
        void onResult(String result);
    }

    /**
     * 提交POST请求
     *
     * @param url       地址
     * @param mediaType 请求体文本类型
     * @param content   请求体
     * @param headers   请求头
     * @return
     * @throws IOException
     */
//    public String post(String url, MediaType mediaType, String content, String[] headers) throws IOException {
//        RequestBody body = RequestBody.create(mediaType, content);
//        Request.Builder builder = new Request.Builder().url(url);
//        if (headers != null && headers.length > 0 && headers.length % 2 == 0) {
//            for (int i = 0; i < headers.length - 1; i++)
//                if (i % 2 == 0) builder.addHeader(headers[i], headers[i + 1]);
//        }
//        Request request = builder.post(body).build();
//        Response response = client.newCall(request).execute();
//        return response.body().string();
//    }

    /**
     * 提交GET请求
     *
     * @param url 地址
     * @return
     * @throws IOException
     */
//    public String get(String url,String[] params) throws IOException {
//
//        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
//        for (String p : params) {
//            builder.addPathSegment(p);
//        }
//        HttpUrl route = builder.build();
//        Request request = new Request.Builder().url(route).get().build();
//        Response response = client.newCall(request).execute();
////        boolean iss = response.isSuccessful();
//        return response.body().string();
//    }

    /**
     * @param url            请求URL地址
     * @param params         请求参数
     * @param resultCallback 回调
     */
    public void delete(final String url, String[] params, final ResultCallback resultCallback) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.MSG_RESULT:
                        resultCallback.onResult((String) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        for (String param : params) {
            builder.addPathSegment(param);
        }
        HttpUrl route = builder.build();
        Request request = new Request.Builder().url(route).delete().build();
        try {
            Response response = client.newCall(request).execute();
//            boolean iss = response.isSuccessful();
            String res = response.body().string();
            mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, res));
        } catch (IOException e) {
            e.printStackTrace();
            mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, e.getMessage()));
        }
    }

    /**
     * 提交POST请求
     *
     * @param url            提交URL地址
     * @param params         参数
     * @param content        RequstBody数据
     * @param resultCallback 回调
     */
    public void post(final String url, final String[] params, final String content, final ResultCallback resultCallback) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.MSG_RESULT:
                        resultCallback.onResult((String) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUrl route = null;
                    //有其他参数
                    if (params != null && params.length > 0) {
                        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
                        for (String param : params) {
                            builder.addPathSegment(param);
                        }
                        route = builder.build();
                    }
                    //无其他参数
                    else {
                        route = HttpUrl.parse(url);
                    }
                    Request request = new Request.Builder()
                            .url(route)
                            .post(RequestBody.create(JSON, content))
                            .build();
                    Response response = client.newCall(request).execute();
//                        boolean iss = response.isSuccessful();
                    String res = response.body().string();
                    mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, res));
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, e.getMessage()));
                }
            }
        }).start();
    }

    /**
     * 提交PUT请求
     *
     * @param url            提交URL地址
     * @param params         参数
     * @param content        RequstBody数据
     * @param resultCallback 回调
     */
    public void put(final String url, final String[] params, final String content, final ResultCallback resultCallback) {
        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.MSG_RESULT:
                        resultCallback.onResult((String) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpUrl route = null;
                    //有其他参数
                    if (params != null && params.length > 0) {
                        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
                        for (String param : params) {
                            builder.addPathSegment(param);
                        }
                        route = builder.build();
                    }
                    //无其他参数
                    else {
                        route = HttpUrl.parse(url);
                    }
                    Request request = new Request.Builder()
                            .url(route)
                            .put(RequestBody.create(JSON, content))
                            .build();
                    Response response = client.newCall(request).execute();
//                        boolean iss = response.isSuccessful();
                    String res = response.body().string();
                    mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, res));
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, e.getMessage()));
                }
            }
        }).start();
    }

    /**
     * 提交GET请求
     *
     * @param url
     * @param params
     * @param resultCallback
     */
    public void get(final String url, final String[] params, final ResultCallback resultCallback) {

        final Handler mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case Config.MSG_RESULT:
                        resultCallback.onResult((String) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
                for (String p : params) {
                    builder.addPathSegment(p);
                }
                HttpUrl route = builder.build();
                Request request = new Request.Builder().url(route).get().build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    boolean iss = response.isSuccessful();
                    mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.sendMessage(mHandler.obtainMessage(Config.MSG_RESULT, e.getMessage()));
                }
            }
        }).start();
    }

    /**
     * 登录方法
     *
     * @param userCode       账号
     * @param userPass       密码
     * @param resultCallback 回调
     */
    public void login(String userCode, String userPass, final ResultCallback resultCallback) {
        String url = Config.CONNECT_ADDRESS + "V1.0/Mh/Login/";
        String[] params = new String[]{userCode, userPass};
        get(url, params, new ResultCallback() {
                    @Override
                    public void onResult(String result) {
                        if (resultCallback != null) {
                            resultCallback.onResult(result);
                        }
                    }
                }
        );
    }

    /**
     * 获取服务器新版本信息
     *
     * @param packageName
     * @param resultCallback
     */
    public void getAppVersion(String packageName, final ResultCallback resultCallback) {
        String url = Config.CONNECT_ADDRESS + "V1.0/Mh/GetAppVersion/";
        String[] params = new String[]{packageName};
        get(url, params, new ResultCallback() {
                    @Override
                    public void onResult(String result) {
                        if (resultCallback != null) {
                            resultCallback.onResult(result);
                        }
                    }
                }
        );
    }

    /**
     * 提交盒子商品关联数据
     *
     * @param boxProdLst     盒子商品关系数据
     * @param resultCallback 回调
     */
    public void insertBoxProd(List<RfBoxProdInfo> boxProdLst, final ResultCallback resultCallback) {
        String url = Config.CONNECT_ADDRESS + "V1.0/Mh/InsertBoxProd/";
        String jsonData = new Gson().toJson(boxProdLst);
        post(url, null, jsonData, new ResultCallback() {
            @Override
            public void onResult(String result) {
                if (resultCallback != null) {
                    resultCallback.onResult(result);
                }
            }
        });
    }

    /**
     * 盒子网点扫描上架
     *
     * @param boxReceiveLst  盒子网点数据
     * @param resultCallback 回调
     */
    public void receiveBox(List<BoxRequestInfo> boxReceiveLst, final ResultCallback resultCallback) {
        String url = Config.CONNECT_ADDRESS + "V1.0/Mh/ReceiveBox/";
        String jsonData = new Gson().toJson(boxReceiveLst);
        put(url, null, jsonData, new ResultCallback() {
            @Override
            public void onResult(String result) {
                if (resultCallback != null) {
                    resultCallback.onResult(result);
                }
            }
        });
    }

    /**
     * 盒子网点回收
     *
     * @param boxRecoverLst  盒子网点数据
     * @param resultCallback 回调
     */
    public void recoverBox(List<BoxRequestInfo> boxRecoverLst, final ResultCallback resultCallback) {
        String url = Config.CONNECT_ADDRESS + "V1.0/Mh/RecoverBox/";
        String jsonData = new Gson().toJson(boxRecoverLst);
        put(url, null, jsonData, new ResultCallback() {
            @Override
            public void onResult(String result) {
                if (resultCallback != null) {
                    resultCallback.onResult(result);
                }
            }
        });
    }

    /**
     * 清空盒子商品
     *
     * @param boxClearLst
     * @param resultCallback
     */
    public void clearBoxProd(List<BoxRequestInfo> boxClearLst, final ResultCallback resultCallback) {
        String url = Config.CONNECT_ADDRESS + "V1.0/Mh/ClearBoxProd/";
        String jsonData = new Gson().toJson(boxClearLst);
        put(url, null, jsonData, new ResultCallback() {
            @Override
            public void onResult(String result) {
                if (resultCallback != null) {
                    resultCallback.onResult(result);
                }
            }
        });
    }
}
