package com.daemon1993.dokhttpdemo;

import android.app.Application;
import android.os.Environment;

import com.daemon1993.dokhttp.DOkHttp;

/**
 * Created by Daemon1993 on 16/4/10.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DOkHttp.init(getApplicationContext());


    }

}
