package com.daemon1993.dokhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;


import com.daemon1993.dokhttp.listener.UIProgressRequestListener;
import com.daemon1993.dokhttp.listener.UIProgressResponseListener;
import com.daemon1993.dokhttp.progress.ProgressHelper;
import com.daemon1993.dokhttp.progress.ProgressRequestBody;
import com.daemon1993.dokhttp.tools.DFileUtil;

import com.google.gson.Gson;

import java.io.IOException;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DOkHttp {

    private static final String TAG = DOkHttp.class.getName();
    private Handler mainHanlder;
    public OkHttpClient mOkHttpClient;

    private Gson gson;
    private Headers responseHeaders;
    private Call call;
    private Response response;

    /**
     * 初始化工作
     *
     * @param context
     */
    public static void init(Context context) {
        DFileUtil.getDiskCacheDir(context);

    }


    /**
     * 自定义缓存路径
     *
     * @param cachePath
     * @return
     */
    public void cachePath(String cachePath) {
        if (TextUtils.isEmpty(cachePath)) {
            return;
        }
        DFileUtil.setCachePath(cachePath);

    }

    private static class OkHttpUtilHolder {
        public static DOkHttp mInstance = new DOkHttp();

    }

    private DOkHttp() {
        mOkHttpClient = new OkHttpClient();


        gson = new Gson();

        //更新UI线程
        mainHanlder = new Handler(Looper.getMainLooper());

    }


    public Gson getGson() {
        return gson;
    }

    public static DOkHttp getInstance() {
        return OkHttpUtilHolder.mInstance;
    }



	/*------------------------------------------------Daemon------------------------------------------------------------------------*/


    public Headers getHeader(Response response) {
        return responseHeaders;
    }

    /**
     * GET请求 异步
     *
     * @param request
     * @param myCallBack
     */
    public void getData4ServerAsync(Request request, final MyCallBack myCallBack) {


        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                logFailure(call, e);
                myCallBack.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json;
                try {
                    json = getJsonContent(response);
                    mainHanlder.post(new Runnable() {
                        @Override
                        public void run() {
                            myCallBack.onResponse(json);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    /**
     * 读取Json 请求线程 后UI线程回调
     *
     * @param response
     * @return
     * @throws IOException
     */
    @NonNull
    private String getJsonContent(Response response) throws IOException {
        responseHeaders = response.headers();
        final String json = response.body().string();

        Log.e(TAG,json);

        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return json;
    }


    /**
     *
     * 异步回调方式 post请求  自定义回调接口  结果运行在UI线程
     * Map 字符串post 服务器
     *
     * @param request
     * @throws Exception
     */
    public void postData2Server(Request request, final MyCallBack myCallBack) {
        try {
            getInstance().mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    logFailure(call, e);
                    myCallBack.onFailure(call, e);
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        final String json = getJsonContent(response);
                        mainHanlder.post(new Runnable() {
                            @Override
                            public void run() {
                                myCallBack.onResponse(json);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 打印请求失败信息
     *
     * @param call
     * @param e
     */
    private void logFailure(Call call, IOException e) {
        Log.e(TAG, call.request().url() + "--->" + e.getMessage());
    }


    /**
     * UI更新
     */
    public interface UIchangeListener {
        void progressUpdate(long bytesWrite, long contentLength, boolean done);
    }



    /**
     * 下载监听
     *
     * @param request
     * @param myCallBack
     * @param uIchangeListener
     */
    public void download4ServerListener(Request request, final MyCallBack_Progress myCallBack,
                                        final UIchangeListener uIchangeListener) {

        ProgressHelper.addProgressResponseListener(getInstance().mOkHttpClient, new UIProgressResponseListener() {
            @Override
            public void onUIResponseProgress(long bytesRead, long contentLength, boolean done) {
                uIchangeListener.progressUpdate(bytesRead, contentLength, done);
            }
        }).newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                logFailure(call, e);
                myCallBack.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                myCallBack.onResponse(response);
            }
        });


    }

    /**
     * 数据回调接口
     */
    public interface MyCallBack {
        void onFailure(Call call, IOException e);

        void onResponse(String json);
    }

    /**
     * 回调进度条
     */
    public interface MyCallBack_Progress {
        void onFailure(Call call, IOException e);

        void onResponse(Response response);
    }

    /**
     * 根据Tag取消Request
     *
     * @param tag
     */
    public void cancelCallsWithTag(Object tag) {
        if (tag == null) {
            return;
        }

        synchronized (mOkHttpClient.dispatcher().getClass()) {
            for (Call call : mOkHttpClient.dispatcher().queuedCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }

            for (Call call : mOkHttpClient.dispatcher().runningCalls()) {
                if (tag.equals(call.request().tag())) call.cancel();
            }
        }
    }


}