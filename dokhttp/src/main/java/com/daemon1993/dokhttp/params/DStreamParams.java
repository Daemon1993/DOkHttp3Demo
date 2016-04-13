package com.daemon1993.dokhttp.params;

import com.daemon1993.dokhttp.DConstant;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Daemon1993 on 16/4/12.
 */
public class DStreamParams extends DBaseParams<DStreamParams> {


    private  RequestBody requestBody;
    private BufferedSink sink;

    public DStreamParams(){

    }

    @Override
    public DStreamParams returnThis() {
        return this;
    }

    public void  setImageFile(final File file){
         requestBody = new RequestBody() {
            @Override public MediaType contentType() {
                return DConstant.MEDIA_TYPE_MARKDOWN;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                sink.writeAll(Okio.source(file));
            }
        };

        builder.post(requestBody);
    }

    public void setTextFile(final String str){
         requestBody = new RequestBody() {
            @Override public MediaType contentType() {
                return DConstant.MEDIA_TYPE_MARKDOWN;
            }

            @Override public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8(str);
            }
        };
        builder.post(requestBody);
    }




}
