package com.daemon1993.dokhttp.params;

import com.daemon1993.dokhttp.params.DFormParams;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2016/4/13.
 */
public abstract class DBaseParams<T extends DBaseParams> {


    public Request.Builder builder;

    public DBaseParams() {
        builder = new Request.Builder();
    }

    public Request.Builder getBuilder() {
        return builder;
    }


    public T setTag(Object tag) {
        builder.tag(tag);
        return returnThis();
    }

    public T setUrl(String url) {
        builder.url(url);
        return returnThis();
    }

    public Request.Builder setPost(RequestBody requestBody) {
        return builder.post(requestBody);
    }

    public Request build() {
        return builder.build();
    }

    public T addHeadr(String key, String value){
        builder.addHeader(key,value);
        return returnThis();
    }



    public abstract T returnThis();
}
