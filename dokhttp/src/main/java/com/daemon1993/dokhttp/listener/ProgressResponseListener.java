package com.daemon1993.dokhttp.listener;

/**
 * Created by h2h on 2015/9/8.
 */
public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
