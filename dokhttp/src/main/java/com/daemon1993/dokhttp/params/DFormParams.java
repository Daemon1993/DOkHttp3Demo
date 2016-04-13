package com.daemon1993.dokhttp.params;


import android.util.Log;

import com.daemon1993.dokhttp.DConstant;
import com.daemon1993.dokhttp.DOkHttp;
import com.daemon1993.dokhttp.listener.UIProgressRequestListener;
import com.daemon1993.dokhttp.progress.ProgressHelper;
import com.daemon1993.dokhttp.progress.ProgressRequestBody;

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/4/13.
 */
public class DFormParams extends DBaseParams<DFormParams> {

    MultipartBody.Builder mlt_builder;
    private boolean isPost=false;


    public DFormParams() {
        mlt_builder = new MultipartBody.Builder();
        mlt_builder.setType(MultipartBody.FORM);
    }

    /**
     * 添加图片 命名用 "XXX.jpg png 等等"
     *
     * @param key
     * @param fileName
     * @param file
     * @return
     */
    public DFormParams addImage(String key, String fileName, File file) {

        mlt_builder.addFormDataPart(key, fileName, RequestBody.create(DConstant.MEDIA_TYPE_PNG, file));

        return this;
    }

    /**
     * String Map
     *
     * @param key
     * @param value
     * @return
     */
    public DFormParams addString(String key, String value) {
        mlt_builder.addFormDataPart(key, value);
        return this;
    }

    /**
     * 添加普通文件
     *
     * @param key
     * @param fileName
     * @param file
     * @return
     */
    public DFormParams addFile(String key, String fileName, File file) {
        mlt_builder.addFormDataPart(key, fileName, RequestBody.create(DConstant.MEDIA_TYPE_MARKDOWN, file));

        return this;
    }

    public Request build() {
        if(!isPost){
            builder.post(mlt_builder.build());
        }
        return builder.build();
    }

    @Override
    public DFormParams returnThis() {
        return this;
    }


    public DFormParams addProgressListener(final DOkHttp.UIchangeListener uIchangeListener){

        ProgressRequestBody progressRequestBody = ProgressHelper.addProgressRequestListener(mlt_builder.build(), new UIProgressRequestListener() {
            @Override
            public void onUIRequestProgress(long bytesWrite, long contentLength, boolean done) {
                Log.e("progress ",bytesWrite+"---- "+contentLength);
                uIchangeListener.progressUpdate(bytesWrite, contentLength, done);
            }
        });

        builder.post(progressRequestBody);

        isPost=true;
        return this;
    }
}
