package com.daemon1993.dokhttp;

import android.content.Context;
import android.os.Environment;
import android.util.Log;



import java.io.File;
import java.io.IOException;

import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * Created by Daemon1993 on 16/4/10.
 */
public class DFileUtil {

    private static final String TAG=DFileUtil.class.getName();
    public static String cachePath= "";


    /**
     * 数据存储判断
     * 数据区域或者缓存区域
     * SDCard/Android/data/你的应用的包名/files/ 目录
     * SDCard/Android/data/你的应用包名/cache/目录
     * @param context
     * @return
     */
    public static void getDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath() ;
        } else {
            cachePath = context.getCacheDir().getPath() ;
        }
    }

    /**
     * 自定义存储地址
     * @param path
     */
    public static void setCachePath(String path) {
        File file=new File(path);
        if(file==null || !file.exists()){
            file.mkdirs();
            cachePath=path;
        }else{
            throw new RuntimeException(DError.CachePathError);
        }
    }


    /**
     * 保存文件的文件名 加上后缀 比如 head.jpg
     * @param response
     * @param saveName
     */
    public static void saveFile(Response response, String saveName){

        Log.e(TAG,"cachePath is "+cachePath);

        try {
            BufferedSource bufferedSource = response.body().source();


            BufferedSink bufferedSink = Okio.buffer(Okio.sink(new File(cachePath+"/"+saveName)));
            bufferedSink.writeAll(bufferedSource);


            bufferedSink.close();
            bufferedSource.close();

        } catch (IOException e) {
            new RuntimeException(DError.CachePathError);
        }

    }
}
