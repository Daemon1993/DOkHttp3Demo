package com.daemon1993.dokhttp.params;

import android.content.Context;

import com.daemon1993.dokhttp.DConstant;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Daemon1993 on 16/4/12.
 */
public class DStringParams extends DBaseParams<DStringParams> {

    /**
     * 生成String字符串 上传的Request
     *
     * @param context
     * @param content
     * @param url
     * @return
     */
    public Request getStringRequest(Context context, String content, String url) {

        return builder.post(RequestBody.create(DConstant.MEDIA_TYPE_MARKDOWN, content))
                .tag(content)
                .url(url)
                .build();
    }


    public Request.Builder post(RequestBody requestBody) {
        return builder.post(requestBody);
    }


    @Override
    public DStringParams returnThis() {
        return this;
    }
}
