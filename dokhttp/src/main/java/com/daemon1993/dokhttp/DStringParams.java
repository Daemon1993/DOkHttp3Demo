package com.daemon1993.dokhttp;

import android.content.Context;

import java.util.Objects;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Daemon1993 on 16/4/12.
 */
public class DStringParams {

    private Request.Builder builder;

    public  Request.Builder getBuilder(){
        return builder;
    }

    public DStringParams(){
         builder = new Request.Builder();
    }
    public Request.Builder putString(String key ,String value){
        return builder.addHeader(key,value);
    }

    public Request.Builder setTag(Object tag){
        return builder.tag(tag);
    }

    public Request.Builder setUrl(String url){
        return builder.url(url);
    }

    public Request build(){
        return builder.build();
    }

    /**
     * 生成String字符串 上传的Request
     * @param context
     * @param content
     * @param url
     * @return
     */
    public Request initRequestString(Context context, String content, String url){
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(RequestBody.create(DConstant.MEDIA_TYPE_MARKDOWN, content))
                .build();

        return request;
    }



}
